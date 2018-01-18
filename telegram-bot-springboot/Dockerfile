FROM java:8-jre
MAINTAINER Aydar Gumerbaev <aydar@gumerbaev.ru>

ENV TELEGRAM_API_KEY $TELEGRAM_API_KEY

ADD ./build/libs/amfibot.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/amfibot.jar"]

EXPOSE 4000