package game.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public final class ConnectHistory {
	static String filePath = "connectHistory.txt";

	private ConnectHistory() {
	}

	private static int checkLines() {
		BufferedReader reader;
		int lines = 0;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			while (reader.readLine() != null)
				lines++;
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public static String[] readLines() {
		String[] addrAll = new String[checkLines()];
		BufferedReader reader;
		int i = addrAll.length;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			while (true) {
				i--;
				String t = reader.readLine();
				if (t == null)
					break;

				addrAll[i] = t;	
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return addrAll;
	}

	public static void addNew(Socket socket) {
		String addrOrg = socket.getInetAddress().toString();
		int n = addrOrg.indexOf("/");
		String addrFin = addrOrg.substring(0, n);
		if (addrFin.isEmpty())
			addrFin = addrOrg.substring(n + 1);

		String msg = addrFin + ":" + socket.getPort();

		String[] exAddr = readLines();
		for (String t : exAddr)
			if (t.equals(msg))
				return;

		//int space = msg.indexOf(":");
		//System.out.println("Address:" + msg.substring(0, space) + " , Port:" + msg.substring(space + 1));

		PrintWriter printWriter = null;
		File file = new File(filePath);
		try {
			if (!file.exists())
				file.createNewFile();
			printWriter = new PrintWriter(new FileOutputStream(filePath, true));
			printWriter.write(msg + System.getProperty("line.separator"));
		} catch (IOException ioex) {
			ioex.printStackTrace();
		} finally {
			if (printWriter != null) {
				printWriter.flush();
				printWriter.close();
			}
		}
		cut();

	}
	private static void cut () {
		while (checkLines() > 3) {
			try {
				removeHeader();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	private static void removeHeader() throws IOException, InterruptedException {
		File inFile = new File(filePath);
		File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

		BufferedReader br = null;
		PrintWriter pw = null;

		try {
			br = new BufferedReader(new FileReader(filePath));
			pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;
			boolean first = true;
			while ((line = br.readLine()) != null) {
				if (!first) {
					pw.println(line);
					pw.flush();
				}

				first = false;
			}
		} finally {
			pw.close();
			br.close();
		}

		if (inFile.exists() && tempFile.exists()) {
			inFile.delete();
			tempFile.renameTo(inFile);
		}
	}
}
