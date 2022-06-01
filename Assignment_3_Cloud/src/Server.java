import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import org.jboss.resteasy.spi.UnhandledException;

/*
 * @author Theo Ellingsen, Samuel Hoskin-Newell, Kate Tanner, Joshua Perrin
 * KIT318
 * Server. Accepts new connections and stores a list of current connections.
 */
public class Server{
	public static ClientHandler clientThread;

	public static Queue<Request> message_queue = new PriorityQueue<>(new RequestPriorityComparator());

	public static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>(); //A list of all clients connected to the server

	static class RequestPriorityComparator implements Comparator<Request> {
		@Override
		public int compare(Request o1, Request o2) {
			if (o1.getPriority() < o2.getPriority()) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	static class RequestQueue implements Runnable {

		@Override
		public void run() {
			try {
				int i = 0;
				while (true) {
					//DO NOT DELETE iterating I!
					i ++;
					if (i % 1000 == 1) {
						System.out.print("");
					}
					if (!message_queue.isEmpty()) {
						message_queue.peek().setStatus("processing");
						
						try {
							Thread.sleep(30000); // wait 30 secs to process string
						} catch( InterruptedException e) {
							e.printStackTrace();
						}
						
						clientThread.check_queued_message(message_queue.poll());
					}
				}
			} catch (UnhandledException e) { // Node failure handling
				System.out.println("Error. Restarting worker node.");
				run();
			}

		}	
	}


	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket;
		Socket clientSocket;
		
		// Note: Uncomment following code to create and a delete a NectarCloud instance.
		
		/*CloudTutorial openstack =new CloudTutorial();//Build the  -openstack client and authenticate
        openstack.getflavors();
				String[] serverDets = openstack.createServer();//Creating a new VM
		String serverIP=serverDets[0];
		String serverid=serverDets[1];
		String test2=openstack.getIP(serverid);
		System.out.println("IP "+serverIP+" "+test2);
		System.out.println(" Successfully Created Virtual Machine(VM) with server id"+ serverid +" and temp folder inside VM Please log in to nectar cloud to verify ");
		
		ServerFileReading.cloud_ip = test2; // sets host ip for downloading/uploading files
		
		ServerFileReading.uploadfile("~/Desktop/test_text.txt");
		
		try {
			Thread.sleep(300000);//wait for 5 minutes to create VM
		} catch( InterruptedException e) {
			e.printStackTrace();
		}
		openstack.deleteServer(serverid);//Delete the created server
		System.out.println(" Successfully deleted Virtual Machine(VM) of server id"+ serverid);*/

		serverSocket = new ServerSocket(6150); //Open server socket on localhost port 6150
		while (true) {
			//System.out.println("Waiting for connection");
			clientSocket = serverSocket.accept(); //Accept requested connection from client
			//System.out.println("Connection accepted");
			clientThread = new ClientHandler(clientSocket, clients); //Create a new thread for the client
			clients.add(clientThread); //Add the client to the ArrayList of clients

			clientThread.run(); //Run the new thread

			Server.RequestQueue requestQueue = new Server.RequestQueue();
			requestQueue.run();
		}
	}
}