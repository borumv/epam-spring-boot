package com.vlasevsky.gym.feign;

import com.vlasevsky.gym.dto.TrainerWorkloadRequest;
import com.vlasevsky.gym.dto.TrainerWorkloadSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient("work-load-service")
public interface WorkLoadClient {

    @GetMapping("/workload/{username}")
    ResponseEntity<TrainerWorkloadSummary> getWorkload(@PathVariable String username,
                                                              @RequestParam int year,
                                                              @RequestParam int month);
    @PostMapping("/workload")
    ResponseEntity<Void> updateWorkload(@RequestBody TrainerWorkloadRequest request);
}
