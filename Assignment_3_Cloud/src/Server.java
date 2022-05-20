import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
 * @author Theo Ellingsen
 * KIT318
 * Server. Accepts new connections, and stores a list of current connections.
 */
public class Server {

	public static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>(); //A list of all clients connected to the server

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
		}
	}
}