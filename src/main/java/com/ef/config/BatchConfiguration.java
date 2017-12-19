package com.ef.config;

import javax.sql.DataSource;

import com.ef.job.AccessItemProcessor;
import com.ef.job.BeanWrapperFieldSetMapperCustom;
import com.ef.job.JobCompletionNotificationListener;
import com.ef.model.Access;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<Access> reader() {
        FlatFileItemReader<Access> reader = new FlatFileItemReader<Access>();
        reader.setResource(new ClassPathResource("access.txt"));
        reader.setLineMapper(new DefaultLineMapper<Access>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "date", "IPAddress", "request", "status", "userAgent" });
                setDelimiter("|");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapperCustom<Access>() {{
                setTargetType(Access.class);
            }});
        }});
        return reader;
    }

    @Bean
    public AccessItemProcessor processor() {
        return new AccessItemProcessor();
    }


    @Bean
    public JdbcBatchItemWriter<Access> writer() {
        JdbcBatchItemWriter<Access> writer = new JdbcBatchItemWriter<Access>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Access>());

        writer.setSql("INSERT INTO access (dt_access, ip_address, request, status, user_agent) VALUES (:date, :IPAddress, :request, :status, :userAgent)");

        writer.setDataSource(dataSource);
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener,
                             CustomJobParametersValidator validator,
                             @Qualifier("step1") Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .validator(validator)
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(ItemWriter<Access> writer) {
        return stepBuilderFactory.get("step1")
                .<Access, Access> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
    // end::jobstep[]
}
