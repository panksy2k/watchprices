# Docker Compose Setup for Broadband Stats Application

This directory contains the Docker Compose configuration to run the Vert.x application with MongoDB dependency.

## Prerequisites

- Docker and Docker Compose installed
- Ensure ports 8080 and 27017 are available on your host machine

## Services

The Docker Compose setup includes:

1. **MongoDB** (`mongodb`):
   - Image: `mongo:7.0`
   - Port: `27017`
   - Database: `productdb`
   - Persistent volume for data storage
   - Health check to ensure MongoDB is ready

2. **Vert.x Application** (`vertx-app`):
   - Built from the Dockerfile in the parent directory
   - Port: `8080`
   - Depends on MongoDB service
   - Environment variables for MongoDB connection
   - Health check on the products API endpoint

## Usage

### Starting the Application

Navigate to the resources directory and run:

```bash
cd resources
docker-compose up --build
```

Or run in detached mode:

```bash
docker-compose up --build -d
docker-compose up mongodb -d
```

### Stopping the Application

```bash
docker-compose down
```

### Stopping and Removing Volumesbroad

To completely clean up including the MongoDB data:

```bash
docker-compose down -v
```

## Environment Variables

The application supports the following environment variables for MongoDB configuration:

- `MONGO_HOST` - MongoDB hostname (default: `mongodb`)
- `MONGO_PORT` - MongoDB port (default: `27017`)
- `MONGO_DATABASE` - Database name (default: `productdb`)
- `MONGO_CONNECTION_STRING` - Full MongoDB connection string (overrides host/port if provided)
- `JAVA_OPTS` - Java runtime options (default: `-Xmx512m -Xms256m`)

## API Endpoints

Once running, the following endpoints are available:

- **Application**: http://localhost:8080
- **Products API**: http://localhost:8080/api/products


## Health Checks

Both services have health checks configured:

- **MongoDB**: Uses `mongosh` to ping the database
- **Vert.x App**: Checks the `/api/products` endpoint

## Volumes

- `mongodb_data`: Persistent storage for MongoDB data

## Network

All services run on the `broadband-network` bridge network, allowing them to communicate using service names as hostnames.

## Troubleshooting

### Check service status:
```bash
docker-compose ps
```

### View logs:
```bash
# All services
docker-compose logs

# Specific service
docker-compose logs vertx-app
docker-compose logs mongodb
```

### Check health:
```bash
docker-compose exec vertx-app curl -f http://localhost:8080/api/products
docker-compose exec mongodb mongosh --eval "db.adminCommand('ping')"
```

### Rebuild only the application:
```bash
docker-compose build vertx-app
docker-compose up vertx-app
```

### Rebuild frontend after changes to Vue components - which will lcompress and put the assets into frontend/dist:
```bash
cd frontend
npm run build
```

### Package through mvn -- including frontend assets - as it will copy first the assets from frontend/dist to stc/main/resources
### and then package the binary along with frontend assets:
```bash
mvn clean package -DskipTests
```
