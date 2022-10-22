package game.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import game.main.Game;
import ui.TButton;

public class ServerPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -26335609067144658L;

	JScrollPane jsp;
	ComponentAdapter ca;
	JLabel error;

	public ServerPanel(JFrame jf, JScrollPane jsp, TButton... btn) {
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
		text = "Tworzenie nowej gry jako host";
		DivLabel title = new DivLabel(text, fontBig);
		gbc.gridy = 0;
		add(title, gbc);

		// grid 1
		text = "<p align='justify'>Wpisz poni¿ej port którego chcesz u¿yæ. Pamiêtaj, ¿e jako host w wiêkszoœci wypadków musisz przekierowaæ na routerze ten port na twój komputer, oraz utworzyæ regu³ê w Twoim firewallu.</p>";
		DivLabel info = new DivLabel(text, fontSmall);
		gbc.gridy = 1;
		add(info, gbc);

		// grid 2
		JLabel port = new JLabel("<html><b>Port:</b></html>");
		port.setFont(fontSmall);
		gbc.insets = new Insets(2, 2, 0, 0);
		gbc.gridy = 2;
		add(port, gbc);

		// grid 3
		JTextField inputPort = new JTextField(5);
		inputPort.setDocument(new JTextFieldLimit(5));
		inputPort.setHorizontalAlignment(JTextField.CENTER);
		inputPort.setFont(new Font("Arial", Font.BOLD, 16));
		inputPort.setMargin(new Insets(2, 2, 2, 2));
		inputPort.setText("25255");
		gbc.gridy = 3;
		add(inputPort, gbc);

		// grid 4
		error = new JLabel(" ");
		error.setForeground(Color.RED);
		gbc.gridy = 4;
		add(error, gbc);

		// grid 5
		JPanel buttons = new JPanel();
		buttons.setOpaque(false);
		TButton go = new TButton("Utwórz grê", "green", 14);
		go.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					String getport = inputPort.getText();
					if (isProperPort(getport)) {
						try {
							new Game(Integer.parseInt(getport),jf,jsp);
							//jf.dispose();
						} catch (NumberFormatException | IOException e) {
							error.setText("Port jest zajêty przez inn¹ aplikacjê.");
						}
					}
				}
			}
		});
		TButton close = new TButton("Zamknij", "yellow", 14);
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
		buttons.add(go);
		buttons.add(close);
		gbc.insets = new Insets(2, 2, 6, 2);
		gbc.gridy = 5;
		add(buttons, gbc);
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
