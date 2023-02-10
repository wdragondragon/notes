## 一、主动被动模式



![ftp工作模式.drawio](https://gitee.com/wdragondragon/picture-bed/raw/master/imgs/202111171726201.png)

## 二、配置参数

### 1. **登录和对匿名用户的设置**

|                              |                                                              |
| ---------------------------- | ------------------------------------------------------------ |
| write_enable=YES             | 是否对登录用户开启写权限。属全局性设置。默认NO               |
| local_enable=YES             | 是否允许本地用户登录FTP服务器。默认为NO                      |
| anonymous_enable=YES         | 设置是否允许匿名用户登录FTP服务器。默认为YES                 |
| ftp_username=ftp             | 定义匿名用户的账户名称，默认值为ftp。                        |
| no_anon_password=YES         | 匿名用户登录时是否询问口令。设置为YES，则不询问。默认NO      |
| anon_world_readable_only=YES | 匿名用户是否允许下载可阅读的文档，默认为YES。                |
| anon_upload_enable=YES       | 是否允许匿名用户上传文件。只有在write_enable设置为<br/>YES时，该配置项才有效。而且匿名用户对相应的目录必须有写权限。默认为NO。 |
| anon_mkdir_write_enable=YES  | 是否允许匿名用户创建目录。只有在write_enable设置为YES时有效。且匿名用户对上层目录有写入的权限。默认为NO。 |
| anon_other_write_enable=NO   | 若设置为YES，则匿名用户会被允许拥有多于上传和建立目录的权限，还会拥有删除和更名权限。默认值为NO。 |

### 2. **登录和对匿名用户的设置**

​	用户登录FTP服务器成功后，服务器可向登录用户输出预设置的欢迎信息。

|                                       |                                                              |
| ------------------------------------- | ------------------------------------------------------------ |
| ftpd_banner=Welcome to my FTP server. | 用户登录FTP服务器成功后，服务器可向登录用户输出预设置的欢迎信息。 |
| banner_file=/etc/vsftpd/banner        | 设置用户登录时，将要显示输出的文件。该设置项将覆盖ftpd_banner的设置。 |
| dirmessage_enable=YES                 | 设置是否显示目录消息。若设置为YES，则当用户进入特定目录（比如/var/ftp/linux）时，将显示该目录中的由message_file配置项指定的文件（.message）中的内容。 |
| message_file=.message                 | 设置目录消息文件。可将显示信息存入该文件。该文件需要放在 相应的目录（比如/var/ftp/linux）下 |

### 3. 设置用户登录后所在的目录 

|                     |                                                              |
| ------------------- | ------------------------------------------------------------ |
| local_root=/var/ftp | 设置本地用户登录后所在的目录。默认配置文件中没有设置该项，此时用户登录FTP服务器后，所在的目录为该用户的主目录，对于root用户，则为/root目录。 |
| anon_root=/var/ftp  | 设置匿名用户登录后所在的目录。若未指定，则默认为/var/ftp目录。 |

### 4. 控制用户是否允许切换到上级目录

​	在默认配置下，用户可以使用“cd..”命名切换到上级目录。比如，若用户登录后所在的目录为/var/ftp，则在“ftp>”命令行 下，执行“cd..”命令后，用户将切换到其上级目录/var，若继续执行该命令，则可进入Linux系统的根目录，从而可以对整个Linux的文件系统 进行操作。

​	若设置了write_enable=YES，则用户还可对根目录下的文件进行改写操作，会给系统带来极大的安全隐患，因此，必须防止用户切换到Linux的根目录，相关的配置项如下：

|                                          |                                                              |
| ---------------------------------------- | ------------------------------------------------------------ |
| chroot_list_enable=YES                   | 设置是否启用chroot_list_file配置项指定的用户列表文件。设置为YES则除了列在j/etc/vsftpd/chroot_list文件中的的帐号外，所有登录的用户都可以进入ftp根目录之外的目录。默认NO |
| chroot_list_file=/etc/vsftpd/chroot_list | 用于指定用户列表文件，该文件用于控制哪些用户可以切换到FTP站点根目录的上级目录。 |
| chroot_local_user=YES                    | 用于指定用户列表文件中的用户，是否允许切换到上级目录。默认NO |


​	注意：要对本地用户查看效果，需先设置local_root=/var/ftp

​	具体情况有以下几种：

1. 当chroot_list_enable=YES，chroot_local_user=YES时，在/etc/vsftpd/chroot_list文件中列出的用户，可以切换到上级目录；未在文件中列出的用户，不能切换到站点根目录的上级目录。
2. 当chroot_list_enable=YES，chroot_local_user=NO时，在/etc/vsftpd/chroot_list文件中列出的用户，不能切换到站点根目录的上级目录；未在文件中列出的用户，可以切换到上级目录。
3. 当chroot_list_enable=NO，chroot_local_user=YES时，所有用户均不能切换到上级目录。
4. 当chroot_list_enable=NO，chroot_local_user=NO时，所有用户均可以切换到上级目录。
5. 当用户不允许切换到上级目录时，登录后FTP站点的根目录“/”是该FTP账户的主目录，即文件的系统的/var/ftp目录。

### 5. 设置访问控制

#### （1）设置允许或不允许访问的主机（见TBP14）

|                  |                                                              |
| ---------------- | ------------------------------------------------------------ |
| tcp_wrappers=YES | 用来设置vsftpd服务器是否与tcp wrapper相结合，进行主机的访问控制。默认设置为YES，vsftpd服务器会检查/etc/hosts.allow和/etc /hosts.deny中的设置，以决定请求连接的主机是否允许访问该FTP服务器。这两个文件可以起到简易的防火墙功能。 |

​	比如，若要仅允许192.168.168.1～192.168.168.254的用户，可以访问连接vsftpd服务器，则可在/etc/hosts.allow文件中添加以下内容：
vsftpd:192.168.168.0/255.255.255.0:allow
all:all:deny

#### （2）设置允许或不允许访问的用户

​	对用户的访问控制由/etc/vsftpd/user_list和/etc/vsftpd/ftpusers文件来控制实现。相关配置命令如下：

|                     |                                                              |
| ------------------- | ------------------------------------------------------------ |
| userlist_enable=YES | 决定/etc/vsftpd/user_list文件是否启用生效。YES则生效，NO不生效。 |
| userlist_deny=YES   | 决定/etc/vsftpd/user_list文件中的用户是允许访问还是不允许访问。若设置为YES，则/etc/vsftpd/user_list 文件中的用户将不允许访问FTP服务器；若设置为NO，则只有vsftpd.user_list文件中的用户，才能访问FTP服务器。 |

### 6. 设置访问速度

|                  |                                                              |
| ---------------- | ------------------------------------------------------------ |
| anon_max_rate=0  | 设置匿名用户所能使用的最大传输速度，单位为b/s。若设置为0，则不受速度限制，此为默认值。 |
| local_max_rate=0 | 设置本地用户所能使用的最大传输速度。默认为0，不受限制。      |

### 7. 定义用户配置文件

​	在vsftpd服务器中，不同用户还可使用不同的配置，这要通过用户配置文件来实现。

|                                      |                                  |
| ------------------------------------ | -------------------------------- |
| user_config_dir=/etc/vsftpd/userconf | 用于设置用户配置文件所在的目录。 |


​	设置了该配置项后，当用户登录FTP服务器时，系统就会到/etc/vsftpd/userconf目录下读取与当前用户名相同的文件，并根据文件中的配 置命令，对当前用户进行更进一步的配置。比如，利用用户配置文件，可实现对不同用户进行访问的速度进行控制，在各用户配置文件中，定义 local_max_rate配置，以决定该用户允许的访问速度。

### 8. 与连接相关的设置

|                             |                                                              |
| --------------------------- | ------------------------------------------------------------ |
| listen=YES                  | 设置vsftpd服务器是否以standalone模式运行。以standalone模式运行是一种较好的方式，此时listen必须设置为YES， 此为默认值，建议不要更改。很多与服务器运行相关的配置命令，需要此运行模式才有效。若设置为NO，则vsftpd不是以独立的服务运行，要受 xinetd服务的管理控制，功能上会受限制。 |
| max_clients=0               | 设置vsftpd允许的最大连接数，默认为0，表示不受限制。若设置为150时，则同时允许有150个连接，超出的将拒绝建立连接。只有在以standalone模式运行时才有效。 |
| max_per_ip=0                | 设置每个IP地址允许与FTP服务器同时建立连接的数目。默认为0，不受限制。通常可对此配置进行设置，防止同一个用户建立太多的连接。只有在以standalone模式运行时才有效。 |
| listen_address=IP地址       | 设置在指定的IP地址上侦听用户的FTP请求。若不设置，则对服务器所绑定的所有IP地址进行侦听。只有在以standalone模式运行时才有效。 对于只绑定了一个IP地址的服务器，不需要配置该项，默认情况下，配置文件中没有该配置项。若服务器同时绑定了多个IP地址，则应通过该配置项，指定在哪 个IP地址上提供FTP服务，即指定FTP服务器所使用的IP地址。<br/>注意：设置此值前后，可以通过netstat -tnl对比端口的监听情况 |
| accept_timeout=60           | 设置建立被动（PASV）数据连接的超时时间，单位为秒，默认值为60。 |
| connect_timeout=60          | PORT方式下建立数据连接的超时时间，单位为秒。                 |
| data_connection_timeout=300 | 设置建立FTP数据连接的超时时间，默认为300秒。                 |
| idle_session_timeout＝600   | 设置多长时间不对FTP服务器进行任何操作，则断开该FTP连接，单位为秒，默认为600秒。即设置发呆的逾时时间，在这个时间内，若没有数据传送或指令的输入，则会强行断开连接。 |
| pam_service_name=vsftpd     | 设置在PAM所使用的名称，默认值为vsftpd。                      |
| setproctitle_enable=NO\|YES | 设置每个与FTP服务器的连接，是否以不同的进程表现出来，默认值为NO，此时只有一个名为vsftpd的进程。若设置为YES，则每个连接都会有一个vsftpd进程，使用“ps -ef\|grep ftp”命令可查看到详细的FTP连接信息。安全起见，建议关闭。 |




### 9. FTP工作方式与端口设置

#### （1）FTP工作方式简介
​      FTP的工作方式有两种，一种是PORT FTP，另一种是PASV FTP。下面介绍其工作方式。
二者的区别在于PORT FTP的数据传输端口是由FTP服务器指定的，而PASV FTP则是由FTP客户端指定的，而且每次数据连接所使用的端口号都不同。正因为如此，所以在CuteFTP等FTP客户端软件中，其连接类型设置项中有PORT和PASV两种选择。

      当FTP服务器设置为PASV工作模式时，客户端也必须设置为PASV连接类型。若客户端连接类型设置为PORT，则能建立FTP连接，但在执行ls或get等需要数据请求的命令时，将会出现无响应并最终报告无法建立数据连接。

#### （2）与端口相关的配置

|                           |                                                              |
| ------------------------- | ------------------------------------------------------------ |
| listen_port=21            | 设置FTP服务器建立连接所侦听的端口，默认值为21。              |
| connect_from_port_20＝YES | 默认值为YES，指定FTP数据传输连接使用20端口。若设置为NO，则进行数据连接时，所使用的端口由ftp_data_port指定。 |
| ftp_data_port=20          | 设置PORT方式下FTP数据连接所使用的端口，默认值为20            |
| pasv_enable=YES\|NO       | 若设置为YES，则使用PASV工作模式；若设置为NO，使用PORT模式。默认为YES，即使用PASV模式。 |
| pasv_max_port=0           | 设置在PASV工作方式下，数据连接可以使用的端口范围的上界。默认值为0，表示任意端口。 |
| pasv_mim_port=0           | 设置在PASV工作方式下，数据连接可以使用的端口范围的下界。默认值为0，表示任意端口。 |
| pasv_promiscuous=no       | 是否屏蔽对pasv进行安全检查，（当有安全隧道时可禁用）         |
| pasv_address=ip地址       | pasv模式中服务器传回的ip地址                                 |



### 10. 设置传输模式

​	FTP在传输数据时，可使用二进制（Binary）方式，也可使用ASCII模式来上传或下载数据。

|                           |                                           |
| ------------------------- | ----------------------------------------- |
| ascii_download_enable=YES | 设置是否启用ASCII模式下载数据。默认为NO。 |
| ascii_upload_enable=YES   | 设置是否启用ASCII模式上传数据。默认为NO。 |

### 11. 设置上传文档的所属关系和权限 

#### （1）设置匿名上传文档的属主

|                        |                                                              |
| ---------------------- | ------------------------------------------------------------ |
| chown_uploads=YES      | 用于设置是否改变匿名用户上传的文档的属主。默认为NO。若设置为YES，则匿名用户上传的文档的属主将被设置为chown_username配置项所设置的用户名。 |
| chown_username=whoever | 设置匿名用户上传的文档的属主名。只有chown_uploads=YES时才有效。建议不要设置为root用户。 但系统默root |

#### （2）新增文档的权限设定

|                     |                                                              |
| ------------------- | ------------------------------------------------------------ |
| local_umask=022     | 设置本地用户新增文档的umask，默认为022，对应的权限为755。umask为022，对应的二进制数为000 010 010，将其取反为111 101 101，转换成十进制数，即为权限值755，代表文档的所有者（属主）有读写执行权，所属组有读和执行权，其他用户有读和执行权。022适合于大多数情 况，一般不需要更改。若设置为077，则对应的权限为700。 |
| anon_umask=022      | 设置匿名用户新增文档的umask。默认077                         |
| file_open_mode=0755 | 设置上传文档的权限。权限采用数字格式。 默认0666              |

### 12. 日志文件

|                                 |                                                              |
| ------------------------------- | ------------------------------------------------------------ |
| xferlog_enable=YES              | 是否启用上传/下载日志记录。默认为NO                          |
| xferlog_file=var/log/vsftpd.log | 设置日志文件名及路径。需启用xferlog_enable选项               |
| xferlog_std_format=YES          | 日志文件是否使用标准的xferlog日志文件格式（与wu-ftpd使用的格式相同） 。默认为NO |

### 13. 其他设置

|                       |                                                              |
| --------------------- | ------------------------------------------------------------ |
| text_userdb_names=NO  | 设置在执行ls命令时，是显示UID、GID还是显示出具体的用户名或组名称。默认为NO，以UID和GID方式显示，若希望显示用户名和组名称，则设置为YES。 |
| ls_recurse_enable=YES | 若设置为YES，则允许执行“ls –R”这个命令，默认值为NO。在配置文件中该配置项被注释掉了，与此类似的还有一些配置，需要启用时，将注释符去掉并进行YES或NO的设置即可 |



## 三、原文链接

[FTP协议的主动模式和被动模式的区别](https://www.cnblogs.com/rainman/p/11647723.html)

[FTP的主动模式和被动模式](https://www.cnblogs.com/mawanglin2008/articles/3607767.html)

[FTP服务之工作模式及配置详解](https://blog.csdn.net/weixin_43279032/article/details/85616585)

[vsftpd主配置文件](https://blog.csdn.net/weixin_44033360/article/details/106425176?spm=1001.2101.3001.6650.3&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-3.no_search_link&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-3.no_search_link)



