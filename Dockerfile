# 使用预装maven 和 jdk 21 的镜像
FROM maven:3.9-amazoncorretto-21
WORKDIR /app

##只复制必要的源代码和配置文件
COPY pom.xml .
COPY src ./src

##构建应用
RUN mvn clean package -DskipTests

# 暴漏端口
EXPOSE 8124

##运行应用
CMD ["java", "-jar", "target/yu-ai-agent-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]