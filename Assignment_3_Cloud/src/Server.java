import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/*
 * @author Theo Ellingsen
 * KIT318
 * Server. Accepts new connections, and stores a list of current connections.
 */
public class Server {

	public static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>(); //A list of all clients connected to the server
	
	static class StringPriorityComparator implements Comparator<String> { // TODO: need to check this works once check_status is implemented
		@Override
		public int compare(String o1, String o2) {
			if (Integer.parseInt(o1.split("priority")[1]) < Integer.parseInt(o2.split("priority")[1])) {
				return 0;
			} else {
				return 1;
			}
		}
	}
	
	public static Queue<String> message_queue = new PriorityQueue<>(new StringPriorityComparator());

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket;
		Socket clientSocket;

		serverSocket = new ServerSocket(6150); //Open server socket on localhost port 6150
		while (true) {
			//System.out.println("Waiting for connection");
			clientSocket = serverSocket.accept(); //Accept requested connection from client
			//System.out.println("Connection accepted");
			ClientHandler clientThread = new ClientHandler(clientSocket, clients); //Create a new thread for the client
			clients.add(clientThread); //Add the client to the ArrayList of clients
			
			clientThread.run(); //Run the new thread
			
			// check profanity for head of the queue and then removes the node from the queue
			if (!message_queue.isEmpty()) {
				clientThread.check_queued_message(message_queue.poll().split("priority")[0]);
			}
		}
	}
}
