package com.dataengine.projectx.analytic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dataengine.projectx.review.Review;
import com.dataengine.projectx.util.Util;

public class DataAnalyzer {

    private final List<String> GOOD_WORD = Arrays.asList(new String[] {
            "happy",
            "good",
            "love",
            "fine",
            "fast",
            "great",
            "nice"
    });
    private final List<String> BAD_WORD = Arrays.asList(new String[] {
            "sad",
            "upset",
            "angry",
            "hate",
            "bad",
            "slow",
            "bad"
    });

    public void dropNoise(List<Review> reviews) {
        List<Review> toRemove = new ArrayList<>();
        for (Review review : reviews) {
            if (Util.hasEmpty(review)) {
                toRemove.add(review);
            }
        }

        reviews.removeAll(toRemove);
    }

    private int getRankFromReviews(String review) {
        int rank = 0;
        for (String good : GOOD_WORD) {
            if (review.contains(good)) {
                rank++;
            }
        }
        for (String bad : BAD_WORD) {
            if (review.contains(bad)) {
                rank--;
            }
        }
        return rank;
    }

    public void analyzeReviews(List<Review> reviews) {
        for (Review review : reviews) {
            int reviewRank = getRankFromReviews(review.getReviews());
            review.setDerivedFields("reviewRank", reviewRank);
        }
    }

    public Map<String, Integer> analyzeProductPurchaseFrequencies(List<Review> reviews) {
        Map<String, Integer> productPurchaseFrequency = new HashMap<>();
        for (Review review : reviews) {
            productPurchaseFrequency.put(review.getProduct(),
                    productPurchaseFrequency.getOrDefault(review.getProduct(), 0)+1);
        }
        return productPurchaseFrequency;
    }

    public Map<String, Integer> analyzeMostBoughtCategory(List<Review> reviews) {
        Map<String, Integer> boughtCategory = new HashMap<>();
        for (Review review : reviews) {
            boughtCategory.put(review.getCategories(), boughtCategory.getOrDefault(review.getCategories(), 0)+1);
        }
        return boughtCategory;
    }

    public Map<String, Float> analyzeProductWiseAnalysedReviews(List<Review> reviews, Map<String, Integer> productBoughtFrequency) {
        Map<String, Float> productWiseAnalysedReviews = new HashMap<>();
        for (Review review : reviews) {
            productWiseAnalysedReviews.put(review.getProduct(), productWiseAnalysedReviews.getOrDefault(review.getProduct(),0f )+ ((Integer)review.getDerivedField("reviewRank")).floatValue());
        }

        for(Map.Entry<String, Float> entry : productWiseAnalysedReviews.entrySet()) {
            productWiseAnalysedReviews.put(entry.getKey(),entry.getValue() / productBoughtFrequency.get(entry.getKey()));
        }

        return productWiseAnalysedReviews;
    }

    public Map<String, Float> analyzeProductWiseReviews(List<Review> reviews, Map<String, Integer> productBoughtFrequency) {
        Map<String, Float> productWiseAnalysedReviews = new HashMap<>();
        for (Review review : reviews) {
            productWiseAnalysedReviews.put(review.getProduct(), productWiseAnalysedReviews.getOrDefault(review.getProduct(),0f )+ ((Short)review.getRating()).floatValue());
        }

        for(Map.Entry<String, Float> entry : productWiseAnalysedReviews.entrySet()) {
            productWiseAnalysedReviews.put(entry.getKey(),entry.getValue() / productBoughtFrequency.get(entry.getKey()));
        }

        return productWiseAnalysedReviews;
    }

}
