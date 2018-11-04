package com.gis.graph;

import java.util.Collection;

public interface Graph  {
    Collection<Edge> getEdges();
    Collection<Vertex> getVertices();
    void addEdge(Edge e, Vertex v1, Vertex v2);
    Collection<Vertex> getSuccessors(Vertex v1);
    Collection<Edge> getIncomingEdges(Vertex v1);
    Collection<Edge> getOutgoingEdges(Vertex v1);
    Vertex getSource(Edge e);
    Vertex getDest(Edge e);
    Graph copy();
    void removeEdges(Collection<Edge> coll);
    void removeEdge(Edge edge);
}
