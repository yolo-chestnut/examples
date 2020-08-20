FROM openjdk:8-jre

ENTRYPOINT ["java", "-jar", "/app/test/web-0.0.1.jar"]

ARG JAR_FILE

# 前者指宿主机路径 后者为容器内路径
ADD ${JAR_FILE} /app/test/web-0.0.1.jar

# dev使用的端口
EXPOSE 8888
