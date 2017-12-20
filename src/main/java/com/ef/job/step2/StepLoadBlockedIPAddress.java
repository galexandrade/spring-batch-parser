package com.ef.job.step2;

import com.ef.model.Access;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alex.andrade on 19/12/2017.
 */
@Configuration
public class StepLoadBlockedIPAddress {
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    BlockedIPAddressProcessor processor;

    @Autowired
    public DataSource dataSource;

    @Value("${startDate}")
    private String startDate;

    @Value("${duration}")
    private String duration;

    @Value("${threshold}")
    private int threshold;

    public Step step2() {
        return stepBuilderFactory.get("stepLoadBlockedIPAddress")
                .<Access, Access> chunk(10)
                .reader(reader2())
                .processor(processor)
                .writer(writer2())
                .build();
    }

    @Bean
    ItemReader<Access> reader2() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDateParam = new Date();
        try {
            startDateParam = format.parse(startDate.replace(".", " "));
        } catch (ParseException e) {
            e.printStackTrace();
        }
				/*Calculating the final date*/
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDateParam);

        if(duration.equals("hourly"))
            cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
        else
            cal.add(Calendar.DAY_OF_MONTH, 1); // adds one day

        Date finalDate = cal.getTime();

        String sql = "SELECT * FROM access.access where access.dt_access between '" + format.format(startDateParam) + "' and '" + format.format(finalDate) + "' group by access.ip_address having count(access.ip_address) > " + threshold + " limit " + threshold;


        JdbcCursorItemReader<Access> databaseReader = new JdbcCursorItemReader<>();

        databaseReader.setDataSource(dataSource);
        databaseReader.setSql(sql);

        databaseReader.setRowMapper(new AccessRowMapper());

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
