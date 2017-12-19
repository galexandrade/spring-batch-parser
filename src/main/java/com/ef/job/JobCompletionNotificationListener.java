package com.ef.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ef.model.Access;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
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
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			//SELECT *, count(access.ip_address) FROM access.access where access.dt_access between '2017-01-01 15:00:00' and '2017-01-01 16:00:00' group by access.ip_address having count(access.ip_address) > 100 limit 100;
			List<Access> results = jdbcTemplate.query("SELECT dt_access, ip_address, request, status, user_agent FROM access", new RowMapper<Access>() {
				@Override
				public Access mapRow(ResultSet rs, int row) throws SQLException {


                    Date accessDt = null;
                    try {
                        accessDt = format.parse(rs.getString(1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
					return new Access(accessDt, rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5));
				}
			});

			try {
				Date startDateParam = format.parse(startDate.replace(".", " "));
				Map<String, Integer> IP_COUNTER = new LinkedHashMap<String, Integer>();

				for (Access access : results) {
					if(!IP_COUNTER.containsKey(access.getIPAddress()))
						IP_COUNTER.put(access.getIPAddress(), 1);
					else
						IP_COUNTER.replace(access.getIPAddress(), IP_COUNTER.get(access.getIPAddress()) + 1);
				}


				for (String IPAddress : IP_COUNTER.keySet()){
					int requests = IP_COUNTER.get(IPAddress);
					if(requests > threshold)
						log.info(IPAddress + " Requests: " + requests);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
	}
}
