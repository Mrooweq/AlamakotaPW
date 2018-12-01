package com.gis;


import com.gis.algorithm.Algorithm;
import com.gis.graph.PathWrapper;
import com.gis.common.exception.NoPathException;
import com.gis.common.fileoperations.FromFile;
import com.gis.graph.Wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<String> najgrubszeSciezki = new ArrayList<>();

        List<Long> minima = new ArrayList<>();
        List<Long> maxima = new ArrayList<>();
        List<Long> executionTimes = new ArrayList<>();


        for (int i = 0; i < 10; i++) {
            args = mockForLongEqualTimeForBothPaths();  //////////////////////////
            long readStart = System.currentTimeMillis();
            Wrapper wrapper = FromFile.generateGraphBasedOnPathFile(args);
            long timeOfRead = System.currentTimeMillis() - readStart;
            System.out.println("\n\nITERACJA " + i);
            System.out.println("\nCzas wczytywania grafu:");
            System.out.println(timeOfRead + " ms");

            if(wrapper == null){
                return;
            }

            List<Integer> minFlowPath;
            List<Integer> maxFlowPath;

            long timeOfExecution;

            PathWrapper paths = null;
            try {
                long start = System.currentTimeMillis();
                paths = Algorithm.getPaths(wrapper.getGraph(), wrapper.getFrom(), wrapper.getTo());
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
            String najgrubsza = Arrays.toString(maxFlowPath.toArray());
            najgrubszeSciezki.add(najgrubsza);
            System.out.println(najgrubsza);

            System.out.println("\nCzas wykonania algorytmu:");
            System.out.println(timeOfExecution + " ms");

            minima.add(paths.getMinimum());
            maxima.add(paths.getMaximum());
            executionTimes.add(timeOfExecution);
        }

        long sumaMinimow = 0;
        for (long s: minima) {
            sumaMinimow += s;
        }

        long sumaMaximow = 0;
        for (long s: maxima) {
            sumaMaximow += s;
        }

        long sumaCalosc = 0;
        for (long s: executionTimes) {
            sumaCalosc += s;
        }

        long sredniaMinimow = sumaMinimow / minima.size();
        System.out.println("\nŚrednia czasów odnalezienia najcieńszej ścieżki: " + sredniaMinimow + " ms");

        long sredniaMaximow = sumaMaximow / maxima.size();
        System.out.println("\nŚrednia czasów odnalezienia najgrubszej ścieżki: " + sredniaMaximow + " ms");

        long sredniaCalosc = sumaCalosc / executionTimes.size();
        System.out.println("\nŚrednia czas działania algorytmu: " + sredniaCalosc + " ms");

        System.out.println("\nCzasy odnalezienia najcieńszych ścieżek");
        for(Long min: minima) {
            System.out.println((long) min + " ms");
        }

        System.out.println("\nCzasy odnalezienia najgrubszych ścieżek");
        for(Long max: maxima) {
            System.out.println((long) max + " ms");
        }

        System.out.println("\nCzasy działania algorytmu");
        for(Long t: executionTimes) {
            System.out.println((long) t + " ms");
        }
    }

    private static String[] mock() {
        String[] args;
        args = new String[3];
        args[0] = "findMinPath.txt";
        args[1] = "74";
        args[2] = "75";
        return args;
    }

    private static String[] mockForLongEqualTimeForBothPaths() {
        String[] args;
        args = new String[3];
        args[0] = "findEqualTimePath.txt";
        args[1] = "5";
        args[2] = "15";
        return args;
    }
}
