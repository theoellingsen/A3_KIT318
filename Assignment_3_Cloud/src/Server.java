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
public class Server extends Thread{
	public static ClientHandler clientThread;

	public static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>(); //A list of all clients connected to the server
	
	static class RequestPriorityComparator implements Comparator<Request> { // TODO: need to check this works once check_status is implemented
		@Override
		public int compare(Request o1, Request o2) {
			if (o1.getPriority(o1) < o2.getPriority(o2)) {
				return 0;
			} else {
				return 1;
			}
		}
	}
	
	public static Queue<Request> message_queue = new PriorityQueue<>(new RequestPriorityComparator());

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket;
		Socket clientSocket;

		serverSocket = new ServerSocket(6150); //Open server socket on localhost port 6150
		while (true) {
			//System.out.println("Waiting for connection");
			clientSocket = serverSocket.accept(); //Accept requested connection from client
			//System.out.println("Connection accepted");
			clientThread = new ClientHandler(clientSocket, clients); //Create a new thread for the client
			clients.add(clientThread); //Add the client to the ArrayList of clients
			
			clientThread.run(); //Run the new thread
			
			while (true) {
				if (!message_queue.isEmpty()) {
					System.out.println("message queue in server main");
					message_queue.poll().setStatus("processing");
					clientThread.check_queued_message(message_queue.poll().getStringInput(message_queue.poll()));
				}
			}
		}
	}
	
	
}
