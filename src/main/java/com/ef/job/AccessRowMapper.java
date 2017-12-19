package com.ef.job;

import com.ef.model.Access;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alex.andrade on 19/12/2017.
 */
public class AccessRowMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date accessDt = null;
        try {
            accessDt = format.parse(rs.getString(3));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Access(accessDt, rs.getString(2), rs.getString(4), rs.getInt(5), rs.getString(6));
    }
}
