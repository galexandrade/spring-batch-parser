package com.ef.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ef.model.Access;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	@Value("${startDate}")
	private String startDate;

	@Value("${duration}")
	private String duration;

	@Value("${threshold}")
	private int threshold;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results ==> " + startDate);

			List<Access> results = jdbcTemplate.query("SELECT dt_access, ip_address, request, status, user_agent FROM access", new RowMapper<Access>() {
				@Override
				public Access mapRow(ResultSet rs, int row) throws SQLException {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    Date accessDt = null;
                    try {
                        accessDt = format.parse(rs.getString(1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
					return new Access(accessDt, rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
				}
			});

			for (Access access : results) {
				log.info("Found <" + access + "> in the database.");
			}

		}
	}
}
