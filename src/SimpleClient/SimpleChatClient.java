package SimpleClient;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SimpleChatClient {
	JTextField outgoing;
	PrintWriter writer;
	Socket sock;
	
	public void go() {
		JFrame frame = new JFrame("Simple Chat Client");
		JPanel mainPanel = new JPanel();
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		mainPanel.add(outgoing);
		mainPanel.add(sendButton);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		setUpNetworking();
		frame.setSize(400,500);
		frame.setVisible(true);

	}

	private void setUpNetworking() {
		try{
			Socket s = new Socket("127.0.0.1", 4242);
			writer = new PrintWriter(s.getOutputStream());
			System.out.println("network established");
		}
		catch(IOException e){
			e.printStackTrace();
		}
		// make a Socket, then make a PrintWriter
		// assign the PrintWriter to writer instance variable
	}

	public class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			String textVal = outgoing.getText();
			writer.println(textVal);
			
			//send then we flush it
			writer.flush();
			// get the text from the text field and
			// send it to the server using the writer (a PrintWriter)
			outgoing.setText("");
			outgoing.requestFocus();
		
		}
	} 
	
	public static void main(String[] args){
		new SimpleChatClient().go();
	}
} 
