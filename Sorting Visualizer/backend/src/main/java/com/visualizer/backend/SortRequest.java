package com.visualizer.backend;

import java.util.List;

public class SortRequest {
    private String algorithm;
    private List<Integer> array;

    // Required: Getters and Setters
    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public List<Integer> getArray() {
        return array;
    }

    public void setArray(List<Integer> array) {
        this.array = array;
    }
}

