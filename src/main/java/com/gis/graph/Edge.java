package com.gis.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
public class Edge implements Serializable {
    private int id;
    private int flow;

    @Override
    public boolean equals(Object o) {
        return o.getClass() == Edge.class && ((Edge) o).getId() == id;
    }

    @Override
    public String toString(){
        return String.valueOf(id);
    }
}
