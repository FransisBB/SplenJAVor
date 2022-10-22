package game.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

class ReadThread extends Thread {
	private BufferedReader reader;
	public AtomicBoolean running = new AtomicBoolean(false);
	Game game;
	Board board;
	GameClient gc;
	// Socket socket;

	@SuppressWarnings("unchecked")
	public ReadThread(Socket socket, GameClient gc) {
		this.gc=gc;
		this.game=gc.game;
		//this.board=game.board;
		running.set(true);
		try {
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));

			ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
			try {
				game.seed = (int) is.readObject();
				boolean[] seatTaken = (boolean[]) is.readObject();
				String[] nickname = (String[]) is.readObject();
				int[] gender = (int[]) is.readObject();
				for (int i = 0; i < seatTaken.length; i++) {
					if (seatTaken[i]) {
						game.chairs[i].set(nickname[i], gender[i]);
					}
				}
				boolean[] options = (boolean[]) is.readObject();
				for (int i = 0; i < options.length; i++) {
					game.options[i].setSelected(options[i]);
				}
				game.isStarted = (boolean) is.readObject();
				if (game.isStarted)
					game._actions = (List<String>) is.readObject();
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}
		} catch (IOException ex) {
			System.out.println("Error getting input stream: " + ex.getMessage());
			ex.printStackTrace();
			gc.recInit();
		}
	}

	synchronized void tWait() {
		try {
			wait();
		} catch (InterruptedException ie) {
			gc.recInit();
		}
	}

	synchronized void tContinue() {
		notifyAll();
	}

	public void run() {

		while (running.get()) {
			try {
				String response = reader.readLine();
				if (response.startsWith("restartRdy")) {
					gc.restartThread();
					running.set(false);
					break;

				}
				if (response.startsWith("serverReady")) {
					game.serverReady = true;
				}
				if (response.startsWith("ACTION")) {
					if (!board.isRecovery) {
						game.handleAction(response);
						tWait();
					}
					if (!game.isServer)
					game._actions.add(response);
				}
				if (response.startsWith("click")) {
					if (!board.isRecovery)
						game.handleClick(response);
				}
				if (response.startsWith("LOB")) {
					game.handleLobby(response);
				}
				if (response.startsWith("CN")) {
					if (game.isStarted) {
						int n = intChar(response, 2);
						// System.out.println(response);
						if (n != 9) {
							board.players[n].isOnline = true;
							board.playersPanel.repaint();
							board.playersPanel.revalidate();
							String g = " po³¹czy³ ";
							if (board.players[n].gender == 2)
								g = " po³¹czy³a ";
							board.chatPanel.addLine("<br><i><font color=#009900>" + board.players[n].playerName + g
									+ "siê z powrotem!</font></i>");
						}
					} else {

					}
				}
				if (response.startsWith("DC")) {
					if (game.isStarted) {
						int n = intChar(response, 2);
						if (n != 9) {
							if (board.players[n].isOnline) {
								String g = " roz³¹czy³ ";
								if (board.players[n].gender == 2)
									g = " roz³¹czy³a ";
								board.chatPanel.addLine("<br><i><font color=red>" + board.players[n].playerName + g
										+ "siê...</font></i>");
								board.players[n].isOnline = false;
								board.playersPanel.repaint();
								board.playersPanel.revalidate();
							}
						}
					} else {

					}
				}

				if (response.startsWith("ONLINE")) {
					game.handleRecoverUser(response);
				}
				if (response.startsWith("OPTION")) {
					game.handleOptions(response);
				}
				if (response.startsWith("CHAT")) {
					if (game.isStarted) {
						board.chatPanel.addLine(response.substring(4));
						board.playSound("sounds/chat.wav");
					} else
						game.chatPanel.addLine(response.substring(4));
				}
				if (!game.isStarted) {
					if (response.startsWith("START")) {
						game.startGame(game.localID == 999);
					}
					if (response.startsWith("READY") && game.localID < 900) {
						game.readyGet();
					}
					board=game.board;
				}

			} catch (IOException ex) {
				System.out.println("Error reading from server: " + ex.getMessage());
				ex.printStackTrace();
				gc.recInit();
				break;
			}
		}
	}
	public int intChar(String str, int n) {
		return Character.getNumericValue(str.charAt(n));
	}
}