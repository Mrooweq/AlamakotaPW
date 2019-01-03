package com.gis.rmi;

import com.gis.Main;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static com.gis.rmi.AlgorithmServer.ALGORITHM_SERVICE;

public class RMIMain {

    private static final String LOCALHOST = "localhost";

    public static void main(String[] args) {
        String host1 = LOCALHOST;
        String host2 = LOCALHOST;
        if (args.length > 0) {
            host1 = args[0];
            host2 = args[1];
        }
        String name1 = ALGORITHM_SERVICE;
        String name2 = ALGORITHM_SERVICE;
        if (args.length > 2) {
            name1 = args[2];
            name2 = args[3];
        }
        getAlgorithmServiceAndBindInLocalRegistry(host1, name1, AlgorithmClient.WORKER_MIN_PATH);
        getAlgorithmServiceAndBindInLocalRegistry(host2, name2, AlgorithmClient.WORKER_MAX_PATH);
        Main.main(args);
    }

    private static void getAlgorithmServiceAndBindInLocalRegistry(String host, String remoteName, String localName) {
        Remote service = getAlgorithmService(host, remoteName);
        bindInLocalRegistry(localName, service);
    }

    private static Remote getAlgorithmService(String host, String remoteName) {
        Registry remoteRegistry;
        try {
            remoteRegistry = LocateRegistry.getRegistry(host);
        } catch (RemoteException e) {
            System.err.println("Can't locate RMI registry on host: " + host);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try {
            return remoteRegistry.lookup(remoteName);
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Lookup for " + remoteName + " on " + host + " failed!");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void bindInLocalRegistry(String name, Remote service) {
        try {
            Registry localRegistry = LocateRegistry.getRegistry();
            localRegistry.bind(name, service);
        } catch (RemoteException | AlreadyBoundException e) {
            System.err.println("Can't bind " + AlgorithmClient.WORKER_MIN_PATH + " in local registry.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
