package com.gis.common.ampl;

import com.gis.algorithm.GraphGenerator;
import com.gis.graph.Graph;
import com.gis.graph.Vertex;
import com.gis.graph.Wrapper;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmplService {

    private static final Pattern PATTERN = Pattern.compile("^(\\d)\\s(\\d)\\s{3}\\d$");

    public static void main(String[] args) {
        Wrapper wrapper = GraphGenerator.veryBigGraph();

        optimize(wrapper);
    }

    public static List<Integer> optimize(Wrapper wrapper){
        List<Integer> path = new ArrayList<>();
        try {
            generateDataFile(wrapper);
            path = getOptimalPathByOptimization();

            System.out.println("size: " + path.size());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return path;
    }

    private static void generateDataFile(Wrapper wrapper) {
        Graph graph = wrapper.getGraph();
        Vertex from = wrapper.getFrom();
        Vertex to = wrapper.getTo();
    }

    private static List<Integer> getOptimalPathByOptimization() throws Exception  {
        String start = "C:\\Users\\Lenovo\\amplide\\ampl.exe";
        String commands = "model C:\\Users\\Lenovo\\AlamakotaPW\\network.mod;\n" +
                "data C:\\Users\\Lenovo\\AlamakotaPW\\data.dat;\n" +
                "option solver knitro;\n" +
                "solve;\n" +
                "option omit_zero_rows 1;\n" +
                "display Use;\n" +
                "reset;\n";

        ProcessBuilder builder = new ProcessBuilder(start);

        Map<String, String> envs = builder.environment();
        envs.put("Path", "C:\\Users\\Lenovo\\amplide");

        Process process = builder.start();

        OutputStream stdin = process.getOutputStream();
        InputStream stdout = process.getInputStream();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        writer.write(commands);
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
