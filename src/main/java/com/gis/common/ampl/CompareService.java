package com.gis.common.ampl;

import com.gis.algorithm.Algorithm;
import com.gis.algorithm.GraphGenerator;
import com.gis.common.fileoperations.ToFile;
import com.gis.graph.*;

import java.util.*;
import java.util.stream.Collectors;

public class CompareService {
    public static void main(String[] args) {


        for(int i=0; i<10000; i++){
            Random random = new Random();

            try {
                int n = 10;
                int e = n * (n-1) / 2;

                Graph graph = ToFile.generateDAG(random.nextInt(n), random.nextInt(e));

                for (Edge edge : graph.getEdges()) {
                    Vertex source = graph.getSource(edge);
                    Vertex dest = graph.getDest(edge);

                    System.out.println(source.getId() + " " + dest.getId() + "\t" + edge.getFlow());
                }

                System.out.println("\n");

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

                Wrapper wrapper = new Wrapper(graph, v1, v2);

                PathWrapper algoPaths = Algorithm.getPaths(wrapper.getGraph(), wrapper.getFrom(), wrapper.getTo());
                PathWrapper2 optPaths = AmplService.optimize(wrapper);

                boolean compare = compare(wrapper, algoPaths, optPaths);

                if(compare){
                    System.out.println();
                }
                else{
                    System.out.println();
                }
            } catch (Exception e) {
                System.out.println();
            }

            Vertex.ID = 0;
            Edge.ID = 0;
        }

    }

    private static boolean compare(Wrapper wrapper, PathWrapper algoPaths, PathWrapper2 optPaths) {

        List<Integer> minAlgoPath = algoPaths.getMinPath().stream()
                .map(Vertex::getId)
                .collect(Collectors.toList());

        List<Integer> minOptPath = optPaths.getMinPath();

        int flowOnPathFromAlgo = getFlowOnPath(wrapper, minAlgoPath);
        int flowOnPathFromOpt = getFlowOnPath(wrapper, minOptPath);

        if(flowOnPathFromAlgo > flowOnPathFromOpt){
            return false;
        }

        List<Integer> maxAlgoPath = algoPaths.getMaxPath().stream()
                .map(Vertex::getId)
                .collect(Collectors.toList());

        List<Integer> maxOptPath = optPaths.getMaxPath();

        flowOnPathFromAlgo = getFlowOnPath(wrapper, maxAlgoPath);
        flowOnPathFromOpt = getFlowOnPath(wrapper, maxOptPath);

        if(flowOnPathFromAlgo < flowOnPathFromOpt){
            return false;
        }

        return true;
    }

    private static int getFlowOnPath(Wrapper wrapper, List<Integer> min) {
        Graph graph = wrapper.getGraph();
        Vertex from = wrapper.getFrom();
        Vertex to = wrapper.getTo();

        Vertex v = from;
        int i = 1;
        int minFlow = Integer.MAX_VALUE;

        if(wrapper.getFrom().getId() != min.get(0)){
            return -1;
        }

        while(v != to){
            if(i == min.size()){
                return -1;
            }

            Collection<Vertex> successors = graph.getSuccessors(v);
            Integer integer = min.get(i);

            Vertex vertex = successors.stream()
                    .filter(x -> x.getId() == integer)
                    .findAny()
                    .orElse(null);

            int widthBetweenTwoVertices;
            try{
                widthBetweenTwoVertices = Algorithm.getWidthBetweenTwoVertices(graph, v, vertex);
            }
            catch (NoSuchElementException e){
                return -1;
            }

            if(vertex == null){
                return -1;
            }
            else {
                v = vertex;
                i++;
                minFlow = Math.min(minFlow, widthBetweenTwoVertices);
            }
        }

        return minFlow;
    }
}
