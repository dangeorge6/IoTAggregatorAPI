package com.shoppertrak.exampleappname.shutdown

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component

@Component
class GracefulShutdownIndicator implements HealthIndicator {
    @Override
    Health health() {
        Health.Builder builder = Health.up()

        switch (AppStatus.getCURRENT_STATUS()) {
            case AppStatus.STATUS.DOWN:
                builder = Health.down()
                break;
            case AppStatus.STATUS.SHUTTING_DOWN:
                builder = Health.outOfService()
                break;
        }

        return builder.build()
    }
}
