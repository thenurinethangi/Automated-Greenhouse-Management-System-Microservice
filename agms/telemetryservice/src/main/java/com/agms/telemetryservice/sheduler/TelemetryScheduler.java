package com.agms.telemetryservice.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.agms.telemetryservice.service.TelemetryService;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelemetryScheduler {

    private final TelemetryService telemetryService;

    @Scheduled(fixedRate = 10000)
    public void fetchData() {
        log.info("telemetry fetch..................................................................");

        try {
            telemetryService.fetchAndProcessTelemetry();
        } catch (Exception e) {
            log.error("Error while fetching telemetry data", e);
        }
    }
}
