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
	private int received = 0;

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
				pac = new DatagramPacket(pacData, pacSize);
				recvSoc.receive(pac);
				String received = new String(pac.getData(), 0, pac.getLength());

				// Process message
				processMessage(received, pac);

			}
		}
		catch (IOException e) {
      e.printStackTrace();
		}
	}

	public void processMessage(String data, DatagramPacket pac) {

		MessageInfo msg = null;
		// TO-DO: Use the data to construct a new MessageInfo object
		try {
			msg = new MessageInfo(data);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// TO-DO: On receipt of first message, initialise the receive buffer
		totalMessages = msg.totalMessages();
		if (receivedMessages == null && totalMessages != -1){
			receivedMessages = new int[totalMessages];
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum()] = 1;


		// Send reply
		/*
		byte[] buf = new byte[256];
		String dstring = (msg.messageNum()+1).toString();
		InetAddress address = pac.getAddress();
		int port = pac.getPort();
		pac = new DatagramPacket(buf, buf.length, address, port);
		recvSoc.send(pac);
		*/
		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if (msg.messageNum() == totalMessages-1) {
			// Check for any missing messages

			for (int i = 0; i < totalMessages; i++){
				if (receivedMessages[i]==1) {
					received++;
					System.out.println("Found packet: " + (i+1));
				}
				else {
					System.out.println("Did not find packet: " + (i+1));
				}
			}
			System.out.println("Summary");
			System.out.println("#############################");
			System.out.println("Found " + received + " packets");
			System.out.println("Out of " + totalMessages + " packets sent");
			double error_rate = (totalMessages - received)/totalMessages;
			System.out.println("Giving an error rate of " + error_rate);
			received = 0;
			receivedMessages = null;
			}
		}

	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try {
			recvSoc = new DatagramSocket(rp);
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
		UDPServer serv = new UDPServer(recvPort);
		serv.run();
	}
}
