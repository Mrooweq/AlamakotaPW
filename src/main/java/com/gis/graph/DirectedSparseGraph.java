package com.gis.graph;


import com.gis.common.exception.EdgeWithGivenIdAlreadyExistsException;
import com.gis.common.exception.ParallelEdgeException;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class DirectedSparseGraph implements Graph {

    private Set<Relation> relations = new HashSet<>();

    private DirectedSparseGraph(Set<Relation> relations){
        Set<Relation> relations2 = new HashSet<>();
        relations2.addAll(relations);
        this.relations = relations2;
    }

    @Override
    public void addEdge(Edge e, Vertex v1, Vertex v2) {
        long numberOfEdgesWithSameId = relations.stream()
                .map(x -> x.getEdge().getId())
                .filter(x -> x == e.getId())
                .count();

        if(numberOfEdgesWithSameId > 0){
            throw new EdgeWithGivenIdAlreadyExistsException();
        }

        long numberOfEdgesParallelToInput = relations.stream()
                .filter(x -> x.getFrom() == v1 && x.getTo() == v2)
                .count();

        if(numberOfEdgesParallelToInput > 0){
            throw new ParallelEdgeException();
        }

        relations.add( new Relation(v1, e, v2) );
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
        return relations.stream()
                .filter(x -> x.getEdge() == e)
                .map(Relation::getFrom)
                .findAny().get();
    }

    @Override
    public Vertex getDest(Edge e) {
        return relations.stream()
                .filter(x -> x.getEdge() == e)
                .map(Relation::getTo)
                .findAny().get();
    }

    @Override
    public Graph copy() {
        return new DirectedSparseGraph(relations);
    }

    @Override
    public void removeEdges(Collection<Edge> coll) {
        List<Relation> relationsToRemove = relations.stream()
                .filter(x -> coll.contains(x.getEdge()))
                .collect(Collectors.toList());

        relations.removeAll(relationsToRemove);
    }
}
