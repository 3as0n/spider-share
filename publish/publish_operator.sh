#!/bin/sh
gradle spider-operator-web:bootRepackage -x test --refresh-dependencies -Denv=dev
app_name="spider-operator-web"
server_application="/dashu/application/$app_name"
server_log_path="/dashu/log/$app_name"

app_name="$app_name-share-main"
server_ip="192.168.5.12"
server_port=22



echo 项目名称:$app_name
echo 发布包路径:$app_name/build/libs

cd $app_name/build/libs;
file=`ls -rt *.jar | grep -v 'source' |tail -1`
if [ ! -e "$file" ]
then
	echo "\033[31mnot found any *.tar.gz\033[0m"
    exit
fi
echo 找到项目文件:$file

ssh root@$server_ip -p $server_port << EOF
    if [ ! -d "$server_application" ] ;
    then
    mkdir -p $server_application ;
    echo "创建目录":$server_application;
    fi
EOF


echo 上传项目:$file 到服务器:$server_ip:$server_port,目录:$server_application
scp -P $server_port  $file root@$server_ip:$server_application/  ;
scp -P $server_port ../../../bin/* root@$server_ip:$server_application/ ;

ssh root@$server_ip -p $server_port << EOF
    cd $server_application
    ./app.sh start;
    echo $file;
#    tail -100f $server_log_path/$app_name.log
    exit;
EOF

