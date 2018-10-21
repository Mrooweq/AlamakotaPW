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
}
