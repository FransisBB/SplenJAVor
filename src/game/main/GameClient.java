package game.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.Timer;


public class GameClient {
	private PrintWriter writer;
	public Socket socket;
	ReadThread readThread;
	Game game;

	public GameClient(String hostname, int port, Game game) {
		this.game=game;
		try {
			socket = new Socket(hostname, port);

			System.out.println("Connected to the chat server");
			readThread = new ReadThread(socket,this);
			readThread.start();
		} catch (UnknownHostException ex) {
			System.out.println("Server not found: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("I/O Error: " + ex.getMessage());
		}
		runPing();
	}

	public GameClient(Socket socket, Game game) {
		this.game=game;
		System.out.println("Connected to the chat server");
		readThread = new ReadThread(socket,this);
		readThread.start();
		this.socket = socket;
		runPing();
	}

	public void restartThread() {
		readThread = new ReadThread(socket,this);
		readThread.start();
	}

	public void sendMove(String s) {
		try {
			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);
			writer.println(s);
		} catch (IOException ex) {
			System.out.println("Error getting output stream: " + ex.getMessage());
			ex.printStackTrace();
			recInit();
		}
	}

	public void runPing() {
		Timer timer = new Timer(3000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sendMove("PONG");
			}
		});
		timer.setRepeats(true);
		timer.start();
	}
	public void recInit() {
		sendMove("DC" + game.localID);
		if (game.isStarted) {
			game.board.lostConnection();
		} else {
			game.lostConnection();
		}

	}
}
