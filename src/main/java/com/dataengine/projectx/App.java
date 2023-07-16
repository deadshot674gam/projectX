package com.dataengine.projectx;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;

import com.dataengine.projectx.analytic.DataAnalyzer;
import com.dataengine.projectx.review.Review;
import com.dataengine.projectx.util.Util;


public class App {
    public static void main(String[] args) throws ParseException, IOException {
        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        long startTime = System.currentTimeMillis();
        List<Review> reviewsFromCSV = Util
                .parseCSVtoReview(
                        Util.readCSV("src/main/resources/f46a7ece-a8d2-4c71-a5ef-1cc9c3df8e3e.xlsx - in.csv"));
        System.out.println("Size before dropping Noise - " + reviewsFromCSV.size());

        dataAnalyzer.dropNoise(reviewsFromCSV);
        System.out.println("Size after dropping Noise - " + reviewsFromCSV.size());
        Map<String, Integer> productPurchaseFrequencies = dataAnalyzer.analyzeProductPurchaseFrequencies(reviewsFromCSV);
        Map<String, Integer> boughtCategory = dataAnalyzer.analyzeMostBoughtCategory(reviewsFromCSV);
        
        
        dataAnalyzer.analyzeReviews(reviewsFromCSV);
        
        System.out.println("Size of productPurchaseFrequencies  - " + productPurchaseFrequencies.size());


        Map<String, Float> avgAnalysedReviewMap =  dataAnalyzer.analyzeProductWiseAnalysedReviews(reviewsFromCSV, productPurchaseFrequencies);
        List<Entry<String, Float>> sortedAnalysedReviewRanks = Util.sortHashMapFloat(avgAnalysedReviewMap, false);


        Map<String, Float> avgRatingMap =  dataAnalyzer.analyzeProductWiseAnalysedReviews(reviewsFromCSV, productPurchaseFrequencies);
        List<Entry<String, Float>> sortedAvgRating = Util.sortHashMapFloat(avgRatingMap, false);
        
        List<Entry<String, Integer>> entries = Util.sortHashMap(productPurchaseFrequencies, false);
        /** Commenting the hashMap Print as Files will be created from Output.
        for (Entry<String, Integer> entry : entries) {
            System.out.println("Product - " + entry.getKey() + " =>  Purchases - " + entry.getValue());
        }
        */
        System.out.println();
        System.out.println();
        System.out.println("Size of mostBoughtCategory  - " + boughtCategory.size());

        
        List<Entry<String, Integer>> boughEntries = Util.sortHashMap(boughtCategory, false);
        /** Commenting the hashMap Print as Files will be created from Output.
        for (Entry<String, Integer> entry : boughEntries) {
            System.out.println("Category - " + entry.getKey() + " =>  Purchases - " + entry.getValue());
        }
        */
        System.out.println();
        System.out.println();
        System.out.println("Time taken to analyze - " + (System.currentTimeMillis() - startTime));

        Util.writeEntriesToJSON("ProductFrequencyMap.json", entries);
        Util.writeEntriesToJSON("CategoryFrequencyMap.json", boughEntries);
        Util.writeFloatEntriesToJSON("ProductWiseAnalysedReviews.json", sortedAnalysedReviewRanks);
        Util.writeFloatEntriesToJSON("AvgRatingProductWise.json", sortedAvgRating);

    }
}
