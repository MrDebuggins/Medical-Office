FROM mariadb:latest
ENV MARIADB_USER=apidev
ENV MARIADB_ROOT_PASSWORD=apidev
ENV MARIADB_DATABASE=Medical
ENV TZ=Europe/Bucharest
RUN ls
COPY PDP/src/main/resources/sql/doctori.sql /docker-entrypoint-initdb.d
COPY PDP/src/main/resources/sql/pacienti.sql /docker-entrypoint-initdb.d
COPY PDP/src/main/resources/sql/programari.sql /docker-entrypoint-initdb.d