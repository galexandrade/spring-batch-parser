package com.ef.job;

import com.ef.model.Access;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class AccessItemProcessor implements ItemProcessor<Access, Access> {

    private static final Logger log = LoggerFactory.getLogger(AccessItemProcessor.class);

    @Override
    public Access process(final Access access) throws Exception {
        final String method = access.getRequest().toUpperCase();

        log.info("Converting (" + access.getDate() + ") into (" + method + ")");

        return access;
    }

}
