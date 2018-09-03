package com.passport.domain.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chensen
 * @create 2018-09-03-11:16
 */
public class DateJsonSerializer extends JsonSerializer<Date> {
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(format.format(date));
    }
}