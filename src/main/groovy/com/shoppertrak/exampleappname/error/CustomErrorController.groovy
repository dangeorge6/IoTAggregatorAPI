package com.shoppertrak.exampleappname.error

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.metrics.CounterService
import org.springframework.boot.autoconfigure.web.ErrorAttributes
import org.springframework.boot.autoconfigure.web.ErrorController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.ServletRequestAttributes

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
@RestController
class CustomErrorController implements ErrorController {

    final String ACTIVE_REQUESTS_STAT = "requests.active"
    final boolean includeStackTrace = false

    ErrorAttributes errorAttributes
    CounterService counterService

    @Autowired
    CustomErrorController(ErrorAttributes errorAttributes, CounterService counterService) {
        this.errorAttributes = errorAttributes
        this.counterService = counterService
    }

    @RequestMapping(value = "/error")
    ErrorJson error(HttpServletRequest request, HttpServletResponse response) {
        try {
            counterService.increment(ACTIVE_REQUESTS_STAT)
            ErrorJson error = new ErrorJson(response.getStatus(), getErrorAttributes(request))
            log.info("{}: {} ({})", error.status, error.error, error.message)

            return error
        } finally {
            counterService.decrement(ACTIVE_REQUESTS_STAT)
        }
    }

    @Override
    String getErrorPath() {
        return "/error"
    }

    Map<String, Object> getErrorAttributes(HttpServletRequest request) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request)

        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace)
    }
}
