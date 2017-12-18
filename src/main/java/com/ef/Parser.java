package com.ef;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class Parser {

    public static void main(String[] args) throws Exception {
        Date startDate = null;
        String duration = null;
        int threshold = 0;

        for (String arg : args) {
            if(arg.contains("--startDate")){
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateConvertion = arg.substring("--startDate=".length()).replace(".", " ");
                startDate = format.parse(dateConvertion);
            }
            else if(arg.contains("--duration")){
                duration = arg.substring("--duration=".length());
            }
            else if(arg.contains("--threshold")){
                threshold = Integer.parseInt(arg.substring("--threshold=".length()));
            }
        }

        System.out.println(startDate);
        System.out.println(duration);
        System.out.println(threshold);

        SpringApplication.run(Parser.class, args);
    }
}
