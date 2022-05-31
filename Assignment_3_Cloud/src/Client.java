import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/*
 * @author Theo Ellingsen, Samuel ***, Kate Tanner, Josh ***
 * KIT318
 * Connects a client to the server, and performs local actions based on server communication.
 */
public class Client {
	public static boolean usernameEntered = false; // Has the user entered their details yet?
	public static String username; // Username of the client
	public static String desc; // The description the client enters
	private static Socket clientSocket; // Value of clients socket
	private static BufferedReader in; // Input
	private static PrintWriter out; // Output
	private static Scanner sc = new Scanner(System.in); // Scans input from console
	private static boolean exited = false; // Has the user exited?
	private static boolean userExists = false;

	public static void main(String[] args) {
		String commands = "Use the following commands to get started!\n" + "'Help': lists all the commands\r\n"
				+ "'SubmitRequest': Submit a new request\r\n"
				+ "'CheckStatus': Check the status of your submitted request\r\n" + 
				"'Delete': Delete your request from the server.\n" + 
				"'Exit': disconnect from server\n";

		// New thread for each client.
		Thread client = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					clientSocket = new Socket("localhost", 6150); // Connect to the localhost, port 6150
					out = new PrintWriter(clientSocket.getOutputStream()); // New output
					in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // New input

					// Input from user
					Thread sender = new Thread(new Runnable() {
						String msg;
						int priority;

						@Override
						public void run() {
							while (true) {
								if (usernameEntered == false) {
									// Registers username and description of the user.

									try {
										if (registerUser(commands)) {
											break;
										}
									} catch (IOException | InterruptedException e) {
										e.printStackTrace();
									}
									String line;

									// While still receiving input from scanner
									while ((line = sc.nextLine()) != null) {

										// Split the input into seperate words
										String[] entries = line.split(" ");
										String command = entries[0];
										System.out.println(command);
										// Exit functionality
										if ("exit".equalsIgnoreCase(line)) {
											line = ("exit " + username);
											out.println(line); // Tells server the user is exiting
											out.flush();

											System.out.println("Exiting :)");
											break;
										} 

										else if ("help".equalsIgnoreCase(line)) {
											System.out.println(commands); //Prints help commands
										} 

										else if ("SubmitRequest".equalsIgnoreCase(line)) {

											System.out.println("Enter 'String' if you would like a String processed,\n"
													+ "or 'txt' if you would like a .txt file processed.");
											boolean valid = false;
											while (!valid) {
												msg = sc.nextLine();

												if (msg.equalsIgnoreCase("String")) {
													System.out.println("Enter the string you would like processed:");
													String user_message = sc.nextLine();
													stringProcess(user_message, "string");
													valid = true;
												} else if (msg.equalsIgnoreCase("txt")) {
													System.out.println("Enter the file path:");
													String file_path = sc.nextLine();
													ServerFileReading.uploadfile(file_path);
													stringProcess(file_path, "txt");
													valid = true;
												}
											}
										}

										else if ("CheckStatus".equalsIgnoreCase(line)) {
											boolean found = false;
											for (Request r : Server.message_queue) {
												if(r.getUsername().equalsIgnoreCase(username)) {
													found = true;
													System.out.println("Status: " + r.getStatus());
												}
											}
											if(!found) {
												System.out.println("Request not found!");
											}
										}

										else if ("Delete".equalsIgnoreCase(line)) {
											System.out.println("To Add: Delete functionality");
										}
										else {
											System.out.println("'" + line
													+ "' is not a valid command. Type 'help' for a list of commands.");
										}
									}
									break;
								}
							}
						}

					});
					sender.start();
					//Recieves new messages from the server
					Thread reciever = new Thread(new Runnable() {
						String msg;

						@Override
						public void run() {
							try {
								msg = in.readLine();
								while (msg != null) {

									System.out.println(msg);

									msg = in.readLine();
								}
								System.out.println("Disconnected");
								out.close();
								clientSocket.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
					reciever.start();

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		client.start();
	}

	//The first steps to register a new user
	public static boolean registerUser(String commands) throws IOException, InterruptedException {
		String msg;
		System.out.println("Enter 'login' if you have already registered.\nIf you a are a new user enter 'register'.");
		usernameEntered = true;
		StringBuffer sb = new StringBuffer();

		boolean chosen = false;
		while (chosen  == false) {
			msg = sc.nextLine();
			if (msg.equalsIgnoreCase("register")) {
				chosen = true;
				register();
			} else if (msg.equalsIgnoreCase("login")) {
				chosen = true;
				System.out.println("Logging you in. Enter your username below:");
				msg = sc.nextLine();
				String username = msg;
				StringBuffer sb2 = new StringBuffer();
				sb2.append("login_username ");
				sb2.append(msg);
				msg = sb2.toString();
				out.println(msg);
				out.flush();

				if (in.read() == 1) {
					System.out.println(username + ", your account has been found!\nPlease enter your password below:");
					msg = sc.nextLine();
					StringBuffer sb3 = new StringBuffer();
					sb3.append("login_password ");
					sb3.append(msg);
					msg = sb3.toString();
					out.println(msg);
					out.flush();

					while (in.read() != 1) {
						System.out.println("Incorrect password. Please try again.\n");
						msg = sc.nextLine();
						StringBuffer sb4 = new StringBuffer();
						sb4.append("login_password ");
						sb4.append(msg);
						msg = sb4.toString();
						out.println(msg);
						out.flush();

					} 
					System.out.println(username + ", you have been logged in :)\n");
				} else {
					System.out.println("No user with that username exists. Please register.");
					register();

				}
			} else {
				System.out.println("Enter 'login' or 'register' only.");
			}
		}
		System.out.println(commands);
		return false;
	}

	public static void register () {
		String msg;
		System.out.println("Registering. Please enter a username below:");
		msg = sc.nextLine();
		String username = msg;
		StringBuffer sb2 = new StringBuffer();
		sb2.append("register_username ");
		sb2.append(msg);
		msg = sb2.toString();
		out.println(msg);
		out.flush();

		System.out.println("Thanks " + username + "! Please enter a password below:");
		msg = sc.nextLine();
		String password = msg;
		StringBuffer sb3 = new StringBuffer();
		sb3.append("register_password ");
		sb3.append(msg);
		msg = sb3.toString();
		out.println(msg);
		out.flush();


		System.out
		.println("You are registered! Here are your details\nUsername: " + username + "\nPassword: " + password);
	}

	public static void stringProcess(String user_input, String type) {

		int priority = 0;
		String msg = user_input;
		String input = msg;
		StringBuffer sb = new StringBuffer();
		sb.append("SubmitRequest ");
		sb.append(msg);
		msg = sb.toString();
		System.out.println(msg);
		out.println(msg);
		out.flush();

		out.println(type);
		out.flush();

		System.out.println("Enter a priority for your request (10 for highest, 1 for lowest):");
		boolean valid = false;
		while (!valid) {
			msg = sc.nextLine();
			if (Integer.valueOf(msg) > 0 && Integer.valueOf(msg) < 11) {
				priority = Integer.valueOf(msg);
				StringBuffer sb1 = new StringBuffer();
				out.write(Integer.valueOf(msg));
				out.flush();
				valid = true;
			} else {
				System.out.println("Enter a valid priority :(");
			}
		}

		//Get price:
		StringBuffer sb1 = new StringBuffer();
		sb1.append("pricing ");
		sb1.append(priority + " ");
		sb1.append(input);
		msg = sb1.toString();
		out.println(msg);
		out.flush();

	}

}
