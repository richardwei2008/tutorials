package org.baeldung.web;

import org.baeldung.config.UiApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=UiApplication.class)
@TestPropertySource(properties = { "spring.config.location=classpath:application-ut.yml" })
//@IntegrationTest
////@ActiveProfiles("test") // Like this
/**
 * Override specific property
 */
//@TestPropertySource(
//    properties = {
//            "spring.jpa.hibernate.ddl-auto=validate",
//            "liquibase.enabled=false"
//    }
//)

public class LoadBalancerClientTest {

    @Autowired
    private LoadBalancerClient loadBalancer;

//    @Autowired
//    private MyConfiguration myConfiguration; //this will be filled with myApp-test.yml

    @Value("${server.port}")
    private String serverPort; //will get value from the yml file. TODO ?

    /**
     * 客户端根据 service 做 load balance
     */
    @Test
    public void testLoadBalancer() {
        for (int i = 0; i < 10; i++) {
            ServiceInstance instance = loadBalancer.choose("foo-service");

            String uri = String.format("http://%s:%s", instance.getHost(), instance.getPort());
            System.out.println("=====================\n" + uri);
            URI storesUri = URI.create(uri);
            // ... do something with the URI
            System.out.println("=====================\n" + serverPort);
        }
    }
}
