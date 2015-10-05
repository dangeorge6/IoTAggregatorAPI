package com.shoppertrak.exampleappname
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
@PropertySource(["file:secure.properties", "buildinfo.properties"])
class Main {

    static void main(String[] args) {
        SpringApplication.run(Main.class)
    }
}
