/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages = null;
	private boolean close;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		try {
			// Until we get to the final message
			while (true) {
				// Initialise pacData and pacSize
				pacData = new byte[256];
				pacSize = pacData.length;
			
				// Receive request
				pac = new Datagrampacket(pacData, pacSize);
				recvsocket.receive(pac);
				String received = new String(pac.getData(), 0, pac.getLength());
				// Process message
				processMessage(received);
			}
		}
	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		msg = new MessageInfo(data);
		// TO-DO: On receipt of first message, initialise the receive buffer
		totalMessages = msg.totalMessages();
		if (receivedMessages = null){
			receivedMessages = new int[totalMessages];
		}
		// TO-DO: Log receipt of the message (use arrayList instead?)
		receivedMessages[msg.messageNum()]
		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if (messageNum = totalMessages-1){
			// Check for any missing messages
			
			for (int i = 0; i < totalMessage; i++){
				if (!receivedMessages[i]){
					// missing message
				}
			}
		}
	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try {
			recvSocket = new DatagramSocket(rp);
		}
		catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port " + rp);
			System.out.println(e.getMessage());
		}
		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		Server = new UDPServer(recvPort);
		run();
	}

}
