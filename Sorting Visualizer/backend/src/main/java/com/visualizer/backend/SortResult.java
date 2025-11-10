package com.visualizer.backend;
import java.util.List;

public class SortResult {
    public List<Integer> sortedArray;
    public int comparisons;
    public String complexity;

    public SortResult(List<Integer> sortedArray, int comparisons, String complexity) {
        this.sortedArray = sortedArray;
        this.comparisons = comparisons;
        this.complexity = complexity;
    }
}

