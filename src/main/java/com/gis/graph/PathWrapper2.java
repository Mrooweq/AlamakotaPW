package com.gis.graph;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PathWrapper2 {
    private List<Integer> minPath;
    private List<Integer> maxPath;
}
