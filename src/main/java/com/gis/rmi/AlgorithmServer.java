package com.gis.rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class AlgorithmServer {

    public static void main(String... args) {
        try {
            String workerName = args[0];
            System.out.println("Starting AlgorithmServer: " + workerName);
            AlgorithmServiceImpl server = new AlgorithmServiceImpl(workerName);
            // Exporting the object of implementation class
            // (here we are exporting the remote object to the stub)
            AlgorithmService stub = (AlgorithmService) UnicastRemoteObject.exportObject(server, 0);
            // Binding the remote object (stub) in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind(workerName, stub);
            System.out.println("Algorithm Server ready");
        } catch (AlreadyBoundException | RemoteException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}