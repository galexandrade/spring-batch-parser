package com.ef.config;

import javax.sql.DataSource;

import com.ef.job.JobCompletionNotificationListener;
import com.ef.job.Step1;
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
                             //@Qualifier("step1") Step step1
                             Step1 step1,
                             Step2 step2) {

        Step s1 = stepBuilderFactory.get("step1")
                .<Access, Access> chunk(1000)
                .reader(step1.reader())
                .writer(step1.writer())
                .build();

        Step s2 = stepBuilderFactory.get("step2")
                .<Access, Access> chunk(1000)
                .reader(step2.reader2())
                .writer(step2.writer2())
                .build();

        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .validator(validator)
                .listener(listener)
                .start(s1)
                .next(s2)
                .build();
    }
}
