## Kinoday

### **Название:**

Сайт кинотеатра с микросервисной архитектурой. 

-----

### **Описание:**

 Приложение, состоящее из 5 сервисов, настроенным CI/CD пайплайном, несколькими окружениями, с подключением сторонних API, таких как платежная система - Xsolla.

-----


### **Технологии:**

    • Java 11

    • Spring Boot (+security,data,mvc)
    
    • Spring Cloud Config Server
    
    • База данных PostgreSQL
   
    • Docker
    
    • Настроенный CI/CD pipeline через GitHub Action
    
    • Шаблонизатор Thymeleaf
    
-----

### **Логика приложения:**

• Пользователь имеет возможность просмотра списка фильмов, билетов, мест, а так же покупки билетов через стороннюю платежную систему

• Пользователь-администратор может добавлять новые сеансы, новости, "пробивать" билеты

• Приложение в фоне обрабатывает списки билетов, меняет им статусы, обрабатывает платежи

• Приложение хранит фильмы, кинотеатры и отдают их на фронт для отображения

### **Архитектура приложения**
![image](https://user-images.githubusercontent.com/47852430/176085972-cf0cae39-eb34-4217-952a-98eac215ac94.png)

-----

### **CI/CD**

Сборка, контейнеризация, доставка и интеграция всех сервисов однотипна

#### **Сборка:**

Сборка осуществляется с помощью makefile

```makefile
#!/usr/bin/env bash
build:
	mvn clean --quiet package -Dmaven.test.skip=true --quiet spring-boot:repackage --quiet -Dmaven.test.skip=true -Dspring.profiles.active=prod
	cp "target/app.jar" "app.jar";
	rm -rf target
```

#### **Контейнеризация:**
С помощью этого Dockerfile в <i>CI/CD pipeline</i> происходила сборка приложения
```dockerfile
FROM openjdk
ADD app.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```
#### **CI/CD pipeline:**
На каждый push/pull_request в master запускается этот pipeline, состоящий из: 

   • Перключения на ветку
   
   • Сборки приложения 
   
   • Авторизации в DockerHub
   
   • Настройки сборщика Docker Buildx для сборки образа для ARM
   
   • Сборка контейнера и отправка его в DockerHub
   
   • Остановка контейнера через SSH-команду
   
   • Запуск контейнера через SSH-команду
   
Секретные параметры передаются в Secrets репозитория
```yml
name: Deploy container

on:
  push:
    branches: [ master ]
  pull_request:
    branches: [ master ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
      
    - name: Set up QEMU #(for support many platforms)
      uses: docker/setup-qemu-action@v1
      
    - name: Build Jar
      run: make    
    -
      name: Login to DockerHub
      uses: docker/login-action@v1 
      with:
        username: ${{ secrets.DOCKERHUB_USER }}
        password: ${{ secrets.DOCKERHUB_PASSWORD }}
    
    - name: Set up Docker Buildx
      uses: docker/setup-buildx-action@v1
      
    - name: Build and push container
      uses: docker/build-push-action@v2
      with:
          context: .
          file: ./Dockerfile
          push: true
          platforms: arm64
          tags: ${{ secrets.DOCKERHUB_USER }}/${{ secrets.DOCKER_IMAGE }}:${{ secrets.DOCKER_IMAGE_TAG }}
       
    - name: Stop container via ssh command
      uses: appleboy/ssh-action@master
      with:
        host: ${{ secrets.HOST_ADDRESS }}
        username: ${{ secrets.HOST_USER }}
        port: ${{ secrets.HOST_PORT }}
        key: ${{ secrets.HOST_KEY }}
        script: |
         (docker stop ${{ secrets.DOCKER_IMAGE }} 2>/dev/null || true)
         (docker rm ${{ secrets.DOCKER_IMAGE }} 2>/dev/null || true) 
    - name: Start container via ssh command 
      uses: cross-the-world/ssh-pipeline@master

      with:
        host: ${{ secrets.HOST_ADDRESS }}
        user: ${{ secrets.HOST_USER }}
        key: ${{ secrets.HOST_KEY }}
        port: ${{ secrets.HOST_PORT }}
        connect_timeout: 10s
        script: |
          docker pull ${{ secrets.DOCKERHUB_USER }}/${{ secrets.DOCKER_IMAGE }}:${{ secrets.DOCKER_IMAGE_TAG }}
          docker run ${{ secrets.DOCKER_RUNPARAMS }} ${{ secrets.DOCKERHUB_USER }}/${{ secrets.DOCKER_IMAGE }}:${{ secrets.DOCKER_IMAGE_TAG }}
```


-----