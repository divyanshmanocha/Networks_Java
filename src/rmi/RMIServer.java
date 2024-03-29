/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.rmi.AlreadyBoundException;
import java.io.*;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;
	private int received = 0;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {
		// TO-DO: On receipt of first message, initialise the receive buffer
		if (receivedMessages== null) {
			receivedMessages = new int [msg.totalMessages];
			totalMessages = msg.totalMessages;
		}

		// TO-DO: Log receipt of the message
		if (msg.messageNum > msg.totalMessages) {
			return;
		}
		receivedMessages[msg.messageNum] = 1;
		received++;
	// TO-DO: If this is the last expected message, then identify
	//        any missing messages

		if (msg.messageNum == totalMessages - 1) {
			//The number of trials is set to 5 by default
			System.out.println("#############################");
			System.out.println("Found " + received + " packets");
			System.out.println("Out of " + totalMessages + " packets sent");
			double error_rate = ((double)(totalMessages - received))/((double)totalMessages);
			System.out.println("Success rate: " + (1.0-error_rate));
			System.out.println("Error rate: " + error_rate);


			System.out.println("Writing to file");
			System.out.println("#############################");

			FileOutputStream out = null;

			try{
			    PrintWriter writer = new PrintWriter(new FileOutputStream(new File("error_rate_rmi.txt"), true /* append = true */) );
			    writer.append(totalMessages + "\t" + error_rate + "\n");
			    writer.close();
			} catch (IOException e) {
			   System.out.println("Error: Failed to output to file");
			}

			received = 0;
			receivedMessages = null;

		}

	}


	public static void main(String[] args) {

		RMIServer rmis = null;

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		String urlServer = new String("rmi://" + "localhost" + "/RMIServer");
		// TO-DO: Instantiate the server class
		// TO-DO: Bind to RMI registry
		try {
			RMIServer myserver = new RMIServer();
			rebindServer(urlServer, myserver);

		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	protected static void rebindServer(String serverURL, RMIServer server) {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		try {
			LocateRegistry.createRegistry(1099);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		try {
			//Naming.bind(serverURL, new RMIServer());
			Naming.bind(serverURL, server);

		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
	}
}
