FROM java:8-jre
MAINTAINER Aydar Gumerbaev <aydar@gumerbaev.ru>

ADD ./build/libs/amfibot.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/amfibot.jar"]

EXPOSE 4000