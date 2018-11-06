package com.gis.common.fileoperations;

import com.gis.algorithm.Algorithm;
import com.gis.common.exception.NoPathException;
import com.gis.graph.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ToFile {

    public static void main(String[] args) {
//        generateFileBasedOnGraph(GraphGenerator.bigGraphWithOnePathWithoutPredecessor());

        long max = -Long.MAX_VALUE;

        for(int i=0; i<10000; i++){
            try {
                Random random = new Random();

                int n = 150;
                int e = n * (n-1) / 2;

                Graph graph = generateDAG(random.nextInt(n), random.nextInt(e));

                List<Vertex> lista = graph.getVertices().stream()
                        .sorted(Comparator.comparing(Vertex::getId))
                        .collect(Collectors.toList());

                int j;
                int k;

                do{
                    j = random.nextInt(lista.size());
                    k = random.nextInt(lista.size());
                } while (j+1 > k);
                Vertex v1 = lista.get(j);
                Vertex v2 = lista.get(k);

                Graph prepareGraph = Algorithm.prepareGraph(graph, v1, v2);

                long start = System.currentTimeMillis();
                List<Vertex> minPath = Algorithm.findMinPath(prepareGraph, v1, v2);
                long end = System.currentTimeMillis() - start;

                if(end > max && !minPath.isEmpty()){
                    max = end;
                    System.out.println("xDDD: " + max + " | " + i);
                    generateFileBasedOnGraph( new Wrapper(graph, v1, v2) );
                }
            } catch (Exception ignored) {}

            Vertex.ID = 0;
            Edge.ID = 0;
        }
    }

    public static Graph generateDAG(int numOfVertices, int numOfEdges){
        int maxEdgesPossible = (numOfVertices - 1) * numOfVertices / 2;

        if(numOfEdges > maxEdgesPossible){
            throw new RuntimeException("Za dużo krawędzi");
        }

        int[][] matrix = new int[numOfVertices][numOfVertices];
        Random random = new Random();

        for (int i = 0; i < numOfEdges; i++) {
            int j;
            int k;

            do{
                j = random.nextInt(numOfVertices);
                k = random.nextInt(numOfVertices);
            } while (j+1 > k || matrix[j][k] == 1);

            matrix[j][k] = 1;
        }

        Graph graph = new DirectedSparseGraph();
        List<Vertex> vertices = new ArrayList<>();

        for(int i=0; i<numOfVertices; i++){
            vertices.add(new Vertex());
        }

        for (int i = 0; i < numOfVertices; i++) {
            for (int j = 0; j < numOfVertices; j++) {
                if(matrix[i][j] == 1){
                    graph.addEdge(new Edge(), vertices.get(i), vertices.get(j));
                }
            }
        }

        return graph;
    }


    public static void generateFileBasedOnGraph(Wrapper wrapper){
        Graph graph = wrapper.getGraph();
        Vertex from = wrapper.getFrom();
        Vertex to = wrapper.getTo();

        StringBuilder linesBuilder = new StringBuilder();

        for (Edge edge : graph.getEdges()) {
            int id = edge.getId();
            int flow = edge.getFlow();
            int src = graph.getSource(edge).getId();
            int dest = graph.getDest(edge).getId();

            String line = String.valueOf(id) + ", "
                    + String.valueOf(flow) + ", "
                    + String.valueOf(src) + ", "
                    + String.valueOf(dest);

            linesBuilder.append(line);
            linesBuilder.append(System.getProperty("line.separator"));
        }

        try {
            Files.write(Paths.get("graf2.txt"), linesBuilder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("from: " + from);
        System.out.println("to: " + to);
    }
}
