
journalctl -xe 查看启动进程系统调用过程

ls -trl 查看文件弱链接

ln -s target source​​ 解释下： ​​ln -s​​：表示创建一个软连接； ​​target​​：表示目标文件（夹）【即被指向的文件（夹）】 ​​source​​：表示当前目录的软连接名。【源文件（夹）】 unlink 软链接名

netstat -naop|grep 23000 端口占用

查看开放端口
netstat -aptn

杀close_wait
netstat -nap |grep :18881|grep CLOSE_WAIT | awk '{print $7}'|awk -F"\/" '{print $1}' |awk '!a[$1]++'  |xargs kill



查看closewait
netstat -n | awk '/^tcp/ {++S[$NF]} END {for(a in S) print a, S[a]}'

统计997的子进程数量
ps -eLf|grep 31692|grep -v "grep"|wc -l

修改进程可打开最大文件数
echo "* soft nofile 1048576" >>/etc/security/limits.conf
echo "* hard nofile 1048576" >>/etc/security/limits.conf

删除90天前的文件
find /bmdata/software/dx_dity_record -mtime +90 -name '*.txt' |xargs rm -rf

find /bmdata/software/dx_dity_record -mtime +30 -name '*.txt' |xargs rm -rf
du -sh -b /bmdata/software/dx_dity_record/* | awk '{ if($1>1048576)print $2}'|xargs rm -rf

查大文件
find /bmdata/software/ -size +500M |xargs ls -lh



# 查脏数据作业语句
 select dcj.name from dc_collect_dirty_data dcdd join dc_collect_job dcj on dcj.id=dcdd.job_id and dirty_path = '/bmdata/software/dx_dity_record/1679280728059.txt';

# 模型oracle改大写

CREATE TABLE  dc_assets_data_model_bk20230322 SELECT * FROM dc_assets_data_model;
CREATE TABLE  dc_model_table_0415051711_bk20230322 SELECT * FROM dc_model_table_0415051711;
CREATE TABLE  dc_collect_job_bk20230322 SELECT * FROM dc_collect_job;

update dc_assets_data_model set table_name=UPPER(table_name) where data_source_type = "oracle";
update dc_model_table_0415051711 set table_name=UPPER(table_name);
update dc_collect_job set target_table_name=UPPER(target_table_name) where target_type=2;

### 先恢复source_type，再根据source_type修改表名大小写

update dc_collect_job set source_type=(select type from dc_collect_source_info where id=source_id);
update dc_collect_job set source_table_name=UPPER(source_table_name) where source_type=2;




# 根据脏数据文件名查询脏数据
SELECT
	dcj.`name` '作业名',
	sum( dcdd.dirty_data_num ) '脏数据总数' 
FROM
	dc_collect_dirty_data dcdd
	JOIN dc_collect_job dcj ON dcdd.job_id = dcj.id 
	AND dirty_name IN ( '1670229146930.txt' ) 
GROUP BY
	dcj.id;



# 查看当前目录文件夹占用大小

du -h --max-depth=1 ./





## curl



curl -X POST "http://127.0.0.1:18880/test/setCreateConfigOnly"  -H "Content-Type:application/x-www-form-urlencoded;charset=UTF-8" -d "value=true"


curl -F  file=@jobName.txt "http://wedatatest-cmhk-cmdp-dataasset-wedata-ocp-sit1.apps.hk-ocp1.sit.cmhk.com/oss/storage/upload?bucketName=kerkeros&objectName="

curl -o "http://wedatatest-cmhk-cmdp-dataasset-wedata-ocp-sit1.apps.hk-ocp1.sit.cmhk.com/oss/storage?bucketName=kerkeros&objectName="

curl -XGET -o jobName.txt "http://data-oss-service-svc:14100/storage?bucketName=test&objectName=jobName.txt"

curl -X POST  -F  file=@jobName.txt "http://127.0.0.1:18880/test/startDataxJob"  -H  "Content-Type:application/json"





curl "http://127.0.0.1:11888//unifyquery/datasources/logLevel?logLevel=debug&package=com.bmsoft.dc.advice.LogAspect"





{"name":"hdfs_test","note":null,"type":"FI_HDFS","host":"hdfs://hacluster","port":null,"database":null,"principal":"wedata_poc@HADOOP_DI.COM","userName":null,"password":null,"other":null,"ftpProtocol":null,"ftpMode":null,"ftpTLS":null,"keyTabPath":"e1d06509-c3f6-423f-8657-a0785c502eca.keytab","krb5Path":null,"jaasZkPath":null,"jaasZkFileName":null,"hetuServerPath":null,"hetuServerFileName":null,"extraParams":{"useNewKrb":"true","coreSiteFileName":"df140ea3-c2e1-410f-9f26-625a37e88f46.xml","haveKerberos":"true","hdfsSiteFileName":"11d51ba8-7cf3-4363-9852-950dc8f0943f.xml"}}







curl -X POST "http://127.0.0.1:11888/unifyquery/datasources/test" -H  "Content-Type:application/json" -d 





{"name":"fi_hdfs_zhjl","note":null,"type":"FI_HDFS","host":null,"port":null,"database":null,"principal":"wedata_poc@HADOOP_DI.COM","userName":null,"password":null,"other":null,"ftpProtocol":null,"ftpMode":null,"ftpTLS":null,"keyTabPath":"0e7021f6-0204-48f1-a089-bde91621ac7d.keytab","krb5Path":null,"jaasZkPath":null,"jaasZkFileName":null,"hetuServerPath":null,"hetuServerFileName":null,"extraParams":{"useNewKrb":"false","coreSiteFileName":"fdb4df93-cb49-4536-abbd-de5be86f0c27.xml","haveKerberos":"true","hdfsSiteFileName":"7ba3ed19-4cae-48bc-9eeb-cd5e03f504df.xml"}}









apache历史版本网站

http://archive.apache.org/dist/