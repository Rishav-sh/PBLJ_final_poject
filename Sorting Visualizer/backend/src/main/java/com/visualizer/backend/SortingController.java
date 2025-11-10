package com.visualizer.backend;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sort")
@CrossOrigin(origins = "*")
public class SortingController {

    @PostMapping("/{algorithm}")
    public SortResult sort(@PathVariable String algorithm, @RequestBody List<Integer> array) {
        return SortService.sort(algorithm, array);
    }
}

