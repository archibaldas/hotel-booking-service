# Hotel Booking Service - Сервис бронирования отелей

## 📋 Описание проекта

Backend-составляющяя сервиса бронирования отелей с
возможностью управлять контентом через административную панель CMS.

### Основные возможности
 * Регистрации новых пользователей с определенной ролью "ADMIN" или "USER"
 * Бронирование комнат из списка отелей на определенный срок.
 * Комнаты можно искать с определенными фильтрами например по отелю, доступности дат, цене, количеству гостей
 * Отели можно искать по удаленности от центра города рейтингу, имени, описанию.
 * Администраторы могут управлять данными, изменять создавать или удалять отели, комнаты, пользователей.
 * Так же администраторы могут получать и статистику регистраций пользователей и бронирований в формате CSV.

### 🔧 Технологии

* Backend: `Java, SpringBoot, Spring Data JPA, Spring Security`
* Контейнер: `docker, docker-compose`
* Базы данных: `PostgreSQL, MongoDB`
* Инициализация базы данных: `Liquibase`
* Интегрированная система: `Apache Kafka`
* Безопасность: `Basic Auth`
* Документация: `OpenApi, spingdoc`

### Установка и запуск

## Установка и запуск

### Требования
- Java 17+
- Gradle
- Docker

### Загрузка проекта
Для загрузки проекта с GITHUB:
```bash
git clone https://github.com/archibaldas/hotel-booking-service.git
cd hotel-booking-service
```

### Настройка Базы Данных
Убедитесь что для вашей системы установлены `Docker` и `Docker-compose`
В директории `docker` проверьте `docker-compose.yml`
```yaml
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: {Имя пользователя}
      POSTGRES_PASSWORD: {Пароль пользователя}
      POSTGRES_DB: {Название базы данных}
```
В Корневом каталоге приложения, через терминал выполнить команды:
```bash
cd docker
docker-compose up -d
```
Перед запуском укажите параметры подключения к базе в `application.yml`.  
Имя пользователя, пароль, и название базы данных должны быть одинаковыми для `docker-compose.yml` и для `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/{Название базы данных}
    username: {Имя пользователя}
    password: {Пароль пользователя}
    driver-class-name: org.postgresql.Driver
```

Так же в файле `docker-compose.yml` реализована сразу возможность разворачивания приложения с подключением к базе данных и изменением в `application.yml`
для этого необходимо раскомментировать участок кода в `docker-compose.yml`

```yaml
  app:
    build:
      context: ..
      dockerfile: docker/Dockerfile
    container_name: hotel-booking-app
    depends_on:
      postgres:
        condition: service_healthy
    ports:
      - 8080:8080
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/hotel_booking_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
    restart: unless-stopped
```

и так же использовать закомментированные данные в `application.yml` что бы код выглядел так:

```yaml
spring:
  application:
    name: HotelBookingService

  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://postgres:5432/hotel_booking_db}
#    url: jdbc:postgresql://localhost:5432/hotel_booking_db
    username: ${SPRING_DATASOURCE_USERNAME:postgres}
#    username: postgres
    password: ${SPRING_DATASOURCE_PASSWORD:postgres}
#    password: postgres
    driver-class-name: org.postgresql.Driver
  jpa:
    properties:
      hibernate:
        default_schema: hotel_booking_schema
    hibernate:
      ddl-auto: ${SPRING_JPA_HIBERNATE_DDL_AUTO:validate}
#      ddl-auto: validate
      show-sql: false
```
При первом запуске приложения, таблицы, индексы и ключи будут созданы автоматически с помощью скриптов `Liquibase`.
`Kafka, Zookeeper, MongoDB` так же разворачиваются из контейнера.

## ▶️ Локальный запуск
1. Убедитесь, что PostgreSQL запущен и настроен
2. Отредактируйте `src/main/resources/application.yml`
3. Запустите приложение: `./gradlew bootRun`
4. Swagger будет доступен по адресу: [http://localhost:8080/swagger-ui/index.html](http://localhost:8087/swagger-ui/index.html)

### ## 📡 API Эндпоинты

Для получения документации из Swagger: [http://localhost:8080/swagger-ui/index.html](http://localhost:8087/swagger-ui/index.html)
Необходимо зарегистрировать первого пользователя после запуска приложения через POST `/api/users/registration`
С определением роли ADMIN или USER `?roleType=USER`
и телом:

```json
{
  "username": "string",
  "password": "string",
  "email": "string"
}
```
После чего будет возможно войти и просмотреть все Эндпоинты, формы запросов, ответов API





