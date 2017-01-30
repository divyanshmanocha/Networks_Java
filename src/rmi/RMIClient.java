/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;


		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);
		//int length  = Integer.parseInt(args[2]);

		// TO-DO: Initialise Security Manager

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		
		/*try {
			Naming.bind(urlServer, new rmi.RMIServer());
		} catch (RemoteException e) {
			e.printStackTrace();
		}*/

		//Either using above or below --> not sure?
		
		try {
				// TO-DO: Bind to RMIServer
				iRMIServer = (RMIServerI) Naming.lookup(urlServer);
				// TO-DO: Attempt to send messages the specified number of times
				for (int n = 0; n < numMessages; n++) {
					MessageInfo packet = new MessageInfo(numMessages, n);
					iRMIServer.receiveMessage(packet);
				}

		
		} catch (MalformedURLException e) {
				e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (NotBoundException e) {
				e.printStackTrace();
		}

	}
}
