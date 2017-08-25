package ru.spanferov.interview.test.bank.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping(method = RequestMethod.GET, produces = {"application/text"})
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("TEST STRING");
    }

}
