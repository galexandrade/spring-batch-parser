package com.ef;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Parser implements ApplicationRunner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Parser.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}
