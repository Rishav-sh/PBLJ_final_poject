package com.visualizer.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortService {

    public static SortResult sort(String algorithm, List<Integer> inputList) {
        int[] array = inputList.stream().mapToInt(i -> i).toArray();
        int comparisons = 0;
        String complexity;

        switch (algorithm.toLowerCase()) {
            case "bubble" -> {
                comparisons = bubbleSort(array);
                complexity = "O(n^2)";
            }
            case "selection" -> {
                comparisons = selectionSort(array);
                complexity = "O(n^2)";
            }
            case "insertion" -> {
                comparisons = insertionSort(array);
                complexity = "O(n^2)";
            }
            case "merge" -> {
                comparisons = mergeSort(array, 0, array.length - 1);
                complexity = "O(n log n)";
            }
            case "quick" -> {
                comparisons = quickSort(array, 0, array.length - 1);
                complexity = "O(n log n)";
            }
            case "heap" -> {
                comparisons = heapSort(array);
                complexity = "O(n log n)";
            }
            case "counting" -> {
                array = countingSort(array);
                complexity = "O(n + k)";
                comparisons = -1;
            }
            case "radix" -> {
                array = radixSort(array);
                complexity = "O(nk)";
                comparisons = -1;
            }
            case "bucket" -> {
                array = bucketSort(array);
                complexity = "O(n + k)";
                comparisons = -1;
            }
            default -> throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }

        List<Integer> sortedList = new ArrayList<>();
        for (int num : array) sortedList.add(num);

        return new SortResult(sortedList, comparisons, complexity);
    }

    // Bubble Sort
    private static int bubbleSort(int[] arr) {
        int comps = 0;
        for (int i = 0; i < arr.length - 1; i++)
            for (int j = 0; j < arr.length - i - 1; j++, comps++)
                if (arr[j] > arr[j + 1]) {
                    int t = arr[j]; arr[j] = arr[j + 1]; arr[j + 1] = t;
                }
        return comps;
    }

    // Selection Sort
    private static int selectionSort(int[] arr) {
        int comps = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < arr.length; j++, comps++)
                if (arr[j] < arr[min]) min = j;
            int t = arr[i]; arr[i] = arr[min]; arr[min] = t;
        }
        return comps;
    }

    // Insertion Sort
    private static int insertionSort(int[] arr) {
        int comps = 0;
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i], j = i - 1;
            while (j >= 0 && ++comps > 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
        return comps;
    }

    // Merge Sort
    private static int mergeSort(int[] arr, int left, int right) {
        int comps = 0;
        if (left < right) {
            int mid = (left + right) / 2;
            comps += mergeSort(arr, left, mid);
            comps += mergeSort(arr, mid + 1, right);
            comps += merge(arr, left, mid, right);
        }
        return comps;
    }

    private static int merge(int[] arr, int l, int m, int r) {
        int comps = 0;
        int n1 = m - l + 1, n2 = r - m;
        int[] L = new int[n1], R = new int[n2];

        System.arraycopy(arr, l, L, 0, n1);
        System.arraycopy(arr, m + 1, R, 0, n2);

        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2 && ++comps > 0) {
            if (L[i] <= R[j]) arr[k++] = L[i++];
            else arr[k++] = R[j++];
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
        return comps;
    }

    // Quick Sort
    private static int quickSort(int[] arr, int low, int high) {
        int comps = 0;
        if (low < high) {
            int[] res = partition(arr, low, high);
            comps += res[1];
            comps += quickSort(arr, low, res[0] - 1);
            comps += quickSort(arr, res[0] + 1, high);
        }
        return comps;
    }

    private static int[] partition(int[] arr, int low, int high) {
        int pivot = arr[high], i = low - 1, comps = 0;
        for (int j = low; j < high; j++, comps++) {
            if (arr[j] <= pivot) {
                i++;
                int t = arr[i]; arr[i] = arr[j]; arr[j] = t;
            }
        }
        int t = arr[i + 1]; arr[i + 1] = arr[high]; arr[high] = t;
        return new int[]{i + 1, comps};
    }

    // Heap Sort
    private static int heapSort(int[] arr) {
        int comps = 0;
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--)
            comps += heapify(arr, n, i);
        for (int i = n - 1; i >= 0; i--) {
            int t = arr[0]; arr[0] = arr[i]; arr[i] = t;
            comps += heapify(arr, i, 0);
        }
        return comps;
    }

    private static int heapify(int[] arr, int n, int i) {
        int comps = 0;
        int largest = i, l = 2 * i + 1, r = 2 * i + 2;
        if (l < n && ++comps > 0 && arr[l] > arr[largest]) largest = l;
        if (r < n && ++comps > 0 && arr[r] > arr[largest]) largest = r;
        if (largest != i) {
            int t = arr[i]; arr[i] = arr[largest]; arr[largest] = t;
            comps += heapify(arr, n, largest);
        }
        return comps;
    }

    // Counting Sort
    private static int[] countingSort(int[] arr) {
        int max = Arrays.stream(arr).max().orElse(0);
        int[] count = new int[max + 1];
        for (int j : arr) count[j]++;
        int idx = 0;
        for (int i = 0; i < count.length; i++)
            for (int j = 0; j < count[i]; j++) arr[idx++] = i;
        return arr;
    }

    // Radix Sort
    private static int[] radixSort(int[] arr) {
        int max = Arrays.stream(arr).max().orElse(0);
        for (int exp = 1; max / exp > 0; exp *= 10)
            countingSortForRadix(arr, exp);
        return arr;
    }

    private static void countingSortForRadix(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];
        for (int j : arr) count[(j / exp) % 10]++;
        for (int i = 1; i < 10; i++) count[i] += count[i - 1];
        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }
        System.arraycopy(output, 0, arr, 0, n);
    }

    // Bucket Sort
    private static int[] bucketSort(int[] arr) {
        int n = arr.length;
        if (n <= 0) return arr;

        int max = Arrays.stream(arr).max().orElse(0);
        List<List<Integer>> buckets = new ArrayList<>(n);

        for (int i = 0; i < n; i++) buckets.add(new ArrayList<>());

        for (int num : arr) {
            int idx = num * n / (max + 1);
            buckets.get(idx).add(num);
        }

        int idx = 0;
        for (List<Integer> bucket : buckets) {
            Collections.sort(bucket);
            for (int val : bucket) arr[idx++] = val;
        }

        return arr;
    }
}
