package com.ef;

import com.ef.job.JobCompletionNotificationListener;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

@SpringBootApplication
public class Parser implements ApplicationRunner {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Parser.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Parser.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(!args.containsOption("startDate")){
            throw new ExceptionInInitializerError("You must add the parameter startDate!");
        }
        if(!args.containsOption("duration")){
            throw new ExceptionInInitializerError("You must add the parameter duration!");
        }
        if(!args.containsOption("threshold")){
            throw new ExceptionInInitializerError("You must add the parameter duration!");
        }
    }
}
