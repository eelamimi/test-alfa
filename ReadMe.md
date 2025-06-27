# RESTful тестовое приложение

Spring Boot приложение с REST API и Swagger документацией.

## Технологии

- Java 21
- Spring Boot 3
- PostgreSQL
- Docker
- Swagger UI

## Быстрый старт

### Настройка окружения

Создайте `.env` файл в корне проекта (на основе `.env.example`).

```bash
cp .env.example .env
```

### Переменные окружения

Основные переменные (настраиваются в .env):

- `DB_URL` - URL базы данных

- `DB_NAME` - имя базы данных

- `DB_USERNAME` - пользователь БД

- `DB_PASSWORD` - пароль БД

Затем отредактируйте его (например, для docker-compose):

```env
DB_URL=jdbc:postgresql://postgres:5432/${DB_NAME}
DB_NAME=Clients
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

### Запуск приложения

```bash
docker-compose up --build
```

Приложение будет доступно на: http://localhost:8080

### Документация API

После запуска откройте Swagger UI:
http://localhost:8080/swagger-ui/index.html

## Работа с приложением

### Доступные endpoints

- `GET /api/clients` - получить список клиентов

- `POST /api/clients` - создать нового клиента

- Другие endpoints смотрите в Swagger UI

### Остановка приложения

```bash
docker-compose down
```

Для полной очистки (включая данные БД):

```bash
docker-compose down -v
```