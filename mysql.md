### innodb单表访问类型

const：通过主键列或唯一二级索引定位唯一记录。（唯一索引回表）。

ref：通过二级索引与常量比较，得出范围聚簇索引值对应对应记录。（具体值二级索引+回表）

ref_or_null: 在ref的基础上加上null值二级索引记录。（具体值或null二级索引+回表）。

range: 对二级索引列范围查询。（范围二级索引+回表）。

index: 不符合最左匹配，但选择所有列在可通过二级索引查出。（类似索引下推）。

all: 全表扫描



### 成本优化

多索引表会优先使用查询成本较低的索引进行查询。

#### 1）计算查询成本

查询成本 = I/O成本+CPU成本

成本常数

- I/O成本常数：读取一个页面花费的成本默认为`1.0`
- CPU成本常数：读取以及检测一条记录是否符合搜索条件的成本默认为`0.2`



I/O成本 = `聚簇索引页数量 * I/O成本常数 + 1.1 `

CPU成本 = `统计数据中表的记录数(Rows) * CPU成本常数 + 1.0`



成本计算示例

```
mysql> SHOW TABLE STATUS LIKE 'single_table'\G
*************************** 1. row ***************************
           Name: single_table
         Engine: InnoDB
        Version: 10
     Row_format: Dynamic
           Rows: 9693
 Avg_row_length: 163
    Data_length: 1589248
Max_data_length: 0
   Index_length: 2752512
      Data_free: 4194304
 Auto_increment: 10001
    Create_time: 2018-12-10 13:37:23
    Update_time: 2018-12-10 13:38:03
     Check_time: NULL
      Collation: utf8_general_ci
       Checksum: NULL
 Create_options:
        Comment:
1 row in set (0.01 sec)
```

Data_length = 聚簇索引的页数量 x 每个页面的大小

(1589248 ÷ 16 ÷ 1024) + (9693 x 0.2 + 1.0) = 98.1 + 1939.6 = 2037.7



#### 2）基于索引统计数据的成本计算

`in`语句会把所有值当成单点区间，在成本计算时需要经过`index dive`的方式去计算。

index dive：通过获取区间最左记录和区间最右记录，计算两记录之间有多少记录（记录过多时通过计算b+树父级节点数）。



`SHOW INDEX FROM single_table;`

| 属性名          | 描述                                                         |
| --------------- | ------------------------------------------------------------ |
| `Table`         | 索引所属表的名称。                                           |
| `Non_unique`    | 索引列的值是否是唯一的，聚簇索引和唯一二级索引的该列值为`0`，普通二级索引该列值为`1`。 |
| `Key_name`      | 索引的名称。                                                 |
| `Seq_in_index`  | 索引列在索引中的位置，从1开始计数。比如对于联合索引`idx_key_part`，来说，`key_part1`、`key_part2`和`key_part3`对应的位置分别是1、2、3。 |
| `Column_name`   | 索引列的名称。                                               |
| `Collation`     | 索引列中的值是按照何种排序方式存放的，值为`A`时代表升序存放，为`NULL`时代表降序存放。 |
| `Cardinality`   | 索引列中不重复值的数量。后边我们会重点看这个属性的。         |
| `Sub_part`      | 对于存储字符串或者字节串的列来说，有时候我们只想对这些串的前`n`个字符或字节建立索引，这个属性表示的就是那个`n`值。如果对完整的列建立索引的话，该属性的值就是`NULL`。 |
| `Packed`        | 索引列如何被压缩，`NULL`值表示未被压缩。这个属性我们暂时不了解，可以先忽略掉。 |
| `Null`          | 该索引列是否允许存储`NULL`值。                               |
| `Index_type`    | 使用索引的类型，我们最常见的就是`BTREE`，其实也就是`B+`树索引。 |
| `Comment`       | 索引列注释信息。                                             |
| `Index_comment` | 索引注释信息。                                               |

`in`中单点区间过多，超过`eq_range_index_dive_limit`的值时，会使用`基于索引统一数据的成本计算`来计算`in`的成本，若`in`中有2000个参数，估算需要回表的记录数为

`Rows ÷ Cardinality * 20000`（参数函数看表show index）





#### 3）连接查询成本优化

扇出值(fanout)：对驱动表进行查询后得到的记录条数。

主键或索引条件能精准计算出`扇出值`的大小，其他条件需要通过`Condition filtering`来计算大概`扇出值`

连接查询成本 = `单次查询驱动表成本` + `扇出值` x `单次查询被驱动表成本`



优化方向

- 尽量减少驱动表的扇出
- 对被驱动表的访问成本尽量低

尽量在被驱动表的连接列上建立索引。使用`ref`访问方法降低成本。



多表连接成本分析中，可能会存在 n！种顺序的连接方式，计算成本方法：

- 提前结束某个顺序成本评估。

  全局变量来存储当前每种顺序的最小连接查询成本，若当前顺序成本已超过最小成本，则中止该顺序成本分析。

- 系统变量`optimizer_search_depth`控制连接表数量。

  连接表小于该变量时，继续使用穷举法计算所有顺序的连接查询成本，大于该值时，只计算到该值的表连接成本。（若该值是2，有4张表需要连接，则只计算4张表中任意2张表任意顺序的成本）



#### 4）调整成本常数

server层成本常数

```
mysql> SELECT * FROM mysql.server_cost;
+------------------------------+------------+---------------------+---------+
| cost_name                    | cost_value | last_update         | comment |
+------------------------------+------------+---------------------+---------+
| disk_temptable_create_cost   |       NULL | 2018-01-20 12:03:21 | NULL    |
| disk_temptable_row_cost      |       NULL | 2018-01-20 12:03:21 | NULL    |
| key_compare_cost             |       NULL | 2018-01-20 12:03:21 | NULL    |
| memory_temptable_create_cost |       NULL | 2018-01-20 12:03:21 | NULL    |
| memory_temptable_row_cost    |       NULL | 2018-01-20 12:03:21 | NULL    |
| row_evaluate_cost            |       NULL | 2018-01-20 12:03:21 | NULL    |
+------------------------------+------------+---------------------+---------+
6 rows in set (0.05 sec)
```



从`server_cost`中的内容可以看出来，目前在`server`层的一些操作对应的`成本常数`有以下几种：

| 成本常数名称                   | 默认值 | 描述                                                         |
| ------------------------------ | ------ | ------------------------------------------------------------ |
| `disk_temptable_create_cost`   | `40.0` | 创建基于磁盘的临时表的成本，如果增大这个值的话会让优化器尽量少的创建基于磁盘的临时表。 |
| `disk_temptable_row_cost`      | `1.0`  | 向基于磁盘的临时表写入或读取一条记录的成本，如果增大这个值的话会让优化器尽量少的创建基于磁盘的临时表。 |
| `key_compare_cost`             | `0.1`  | 两条记录做比较操作的成本，多用在排序操作上，如果增大这个值的话会提升`filesort`的成本，让优化器可能更倾向于使用索引完成排序而不是`filesort`。 |
| `memory_temptable_create_cost` | `2.0`  | 创建基于内存的临时表的成本，如果增大这个值的话会让优化器尽量少的创建基于内存的临时表。 |
| `memory_temptable_row_cost`    | `0.2`  | 向基于内存的临时表写入或读取一条记录的成本，如果增大这个值的话会让优化器尽量少的创建基于内存的临时表。 |
| `row_evaluate_cost`            | `0.2`  | 这个就是我们之前一直使用的检测一条记录是否符合搜索条件的成本，增大这个值可能让优化器更倾向于使用索引而不是直接全表扫描。 |



engine层成本常数

```
mysql> SELECT * FROM mysql.engine_cost;
+-------------+-------------+------------------------+------------+---------------------+---------+
| engine_name | device_type | cost_name              | cost_value | last_update         | comment |
+-------------+-------------+------------------------+------------+---------------------+---------+
| default     |           0 | io_block_read_cost     |       NULL | 2018-01-20 12:03:21 | NULL    |
| default     |           0 | memory_block_read_cost |       NULL | 2018-01-20 12:03:21 | NULL    |
+-------------+-------------+------------------------+------------+---------------------+---------+
2 rows in set (0.05 sec)
```



| 成本常数名称             | 默认值 | 描述                                                         |
| ------------------------ | ------ | ------------------------------------------------------------ |
| `io_block_read_cost`     | `1.0`  | 从磁盘上读取一个块对应的成本。请注意我使用的是`块`，而不是`页`这个词儿。对于`InnoDB`存储引擎来说，一个`页`就是一个块，不过对于`MyISAM`存储引擎来说，默认是以`4096`字节作为一个块的。增大这个值会加重`I/O`成本，可能让优化器更倾向于选择使用索引执行查询而不是执行全表扫描。 |
| `memory_block_read_cost` | `1.0`  | 与上一个参数类似，只不过衡量的是从内存中读取一个块对应的成本。 |



修改成本常数步骤：

- 使用update更新需要更新的成本常数表

  ```sql
  UPDATE mysql.server_cost 
      SET cost_value = 0.4
      WHERE cost_name = 'row_evaluate_cost';
  ```

- 重新加载 `FLUSH OPTIMIZER_COSTS;`





### explain

```sql
CREATE TABLE single_table (
    id INT NOT NULL AUTO_INCREMENT,
    key1 VARCHAR(100),
    key2 INT,
    key3 VARCHAR(100),
    key_part1 VARCHAR(100),
    key_part2 VARCHAR(100),
    key_part3 VARCHAR(100),
    common_field VARCHAR(100),
    PRIMARY KEY (id),
    KEY idx_key1 (key1),
    UNIQUE KEY idx_key2 (key2),
    KEY idx_key3 (key3),
    KEY idx_key_part(key_part1, key_part2, key_part3)
) Engine=InnoDB CHARSET=utf8;
```



| 列名            | 描述                                                       |
| --------------- | ---------------------------------------------------------- |
| `id`            | 在一个大的查询语句中每个`SELECT`关键字都对应一个唯一的`id` |
| `select_type`   | `SELECT`关键字对应的那个查询的类型                         |
| `table`         | 表名                                                       |
| `partitions`    | 匹配的分区信息                                             |
| `type`          | 针对单表的访问方法                                         |
| `possible_keys` | 可能用到的索引                                             |
| `key`           | 实际上使用的索引                                           |
| `key_len`       | 实际使用到的索引长度                                       |
| `ref`           | 当使用索引列等值查询时，与索引列进行等值匹配的对象信息     |
| `rows`          | 预估的需要读取的记录条数                                   |
| `filtered`      | 某个表经过搜索条件过滤后剩余记录条数的百分比               |
| `Extra`         | 一些额外的信息                                             |



#### select_type

| 名称                   | 描述                                                         |
| ---------------------- | ------------------------------------------------------------ |
| `SIMPLE`               | 查询语句中不包含`UNION`或者子查询的查询都算作是`SIMPLE`类型，比方说下边这个单表查询的`select_type`的值就是`SIMPLE` |
| `PRIMARY`              | 对于包含`UNION`、`UNION ALL`或者子查询的大查询来说，它是由几个小查询组成的，其中最左边的那个查询的`select_type`值就是`PRIMARY` |
| `UNION`                | 对于包含`UNION`或者`UNION ALL`的大查询来说，它是由几个小查询组成的，其中除了最左边的那个小查询以外，其余的小查询的`select_type`值就是`UNION` |
| `UNION RESULT`         | MySQL`选择使用临时表来完成`UNION`查询的去重工作，针对该临时表的查询的`select_type`就是`UNION RESULT |
| `SUBQUERY`             | 如果包含子查询的查询语句不能够转为对应的`semi-join`的形式，并且该子查询是不相关子查询，并且查询优化器决定采用将该子查询物化的方案来执行该子查询时，该子查询的第一个`SELECT`关键字代表的那个查询的`select_type`就是`SUBQUERY` |
| `DEPENDENT SUBQUERY`   | 如果包含子查询的查询语句不能够转为对应的`semi-join`的形式，并且该子查询是相关子查询，则该子查询的第一个`SELECT`关键字代表的那个查询的`select_type`就是`DEPENDENT SUBQUERY` |
| `DEPENDENT UNION`      | 在包含`UNION`或者`UNION ALL`的大查询中，如果各个小查询都依赖于外层查询的话，那除了最左边的那个小查询之外，其余的小查询的`select_type`的值就是`DEPENDENT UNION` |
| `DERIVED`              | 对于采用物化的方式执行的包含派生表的查询，该派生表对应的子查询的`select_type`就是`DERIVED` |
| `MATERIALIZED`         | 当查询优化器在执行包含子查询的语句时，选择将子查询物化之后与外层查询进行连接查询时，该子查询对应的`select_type`属性就是`MATERIALIZED` |
| `UNCACHEABLE SUBQUERY` | A subquery for which the result cannot be cached and must be re-evaluated for each row of the outer query |
| `UNCACHEABLE UNION`    | The second or later select in a UNION that belongs to an uncacheable subquery (see UNCACHEABLE SUBQUERY) |



#### type（访问类型）

| 名称            | 描述                                                         |
| --------------- | ------------------------------------------------------------ |
| system          | 当表中只有一条记录并且该表使用的存储引擎的统计数据是精确的，比如MyISAM、Memory，那么对该表的访问方法就是`system`。 |
| const           |                                                              |
| eq_ref          | 在连接查询时，如果被驱动表是通过主键或者唯一二级索引列等值匹配的方式进行访问的（如果该主键或者唯一二级索引是联合索引的话，所有的索引列都必须进行等值比较），则对该被驱动表的访问方法就是`eq_ref` |
| ref             |                                                              |
| fulltext        |                                                              |
| ref_or_null     |                                                              |
| index_merge     |                                                              |
| unique_subquery | 类似于两表连接中被驱动表的`eq_ref`访问方法，`unique_subquery`是针对在一些包含`IN`子查询的查询语句中，如果查询优化器决定将`IN`子查询转换为`EXISTS`子查询，而且子查询可以使用到主键进行等值匹配的话，那么该子查询执行计划的`type`列的值就是`unique_subquery` |
| index_subquery  | `index_subquery`与`unique_subquery`类似，只不过访问子查询中的表时使用的是普通的索引 |
| range           |                                                              |
| idnex           |                                                              |
| all             |                                                              |



#### possible_keys和key

在`EXPLAIN`语句输出的执行计划中，`possible_keys`列表示在某个查询语句中，对某个表执行单表查询时可能用到的索引有哪些，`key`列表示实际用到的索引有哪些。