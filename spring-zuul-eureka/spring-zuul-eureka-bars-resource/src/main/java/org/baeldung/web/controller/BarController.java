package org.baeldung.web.controller;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.baeldung.web.dto.Bar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BarController {

    public BarController() {
        super();
    }

    // API - read
    @RequestMapping(method = RequestMethod.GET, value = "/bars/{id}")
    @ResponseBody
    public Bar findById(@PathVariable final long id, HttpServletRequest req, HttpServletResponse res) {
        // System.out.println(req.getHeaderNames());
        // System.out.println("------" + req.getHeader("Test"));
        if (req.getHeader("TestBar") != null) {
            res.addHeader("TestBar", req.getHeader("TestBar"));
        }
        return new Bar(Long.parseLong(randomNumeric(2)), randomAlphabetic(4));
    }

}
