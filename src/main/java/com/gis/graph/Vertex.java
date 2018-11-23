package com.gis.graph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vertex {
    public static int ID = 0;
    private int id;

    public Vertex() {
        this.id = ID++;
    }
}
