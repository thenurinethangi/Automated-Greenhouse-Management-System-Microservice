# Automated Greenhouse Management System (AGMS)
## A Microservice-Based Application for Modern Agricultural Automation

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Learning Outcomes](#learning-outcomes)
3. [Business Scenario](#business-scenario)
4. [Technology Stack](#technology-stack)
5. [Architecture](#architecture)
6. [Prerequisites](#prerequisites)
7. [Installation & Setup](#installation--setup)
8. [Running the Services](#running-the-services)
9. [Service Details](#service-details)
10. [Communication Patterns](#communication-patterns)
11. [Security Implementation](#security-implementation)
12. [Configuration Management](#configuration-management)
13. [API Endpoints](#api-endpoints)
14. [System Workflow](#system-workflow)
15. [Testing](#testing)

---

## Project Overview

The **Automated Greenhouse Management System (AGMS)** is a cloud-native, microservice-based platform designed to revolutionize agricultural greenhouse management through intelligent automation. Instead of traditional manual management, AGMS leverages real-time IoT data integration and automated rule engines to maintain optimal growing conditions across multiple zones.

### Key Objectives
- **Zone Management**: Allow farmers to define distinct greenhouse sections with custom environmental thresholds
- **Real-Time Data Ingestion**: Fetch live telemetry data (Temperature/Humidity) from external IoT devices
- **Automated Control**: Trigger actions based on intelligent rule engines (e.g., activate fan if temperature exceeds threshold)
- **Inventory Tracking**: Monitor crop lifecycle from seedling to harvest
- **Secure Access**: Centralized authentication and authorization across all services

---

## Learning Outcomes

By completing this project, developers will master:

✓ **Distributed Systems Architecture**: Design and implement microservices using Spring Boot and Spring Cloud  
✓ **Service Discovery**: Utilize Netflix Eureka for dynamic service registration  
✓ **API Routing & Gateway Pattern**: Configure Spring Cloud Gateway as a central entry point  
✓ **Security**: Implement JWT-based authorization at the Gateway level  
✓ **Inter-Service Communication**: Use OpenFeign for synchronous HTTP communication  
✓ **Centralized Configuration**: Manage distributed properties via Spring Cloud Config Server  
✓ **Polyglot Development**: Integrate services built with different technology stacks

---

## Business Scenario

### Problem Statement
Traditional greenhouse management is inherently:
- **Manual & Error-Prone**: Heavy reliance on human intervention leads to oversight
- **Resource-Inefficient**: Inconsistent environmental control wastes water, electricity, and nutrients
- **Reactive vs. Proactive**: Climate fluctuations often go unnoticed until crop damage occurs

### Proposed Solution
AGMS provides a **data-driven, automated platform** that:
1. Continuously monitors environmental conditions via external IoT sensors
2. Processes telemetry through intelligent rule engines
3. Automatically triggers corrective actions to maintain optimal conditions
4. Provides farmers with real-time visibility and control

### Key Stakeholders
- **Farmers**: Define zones, set thresholds, monitor crop health
- **IoT Sensors**: Provide real-time temperature and humidity readings
- **System Administrators**: Monitor microservice health and performance

---

## Technology Stack

### Infrastructure & Services
| Component | Technology | Purpose |
|-----------|-----------|---------|
| **Framework** | Spring Boot 2.x / 3.x | Microservice development framework |
| **Service Discovery** | Netflix Eureka | Dynamic service registration & discovery |
| **API Gateway** | Spring Cloud Gateway | Central request routing & security |
| **Configuration** | Spring Cloud Config | Centralized property management |
| **Inter-Service Communication** | OpenFeign / RestTemplate | Synchronous HTTP communication |
| **Security** | JWT (JSON Web Token) | Bearer token-based authentication |

### Domain Microservices
Each microservice has **freedom in technology selection**:
- **Frameworks**: Spring Boot (Java), Node.js (Express/NestJS), Python (Flask/FastAPI), Go, etc.
- **Databases**: MySQL, PostgreSQL, MongoDB, Redis (Polyglot Persistence)
- **External Integration**: Live IoT Provider API (Reactive WebFlux)

### Development Tools
- **Postman**: API testing and documentation
- **Maven**: Build automation and dependency management
- **Docker**: Containerization (for IoT Backend Service)
- **Git**: Configuration repository for Spring Cloud Config

---

## Architecture

### Microservices Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                     External IoT Provider                        │
│              (Live Temperature/Humidity Data API)                │
└────────────────────────────┬────────────────────────────────────┘
                             │ (Bearer Token)
          ┌──────────────────┴──────────────────┐
          │                                      │
    [Sensor Telemetry]                    [User Auth]
    (Every 10 seconds)
          │                                      │
          ├──────────────────┬──────────────────┤
          │                  │                  │
    ┌─────▼────────────────────────────────────▼──────┐
    │         Spring Cloud Gateway (Port 8080)        │
    │  • JWT Validation & Route Authorization        │
    │  • Central Entry Point for All External Clients │
    └──────────────────────────┬──────────────────────┘
              │                 │                 │              │
              │                 │                 │              │
        ┌─────▼─────┐    ┌─────▼──────┐   ┌─────▼──────┐ ┌────▼──────┐
        │    Zone    │    │  Sensor    │   │ Automation │ │   Crop    │
        │ Management │    │ Telemetry  │   │  & Control │ │ Inventory │
        │  Service   │    │  Service   │   │  Service   │ │  Service  │
        │ :8081      │    │  :8082     │   │   :8083    │ │   :8084   │
        └────────────┘    └────────────┘   └────────────┘ └───────────┘
             │                  │                │              │
             └──────────────────┴────────────────┴──────────────┘
                                 │
                    ┌────────────┴────────────┐
                    │                         │
            ┌───────▼──────────┐    ┌────────▼─────────────┐
            │  Service Registry│    │ Configuration Server │
            │    (Eureka)      │    │  (Spring Cloud Config)│
            │   :8761          │    │      :8888           │
            └──────────────────┘    └──────────────────────┘
```

### Communication Flows

#### 1. **Service Discovery**
- All domain microservices self-register with Eureka during startup
- Gateway and services discover each other dynamically (no hardcoded URLs)

#### 2. **Request Flow**
```
Client Request
    ↓
Spring Cloud Gateway (8080) — JWT Validation
    ↓
Route to specific microservice based on path
    ↓
Service processes request
    ↓
Service may call other services via OpenFeign
    ↓
Response returns to client
```

#### 3. **Telemetry Pipeline**
```
External IoT API
    ↓
Sensor Telemetry Service (fetches every 10 seconds)
    ↓
Forwards to Automation Service
    ↓
Automation Service queries Zone Service for thresholds
    ↓
Rule Engine evaluates conditions
    ↓
Log actions (TURN_FAN_ON, TURN_HEATER_ON, etc.)
```

---

## Prerequisites

### System Requirements
- **Java**: JDK 11 or higher (for Spring Boot services)
- **Maven**: 3.6.0 or higher (build tool)
- **Node.js**: 14+ (optional, if implementing services in Node.js/Express)
- **Python**: 3.8+ (optional, if implementing services in Python)
- **Git**: For cloning and managing configuration repository
- **Postman**: For API testing (optional but recommended)
- **Docker**: For containerized deployments (optional)

### Port Requirements (Ensure these ports are available)
- `8761` - Service Registry (Eureka)
- `8888` - Configuration Server
- `8080` - API Gateway
- `8081` - Zone Management Service
- `8082` - Sensor Telemetry Service
- `8083` - Automation & Control Service
- `8084` - Crop Inventory Service

---

## Installation & Setup

### Step 1: Clone the Repository
```bash
git clone <repository-url>
cd agms
```

### Step 2: Verify Project Structure
```
agms/
├── serviceRegistry/          # Eureka Service Discovery
├── apiGetway/               # Spring Cloud Gateway
├── telemetryservice/        # Configuration Server
├── authService/             # Authentication Service
├── zoneservice/             # Zone Management Service
├── automationservice/       # Automation & Control Service
├── cropInventoryService/    # Crop Inventory Service
└── IoTService/
    └── iot-backend/         # IoT Backend (if applicable)
```

### Step 3: Configure External IoT Provider
Update credentials in the centralized configuration for accessing:
- **Base URL**: `http://104.211.95.241:8080/api`
- **Authentication**: JWT Bearer Token (obtained from `/auth/login`)

---

## Running the Services

### ⚠️ CRITICAL: Service Startup Order

**Services MUST be started in this specific order:**

#### **Phase 1: Infrastructure Services (Start First)**

##### 1️⃣ **Start Eureka Service Registry (Port: 8761)**
```bash
cd serviceRegistry
mvn clean install
mvn spring-boot:run
```
**Output**: `Eureka Dashboard available at http://localhost:8761`

⏳ Wait 5-10 seconds to ensure Eureka is fully initialized.

---

##### 2️⃣ **Start Configuration Server (Port: 8888)**
```bash
cd telemetryservice
mvn clean install
mvn spring-boot:run
```
**Output**: Config Server starts and connects to Eureka.

⏳ Wait for: `"Registering application TELEMETRY-SERVICE with Eureka"`

> **Note**: This service doubles as the Config Server. Ensure Git repository is accessible for centralized configuration files.

---

##### 3️⃣ **Start API Gateway (Port: 8080)**
```bash
cd apiGetway
mvn clean install
mvn spring-boot:run
```
**Output**: Gateway starts, registers with Eureka, and begins accepting external requests.

⏳ Verify: Check Eureka dashboard (http://localhost:8761) - should show `APIGATEWAY` instance.

---

#### **Phase 2: Domain Microservices (Start After Infrastructure)**

##### 4️⃣ **Start Zone Management Service (Port: 8081)**
```bash
cd zoneservice
mvn clean install
mvn spring-boot:run
```

---

##### 5️⃣ **Start Sensor Telemetry Service (Port: 8082)**
```bash
cd automationservice
mvn clean install
mvn spring-boot:run
```

---

##### 6️⃣ **Start Automation & Control Service (Port: 8083)**
```bash
cd automationservice
mvn clean install
mvn spring-boot:run
```

---

##### 7️⃣ **Start Crop Inventory Service (Port: 8084)**
```bash
cd cropInventoryService
mvn clean install
mvn spring-boot:run
```

---

### Verification Checklist

After all services are running, verify:

1. **Eureka Dashboard**: http://localhost:8761
   - ✓ APIGATEWAY
   - ✓ ZONE-SERVICE
   - ✓ SENSOR-TELEMETRY-SERVICE
   - ✓ AUTOMATION-SERVICE
   - ✓ CROP-INVENTORY-SERVICE

2. **Gateway Accessibility**: 
   ```bash
   curl http://localhost:8080/api
   ```

3. **Check Service Logs**: No errors or exceptions

---

## Service Details

### 1. Service Registry (Eureka) - Port 8761
**Location**: `serviceRegistry/`

**Purpose**: Central service discovery mechanism enabling dynamic service location without hardcoded URLs.

**Key Features**:
- Dynamic service registration
- Health check monitoring
- Service instance load balancing support

**Dashboard**: http://localhost:8761

---

### 2. Configuration Server - Port 8888
**Location**: `telemetryservice/` (dual-purpose service)

**Purpose**: Centralized management of all configuration properties for the distributed system.

**Key Features**:
- Git-backed configuration repository
- Dynamic property refresh without service restart
- Environment-specific configurations (dev, staging, prod)
- Support for encrypted sensitive data

**Typical Configuration Files** (stored in Git):
```
configs/
├── application.yml (shared properties)
├── zone-service.yml
├── sensor-service.yml
├── automation-service.yml
├── crop-inventory-service.yml
```

---

### 3. API Gateway - Port 8080
**Location**: `apiGetway/`

**Purpose**: Central entry point routing all external client requests to appropriate microservices.

**Key Responsibilities**:
- ✓ Route requests to correct microservice (`/api/zones/**` → Zone Service)
- ✓ JWT token validation (see [Security Implementation](#security-implementation))
- ✓ Request/Response transformation
- ✓ Cross-cutting concerns (logging, metrics)

**Route Configuration Example**:
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: zone-service
          uri: lb://ZONE-SERVICE
          predicates:
            - Path=/api/zones/**
        - id: sensor-service
          uri: lb://SENSOR-TELEMETRY-SERVICE
          predicates:
            - Path=/api/sensors/**
        - id: automation-service
          uri: lb://AUTOMATION-SERVICE
          predicates:
            - Path=/api/automation/**
        - id: crop-service
          uri: lb://CROP-INVENTORY-SERVICE
          predicates:
            - Path=/api/crops/**
```

---

### 4. Zone Management Service - Port 8081
**Location**: `zoneservice/`

**Purpose**: Manage greenhouse zones and their environmental thresholds.

**API Endpoints**:
```
POST   /api/zones               # Create new zone (registers device with external IoT)
GET    /api/zones/{id}         # Fetch specific zone details
PUT    /api/zones/{id}         # Update temperature thresholds
DELETE /api/zones/{id}         # Remove zone
```

**Database**: [Your choice - MySQL/PostgreSQL/MongoDB]

**External Integration**: 
- Calls external IoT API to register device
- Stores returned `deviceId` for future telemetry queries

**Request Example**:
```json
POST /api/zones
{
  "name": "Tomato Zone",
  "minTemp": 18.0,
  "maxTemp": 28.0,
  "minHumidity": 50.0,
  "maxHumidity": 80.0
}
```

---

### 5. Sensor Telemetry Service - Port 8082
**Location**: `automationservice/`

**Purpose**: Act as the "Data Bridge" between external IoT provider and internal automation system.

**Key Responsibilities**:
- **Scheduled Fetcher**: Runs every 10 seconds
- **Data Pipeline**: Fetches latest T/H readings from external IoT API
- **Forward Data**: Immediately sends to Automation Service via POST request

**API Endpoints**:
```
GET /api/sensors/latest    # Debug endpoint - returns last fetched reading
```

**Critical Implementation**:
```java
@Scheduled(fixedRate = 10000)  // Every 10 seconds
public void fetchAndForwardTelemetry() {
    // 1. Authenticate with external IoT API (Bearer Token)
    String token = getValidToken();
    
    // 2. Fetch latest telemetry from external provider
    TelemetryData data = externalIoTClient.getLatestTelemetry(token);
    
    // 3. Forward to Automation Service
    automationServiceClient.processTelemetry(data);
}
```

---

### 6. Automation & Control Service - Port 8083
**Location**: `automationservice/`

**Purpose**: The system's "Brain" - rule engine that makes intelligent decisions based on sensor data.

**Key Responsibilities**:
- **Decision Making**: Receives telemetry from Sensor Service
- **Threshold Fetching**: Queries Zone Service for min/max limits (OpenFeign)
- **Rule Evaluation**: Compares current readings against thresholds
- **Action Logging**: Records triggered actions for farmer visibility

**API Endpoints**:
```
POST /api/automation/process      # Internal endpoint (receives telemetry)
GET  /api/automation/logs         # List triggered actions
```

**Rule Engine Logic**:
```
If currentTemp > maxTemp:
    → Log action: "TURN_FAN_ON"
    → Call external IoT API to activate fan (optional)

If currentTemp < minTemp:
    → Log action: "TURN_HEATER_ON"
    → Call external IoT API to activate heater (optional)

If humidity > maxHumidity:
    → Log action: "OPEN_VENTILATION"

If humidity < minHumidity:
    → Log action: "ACTIVATE_MISTER"
```

**Inter-Service Call Pattern** (OpenFeign):
```java
@FeignClient("ZONE-SERVICE")
public interface ZoneServiceClient {
    @GetMapping("/api/zones/{id}")
    ZoneDTO getZoneThresholds(@PathVariable String id);
}
```

---

### 7. Crop Inventory Service - Port 8084
**Location**: `cropInventoryService/`

**Purpose**: Track crop lifecycle and inventory management.

**API Endpoints**:
```
POST /api/crops                  # Register new crop batch
PUT  /api/crops/{id}/status     # Update lifecycle stage
GET  /api/crops                 # View current inventory
```

**Crop Lifecycle States**:
- `SEEDLING` - Initial planting stage
- `VEGETATIVE` - Growth phase
- `HARVESTED` - Ready for harvest

**Data Model**:
```json
{
  "cropId": "CROP-001",
  "cropName": "Tomato",
  "zoneId": "Zone-A",
  "batchDate": "2026-02-20",
  "status": "VEGETATIVE",
  "quantity": 150,
  "expectedHarvestDate": "2026-04-20"
}
```

---

## Communication Patterns

### 1. Synchronous Inter-Service Communication (OpenFeign)

**Purpose**: Services communicate with each other via HTTP to fetch real-time data.

**Example: Automation Service → Zone Service**

```java
// Define Feign Client in Automation Service
@FeignClient("ZONE-SERVICE")
public interface ZoneServiceClient {
    
    @GetMapping("/api/zones/{zoneId}")
    ZoneThresholds getThresholds(@PathVariable String zoneId);
}

// Use in business logic
@Service
public class RuleEngine {
    
    @Autowired
    private ZoneServiceClient zoneClient;
    
    public void processTelemetry(TelemetryData data) {
        // Fetch thresholds synchronously
        ZoneThresholds thresholds = zoneClient.getThresholds(data.getZoneId());
        
        // Evaluate rules
        if (data.getTemperature() > thresholds.getMaxTemp()) {
            logAction("TURN_FAN_ON");
        }
    }
}
```

**Benefits**:
- ✓ Simple and intuitive (RPC-style)
- ✓ Guaranteed delivery and response
- ✓ Synchronous transaction support

**Drawbacks**:
- ✗ Service coupling
- ✗ Cascading failures if called service is down

---

### 2. Asynchronous Service-to-Service Communication (Push Pattern)

**Purpose**: Sensor Service pushes data to Automation Service without waiting for response.

```java
// Sensor Telemetry Service
@Service
public class TelemetryFetcher {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Scheduled(fixedRate = 10000)
    public void fetchAndForward() {
        TelemetryData data = fetchFromExternalAPI();
        
        // Fire-and-forget POST to Automation Service
        restTemplate.postForObject(
            "http://AUTOMATION-SERVICE/api/automation/process",
            data,
            String.class
        );
    }
}
```

---

### 3. Calling External IoT Provider

**Authentication**:
```java
@Service
public class ExternalIoTClient {
    
    private String accessToken;
    private Long tokenExpiry;
    
    public String obtainToken() {
        // POST /auth/login
        LoginRequest request = new LoginRequest("username", "password");
        LoginResponse response = restTemplate.postForObject(
            "http://104.211.95.241:8080/api/auth/login",
            request,
            LoginResponse.class
        );
        this.accessToken = response.getAccessToken();
        this.tokenExpiry = response.getExpiresIn();
        return accessToken;
    }
    
    public TelemetryData fetchTelemetry(String deviceId) {
        // Ensure valid token
        if (isTokenExpired()) {
            obtainToken();
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        
        // GET /devices/telemetry/{deviceId}
        ResponseEntity<TelemetryData> response = restTemplate.exchange(
            "http://104.211.95.241:8080/api/devices/telemetry/" + deviceId,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            TelemetryData.class
        );
        
        return response.getBody();
    }
}
```

---

## Security Implementation

### Overview

**Security Layers**:
1. **Gateway-Level JWT Validation**: All external requests validated before reaching microservices
2. **Service-Level Authentication**: Services authenticate with external IoT API
3. **Token Management**: Handle token refresh and expiration

---

### Layer 1: API Gateway JWT Validation

**Purpose**: Centralized authentication - no need for each service to validate tokens independently.

**Implementation** (Spring Cloud Gateway Filter):

```java
@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory {
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // Extract Bearer token
            String token = extractToken(request);
            
            if (token == null) {
                return sendError(exchange, HttpStatus.UNAUTHORIZED, 
                    "Missing authorization header");
            }
            
            try {
                // Validate token
                if (!tokenProvider.validateToken(token)) {
                    return sendError(exchange, HttpStatus.UNAUTHORIZED, 
                        "Invalid or expired token");
                }
                
                // Add user info to request for downstream services
                String userId = tokenProvider.getUserId(token);
                request = request.mutate()
                    .header("X-User-Id", userId)
                    .build();
                
            } catch (Exception e) {
                return sendError(exchange, HttpStatus.UNAUTHORIZED, 
                    "Token validation failed");
            }
            
            return chain.filter(exchange.mutate().request(request).build());
        };
    }
    
    private String extractToken(ServerHttpRequest request) {
        return request.getHeaders().getFirst("Authorization")
            .replace("Bearer ", "");
    }
}
```

**Route Configuration with Security**:
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: zone-service
          uri: lb://ZONE-SERVICE
          predicates:
            - Path=/api/zones/**
          filters:
            - name: JwtAuthenticationFilter
              args:
                requiredRole: USER

        - id: sensors-service
          uri: lb://SENSOR-TELEMETRY-SERVICE
          predicates:
            - Path=/api/sensors/**
          filters:
            - name: JwtAuthenticationFilter
              args:
                requiredRole: USER
```

**Request Flow with Security**:
```
Client Request (with Bearer token in header)
    ↓
API Gateway receives request
    ↓
JwtAuthenticationFilter intercepts
    ↓
Extract & Validate token (check signature, expiry)
    ↓
If valid: Continue to microservice
If invalid: Return 401 Unauthorized
    ↓
Microservice receives authenticated request + X-User-Id header
    ↓
Process and respond
```

**Example Client Request**:
```bash
curl -X GET http://localhost:8080/api/zones/zone-123 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

### Layer 2: Microservice Authentication with External IoT

**Purpose**: Services authenticate with external IoT provider to access sensor data.

**Token Management Strategy**:
```java
@Service
public class IoTTokenManager {
    
    private String accessToken;
    private OffsetDateTime tokenExpiresAt;
    
    public String getValidToken() {
        // Check if current token is valid
        if (accessToken != null && 
            OffsetDateTime.now().isBefore(tokenExpiresAt.minusMinutes(5))) {
            return accessToken;
        }
        
        // Refresh token if expired or near expiry
        refreshToken();
        return accessToken;
    }
    
    private void refreshToken() {
        try {
            LoginResponse response = externalIoTClient.login(
                new LoginRequest("username", "password")
            );
            this.accessToken = response.getAccessToken();
            this.tokenExpiresAt = response.getTokenExpiresAt();
        } catch (Exception e) {
            logger.error("Failed to obtain IoT token", e);
            throw new SecurityException("Cannot authenticate with IoT provider");
        }
    }
    
    // Used by any service needing to call external API
    public HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getValidToken());
        return headers;
    }
}
```

---

### Layer 3: Request/Response Flow with Authentication

```
Farmer's Client App
    │
    ├─ POST /api/zones (with JWT token)
    │
    └─→ API Gateway
         ├─ JwtAuthenticationFilter validates token
         ├─ Extracts userId, adds X-User-Id header
         │
         └─→ Zone Service
              ├─ Receives authenticated request
              ├─ Needs to register device with external IoT
              │
              └─→ ExternalIoTClient.registerDevice()
                   ├─ Gets valid Bearer token (auto-refresh if needed)
                   ├─ POST to external IoT /devices
                   ├─ Stores returned deviceId in database
                   │
                   └─→ Returns to Zone Service
                       └─→ Returns to Gateway
                           └─→ Returns to Client
```

---

## Configuration Management

### Overview

Spring Cloud Config Server provides centralized management of all configuration properties across the entire AGMS distributed system. This eliminates configuration drift and ensures consistency across all microservices.

### ✨ Implementation Status: ✅ COMPLETED

The centralized configuration system is now fully implemented and operational.

---

### Architecture

```
Git Repository (Source of Truth)
    ↓ (Central Config Storage)
Spring Cloud Config Server (Port 8888)
    ↓ (REST API)
Microservices (Config Clients with bootstrap.yml)
    ├─ Auth Service (8085)
    ├─ Zone Service (8081)
    ├─ Telemetry Service (8082)
    ├─ Automation Service (8083)
    ├─ Crop Inventory Service (8084)
    ├─ API Gateway (8090)
    ├─ IoT Service (8080)
    └─ Service Registry (8761)
```

---

### Configuration Repository Structure

**Centralized Configuration Storage**:
```
config-repo/                          # Git-based configuration repository
├── .gitignore
├── authService.properties            # Auth Service configuration
├── apiGetway.properties              # API Gateway configuration
├── serviceRegistry.properties        # Eureka Server configuration
├── automationservice.properties      # Automation Service configuration
├── cropInventoryService.properties   # Crop Inventory Service configuration
├── telemetryservice.properties       # Telemetry Service configuration
├── zoneservice.properties            # Zone Service configuration
└── iot-service.properties            # IoT Service configuration
```

**Profile-Specific Configurations** (can be added for environments):
```
authService-dev.properties            # Development environment
authService-prod.properties           # Production environment
authService-staging.properties        # Staging environment
... (same pattern for other services)
```


### Configuration Files Content

Each service configuration file contains all necessary properties for that service:

**authService.properties** (Example):
```properties
spring.application.name=authService
server.port=8085

# Database Configuration
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/authdb
spring.datasource.username=root
spring.datasource.password=Ijse@1234

# Connection Pool Settings (HikariCP)
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.maximum-pool-size=20

# JPA/Hibernate Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT Configuration
accessToken.expiration=86000000
secretKey1=MUZ21oHLiPPjCx8TbFBwp7tZA7mjhyZy3iUC1YItKUb
secretKey2=joLo5dOLMXDGEaOvctN0w0YmENKBnVacSDLWET8WEEl

# Eureka Service Discovery
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eureka.client.register-with-eureka=true
```

**zoneservice.properties** (Example):
```properties
spring.application.name=zoneservice
server.port=8081

# Database
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/zonedb
spring.datasource.username=root
spring.datasource.password=Ijse@1234

# IoT External Service
iot.username=thenuri
iot.password=1234567
iot.base-url=http://localhost:8080/api

# Service Discovery
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eureka.client.register-with-eureka=true
```

---

### Client Configuration

**Each microservice uses bootstrap.yml** to connect to the Config Server:

```yaml
# bootstrap.yml (present in each service's src/main/resources/)
spring:
  cloud:
    config:
      uri: http://localhost:8888              # Config Server URL
      name: authService                       # Must match config filename
      profile: default                        # default, dev, prod, etc.
      fail-fast: true                         # Fail if Config Server unavailable
      retry:
        initial-interval: 1000                # Milliseconds
        max-interval: 2000
        max-attempts: 6                       # Retry 6 times before failing
```

#### Startup Sequence
1. Service starts and loads `bootstrap.yml`
2. `bootstrap.yml` points to Config Server at `http://localhost:8888`
3. Config Server is contacted with service name (e.g., `authService`)
4. Config Server retrieves `authService.properties` from Git repository
5. Configuration is loaded and applied to the service
6. Service continues normal initialization

---

### Supported Configurations Per Service

#### Common Across All Services
- `spring.application.name`: Unique service identifier
- `server.port`: Service port
- `spring.datasource.*`: Database connection settings
- `spring.jpa.*`: JPA/Hibernate configuration
- `eureka.client.*`: Service discovery settings
- `server.tomcat.*`: Tomcat server settings

#### Service-Specific
- **Auth Service**: JWT secret keys, token expiration
- **API Gateway**: Route definitions, JWT configuration
- **Telemetry Service**: IoT sensor URLs, credentials
- **Automation Service**: Rule engine settings, service URLs
- **Zone Service**: IoT credentials, base URLs
- **IoT Service**: R2DBC configuration, JWT secrets

---

### How to Update Configurations

#### Method 1: Direct File Update
```bash
# 1. Navigate to config repository
cd d:\agms\agms\config-repo

# 2. Edit the service configuration
vim authService.properties   # or use your editor

# 3. Commit to Git
git add authService.properties
git commit -m "Update database credentials for authService"
```

#### Method 2: Using Git Commands
```bash
git status                                    # Check changes
git diff authService.properties               # Review changes
git add .
git commit -m "Configuration updates"
git log --oneline                            # View commit history
```

#### Refresh Service After Update
```bash
# Option 1: Restart the service
# Stop the service (Ctrl+C) and restart
mvn spring-boot:run

# Option 2: Use /actuator/refresh endpoint (if enabled)
curl -X POST http://localhost:8085/actuator/refresh
```

---

### Environment-Specific Configurations

To support multiple environments (development, staging, production):

#### 1. Create Profile-Specific Files
```
config-repo/
├── authService.properties          # Default/shared config
├── authService-dev.properties      # Development overrides
├── authService-staging.properties  # Staging overrides
└── authService-prod.properties     # Production config
```

#### 2. Update bootstrap.yml Profile
```yaml
spring:
  cloud:
    config:
      uri: http://localhost:8888
      name: authService
      profile: prod                  # Change this for different environments
```

#### 3. Configuration Priority (Highest to Lowest)
1. Environment-specific profile (e.g., `authService-prod.properties`)
2. Default profile (e.g., `authService.properties`)
3. Service defaults in code

---

### Getting Started with Config Server

For quick setup and detailed instructions, see:

---

- 📖 [Quick Start Guide](./QUICKSTART_CONFIG_SERVER.md)
- 📘 [Detailed Configuration Documentation](./agms/configServer/CONFIG_SERVER_README.md)

---

### Quick Reference Commands

**Start Config Server**:
```bash
cd d:\agms\agms\configServer
mvn clean install
mvn spring-boot:run
```

**Test Config Server is Running**:
```bash
# Fetch authService configuration
curl http://localhost:8888/authService/default

# Should return JSON with all properties for authService
```

**Start All Services** (in order):
```bash
# Terminal 1: Config Server
cd d:\agms\agms\configServer && mvn spring-boot:run

# Terminal 2: Service Registry
cd d:\agms\agms\serviceRegistry && mvn spring-boot:run

# Terminals 3-10: Other services (any order)
cd d:\agms\agms\authService && mvn spring-boot:run
cd d:\agms\agms\zoneservice && mvn spring-boot:run
cd d:\agms\agms\telemetryservice && mvn spring-boot:run
cd d:\agms\agms\automationservice && mvn spring-boot:run
cd d:\agms\agms\cropInventoryService && mvn spring-boot:run
cd d:\agms\agms\apiGetway && mvn spring-boot:run
cd d:\agms\agms\IoTService\iot-backend && mvn spring-boot:run
```

**Verify Services are Registered**:
```bash
curl http://localhost:8761/  # Eureka Dashboard
```

**View Git Commit History of Configurations**:
```bash
cd d:\agms\agms\config-repo
git log --oneline             # Show commits
git diff HEAD~1               # Show latest changes
```

---

### Key Benefits

✅ **Single Source of Truth**: All configurations in one Git repository  
✅ **Version Control**: Track all configuration changes  
✅ **No Code Rebuild Required**: Update configs and restart service  
✅ **Multi-Environment Support**: Dev, staging, production profiles  
✅ **Consistency**: Ensure all services have correct configurations  
✅ **Easy Rollback**: Revert configurations via Git  
✅ **Scalability**: Simple to add new services  

---

### API Endpoints

### Zone Management Service (Port 8081)

```
POST /api/zones
  Description: Create new zone + register device with external IoT
  Authorization: Bearer {token}
  Request:
    {
      "name": "Tomato Zone",
      "minTemp": 18.0,
      "maxTemp": 28.0,
      "minHumidity": 50.0,
      "maxHumidity": 80.0
    }
  Response:
    {
      "zoneId": "ZONE-001",
      "name": "Tomato Zone",
      "minTemp": 18.0,
      "maxTemp": 28.0,
      "deviceId": "b751b8c9-644a-484c-ba3f-be63f9b27ad0"
    }

GET /api/zones/{id}
  Description: Fetch specific zone details
  Authorization: Bearer {token}
  Response:
    {
      "zoneId": "ZONE-001",
      "name": "Tomato Zone",
      "minTemp": 18.0,
      "maxTemp": 28.0,
      "deviceId": "b751b8c9-644a-484c-ba3f-be63f9b27ad0"
    }

PUT /api/zones/{id}
  Description: Update zone thresholds
  Authorization: Bearer {token}
  Request:
    {
      "minTemp": 16.0,
      "maxTemp": 30.0
    }

DELETE /api/zones/{id}
  Description: Remove zone
  Authorization: Bearer {token}
```

### Sensor Telemetry Service (Port 8082)

```
GET /api/sensors/latest
  Description: Debug endpoint - returns last fetched telemetry
  Authorization: Bearer {token}
  Response:
    {
      "deviceId": "b751b8c9-644a-484c-ba3f-be63f9b27ad0",
      "zoneId": "ZONE-001",
      "temperature": 23.81,
      "humidity": 55.09,
      "capturedAt": "2026-02-22T08:31:39Z"
    }
```

### Automation & Control Service (Port 8083)

```
POST /api/automation/process
  Description: Internal endpoint - receives telemetry from Sensor Service
  Content-Type: application/json
  Request:
    {
      "deviceId": "b751b8c9-644a-484c-ba3f-be63f9b27ad0",
      "zoneId": "ZONE-001",
      "temperature": 23.81,
      "humidity": 55.09,
      "capturedAt": "2026-02-22T08:31:39Z"
    }

GET /api/automation/logs
  Description: Retrieve triggered actions
  Authorization: Bearer {token}
  Query Parameters:
    - zoneId: Filter by zone
    - actionType: Filter by action (TURN_FAN_ON, TURN_HEATER_ON, etc.)
    - limit: Number of records (default: 50)
  Response:
    [
      {
        "logId": "LOG-001",
        "zoneId": "ZONE-001",
        "actionType": "TURN_FAN_ON",
        "reason": "Temperature exceeded threshold (28.5°C > 28°C)",
        "timestamp": "2026-02-22T08:32:00Z"
      },
      {
        "logId": "LOG-002",
        "zoneId": "ZONE-001",
        "actionType": "TURN_HEATER_ON",
        "reason": "Temperature below threshold (15.2°C < 16°C)",
        "timestamp": "2026-02-22T08:35:15Z"
      }
    ]
```

### Crop Inventory Service (Port 8084)

```
POST /api/crops
  Description: Register new crop batch
  Authorization: Bearer {token}
  Request:
    {
      "cropName": "Tomato",
      "zoneId": "ZONE-001",
      "batchDate": "2026-02-20",
      "quantity": 150,
      "expectedHarvestDate": "2026-04-20"
    }
  Response:
    {
      "cropId": "CROP-001",
      "cropName": "Tomato",
      "status": "SEEDLING",
      "zoneId": "ZONE-001",
      "quantity": 150
    }

PUT /api/crops/{id}/status
  Description: Update crop lifecycle stage
  Authorization: Bearer {token}
  Request:
    {
      "status": "VEGETATIVE"  # SEEDLING, VEGETATIVE, HARVESTED
    }

GET /api/crops
  Description: List all crops in inventory
  Authorization: Bearer {token}
  Query Parameters:
    - zoneId: Filter by zone
    - status: Filter by status
  Response:
    [
      {
        "cropId": "CROP-001",
        "cropName": "Tomato",
        "status": "VEGETATIVE",
        "zoneId": "ZONE-001",
        "batchDate": "2026-02-20",
        "quantity": 150
      }
    ]
```

---

## System Workflow

### Complete End-to-End Flow

#### **Scenario**: Farmer Creates Zone and System Monitors Temperature

```
1. ZONE CREATION (Farmer)
   ├─ Farmer: POST /api/zones with credentials
   │           {
   │             "name": "Tomato Zone",
   │             "minTemp": 18, "maxTemp": 28,
   │             "minHumidity": 50, "maxHumidity": 80
   │           }
   │
   ├─ API Gateway: Validates JWT token
   │
   ├─ Zone Service: 
   │   ├─ Validates: 18 < 28 ✓
   │   ├─ Calls External IoT API to register device
   │   ├─ Stores zone + deviceId in database
   │   └─ Returns zoneId to farmer
   │
   └─ Response: HTTP 201 Created
                {
                  "zoneId": "ZONE-001",
                  "deviceId": "dev-xyz-123"
                }

2. CONTINUOUS MONITORING (Every 10 seconds)

   a) SENSOR TELEMETRY SERVICE
      ├─ Timer triggers (fixedRate: 10000ms)
      ├─ Gets valid IoT token (auto-refreshes if expired)
      ├─ Calls External IoT: GET /devices/telemetry/dev-xyz-123
      ├─ Receives: 
      │   {
      │     "deviceId": "dev-xyz-123",
      │     "temperature": 29.5,
      │     "humidity": 72.3,
      │     "capturedAt": "2026-02-22T08:35:00Z"
      │   }
      └─ POSTs to Automation Service /api/automation/process

   b) AUTOMATION SERVICE (Rule Engine)
      ├─ Receives telemetry
      ├─ Calls Zone Service (via OpenFeign): GET /api/zones/ZONE-001
      ├─ Receives thresholds:
      │   {
      │     "minTemp": 18, "maxTemp": 28,
      │     "minHumidity": 50, "maxHumidity": 80
      │   }
      ├─ Evaluates rules:
      │   ✓ temperature 29.5 > maxTemp 28
      │   → Logs action: "TURN_FAN_ON"
      │   
      │   ✓ humidity 72.3 within range
      │   → No action
      │
      └─ Stores log entry in database

   c) FARMER VIEWS LOGS
      ├─ GET /api/automation/logs?zoneId=ZONE-001
      ├─ API Gateway validates token
      ├─ Automation Service returns:
      │   [
      │     {
      │       "logId": "LOG-001",
      │       "actionType": "TURN_FAN_ON",
      │       "timestamp": "2026-02-22T08:35:00Z"
      │     }
      │   ]
      └─ Farmer sees that fan was activated automatically
```

### Data Flow Diagram

```
External IoT Provider (104.211.95.241:8080)
    ↓
    ├─ [Auth] → getToken()
    │
    ├─ [Telemetry] → {temp: 29.5, humidity: 72.3}
    │
    ↓
Sensor Telemetry Service (8082)
    ├─ Scheduled task every 10s
    ├─ Fetch telemetry + auth header
    ├─ POST → Automation Service
    │
    ↓
Automation Service (8083)
    ├─ Receive telemetry
    ├─ Query Zone Service via Feign
    │   ├─ "Get thresholds for ZONE-001"
    │   ←─ {minTemp: 18, maxTemp: 28}
    ├─ Compare: 29.5 > 28 ✓
    ├─ Log action: "TURN_FAN_ON"
    ├─ Store in database
    │
    ↓
Farmer (via API Gateway)
    ├─ GET /api/automation/logs
    ├─ API Gateway validates JWT
    ├─ Automation Service responds
    ├─ See: "TURN_FAN_ON" action triggered
    └─ Can take manual action if needed
```

---

## Testing

### Prerequisites for Testing
- All services running and visible in Eureka
- Postman installed (or curl available)
- Valid JWT token from external IoT provider

### Test Sequence

#### **Step 1: Obtain JWT Token**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "your-iot-username",
    "password": "your-iot-password"
  }'

# Response:
# {
#   "accessToken": "eyJhbGciOiJIUzI1NiIsI...",
#   "refreshToken": "...",
#   "expiresIn": 3600
# }

# Store accessToken for subsequent requests
TOKEN="eyJhbGciOiJIUzI1NiIsI..."
```

#### **Step 2: Create Zone**
```bash
curl -X POST http://localhost:8080/api/zones \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Tomato Zone",
    "minTemp": 18.0,
    "maxTemp": 28.0,
    "minHumidity": 50.0,
    "maxHumidity": 80.0
  }'

# Response should include zoneId and deviceId
# Store zoneId: ZONE_ID="ZONE-001"
```

#### **Step 3: Verify Sensor Telemetry (Debug)**
```bash
# Wait for scheduler to run (10 seconds)
sleep 10

curl -X GET http://localhost:8080/api/sensors/latest \
  -H "Authorization: Bearer $TOKEN"

# Should show latest fetched temperature/humidity
```

#### **Step 4: Check Automation Logs**
```bash
curl -X GET http://localhost:8080/api/automation/logs \
  -H "Authorization: Bearer $TOKEN"

# Should show triggered actions like:
# - TURN_FAN_ON (if temp > max)
# - TURN_HEATER_ON (if temp < min)
# - etc.
```

#### **Step 5: Register Crop**
```bash
curl -X POST http://localhost:8080/api/crops \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "cropName": "Tomato",
    "zoneId": "ZONE-001",
    "quantity": 150,
    "expectedHarvestDate": "2026-04-20"
  }'
```

#### **Step 6: Update Crop Status**
```bash
CROP_ID="CROP-001"  # From previous response

curl -X PUT http://localhost:8080/api/crops/$CROP_ID/status \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "VEGETATIVE"
  }'
```

### Troubleshooting

| Issue | Solution |
|-------|----------|
| **401 Unauthorized** | Verify JWT token is valid and not expired; refresh if needed |
| **503 Service Unavailable** | Check all services registered in Eureka (http://localhost:8761) |
| **504 Gateway Timeout** | Ensure called microservice is running and responsive |
| **Connection refused on port 8081** | Verify Zone Service started successfully |
| **External IoT API connection fails** | Check network connectivity and IoT provider URL/credentials |

---

## Conclusion

The **Automated Greenhouse Management System (AGMS)** demonstrates enterprise-grade microservices architecture with:

✓ **Service Discovery**: Dynamic registration via Eureka  
✓ **API Gateway**: Centralized routing and security  
✓ **Configuration Management**: Centralized properties via Spring Cloud Config  
✓ **Inter-Service Communication**: Synchronous via OpenFeign, asynchronous via REST  
✓ **Security**: JWT-based authentication at Gateway level  
✓ **External Integration**: Real-time IoT data ingestion with token management  
✓ **Scalability**: Loosely-coupled services that can scale independently  

This architecture provides the foundation for building production-grade distributed systems with proper separation of concerns, centralized management, and secure inter-service communication.

---

## References

- **Spring Boot Documentation**: https://spring.io/projects/spring-boot
- **Spring Cloud**: https://spring.io/projects/spring-cloud
- **Netflix Eureka**: https://github.com/Netflix/eureka
- **Spring Cloud Gateway**: https://spring.io/projects/spring-cloud-gateway
- **OpenFeign**: https://github.com/OpenFeign/feign
- **JWT (JSON Web Tokens)**: https://jwt.io
- **External IoT Provider**: http://104.211.95.241:8080/api

---

**Version**: 1.0  
**Last Updated**: April 2026  
**Maintained By**: AGMS Development Team
