::set path=%PATH%;C:\Program Files\Eclipse Adoptium\jdk-11.0.16.101-hotspot\bin
java -jar selenium-server-4.6.0.jar hub --port 4444 --session-request-timeout 180 --healthcheck-interval 120