package com.passport.domain.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chensen
 * @create 2018-09-03-11:17
 */
public class DateJsonDeserializer extends JsonDeserializer<Date> {
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            return format.parse(jsonParser.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}