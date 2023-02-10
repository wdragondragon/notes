

[frp](https://frp.tyu.wiki/)

[nacos](https://nacos.tyu.wiki/nacos/) 

[gitlab](https://gitlab.tyu.wiki/users/sign_in) 

[jenkins](https://jenkins.tyu.wiki/login?from=%2F) 

[rabbitmq](https://rabbitmqui.tyu.wiki/) 



## 开机

```shell
#开机docker命令
#1.博客
#docker run --restart=always -it -d --name halo-dev -p 8090:8090  -v ~/.halo:/root/.halo ruibaby/halo
docker run --restart=always --rm -it -d --name halo-dev -p 8090:8090  -v /var/data/halo:/root/.halo ruibaby/halo:1.4.0

#2.mysql
docker run --restart=always -d -v /var/data/mysql:/var/lib/mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=951753 --network mysql-network --name mysql mysql:8.0.21

docker run --restart=always -d -v /var/data/mysql57:/var/lib/mysql -p 3305:3306 -e MYSQL_ROOT_PASSWORD=951753 --network mysql-network --name mysql57 mysql:5.7

#3.tomcat
docker run --rm -it -d -v /var/data/tomcat/:/usr/local/tomcat/webapps/ -p 8080:8080 --network mysql-network --name tomcat tomcat:jdk8
#4.nocas单机/mysql集群
docker-compose -f /usr/local/src/nacos-docker/example/standalone-derby.yaml up -d

docker-compose -f /usr/local/src/nacos-docker/example/cluster-hostname.yaml up -d
#5.rabbitmq
docker run --restart=always -d -p 5672:5672 -p 15672:15672 --name myrabbitmq rabbitmq:3.8.3-management
#6.redis
docker run --restart=always -d -p 9736:6379 --name myredis -v /var/data/redis:/data redis
#7.jenkins
docker run \
--restart=always -itd --tty \
-u root \
-p 18080:8080 \
-v /var/data/jenkins/jenkins_home:/var/jenkins_home \
-v /var/run/docker.sock:/var/run/docker.sock \
-v /usr/bin/docker:/usr/bin/docker \
-v /usr/lib64/libltdl.so.7:/lib64/libltdl.so.7 \
-v /var/data/jenkins/home:/home \
-v /root/.ssh:/root/.ssh \
--name jenkins-tutorials \
jenkinsci/blueocean
#8.gitlab
docker run \
--restart=always -d \
--hostname gitlab.tyu.wiki \
--publish 1443:443 --publish 8880:80 --publish 2222:22 \
--name gitlab \
-v /var/data/gitlab/config:/etc/gitlab \
-v /var/data/gitlab/logs:/var/log/gitlab \
-v /var/data/gitlab/data:/var/opt/gitlab \
gitlab/gitlab-ce:latest

#zipkin
docker run -d -p 9411:9411 openzipkin/zipkin


#9.tlj
cd /var/java/tlj;./start.sh
docker run \
--restart=always -d \
-p 8081:8081 \
-v /var/java/tlj/tlj-0.0.1-SNAPSHOT.jar:/tlj-0.0.1-SNAPSHOT.jar \
-v /var/java/tlj/编码文件:/编码文件 \
-v /var/data/UEditor:/UEditor \
-v /var/data/MEditor:/MEditor \
-v /var/java/tlj/download:/download \
--network mysql-network \
--name tlj-app \
wdragondragon/java8 \
java -jar tlj-0.0.1-SNAPSHOT.jar

#10.robot
cd /var/java/robot;./start.sh
##
#start.sh
##
docker stop robot-app
docker rm robot-app
docker rmi robot-images
docker build -t robot-images .
docker run \
--rm -it --tty \
-v $(pwd)/plugins:/plugins \
-v /var/data/robot/image/:/root/coolq/data/image/ \
--network public --name robot-app robot-images
##
#Dockerfile
##
FROM classmethod/openjdk-alpine-git
MAINTAINER jdragon
ENV LANG C.UTF-8
ENV TZ=Asia/Shanghai
ENV LC_ALL C.UTF-8
COPY cqhttp-mirai-0.2.3-embedded-all.jar cqhttp-mirai-0.2.3-embedded-all.jar
CMD java -jar /cqhttp-mirai-0.2.3-embedded-all.jar --account 1061917110 --password aa456+++

#11.robot-tlj程序
cd /var/java/robot-tlj;./start.sh
##start.sh
docker stop robot-tlj
docker rmi robot-tlj
docker build -t robot-tlj .
docker run --rm -d \
-v /root/coolq/data/image/typinggroup:/root/coolq/data/image/typinggroup \
-v /var/java/robot-tlj/编码文件:/robot/编码文件 \
--network public \
--name robot-tlj \
robot-tlj

##Dockerfile
FROM classmethod/openjdk-alpine-git
ENV TZ=Asia/Shanghai
RUN echo "http://mirrors.aliyun.com/alpine/v3.6/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.6/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache procps unzip curl bash tzdata \
    && apk add ttf-dejavu \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone
COPY robot.jar /robot/robot.jar
COPY ./fonts/* /usr/lib/jvm/java-1.8-openjdk/jre/lib/fonts/fallback/
workdir /robot
ENTRYPOINT java -jar robot.jar


#12.seata
docker run -d --restart=always --name seata-server \
-p 8091:8091 \
-e SEATA_IP=39.96.83.89 \
-e SEATA_CONFIG_NAME=file:/root/seata-config/registry \
-v /usr/local/src/seata1.3.0/conf:/root/seata-config  \
seataio/seata-server:1.3.0

#13.nexus
docker run --restart=always -d \
-p 12345:8081 \
--name nexus \
-v /var/data/nexus-data:/nexus-data \
sonatype/nexus3:3.28.0

#14.禅道
docker run -d --restart=always -p 13697:80 -p 3316:3306 \
-e ADMINER_USER="admin" -e ADMINER_PASSWD="123456" \
-e BIND_ADDRESS="false" \
-v /var/data/zbox/:/opt/zbox/ \
--add-host smtp.exmail.qq.com:163.177.90.125 \
--name zentao-server \
idoop/zentao:latest

#15.showdoc
docker run -d  --restart=always -p 4999:80 \
-v /var/data/showdoc/html:/var/www/html/ \
--name showdoc --user=root --privileged=true \
star7th/showdoc


#16.seafile
docker run -d --restart=always --name seafile \
-e SEAFILE_SERVER_HOSTNAME=seafile.tyu.wiki \
-e SEAFILE_ADMIN_EMAIL=jdragon \
-e SEAFILE_ADMIN_PASSWORD=zhjl951753 \
-v /var/data/seafile:/shared \
-p 9090:80 \
seafileltd/seafile:latest 
```



```dockerfile
#进入容器
docker exec -it 277b /bin/bash
```



## 昊方

```dockerfile
docker pull mysql:8.0.21
docker pull tomcat:jdk8
docker pull classmethod/openjdk-alpine-git

#昊方docker-mysql
docker run --rm -d -v /var/docker-mysql:/var/lib/mysql -p 13307:3306 -e MYSQL_ROOT_PASSWORD=951753 --network haofang-server --name haofang-mysql mysql:8.0.21

#昊方前端docker
docker run -it --rm -v /var/haofangproject/haofangUI/:/usr/local/tomcat/webapps/ROOT -p 8080:8080 --name haofang-UI tomcat:jdk8

#昊方后端docker
docker build -t haofang-service-image
docker run -d -it --restart=always -p 8082:8082 -v /var/haofangproject/haofangServer/log:/log --network haofang-server --name haofang-service-app haofang-service-image


#通用mysql
docker run --rm -d -v /var/data/mysql:/var/lib/mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=951753 --name mysql mysql:8.0.21

docker run --rm -d -v /var/data/mysql57:/var/lib/mysql -p 3305:3306 -e MYSQL_ROOT_PASSWORD=951753 --name mysql57 mysql:5.7

#通用tomcat
docker run --rm -it -d -v /var/data/tomcat/:/usr/local/tomcat/webapps/ROOT -p 8080:8080 --name tomcat tomcat:jdk8


mysql -h127.0.0.1 -P13307 -paAswabc.gzEA951753 -uroot

mysql -h cdb-dw3vdsr1.gz.tencentcdb.com -u root -P 10156 -p 
mysql -h cdb-dw3vdsr1.gz.tencentcdb.com -u hfdba -P 10156 -p 

root:ImTuJWItb2H9zM02Mp4K
hfdba:aAswabc.gzEA951753

create user 'hfdba'@'%' IDENTIFIED BY 'aAswabc.gzEA951753';
GRANT ALL ON haofangerp.* TO 'hfdba'@'%';

GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'aAswabc.gzEA951753' WITH GRANT OPTION; #授权任意地方可登录root用户 并权限具有传递性

FLUSH PRIVILEGES

select host,user,plugin,authentication_string from user;
```



## Redis Rabbit

```dockerfile
#5.rabbitmq
docker run --restart=always -d -p 5672:5672 -p 15672:15672 --name myrabbitmq rabbitmq:3.8.3-management
username:guest
password:guest

#rabbitmq页面查看queue exchange route页面报错
#the object you clicked on was not found it may have been deleted on the server
https://blog.csdn.net/luxiaoruo/article/details/106637281

#6.redis
docker run --restart=always -d -p 9736:6379 --name myredis -v /var/data/redis:/data redis
```



## Halo

```dockerfile
#halo
docker run --rm -it -d --name halo-dev -p 8090:8090  -v /var/data/halo:/root/.halo ruibaby/halo
```



## docker

```dockerfile
#docker可视化
docker run -d -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer

docker run -d -it -p 8081:80 linode/lamp bash
```



## Jenkins

```dockerfile
#jenkins
docker pull jenkins/jenkins:lts
#容器启动
docker run \
--restart=always -itd --tty \
-u root \
-e JAVA_OPTS=-Duser.timezone=Asia/Shanghai \
-p 18080:8080 \
-v /var/data/jenkins:/var/jenkins_home \
-v /var/run/docker.sock:/var/run/docker.sock \
-v /usr/bin/docker:/usr/bin/docker \
-v /usr/lib64/libltdl.so.7:/lib64/libltdl.so.7 \
-v /root/.ssh:/root/.ssh \
-v /etc/localtime:/etc/localtime \
-v /var/data/jenkins/.gitconfig:/root/.gitconfig \
--name jenkins \
jenkins/jenkins:lts

换
https://mirrors.tuna.tsinghua.edu.cn/jenkins/updates/update-center.json
#进入
docker exec -it jenkins bash

#时区设置 进入系统管理->脚本命令行 
System.setProperty('org.apache.commons.jelly.tags.fmt.timeZone', 'Asia/Shanghai')

#上传远程与远程启动
scp /var/jenkins_home/workspace/haofang-server/haofang-server/haofang-service/target/*.{jar,sh} root@175.24.121.41:/home

scp /var/jenkins_home/workspace/haofang-server/haofang-server/haofang-service/target/Dockerfile root@175.24.121.41:/home

ssh root@175.24.121.41 "cd /home;chmod +x build.sh;./build.sh"
```



## Gitlab

```dockerfile
#gitlab
docker pull gitlab/gitlab-ce:latest
docker run \
--restart=always -d \
--hostname gitlab.tyu.wiki \
--publish 1443:443 --publish 8880:80 --publish 2222:22 \
--name gitlab \
-v /var/data/gitlab/config:/etc/gitlab \
-v /var/data/gitlab/logs:/var/log/gitlab \
-v /var/data/gitlab/data:/var/opt/gitlab \
gitlab/gitlab-ce:latest
```



## Nacos

```shell
#到nacos目录下启动
#单机模式
docker-compose -f example/standalone-derby.yaml up
#单机mysql模式
docker-compose -f example/standalone-mysql.yaml up
#集群模式
docker-compose -f example/cluster-hostname.yaml up 
```



## Seata

```dockerfile
#seata
docker run -d --rm --name seata-server \
        -p 8091:8091 \
        -e SEATA_IP=39.96.83.89 \
        -e SEATA_CONFIG_NAME=file:/root/seata-config/registry \
        -v /usr/local/src/seata1.3.0/conf:/root/seata-config  \
        seataio/seata-server:1.3.0
```



## Nexus

```dockerfile
#nexus
##需要先改变挂载目录的权限
chown -R 200 /var/data/nexus-data

docker run --restart=always -d \
-p 12345:8081 \
--name nexus \
-v /var/data/nexus-data:/nexus-data \
sonatype/nexus3:3.28.0
```





```dockerfile
#docker in docker
docker run -it \
-v /var/run/docker.sock:/var/run/docker.sock \
-v /usr/bin/docker:/usr/bin/docker \
-v /lib64/libltdl.so.7:/lib64/libltdl.so.7 \
```



```dockerfile
#docker-compose安装
sudo curl -L "https://github.com/docker/compose/releases/download/1.25.5/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
```

## seafile

```dockerfile
docker pull seafileltd/seafile 
docker run -d --restart=always --name seafile \
-e SEAFILE_SERVER_HOSTNAME=seafile.tyu.wiki \
-e SEAFILE_ADMIN_EMAIL=jdragon \
-e SEAFILE_ADMIN_PASSWORD=zhjl951753 \
-v /var/data/seafile:/shared \
-p 9090:80 \
seafileltd/seafile:latest

/opt/seafile/seafile-data
##需要修改为https
/var/data/seafile/conf/seahub_settings.py 将FILE_SERVER_ROOT 修改为https
```



## oracle

```dockerfile
docker run -it -d -p 1521:1521 -v /var/data/oracle:/data/oracle --name oracle11 registry.cn-hangzhou.aliyuncs.com/helowin/oracle_11g


sqlplus /nolog

发现没有该命令，所以切换root用户。

su root 

输入密码：helowin

编辑profile文件配置ORACLE环境变量

打开：vi /etc/profile ，在文件最后写上下面内容：
export ORACLE_HOME=/home/oracle/app/oracle/product/11.2.0/dbhome_2
export ORACLE_SID=helowin
export PATH=$ORACLE_HOME/bin:$PATH
source /etc/profile
创建软连接 ln -s $ORACLE_HOME/bin/sqlplus /usr/bin
su - oracle

登录sqlplus并修改sys、system用户密码

sqlplus /nolog   --登录
conn /as sysdba

alter user system identified by system;
alter user sys identified by system;
create user test identified by test;
grant connect,resource,dba to test;
ALTER PROFILE DEFAULT LIMIT PASSWORD_LIFE_TIME UNLIMITED;
alter system set processes=1000 scope=spfile;
conn /as sysdba
shutdown immediate; --关闭数据库
startup; --启动数据库
exit



39.96.83.89:1521
服务名：helowin
test:test
```



## sqlserver

```dockerfile
sudo docker pull mcr.microsoft.com/mssql/server:2019-latest
#启动
docker run -e 'ACCEPT_EULA=Y' -e 'MSSQL_SA_PASSWORD=Zhjl.sqlserver' \
--name sqlserver -h sqlserver \
-p 1433:1433 \
-v sqlvolume:/var/opt/mssql \
-d mcr.microsoft.com/mssql/server:2019-latest

#查看数据卷
docker volume ls

#密码修改
sudo docker exec -it sql1 /opt/mssql-tools/bin/sqlcmd \
   -S localhost -U SA -P "旧密码" \
   -Q 'ALTER LOGIN SA WITH PASSWORD="新密码"'
   
   
39.96.83.89:1433 
SA Zhjl.sqlserver
```



## 达梦

```dockerfile
wget -O dm8_docker.tar -c http://download.dameng.com/eco/dm8/dm8_docker.tar
docker import dm8_docker.tar dm8:v01


docker run -d  -p 31880:8080 -p 30236:5236 --name dm \
-e LD_LIBRARY_PATH=/opt/dmdbms/bin  \
-e INSTANCE_NAME=testdb \
-v /var/data/dm8/data:/opt/dmdbms/data \
-v /var/data/dm8/dm8.key:/opt/dmdbms/bin/dm.key \
dm8:v01 /bin/bash /startDm.sh

testdb
SYSDBA：SYSDBA
```



## pgsql

```dockerfile
docker pull postgres:10.16

docker run -d \
--name postgres \
-p 15432:5432 \
-e POSTGRES_USER=jdragon \
-e POSTGRES_PASSWORD=Zhjl.postgres \
-e PGDATA=/var/lib/postgresql/data/pgdata \
-v /var/data/pg:/var/lib/postgresql/data \
postgres:10.16

jdragon：Zhjl.postgres
```





## es

```dockerfile
#es7
docker pull elasticsearch:7.12.0
docker pull kibana:7.12.0

docker network create es_default

#无映射卷启动
docker run --name es -p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms512m -Xmx512m" \
-d elasticsearch:7.12.0

docker cp es:/usr/share/elasticsearch/config /var/data/es/
docker cp es:/usr/share/elasticsearch/data /var/data/es/
docker cp es:/usr/share/elasticsearch/plugins /var/data/es/
docker stop es
docker rm es

#映射卷启动
docker run --name es --net es_default -p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms512m -Xmx512m" \
-v /var/data/es/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /var/data/es/data:/usr/share/elasticsearch/data \
-v /var/data/es/plugins:/usr/share/elasticsearch/plugins \
-d elasticsearch:7.12.0


docker run -d --name kibana --net es_default \
-e ELASTICSEARCH_HOSTS=http://es:9200 \
-p 5601:5601 kibana:7.12.0




# es6
docker pull elasticsearch:6.8.23
docker pull kibana:6.8.23

docker network create es_default
#无映射卷启动
docker run --name es6 -p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms512m -Xmx512m" \
-d elasticsearch:6.8.23

docker cp es6:/usr/share/elasticsearch/config /var/data/es6/
docker cp es6:/usr/share/elasticsearch/data /var/data/es6/
docker cp es6:/usr/share/elasticsearch/plugins /var/data/es6/
docker stop es6
docker rm es6

#映射卷启动
docker run --name es6 --net es_default -p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms512m -Xmx512m" \
-v /var/data/es6/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /var/data/es6/data:/usr/share/elasticsearch/data \
-v /var/data/es6/plugins:/usr/share/elasticsearch/plugins \
-d elasticsearch:6.8.23


docker run -d --name kibana6 --net es_default \
-e ELASTICSEARCH_HOSTS=http://es6:9200 \
-p 5601:5601 kibana:6.8.23
```



## kafka

```dockerfile
docker pull wurstmeister/kafka:2.13-2.7.0
git clone https://github.com/wurstmeister/kafka-docker.git

#创建一个topic，名为topic001，4个partition，副本因子2。
docker exec kafka-docker_kafka_1 \
kafka-topics.sh \
--create --topic topic001 \
--partitions 4 \
--zookeeper zookeeper:2181 \
--replication-factor 2


#执行以下命令查看刚刚创建的topic
docker exec kafka-docker_kafka_1 \
kafka-topics.sh --list \
--zookeeper zookeeper:2181 \
stream-in

#查看刚刚创建的topic的情况，borker和副本情况一目了然
docker exec kafka-docker_kafka_1 \
kafka-topics.sh \
--describe \
--topic stream-in \
--zookeeper zookeeper:2181


#生产
docker exec kafka-docker_kafka_1 \
kafka-console-consumer.sh \
--topic stream-in \
--bootstrap-server kafka-docker_kafka_1:9092

#消费
docker exec -it kafka-docker_kafka_1 \
kafka-console-producer.sh \
--topic stream-in \
--broker-list kafka-docker_kafka_1:9092

```



## minio

```dockerfile
docker pull minio/minio:RELEASE.2021-06-14T01-29-23Z

docker run -p 32099:9000 --name minio \
  --rm -d \
  -e "MINIO_ACCESS_KEY=minioadmin" \
  -e "MINIO_SECRET_KEY=minioadmin" \
  -v /var/data/minio/data:/data \
  -v /var/data/minio/config:/root/.minio \
  minio/minio:RELEASE.2021-06-14T01-29-23Z server /data
  
docker.io/bitnami/minio:2021.6.17-debian-10-r15

# 新版

docker run --restart=always -d \
  --name minio \
  -p 9000:9000 \
  -p 9001:9001 \
  -v /var/data/minio/config:/root/.minio \
  -v /var/data/minio/data:/data \
  -e "MINIO_ROOT_USER=minioadmin" \
  -e "MINIO_ROOT_PASSWORD=minioadmin" \
  quay.io/minio/minio server \
  --address ":9000" \
  --console-address ":9001" /data
```



## nginx

```dockerfile
docker run -d -p 80:80 -p 8848:8848 -p 32280:32280 --restart=always --name nginx-normal \
-v /var/data/nginx/html:/usr/share/nginx/html \
-v /var/data/nginx/conf/nginx.conf:/etc/nginx/nginx.conf  \
-v /var/data/nginx/logs:/var/log/nginx \
-v /var/data/nginx/conf.d:/etc/nginx/conf.d \
nginx:latest
```



## KingBase

```dockerfile
#https://registry.hub.docker.com/r/godmeowicesun/kingbase
#https://www.kingbase.com.cn/index/download/c_id/401.html
docker pull godmeowicesun/kingbase
mkdir -p /opt/docker/kingbase-latest/opt
chmod 777 /opt/docker/kingbase-latest/opt
mkdir -p /opt/docker/kingbase-latest/opt/license
cp license.dat /opt/docker/kingbase-latest/opt/license/
docker run -d -it --privileged=true -p 14321:54321 -v /opt/docker/kingbase-latest/opt:/opt --name kingbase-rv1 godmeowicesun/kingbase:latest

#端口: 14321
#用户名: SYSTEM
#密码: 123456
#默认数据库: TEST
```



## GBase8a

```dockerfile
`#https://hub.docker.com/r/shihd/gbase8a
docker pull shihd/gbase8a:1.0
docker run -itd -p5258:5258 --name gbase8a shihd/gbase8a:1.0

DB: gbase
User: root
Password: root
Port: 5258
```



## GaussDB

```
# https://hub.docker.com/r/enmotech/opengauss
docker run --name opengauss --privileged=true -d -e GS_PASSWORD=Gauss@123 -v /var/data/opengauss:/var/lib/opengauss  -u root -p 11432:5432 enmotech/opengauss:latest
```

## MongoDB



```dockerfile
cat << EOF > docker-compose_2.yml
version: '3.1'
services:
  mongo:
    image: mongo
    restart: always
    environment:
      MONGO_INITDB_ROOT_USERNAME: root
      MONGO_INITDB_ROOT_PASSWORD: example
  mongo-express:
    image: mongo-express
    restart: always
    ports:
      - 8081:8081
    environment:
      ME_CONFIG_MONGODB_ADMINUSERNAME: root
      ME_CONFIG_MONGODB_ADMINPASSWORD: example
      ME_CONFIG_MONGODB_URL: mongodb://root:example@mongo:27017/
EOF
      
docker-compose -f docker-compose.yml up -d
```







## DB2

```dockerfile
docker pull ibmoms/db2

docker run -itd --name mydb2 --privileged=true -p 50000:50000 -e LICENSE=accept -e DB2INST1_PASSWORD=db2@123 -e DBNAME=testdb -v /var/data/db2:/database ibmcom/db2

db2inst1/db2@123


su - db2inst1 // 切换用户
db2start //启动DB2
db2sampl // 创建默认的数据库SAMPLE


[root@localhost /]# su - db2inst1
Last login: Wed May 20 21:57:28 UTC 2015
[db2inst1@localhost ~]$ db2start
SQL1063N  DB2START processing was successful.
[db2inst1@localhost ~]$ db2sampl 

  Creating database "SAMPLE"...
  Connecting to database "SAMPLE"...
  Creating tables and data in schema "DB2INST1"...
  Creating tables with XML columns and XML data in schema "DB2INST1"...

  'db2sampl' processing complete.

# 常用命令
db2 create db [dbname]   #创建数据库
db2 list db directory  # 列出所有数据库
db2 list active databases  # 列出所有激活的数据库
db2 get db cfg # 列出所有数据库配置


```



## ClickHouse

```
# 拉取 server 镜像
docker pull clickhouse/clickhouse-server:21.12.3

# 拉取 client 镜像
docker pull clickhouse/clickhouse-client:21.12.3


docker run -d --name temp-clickhouse-server -p 8123:8123 --ulimit nofile=262144:262144 clickhouse/clickhouse-server:21.12.3

docker run -d \
 --name=single-clickhouse-server \
 -p 18123:8123 -p 19000:9000 -p 19009:9009 \
 --ulimit nofile=262144:262144 \
 -v /var/data/clickhouse/data:/var/lib/clickhouse:rw \
 -v /var/data/clickhouse/config:/etc/clickhouse-server:rw \
 -v /var/data/clickhouse/log:/var/log/clickhouse-server:rw \
 -e CLICKHOUSE_DB=my_database \
 -e CLICKHOUSE_USER=test \
 -e CLICKHOUSE_PASSWORD=test \
 clickhouse/clickhouse-server:21.12.3


docker run -it --rm \
--link single-clickhouse-server:clickhouse-server \
clickhouse/clickhouse-client:21.12.3 \
--host clickhouse-server -u test --password test
```





SELECT
    dadm.table_name as "表名",
    dmf.field_name as "字段名",
    case is_primary_key when 1 then "是" else '否' end as "是否主键",
    case is_null when 1 then '是' else '否' end as "是否可空",
    field_length as "长度",
    field_precision as "精度",
    field_comment as "字段注释"
FROM
    ( SELECT id, table_name FROM dc_assets_data_model WHERE data_source_type = 'FI_HIVE' GROUP BY table_name ) dadm
    JOIN dc_model_field_024 dmf ON dadm.id = dmf.data_model_id into outfile "/var/lib/mysql-files/test.csv" character set utf8 fields terminated by ',' optionally enclosed by '"' escaped by '"' lines terminated by "\r\n";



## k8s

```
wget  https://github.com/labring/sealos/releases/download/v4.1.4/sealos_4.1.4_linux_amd64.tar.gz  && \
    tar -zxvf sealos_4.1.4_linux_amd64.tar.gz sealos &&  chmod +x sealos && mv sealos /usr/bin 

sealos run labring/kubernetes:v1.25.0 labring/helm:v3.8.2 labring/calico:v3.24.1 \
     --masters 192.168.1.161 \
     --nodes 192.168.1.162,192.168.1.163 -p zhjl951753
     
     
     
## 移除污点
kubectl taint nodes centos1 node-role.kubernetes.io/control-plane:NoSchedule-
kubectl taint nodes --all node-role.kubernetes.io/control-plane-

```



## k8s-dashboard

```shell
https://blog.csdn.net/weixin_43501172/article/details/125994553
https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md
https://kubernetes.io/zh-cn/docs/tasks/access-application-cluster/web-ui-dashboard/

https://github.com/kubernetes/dashboard/releases
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml
## 修改dashboard的service为nodeport

## 创建dashboard管理用户
cat > dashboard-svc-account.yaml <<-EOF
apiVersion: v1
kind: ServiceAccount
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: dashboard-admin
  namespace: kubernetes-dashboard
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: dashboard-admin-cluster-role
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
  - kind: ServiceAccount
    name: dashboard-admin
    namespace: kubernetes-dashboard
EOF
kubectl apply -f dashboard-svc-account.yaml

## 生成token
kubectl -n kubernetes-dashboard  create token dashboard-admin
```





## kube-prometheus

```shell
git clone -b release-0.12 https://github.com/prometheus-operator/kube-prometheus.git
cd kube-prometheus
kubectl apply --server-side -f manifests/setup
kubectl wait \
	--for condition=Established \
	--all CustomResourceDefinition \
	--namespace=monitoring
kubectl apply -f manifests/
```



这两个deploy需要自己下载镜像后重新tag，上传到私库，修改镜像号

- prometheus-adapter
- kube-state-metrics
  

移除networkpolicy，不配置会影响访问。

`kubectl delete networkpolicy --all -n monitoring`



修改以下三个service的type为NodePort，端口随机

- prometheus-k8s
- grafana
- alertmanager-main





## Harbor

```shell
wget -c https://github.com/goharbor/harbor/releases/download/v2.3.5/harbor-offline-installer-v2.3.5.tgz

## 解压
cd harbor
cp harbor.yml.tmpl harbor.yml

# 生成证书
mkdir -p /data/cert/
openssl genrsa -out ca.key 4096
openssl req -x509 -new -nodes -sha512 -days 3650 \
 -subj "/C=CN/ST=Beijing/L=Beijing/O=example/OU=Personal/CN=harbor.jdragon.club" \
 -key ca.key \
 -out ca.crt


openssl genrsa -out harbor.jdragon.club.key 4096
openssl req -sha512 -new \
    -subj "/C=CN/ST=Beijing/L=Beijing/O=example/OU=Personal/CN=harbor.jdragon.club" \
    -key harbor.jdragon.club.key \
    -out harbor.jdragon.club.csr


cat > v3.ext <<-EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt_names

[alt_names]
DNS.1=harbor.jdragon.club
DNS.2=harbor.jdragon
DNS.3=localhost
EOF

openssl x509 -req -sha512 -days 3650 \
    -extfile v3.ext \
    -CA ca.crt -CAkey ca.key -CAcreateserial \
    -in harbor.jdragon.club.csr \
    -out harbor.jdragon.club.crt


openssl x509 -inform PEM -in harbor.jdragon.club.crt -out harbor.jdragon.club.cert

## 修改端口，ip，密码，https的证书配置
vim harbor.yml
## 生成的证书到/data/cert下(docker-compose下挂载了/data盘)
https:
  # https port for harbor, default is 443
  port: 11843
  # The path of cert and key files for nginx
  certificate: /data/cert/harbor.jdragon.club.cert
  private_key: /data/cert/harbor.jdragon.club.key


## 执行install
./prepare
./install.sh
```



## Containerd

```shell
# 对接harbor
## 将harbor生成的证书复制到containerd中。
scp harbor.jdragon.club.cert root@192.168.1.161:/etc/containerd/certs.d/harbor.jdragon.club:11843/
scp harbor.jdragon.club.key root@192.168.1.161:/etc/containerd/certs.d/harbor.jdragon.club:11843/
scp ca.crt root@192.168.1.161:/etc/containerd/certs.d/harbor.jdragon.club:11843/

## 证书生效
yum install -y ca-certificates
cp -a ca.crt /etc/pki/ca-trust/source/anchors/
ln -s /etc/pki/ca-trust/source/anchors/ca.crt /etc/ssl/certs/
update-ca-trust

## containerd配置私有harbor和国内镜像
   [plugins."io.containerd.grpc.v1.cri".registry]
      config_path = ""
      [plugins."io.containerd.grpc.v1.cri".registry.auths]
      [plugins."io.containerd.grpc.v1.cri".registry.configs]
        [plugins."io.containerd.grpc.v1.cri".registry.configs."harbor.jdragon.club".tls]
          insecure_skip_verify = true
        [plugins."io.containerd.grpc.v1.cri".registry.configs."harbor.jdragon.club".auth]
          username = "admin"
          password = "zhjl951753"
      [plugins."io.containerd.grpc.v1.cri".registry.headers]
      [plugins."io.containerd.grpc.v1.cri".registry.mirrors]
        [plugins."io.containerd.grpc.v1.cri".registry.mirrors."docker.io"]
            endpoint = ["https://docker.mirrors.ustc.edu.cn","http://hub-mirror.c.163.com"]
        [plugins."io.containerd.grpc.v1.cri".registry.mirrors."gcr.io"]
            endpoint = ["https://gcr.mirrors.ustc.edu.cn"]
        [plugins."io.containerd.grpc.v1.cri".registry.mirrors."k8s.gcr.io"]
            endpoint = ["https://gcr.mirrors.ustc.edu.cn/google-containers"]
        [plugins."io.containerd.grpc.v1.cri".registry.mirrors."quay.io"]
            endpoint = ["https://quay.mirrors.ustc.edu.cn"]
        [plugins."io.containerd.grpc.v1.cri".registry.mirrors."harbor.jdragon.club"]
            endpoint = ["https://harbor.jdragon.club"]

# 重启
systemctl daemon-reload && systemctl restart containerd.service

# 安装命令行客户端
## 安装nerdctl
wget https://github.com/containerd/nerdctl/releases/download/v1.1.0/nerdctl-1.1.0-linux-amd64.tar.gz
## 解压后将nerdctl移到/usr/local/bin下
mv nerdctl /usr/local/bin/

## 安装crictl
wget https://github.com/kubernetes-sigs/cri-tools/releases/download/v1.25.0/crictl-v1.25.0-linux-amd64.tar.gz
tar -zxvf crictl-v1.25.0-linux-amd64.tar.gz
mv crictl /usr/local/bin/

cat > /etc/crictl.yaml <<EOF
runtime-endpoint: unix:///var/run/containerd/containerd.sock
image-endpoint: unix:///var/run/containerd/containerd.sock
timeout: 10
debug: false
pull-image-on-create: false
EOF



# 使用nerdctl登录harbor https
nerdctl login -u admin harbor.jdragon.club:11843
```

