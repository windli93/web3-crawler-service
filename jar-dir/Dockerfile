# 设置基础镜像
FROM adoptopenjdk/openjdk11:jdk-11.0.8_10-alpine
#下载字体
RUN apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*
# 设置时区与语言环境变量
ENV TIME_ZONE=Asia/Shanghai \
        LANG=en_US.UTF-8 \
        LANGUAGE=en_US.UTF-8 \
        LC_ALL=en_US.UTF-8
# 执行以下命令设置时区
RUN apk add --no-cache tzdata \
  && echo "${TIME_ZONE}" > /etc/timezone \
  && ln -sf /usr/share/zoneinfo/${TIME_ZONE} /etc/localtime
#将当前的jar包改名
COPY  web3-crawler-*.jar app.jar
#暴露端口
EXPOSE 9901
#java启动命令
CMD ["java", "-Dspring.profiles.active=dev", "-jar","app.jar"]