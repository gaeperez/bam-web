package es.uvigo.ei.sing.bam.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.Map;

@Log4j2
@Converter(autoApply = true)
public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, Object> jsonValue) {
        String databaseValue = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            databaseValue = objectMapper.writeValueAsString(jsonValue);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error", e);
        }

        return databaseValue;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String databaseValue) {
        Map<String, Object> jsonValue = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonValue = objectMapper.readValue(databaseValue, Map.class);
        } catch (final IOException e) {
            log.error("JSON reading error", e);
        }

        return jsonValue;
    }
}