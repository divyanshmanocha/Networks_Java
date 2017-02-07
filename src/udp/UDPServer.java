/*
 * Created on 01-Mar-2016
 */
package udp;
import java.util.Timer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.InetAddress;
import java.util.*;
import java.io.*;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int msgNum = -1;
	private ArrayList<Integer> receivedMessages = new ArrayList<Integer>();
	private boolean close;
	private int received = 0;
	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;
		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever


		try {
			recvSoc.setSoTimeout(3000);
			// Until we get to the final message
			while (true) {
				// Initialise pacData and pacSize
				pacData = new byte[256];
				pacSize = pacData.length;
				// Receive request
				pac = new DatagramPacket(pacData, pacSize);
				try {
					recvSoc.receive(pac);
					String recStr= new String(pac.getData(), 0, pac.getLength());
					// Process message
	        processMessage(recStr, pac);
	        // print stats
	        if (msgNum == totalMessages-1 || received==totalMessages){
						printStats();
					}
				}
				catch (SocketTimeoutException e) {
					if (totalMessages!=-1)
						printStats();
				}
			}
		}
		catch (IOException e) {
      e.printStackTrace();
		}
	}

	public void printStats(){
		System.out.println("#############################");
		/*if (received!=totalMessages){
		System.out.println("Lost some packets: ");
			for (int i = 0; i < totalMessages; i++){
				if (!receivedMessages.contains(i)){
					System.out.println("Lost packet: " + i);
				}
		 }
	 }*/
		receivedMessages.clear();

		System.out.println("Found " + received + " packets");
		System.out.println("Out of " + totalMessages + " packets sent");
		double error_rate = ((double)(totalMessages - received))/((double)totalMessages);
		System.out.println("Success rate: " + (1.0-error_rate));
		System.out.println("Error rate: " + error_rate);


		System.out.println("Writing to file");
		System.out.println("#############################");

		FileOutputStream out = null;

		try{
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File("error_rate.txt"), true /* append = true */) );
			writer.append(totalMessages + "\t" + received + "\t" + error_rate  + "\n");
			writer.close();
		} catch (IOException e) {
			System.out.println("Error: Failed to output to file");
		}
		received = 0;
		totalMessages = -1;
	}

	public void processMessage(String data, DatagramPacket pac) {
		MessageInfo msg = null;
		// Use the data to construct a new MessageInfo object
		try {
			msg = new MessageInfo(data);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// On receipt of first message, initialise the receive buffer

		msgNum = msg.messageNum();
		// System.out.println("Found packet:" + msgNum);
		totalMessages = msg.totalMessages();
		// Log receipt of the message
		if (totalMessages > receivedMessages.size()){
			receivedMessages.ensureCapacity(totalMessages);
		}

		if (msgNum < totalMessages && !receivedMessages.contains(msgNum) && received < totalMessages && msgNum!=totalMessages-1) {
			receivedMessages.add(msgNum);
			received++;
		}

		else if (msgNum==totalMessages-1) {
			// add the final message
			receivedMessages.add(msgNum);
			received++;
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
