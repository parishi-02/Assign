package com.parishi.utility;

import java.beans.PropertyEditorSupport;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalTimeEditor extends PropertyEditorSupport {

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            // Parse the string to LocalTime using 24-hour format
            LocalTime localTime = LocalTime.parse(text, INPUT_FORMATTER);
            setValue(localTime);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format: " + text, e);
        }
    }

    @Override
    public String getAsText() {
        LocalTime localTime = (LocalTime) getValue();
        // Format LocalTime to a 12-hour time string
        return localTime != null ? localTime.format(OUTPUT_FORMATTER) : "";
    }
}
