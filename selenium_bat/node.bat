::set path=%PATH%;C:\Users\Administrator\Desktop\selenium_server\;C:\Program Files\Eclipse Adoptium\jdk-11.0.16.101-hotspot\bin
set command=java
::set chromeDriver=-Dwebdriver.chrome.driver="chromedriver.exe --whitelisted-ips --allowed-origins='*'"
set jarParams=-jar selenium-server-4.6.0.jar
set type=node 
::set hub=--hub http://10.7.0.12:4444
set hub=--hub http://192.168.47.1:4444
set port=--port 18882
::set chrome= --allow-cors true --driver-configuration display-name="chrome" max-sessions=5 webdriver-path="D:\selenium_server\chromedriver.exe" stereotype="{}"
set chrome = --detect-drivers --max-sessions 16 --session-timeout 180 --heartbeat-period 60
%command% %chromeDriver% %jarParams% %type% %hub% %port% %chrome%