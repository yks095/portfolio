package com.dblab.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();

        errors.getFieldErrors().stream().forEach(index -> {
            try {
                gen.writeStartObject();
                gen.writeStringField("objectName", index.getObjectName());
                gen.writeStringField("field", index.getField());
                gen.writeStringField("code", index.getCode());
                gen.writeStringField("defaultMessage", index.getDefaultMessage());

                Object rejectedValue = index.getRejectedValue();
                if (rejectedValue != null){
                    gen.writeStringField("objectName", rejectedValue.toString());
                }
                gen.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        errors.getGlobalErrors().stream().forEach(index -> {
            try {
                gen.writeStartObject();
                gen.writeStringField("objectName", index.getObjectName());
                gen.writeStringField("code", index.getCode());
                gen.writeStringField("defaultMessage", index.getDefaultMessage());
                gen.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gen.writeEndArray();
    }
}
