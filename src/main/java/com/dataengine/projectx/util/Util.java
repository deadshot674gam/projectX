package com.dataengine.projectx.util;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.dataengine.projectx.review.Review;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class Util {

    /**
     * List of formats
     */
    public static List<String> datetimeformats = Arrays.asList(new String[] {
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyyMM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'"
    });

    /**
     * 
     * @param path - String , filepath where CSV is present
     * @return List of Records from CSV, ignores header
     * @throws IOException
     */
    public static List<String[]> readCSV(String path) throws IOException {
        FileReader fReader = new FileReader(path);
        CSVReader csvReader = new CSVReaderBuilder(fReader).withSkipLines(1).build();
        return csvReader.readAll();
    }

    /**
     * 
     * @param recordList - List of Records from CSV reader
     * @return List of Review Objects
     * @throws ParseException
     */
    public static List<Review> parseCSVtoReview(List<String[]> recordList) throws ParseException {
        List<Review> reviews = new ArrayList<>();
        for (String[] record : recordList) {
            reviews.add(new Review(record));
        }
        return reviews;
    }

    /**
     * 
     * @param review - Review Object
     * @return returns true if necessary members are empty
     */
    public static boolean hasEmpty(Review review) {
        return review.getCategories().isEmpty() ||
                review.getReviews().isEmpty() ||
                review.getProduct().isEmpty() ||
                review.getSource().isEmpty() ||
                review.getTitle().isEmpty();
    }

    /**
     * 
     * @param format - Datetime format
     * @param value  - Datetime as String
     * @param locale - Local Constant
     * @return true if format matches
     */
    public static boolean isValidFormat(String format, String value, Locale locale) {
        LocalDateTime ldt = null;
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(value, fomatter);
            String result = ldt.format(fomatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, fomatter);
                String result = ld.format(fomatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, fomatter);
                    String result = lt.format(fomatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    // e2.printStackTrace();
                }
            }
        }

        return false;
    }

    /**
     * 
     * @param dateString Datetime in String
     * @return format which matches String
     */
    public static String getDateTimeFormat(String dateString) {
        String ans = "";

        if (dateString != null && dateString.isEmpty()) {
            return "NOW";
        }

        for (String format : datetimeformats) {
            if (isValidFormat(format, dateString, Locale.US)) {
                return format;
            }
        }
        return ans;
    }

    /**
     * 
     * @param format     Datetime format , should be correct format
     * @param dateString Datetime as String
     * @return Date Object from String , if String is NOW / Empty then Current
     *         DATETIME
     * @throws ParseException if format is incorrect
     */
    public static Date getDateTimeFromString(String format, String dateString) throws ParseException {

        if (format == "" || format == "NOW") {
            return new Date();
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(dateString);
    }

    /**
     * 
     * @param data - Any data
     * @return Removes Redundant duplicate from product name ( such as same name
     *         added twice in new lines)
     */
    public static String sanitizeDuplicates(String data) {
        if (data.isEmpty() || !data.contains("\n")) {
            return data;
        }
        String[] eStrings = data.split("\n");
        eStrings[0] = eStrings[0].trim();
        eStrings[1] = eStrings[1].trim();

        if (eStrings[0].equals(eStrings[1])) {
            return eStrings[0];
        }

        return data;
    }

    /**
     * 
     * @param map hashMap to sort
     * @param asc true is asc order
     * @return List of Map Entry in mentioned order
     */
    public static List<Entry<String, Integer>> sortHashMap(Map<String, Integer> map, boolean asc) {
        List<Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
        if (asc) {
            entries.sort(Entry.<String, Integer>comparingByValue());
        } else {
            entries.sort(Entry.<String, Integer>comparingByValue().reversed());

        }

        return entries;
    }

    /**
     * 
     * @param map hashMap to sort
     * @param asc true is asc order
     * @return List of Map Entry in mentioned order
     */
    public static List<Entry<String, Float>> sortHashMapFloat(Map<String, Float> map, boolean asc) {
        List<Entry<String, Float>> entries = new ArrayList<>(map.entrySet());
        if (asc) {
            entries.sort(Entry.<String, Float>comparingByValue());
        } else {
            entries.sort(Entry.<String, Float>comparingByValue().reversed());

        }

        return entries;
    }

    /**
     * 
     * @param fileName output filename
     * @param entries  List of Map Entry
     */
    public static void writeEntriesToJSON(String fileName, List<Entry<String, Integer>> entries) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(entries);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * @param entries List of Map Entry
     */
    public static void writeFloatEntriesToJSON(String fileName, List<Entry<String, Float>> entries) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(entries);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
