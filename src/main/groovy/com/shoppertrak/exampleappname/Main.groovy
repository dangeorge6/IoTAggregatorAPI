package com.shoppertrak.exampleappname

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
@ComponentScan("com.shoppertrak.*")
@PropertySource('file:secure.properties')
class Main {

    static void main(String[] args) {
        SpringApplication.run(Main.class)
    }
}
