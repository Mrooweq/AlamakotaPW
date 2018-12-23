package com.gis.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Relation implements Serializable {
    private Vertex from;
    private Edge edge;
    private Vertex to;

    @Override
    public String toString(){
        return String.valueOf(edge.getId());
    }
}
