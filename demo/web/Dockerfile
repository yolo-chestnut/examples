FROM openjdk:8-jre

#ENTRYPOINT ["java", "-jar", "/app/test/web-0.0.1.jar"]
#spring.config.location参数必须是绝对路径！！！
ENTRYPOINT ["java", "-jar", "/app/web-0.0.1/web-0.0.1.jar", "--spring.config.location=/app/web-0.0.1/conf/"]

ARG JAR_FILE

# 前者指宿主机路径 后者为容器内路径
ADD ${JAR_FILE} /app/

# dev使用的端口
EXPOSE 8888
