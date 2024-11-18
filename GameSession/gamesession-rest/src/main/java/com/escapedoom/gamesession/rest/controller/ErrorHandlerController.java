package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.rest.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ErrorHandlerController implements ErrorController {

    @RequestMapping(Constants.ERROR_URL)
    public String handleError(HttpServletRequest request) {

        return request.getRequestURL().toString();
    }
}
