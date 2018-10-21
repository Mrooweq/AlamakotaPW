package com.gis.common.fileoperations;

import com.gis.graph.*;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FromFile {
    public static Wrapper generateGraphBasedOnPathFile(String[] args){
        File file = getValidFile(args);
        if (file == null) {
            return null;
        }

        List<String> linesOfFile = getLinesOfFile(file);
        if (linesOfFile == null) {
            return null;
        }

        Map<Edge, Pair<Vertex, Vertex>> graphMap = parseToGraph(linesOfFile, args[1], args[2]);
        if (graphMap == null) {
            return null;
        }

        return createGraph(graphMap);
    }

    private static Wrapper createGraph(Map<Edge, Pair<Vertex, Vertex>> graphMap) {
        Graph g = new DirectedSparseGraph();
        Vertex start = null, end = null;

        for (Map.Entry<Edge, Pair<Vertex, Vertex>> x : graphMap.entrySet()) {
            Edge edge = x.getKey();

            if(edge != null){
                Vertex vertexFrom = x.getValue().getKey();
                Vertex vertexTo = x.getValue().getValue();
                g.addEdge(edge, vertexFrom, vertexTo);
            }
            else {
                start = x.getValue().getKey();
                end = x.getValue().getValue();
            }
        }

        return new Wrapper(g, start, end);
    }

    private static Map<Edge, Pair<Vertex, Vertex>> parseToGraph(List<String> lines, String startStr, String endStr) {
        Pattern pattern = Pattern.compile("^\\s*(\\d+),\\s*(\\d+),\\s*(\\d+),\\s*(\\d+)$");
        Map<Integer, Vertex> mapOfVertices = new HashMap<>();
        Map<Edge, Pair<Vertex, Vertex>> mapOfGraph = new HashMap<>();

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);

            if(!matcher.matches()){
                return null;
            }

            int edgeId = Integer.parseInt(matcher.group(1));
            int edgeFlow = Integer.parseInt(matcher.group(2));
            int vertexFromId = Integer.parseInt(matcher.group(3));
            int vertexToId = Integer.parseInt(matcher.group(4));

            if(!mapOfVertices.containsKey(vertexFromId)){
                mapOfVertices.put(vertexFromId, new Vertex(vertexFromId));
            }

            if(!mapOfVertices.containsKey(vertexToId)){
                mapOfVertices.put(vertexToId, new Vertex(vertexToId));
            }

            mapOfGraph.put(new Edge(edgeId, edgeFlow),
                    new Pair<>(mapOfVertices.get(vertexFromId), mapOfVertices.get(vertexToId)));
        }

        Integer startId = Integer.valueOf(startStr);
        Integer endId = Integer.valueOf(endStr);

        if(mapOfVertices.get(startId) == null){
            System.out.println("Podano nieprawidłowy wierzchołek początkowy");
            return null;
        }

        if(mapOfVertices.get(endId) == null){
            System.out.println("Podano nieprawidłowy wierzchołek końcowy");
            return null;
        }

        mapOfGraph.put(null,
                new Pair<>(mapOfVertices.get(startId), mapOfVertices.get(endId)));

        return mapOfGraph;
    }

    private static List<String> getLinesOfFile(File file) {
        List<String> lines = null;

        try {
            lines = Files.lines(Paths.get(file.getAbsolutePath()))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    private static File getValidFile(String[] args) {
        if(args.length != 3){
            System.out.println("Należy podać ścieżke do pliku oraz numery id wierzchołka początkowego i końcowego");
            System.out.println("Przykład:");
            System.out.println("java -jar C:\\Users\\Lenovo\\Desktop\\gis.jar C:\\Users\\Lenovo\\Desktop\\graf.txt    0    4");
            return null;
        }

        File file = new File(args[0]);

        if(!file.exists()){
            System.out.println("Niepoprawny plik");
            return null;
        }

        try {
            Integer.valueOf(args[1]);
            Integer.valueOf(args[2]);
        }
        catch (NumberFormatException e){
            System.out.println("Podano nieprawidłowe numery id wierzchołkow");
            return null;
        }

        return file;
    }
}
