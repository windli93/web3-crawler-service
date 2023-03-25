### demo介绍

1. 项目使用了Java的Spring Boot框架，使用了maven来管理项目依赖
2. 项目分成了三个小模块：
    - api模块，主要提供对内接口
    - logic业务逻辑模块，业务逻辑
    - web模块，提供对外Http接口+项目启动类
3. 项目主要使用Selenium+Htmlunit来构建爬虫服务，
    - selenium的作用是获取网页信息+操作Dom树，爬取包括Cookie信息供后台进行接口请求，最大程度的伪装成自然用户进行网页操作。
    - Htmlunit主要用来进行接口通讯，可以绕过Cloudflare反爬虫服务和目标接口进行服务通讯，也就是俗称的等5秒问题。

### 服务组件版本

1. matemask历史版本 10.22 下载地址：https://github.com/MetaMask/metamask-extension/releases
2. chrome历史版本：107 下载地址：https://www.chromedownloads.net/chrome64win/
4. chrome驱动：107 下载地址：https://chromedriver.chromium.org/downloads
5. selenium版本 4.6.0 下载地址：https://www.selenium.dev/downloads/
6. selenium容器版本 4.6.0 下载地址: https://hub.docker.com/r/selenium/standalone-chrome/tags?page=1&name=4.6.0

### 本地开发调试：

    - 进入selenium_bat文件夹，修改node.bat的ip地址为你的本地地址
    - 控制台开启注册节点 ./hub.bat
    - 控制台开启工作节点  ./node.bat

### 容器构建过程：

- 前置条件 一台Linux机器+Docker服务
- 第一步 简单构建mysql服务：
   ```
  sudo docker run -p 3306:3306 \
  --name mymysql \
  --restart=always \
  -v $PWD/mysql/conf:/etc/mysql/conf.d \
  -v $PWD/mysql/logs:/logs \
  -v $PWD/mysql/data:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -d mysql:5.7
   ```
- 第二步 简单构建Selenium服务:
   ```
  sudo  docker run --name selenium-stadalone \
  -p 4444:4444 \
  -d selenium/standalone-chrome:4.6.0-20221104
  ```
- 第三步 简单构建Java服务
  ```
    1. 修改web模块resource中的application-dev文件中+MSYQL信息
    2. 修改web模块resource中的application-dev文件中selenium ip地址
    3. 使用maven编译项目，编译完成之后将jar-dir文件夹放入linux机器中，进入该文件夹，执行 sudo sh start.sh
  ```

### 参考文章和项目

- [Java+Selenium3方法篇21-webdriver处理浏览器多窗口切换](https://blog.csdn.net/u011541946/article/details/73611301)
- [Selenium+Java 中的窗口切换](https://blog.csdn.net/qq_42014098/article/details/109765059)
- [Python + Selenium + matemask自动化测试](https://github.com/luoyeETH/selenium_metamask_auto_testing)
- [Python + Seleniuium 视频说明](https://www.bilibili.com/video/BV1yq4y1v7xQ/?vd_source=80deb42ba1fe24ce32dbe827b821ef36)