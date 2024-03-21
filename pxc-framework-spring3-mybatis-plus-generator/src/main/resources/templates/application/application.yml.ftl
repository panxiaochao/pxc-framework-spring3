# server
server:
port: 8088
servlet:
context-path: /

spring:
# 关闭使用thymeleaf
thymeleaf:
enabled: false
application:
name: springboot-demo
## 数据库配置
datasource:
type: com.zaxxer.hikari.HikariDataSource
driver-class-name: com.mysql.cj.jdbc.Driver
url: jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false&rewriteBatchedStatements=true&characterEncoding=utf-8&autoReconnect=true&useUnicode=true&allowMultiQueries=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
username: root
password: 123456