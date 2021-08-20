package com.example.ad_project_kampung_unite.ml;

import java.util.ArrayList;
import java.util.List;

public class Recommendation {
    private List<Double> distance;
    private List<Integer> plandIds;
    private List<Double> product_score;
    private List<Double> total_score;




    public Recommendation(List<Double> distance, List<Integer> plandIds, List<Double> product_score, List<Double> total_score) {
        this.distance = distance;
        this.plandIds = plandIds;
        this.product_score = product_score;
        this.total_score = total_score;
    }

    public Recommendation() {
        this.distance = new ArrayList<>();
        this.plandIds = new ArrayList<>();
        this.product_score = new ArrayList<>();
        this.total_score = new ArrayList<>();
    }

    public void setDistance(List<Double> distance) {
        this.distance = distance;
    }

    public List<Double> getDistance() {
        return distance;
    }

    public List<Integer> getPlandIds() {
        return plandIds;
    }

    public List<Double> getProduct_score() {
        return product_score;
    }

    public List<Double> getTotal_score() {
        return total_score;
    }
}
