package com.gis.algorithm;

import com.gis.graph.*;
import com.gis.common.exception.NoPathException;

import java.util.*;
import java.util.concurrent.*;
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
        //Start of paralleling
        //List<Vertex> minPath = findMinPath(graph, source, end);
        //List<Vertex> maxPath = findMaxPath(graph, source, end);
        //Parallel execution of finding path findMinPath and findMaxPath
        ExecutorService findPathsExecutorService = Executors.newFixedThreadPool(2);

        Callable<List<Vertex>> findMinPathCallable = () -> findMinPath(graph, source, end);
        long startMin = System.currentTimeMillis();
        Future<List<Vertex>> futureMinPath = findPathsExecutorService.submit(findMinPathCallable);

        Callable<List<Vertex>> findMaxPathCallable = () -> findMaxPath(graph, source, end);
        long startMax = System.currentTimeMillis();
        Future<List<Vertex>> futureMaxPath = findPathsExecutorService.submit(findMaxPathCallable);

        List<Vertex> maxPath = null;
        long endMax = 0;
        try {
            maxPath = futureMaxPath.get();
            endMax = System.currentTimeMillis();
            System.out.println("max: " + (endMax - startMax));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        List<Vertex> minPath = null;
        long endMin = 0;
        try {
            minPath = futureMinPath.get();
            endMin = System.currentTimeMillis();
            System.out.println("min: " + (endMin - startMin));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        findPathsExecutorService.shutdownNow();
        //End of paralleling
        PathWrapper pathWrapper = new PathWrapper(minPath, maxPath);
        pathWrapper.setMinimum(endMin - startMin);
        pathWrapper.setMaximum(endMax - startMax);

        return pathWrapper;
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

        throw new RuntimeException();
    }

    private static List<Vertex> findMinPath(Graph g, Vertex source, Vertex end) throws NoPathException, ExecutionException, InterruptedException {
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

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        for (Edge edge : listOfEdges) {
            //Start of paralleling
            //Pallalel 2 executions of method checkIfExistsPath
            Callable<Boolean> firstCallable = () -> checkIfExistsPath(g, source, graph.getSource(edge));
            Future<Boolean> futureFirst = executorService.submit(firstCallable);

            Callable<Boolean> secondCallable = () -> checkIfExistsPath(g, graph.getDest(edge), end);
            Future<Boolean> futureSecond = executorService.submit(secondCallable);

            boolean first = false;
            try {
                first = futureFirst.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            boolean second = false;
            try {
                second = futureSecond.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            //End of paralleling

            if (first && second) {
                //Start of paralleling
                //List<Vertex> firstPartPath = findShortestPath(graph, source, graph.getSource(edge));
                //List<Vertex> secondPartPath = findShortestPath(graph, graph.getDest(edge), end);
                ExecutorService shortestPathExecutorService = Executors.newFixedThreadPool(2);

                Callable<List<Vertex>> firstFindShortestCallable = () -> findShortestPath(graph, source, graph.getSource(edge));
                Future<List<Vertex>> futureFirstShortest = shortestPathExecutorService.submit(firstFindShortestCallable);

                Callable<List<Vertex>> secondFindShortestCallable = () -> findShortestPath(graph, graph.getDest(edge), end);
                Future<List<Vertex>> futureSecondShortest = shortestPathExecutorService.submit(secondFindShortestCallable);

                List<Vertex> firstPartPath = futureFirstShortest.get();
                List<Vertex> secondPartPath = futureSecondShortest.get();

                List<Vertex> sum = new ArrayList<>();
                sum.addAll(firstPartPath);
                sum.addAll(secondPartPath);

                Set<Vertex> setToEliminateRepeats = new HashSet<>();
                setToEliminateRepeats.addAll(sum);

                if (setToEliminateRepeats.size() == firstPartPath.size() + secondPartPath.size()) {
                    return sum;
                }
                shortestPathExecutorService.shutdown();
            }
        }
        executorService.shutdownNow();
        //End of paralleling
        throw new NoPathException();
    }

    private static List<Vertex> findMaxPath(Graph g, Vertex source, Vertex end) throws NoPathException, ExecutionException, InterruptedException {
        if(source == end){
            return Collections.singletonList(source);
        }

        Graph graph = g.copy();

        List<Edge> listOfEdges = new ArrayList<>();
        listOfEdges.addAll(graph.getEdges());

        if(listOfEdges.size() == 0){
            return null;
        }

        //Start of paralleling

        ExecutorService findMaxPathLoopExecutorService = Executors.newFixedThreadPool(4);

        Callable<Relation> findMaxPathLoopCallable1 = () -> loop(g, 0, source, end);
        Future<Relation> futureLol1 = findMaxPathLoopExecutorService.submit(findMaxPathLoopCallable1);

        Callable<Relation> findMaxPathLoopCallable2 = () -> loop(g, 1, source, end);
        Future<Relation> futureLol2 = findMaxPathLoopExecutorService.submit(findMaxPathLoopCallable2);

        Callable<Relation> findMaxPathLoopCallable3 = () -> loop(g, 2, source, end);
        Future<Relation> futureLol3 = findMaxPathLoopExecutorService.submit(findMaxPathLoopCallable3);

        Callable<Relation> findMaxPathLoopCallable4 = () -> loop(g, 3, source, end);
        Future<Relation> futureLol4 = findMaxPathLoopExecutorService.submit(findMaxPathLoopCallable4);

        Relation lol1 = futureLol1.get();
        Relation lol2 = futureLol2.get();
        Relation lol3 = futureLol3.get();
        Relation lol4 = futureLol4.get();


        //Relation lol1 = loop(g, 0, source, end);
        //Relation lol2 = loop(g, 1, source, end);
        //Relation lol3 = loop(g, 2, source, end);
        //Relation lol4 = loop(g, 3, source, end);

        //End of paralleling

        Relation bestRelation = Stream.of(lol1, lol2, lol3, lol4)
                .filter(Objects::nonNull)
                .findAny()
                .get();

        Vertex v1 = bestRelation.getFrom();
        Vertex v2 = bestRelation.getTo();

        //Start of paralleling
        //List<Vertex> firstPartPath = findShortestPath(graph, source, v1);
        //List<Vertex> secondPartPath = findShortestPath(graph, v2, end);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Vertex finalV1 = v1;
        Callable<List<Vertex>> callFirst = () -> findShortestPath(graph, source, finalV1);
        Future<List<Vertex>> futureFirstPathPath = executorService.submit(callFirst);

        Vertex finalV2 = v2;
        Callable<List<Vertex>> callSecond = () -> findShortestPath(graph, finalV2, end);
        Future<List<Vertex>> futureSecondPathPath = executorService.submit(callSecond);

        List<Vertex> firstPartPath = futureFirstPathPath.get();
        List<Vertex> secondPartPath = futureSecondPathPath.get();


        firstPartPath.addAll(secondPartPath);
        findMaxPathLoopExecutorService.shutdownNow();
        executorService.shutdown();
        //End of paralleling

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


    private static Graph prepareGraph(Graph inputGraph, Vertex source, Vertex end) {
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
