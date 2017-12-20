package com.ef.job.step2;

import com.ef.job.JobCompletionNotificationListener;
import com.ef.model.Access;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by Alex P. Andrade on 19/12/2017.
 */
@Component
public class BlockedIPAddressProcessor implements ItemProcessor<Access, Access> {
    private static final Logger logger = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Override
    public Access process(Access access) throws Exception {
        logger.info("Blocked IPAddress -> " + access.getIPAddress());
        return access;
    }
}
