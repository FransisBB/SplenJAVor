package game.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import game.panels.ServerPanel;

public class GameServer extends Thread {
	int port;
	private Set<UserThread> userThreads = new HashSet<>();
	private Game game;
	ServerSocket serverSocket;
	
	public GameServer(int port, Game game) throws IOException{
		this.port = port;
		this.game = game;	
		game.seed=new Random().nextInt();
		serverSocket = new ServerSocket(port);	
		start();
	}

	public void run() {
			while (true) {
				Socket socket;
				try {
					socket = serverSocket.accept();
					System.out.println("New user connected");
					createUserThread(socket);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
	}
	void restartUser(UserThread targetUser, Socket socket) {
		for (UserThread aUser : userThreads) {
			if (aUser == targetUser) {
				aUser.sendMessage("restartRdy");
				removeUser(aUser);
				createUserThread(socket);
				break;
			}
		}
	}

	void createUserThread(Socket socket) {
		UserThread newUser = new UserThread(socket, game, this);
		userThreads.add(newUser);
		newUser.start();

	}

	void checkDouble(int n, UserThread excludeUser) {
		for (UserThread aUser : userThreads) {
			if (aUser != excludeUser) {
				if (aUser.playerID == n) {
					aUser.playerID = 999;
					broadcast("DC" + n, excludeUser);
				}
			}
		}
		broadcast("CN" + n, excludeUser);
	}

	public void resetSeat(int n) {
		for (UserThread aUser : userThreads) {
			if (aUser.seat == n) {
				aUser.seat = 999;
				broadcast("LOBLVE" + n, null);
			}
		}
	}

	void broadcast(String message, UserThread excludeUser) {
		if (message.startsWith("ACTION") && game.isServer) {
				game._actions.add(message);
				System.out.println(message);
		}
		for (UserThread aUser : userThreads) {
			if (aUser != excludeUser) {
				aUser.sendMessage(message);
			}
		}
	}

	void removeUser(UserThread aUser) {
		aUser.running.set(false);
		userThreads.remove(aUser);
		System.out.println("The user quitted");
	}
}