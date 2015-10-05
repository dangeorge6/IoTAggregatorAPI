package com.shoppertrak.exampleappname.shutdown

import org.springframework.boot.actuate.endpoint.Endpoint
import org.springframework.stereotype.Component

@Component
class GracefulShutdown implements Endpoint<List<String>> {

    String getId() {
        return "gracefulShutdown"
    }

    boolean isEnabled() {
        return true
    }

    boolean isSensitive() {
        return true
    }

    List<String> invoke() {
        AppStatus.setCURRENT_STATUS(AppStatus.STATUS.SHUTTING_DOWN)

        return ["Setting status to 'SHUTTING_DOWN'"]
    }
}
