package com.gis.common.ampl;

import com.gis.algorithm.Algorithm;
import com.gis.algorithm.GraphGenerator;
import com.gis.common.exception.NoPathException;
import com.gis.graph.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AmplService {

    private static final Pattern PATTERN = Pattern.compile("^(\\d)\\s(\\d)\\s{3}\\d$");

    public static void main(String[] args) {
        Wrapper wrapper = GraphGenerator.smallGraph3();

        try {
            PathWrapper algoPaths = Algorithm.getPaths(wrapper.getGraph(), wrapper.getFrom(), wrapper.getTo());
            PathWrapper2 optPaths = optimize(wrapper);

            System.out.println();

        } catch (NoPathException e) {
            e.printStackTrace();
        }
    }

    public static PathWrapper2 optimize(Wrapper wrapper){
        try {
            generateDataFile(wrapper);
            return getOptimalPathByOptimization();
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Nie udalo sie przeprowadzic optymalizacji");
        }
    }

    private static void generateDataFile(Wrapper wrapper) {
        String setInter = "set INTER := ";
        String entr = "param entr := ";
        String exit = "param exit := ";
        String param = "param:  ROADS:  flow := ";

        Graph graph = wrapper.getGraph();
        String from = String.valueOf(wrapper.getFrom().getId());
        String to = String.valueOf(wrapper.getTo().getId());

        String setInterInput = graph.getVertices().stream()
                .map(Vertex::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(" "));

        StringBuilder sb = new StringBuilder();

        sb.append(setInter);
        sb.append(setInterInput);
        sb.append(";");
        sb.append(System.getProperty("line.separator"));
        sb.append(entr);
        sb.append(from);
        sb.append(";");
        sb.append(System.getProperty("line.separator"));
        sb.append(exit);
        sb.append(to);
        sb.append(";");
        sb.append(System.getProperty("line.separator"));
        sb.append(param);
        sb.append(System.getProperty("line.separator"));

        for (Iterator<Edge> iterator = graph.getEdges().iterator(); iterator.hasNext(); ) {
            Edge edge = iterator.next();
            String source = String.valueOf(graph.getSource(edge).getId());
            String dest = String.valueOf(graph.getDest(edge).getId());

            String line = "\t" + "\t" + source + " " + dest + "\t" + "\t" + String.valueOf(edge.getFlow());

            if(iterator.hasNext()){
                line = line + ",";
            }
            else {
                line = line + ";";
            }

            sb.append(line);
            sb.append(System.getProperty("line.separator"));
        }

        try {
            Files.write(Paths.get("data.dat"), sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static PathWrapper2 getOptimalPathByOptimization() throws Exception  {
        String minCommand = "model C:\\Users\\Lenovo\\AlamakotaPW\\networkMin.mod;\n" +
                "data C:\\Users\\Lenovo\\AlamakotaPW\\data.dat;\n" +
                "option solver knitro;\n" +
                "solve;\n" +
                "option omit_zero_rows 1;\n" +
                "display Use;\n" +
                "reset;\n";

        String maxCommand = "model C:\\Users\\Lenovo\\AlamakotaPW\\networkMax.mod;\n" +
                "data C:\\Users\\Lenovo\\AlamakotaPW\\data.dat;\n" +
                "option solver knitro;\n" +
                "solve;\n" +
                "option omit_zero_rows 1;\n" +
                "display Use;\n" +
                "reset;\n";

        List<Integer> minPathByOptimization = getOptPathByOptimization(minCommand);
        List<Integer> maxPathByOptimization = getOptPathByOptimization(maxCommand);

        return new PathWrapper2(minPathByOptimization, maxPathByOptimization);
    }

    private static List<Integer> getOptPathByOptimization(String command) throws Exception  {
        String start = "C:\\Users\\Lenovo\\amplide\\ampl.exe";

        ProcessBuilder builder = new ProcessBuilder(start);

        Map<String, String> envs = builder.environment();
        envs.put("Path", "C:\\Users\\Lenovo\\amplide");

        Process process = builder.start();

        OutputStream stdin = process.getOutputStream();
        InputStream stdout = process.getInputStream();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        writer.write(command);
        writer.flush();
        writer.close();

        Scanner scanner = new Scanner(stdout);

        List<Integer> path = new ArrayList<>();

        while (scanner.hasNextLine()) {
            Matcher matcher = PATTERN.matcher(scanner.nextLine());

            if(matcher.matches()) {
                int a = Integer.parseInt(matcher.group(1));
                int b = Integer.parseInt(matcher.group(2));

                if(!path.contains(a)){
                    path.add(a);
                }

                if(!path.contains(b)){
                    path.add(b);
                }
            }
        }

        stdin.close();
        stdout.close();
        process.destroy();
        return path;
    }
}
