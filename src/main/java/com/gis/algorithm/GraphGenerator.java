package com.gis.algorithm;

import com.gis.graph.*;

import java.util.Random;

public class GraphGenerator {
    public static Wrapper zeroFlowGraph(){
        Graph g = new DirectedSparseGraph();

        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        g.addEdge(new Edge(1, 0), v0, v1);
        g.addEdge(new Edge(2, 0), v0, v2);
        g.addEdge(new Edge(3, 0), v1, v3);
        g.addEdge(new Edge(4, 0), v2, v3);

        return new Wrapper(g, v0, v3);
    }

    public static Wrapper oneVertexGraph(){
        Graph g = new DirectedSparseGraph();
        Vertex v0 = new Vertex(0);
        g.addEdge(new Edge(1, 350), v0, v0);
        return new Wrapper(g, v0, v0);
    }

    public static Wrapper smallGraph(){
        Graph g = new DirectedSparseGraph();

        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        g.addEdge(new Edge(1, 350), v0, v1);
        g.addEdge(new Edge(2, 350), v0, v2);
        g.addEdge(new Edge(3, 200), v1, v3);
        g.addEdge(new Edge(4, 250), v2, v3);

        return new Wrapper(g, v0, v3);
    }

    public static Wrapper simpleGraph(){
        Graph g = new DirectedSparseGraph();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);
        Vertex v5 = new Vertex(5);
        Vertex v6 = new Vertex(6);

        g.addEdge(new Edge(1, 2), v1, v2);
        g.addEdge(new Edge(2, 1), v1, v3);
        g.addEdge(new Edge(3, 3), v2, v3);
        g.addEdge(new Edge(4, 1), v3, v5);
        g.addEdge(new Edge(5, 3), v2, v4);
        g.addEdge(new Edge(6, 2), v4, v6);
        g.addEdge(new Edge(7, 2), v5, v4);
        g.addEdge(new Edge(8, 5), v5, v6);

        return new Wrapper(g, v1, v6);
    }

    public static Wrapper veryBigGraph(){
        Graph g = new DirectedSparseGraph();

        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);
        Vertex v5 = new Vertex(5);
        Vertex v6 = new Vertex(6);
        Vertex v7 = new Vertex(7);
        Vertex v8 = new Vertex(8);
        Vertex v9 = new Vertex(9);
        Vertex v10 = new Vertex(10);
        Vertex v11 = new Vertex(11);
        Vertex v12 = new Vertex(12);
        Vertex v13 = new Vertex(13);

        g.addEdge(new Edge(1, 7), v0, v1);
        g.addEdge(new Edge(2, 3), v0, v3);
        g.addEdge(new Edge(3, 11), v0, v2);
        g.addEdge(new Edge(4, 3), v1, v4);
        g.addEdge(new Edge(5, 4), v2, v3);
        g.addEdge(new Edge(6, 6), v3, v5);
        g.addEdge(new Edge(7, 6), v3, v6);
        g.addEdge(new Edge(8, 2), v4, v9);
        g.addEdge(new Edge(9, 9), v4, v10);
        g.addEdge(new Edge(10, 7), v5, v7);
        g.addEdge(new Edge(11, 1), v6, v11);
        g.addEdge(new Edge(12, 4), v6, v12);
        g.addEdge(new Edge(13, 12), v7, v8);
        g.addEdge(new Edge(14, 5), v7, v13);
        g.addEdge(new Edge(15, 4), v8, v11);
        g.addEdge(new Edge(16, 7), v9, v10);
        g.addEdge(new Edge(17, 11), v11, v13);
        g.addEdge(new Edge(18, 5), v12, v11);

        return new Wrapper(g, v0, v13);
    }

    public static Wrapper bigGraphWithOneZeroPath(){
        Graph g = new DirectedSparseGraph();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);
        Vertex v5 = new Vertex(5);
        Vertex v6 = new Vertex(6);

        g.addEdge(new Edge(1, 2), v1, v2);
        g.addEdge(new Edge(2, 1), v1, v3);
        g.addEdge(new Edge(3, 3), v2, v3);
        g.addEdge(new Edge(4, 0), v3, v5);
        g.addEdge(new Edge(5, 3), v2, v4);
        g.addEdge(new Edge(6, 2), v4, v6);
        g.addEdge(new Edge(7, 2), v5, v4);
        g.addEdge(new Edge(8, 5), v5, v6);

        return new Wrapper(g, v1, v6);
    }

    public static Wrapper bigGraphWithOnePathWithoutPredecessor() {
        Graph g = new DirectedSparseGraph();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);
        Vertex v5 = new Vertex(5);
        Vertex v6 = new Vertex(6);

        g.addEdge(new Edge(1, 2), v1, v2);
        g.addEdge(new Edge(2, 1), v1, v3);
        g.addEdge(new Edge(3, 3), v2, v3);
        g.addEdge(new Edge(5, 3), v2, v4);
        g.addEdge(new Edge(6, 2), v4, v6);
        g.addEdge(new Edge(7, 2), v5, v4);
        g.addEdge(new Edge(8, 5), v5, v6);

        return new Wrapper(g, v1, v6);
    }

    public static Wrapper graphWithNoPathBetweenSourceAndEnd() {
        Graph g = new DirectedSparseGraph();

        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);
        Vertex v5 = new Vertex(5);
        Vertex v6 = new Vertex(6);
        Vertex v7 = new Vertex(7);
        Vertex v8 = new Vertex(8);
        Vertex v9 = new Vertex(9);
        Vertex v10 = new Vertex(10);
        Vertex v11 = new Vertex(11);
        Vertex v12 = new Vertex(12);
        Vertex v13 = new Vertex(13);

        g.addEdge(new Edge(1, 7), v0, v1);
        g.addEdge(new Edge(2, 3), v0, v3);
        g.addEdge(new Edge(3, 11), v0, v2);
        g.addEdge(new Edge(4, 3), v1, v4);
        g.addEdge(new Edge(5, 4), v2, v3);
        g.addEdge(new Edge(6, 6), v3, v5);
        g.addEdge(new Edge(7, 6), v3, v6);
        g.addEdge(new Edge(13, 12), v7, v8);
        g.addEdge(new Edge(14, 5), v7, v13);
        g.addEdge(new Edge(15, 4), v8, v11);
        g.addEdge(new Edge(16, 7), v9, v10);
        g.addEdge(new Edge(17, 11), v11, v13);
        g.addEdge(new Edge(18, 5), v12, v11);

        return new Wrapper(g, v0, v13);
    }

    public static Wrapper smallGraphWithTwoPathsInBothDirection(){
        Graph g = new DirectedSparseGraph();

        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        g.addEdge(new Edge(1, 350), v0, v1);
        g.addEdge(new Edge(2, 350), v0, v2);
        g.addEdge(new Edge(3, 350), v1, v2);
        g.addEdge(new Edge(4, 350), v2, v1);
        g.addEdge(new Edge(5, 200), v1, v3);
        g.addEdge(new Edge(6, 250), v2, v3);

        return new Wrapper(g, v0, v3);
    }

    public static Wrapper graphWithRepeatedEdges(){
        Graph g = new DirectedSparseGraph();

        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        g.addEdge(new Edge(1, 350), v0, v1);
        g.addEdge(new Edge(2, 350), v0, v2);
        g.addEdge(new Edge(3, 350), v1, v2);
        g.addEdge(new Edge(4, 200), v1, v3);
        g.addEdge(new Edge(4, 250), v2, v3);

        return new Wrapper(g, v0, v3);
    }

    public static Wrapper smallGraph1(){
        Graph g = new DirectedSparseGraph();

        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        g.addEdge(new Edge(1, 4), v2, v1);
        g.addEdge(new Edge(2, 1), v0, v2);
        g.addEdge(new Edge(3, 8), v1, v2);
        g.addEdge(new Edge(4, 7), v1, v0);
        g.addEdge(new Edge(5, 4), v2, v0);
        g.addEdge(new Edge(6, 4), v0, v1);

        return new Wrapper(g, v0, v2);
    }

    public static Wrapper smallGraph2(){
        Graph g = new DirectedSparseGraph();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);

        g.addEdge(new Edge(1, 10), v1, v2);
        g.addEdge(new Edge(2, 10), v1, v3);
        g.addEdge(new Edge(3, 1), v3, v2);
        g.addEdge(new Edge(4, 2), v2, v3);
        g.addEdge(new Edge(5, 10), v2, v4);
        g.addEdge(new Edge(6, 10), v3, v4);

        return new Wrapper(g, v1, v4);
    }

    public static Wrapper smallGraph3(){
        Graph g = new DirectedSparseGraph();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);

        g.addEdge(new Edge(1, 10), v1, v2);
        g.addEdge(new Edge(2, 10), v1, v3);
        g.addEdge(new Edge(3, 2), v3, v2);
        g.addEdge(new Edge(4, 1), v2, v3);
        g.addEdge(new Edge(5, 10), v2, v4);
        g.addEdge(new Edge(6, 10), v3, v4);

        return new Wrapper(g, v1, v4);
    }

    public static Wrapper mediumGraph1(){
        Graph g = new DirectedSparseGraph();

        Vertex v0 = new Vertex(0);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v5 = new Vertex(5);

        g.addEdge(new Edge(1, 25), v5, v2);
        g.addEdge(new Edge(2, 6), v3, v0);
        g.addEdge(new Edge(5, 28), v0, v3);
        g.addEdge(new Edge(10, 8), v2, v5);
        g.addEdge(new Edge(12, 17), v2, v3);
        g.addEdge(new Edge(14, 8), v2, v0);
        g.addEdge(new Edge(21, 6), v3, v2);
        g.addEdge(new Edge(23, 23), v3, v5);
        g.addEdge(new Edge(24, 5), v5, v0);
        g.addEdge(new Edge(25, 29), v0, v2);
        g.addEdge(new Edge(26, 24), v0, v5);
        g.addEdge(new Edge(27, 11), v5, v3);

        return new Wrapper(g, v0, v5);

//        Graph g = new DirectedSparseGraph();
//
//        Vertex v0 = new Vertex(0);
//        Vertex v2 = new Vertex(2);
//        Vertex v3 = new Vertex(3);
//        Vertex v5 = new Vertex(5);
//
//        g.addEdge(new Edge(1, 25), v5, v2);
//        g.addEdge(new Edge(2, 6), v3, v0);
//        g.addEdge(new Edge(5, 28), v0, v3);
//        g.addEdge(new Edge(10, 8), v2, v5);
//        g.addEdge(new Edge(12, 17), v2, v3);
//        g.addEdge(new Edge(14, 8), v2, v0);
//        g.addEdge(new Edge(21, 6), v3, v2);
//        g.addEdge(new Edge(23, 23), v3, v5);
//        g.addEdge(new Edge(24, 5), v5, v0);
//        g.addEdge(new Edge(25, 29), v0, v2);
//        g.addEdge(new Edge(26, 24), v0, v5);
//        g.addEdge(new Edge(27, 11), v5, v3);
//
//        return new Wrapper(g, v0, v5);
    }

    public static Wrapper graphWithCycle(){
        Graph g = new DirectedSparseGraph();

        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);

        g.addEdge(new Edge(12, 17), v2, v3);
        g.addEdge(new Edge(16, 15), v0, v1);
        g.addEdge(new Edge(18, 20), v1, v4);
        g.addEdge(new Edge(28, 6), v1, v2);
        g.addEdge(new Edge(29, 20), v3, v1);

        return new Wrapper(g, v0, v4);
    }


    public static Wrapper mediumGraph2(){
        Graph g = new DirectedSparseGraph();

        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);

        g.addEdge(new Edge(5, 28), v0, v3);
        g.addEdge(new Edge(11, 28), v3, v4);
        g.addEdge(new Edge(12, 17), v2, v3);
        g.addEdge(new Edge(16, 15), v0, v1);
        g.addEdge(new Edge(25, 29), v0, v2);
        g.addEdge(new Edge(28, 6), v1, v2);
        g.addEdge(new Edge(29, 20), v3, v1);

        return new Wrapper(g, v0, v4);
    }

    public static Wrapper bigGraph2(){
        Graph g = new DirectedSparseGraph();

        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);
        Vertex v5 = new Vertex(5);

        g.addEdge(new Edge(1, 25), v5, v2);
        g.addEdge(new Edge(2, 6), v3, v0);
        g.addEdge(new Edge(3, 27), v0, v4);
        g.addEdge(new Edge(4, 25), v5, v4);
        g.addEdge(new Edge(5, 28), v0, v3);
        g.addEdge(new Edge(6, 8), v2, v4);
        g.addEdge(new Edge(7, 26), v1, v3);
        g.addEdge(new Edge(8, 15), v4, v5);
        g.addEdge(new Edge(9, 12), v5, v1);
        g.addEdge(new Edge(10, 8), v2, v5);
        g.addEdge(new Edge(11, 28), v3, v4);
        g.addEdge(new Edge(12, 17), v2, v3);
        g.addEdge(new Edge(13, 11), v1, v0);
        g.addEdge(new Edge(14, 8), v2, v0);
        g.addEdge(new Edge(15, 16), v1, v5);
        g.addEdge(new Edge(16, 15), v0, v1);
        g.addEdge(new Edge(17, 7), v4, v2);
        g.addEdge(new Edge(18, 20), v1, v4);
        g.addEdge(new Edge(19, 24), v4, v1);
        g.addEdge(new Edge(20, 1), v4, v0);
        g.addEdge(new Edge(21, 6), v3, v2);
        g.addEdge(new Edge(22, 28), v2, v1);
        g.addEdge(new Edge(23, 23), v3, v5);
        g.addEdge(new Edge(24, 5), v5, v0);
        g.addEdge(new Edge(25, 29), v0, v2);
        g.addEdge(new Edge(26, 24), v0, v5);
        g.addEdge(new Edge(27, 11), v5, v3);
        g.addEdge(new Edge(28, 6), v1, v2);
        g.addEdge(new Edge(29, 20), v3, v1);
        g.addEdge(new Edge(30, 17), v4, v3);

        return new Wrapper(g, v0, v5);
    }

    public static Wrapper graphWithComingBackPath(){
        Graph g = new DirectedSparseGraph();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);
        Vertex v5 = new Vertex(5);
        Vertex v6 = new Vertex(6);

        g.addEdge(new Edge(1, 100), v1, v5);
        g.addEdge(new Edge(2, 100), v1, v2);
        g.addEdge(new Edge(3, 100), v2, v3);
        g.addEdge(new Edge(4, 100), v3, v4);
        g.addEdge(new Edge(5, 100), v5, v6);
        g.addEdge(new Edge(6, 1), v6, v2);

        return new Wrapper(g, v1, v4);
    }

    public static Wrapper graphWithManyEdgesInSameDirections() {
        Graph g = new DirectedSparseGraph();

        Vertex v1 = new Vertex(0);
        Vertex v2 = new Vertex(1);

        g.addEdge(new Edge(1, 3), v1, v2);
        g.addEdge(new Edge(2, 5), v1, v2);
        g.addEdge(new Edge(3, 2), v1, v2);
        g.addEdge(new Edge(4, 1), v1, v2);
        g.addEdge(new Edge(5, 2), v1, v2);
        g.addEdge(new Edge(6, 4), v1, v2);

        return new Wrapper(g, v1, v2);
    }

    ///////////////////

    public static Wrapper generateFullGraph(int size){
        Graph g = new DirectedSparseGraph();

        Vertex[] tabOfVertices = new Vertex[size];

        for (int i = 0; i < tabOfVertices.length; i++) {
            tabOfVertices[i] = new Vertex(i);
        }

        int edgeCnt = 0;
        Random random = new Random();

        for (int i = 0; i < tabOfVertices.length; i++) {
            for (int j = 0; j < tabOfVertices.length; j++) {
                if(i != j){
                    Vertex vertex = tabOfVertices[i];
                    Vertex vertex1 = tabOfVertices[j];
                    System.out.println("xx");
                    g.addEdge(new Edge(edgeCnt++, random.nextInt(100) + 1), vertex, vertex1);
                }
            }
        }
        return new Wrapper(g, tabOfVertices[0], tabOfVertices[tabOfVertices.length-1]);
    }
}
