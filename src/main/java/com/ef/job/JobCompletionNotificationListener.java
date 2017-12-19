package com.ef.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
			try {
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date startDateParam = format.parse(startDate.replace(".", " "));

				/*Calculating the final date*/
				Calendar cal = Calendar.getInstance();
				cal.setTime(startDateParam);

				if(duration.equals("hourly"))
					cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
				else
					cal.add(Calendar.DAY_OF_MONTH, 1); // adds one day

				Date finalDate = cal.getTime();

				String sql = "SELECT * FROM access.access where access.dt_access between '" + format.format(startDateParam) + "' and '" + format.format(finalDate) + "' group by access.ip_address having count(access.ip_address) > " + threshold + " limit " + threshold;

				List<Access> results = jdbcTemplate.query(sql, new RowMapper<Access>() {
					@Override
					public Access mapRow(ResultSet rs, int row) throws SQLException {


						Date accessDt = null;
						try {
							accessDt = format.parse(rs.getString(3));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						return new Access(accessDt, rs.getString(2), rs.getString(4), rs.getInt(5), rs.getString(6));
					}
				});

				for (Access access : results) {
					log.info(access.getIPAddress());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
	}
}
