package com.gis.algorithm;

import com.gis.common.exception.NoPathException;
import com.gis.graph.Edge;
import com.gis.graph.Graph;
import com.gis.graph.PathWrapper;
import com.gis.graph.Vertex;
import com.gis.rmi.AlgorithmClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.gis.rmi.AlgorithmClient.WORKER_MAX_PATH;
import static com.gis.rmi.AlgorithmClient.WORKER_MIN_PATH;


@SuppressWarnings("Duplicates")
public class Algorithm {
    private static final Comparator<Edge> COMPARATOR = (x, y) -> {
        if (x.getFlow() > y.getFlow())
            return 1;
        else if (x.getFlow() < y.getFlow())
            return -1;
        else
            return 0;
    };

    public static PathWrapper getPaths(Graph inputGraph, Vertex source, Vertex end) throws NoPathException {

        Graph graph = prepareGraph(inputGraph, source, end);

        ////////

        if (!checkIfExistsPath(graph, source, end)) {
            throw new NoPathException();
        }
        //Start of paralleling with RMI usage
        //Parallel execution of finding path findMinPath and findMaxPath
        ExecutorService findPathsExecutorService = Executors.newFixedThreadPool(2);

        long startMin = System.currentTimeMillis();
        AlgorithmClient minPathClient = AlgorithmClient.of(WORKER_MIN_PATH);
        Future<List<Vertex>> futureMinPath = findPathsExecutorService.submit(
                () -> minPathClient.findMinPath(graph, source, end)
        );

        long startMax = System.currentTimeMillis();
        AlgorithmClient maxPathClient = AlgorithmClient.of(WORKER_MAX_PATH);
        Future<List<Vertex>> futureMaxPath = findPathsExecutorService.submit(
                () -> maxPathClient.findMaxPath(graph, source, end)
        );

        List<Vertex> minPath = null;
        long endMin = 0;
        try {
            minPath = futureMinPath.get();
            endMin = System.currentTimeMillis();
            System.out.println("min: " + (endMin - startMin));


        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        List<Vertex> maxPath = null;
        long endMax = 0;
        try {
            maxPath = futureMaxPath.get();
            endMax = System.currentTimeMillis();
            System.out.println("max: " + (endMax - startMax));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //End of paralleling
        findPathsExecutorService.shutdownNow();
        PathWrapper pathWrapper = new PathWrapper(minPath, maxPath);
        pathWrapper.setMinimum(endMin - startMin);
        pathWrapper.setMaximum(endMax - startMax);

        return pathWrapper;
    }

    public static List<Vertex> findShortestPath(Graph g, Vertex start, Vertex stop) {
        Queue<Vertex> toVisit = new LinkedList<>();
        Set<Vertex> alreadyVisited = new HashSet<>();

        Map<Vertex, Vertex> parent = new HashMap<>();
        alreadyVisited.add(start);
        toVisit.add(start);

        parent.put(start, null);
        while (!toVisit.isEmpty()) {
            Vertex cur = toVisit.poll();

            if (cur == stop) {
                Vertex at = cur;
                List<Vertex> toReturn = new ArrayList<>();

                while (at != null) {
                    toReturn.add(at);
                    at = parent.get(at);
                }

                Collections.reverse(toReturn);
                return toReturn;
            }
            for (Edge e : g.getOutgoingEdges(cur)) {
                Vertex dest = g.getDest(e);

                if (!alreadyVisited.contains(dest)) {
                    parent.put(dest, cur);
                    alreadyVisited.add(dest);
                    toVisit.offer(dest);
                }
            }
        }

        throw new RuntimeException();
    }

    public static List<Vertex> findMinPath(Graph g, Vertex source, Vertex end) throws NoPathException, ExecutionException, InterruptedException {
        if (source == end) {
            return Collections.singletonList(source);
        }

        Graph graph = g.copy();

        List<Edge> listOfEdges = new ArrayList<>(graph.getEdges());

        if (listOfEdges.size() == 0) {
            return null;
        }

        listOfEdges.sort(COMPARATOR);

        for (Edge edge : listOfEdges) {
            //Start of paralleling
            //Pallalel 2 executions of method checkIfExistsPath
            ExecutorService executorService = Executors.newFixedThreadPool(2);

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
            executorService.shutdownNow();


            if (first && second) {
                //Start of paralleling
                //List<Vertex> firstPartPath = findShortestPath(graph, source, graph.getSource(edge));
                //List<Vertex> secondPartPath = findShortestPath(graph, graph.getDest(edge), end);
                ExecutorService shortestPathExecutorService = Executors.newFixedThreadPool(2);

                Callable<List<Vertex>> firstFindShortestCallable = () -> findShortestPath(graph, source,
                        graph.getSource(edge));
                Future<List<Vertex>> futureFirstShortest = shortestPathExecutorService.submit(
                        firstFindShortestCallable);

                Callable<List<Vertex>> secondFindShortestCallable = () -> findShortestPath(graph, graph.getDest(edge),
                        end);
                Future<List<Vertex>> futureSecondShortest = shortestPathExecutorService.submit(
                        secondFindShortestCallable);

                List<Vertex> firstPartPath = futureFirstShortest.get();
                List<Vertex> secondPartPath = futureSecondShortest.get();

                List<Vertex> sum = new ArrayList<>();
                sum.addAll(firstPartPath);
                sum.addAll(secondPartPath);

                Set<Vertex> setToEliminateRepeats = new HashSet<>(sum);
                //End of paralleling
                shortestPathExecutorService.shutdownNow();

                if (setToEliminateRepeats.size() == firstPartPath.size() + secondPartPath.size()) {
                    return sum;
                }
            }
        }
        throw new NoPathException();
    }

    public static List<Vertex> findMaxPath(Graph g, Vertex source, Vertex end) throws NoPathException, ExecutionException, InterruptedException {
        if (source == end) {
            return Collections.singletonList(source);
        }

        Graph graph = g.copy();

        List<Edge> listOfEdges = new ArrayList<>(graph.getEdges());

        if (listOfEdges.size() == 0) {
            return null;
        }

        listOfEdges.sort(COMPARATOR);

        Edge edgeSaved = null;
        Vertex v1 = null, v2 = null;

        for (int i = 0; i < listOfEdges.size(); i++) {
            edgeSaved = listOfEdges.get(i);
            v1 = graph.getSource(edgeSaved);
            v2 = graph.getDest(edgeSaved);

            graph.removeEdge(edgeSaved);

            boolean ifPathExists = checkIfExistsPath(graph, source, end);

            if (!ifPathExists) {
                break;
            }

            if (i == listOfEdges.size() - 1) {
                throw new NoPathException();
            }
        }

        graph.addEdge(edgeSaved, v1, v2);
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
        //End of paralleling
        executorService.shutdownNow();
        return firstPartPath;
    }

    public static boolean checkIfExistsPath(Graph graph, Vertex start, Vertex end) {
        if (start == end) {
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

    public static List<Integer> parseListOfVerticesToIdList(List<Vertex> list) {
        if (list.size() == 0)
            System.out.println();

        if (list == null) {
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
