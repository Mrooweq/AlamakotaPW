package com.gis.common.fileoperations;

import com.gis.algorithm.GraphGenerator;
import com.gis.graph.*;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ToFile {

    public static void main(String[] args) {
        generateFileBasedOnGraph(GraphGenerator.bigGraphWithOnePathWithoutPredecessor());
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
            Files.write(Paths.get("C:\\\\Users\\\\Lenovo\\\\ProjektPORR\\\\graf2.txt"), linesBuilder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("from: " + from);
        System.out.println("to: " + to);
    }
}
