以下是 HBase 2.x 中 HBase Shell 常用的命令：

1. 创建表：

   ```
   create 'table_name', 'column_family'
   ```

   - 示例：`create 'user', 'info'`

2. 查看所有表：`list`

3. 查看表结构：

   ```
   describe 'table_name'
   ```

   - 示例：`describe 'user'`

4. 向表中插入数据：

   ```
   put 'table_name', 'row_key', 'column_family:column', 'value'
   ```

   - 示例：`put 'user', '1', 'info:name', '张三'`

5. 获取表中的数据：

   ```
   get 'table_name', 'row_key'
   ```

   - 示例：`get 'user', '1'`

6. 扫描表中的数据：

   ```
   scan 'table_name'
   ```

   - 示例：`scan 'user'`

7. 删除表中的数据：

   ```
   delete 'table_name', 'row_key', 'column_family:column'
   ```

   - 示例：`delete 'user', '1', 'info:name'`

8. 删除表：

   ```
   disable 'table_name'
   ```

    

   和

    

   ```
   drop 'table_name'
   ```

   - 示例：`disable 'user'` 和 `drop 'user'`

9. 退出 HBase Shell：`exit`

其中，`column_family` 表示列族，可以指定多个列族，用逗号隔开。`column` 表示列名，可以是列族名加上冒号再加上列名。例如，`info:name` 表示 `info` 列族下的 `name` 列。`row_key` 表示行键，必须是字符串类型。