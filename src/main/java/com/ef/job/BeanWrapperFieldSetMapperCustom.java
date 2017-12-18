package com.ef.job;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.util.StringUtils;
import org.springframework.validation.DataBinder;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BeanWrapperFieldSetMapperCustom <Access> extends BeanWrapperFieldSetMapper <Access> {
    @Override
    protected void initBinder(DataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (!StringUtils.isEmpty(text)) {
                    try {
                        setValue(format.parse(text));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    setValue(null);
                }
            }

            @Override
            public String getAsText() throws IllegalArgumentException {
                Object date = getValue();
                if (date != null) {
                    return (String) date;
                } else {
                    return "";
                }
            }
        });
    }
}
