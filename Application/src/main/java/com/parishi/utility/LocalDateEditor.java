package com.parishi.utility;

import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateEditor extends PropertyEditorSupport {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.trim().isEmpty()) {
            setValue(null); // Treat empty or null input as null
        } else {
            try {
                LocalDate parsedDate = LocalDate.parse(text, formatter);
                setValue(parsedDate);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format. Please use format: " + formatter.toString());
            }
        }
    }

    @Override
    public String getAsText() {
        LocalDate value = (LocalDate) getValue();
        return (value != null ? value.format(formatter) : "");
    }
}
