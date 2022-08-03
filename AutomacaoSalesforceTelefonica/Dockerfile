FROM maven:3.8.4-jdk-11
LABEL description="Telefonica - Testes automatizados"
LABEL version="1.0.0"

ENV DISPLAY=":1"

ARG TELEFONICA_TESTS_FOLDER="/usr/src/claro-tests"
ARG GOOGLE_CHROME_DRIVER_VERSION="96.0.4664.45"

### install google chrome and dependencies
RUN set -ex \
    cd /tmp \
    && wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && sh -c 'echo "deb https://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list' \
    && apt-get update \
    && apt-get install --yes google-chrome-stable xvfb \
    && rm -rf /var/lib/apt/lists/*

### install chromedriver
RUN set -ex \
    cd /tmp \
    && wget -Nv https://chromedriver.storage.googleapis.com/$GOOGLE_CHROME_DRIVER_VERSION/chromedriver_linux64.zip \
    && unzip chromedriver_linux64.zip \
    && chmod -v +x chromedriver \
    && mv -v chromedriver /usr/bin/ \
    && rm -v chromedriver_linux64.zip

### create TELEFONICA tests folder
RUN set -ex \
    mkdir -p $TELEFONICA_TESTS_FOLDER

### copy project contents
COPY pom.xml .
RUN mvn -B -f pom.xml dependency:go-offline

COPY . $TELEFONICA_TESTS_FOLDER
COPY entrypoint.sh /opt/entrypoint.sh

### run tests
WORKDIR $TELEFONICA_TESTS_FOLDER

EXPOSE 7644

RUN set -ex \
    chmod +x /opt/entrypoint.sh

ENTRYPOINT ["/bin/sh", "/opt/entrypoint.sh"]