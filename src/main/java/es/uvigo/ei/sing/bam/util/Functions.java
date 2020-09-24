package es.uvigo.ei.sing.bam.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import lombok.experimental.UtilityClass;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@UtilityClass
public class Functions {

    private final JsonSchemaFactory JSON_SCHEMA_FACTORY;

    static {
        JSON_SCHEMA_FACTORY = JsonSchemaFactory.byDefault();
    }

    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date != null ? date.toInstant().atZone(ZoneId.of("CET")).toLocalDateTime() : null;
    }

    public static Date convertToDate(LocalDateTime date) {
        return date != null ? Date.from(date.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    public static Date unixTimestampToDate(int unixDate) {
        return new Date((long) unixDate * 1000);
    }

    public static LocalDateTime unixTimestampToLocalDateTime(int unixDate) {
        return convertToLocalDateTime(new Date((long) unixDate * 1000));
    }

    public static Date minimumDate() {
        return new GregorianCalendar(1900, Calendar.JANUARY, 1).getTime();
    }

    public static LocalDateTime minimumLocalDateTime() {
        return LocalDateTime.of(1900, 1, 1, 1, 1);
    }

    public static String doHash(String toHash) {
        String toRet = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(toHash.getBytes(StandardCharsets.UTF_8));

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < encodedHash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedHash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            toRet = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return toRet;
    }

    public static BindingResult validateSchema(String schema, String fieldName, BindingResult result) {
        if (!schema.isEmpty()) {
            try {
                // Check if the schema is valid
                JsonNode schemaNode = JsonLoader.fromString(schema);
                // If schema is not valid, get all errors
                if (!JSON_SCHEMA_FACTORY.getSyntaxValidator().schemaIsValid(schemaNode)) {
                    ProcessingReport report = JSON_SCHEMA_FACTORY.getSyntaxValidator().validateSchema(schemaNode);

                    StringBuilder schemaError = new StringBuilder();
                    report.forEach(message -> schemaError.append(message.getMessage()).append("<br/>"));
                    result.rejectValue(fieldName, null, schemaError.toString());
                }
            } catch (IOException e) {
                result.rejectValue(fieldName, null, Constants.JSON_ERROR);
            }
        }

        return result;
    }

    public static BindingResult validateJsonWithSchema(String json, String schema, String fieldName, BindingResult result) {
        if (!json.isEmpty()) {
            try {
                // Check if the schema is valid
                JsonNode schemaNode = JsonLoader.fromString(schema);
                JsonNode jsonNode = JsonLoader.fromString(json);

                // If json has errors, get them
                ProcessingReport report = JSON_SCHEMA_FACTORY.getValidator().validate(schemaNode, jsonNode);
                if (!report.isSuccess()) {
                    StringBuilder schemaError = new StringBuilder();
                    report.forEach(message -> schemaError.append(message.getMessage()).append("<br/>"));
                    result.rejectValue(fieldName, null, schemaError.toString());
                }
            } catch (IOException | ProcessingException e) {
                result.rejectValue(fieldName, null, Constants.JSON_ERROR);
            }
        }

        return result;
    }
}
