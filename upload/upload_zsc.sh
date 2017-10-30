#!/bin/sh
host="rawdatacentral.approach.saas.treefinance.com.cn"


url="http://$host/website/uploadPluginJar"
gradle clean install -x test
list=`find rawdatacentral-plugin-operator/build/libs/rawdatacentral-plugin-operator.jar -name '*.jar' | grep -v 'sources'`
for file in $list
do
    curl  -F "jar=@$file" $url
    curl  -F "jar=@$file;fileName=rawdatacentral-plugin-operator-$HOSTNAME.jar" $url
done

url="http://$host/redis/uploadFile"
list=`find rawdatacentral-plugin-operator/src/main/resources -name '*.js'`
for file in $list
do
   parent_name=`dirname $file`
   file_name=${parent_name##*/}'.'${file##*/}
   curl  -F "cache_file=@$file;fileName=$file_name" $url
   echo \n
done

