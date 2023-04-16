#!/bin/bash
# Ingresamos con el usuario con el cual entreremos al servidor
#  y nos dirigmos a su carpeta
su ec2-user
cd /home/ec2-user/
# Configuracion e instalacion de Docker
yum -y update
yum -y install docker
usermod -a -G docker ec2-user #sudo
id ec2-user
newgrp docker
# Configuracion e instalacion de Docker Compose
wget https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) 
mv docker-compose-$(uname -s)-$(uname -m) /usr/local/bin/docker-compose
chmod -v +x /usr/local/bin/docker-compose
systemctl enable docker.service
systemctl start docker.service
# Creacion archivo docker-compose.yml
cat > docker-compose.yml <<- "EOF"
version: "3.5"
services:
  nginx:
      image: nginx:latest
      container_name: nginx-proxy
      restart: always
      ports:
        - "80:80"
      volumes:
        - ./nginx/conf.d:/etc/nginx/conf.d
      depends_on:
        - app1
        - app2

  postgres-db:
    image: postgres
    restart: always
    volumes:
      - ./pgdata:/var/lib/postgresql/data/
    environment:
      POSTGRES_USER: tingeso
      POSTGRES_PASSWORD: tingeso
      POSTGRES_DB: milkstgo_tingeso
    ports:
      - "5432:5432"

  app1:
    container_name: milkstgo1
    image: matiasfc/tingeso_evaluacion01
    depends_on:
      - postgres-db
    ports:
      - "8091:8090"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/milkstgo_tingeso
      - SPRING_DATASOURCE_USERNAME=tingeso
      - SPRING_DATASOURCE_PASSWORD=tingeso
    deploy:
      restart_policy:
        condition: on-failure

  app2:
    container_name: milkstgo2
    image: matiasfc/tingeso_evaluacion01
    depends_on:
      - postgres-db
    ports:
      - "8092:8090"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/milkstgo_tingeso
      - SPRING_DATASOURCE_USERNAME=tingeso
      - SPRING_DATASOURCE_PASSWORD=tingeso
    deploy:
      restart_policy:
        condition: on-failure
EOF
#Creacion del volumen utilizado por la imagen de nginx junto a su configuracion
mkdir nginx
cd nginx
mkdir conf.d
cd conf.d
cat > milkstgo.conf <<- "EOF"
upstream milkstgo {
    ip_hash;
    server milkstgo1:8090;
    server milkstgo2:8090;
}

server {
    listen 80;
    charset utf-8;
    access_log off;

    location / {
        proxy_pass http://milkstgo;
        proxy_set_header Host $host:$server_port;
        proxy_set_header X-Forwarded-Host $server_name;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    location /static {
        access_log   off;
        expires      30d;

        alias /app/static;
    }
}
EOF
