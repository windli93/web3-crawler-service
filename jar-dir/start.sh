#编译docker镜像
sudo docker build -t web3-crawler-service:v0.1 .

#启动docker服务
sudo  docker run --name web3-crawler-service \
  -p 9901:9901 \
  -d web3-crawler-service:v0.1

#清除无用的镜像
sudo docker rmi -f  `docker images | grep '<none>' | awk '{print $3}'`
