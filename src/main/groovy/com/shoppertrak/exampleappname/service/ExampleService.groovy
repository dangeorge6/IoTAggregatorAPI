package com.shoppertrak.exampleappname.service

import com.shoppertrak.exampleappname.db.ExampleRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class ExampleService {

    ExampleRepository repo

    @Autowired
    ExampleService(ExampleRepository repo) {
        this.repo = repo
    }

    Object[] get_stuff(String arg1, String arg2) {
        repo.example_query(arg1, arg2)
    }
}
