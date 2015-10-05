import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.status.OnConsoleStatusListener
// For syntax, see http://logback.qos.ch/manual/groovy.html
// Logging detail levels: TRACK > DEBUG > INFO > WARN > ERROR

def logPath = "/opt/application/logs/app.log"
def logPattern = "%d{yyyy-MM-dd_HH:mm:ss.SSS} [%thread] %-5level %logger{66} - %msg%n"

displayStatusOnConsole()
scan('5 minutes') // periodically scan for log configuration changes
setupAppenders(logPath, logPattern)
setupLoggers()


def displayStatusOnConsole() {
    // According to the "logback" documentation, always a good idea to add an OnConsoleStatusListener
    statusListener OnConsoleStatusListener
}

def setupAppenders(logPath, logPattern) {
    appender("FILE", RollingFileAppender) {
        file = "${logPath}"
        rollingPolicy(TimeBasedRollingPolicy) {
            fileNamePattern = "${logPath}.%d{yyyy-MM-dd}.%i"
            timeBasedFileNamingAndTriggeringPolicy(SizeAndTimeBasedFNATP) {
                maxFileSize = "100MB"
            }
            maxHistory = 30
        }
        encoder(PatternLayoutEncoder) {
            pattern = logPattern
        }
    }

    appender("CONSOLE", ConsoleAppender) {
        encoder(PatternLayoutEncoder) {
            pattern = logPattern
        }
    }
}

def setupLoggers() {
    logger("com.zaxxer.hikari", INFO) // Hikari
    logger("org.springframework", INFO) // Spring
    root(DEBUG, ["CONSOLE", "FILE"])
}
