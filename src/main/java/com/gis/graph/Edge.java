package com.gis.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Setter
@Getter
@AllArgsConstructor
public class Edge {
    public static int ID = 0;

    private int id;
    private int flow;

    public Edge() {
        this.id = ID++;
        this.flow = (new Random()).nextInt(100) + 1;
    }

    @Override
    public boolean equals(Object o) {
        return o.getClass() == Edge.class && ((Edge) o).getId() == id;
    }

    @Override
    public String toString(){
        return String.valueOf(id);
    }
}
