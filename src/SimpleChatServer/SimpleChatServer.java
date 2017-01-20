package SimpleChatServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class SimpleChatServer {
	ArrayList clientOutputStreams;

	public class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket sock;

		// constructor is listening to the socket
		public ClientHandler(Socket clientSocket) {
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void run() {
			String message;
			try {
				// set the variable message to readline and compare
				// if it is not null
				while ((message = reader.readLine()) != null) {
					System.out.println("read" + message);

					// server communicates with everyone
					tellEveryone(message);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	public static void main(String[] args) {
		new SimpleChatServer().go();
	}

	public void tellEveryone(String message) {

		// writing to everyones
		// goes through the array list
		Iterator it = clientOutputStreams.iterator();
		while (it.hasNext()) {
			try {
				// create a printwriter and flush the message
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} // end while
	} // close tellEveryone

	public void go() {
		clientOutputStreams = new ArrayList();
		try {

			// create that server socket at 5000
			ServerSocket serverSock = new ServerSocket(5000);
			while (true) {
				// keep listening on while loop
				// accept any server sockets that are coming in
				Socket clientSocket = serverSock.accept();

				// writer to the client socket output stream
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				// it will add this new created writer into the list of writers
				clientOutputStreams.add(writer);

				// this thread handles the communication from server side
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("got a connection");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
