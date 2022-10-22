package game.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import game.main.ConnectHistory;
import game.main.Game;
import ui.TButton;

public class ClientPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -26335609067144658L;

	JScrollPane jsp;
	ComponentAdapter ca;
	JLabel error;
	TButton[] histBtn;
	TButton go, close;
	JFrame jf;

	public ClientPanel(JFrame jf, JScrollPane jsp, TButton... btn) {
		this.jf = jf;
		this.jsp = jsp;
		if (btn != null) {
			for (TButton t : btn) {
				t.setActive(false);
			}
		}
		this.ca = new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				update();
			}
		};

		setOpaque(false);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.insets = new Insets(5, 7, 5, 5);
		String text;
		Font fontBig = new Font("Verdana", Font.BOLD, 14);
		Font fontSmall = new Font("Verdana", Font.PLAIN, 13);

		// grid 0
		text = "Do³¹czanie do istniej¹cej gry";
		DivLabel title = new DivLabel(text, fontBig);
		gbc.gridy = 0;
		add(title, gbc);

		// grid 1
		text = "<p align='justify'>Wpisz poni¿ej IP lub nazwê komputera w sieci lokalnej oraz port, które otrzyma³eœ od hosta, lub wybierz przyciskiem jedno z poprzednich po³¹czeñ (jeœli dostêpne).</p>";
		DivLabel info = new DivLabel(text, fontSmall);
		gbc.gridy = 1;
		add(info, gbc);

		// grid 2
		JPanel pInput = new JPanel();
		pInput.setOpaque(false);
		pInput.setLayout(new GridBagLayout());
		GridBagConstraints gbcb = new GridBagConstraints();
		gbcb.gridx = 0;
		gbcb.gridy = 0;
		JLabel ip = new JLabel("<html><b>IP lub nazwa komputera:</b></html>");
		ip.setFont(fontSmall);
		pInput.add(ip, gbcb);
		JLabel port = new JLabel("<html><b>Port:</b></html>");
		port.setFont(fontSmall);
		gbcb.gridx = 1;
		pInput.add(port, gbcb);

		gbc.insets = new Insets(2, 2, 0, 0);
		gbc.gridy = 2;
		// add(p1, gbc);

		// grid 3
		JTextField inputIP = new JTextField(20);
		inputIP.setHorizontalAlignment(JTextField.CENTER);
		inputIP.setFont(new Font("Arial", Font.BOLD, 16));
		inputIP.setMargin(new Insets(2, 2, 2, 2));
		inputIP.setText("localhost");

		gbcb.gridx = 0;
		gbcb.gridy = 1;
		pInput.add(inputIP, gbcb);

		JTextField inputPort = new JTextField(5);
		inputPort.setDocument(new JTextFieldLimit(5));
		inputPort.setHorizontalAlignment(JTextField.CENTER);
		inputPort.setFont(new Font("Arial", Font.BOLD, 16));
		inputPort.setMargin(new Insets(2, 2, 2, 2));
		inputPort.setText("25255");
		gbcb.gridx = 1;
		gbcb.gridy = 1;
		pInput.add(inputPort, gbcb);

		gbc.gridy = 3;
		add(pInput, gbc);

		String addr[] = ConnectHistory.readLines();
		histBtn = new TButton[addr.length];

		JPanel history = new JPanel(new GridBagLayout());
		history.setOpaque(false);
		gbcb.insets = new Insets(0, 2, 0, 2);
		gbcb.gridy = 0;
		for (int i = 0; i < addr.length; i++) {
			int dots = addr[i].indexOf(":");
			if (i == 0) {
				inputIP.setText(addr[i].substring(0, dots));
				inputPort.setText(addr[i].substring(dots + 1));
			}
			gbcb.gridx = i;
			int j=i;
			histBtn[i] = new TButton(addr[i], "green", 12, 4);
			histBtn[i].addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						inputIP.setText(addr[j].substring(0, dots));
						inputPort.setText(addr[j].substring(dots + 1));
						tryConnect(inputPort.getText(), inputIP.getText());
					}
				}
			});

			history.add(histBtn[i], gbcb);
		}
		gbc.gridy = 4;
		add(history, gbc);
		// grid 5
		error = new JLabel(" ");
		error.setForeground(Color.RED);
		gbc.gridy = 5;
		add(error, gbc);

		// grid 6
		JPanel buttons = new JPanel();
		buttons.setOpaque(false);
		close = new TButton("Zamknij", "yellow", 14);
		close.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					if (btn != null) {
						for (TButton t : btn) {
							t.setActive(true);
						}
					}
					clear();
				}
			}
		});
		go = new TButton("Do³¹cz do gry", "green", 14);
		go.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					tryConnect(inputPort.getText(), inputIP.getText());
				}
			}
		});

		buttons.add(go);
		buttons.add(close);

		gbc.insets = new Insets(2, 2, 6, 2);
		gbc.gridy = 6;
		add(buttons, gbc);
	}

	public void tryConnect(String getport, String getIP) {
		if (getIP != null && !getIP.isEmpty()) {
			if (isProperPort(getport)) {
				go.setActive(false);
				close.setActive(false);
				for (TButton t : histBtn)
					t.setActive(false);
				Timer timer = new Timer(500, new ActionListener() {
					int tick = 0;

					public void actionPerformed(ActionEvent evt) {
						String dots = ".";
						for (int i = 0; i < tick % 3; i++) {
							dots = dots + ".";
						}
						error.setText(dots + "Próbujê po³¹czyæ" + dots);
						tick++;
					}
				});
				timer.setRepeats(true);
				timer.setInitialDelay(0);
				timer.start();
				Thread thread = new Thread() {
					public void run() {
						try {
							Socket socket = new Socket(getIP, Integer.parseInt(getport));
							new Game(socket, 999, jf, jsp);
						} catch (NumberFormatException e) {
							timer.stop();
							System.out.println(e);
							error.setText("Niepoprawny format");
						} catch (UnknownHostException e) {
							timer.stop();
							System.out.println(e);
							error.setText("B³êdne IP");
						} catch (IOException e) {
							timer.stop();
							System.out.println(e);
							error.setText("Nie znaleziono serwera lub port jest zajêty");
						}
						timer.stop();
						go.setActive(true);
						close.setActive(true);
						for (TButton t : histBtn)
							t.setActive(true);

					}
				};
				thread.start();
			}
		} else {
			error.setText("Musisz podaæ IP serwera");
		}
	}

	public void update() {
		int widthp = getPreferredSize().width;
		int heightp = getPreferredSize().height;
		setBounds(2 + (jsp.getWidth() - widthp) / 2, (jsp.getHeight() - heightp) / 2, widthp, heightp);
		jsp.repaint();
		jsp.revalidate();
	}

	public void showThis() {
		int widthp = getPreferredSize().width;
		int heightp = getPreferredSize().height;
		setBounds(2 + (jsp.getWidth() - widthp) / 2, (jsp.getHeight() - heightp) / 2, widthp, heightp);
		jsp.add(this, 0);
		jsp.repaint();
		jsp.revalidate();
		jsp.addComponentListener(ca);
	}

	public void clear() {
		jsp.removeComponentListener(ca);
		jsp.remove(this);
		jsp.repaint();
		jsp.revalidate();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color(0, 0, 0, 50));
		g2.fillRoundRect(2, 2, this.getWidth() - 3, this.getHeight() - 3, 10, 10);
		g2.setColor(new Color(255, 255, 245));
		g2.fillRoundRect(1, 1, this.getWidth() - 4, this.getHeight() - 4, 10, 10);
		g2.setColor(new Color(50, 50, 0, 150));
		g2.drawRoundRect(1, 1, this.getWidth() - 4, this.getHeight() - 4, 10, 10);
	}

	private class JTextFieldLimit extends PlainDocument {
		/**
		* 
		*/
		private static final long serialVersionUID = -2620615497599891558L;

		private int limit;

		JTextFieldLimit(int limit) {
			super();
			this.limit = limit;
		}

		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null)
				return;

			if ((getLength() + str.length()) <= limit) {

				try {
					double d = Integer.parseInt(str);
					super.insertString(offset, str, attr);
				} catch (NumberFormatException nfe) {
					return;
				}

			}
		}
	}

	private class DivLabel extends JLabel {
		public DivLabel(String s, Font f) {
			setFont(f);
			String labelText = String.format("<html><div style=\"width:%dpx;\"><center>%s</center></div></html>", 300,
					s);
			setText(labelText);
		}

		public void putText(String s) {
			String labelText = String.format("<html><div style=\"width:%dpx;\"><center>%s</center></div></html>", 300,
					s);
			setText(labelText);
		}
	}

	public boolean isProperPort(String strNum) {
		if (strNum == null || strNum.isEmpty()) {
			error.setText("Musisz wpisaæ port");
			return false;
		}
		try {
			double d = Integer.parseInt(strNum);
			if (d < 10000 || d > 65353) {
				error.setText("Port musi byæ w zakresie 10000-65363");
				return false;
			}
		} catch (NumberFormatException nfe) {
			error.setText("Niepoprawny format");
			return false;
		}
		return true;
	}

}
