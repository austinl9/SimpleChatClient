package SimpleChatClientImproved;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class SimpleChatClientImproved {

	JTextArea incoming;
	JTextField outgoing;
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;

	public void go() {
		JFrame frame = new JFrame("Simple Chat Client");
		JPanel mainPanel = new JPanel();

		// incoming chat information - can't edit with a scroll bar
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);

		// scroll bar info
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// outgoing text field takes 20 chars
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());

		// add all the info into the main panel
		mainPanel.add(qScroller);
		mainPanel.add(outgoing);
		mainPanel.add(sendButton);
		setUpNetworking();

		// give the thread a job from incoming Readers
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();

		// set the size of the content
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(400, 500);
		frame.setVisible(true);
	}

	// handle the sockets here
	private void setUpNetworking() {
		try {
			// setup a socket to the server
			sock = new Socket("127.0.0.1", 5000);

			// flow for reader client -> buffered reader -> inputstream reader
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);

			// flow for writer client -> printwriter ->outputstream -> server
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("networking established");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public class SendButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				// fill the print writer
				writer.println(outgoing.getText());
				writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			outgoing.setText("");
			outgoing.requestFocus();
		}
	}

	public class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("read “ + message");
					incoming.append(message + "\n");
				} // close while
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} // close run
	}

}
