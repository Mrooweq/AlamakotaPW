package com.gis.graph;


import com.gis.common.exception.EdgeWithGivenIdAlreadyExistsException;
import com.gis.common.exception.ParallelEdgeException;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
public class DirectedSparseGraph implements Graph {

    private Set<Relation> relations = new HashSet<>();

    // Indeks poprawiajacy wydajnosc metod getDest i getSource
    private Map<Edge, Relation> relationsMap = new HashMap<>();

    private DirectedSparseGraph(Set<Relation> relations){
        this.relations  = new HashSet<>(relations);
        this.relationsMap = relations.stream().collect(Collectors.toMap(Relation::getEdge, Function.identity()));
    }

    @Override
    public void addEdge(Edge e, Vertex v1, Vertex v2) {
        boolean edgeWithSameIdExists = relations.stream().
                anyMatch(x -> x.getEdge().getId() == e.getId());
        if(edgeWithSameIdExists){
            throw new EdgeWithGivenIdAlreadyExistsException();
        }

        boolean edgeParallelToInputExists = relations.stream()
                .anyMatch(x -> x.getFrom() == v1 && x.getTo() == v2);
        if(edgeParallelToInputExists){
            throw new ParallelEdgeException();
        }

        Relation relation = new Relation(v1, e, v2);
        relations.add(relation);
        relationsMap.put(e, relation);
    }


    @Override
    public Collection<Edge> getEdges() {
        return relations.stream()
                .map(Relation::getEdge)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Vertex> getVertices() {
        Set<Vertex> setFrom = relations.stream()
                .map(Relation::getFrom)
                .collect(Collectors.toSet());

        Set<Vertex> setTo = relations.stream()
                .map(Relation::getTo)
                .collect(Collectors.toSet());

        setFrom.addAll(setTo);
        return setFrom;
    }

    @Override
    public Collection<Vertex> getSuccessors(Vertex v1) {
        return relations.stream()
                .filter(x -> x.getFrom() == v1)
                .map(Relation::getTo)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Edge> getIncomingEdges(Vertex v1) {
        return relations.stream()
                .filter(x -> x.getTo() == v1)
                .map(Relation::getEdge)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Edge> getOutgoingEdges(Vertex v1) {
        return relations.stream()
                .filter(x -> x.getFrom() == v1)
                .map(Relation::getEdge)
                .collect(Collectors.toList());
    }

    @Override
    public Vertex getSource(Edge e) {
        return relationsMap.get(e).getFrom();
    }

    @Override
    public Vertex getDest(Edge e) {
        return relationsMap.get(e).getTo();
    }

    @Override
    public Graph copy() {
        return new DirectedSparseGraph(relations);
    }

    @Override
    public void removeEdges(Collection<Edge> edges) {
        List<Relation> relationsToRemove = relations.stream()
                .filter(x -> edges.contains(x.getEdge()))
                .collect(Collectors.toList());
        relations.removeAll(relationsToRemove);
        for(Edge edge : edges) {
            relationsMap.remove(edge);
        }
    }

    @Override
    public void removeEdge(Edge edge) {
        relations.removeIf(x -> edge == x.getEdge());
        relationsMap.remove(edge);
    }
}
