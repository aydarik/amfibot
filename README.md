[![Build Status](https://travis-ci.org/aydarik/amfibot.svg?branch=master)](https://travis-ci.org/aydarik/amfibot)

# Amfibot

Telegram Bot

### How to build

Gradle: `./gradlew clean build`

Docker: `docker build -t aydarik/amfibot:latest ./telegram-bot-springboot`

### How to start

Be sure, that you have exported environment variable: `export TELEGRAM_API_KEY="xxxxx"`

Then run: `docker run -d -p 0.0.0.0:8888:8888 aydarik/amfibot`

### How to use

Open http://localhost:8888 in any browser for landing page.

Write to your bot (can be created by @BotFather bot).
