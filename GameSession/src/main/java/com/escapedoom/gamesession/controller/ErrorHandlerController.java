package com.escapedoom.gamesession.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ErrorHandlerController implements ErrorController {

    @RequestMapping(UrlConstants.ERROR_URL)
    public String handleError(HttpServletRequest request) {

        return request.getRequestURL().toString();
    }
}
