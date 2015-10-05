package com.shoppertrak.exampleappname.controller

import com.shoppertrak.exampleappname.service.ExampleService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.metrics.CounterService
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
@RequestMapping("/example-app/v1.0")
class ExampleController {

    final String ACTIVE_REQUESTS_STAT = "requests.active"

    ExampleService exampleService
    CounterService counterService

    @Autowired
    ExampleController(ExampleService exampleService, CounterService counterService) {
        this.exampleService = exampleService
        this.counterService = counterService
    }

    @RequestMapping(value = "/url", method = RequestMethod.GET)
    def get_stuff(
            @RequestParam(value = "some_arg1") String someArg1,
            @RequestParam(value = "some_arg2") String someArg2) {
        try {
            counterService.increment(ACTIVE_REQUESTS_STAT)

            return exampleService.get_stuff(someArg1, someArg2)
        } finally {
            counterService.decrement(ACTIVE_REQUESTS_STAT)
        }
    }

    // This will turn off security so that controller does not require authentication
    @Bean
    public SecurityProperties securityProperties() {
        SecurityProperties security = new SecurityProperties()
        security.getBasic().setPath("")
        return security
    }
}
