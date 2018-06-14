package org.baeldung.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ROUTE_TYPE;

@Component
public class CustomZuulFilter extends ZuulFilter {

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${routing.tag.domain}")
    private String tag;

    @Override
    public Object run() {
        System.out.println(String.format("Custom Filter"));


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

//        PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
//        Applications applications = registry.getApplications();
//
//        applications.getRegisteredApplications().forEach((registeredApplication) -> {
//            registeredApplication.getInstances().forEach((instance) -> {
//                System.out.println(instance.getAppName() + " (" + instance.getInstanceId() + ") : ");
//            });
//        });

//
//        List<Server> serverList = dynamicServerListLoadBalancer.getServerList(true);
//        for (Server server : serverList) {
//            System.out.println(String.format("Server ID %s host %s : port %s", server.getId(), server.getHost(), server.getHostPort()));
//        }
//        ctx.set("serviceId", “service-a”);
        try {
            final RequestContext ctx = RequestContext.getCurrentContext();
            String serviceId = (String) ctx.get("serviceId");
            System.out.println("=============== serviceId\n" + serviceId);
//            System.out.println("===============" + ctx.getRouteHost().toString());
            ServiceInstance instance = loadBalancer.choose(serviceId);

            String requestURI = ctx.getRequest().getRequestURI();
            String proxy = (String) ctx.get("proxy");
            String path = (String) ctx.get("path");
            String uri = String.format("http://%s:%s/%s",
                    instance.getHost(), instance.getPort(), proxy);
            System.out.println("=============== routeHost \n" + uri);
//            ctx.setRouteHost(new URL("http://localhost:8082/foos"));
            ctx.setRouteHost(new URL(uri));
//            ctx.setRouteHost(new URL("http://localhost:8082/foos"));
        } catch (Exception e) {
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
        return 2;
    }

    @Override
    public String filterType() {
        return ROUTE_TYPE;
    }

}
