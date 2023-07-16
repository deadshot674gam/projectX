package com.dataengine.projectx.review;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.dataengine.projectx.util.Util;

public class Review {

    private String product;
    private String source;
    private String categories;
    private Date date;
    private boolean didPurchase;
    private boolean doRecommend;
    private short rating;
    private String reviews;
    private String title;
    private Map<String, Object> derivedFields = new HashMap<>();

    public Map<String, Object> getDerivedFields() {
        return derivedFields;
    }

    public  Object getDerivedField(String field) {
        return derivedFields.get(field);
    }

    public void setDerivedFields(String field, Object value) {
        this.derivedFields.put(field, value);
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getDidPurchase() {
        return didPurchase;
    }

    public void setDidPurchase(boolean didPurchase) {
        this.didPurchase = didPurchase;
    }

    public boolean getDoRecommend() {
        return doRecommend;
    }

    public void setDoRecommend(boolean doRecommend) {
        this.doRecommend = doRecommend;
    }

    public short getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Review(String[] record) throws ParseException {
        this.product = Util.sanitizeDuplicates(record[0].trim()).replace("\n", " ");
        this.source = record[1];
        this.categories = record[2];
        this.date = Util.getDateTimeFromString(Util.getDateTimeFormat(record[3]), record[3]);
        this.didPurchase = record[4] == "TRUE";
        this.doRecommend = record[5] == "TRUE";
        this.rating = Short.parseShort(record[6].isEmpty()? "-1": record[6]);
        this.reviews = record[7];
        this.title = record[8];
    }

    
}
