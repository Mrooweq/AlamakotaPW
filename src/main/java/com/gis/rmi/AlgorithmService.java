package com.gis.rmi;

import com.gis.graph.Graph;
import com.gis.graph.Vertex;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AlgorithmService extends Remote {

    void setGraph(Graph graph) throws RemoteException;

    List<Vertex> findMinPath(Graph g, Vertex source, Vertex end) throws RemoteException;

    List<Vertex> findMaxPath(Graph g, Vertex source, Vertex end) throws RemoteException;

    boolean checkIfExistsPath(Graph graph, Vertex start, Vertex end) throws RemoteException;

    List<Vertex> findShortestPath(Graph graph, Vertex start, Vertex stop) throws RemoteException;
}
