package org.baeldung.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
public class PreZuulFilter extends ZuulFilter {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${routing.tag.domain}")
    private String tag;

    @Override
    public Object run() {
        System.out.println(String.format(filterType() + "- Filter"));
        final RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader("TestFoo", "FooSample");
        ctx.addZuulRequestHeader("TestBar", "BarSample");
        ctx.addZuulRequestHeader("Routing", tag);
        HttpServletRequest request = ctx.getRequest();
        System.out.println(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

}
