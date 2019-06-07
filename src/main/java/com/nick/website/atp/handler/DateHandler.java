package com.nick.website.atp.handler;

import com.nick.website.atp.pojo.Song;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.HandlesTypes;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Nick Yuan
 * @date 2019/6/4
 * @mood shitty
 */
@AllArgsConstructor
@NoArgsConstructor
@MappedTypes(value = String.class) //java字段
@MappedJdbcTypes(value = JdbcType.DATE)//数据库字段
@Component
public class DateHandler extends BaseTypeHandler<String> {
     private SimpleDateFormat format;
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        Date date=null;
        try {
            date = (Date) format.parse(parameter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
         ps.setDate(i,date);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Date date = rs.getDate(columnName);
        if(date==null)
        return null;
        String s = this.format.format(date);
        return s;
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Date date = rs.getDate(columnIndex);
        if(date==null)
            return null;
        String s = this.format.format(date);
        return s;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Date date = cs.getDate(columnIndex);
        if(date==null)
            return null;
        String s = this.format.format(date);
        return s;
    }
}
