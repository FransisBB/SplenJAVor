package game.obj;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import game.main.Game;
import game.main.Utils;

public class ChairBCKP extends JPanel {

	private static final long serialVersionUID = 9165184660889321115L;

	public boolean taken = false;
	public String nick;
	public int gender = 1, color = 1, id;
	Game game;
	// int id;

	Color bgColor, txtColor;
	Font f;
	String text;

	public ChairBCKP(int x, Game game) {
		this.game = game;
		this.id = x;
		change();
		setOpaque(false);
	}

	public void change() {
		boolean active;

		if (game.localID == 999) {
			active = !taken;
		} else
			active = false;

		text = "";
		if (active) {
			text = "Kliknij aby zająć miejsce";
		} else {
			if (taken) {
				if (nick == null || nick.isEmpty()) {
					text = "Zajęte! Czekam na nick...";
				} else
					text = nick;
			} else if (!taken) {
				if (game.localID != id) {
					text = "Oczekiwanie na gracza";
				} else
					text = "Czekam na nick...";
			}
		}
		this.removeAll();
		f = new Font("Arial", Font.PLAIN, 16);
		txtColor = Color.black;
		if (active) {
			bgColor = new Color(150, 255, 150);
		} else if (taken && nick != null && !nick.isEmpty()) {
			f = new Font("Arial", Font.BOLD, 26);
			switch (color) {
			case 1:

				bgColor = new Color(252, 212, 100);
				txtColor = new Color(255, 255, 0, 175);
				break;
			case 2:
				txtColor = Color.red;
				bgColor = new Color(255, 200, 255);
				break;
			default:
				bgColor = new Color(150, 255, 150);
				break;
			}
		} else if (game.localID == id) {
			bgColor = new Color(255, 255, 150);
		} else {
			bgColor = new Color(255, 200, 200);
		}
		JLabel chair = new JLabel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			// @Override
			public void paintComponent(Graphics g) {
				FontMetrics metrics = g.getFontMetrics(f);
				super.paintComponent(g);
				Graphics2D g2;
				g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2.setColor(bgColor.brighter());
				g2.fillRoundRect(0, 0, 248, 58, 10, 10);
				g2.setColor(bgColor.darker());
				g2.fillRoundRect(2, 2, 248, 58, 10, 10);
				g2.setColor(bgColor);
				g2.fillRoundRect(1, 1, 248, 58, 10, 10);
				int fx = 125 - metrics.stringWidth(text) / 2;
				int fy = 30 - metrics.getHeight() / 2 + metrics.getAscent();
				g2.setFont(f);

				if (taken && nick != null && !nick.isEmpty()) {
					g2.setColor(new Color(0, 0, 0, 125));
					g2.drawString(text, fx + 1, fy + 1);
					g2.drawString(text, fx + 0, fy + 1);
					g2.drawString(text, fx + 1, fy + 0);
					g2.setColor(new Color(255, 255, 255, 125));
					g2.drawString(text, fx - 1, fy - 1);
					g2.drawString(text, fx - 0, fy - 1);
					g2.drawString(text, fx - 1, fy - 0);
					g2.setColor(new Color(255, 255, 255, 150));
					g2.fillOval(2, 2, 22, 22);
				}
				g2.setColor(txtColor);
				g2.drawString(text, fx, fy);
			}
		};
		chair.setPreferredSize(new Dimension(250, 60));
		chair.setOpaque(false);
		chair.setHorizontalAlignment(SwingConstants.CENTER);
		/*if (!active) {
			chair.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					sitDown();
				}
			});
		}*/
		if (active) {
			chair.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent me) {
					bgColor = new Color(200, 255, 200);
					chair.repaint();
				}

				public void mouseExited(MouseEvent me) {
					bgColor = new Color(150, 255, 150);
					chair.repaint();
				}

				public void mousePressed(MouseEvent me) {
					sitDown();
				}
			});
			add(chair);
		} else if (game.localID == id) {
			JLayeredPane pPanel;
			pPanel = new JLayeredPane();
			pPanel.setOpaque(false);
			pPanel.setLayout(null);
			pPanel.setPreferredSize(new Dimension(250, 60));
			chair.setBounds(0, 0, 250, 60);
			pPanel.add(chair, new Integer(1));

			JLabel del = new JLabel(getIcon("images/remove.png"));
			del.setBounds(231, 3, 15, 15);
			del.setOpaque(false);
			del.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent me) {
					del.setIcon(getIcon("images/removeOver.png"));
				}

				public void mouseExited(MouseEvent me) {
					del.setIcon(getIcon("images/remove.png"));
				}

				public void mousePressed(MouseEvent me) {
					standUp();
				}
			});
			pPanel.add(del, new Integer(2));

			JLabel edit = new JLabel(getIcon("images/edit.png"));
			edit.setBounds(214, 3, 15, 15);
			edit.setOpaque(false);
			edit.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent me) {
					edit.setIcon(getIcon("images/editOver.png"));
				}

				public void mouseExited(MouseEvent me) {
					edit.setIcon(getIcon("images/edit.png"));
				}

				public void mousePressed(MouseEvent me) {
					editNick();
				}
			});
			pPanel.add(edit, new Integer(2));

			// end of del/edit
			add(pPanel);
		} else {

			if (game.isServer && taken) {
				JLabel del = new JLabel(getIcon("images/remove.png"));
				del.setBounds(231, 3, 15, 15);
				del.setOpaque(false);
				del.addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent me) {
						del.setIcon(getIcon("images/removeOver.png"));
					}

					public void mouseExited(MouseEvent me) {
						del.setIcon(getIcon("images/remove.png"));
					}

					public void mousePressed(MouseEvent me) {
						free();
						//game.sendAction("LOBLVE" + id);
						game.server.resetSeat(id);
					}
				});
				chair.add(del, new Integer(2));

			}

			add(chair);

		}
		if (taken && nick != null && !nick.isEmpty()) {
			JLabel gIcon = new JLabel(Utils.scaleIcon(getIcon("images/gender" + gender + ".png"), 20, 20));
			gIcon.setBounds(3, 3, 20, 20);
			chair.add(gIcon);
		}
		// }
		this.repaint();
		this.revalidate();
	}

	public void free() {

		gender = 1;
		taken = false;
		nick = "";
		game.setChairs();

	}

	public void set(String nick, int g) {
		taken = true;
		this.nick = nick;
		gender = g;
		game.setChairs();
	}

	public void editNick() {
		SitPanel sitPanel = new SitPanel();
		Object[] options = { "Zmień", "Anuluj" };
		int n = JOptionPane.showOptionDialog(this, sitPanel, "Edytuj nick", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (n == 0 && game.localID == id) {
			String nick = sitPanel.getNick();
			if (nick != null && !nick.isEmpty()) {
				set(nick, gender);
				game.sendAction("LOBNCK" + id + gender + nick);
				game.sendAction(game.chatPanel.editNick(nick));
			}
		}
	}

	public void sitDown() {
		game.localID = id;
		game.setChairs();
		game.sendAction("LOBRES" + id);
		//if (game.isServer)
		//	taken=true;

	}

	public void sitDownAccepted() {
		SitPanel sitPanel = new SitPanel();
		Object[] options = { "Usiadz", "Anuluj" };
		int n = JOptionPane.showOptionDialog(this, sitPanel, "Zajmij miejsce", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		//if (n == 0 && game.localID == id) {
		if (n == 0) {
			String nick = sitPanel.getNick();
			if (nick != null && !nick.isEmpty()) {
					set(nick, gender);
					game.sendAction("LOBNCK" + id + gender + nick);
					game.sendAction(game.chatPanel.editNick(nick));
			} else
				standUp();
		} else
			standUp();
	}
	
	public void standUp() {
		game.sendAction("LOBLVE" + id);

		game.localID = 999;
		if (taken) {
			game.sendAction(game.chatPanel.editNick("Guest" + game.rand));
		}
		free();
	}

	public JLabel genderIcon(int g) {
		JLabel gl = new JLabel(getIcon("images/gender" + g + ".png"));
		gl.setPreferredSize(new Dimension(50, 50));
		gl.setOpaque(true);
		return gl;
	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	class SitPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2633538609067144658L;
		JTextField inputNick = new JTextField(13);

		SitPanel() {
			setOpaque(false);
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			inputNick.setHorizontalAlignment(JTextField.CENTER);
			inputNick.setFont(new Font("Arial", Font.BOLD, 16));
			inputNick.setMargin(new Insets(2, 2, 2, 2));
			inputNick.setText(nick);
			gbc.gridwidth = 2;
			gbc.gridy = 0;
			add(new JLabel("Podaj nick i płeć:"), gbc);
			gbc.gridy = 1;
			add(inputNick, gbc);
			gbc.insets = new Insets(2, 2, 2, 2);
			JLabel genderLab1 = genderIcon(1);
			JLabel genderLab2 = genderIcon(2);
			if (gender == 1) {
				genderLab1.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
				genderLab1.setBackground(new Color(200, 200, 255));
				genderLab2.setBackground(new Color(255, 200, 255, 50));
				genderLab2.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0, 75), 1));
			} else {
				genderLab1.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 255, 75), 1));
				genderLab1.setBackground(new Color(200, 200, 255, 50));
				genderLab2.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
				genderLab2.setBackground(new Color(255, 200, 255));
			}

			genderLab1.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					genderLab2.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0, 75), 1));
					genderLab2.setBackground(new Color(255, 200, 255, 50));
					genderLab1.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
					genderLab1.setBackground(new Color(200, 200, 255));
					gender = 1;
					repaint();
					revalidate();
				}
			});
			genderLab2.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					genderLab1.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 255, 75), 1));
					genderLab1.setBackground(new Color(200, 200, 255, 50));
					genderLab2.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
					genderLab2.setBackground(new Color(255, 200, 255));
					gender = 2;
					repaint();
					revalidate();
				}
			});
			gbc.gridy = 2;
			JPanel temp = new JPanel();
			temp.add(genderLab1);
			temp.add(genderLab2);
			add(temp, gbc);
			/*
			 * gbc.gridy = 3; gbc.gridwidth=1; TButton a= new TButton("TEst1","green",14);
			 * TButton b= new TButton("TEst2","green",14); gbc.gridx=0; add(a,gbc);
			 * gbc.gridx=1; add(b,gbc);
			 */
			Timer timer = new Timer(100, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					inputNick.requestFocus();
				}

			});
			timer.setRepeats(false);
			timer.start();
		}

		public String getNick() {
			return inputNick.getText();
		}
	}
}
