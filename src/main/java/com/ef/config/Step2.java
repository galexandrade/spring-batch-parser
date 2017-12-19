package com.ef.config;

import com.ef.model.Access;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

/**
 * Created by alex.andrade on 19/12/2017.
 */
@Configuration
public class Step2 {
    @Autowired
    public DataSource dataSource;

    @Bean
    ItemReader<Access> reader2() {
        JdbcCursorItemReader<Access> databaseReader = new JdbcCursorItemReader<>();

        databaseReader.setDataSource(dataSource);
        databaseReader.setSql("select * from access where id < 10");

        databaseReader.setRowMapper(new BeanPropertyRowMapper<>(Access.class));

        return databaseReader;
    }

    @Bean
    public JdbcBatchItemWriter<Access> writer2() {
        JdbcBatchItemWriter<Access> writer = new JdbcBatchItemWriter<Access>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Access>());

        writer.setSql("INSERT INTO blocked_ips (ip_address, comment) VALUES (:IPAddress, 'IP blocked due to many requests')");

        writer.setDataSource(dataSource);
        return writer;
    }
}
