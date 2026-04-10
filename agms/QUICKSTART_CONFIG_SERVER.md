# AGMS Centralized Configuration - Quick Start Guide

## What Changed

Your microservices now use **Spring Cloud Config Server** for centralized configuration management instead of having separate properties files in each service.

### Before (Individual Configuration)
```
authService/
  └── src/main/resources/application.properties
apiGetway/
  └── src/main/resources/application.properties
... (same for each service)
```

### After (Centralized Configuration)
```
configServer/                          ← NEW Config Server
config-repo/                           ← Central Git-based repository
  ├── authService.properties
  ├── apiGetway.properties
  ├── serviceRegistry.properties
  ... etc

Each Service:
  ├── src/main/resources/bootstrap.yml  ← NEW (points to Config Server)
  └── src/main/resources/application.properties (minimal)
```

## 🚀 Getting Started

### Step 1: Build the Config Server
```bash
cd d:\agms\agms\configServer
mvn clean install
```

### Step 2: Start the Config Server (Terminal 1)
```bash
cd d:\agms\agms\configServer
mvn spring-boot:run
```

Expected output:
```
Started ConfigServerApplication in X.XXX seconds
Server running on port 8888
```

### Step 3: Start Service Registry/Eureka (Terminal 2)
```bash
cd d:\agms\agms\serviceRegistry
mvn spring-boot:run
```

Expected output:
```
Started serviceRegistry in X.XXX seconds
Eureka Server started on port 8761
```

### Step 4: Start Other Services (Additional Terminals)
In separate terminals, run each service:

```bash
# Auth Service
cd d:\agms\agms\authService && mvn spring-boot:run

# Zone Service
cd d:\agms\agms\zoneservice && mvn spring-boot:run

# Telemetry Service
cd d:\agms\agms\telemetryservice && mvn spring-boot:run

# Automation Service
cd d:\agms\agms\automationservice && mvn spring-boot:run

# Crop Inventory Service
cd d:\agms\agms\cropInventoryService && mvn spring-boot:run

# API Gateway
cd d:\agms\agms\apiGetway && mvn spring-boot:run

# IoT Service
cd d:\agms\agms\IoTService\iot-backend && mvn spring-boot:run
```

## ✅ Verification

### Verify Config Server is serving configurations:
```bash
curl http://localhost:8888/authService/default
curl http://localhost:8888/apiGetway/default
curl http://localhost:8888/serviceRegistry/default
```

### Verify Service is registered with Eureka:
```bash
curl http://localhost:8761/
```

### Check service logs:
Each service should show:
```
Fetching config from server at: http://localhost:8888
Located environment: name=authService, profiles=[default]
```

## 📝 Managing Configurations

### Update a Configuration

1. Edit the config file:
```bash
# Example: Update database URL for authService
vim d:\agms\agms\config-repo\authService.properties
```

2. Commit the changes:
```bash
cd d:\agms\agms\config-repo
git add .
git commit -m "Update database URL for authService"
```

3. Restart the service:
```bash
# Stop the service: Ctrl+C
# Restart it: mvn spring-boot:run
```

### Add Configuration for New Environment

1. Create a new profile-specific file:
```bash
# For production environment
d:\agms\agms\config-repo\authService-prod.properties
```

2. Add your production configs and commit:
```bash
cd d:\agms\agms\config-repo
git add authService-prod.properties
git commit -m "Add production configuration for authService"
```

3. Update bootstrap.yml to use the profile:
```yaml
spring:
  cloud:
    config:
      profile: prod  # Change from 'default' to 'prod'
```

## 🔍 Troubleshooting

### Config Server won't start
```
Error: Port 8888 already in use
Solution: Kill process on port 8888 or change port in configServer/src/main/resources/application.properties
```

### Service can't connect to Config Server
```
Error: Unable to load configuration from http://localhost:8888
Solution: Ensure Config Server is running and Git repository is initialized
```

### Service shows old configuration
```
Error: Property values not updated
Solution: Services cache configurations - restart the service to refresh
```

### Git errors in Config Server logs
```
Error: Can't change to directory 'd:/agms/agms/config-repo'
Solution: Verify Git repository exists and is initialized with at least one commit
```

## 📂 Directory Structure Reference

```
d:\agms\agms\
├── configServer/                    ← Config Server source code
│   ├── pom.xml                      ← Added spring-cloud-config-server
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/agms/configserver/ConfigServerApplication.java
│   │       └── resources/
│   │           └── application.properties
│   └── CONFIG_SERVER_README.md      ← Detailed documentation
│
├── config-repo/                     ← Centralized configuration repository (Git)
│   ├── .git/
│   ├── .gitignore
│   ├── authService.properties
│   ├── apiGetway.properties
│   ├── serviceRegistry.properties
│   ├── automationservice.properties
│   ├── cropInventoryService.properties
│   ├── telemetryservice.properties
│   ├── zoneservice.properties
│   └── iot-service.properties
│
├── authService/
│   ├── pom.xml                      ← Added spring-cloud-starter-config
│   ├── src/
│   │   └── main/resources/
│   │       ├── bootstrap.yml        ← NEW (points to Config Server)
│   │       └── application.properties
│   └── HELP.md
│
├── apiGetway/
│   ├── pom.xml                      ← Added spring-cloud-starter-config
│   ├── src/
│   │   └── main/resources/
│   │       ├── bootstrap.yml        ← NEW
│   │       └── application.properties
│   └── HELP.md
│
└── ... (same pattern for other services)
```

## 🎯 Key Points

- ✅ No code changes required - only configuration management changed
- ✅ All services remain functional with same behavior
- ✅ Configuration is version-controlled in Git
- ✅ Easy to update configs without rebuilding code
- ✅ Support for multiple environments via profiles
- ✅ Single source of truth for all configurations

## 📖 Full Documentation

For detailed information, see: `d:\agms\agms\configServer\CONFIG_SERVER_README.md`

---

**Date**: 2026-04-10
**Status**: ✅ Ready to use
**Questions?**: Check CONFIG_SERVER_README.md or enable DEBUG logging
