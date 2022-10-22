/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package game.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import game.panels.ServerPanel;
import game.panels.ClientPanel;
import game.panels.EndGamePanel;
import ui.Logo;
import ui.MPanel;
import ui.TButton;

public class MainMenu {
	public static void main(String[] args) {
		Locale.setDefault(new Locale("pl", "PL"));
		ResourceBundle txt = ResourceBundle.getBundle("Labels");

		JFrame mainFrame = new JFrame("SplenJAVor");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setPreferredSize(new Dimension(1290, 680));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				Object[] options = { lt(txt, "exit"), lt(txt, "cancel") };
				int n = JOptionPane.showOptionDialog(mainFrame, lt(txt, "exit_confirm"), lt(txt, "leaving_game"),
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (n == JOptionPane.YES_OPTION) {
					mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} else {
					mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setLocation(dim.width / 2 - 645, 0);
		JPanel mainPanel = new BackgroundPane();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		Font font2 = new Font("Verdana", Font.BOLD, 12);
		JLabel bot = new JLabel("Copyright © 2020 Fransis (fransis1989@gmail.com) " + lt(txt, "ver"));
		bot.setHorizontalAlignment(SwingConstants.CENTER);
		bot.setFont(font2);

		gbc.insets = new Insets(5, 0, 5, 0);
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.weighty = 1;

		Logo top = new Logo(150);
		top.setPreferredSize(new Dimension(1000, 170));
		gbc.gridx = 0;
		gbc.gridy = 0;
		// gbc.gridwidth = 1;

		mainPanel.add(top, gbc);
		// gbc.weighty = 1;
		// gbc.gridwidth = 1;
		MPanel about_handler = new MPanel("Informacje");
		about_handler.setLayout(null);
		about_handler.setPreferredSize(new Dimension(800, 330));
		String text = "<p align='justify'>Gra jest adaptacj¹ znanej gry karcianej o nazwie Splendor. "
				+ "Powsta³a jako projekt szkoleniowy podczas nauki jêzyka JAVA. "
				+ "Jest ca³kowicie darmowa i mo¿na j¹ kopiowaæ bez ograniczeñ. "
				+ "Autor nie czerpie z niej ¿adnych korzyœci maj¹tkowych i za wszystkie b³êdy i niedogodnoœci z góry przeprasza;) "
				+ "Wszelkie uwagi i opinie mile widziane na mail podany w stopce.<br><br>"
				+ "<font color='green'><b>NOWA GRA:</b></font> - Utwórz now¹ grê. Podaj numer portu lub pozostaw domyœlny, a nastêpnie podaj "
				+ "go innym graczom którzy bêd¹ chcieli siê z Tob¹ po³¹czyæ, wraz z lokalnym lub publicznym adresem IP "
				+ "(wyœwietl¹ Ci siê po utworzeniu gry). "
				+ "Podaj lokalny adres (lub nazwê komputera w sieci) jeœli gracze s¹ z Tob¹ w jednej sieci, lub publiczny "
				+ "jeœli bêd¹ ³¹czyæ siê przez "
				+ "internet (pamiêtaj te¿ wtedy aby w ustawieniach routera przekierowaæ odpowiedni port do Twojego komputera).<br><br>"
				+ "<font color='#999900'><b>DO£¥CZ DO GRY:</b></font> - Wpisz adres IP lub nazwê hosta, a nastêpnie podaj port. Dane te "
				+ "uzyskasz od osoby która utworzy³a grê.<br><br>"
				+ "<i>Wszystkie grafiki pochodz¹ z darmowych stocków, znalezione poprzez Google.</i></p>";
		JLabel about = new JLabel("<html>" + text + "</html>");
		about.setVerticalAlignment(SwingConstants.TOP);

		about.setBounds(10, 20, 780, 300);
		about.setFont(new Font("Arial", Font.PLAIN, 16));
		about_handler.add(about);
		gbc.gridy = 1;
		mainPanel.add(about_handler, gbc);

		JScrollPane scrollPane = new JScrollPane(mainPanel);

		TButton sServer = new TButton(lt(txt, "new_game"), "green", 24);
		TButton sClient = new TButton(lt(txt, "join_game"), "yellow", 24);
		TButton sExit = new TButton(lt(txt, "exit"), "red", 24);
		sExit.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
				}
			}
		});

		sServer.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					ServerPanel egp = new ServerPanel(mainFrame, scrollPane, sServer, sClient);
					egp.showThis();
				}
			}
		});
		sClient.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					ClientPanel cp = new ClientPanel(mainFrame, scrollPane, sServer, sClient);
					cp.showThis();

				}
			}
		});
		JPanel buttons = new JPanel();
		buttons.setOpaque(false);
		buttons.add(sServer);
		buttons.add(sClient);
		buttons.add(sExit);
		gbc.gridy = 2;
		mainPanel.add(buttons, gbc);

		gbc.gridy = 3;
		mainPanel.add(bot, gbc);

		mainFrame.add(scrollPane);
		mainFrame.pack();
		mainFrame.setVisible(true);

	}

	public static boolean isProperPort(String strNum) {
		if (strNum == null || strNum.isEmpty()) {
			return false;
		}
		try {
			double d = Integer.parseInt(strNum);
			if (d < 10000 || d > 65353)
				return false;
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static String lt(ResourceBundle txt, String s) {
		try {
			return txt.getString(s);
		} catch (Exception e) {
			return s;
		}
	}

}
