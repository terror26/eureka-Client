package com.clientSpringSimple.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ServiceInstanceRestController {


    private final LoadBalancerClient loadBalancer;
    private final RestTemplate restTemplate;


    @Autowired
    public ServiceInstanceRestController(LoadBalancerClient loadBalancer, RestTemplate restTemplate) {
        this.loadBalancer = loadBalancer;
        this.restTemplate = restTemplate;
    }


    @GetMapping("/balance")
    public String sendRequestToService() {
        // Load balance the service instances using Eureka
        ServiceInstance serviceInstance = loadBalancer.choose("EUREKA-CLIENT"); // Replace "service-name" with the name of your service registered in Eureka

        // Build URL using the service instance
        String url = serviceInstance.getUri().toString() + "/ping"; // Assuming the endpoint is /hello, adjust as needed
        System.out.println("url accessed = " + serviceInstance.getUri());

        // Send request to the chosen service instance
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Return response body
        return response.getBody();
    }

    @GetMapping("/ping")
    public String getPing() {
        return "pong";
    }

}
