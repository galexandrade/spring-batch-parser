package com.ef.config;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Alex P. Andrade on 19/12/2017.
 */
@Component
public class CustomJobParametersValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {

        Map<String, JobParameter> params = parameters.getParameters();

        String duration = params.get("-duration").getValue().toString();
        if(!duration.equals("hourly") || !duration.equals("daily")){
            throw new JobParametersInvalidException("Parameter duration must be HOURLY or DAILY");
        }
    }
}
