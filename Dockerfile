# 注意：该 Dockfile 文件放置在项目的根目录，即：跟 pom.xml 同层级的目录中
# 设置JAVA版本，如果需要生成图片，则需要使用 java:8 这个基础镜像
FROM adoptopenjdk/openjdk8
ARG jarFile
VOLUME /app
COPY ${jarFile} /app/app.jar
EXPOSE 20000

CMD ["java", "-jar", "/app/app.jar"]