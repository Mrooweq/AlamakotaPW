package com.gis.algorithm;

import com.gis.common.exception.NoPathException;
import com.gis.graph.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SuppressWarnings("Duplicates")
public class Algorithm {
    private static final Comparator<Edge> COMPARATOR = (x, y) -> {
        if(x.getFlow() > y.getFlow())
            return 1;
        else if(x.getFlow() < y.getFlow())
            return -1;
        else
            return 0;
    };

    public static PathWrapper getPaths(Graph inputGraph, Vertex source, Vertex end) throws NoPathException {
        long start = System.currentTimeMillis();

        Graph graph = prepareGraph(inputGraph, source, end);

        ////////

        if(!checkIfExistsPath(graph, source, end)){
            throw new NoPathException();
        }

        List<Vertex> minPath = null;
        try{
            minPath = findMinPath(graph, source, end);
        } catch (NoPathException ignored){}

        List<Vertex> maxPath = null;
        try{
            maxPath = findMaxPath(graph, source, end);
        } catch (NoPathException ignored){ }

        return new PathWrapper(minPath, maxPath);
    }

    private static List<Vertex> findShortestPath(Graph g, Vertex start, Vertex stop) {
        Queue <Vertex> toVisit = new LinkedList<>();
        Set <Vertex> alreadyVisited = new HashSet<>();

        Map <Vertex, Vertex> parent = new HashMap<>();
        alreadyVisited.add(start);
        toVisit.add(start);

        parent.put(start, null);
        while(!toVisit.isEmpty()){
            Vertex cur = toVisit.poll();

            if(cur == stop){
                Vertex at = cur;
                List<Vertex> toReturn = new ArrayList<>();

                while(at != null){
                    toReturn.add(at);
                    at = parent.get(at);
                }

                Collections.reverse(toReturn);
                return toReturn;
            }
            for(Edge e : g.getOutgoingEdges(cur)){
                Vertex dest = g.getDest(e);

                if(!alreadyVisited.contains(dest)){
                    parent.put(dest, cur);
                    alreadyVisited.add(dest);
                    toVisit.offer(dest);
                }
            }
        }

        return null;
    }

    public static List<Vertex> findMinPath(Graph g, Vertex source, Vertex end) throws NoPathException {
        if(source == end){
            return Collections.singletonList(source);
        }

        Graph graph = g.copy();

        List<Edge> listOfEdges = new ArrayList<>();
        listOfEdges.addAll(graph.getEdges());

        if(listOfEdges.size() == 0){
            return null;
        }

        listOfEdges.sort(COMPARATOR);

        for (Edge edge : listOfEdges) {
            boolean first = checkIfExistsPath(g, source, graph.getSource(edge));
            boolean second = checkIfExistsPath(g, graph.getDest(edge), end);

            if (first && second) {
                List<Vertex> firstPartPath = findShortestPath(graph, source, graph.getSource(edge));
                List<Vertex> secondPartPath = findShortestPath(graph, graph.getDest(edge), end);

                List<Vertex> sum = new ArrayList<>();
                sum.addAll(firstPartPath);
                sum.addAll(secondPartPath);

                Set<Vertex> setToEliminateRepeats = new HashSet<>();
                setToEliminateRepeats.addAll(sum);

                if (setToEliminateRepeats.size() == firstPartPath.size() + secondPartPath.size()) {
                    return sum;
                }
            }
        }

        throw new NoPathException();
    }

    public static List<Vertex> findMaxPath(Graph g, Vertex source, Vertex end) throws NoPathException {
        if(source == end){
            return Collections.singletonList(source);
        }

        Graph graph = g.copy();

        List<Edge> listOfEdges = new ArrayList<>();
        listOfEdges.addAll(graph.getEdges());

        if(listOfEdges.size() == 0){
            return null;
        }

        Relation lol1 = loop(g, 0, source, end);
        Relation lol2 = loop(g, 1, source, end);
        Relation lol3 = loop(g, 2, source, end);
        Relation lol4 = loop(g, 3, source, end);

        Relation bestRelation = Stream.of(lol1, lol2, lol3, lol4)
                .filter(Objects::nonNull)
                .findAny()
                .get();

        Vertex v1 = bestRelation.getFrom();
        Vertex v2 = bestRelation.getTo();

        List<Vertex> firstPartPath = findShortestPath(graph, source, v1);
        List<Vertex> secondPartPath = findShortestPath(graph, v2, end);

        firstPartPath.addAll(secondPartPath);
        return firstPartPath;
    }

    private static Relation loop(Graph oryginalGraph, int phaseIndex, Vertex source, Vertex end) throws NoPathException {
        Edge edgeSaved;
        Vertex v1, v2;

        Graph copy = oryginalGraph.copy();
        List<Edge> sortedEdges = copy.getEdges().stream()
                .sorted(COMPARATOR)
                .collect(Collectors.toList());

        for (int j = 0; j < phaseIndex; j++) {
            if(!sortedEdges.isEmpty()){
                Edge remove = sortedEdges.remove(0);
                copy.removeEdge(remove);
            }
        }

        while (true){
            if(sortedEdges.isEmpty()){
                return null;
            }

            edgeSaved = sortedEdges.get(0);
            v1 = copy.getSource(edgeSaved);
            v2 = copy.getDest(edgeSaved);

            boolean ifPathExists1 = checkIfExistsPath(copy, source, end);
            copy.removeEdge(edgeSaved);
            boolean ifPathExists2 = checkIfExistsPath(copy, source, end);

            if(ifPathExists1 && !ifPathExists2){
                return new Relation(v1, edgeSaved, v2);
            }
            else if(!ifPathExists2){
                return null;
            }

            copy.addEdge(edgeSaved, v1, v2);

            for (int j = 0; j < 4; j++) {
                if(!sortedEdges.isEmpty()){
                    Edge remove = sortedEdges.remove(0);
                    copy.removeEdge(remove);
                }
            }
        }
    }

    public static boolean checkIfExistsPath(Graph graph, Vertex start, Vertex end) {
        if(start == end){
            return true;
        }
        Set<Vertex> visited = new HashSet<>();
        LinkedList<Vertex> queue = new LinkedList<>();
        visited.add(start);
        queue.add(start);

        while (queue.size() != 0) {
            Vertex v = queue.poll();
            Collection<Vertex> successors = graph.getSuccessors(v);

            for (Vertex successor : successors) {
                if (successor == end) {
                    return true;
                }

                if (!visited.contains(successor)) {
                    visited.add(successor);
                    queue.add(successor);
                }
            }
        }
        return false;
    }

    public static List<Integer> parseListOfVerticesToIdList(List<Vertex> list){
        if(list.size() == 0)
            System.out.println();

        if (list == null){
            return null;
        }

        return list.stream()
                .map(Vertex::getId)
                .collect(Collectors.toList());
    }


    public static Graph prepareGraph(Graph inputGraph, Vertex source, Vertex end) {
        Graph graph = inputGraph.copy();

        Set<Edge> edgesToBeRemoved = graph.getEdges().stream()
                .filter(x -> x.getFlow() == 0)
                .collect(Collectors.toSet());

        Collection<Edge> incomingEdges = graph.getIncomingEdges(source);
        Collection<Edge> outgoingEdges = graph.getOutgoingEdges(end);

        edgesToBeRemoved.addAll(incomingEdges);
        edgesToBeRemoved.addAll(outgoingEdges);

        graph.removeEdges(edgesToBeRemoved);

        return graph;
    }

    public static int getWidthBetweenTwoVertices(Graph graph, Vertex from, Vertex to) {
        Collection<Edge> outEdges = graph.getOutgoingEdges(from);

        return outEdges.stream()
                .filter(x -> graph.getDest(x) == to)
                .min(Comparator.comparing(Edge::getFlow))
                .get()
                .getFlow();
    }
}
