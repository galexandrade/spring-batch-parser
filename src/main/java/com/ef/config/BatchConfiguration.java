package com.ef.config;

import javax.sql.DataSource;

import com.ef.job.JobCompletionNotificationListener;
import com.ef.job.step1.StepLoadLogFile;
import com.ef.job.step2.StepLoadBlockedIPAddress;
import com.ef.model.Access;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener,
                             CustomJobParametersValidator validator,
                             StepLoadLogFile stepLoadLogFile,
                             StepLoadBlockedIPAddress stepLoadBlockedIPAddress) {

        Step s1 = stepLoadLogFile.step1();

        Step s2 = stepLoadBlockedIPAddress.step2();

        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .validator(validator)
                .listener(listener)
                .start(s1)
                .next(s2)
                .build();
    }
}
