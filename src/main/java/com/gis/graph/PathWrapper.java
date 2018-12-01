package com.gis.graph;


import com.gis.graph.Vertex;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PathWrapper {
    private List<Vertex> minPath;
    private List<Vertex> maxPath;
    private long minimum;
    private long maximum;

    public PathWrapper(List<Vertex> minPath, List<Vertex> maxPath) {
        this.minPath = minPath;
        this.maxPath = maxPath;
    }
}
