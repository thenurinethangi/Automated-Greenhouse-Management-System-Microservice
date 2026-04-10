# Centralized Configuration Server Setup Guide

## Overview
This document describes the centralized configuration management using Spring Cloud Config Server for the AGMS microservices project.

## Architecture

### Components
1. **Config Server**: Central configuration server running on port 8888
2. **Config Repository**: Git-based configuration storage at `d:/agms/agms/config-repo`
3. **Config Clients**: All microservices configured to fetch settings from the Config Server

### File Structure
```
config-repo/
├── authService.properties
├── apiGetway.properties
├── serviceRegistry.properties
├── automationservice.properties
├── cropInventoryService.properties
├── telemetryservice.properties
├── zoneservice.properties
└── iot-service.properties
```

## Startup Order

**IMPORTANT**: Start services in this order to avoid connection errors:

1. **Config Server** (Port 8888)
   ```bash
   cd d:\agms\agms\configServer
   mvn spring-boot:run
   ```

2. **Service Registry/Eureka** (Port 8761)
   ```bash
   cd d:\agms\agms\serviceRegistry
   mvn spring-boot:run
   ```

3. **Other Services** (Can be started in any order):
   - Auth Service (8085)
   - Zone Service (8081)
   - Telemetry Service (8082)
   - Automation Service (8083)
   - Crop Inventory Service (8084)
   - API Gateway (8090)
   - IoT Service (8080)

## Configuration Files

All configuration files are stored in `d:\agms\agms\config-repo` and are managed by Git:

### Each Service Configuration Includes:
- **Application Name**: Unique identifier for the service
- **Server Port**: Port on which the service runs
- **Database Settings**: Connection URL, username, password
- **HikariCP Settings**: Connection pool configuration
- **JPA/Hibernate Settings**: ORM configuration
- **Tomcat Settings**: Server configuration
- **JWT Settings**: Token expiration and secret keys
- **Eureka Settings**: Service discovery configuration
- **IoT Settings**: External service URLs and credentials

## How It Works

1. When a microservice starts, it reads `bootstrap.yml`
2. `bootstrap.yml` points to the Config Server at `http://localhost:8888`
3. Config Server retrieves the service's configuration from Git repository
4. Service loads the centralized configuration properties
5. If Config Server is unavailable, the service fails fast (after retries)

## Configuration Updates

To update configurations:

1. Edit the corresponding `.properties` file in `d:\agms\agms\config-repo`
2. Commit changes to Git:
   ```bash
   cd d:\agms\agms\config-repo
   git add .
   git commit -m "Update configuration"
   ```
3. Services will need to be restarted to pick up new configurations (or use `/actuator/refresh` endpoint if enabled)

## Key Bootstrap Configuration

Each service has a `bootstrap.yml` file that contains:

```yaml
spring:
  cloud:
    config:
      uri: http://localhost:8888           # Config Server URL
      name: serviceName                     # Service name (must match config filename)
      profile: default                      # Profile (for environment-specific configs)
      fail-fast: true                       # Fail if Config Server is unavailable
      retry:
        initial-interval: 1000              # Retry configuration
        max-interval: 2000
        max-attempts: 6
```

## Troubleshooting

### Service fails to start with "Connect refused"
- Ensure Config Server is running on port 8888
- Check network connectivity
- Verify Config Server logs: `INFO org.springframework.cloud.config.server.git`

### Configuration not updating
- Changes require service restart
- Verify Git commit is successful
- Check that service name in `bootstrap.yml` matches the config filename

### Config Server Git errors
- Ensure Git repository is initialized at `d:/agms/agms/config-repo`
- Check Git repository has at least one commit
- Verify file path in `application.properties` is correct

## Security Considerations

Currently, the Config Server has no authentication. For production:

1. Add Spring Security to Config Server
2. Implement client authentication
3. Use encrypted configuration values
4. Secure the Git repository
5. Use HTTPS for Config Server endpoints

## Environment-Specific Configurations

To support multiple environments (dev, staging, production):

1. Create profile-specific files: `serviceName-dev.properties`, `serviceName-prod.properties`
2. Update `bootstrap.yml` to specify the profile: `profile: dev`
3. Config Server will merge base and profile-specific configurations

## Adding New Services

To add a new microservice to the centralized config:

1. Create new configuration file in `config-repo`: `newService.properties`
2. Add all required properties
3. Commit to Git
4. Add Config Client dependencies to the new service's `pom.xml`
5. Create `bootstrap.yml` in service's resources pointing to Config Server
6. Start the service (ensure Config Server is running first)

## Commands Reference

### Start Config Server
```bash
cd d:\agms\agms\configServer
mvn clean install
mvn spring-boot:run
```

### Access Configuration via REST
```bash
curl http://localhost:8888/authService/default
curl http://localhost:8888/serviceRegistry/default
```

### Git Commit Configuration Changes
```bash
cd d:\agms\agms\config-repo
git status
git add .
git commit -m "Your message"
git log
```

## Benefits of Centralized Configuration

✓ **Single Source of Truth**: All configurations in one location
✓ **Easy Updates**: Change properties without code rebuild
✓ **Version Control**: Track configuration changes via Git
✓ **Environment Management**: Support multiple environments with profiles
✓ **Security**: Centralized credential management (with encryption)
✓ **Consistency**: Ensure all services have correct configurations
✓ **Scalability**: Easy to add new services and configurations

---

**Last Updated**: 2026-04-10
**Config Server Version**: 2023.0.0
**Spring Boot Version**: 3.2.0
