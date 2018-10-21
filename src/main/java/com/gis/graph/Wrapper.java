package com.gis.graph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Wrapper {
    private Graph graph;
    private Vertex from;
    private Vertex to;
}
