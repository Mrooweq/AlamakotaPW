package com.gis;


import com.gis.algorithm.Algorithm;
import com.gis.graph.PathWrapper;
import com.gis.common.exception.NoPathException;
import com.gis.common.fileoperations.FromFile;
import com.gis.graph.Wrapper;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        args = mock();  //////////////////////////
        long readStart = System.currentTimeMillis();
        Wrapper wrapper = FromFile.generateGraphBasedOnPathFile(args);
        long timeOfRead = System.currentTimeMillis() - readStart;
        System.out.println("\nCzas wczytywania grafu:");
        System.out.println(timeOfRead + " ms");

        if(wrapper == null){
            return;
        }

        List<Integer> minFlowPath;
        List<Integer> maxFlowPath;

        long timeOfExecution;

        try {
            long start = System.currentTimeMillis();
            PathWrapper paths = Algorithm.getPaths(wrapper.getGraph(), wrapper.getFrom(), wrapper.getTo());
            timeOfExecution = System.currentTimeMillis() - start;

            minFlowPath = Algorithm.parseListOfVerticesToIdList(paths.getMinPath());
            maxFlowPath = Algorithm.parseListOfVerticesToIdList(paths.getMaxPath());
        } catch (NoPathException e) {
            System.out.println("Nie istnieje ścieżka między podanymi wierzchołkami");
            return;
        }

        System.out.println("\nNajcieńsza ścieżka:");
        System.out.println(Arrays.toString(minFlowPath.toArray()));

        System.out.println("\nNajgrubsza ścieżka:");
        System.out.println(Arrays.toString(maxFlowPath.toArray()));

        System.out.println("\nCzas wykonania algorytmu:");
        System.out.println(timeOfExecution + " ms");
    }

    private static String[] mock() {
        String[] args;
        args = new String[3];
        args[0] = "findMinPath.txt";
        args[1] = "74";
        args[2] = "75";
        return args;
    }
}
