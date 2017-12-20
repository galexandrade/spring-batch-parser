package com.ef.job.step1;

import com.ef.model.Access;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

/**
 * Created by alex.andrade on 19/12/2017.
 */
@Configuration
public class StepLoadLogFile {
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Value("${accesslog}")
    private String logFile;

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("stepLoadLogFile")
                .<Access, Access> chunk(1000)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public FlatFileItemReader<Access> reader() {
        FlatFileItemReader<Access> reader = new FlatFileItemReader<Access>();
        reader.setResource(new ClassPathResource(logFile));
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
    public JdbcBatchItemWriter<Access> writer() {
        JdbcBatchItemWriter<Access> writer = new JdbcBatchItemWriter<Access>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Access>());

        writer.setSql("INSERT INTO access (dt_access, ip_address, request, status, user_agent) VALUES (:date, :IPAddress, :request, :status, :userAgent)");

        writer.setDataSource(dataSource);
        return writer;
    }
}
