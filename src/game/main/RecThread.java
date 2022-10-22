package game.main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

class RecThread extends Thread {
	public AtomicBoolean running = new AtomicBoolean(false);
	Game gameOrigin;
	

	public RecThread(Game game) {
		this.gameOrigin=game;
		running.set(true);
	}

	public void run() {
		Socket socket;
		InetAddress ip = gameOrigin.localClient.socket.getInetAddress();
		int port = gameOrigin.localClient.socket.getPort();
		ResourceBundle txt = gameOrigin.txt;
		while (running.get()) {
			// System.out.println("RUN REC" + new Random().nextInt(1000));
			try {
				socket = new Socket(ip, port);
				Game game = new Game(socket,gameOrigin.localID,gameOrigin.mainFrame, gameOrigin.scrollPane);
				running.set(false);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

}
