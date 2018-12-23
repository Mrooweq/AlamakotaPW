package com.gis.rmi;

import com.gis.algorithm.Algorithm;
import com.gis.common.exception.NoPathException;
import com.gis.graph.Graph;
import com.gis.graph.Vertex;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AlgorithmServiceImpl implements AlgorithmService {

    private Graph graph = null;
    private final String name;

    AlgorithmServiceImpl(String name) {
        this.name = name;
    }

    @Override
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    @Override
    public List<Vertex> findMinPath(Graph g, Vertex source, Vertex end) {
        try {
            System.out.println("Serving findMinPath remote call.");
            return Algorithm.findMinPath(g, source, end);
        } catch (NoPathException | ExecutionException | InterruptedException e) {
            System.err.println("Error during findMinPath call in " + name);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Vertex> findMaxPath(Graph g, Vertex source, Vertex end) {
        try {
            System.out.println("Serving findMaxPath remote call.");
            return Algorithm.findMaxPath(g, source, end);
        } catch (NoPathException | ExecutionException | InterruptedException e) {
            System.err.println("Error during findMaxPath call in " + name);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkIfExistsPath(Graph graph, Vertex start, Vertex end) {
        //System.out.println("Serving checkIfExistsPath remote call.");
        return Algorithm.checkIfExistsPath(graph, start, end);
    }

    @Override
    public List<Vertex> findShortestPath(Graph graph, Vertex start, Vertex stop) {
        //System.out.println("Serving findShortestPath remote call.");
        return Algorithm.findShortestPath(graph, start, stop);
    }

     private void checkGraph() {
        if (graph == null) {
            throw new RuntimeException("Graph was not set for this worker: " + name);
        }
    }
}
