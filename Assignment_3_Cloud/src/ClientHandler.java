import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/*
 * @author Theo Ellingsen
 * KIT318
 * Client Handler class
 * Handles a new thread for each client created by the server. Can handle various input from the client 
 */
public class ClientHandler implements Runnable {
	private Socket clientSocket; // The clients socket
	private BufferedReader in; // Input
	private static PrintWriter out; // Output
	public static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>(); // ArrayList of all clients
																						// connected to server
	public String username; //Clients username
	public String password; //Clients password
	public ClientHandler login_user; //User that has logged in before
	
	//TODO
	public boolean request;
	public boolean status;
	
	public int priority;
	

	//List of profane words for profanity filter, taken from https://github.com/MauriceButler/badwords/blob/master/array.js
	private static String[] profaneWords = new String[] {"4r5e", "5h1t", "5hit", "a55", "anal", "anus", "ar5e", "arrse", "arse", "ass", "ass-fucker", "asses", "assfucker", "assfukka", "asshole", "assholes", "asswhole", "a_s_s", "b!tch", "b00bs", "b17ch", "b1tch", "ballbag", "balls", "ballsack", "bastard", "beastial", "beastiality", "bellend", "bestial", "bestiality", "bi+ch", "biatch", "bitch", "bitcher", "bitchers", "bitches", "bitchin", "bitching", "bloody", "blow job", "blowjob", "blowjobs", "boiolas", "bollock", "bollok", "boner", "boob", "boobs", "booobs", "boooobs", "booooobs", "booooooobs", "breasts", "buceta", "bugger", "bum", "bunny fucker", "butt", "butthole", "buttmuch", "buttplug", "c0ck", "c0cksucker", "carpet muncher", "cawk", "chink", "cipa", "cl1t", "clit", "clitoris", "clits", "cnut", "cock", "cock-sucker", "cockface", "cockhead", "cockmunch", "cockmuncher", "cocks", "cocksuck", "cocksucked", "cocksucker", "cocksucking", "cocksucks", "cocksuka", "cocksukka", "cok", "cokmuncher", "coksucka", "coon", "cox", "crap", "cum", "cummer", "cumming", "cums", "cumshot", "cunilingus", "cunillingus", "cunnilingus", "cunt", "cuntlick", "cuntlicker", "cuntlicking", "cunts", "cyalis", "cyberfuc", "cyberfuck", "cyberfucked", "cyberfucker", "cyberfuckers", "cyberfucking", "d1ck", "damn", "dick", "dickhead", "dildo", "dildos", "dink", "dinks", "dirsa", "dlck", "dog-fucker", "doggin", "dogging", "donkeyribber", "doosh", "duche", "dyke", "ejaculate", "ejaculated", "ejaculates", "ejaculating", "ejaculatings", "ejaculation", "ejakulate", "f u c k", "f u c k e r", "f4nny", "fag", "fagging", "faggitt", "faggot", "faggs", "fagot", "fagots", "fags", "fanny", "fannyflaps", "fannyfucker", "fanyy", "fatass", "fcuk", "fcuker", "fcuking", "feck", "fecker", "felching", "fellate", "fellatio", "fingerfuck", "fingerfucked", "fingerfucker", "fingerfuckers", "fingerfucking", "fingerfucks", "fistfuck", "fistfucked", "fistfucker", "fistfuckers", "fistfucking", "fistfuckings", "fistfucks", "flange", "fook", "fooker", "fuck", "fucka", "fucked", "fucker", "fuckers", "fuckhead", "fuckheads", "fuckin", "fucking", "fuckings", "fuckingshitmotherfucker", "fuckme", "fucks", "fuckwhit", "fuckwit", "fudge packer", "fudgepacker", "fuk", "fuker", "fukker", "fukkin", "fuks", "fukwhit", "fukwit", "fux", "fux0r", "f_u_c_k", "gangbang", "gangbanged", "gangbangs", "gaylord", "gaysex", "goatse", "God", "god-dam", "god-damned", "goddamn", "goddamned", "hardcoresex", "hell", "heshe", "hoar", "hoare", "hoer", "homo", "hore", "horniest", "horny", "hotsex", "jack-off", "jackoff", "jap", "jerk-off", "jism", "jiz", "jizm", "jizz", "kawk", "knob", "knobead", "knobed", "knobend", "knobhead", "knobjocky", "knobjokey", "kock", "kondum", "kondums", "kum", "kummer", "kumming", "kums", "kunilingus", "l3i+ch", "l3itch", "labia", "lust", "lusting", "m0f0", "m0fo", "m45terbate", "ma5terb8", "ma5terbate", "masochist", "master-bate", "masterb8", "masterbat*", "masterbat3", "masterbate", "masterbation", "masterbations", "masturbate", "mo-fo", "mof0", "mofo", "mothafuck", "mothafucka", "mothafuckas", "mothafuckaz", "mothafucked", "mothafucker", "mothafuckers", "mothafuckin", "mothafucking", "mothafuckings", "mothafucks", "mother fucker", "motherfuck", "motherfucked", "motherfucker", "motherfuckers", "motherfuckin", "motherfucking", "motherfuckings", "motherfuckka", "motherfucks", "muff", "mutha", "muthafecker", "muthafuckker", "muther", "mutherfucker", "n1gga", "n1gger", "nazi", "nigg3r", "nigg4h", "nigga", "niggah", "niggas", "niggaz", "nigger", "niggers", "nob", "nob jokey", "nobhead", "nobjocky", "nobjokey", "numbnuts", "nutsack", "orgasim", "orgasims", "orgasm", "orgasms", "p0rn", "pawn", "pecker", "penis", "penisfucker", "phonesex", "phuck", "phuk", "phuked", "phuking", "phukked", "phukking", "phuks", "phuq", "pigfucker", "pimpis", "piss", "pissed", "pisser", "pissers", "pisses", "pissflaps", "pissin", "pissing", "pissoff", "poop", "porn", "porno", "pornography", "pornos", "prick", "pricks", "pron", "pube", "pusse", "pussi", "pussies", "pussy", "pussys", "rectum", "retard", "rimjaw", "rimming", "s hit", "s.o.b.", "sadist", "schlong", "screwing", "scroat", "scrote", "scrotum", "semen", "sex", "sh!+", "sh!t", "sh1t", "shag", "shagger", "shaggin", "shagging", "shemale", "shi+", "shit", "shitdick", "shite", "shited", "shitey", "shitfuck", "shitfull", "shithead", "shiting", "shitings", "shits", "shitted", "shitter", "shitters", "shitting", "shittings", "shitty", "skank", "slut", "sluts", "smegma", "smut", "snatch", "son-of-a-bitch", "spac", "spunk", "s_h_i_t", "t1tt1e5", "t1tties", "teets", "teez", "testical", "testicle", "tit", "titfuck", "tits", "titt", "tittie5", "tittiefucker", "titties", "tittyfuck", "tittywank", "titwank", "tosser", "turd", "tw4t", "twat", "twathead", "twatty", "twunt", "twunter", "v14gra", "v1gra", "vagina", "viagra", "vulva", "w00se", "wang", "wank", "wanker", "wanky", "whoar", "whore", "willies", "willy", "xrated", "xxx"};
	

	//Create a new ClientHandler with all the fields necessary.
	public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients) throws IOException {
		this.clientSocket = clientSocket;
		this.clients = clients;
		this.out = new PrintWriter(clientSocket.getOutputStream());
		this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		for (int i = 0; i < clients.size(); i++) {
			clients.get(i).clients = this.clients;
		}
		this.username = "";
		this.password = "";
	}

	@Override
	public void run() {

		Thread recieve = new Thread(new Runnable() {

			String msg;
			int deadline;

			@Override
			public void run() {
				try {

					//System.out.println("Client recieved in server");
					String[] entries; //String array to be split into individual words.
					String command;
					msg = " ";
					msg = in.readLine();
					while (msg != null) {

						entries = msg.split(" ");
						command = entries[0];

						//Sets username for a new user.
						if (command.equalsIgnoreCase("register_username")) {
							if (entries.length < 2) {
								username = "blank";
							} else {
								username = entries[1];
							}
							

							command = " ";
						}	
						//Set a password for a new user.
						else if (command.equalsIgnoreCase("register_password")) {
							if (entries.length < 2) {
								password = "blank";
							} else {
								password = entries[1];
							}
						}
						
						//Check for username for an existing user.
						else if (command.equalsIgnoreCase("login_username")) {
							int i = 0;
							for (ClientHandler client : clients) {
								i++;
								if (client.username.equalsIgnoreCase(entries[1])) {
									login_user = client;
									login_user.clientSocket = clientSocket;
									out.write(1); //Indicate to server the user exists
									out.flush();
									break;
								}
								else if (i == clients.size() && !client.username.equalsIgnoreCase(entries[1])) {
									out.write(0);
									out.flush();
								}
							} 
						}
						//Check a password for an existing user.
						else if (command.equalsIgnoreCase("login_password")) {
							//Check if password is correct for user.
							System.out.println(entries[1]);
							System.out.println(login_user.password);
							if (login_user.password.equalsIgnoreCase(entries[1])) {
								out.write(1); //Indicate to server the password is correct
								out.flush();
							} else {
								out.write(0); //Indicate to server that password is incorrect
								out.flush();
							}
						} else if (command.equalsIgnoreCase("submitrequest")) {
							
							
							//In final product, this won't be here. Will need to be sent to queue first
							String message = msg.replace("SubmitRequest ", "");
							System.out.println(message);
							out.write(profanity_filter(message));
							out.flush();

							priority = in.read();
							System.out.println(priority);
							//Keep this bit to keep it from breaking pretty much.
							
							String premessage = msg.replace("SubmitRequest ", "");
							Server.message_queue.add(premessage);
						}	else if (command.equalsIgnoreCase("pricing")) {
							String [] arr = msg.split(" ", 3);
							System.out.println(arr[2]);
							pricing(arr[2], Integer.valueOf(entries[1]));
						}
						//User exits.
						else if (command.equalsIgnoreCase("exit")) {
							break;
						} else {
							msg = in.readLine();
							command = entries[0];
							entries = msg.split(" ");
						}
						msg = in.readLine();
					}
					//Only reaches this point if client has disconnected
					//System.out.println("Client Disconnected");
					//Remove client from clients list
					
					//close all sockets
					out.close();
					clientSocket.close();
				} catch (IOException e) {
					out.close();
					//System.out.println("Client disconnected");
					try {
						clientSocket.close();
					} catch (IOException e1) {
					}
				}
			}
		});
		recieve.start();
	}
	
	public static void pricing(String line, int priority) {
		//System.out.println(line);
		String[] arr = line.split(" ");
		Double length = Double.valueOf(arr.length);
		//System.out.println(length);

		Double D_priority = Double.valueOf(priority);
		//System.out.println(D_priority);

		Double price = (Double.valueOf(0.01)*length*D_priority);

		out.println("The price of this service is: $" + String.format("%.2f", price));
		out.flush();
	}

	public void check_queued_message(String message) {
		out.write(profanity_filter(message));
		out.flush();
	}

	//A filter for all profane words in the private array for this class.
	
	public static String profanity_filter(String line){
		Double num_profane = 0.0;

		//Split string by any punctuation
		String[] sent = line.split("[:;()-/_'!,.?\\n ] ?");

		//Iterate through strings
		for (int i = 0; i < sent.length; i++) {

			//Iterate through list of bad words
			for (int k = 0; k < profaneWords.length; k++) {

				//If word is bad
				if (sent[i].equalsIgnoreCase(profaneWords[k])) {

					//get length
					int stringSize = sent[i].length();

					//Add 1 to number of profane words
					num_profane++;

					//Make new string containing same number of *'s as letters
					char[] replacement = new char[stringSize];
					for (int j = 0; j < stringSize; j++) {
						replacement[j] = '*';
					}
					//Replace
					sent[i] = String.valueOf(replacement);
				}
			}
		}

		//Append the string together
		StringBuffer sb = new StringBuffer();
		sb.append("The result of your input String is: ");
		for (int i = 0; i < sent.length; i++) {
			sb.append(sent[i]);
			if (i != sent.length - 1) {
				sb.append(" ");
			}
			
		}
		sb.append("\n");
		//Calculate profane %
		double percent_profane = (num_profane/Double.valueOf(sent.length))*100;
		try {
			out.println("This entry contains " + String.format("%.2f", percent_profane) + "% profanity.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = sb.toString();
		//System.out.println(str);
		return str;
	}
}
