package org.baeldung.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
public class CustomZuulFilter extends ZuulFilter {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${routing.tag.domain}")
    private String tag;

    @Override
    public Object run() {
        System.out.println(String.format("Pre Filter"));
        final RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader("TestFoo", "FooSample");
        ctx.addZuulRequestHeader("TestBar", "BarSample");
        ctx.addZuulRequestHeader("Routing", tag);
        HttpServletRequest request = ctx.getRequest();
        System.out.println(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        List<String> services = discoveryClient.getServices();
        for (String service :services) {
            System.out.println(service);
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            for (ServiceInstance instance : instances) {
                System.out.println(String.format("Server ID %s host %s : port %s", instance.getServiceId(), instance.getHost(), instance.getPort()));
                Map<String, String> meta = instance.getMetadata();
                System.out.println(String.format("Server Meta %s", instance.getMetadata()));
            }
        }

//
//        List<Server> serverList = dynamicServerListLoadBalancer.getServerList(true);
//        for (Server server : serverList) {
//            System.out.println(String.format("Server ID %s host %s : port %s", server.getId(), server.getHost(), server.getHostPort()));
//        }
//        ctx.set("serviceId", “service-a”);
        try {
            ctx.setRouteHost(new URL("http://localhost:8082/foos"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 1110;
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

}
