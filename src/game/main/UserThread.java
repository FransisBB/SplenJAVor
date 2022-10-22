package game.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JCheckBox;
import javax.swing.Timer;

import game.obj.Chair;

class UserThread extends Thread {
	private Socket socket;
	private PrintWriter writer;
	int playerID = 999, seat = 999;
	final AtomicBoolean running = new AtomicBoolean(false);
	BufferedReader reader;
	private Game game;
	private GameServer server;
	
	
	public UserThread(Socket socket, Game game, GameServer server) {
		this.socket = socket;
		this.game = game;
		this.server = server;
		running.set(true);
	}

	public void run() {
			Chair[] chairs = game.chairs;
			int maxPlayers = game.maxPlayers;
		try {
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));

			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);

			ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
			os.writeObject(game.seed);
			os.writeObject(seatTaken(chairs,maxPlayers));
			os.writeObject(nickname(chairs,maxPlayers));
			os.writeObject(gender(chairs,maxPlayers));
			os.writeObject(options(game.options));
			os.writeObject(game.isStarted);
			if (game.isStarted)
				os.writeObject(game._actions);

			Timer timer = new Timer(3000, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					sendMessage("PING");
				}
			});
			timer.setRepeats(true);
			timer.start();

			String clientMessage;
			boolean broadcast;
			do {
				clientMessage = reader.readLine();
				broadcast = true;

				if (clientMessage.startsWith("restart")) {
					server.restartUser(this, this.socket);
					break;
				} else if (clientMessage.startsWith("ID")) {
					playerID = intChar(clientMessage, 2);
					server.checkDouble(playerID, this);
				} else if (clientMessage.startsWith("ASKONLINE")) {
					String serverResponse = "ONLINE";
					for (int i = 0; i < game.board.players.length; i++) {
						if (game.board.players[i].isOnline) {
							serverResponse += "t";
						} else
							serverResponse += "n";
					}
					sendMessage(serverResponse);
				} else if (clientMessage.startsWith("LOBRES")) {
					int n = intChar(clientMessage, 6);
					if (chairs[n].taken) {
						sendMessage("LOBDEC" + n);
						sendMessage("LOBNCK" + n + chairs[n].gender + chairs[n].nick);
						broadcast = false;

					} else {
						chairs[n].taken = true;
						sendMessage("LOBACC" + n);
						seat = n;
					}
				} else if (clientMessage.startsWith("DC")) {
					reader.close();
					writer.close();
					os.close();
					socket.close();
				} else if (clientMessage.startsWith("LOBNCK")) {
					if (seat != intChar(clientMessage, 6)) {
						broadcast = false;
					}
				} else if (clientMessage.startsWith("LOBLVE")) {
					int n = intChar(clientMessage, 6);
					if (seat == n) {
						seat = 999;
					} else
						broadcast = false;
					// server.resetSeat(n);
				}

				if (clientMessage.startsWith("PONG")) {
					broadcast = false;
				}
				if (broadcast)
					server.broadcast(clientMessage, this);

			} while (running.get());

		} catch (IOException ex) {
			if (running.get()) {
				if (playerID != 999) {
					server.broadcast("DC" + playerID, this);
					// System.out.println("DCDC" + this);
				}
				if (seat != 999 && !game.isStarted)
					server.broadcast("LOBLVE" + seat, this);
				if (!game.isStarted)
					server.broadcast("CHAT<br><i><font color=red>Jeden z graczy roz³¹czy³ siê...</font></i>", this);
				server.removeUser(this);
			}
			System.out.println("Error in UserThread: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	void sendMessage(String message) {
		writer.println(message);
	}
	public int intChar(String str, int n) {
		return Character.getNumericValue(str.charAt(n));
	}
	
	public boolean[] seatTaken(Chair [] chairs, int maxPlayers) {
		boolean[] seatTaken = new boolean[maxPlayers];
		for (int i = 0; i < maxPlayers; i++) {
			seatTaken[i] = chairs[i].taken;
		}
		return seatTaken;
	}

	public String[] nickname(Chair [] chairs, int maxPlayers) {
		String[] nickname = new String[maxPlayers];
		for (int i = 0; i < maxPlayers; i++)
			nickname[i] = chairs[i].nick;
		return nickname;
	}

	public int[] gender(Chair [] chairs, int maxPlayers) {
		int[] gender = new int[maxPlayers];
		for (int i = 0; i < maxPlayers; i++)
			gender[i] = chairs[i].gender;
		return gender;
	}

	public boolean[] options(JCheckBox[]options) {
		boolean[] option = new boolean[options.length];
		for (int i = 0; i < options.length; i++) {
			option[i] = options[i].isSelected();
		}

		return option;
	}
}
