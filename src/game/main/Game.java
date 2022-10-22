package game.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import game.box.ChatPanel;
import game.box.LocalPlayerPanel;
import game.obj.Chair;
import ui.Logo;
import ui.MPanel;
import ui.TButton;

public class Game{
	int maxPlayers = 5;
	public ResourceBundle txt = ResourceBundle.getBundle("Labels");
	public int seed, localID, rdyCount, rand = new Random().nextInt(1000);
	JFrame mainFrame;
	//JPanel mainGamePanel;
	Chair[] chairs = new Chair[maxPlayers];
	JCheckBox[] options = new JCheckBox[9];
	public JScrollPane scrollPane;
	public boolean serverReady, isStarted, isServer, isOwner = false;
	public List<String> _actions;
	public GameClient localClient;
	public GameServer server;
	public ChatPanel chatPanel;
	public Board board;
	MPanel chat;
	public MPanel table;

	public Game(int port, JFrame jf, JScrollPane jsp) throws IOException {
		mainFrame = jf;
		scrollPane = jsp;
		setup(true, 999, jf, jsp);
		server = new GameServer(port, this);
		localClient = new GameClient("localhost", port, this);
		startTable();

	}

	public Game(Socket socket, int localID, JFrame jf, JScrollPane jsp) {
		setup(false, localID, jf, jsp);
		localClient = new GameClient(socket, this);
		ConnectHistory.addNew(socket);
		startTable();
	}
	
	public void sendAction(String s) {
		localClient.sendMove(s);
	}

	public void lostConnection() {
		Game game = this;
		JLabel info = new JLabel();
		info.setBackground(Color.RED);
		info.setFont(new Font("Verdana", Font.BOLD, 13));

		JPanel reco = new JPanel();
		reco.setPreferredSize(new Dimension(0, 0));
		reco.setOpaque(false);

		String text = "Utracono po³¹czenie z serwerem!<br>";
		int textWidth = chat.getWidth() - 20;
		info.setText(
				String.format("<html><div style=\"width:%dpx;\"><center>%s</center></div></html>", textWidth, text));
		Timer timer = new Timer(1000, new ActionListener() {
			int tick = 0;
			boolean listener = true;

			public void actionPerformed(ActionEvent evt) {
				tick++;
				// Timer t = (Timer) evt.getSource();
				String dots = " .";
				for (int i = 0; i < tick % 10; i++)
					dots = dots + " .";
				String text = lt("trying_connect") + dots;
				info.setText(String.format("<html><div style=\"width:%dpx;\"><center>%s</center></div></html>",
						textWidth, text));
				if (listener) {
					reco.removeAll();
					reco.add(info);
					reco.repaint();
					chat.repaint();
					RecThread rc = new RecThread(game);
					rc.start();
					listener = false;
				}
			}
		});
		timer.setRepeats(true);
		timer.setInitialDelay(0);
		TButton doRec = new TButton("Po³¹cz ponownie", "green", 13);
		doRec.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					timer.start();
				}
			}
		});
		chat.removeAll();
		reco.add(info);
		reco.add(doRec);
		chat.add(reco);
		chat.repaint();
		chat.revalidate();
	}
	public void closeTry() {
		mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
	}
	public void setup(boolean isServer, int localID, JFrame jf, JScrollPane jsp) {
		mainFrame = jf;
		scrollPane = jsp;
		rdyCount = 0;
		isStarted = false;
		serverReady = false;
		_actions = new ArrayList<String>();
		this.isServer = isServer;
		this.localID = localID;
		
		for (int i = 0; i < maxPlayers; i++)
			chairs[i] = new Chair(i, this);
		for (int i = 0; i < options.length; i++) {
			options[i] = new JCheckBox("", false);
			options[i].setOpaque(false);
			options[i].setEnabled(false);
			// 21 / 21
			// Set default icon for checkbox
			// options[i].setIcon(getIcon("icon.png"));
			// Set selected icon when checkbox state is selected
			// options[i].setSelectedIcon(getIcon("selectedIcon.png"));
			// Set disabled icon for checkbox
			// options[i].setDisabledIcon(getIcon("disabledIcon.png"));
			// Set disabled-selected icon for checkbox
			// options[i].setDisabledSelectedIcon(getIcon("disabledSelectedIcon.png"));
			// Set checkbox icon when checkbox is pressed
			// options[i].setPressedIcon(getIcon("pressedIcon.png"));
			// Set icon when a mouse is over the checkbox
			// options[i].setRolloverIcon(getIcon("rolloverIcon.png"));
			// Set icon when a mouse is over a selected checkbox
			// options[i].setRolloverSelectedIcon(getIcon("rolloverSelectedIcon.png"));
		}
	}

	public void startTable() {
		mainFrame.remove(scrollPane);
		GridBagConstraints gbc = new GridBagConstraints();
		JPanel mainGamePanel = new BackgroundPane();
		mainGamePanel.setLayout(new GridBagLayout());
		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 0;

		Logo top = new Logo(100);
		top.setPreferredSize(new Dimension(mainGamePanel.getWidth(), 110));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 5;

		mainGamePanel.add(top, gbc);
		gbc.weighty = 1;
		gbc.gridwidth = 1;

		JLabel bot = new JLabel("Copyright © 2020 Fransis (fransis1989@gmail.com) " + lt("ver"));
		bot.setFont(new Font("Verdana", Font.BOLD, 12));
		bot.setVerticalAlignment(SwingConstants.BOTTOM);
		bot.setHorizontalAlignment(SwingConstants.CENTER);

		if (!isStarted) {
			localID = 999;
			gbc.weightx = 0;
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.gridheight = 2;
			mainGamePanel.add(new GameAbout("Informacje:", isServer, isServer ? server.port : 0), gbc);

			gbc.gridx = 3;
			gbc.gridheight = 1;
			gbc.weighty = 0;
			mainGamePanel.add(new GameSettings("Ustawienia gry:", this), gbc);

			chat = new MPanel("Chat:");
			chat.setLayout(new GridLayout(1, 1));
			chatPanel = new ChatPanel(this, "Guest" + rand, false);
			if (!isServer) {
				String s = "CHAT<br><i><font color=blue>" + chatPanel.nick + " do³¹czy³ do gry!</font></i>";
				sendAction(s);
			}
			chat.add(chatPanel);

			gbc.gridy = 2;
			gbc.weighty = 1;
			mainGamePanel.add(chat, gbc);

			table = new MPanel(lt("take_seat"));
			table.setLayout(new BoxLayout(table, BoxLayout.Y_AXIS));
			for (int i = 0; i < 4; i++) {
				table.add(chairs[i]);
			}
			if (options[7].isSelected()) {
				table.add(chairs[4]);
			}

			gbc.gridy = 1;
			gbc.gridheight = 2;
			gbc.gridx = 2;
			gbc.weightx = 1;
			mainGamePanel.add(table, gbc);

			JLabel filler1 = new JLabel();
			JLabel filler2 = new JLabel();
			gbc.gridx = 0;
			mainGamePanel.add(filler1, gbc);
			gbc.gridx = 5;
			mainGamePanel.add(filler2, gbc);

			JPanel buttons = new JPanel();
			buttons.setOpaque(false);
			if (isServer || isOwner) {
				TButton start = new TButton(lt("start_game"), "green", 16);
				start.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent me) {
						if (((TButton) me.getComponent()).releasedInside()) {
							if (canStart()) {
								if (isServer) {
									startGame(localID == 999);
									sendAction("START");
								} else if (isOwner) {

								}
							} else {
								JOptionPane.showMessageDialog(mainGamePanel, lt("start_error"), lt("start_error_lab"),
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				});
				buttons.add(start);
			}
			TButton exit = new TButton(lt("exit"), "red", 16);
			exit.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						closeTry();
					}
				}
			});
			buttons.add(exit);

			gbc.gridx = 1;
			gbc.gridy = 4;
			gbc.gridheight = 1;
			gbc.gridwidth = 3;

			mainGamePanel.add(buttons, gbc);
			gbc.gridy = 5;

			mainGamePanel.add(bot, gbc);

			
			scrollPane = new JScrollPane(mainGamePanel);
			mainFrame.add(scrollPane);
			mainFrame.repaint();
			mainFrame.revalidate();
			setChairs();
		} else {
			rdyCount = 0;
			startGame(true);
		}
	}

	public boolean canStart() {
		int n = 0;
		for (Chair p : chairs)
			if (p.taken)
				n++;
		return n >= 2;

		/*
		 * TODO Warunki startu
		 */
	}

	public void restart() {
		setup(isServer, 999,mainFrame,scrollPane);
		if (isServer) {
			seed = new Random().nextInt();
			sendAction("serverReady");
		} else {
			sendAction("restart");
		}
		startTable();

	}

	public void nextRecStep() {

		if (rdyCount < _actions.size()) {
			handleAction(_actions.get(rdyCount));
			rdyCount++;
		} else {
			// int n = rn;
			board.isRecovery = false;
			if (localID < 900) {
				board.players[localID].isLocal = true;
				board.players[localID].isOnline = true;
				board.localPlayerPanel = new LocalPlayerPanel(localID, board);
				board.addComp(1, 3, 1, 1, 1, 0, 0, 0, board.localPlayerPanel);
				board.chatPanel = new ChatPanel(this, board.players[localID].playerName, true);
				board.addComp(2, 3, 2, 1, 1, 0, 0, 0, board.chatPanel);
				sendAction("ID" + localID);
				if (board.aPID == localID)
					board.cancelAction(false);

			} else {
				localID = 900;
				board.chatPanel = new ChatPanel(this, "Spec" + rand, true);
				board.addComp(2, 3, 2, 1, 1, 0, 0, 0, board.chatPanel);

			}
			isStarted = true;
			board.clearAndAddInfoLabel("Po³¹czenie udane!");
			board.boardPanel.repaint();
			board.boardPanel.revalidate();
		}
	}

	public void startGame(boolean isRecovery) {
		mainFrame.remove(scrollPane);
		board = new Board(this, isRecovery);
		scrollPane = new JScrollPane(board.boardPanel);
		mainFrame.add(scrollPane);
		mainFrame.repaint();
		mainFrame.revalidate();

	}

	public void setChairs() {
		for (int i = 0; i < maxPlayers; i++) {
			chairs[i].change();
		}
		for (int i = 0; i < maxPlayers; i++) {
			if (chairs[i].sp != null)
				chairs[i].sp.update();
		}
	}

	public void handleOptions(String o) {
		int n = intChar(o, 6);
		int s = intChar(o, 7);
		switch (s) {
		case 1:
			options[n].setSelected(true);
			break;
		case 2:
			options[n].setSelected(false);
			break;
		default:
			options[n].setSelected(false);
			break;
		}
	}

	public void handleLobby(String lob) {

		if (lob.substring(3).startsWith("RES")) {
			int i = intChar(lob, 6);
			chairs[i].set("", 0);
		}

		if (lob.substring(3).startsWith("NCK")) {
			int i = intChar(lob, 6);
			chairs[i].set(lob.substring(8), intChar(lob, 7));
		}
		if (lob.substring(3).startsWith("LVE")) {
			int i = intChar(lob, 6);
			if (i == localID) {
				localID = 999;
				System.out.println("ABB" + localID);
				sendAction(chatPanel.editNick("Guest" + rand));
			}
			chairs[i].free();
////
		}
		if (lob.substring(3).startsWith("ACC")) {
			chairs[intChar(lob, 6)].sitDownAccepted();
		}
		if (lob.substring(3).startsWith("DEC")) {
			chairs[intChar(lob, 6)].standUp();
		}
		////

	}

	public void handleClick(String clack) {
		String gender = board.players[board.aPID].gender == 2 ? "a" : "";
		if (clack.charAt(5) == '1') {
			String[] text = new String[] { "patrzy na karty!", "wyci¹ga rêkê ku kartom...",
					"intensywnie wpatruje siê w jedn¹ kartê...", "wygl¹da jakby chcia³" + gender + " kupiæ kartê..." };
			int n = new Random().nextInt(text.length);
			board.remoteInfoLabel(text[n]);
		}
		if (clack.charAt(5) == '2') {
			String[] text = new String[] { "grzebie w ¿etonach...", "bawi siê ¿etonami...",
					"zapatrzy³" + gender + " siê na ¿etony...", "podnosi i opuszcza ¿etony..." };
			int n = new Random().nextInt(text.length);
			board.remoteInfoLabel(text[n]);
		}
		if (clack.charAt(5) == '3') {
			String[] text = new String[] { "ma za du¿o ¿etonów i teraz wybiera co oddaæ...",
					"nabra³" + gender + " ¿etonów i teraz musi coœ oddaæ..." };
			int n = new Random().nextInt(text.length);
			board.remoteInfoLabel(text[n]);
		}
		if (clack.charAt(5) == '4') {
			String[] text = new String[] { "no, to chyba ju¿!", "chyba wybra³" + gender + "!", "mo¿liwe ¿e koñczy!" };
			int n = new Random().nextInt(text.length);
			board.remoteInfoLabel(text[n]);
		}
		if (clack.charAt(5) == '5') {
			board.remoteInfoLabel("wykonuje akcjê twierdz.");
		}
		// 5 "wykonuje akcjê twierdz.
		if (clack.charAt(5) == '0') {
			board.remoteInfoLabel("start");

		}
	}

	public void handleRecoverUser(String response) {
		Object[] options;
		int[] pID;
		int offNum = 0;
		for (int i = 0; i < board.players.length; i++)
			if (response.charAt(i + 6) == 'n')
				offNum++;
		if (offNum > 0 && localID == 999) {
			options = new Object[offNum];
			pID = new int[offNum];
			offNum = 0;
			for (int i = 0; i < board.players.length; i++) {
				if (response.charAt(i + 6) == 'n') {
					board.players[i].isOnline = false;
					options[offNum] = board.players[i].playerName;
					pID[offNum] = i;
					offNum++;
				}
			}
			int n = JOptionPane.showOptionDialog(mainFrame, "Usi¹dz na miejscu:", "Wybierz gracza",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (n == -1) {
				handleRecoverUser(response);
			} else {
				localID = pID[n];
				board.startTurn(true);
			}
		} else {
			board.startTurn(true);
		}
		board.boardPanel.repaint();
		board.boardPanel.revalidate();
	}

	public void handleAction(String action) {

		if (action.indexOf("ARI") != -1)
			board.ariGet = intChar(action, action.indexOf("ARI") + 3);
		if (action.indexOf("CITY") != -1)
			board.cityGet = intChar(action, action.indexOf("CITY") + 4);
		if (action.indexOf("SPEC") != -1)
			board.specialModifier = intChar(action, action.indexOf("SPEC") + 4);
		if (action.indexOf("SPED") != -1)
			board.specialModifierB = intChar(action, action.indexOf("SPED") + 4);
		if (action.indexOf("DBLC") != -1)
			board.dblc = Integer.parseInt(action.substring(action.indexOf("DBLC") + 4, action.indexOf("DBLC") + 6));
		if (action.indexOf("DBLD") != -1)
			board.dbld = Integer.parseInt(action.substring(action.indexOf("DBLD") + 4, action.indexOf("DBLD") + 6));

		if (action.indexOf("FRA") != -1) {
			for (int i = 0; i < 4; i++)
				board.fra[i] = intChar(action, action.indexOf("FRA") + 3 + i);
		} else
			board.fra[0] = 9;

		if (action.indexOf("FRB") != -1) {
			for (int i = 0; i < 4; i++)
				board.frb[i] = intChar(action, action.indexOf("FRB") + 3 + i);
		} else
			board.frb[0] = 9;

		if (action.indexOf("FRC") != -1) {
			for (int i = 0; i < 4; i++)
				board.frc[i] = intChar(action, action.indexOf("FRC") + 3 + i);
		} else
			board.frc[0] = 9;

		if (action.indexOf("FRD") != -1) {
			for (int i = 0; i < 4; i++)
				board.frd[i] = intChar(action, action.indexOf("FRD") + 3 + i);
		} else
			board.frd[0] = 9;

		if (action.indexOf("BCA") != -1)
			board.fra[0] = Integer.parseInt(action.substring(action.indexOf("BCA") + 3, action.indexOf("BCA") + 6));
		if (action.indexOf("BCB") != -1)
			board.fra[1] = Integer.parseInt(action.substring(action.indexOf("BCB") + 3, action.indexOf("BCB") + 6));
		if (action.indexOf("BCC") != -1)
			board.frc[0] = Integer.parseInt(action.substring(action.indexOf("BCC") + 3, action.indexOf("BCC") + 6));
		if (action.indexOf("BCD") != -1)
			board.frc[1] = Integer.parseInt(action.substring(action.indexOf("BCD") + 3, action.indexOf("BCD") + 6));

		if (action.indexOf("STR") != -1) {
			int startIndex = action.indexOf("STR") + 3;
			// str[ADD,MOVE,REMOVE],D,R,C,D2,R2,C2,[ADD,REMOVE],D,R,C,ifBuy;
			board.strTypeA = intChar(action, startIndex);
			board.strAD = intChar(action, startIndex + 1);
			board.strAR = intChar(action, startIndex + 2);
			board.strAC = intChar(action, startIndex + 3);
			board.strAD2 = intChar(action, startIndex + 4);
			board.strAR2 = intChar(action, startIndex + 5);
			board.strAC2 = intChar(action, startIndex + 6);
			board.strTypeB = intChar(action, startIndex + 7);
			board.strBD = intChar(action, startIndex + 8);
			board.strBR = intChar(action, startIndex + 9);
			board.strBC = intChar(action, startIndex + 10);
			board.strBuy = intChar(action, startIndex + 11) == 1 ? true : false;

		}
		if (action.indexOf("STCN") != -1) {
			int startIndex = action.indexOf("STCN") + 4;
			// str[ADD,MOVE,REMOVE],D,R,C,D2,R2,C2,[ADD,REMOVE],D,R,C,ifBuy;
			for (int i = 0; i < 6; i++)
				board.strCoinsNeeded[i] = intChar(action, startIndex + i);
		}
		if (action.charAt(7) == '0')
			handleActionCard(action.substring(8));
		if (action.charAt(7) == '1')
			handleActionCoins(action.substring(8));
		if (action.charAt(7) == '3')
			board.remoteAction(null, 4, 0, false, false, false, false);
	}

	public void handleActionCard(String action) {
		if (action.charAt(0) == '0') {
			int coinsNeeded[] = new int[6];
			for (int i = 0; i < 6; i++)
				coinsNeeded[i] = intChar(action, 1 + i);
			board.actionBuyCard(intChar(action, 7), intChar(action, 8), intChar(action, 9), false, false, coinsNeeded);
		}
		if (action.charAt(0) == '1') {
			if (action.charAt(2) == 'B') {

				int[] coinsBack = new int[6];
				for (int i = 0; i < 6; i++) {
					coinsBack[i] = intChar(action, i + 3);
				}
				board.actionReserveCard(intChar(action, 9), intChar(action, 10), intChar(action, 11));
				board.remoteAction(null, intChar(action, 1), intChar(action, 11), true, false, false, false, coinsBack);
			} else {
				board.actionReserveCard(intChar(action, 2), intChar(action, 3), intChar(action, 4));
				board.remoteAction(null, intChar(action, 1), intChar(action, 4), false, false, false, false);
			}
		}
		if (action.charAt(0) == '2') {
			int coinsNeeded[] = new int[6];
			for (int i = 0; i < 6; i++) {
				coinsNeeded[i] = intChar(action, 1 + i);
			}
			// board.actionBuyResCard(intChar(action, 7), coinsNeeded);
			int n = intChar(action, 7);
			board.actionBuyCard(n, n, 0, true, false, coinsNeeded);
		}
	}

	public void handleActionCoins(String action) {
		int[] coins = new int[6];
		for (int i = 0; i < 6; i++) {
			coins[i] = Character.getNumericValue(action.charAt(i));
		}
		if (action.charAt(6) == 'B') {
			int[] coinsBack = new int[6];
			for (int i = 0; i < 6; i++) {
				coinsBack[i] = Character.getNumericValue(action.charAt(i + 7));
			}
			board.remoteAction(coins, 3, 0, true, false, false, false, coinsBack);
		} else
			board.remoteAction(coins, 3, 0, false, false, false, false);

	}

	public void readyGet() {
		rdyCount++;
		int n = 0;
		for (Chair p : chairs)
			if (p.taken)
				n++;
		if (rdyCount == n) {
			board.startGame();
		}
	}

	public boolean readyCheck() {
		sendAction("READY");
		rdyCount++;
		int n = 0;
		for (Chair p : chairs)
			if (p.taken)
				n++;
		if (rdyCount == n) {
			return true;
		} else
			return false;
	}

	public int intChar(String str, int n) {
		return Character.getNumericValue(str.charAt(n));
	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	public String lt(String s) {
		try {
			return txt.getString(s);
		} catch (Exception e) {
			return s;
		}
	}
}