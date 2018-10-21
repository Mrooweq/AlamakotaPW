package com.gis;


import com.gis.algorithm.GraphGenerator;
import com.gis.algorithm.Algorithm;
import com.gis.common.fileoperations.FromFile;
import com.gis.common.fileoperations.ToFile;
import com.gis.graph.PathWrapper;
import com.gis.common.exception.NoPathException;
import com.gis.graph.Graph;
import com.gis.graph.Vertex;
import com.gis.graph.Wrapper;

@SuppressWarnings("Duplicates")
public class WorstCaseComplexityTest {

    public static void main(String[] args) {

        Wrapper xD = GraphGenerator.generateFullGraph(120);
        ToFile.generateFileBasedOnGraph(xD);



        for(int size = 2; size < 1000; size++){
            Wrapper wrapper = GraphGenerator.generateFullGraph(size);

            Graph graph = wrapper.getGraph();
            Vertex from = wrapper.getFrom();
            Vertex to = wrapper.getTo();

            try {
                long start = System.currentTimeMillis();
                PathWrapper paths = Algorithm.getPaths(graph, from, to);
                System.out.println(size + ") " + (System.currentTimeMillis() - start));

                Algorithm.parseListOfVerticesToIdList(paths.getMinPath());
                Algorithm.parseListOfVerticesToIdList(paths.getMaxPath());
            } catch (NoPathException e) { }
        }
    }
}
