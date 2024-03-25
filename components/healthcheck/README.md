## Ledger Sync Health Check component

The Ledger Sync health check component provides custom Spring Actuator endpoints, facilitating health checks 
for applications that receive events from the Yaci Store

### Endpoint

The Ledger Sync health check component provides a custom Spring Actuator endpoint named `health-status`.
This endpoint is responsible for checking the health status of the application.

### Configuration

- `ledger-sync.healthcheck.enabled`: Set this property to `true` to enable the health check component. By default, it's set to `false`.

- `ledger-sync.healthcheck.eventTimeThreshold`: Specifies the time threshold (in seconds) within which the application should have received a new event.
If the time since the last block event exceeds this threshold, the health status will be marked as unhealthy. Default value is 240 seconds.

- `ledger-sync.healthcheck.blockTimeCheckEnabled`: Enables or disables the check for block time. 
If enabled, the health check component will also verify whether the data has reached the tip.
In many cases, this can help determine if the data is ready to be served. Default is `false`.

- `ledger-sync.healthcheck.blockTimeThreshold`: Specifies the time threshold (in seconds) for block time.
If the latest block time is within this threshold and block time check is enabled,
the health status will be marked as healthy, and the message will indicate that the data has reached or is near the ti
Default value is 180 seconds.

- `ledger-sync.healthcheck.stopSlot`: Specifies the stop slot number.
If provided and the latest slot number exceeds this stop slot, the health status will be marked as healthy,
indicating that the stop slot has been reached. Default is not set.

When integrating the Ledger Sync health check component into your application, ensure that you configure 
the Spring Actuator endpoint `health-status`. 
Below is an example configuration snippet:

```yaml
management:
  endpoints:
    enabled-by-default: false
    web:
      exposure:
        include: "health,prometheus,health-status"
  endpoint:
    health:
      enabled: true
    prometheus:
      enabled: true
    health-status:
      enabled: true
```
For more detailed information on configuring Spring Actuator endpoints, refer to the Spring Boot documentation:
[Spring Actuator Endpoint](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints)

### Usage
To utilize the health check endpoint, make a GET request to `/actuator/health-status`. 
The response will contain the current health status of the application.

The response format is a JSON object containing information about the health status. 
It includes fields: `isHealthy`, `messageCode`, `messageDesc`

- `isHealthy`: A boolean value indicating whether the application is healthy (`true`) or unhealthy (`false`).
- `messageCode`: A code representing the current health status message.
- `messageDesc`: A descriptive message explaining the current health status, corresponding to the `messageCode`.

Response:

| HTTP Status | Health Status | Message Code          | Message Description                                           |
|-------------|---------------|-----------------------|---------------------------------------------------------------|
| 200         | Healthy       | IS_GOOD               | The last time received a block event is within the threshold  |
| 500         | Unhealthy     | IS_BAD                | The last time received a block event beyond the threshold     |
| 200         | Healthy       | STOP_SLOT_HAS_REACHED | Stop slot has been reached                                    |    
| 200         | Healthy       | BLOCK_HAS_REACHED_TIP | The latest block is the tip or near the tip                   |     
