package org.baeldung.web.controller;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.baeldung.web.dto.Foo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class FooController {

    @Value("${routing.tag.domain}")
    private String tag;

    public FooController() {
        super();
    }

    // API - read
    @RequestMapping(method = RequestMethod.GET, value = "/foos/{id}")
    @ResponseBody
    public Foo findById(@PathVariable final long id, HttpServletRequest req, HttpServletResponse res) {
        // System.out.println(req.getHeaderNames());
        // System.out.println("------" + req.getHeader("Test"));
        if (req.getHeader("TestFoo") != null) {
            res.addHeader("TestFoo", req.getHeader("TestFoo"));
            res.addHeader("TestBar", req.getHeader("TestBar"));
            res.addHeader("Routing", req.getHeader("Routing"));
        }
        return new Foo(Long.parseLong(randomNumeric(2)), tag + "-" +randomAlphabetic(4));
    }

}
