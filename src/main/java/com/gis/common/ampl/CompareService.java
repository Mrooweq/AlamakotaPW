package com.gis.common.ampl;

import com.gis.algorithm.Algorithm;
import com.gis.algorithm.GraphGenerator;
import com.gis.common.fileoperations.ToFile;
import com.gis.graph.*;

import java.util.*;
import java.util.stream.Collectors;

public class CompareService {
    public static final int LOOP_SIZE = 1000;

    public static void main(String[] args) {

        int index = 0;
        int optWorseThanProper = 0;
        int differentResults = 0;

        while (index < LOOP_SIZE){
            Random random = new Random();
            System.out.println("index: " + index);

            try {
                int n = 10;
                int e = n * (n-1) / 2;

                int randomN = random.nextInt(n) + 1;
                int randomE;

                if(randomN == 1){
                    continue;
                }

                do{
                    randomE = random.nextInt(e);
                } while (randomE > randomN * (randomN-1) / 2 || randomE == 0);

                Graph graph = ToFile.generateDAG(randomN, randomE);

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

                List<Integer> minAlgoPath = algoPaths.getMinPath().stream().map(Vertex::getId).collect(Collectors.toList());
                List<Integer> minOptPath = optPaths.getMinPath();
                List<Integer> maxAlgoPath = algoPaths.getMaxPath().stream().map(Vertex::getId).collect(Collectors.toList());
                List<Integer> maxOptPath = optPaths.getMaxPath();

                boolean compare = compare(wrapper, algoPaths, optPaths);

                index++;

                if(compare){
                    if(minAlgoPath.hashCode() != minOptPath.hashCode() || maxAlgoPath.hashCode() != maxOptPath.hashCode()){ //jedno z tych dwoch
                        differentResults++;
                    }
                }
                else{
                    optWorseThanProper++;
                }
            } catch (Exception ignored) {}

            Vertex.ID = 0;
            Edge.ID = 0;
        }

        System.out.println("allProper: " + index);
        System.out.println("optWorseThanProper: " + optWorseThanProper);
        System.out.println("differentResults: " + differentResults);
    }

    private static boolean compare(Wrapper wrapper, PathWrapper algoPaths, PathWrapper2 optPaths) {

        List<Integer> minAlgoPath = algoPaths.getMinPath().stream().map(Vertex::getId).collect(Collectors.toList());
        List<Integer> minOptPath = optPaths.getMinPath();
        List<Integer> maxAlgoPath = algoPaths.getMaxPath().stream().map(Vertex::getId).collect(Collectors.toList());
        List<Integer> maxOptPath = optPaths.getMaxPath();

        validatePath(wrapper, minAlgoPath);
        validatePath(wrapper, minOptPath);
        validatePath(wrapper, maxAlgoPath);
        validatePath(wrapper, maxOptPath);

        //////////

        int flowOnPathFromAlgo = getFlowOnPath(wrapper, minAlgoPath);
        int flowOnPathFromOpt = getFlowOnPath(wrapper, minOptPath);

        if(flowOnPathFromAlgo != flowOnPathFromOpt){
            return false;
        }

        /////////////

        flowOnPathFromAlgo = getFlowOnPath(wrapper, maxAlgoPath);
        flowOnPathFromOpt = getFlowOnPath(wrapper, maxOptPath);

        if(flowOnPathFromAlgo != flowOnPathFromOpt){
            return false;
        }

        return true;
    }

    private static void validatePath(Wrapper wrapper, List<Integer> path){
        Graph graph = wrapper.getGraph();
        Vertex from = wrapper.getFrom();
        Vertex to = wrapper.getTo();

        Vertex v = from;
        int i = 0;

        while (true){
            Collection<Vertex> successors = graph.getSuccessors(v);
            int finalI = i;
            Optional<Vertex> any = successors.stream().filter(x -> x.getId() == path.get(finalI + 1)).findAny();

            if(any.isPresent()){
                v = any.get();

                if(v == to){
                    return;
                }
                else {
                    i++;
                }
            }
            else{
                break;
            }
        }

        throw new IllegalArgumentException();
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
