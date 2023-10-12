

### ES字段类型
#### 核心数据类型

String类型：text、keywork
number类型：long,integer,short,byte,double,float,half_float,scaled_float
date类型：date
boolean类型：boolean
binary类型 ： binary
range类型：integer_range, float_range, long_range, double_range, date_range

#### 复杂数据类型

对象数据类型：object 用于单个JSON对象
嵌套数据类型：nested 用于JSON对象数组

#### 地理数据类型

地理位置数据类型：eo_point 纬度/经度
地理形状数据类型：geo_shape 适用于多边形等复杂形状



```http
_index=db
_type=表
_doc=记录
_id=文档唯一标识

#单条插入 PUT /{_index}/{_type}/{_id}
PUT /customer/_doc/1?pretty
{"name":"John Doe"}

#获取索引单条文档 GET /{_index}/{_type}/{_id}
GET /customer/_doc/1

#查询字段定义
GET /customer/_mapping

#简单批量插入(_index和_type都相同,_id也可去掉，自动生成) 
#PUT /{_index}/{_type}/_bulk
PUT /customer/_doc/_bulk 
{"index":{"_id":2}}
{"name": "张三"}
{"index":{"_id":3}}
{"name": "李四"}

#自定义批量插入
#POST /_bulk {"index":{"_index":"","_type":"","_id",""}} {data}
POST /_bulk
{ "index":{ "_index": "myIndex", "_type": "person" } }
{ "name":"john doe","age":25 }
{ "index":{ "_index": "myOtherIndex", "_type": "dog" } }
{ "name":"fido","breed":"chihuahua" }

#获取所有文档
GET /_search
GET /customer/_search
GET /customer/_doc/_search

DELETE /customer/_doc/F3_qbHMBTCgsfWai4hQO

GET /customer/_search
{
  "query": { "match_all": {} },
  "sort": [
    { "name.keyword": "asc" }
  ]
}

GET /customer/_search
{
  "query": { "match_phrase": { "name": "John" } }
}
```



```
#hibana创建索引并插入
PUT /test_date
{
  "settings":{
      "number_of_shards":2,
      "number_of_replicas":2
  },
  "mappings":{
    "properties":{
      "id":{
        "type":"long",
        "index": "true"
      },
      "name":{
        "type":"text",
        "index": "true"
      },
      "create_time": {
        "type": "date",
        "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis",
        "index": "true"
      }
    }
  }
}

DELETE test_date

GET test_date

POST /test_date/_doc?pretty
{
  "name":"张三",
  "create_time":"2020-07-22"
}

GET test_date/_search



##用mappings创建索引语句
curl -X PUT localhost:9400/dm_dj_two_center?pretty -H 'Content-Type: application/json' -d

 '{"mappings":{"_doc":{"properties":{"id":{"type":"long","index":"true"},"code":{"type":"text","index":"true"},"name":{"type":"text","index":"true","analyzer":"ik_max_word","copy_to":"full_data"},"longitude":{"type":"text","index":"true"},"latitude":{"type":"text","index":"true"},"address":{"type":"text","index":"true","analyzer":"ik_max_word","copy_to":"full_data"},"type":{"type":"text","index":"true"},"president":{"type":"text","index":"true"},"remark":{"type":"text","index":"true"},"imgs_url":{"type":"text","index":"true"},"videos_url":{"type":"text","index":"true"},"location_type":{"type":"text","index":"true"},"location_es":{"type":"geo_point","index":"true"},"data_type":{"type":"keyword","index":"true"},"module":{"type":"keyword","index":"true"},"full_data":{"type":"text","index":"true"}}}}}'

{"mappings":{"_doc":{"properties":{"id":{"type":"long","index":"true"},"name":{"type":"text","index":"true","analyzer":"ik_max_word","copy_to":"full_data"},"longitude":{"type":"text","index":"true"},"latitude":{"type":"text","index":"true"},"location":{"type":"text","index":"true"},"level":{"type":"text","index":"true"},"rank":{"type":"integer","index":"true"},"approve":{"type":"text","index":"true"},"integerroduction":{"type":"text","index":"true"},"phone":{"type":"text","index":"true"},"provide_service":{"type":"text","index":"true"},"img_url":{"type":"text","index":"true"},"terminal_id":{"type":"text","index":"true"},"location_type":{"type":"text","index":"true"},"fusion_phone":{"type":"text","index":"true"},"location_es":{"type":"geo_point","index":"true"},"data_type":{"type":"keyword","index":"true"},"module":{"type":"keyword","index":"true"},"full_data":{"type":"text","index":"true"}}}}}
```





```
根据索引删数据
curl -XPOST localhost:9400/dwd_yj_safe_company/_doc/_delete_by_query?pretty -H 'Content-Type: application/json' -d '
{
    "query": {
        "match_all": {
        }	
    }
}'
```







```
创建Pipeline
PUT _ingest/pipeline/test_pipeline
{
  "description" : "这是测试的管道内容",
  "processors" : [
    {
      "set" : {
        "field": "name",
        "value": "这是管道默认数据"
      }
    }
  ]
}


# 获取管道信息
GET _ingest/pipeline/test_pipeline


```





curl -X PUT -u'sxdi:sxdi@2021' http://100.76.74.145:9200/test_1?pretty -H 'Content-Type: application/json' -d '{"mappings":{"properties":{"id":{"type":"long","index":"true"},"code":{"type":"text","index":"true"}}}}'





curl -X POST  -u 'sxdi:sxdi@2021' http://100.76.74.145:9200/test_1/_doc?pretty -H 'Content-Type: application/json' -d '{"id":"1","code":"one"}'



curl -X GET -u'sxdi:sxdi@2021' http://100.76.74.145:9200/test_1?pretty



curl -X GET -u'sxdi:sxdi@2021' http://100.76.74.145:9200/test_1/_search

