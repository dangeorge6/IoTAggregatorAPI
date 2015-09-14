package com.shoppertrak.exampleappname.controller

import com.shoppertrak.exampleappname.service.ExampleService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
@RequestMapping("/v1.0")
class ExampleController {

    ExampleService service

    @Autowired
    ExampleController(ExampleService service) {
        this.service = service
    }

    @RequestMapping(value = "/url", method = RequestMethod.GET)
    def get_stuff(
            @RequestParam(value = "some_arg1") String someArg1,
            @RequestParam(value = "some_arg2") String someArg2) {

        return service.get_stuff(someArg1, someArg2)
    }
}
