package com.gis.test;

import com.gis.algorithm.GraphGenerator;
import com.gis.common.exception.EdgeWithGivenIdAlreadyExistsException;
import com.gis.common.exception.ParallelEdgeException;
import com.gis.graph.Wrapper;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GraphTest extends GeneralGraphTest {

    @Test
    public void test1() {
        Wrapper wrapper = GraphGenerator.oneVertexGraph();
        List<Integer> min = Collections.singletonList(0);
        List<Integer> max = Collections.singletonList(0);
        compare(wrapper, min, max);
    }

    @Test
    public void test2() {
        Wrapper wrapper = GraphGenerator.zeroFlowGraph();
        compare(wrapper, null, null);
    }

    @Test
    public void test3() {
        Wrapper wrapper = GraphGenerator.graphWithNoPathBetweenSourceAndEnd();
        compare(wrapper, null, null);
    }

    @Test
    public void test4() {
        Wrapper wrapper = GraphGenerator.smallGraph();
        List<Integer> min = Arrays.asList(0, 1, 3);
        List<Integer> max = Arrays.asList(0, 2, 3);
        compare(wrapper, min, max);
    }

    @Test
    public void test5() {
        Wrapper wrapper = GraphGenerator.simpleGraph();
        List<Integer> min = Arrays.asList(1, 3, 5, 6);
        List<Integer> max = Arrays.asList(1, 2, 4, 6);
        compare(wrapper, min, max);
    }

    @Test
    public void test6() {
        Wrapper wrapper = GraphGenerator.veryBigGraph();
        List<Integer> min = Arrays.asList(0, 3, 6, 11, 13);
        List<Integer> max = Arrays.asList(0, 2, 3, 5, 7, 13);
        compare(wrapper, min, max);
    }

    @Test
    public void test7() {
        Wrapper wrapper = GraphGenerator.bigGraphWithOneZeroPath();
        List<Integer> min = Arrays.asList(1, 2, 4, 6);
        List<Integer> max = Arrays.asList(1, 2, 4, 6);
        compare(wrapper, min, max);
    }

    @Test
    public void test8() {
        Wrapper wrapper = GraphGenerator.bigGraphWithOnePathWithoutPredecessor();
        List<Integer> min = Arrays.asList(1, 2, 4, 6);
        List<Integer> max = Arrays.asList(1, 2, 4, 6);
        compare(wrapper, min, max);
    }

    @Test
    public void test9() {
        Wrapper wrapper = GraphGenerator.smallGraphWithTwoPathsInBothDirection();
        List<Integer> min = Arrays.asList(0, 1, 3);
        List<Integer> max = Arrays.asList(0, 2, 3);
        compare(wrapper, min, max);
    }

    @Test(expected = EdgeWithGivenIdAlreadyExistsException.class)
    public void test10() {
        Wrapper wrapper = GraphGenerator.graphWithRepeatedEdges();
        List<Integer> min = Arrays.asList(0, 1, 3);
        List<Integer> max = Arrays.asList(0, 2, 3);
        compare(wrapper, min, max);
    }

    @Test
    public void test11() {
        Wrapper wrapper = GraphGenerator.smallGraph1();
        List<Integer> min = Arrays.asList(0, 2);
        List<Integer> max = Arrays.asList(0, 1, 2);
        compare(wrapper, min, max);
    }

    @Test
    public void test12() {
        Wrapper wrapper = GraphGenerator.smallGraph2();
        List<Integer> min = Arrays.asList(1, 3, 2, 4);
        List<Integer> max = Arrays.asList(1, 2, 4);
        compare(wrapper, min, max);
    }

    @Test
    public void test13() {
        Wrapper wrapper = GraphGenerator.smallGraph3();
        List<Integer> min = Arrays.asList(1, 2, 3, 4);
        List<Integer> max = Arrays.asList(1, 3, 4);
        compare(wrapper, min, max);
    }

    @Test
    public void test14() {
        Wrapper wrapper = GraphGenerator.mediumGraph1();
        List<Integer> min = Arrays.asList(0, 3, 2, 5);
        List<Integer> max = Arrays.asList(0, 5);
        compare(wrapper, min, max);
    }

    @Test
    public void test15() {
        Wrapper wrapper = GraphGenerator.graphWithCycle();
        List<Integer> min = Arrays.asList(0, 1, 4);
        List<Integer> max = Arrays.asList(0, 1, 4);
        compare(wrapper, min, max);
    }

    @Test
    public void test16() {
        Wrapper wrapper = GraphGenerator.mediumGraph2();
        List<Integer> min = Arrays.asList(0, 1, 2, 3, 4);
        List<Integer> max = Arrays.asList(0, 3, 4);
        compare(wrapper, min, max);
    }

    @Test
    public void test17() {
        Wrapper wrapper = GraphGenerator.bigGraph2();
        List<Integer> min = Arrays.asList(0, 4, 1, 2, 3, 5);
        List<Integer> max = Arrays.asList(0, 5);
        compare(wrapper, min, max);
    }

    @Test
    public void test18() {
        Wrapper wrapper = GraphGenerator.graphWithComingBackPath();
        List<Integer> min = Arrays.asList(1, 5, 6, 2, 3, 4);
        List<Integer> max = Arrays.asList(1, 2, 3, 4);
        compare(wrapper, min, max);
    }

    @Test(expected = ParallelEdgeException.class)
    public void test19() {
        Wrapper wrapper = GraphGenerator.graphWithManyEdgesInSameDirections();
        List<Integer> min = Arrays.asList(0, 1);
        List<Integer> max = Arrays.asList(0, 1);
        compare(wrapper, min, max);
    }
}