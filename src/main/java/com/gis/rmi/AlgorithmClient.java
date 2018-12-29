package com.gis.rmi;

import com.gis.graph.Graph;
import com.gis.graph.Vertex;
import lombok.AllArgsConstructor;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

@AllArgsConstructor(staticName = "of")
public class AlgorithmClient {

    public static final String WORKER_MIN_PATH = "WorkerMinPath";
    public static final String WORKER_MAX_PATH = "WorkerMaxPath";

    private final String serviceName;

    public List<Vertex> findMinPath(Graph g, Vertex source, Vertex end) {
        try {
            return lookupService().findMinPath(g, source, end);
        } catch (RemoteException e) {
            throw wrapRemoteException(e, "findMinPath");
        }
    }

    public List<Vertex> findMaxPath(Graph g, Vertex source, Vertex end) {
        try {
            return lookupService().findMaxPath(g, source, end);
        } catch (RemoteException e) {
            throw wrapRemoteException(e, "findMaxPath");
        }
    }

    private AlgorithmService lookupService() throws RemoteException{
        return lookupService(serviceName);
    }

    private static AlgorithmService lookupService(String serviceName) throws RemoteException {
        Registry registry = LocateRegistry.getRegistry();
        try {
            return (AlgorithmService) registry.lookup(serviceName);
        } catch (NotBoundException e) {
            System.err.println("Lookup of " + serviceName + " failed");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private RuntimeException wrapRemoteException(RemoteException e, String methodName) {
        System.err.println("Remote error when calling " + methodName + " on service: " + serviceName);
        e.printStackTrace();
        return new RuntimeException(e);
    }
}
