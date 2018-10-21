package com.gis.test;


import com.gis.algorithm.Algorithm;
import com.gis.graph.PathWrapper;
import com.gis.common.exception.NoPathException;
import com.gis.graph.Graph;
import com.gis.graph.Vertex;
import com.gis.graph.Wrapper;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GeneralGraphTest {
    public void compare(Wrapper wrapper, List<Integer> min, List<Integer> max){
        PathWrapper paths = null;
        try {
            paths = Algorithm.getPaths(wrapper.getGraph(), wrapper.getFrom(), wrapper.getTo());
        } catch (NoPathException ignored) {}

        List<Integer> minFlowPath;
        List<Integer> maxFlowPath;

        if(paths == null){
            minFlowPath = null;
            maxFlowPath = null;
        }
        else {
            minFlowPath = Algorithm.parseListOfVerticesToIdList(paths.getMinPath());
            maxFlowPath = Algorithm.parseListOfVerticesToIdList(paths.getMaxPath());
        }

        performTest(wrapper, min, minFlowPath);
        performTest(wrapper, max, maxFlowPath);
    }

    private void performTest(Wrapper wrapper, List<Integer> expected, List<Integer> actual) {
        if(expected != null
                && actual != null
                && !expected.equals(actual)
                && getFlowOnPath(wrapper, expected) == getFlowOnPath(wrapper, actual)
                ){
            assertTrue(true);
        }
        else {
            assertEquals(expected, actual);
        }
    }

    private int getFlowOnPath(Wrapper wrapper, List<Integer> min) {
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
