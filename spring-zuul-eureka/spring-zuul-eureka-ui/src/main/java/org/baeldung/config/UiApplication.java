package org.baeldung.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;


@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient // put zuul into eureka
@RestController
//@ComponentScan(
//        value = "org.baeldung.config",
//        excludeFilters =
//                {@ComponentScan.Filter(
//                        type = FilterType.ANNOTATION,
//                        value=ExcludeFromComponentScan.class)}
//)
//@RibbonClient(
//        name = "some-service",
//        configuration = RibbonConfiguration.class)
public class UiApplication extends SpringBootServletInitializer {

    @LoadBalanced
    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/server-location")
    public String serverLocation() {
        return this.restTemplate.getForObject(
                "http://foo-service/foos/" + randomNumeric(2), String.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(UiApplication.class, args);
    }
}