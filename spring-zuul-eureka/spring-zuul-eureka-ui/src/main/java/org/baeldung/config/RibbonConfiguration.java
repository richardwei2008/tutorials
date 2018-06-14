package org.baeldung.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.WeightedResponseTimeRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 这个类不能与Spring Boot @ExcludeFromComponentScan
 * 所在主类放在同一个包或其子包下，否则需要些 Exclude 类做区分
 */
//@Configuration
//@ExcludeFromComponentScan
public class RibbonConfiguration {
 
    @Autowired
    IClientConfig ribbonClientConfig;
 
    @Bean
    public IPing ribbonPing(IClientConfig config) {
        return new PingUrl();
    }
 
    @Bean
    public IRule ribbonRule(IClientConfig config) {
        /**
         * 根据响应时间
         */
        return new WeightedResponseTimeRule();


    }
}