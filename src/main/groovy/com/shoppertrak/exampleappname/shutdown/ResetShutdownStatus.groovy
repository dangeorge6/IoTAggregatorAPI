package com.shoppertrak.exampleappname.shutdown

import org.springframework.boot.actuate.endpoint.Endpoint
import org.springframework.stereotype.Component

@Component
class ResetShutdownStatus implements Endpoint<List<String>> {

    String getId() {
        return "resetShutdownStatus"
    }

    boolean isEnabled() {
        return true
    }

    boolean isSensitive() {
        return true
    }

    List<String> invoke() {
        AppStatus.setCURRENT_STATUS(AppStatus.STATUS.UP)

        return ["Setting status to 'UP'"]
    }
}
