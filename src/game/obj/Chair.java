package game.obj;

import java.awt.Color;
import java.awt.Component;
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
import game.panels.SitPanel;
import ui.STRButton;
import ui.TButton;

public class Chair extends JPanel {

	private static final long serialVersionUID = 9165184660889321115L;

	public boolean taken = false, iconsVisible = true;
	public String nick;
	public int gender = 1, color = 1, id;
	Game game;
	public SitPanel sp;
	JLabel del, edit;
	// int id;
	Color bgColor = Color.BLACK, txtColor = Color.BLACK;
	Font f = new Font("Arial", Font.PLAIN, 16);
	String text = "";
	ChairLabel chairLabel;
	JLayeredPane pPanel;

	public Chair(int x, Game game) {
		this.game = game;
		this.id = x;
		setOpaque(false);
		chairLabel = new ChairLabel();
		pPanel = new JLayeredPane();
		pPanel.setOpaque(false);
		pPanel.setLayout(null);
		pPanel.setPreferredSize(new Dimension(250, 60));
		pPanel.add(chairLabel, new Integer(1));
		add(pPanel);
		change();
	}

	public void change() {

		boolean active;
		String tempText = text;
		if (game.localID == 999) {
			active = !taken;
		} else
			active = false;

		f = new Font("Arial", Font.PLAIN, 16);
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
		
			txtColor = Color.black;
			if (active) {
				bgColor = new Color(150, 255, 150);
			} else if (taken && nick != null && !nick.isEmpty()) {
				f = new Font("Arial", Font.BOLD, 26);
				switch (color) {
				case 1:
					bgColor = new Color(255, 245, 140);
					//bgColor = new Color(180, 225, 255);
					txtColor = new Color(255, 100, 0,200);
					break;
				case 2:
					bgColor = new Color(255, 205, 225);
					txtColor = new Color(225, 70, 225,200);
					break;
				case 3:
					//bgColor = new Color(255, 245, 140);
					bgColor = new Color(180, 225, 255);
					txtColor = new Color(0, 100, 255,200);
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
			if (tempText != text) {
			// czyszczenie
			chairLabel.repaint();
			for (Component c : pPanel.getComponents()) {
				if (!(c instanceof ChairLabel)) {
					pPanel.remove(c);
				}
			}
			while (pPanel.getMouseListeners().length != 0) {
				pPanel.removeMouseListener(pPanel.getMouseListeners()[0]);
			}

			// koniec czyszczenia

			if (active) {
				pPanel.addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent me) {
						bgColor = new Color(200, 255, 200);
						chairLabel.repaint();
					}

					public void mouseExited(MouseEvent me) {
						bgColor = new Color(150, 255, 150);
						chairLabel.repaint();
					}

					public void mousePressed(MouseEvent me) {
						sitDown();
						hideIcons();
					}
				});
			} else if (game.localID == id) {
				del = new JLabel(getIcon("images/remove.png"));
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

				edit = new JLabel(getIcon("images/edit.png"));
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

			} else {
				if ((game.isServer || game.isOwner) && taken) {
					del = new JLabel(getIcon("images/remove.png"));
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
							if(game.isServer) {
							free();
							game.server.resetSeat(id);
							} else if (game.isOwner) {
								
							}
						}
					});
					pPanel.add(del, new Integer(2));

				}
			}
			if (taken && nick != null && !nick.isEmpty()) {
				JLabel gIcon = new JLabel(Utils.scaleIcon(getIcon("images/gender" + gender + ".png"), 20, 20));
				gIcon.setBounds(3, 3, 20, 20);
				pPanel.add(gIcon, new Integer(2));
			}
		}
	}

	public void free() {
		if (sp != null && sp.isVisible()) {
			sp.clear();
		}
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
		sp = new SitPanel(game.scrollPane, this, true);
		sp.showThis();
	}

	public void editNickFinish(String nick, int gender) {
		if (game.localID == id && nick != null && !nick.isEmpty()) {
			set(nick, gender);
			game.sendAction("LOBNCK" + id + gender + nick);
			game.sendAction(game.chatPanel.editNick(nick));
		}
	}

	public void sitDown() {
		game.localID = id;
		game.setChairs();
		game.sendAction("LOBRES" + id);
	}

	public void sitDownAccepted() {
		sp = new SitPanel(game.scrollPane, this, false);
		sp.showThis();
	}

	public void sitDownAcceptedFinish(String nick, int gender) {
		if (nick != null && !nick.isEmpty()) {
			set(nick, gender);
			game.sendAction("LOBNCK" + id + gender + nick);
			game.sendAction(game.chatPanel.editNick(nick));
		} else {
			standUp();
		}
	}

	public void standUp() {
		game.sendAction("LOBLVE" + id);
		game.localID = 999;
		if (taken) {
			game.sendAction(game.chatPanel.editNick("Guest" + game.rand));
		}
		free();
	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	public void showIcons() {
		del.setVisible(true);
		edit.setVisible(true);
	}

	public void hideIcons() {
		del.setVisible(false);
		edit.setVisible(false);
	}

	private class ChairLabel extends JLabel {
		private static final long serialVersionUID = 1L;

		ChairLabel() {
			setPreferredSize(new Dimension(250, 60));
			setOpaque(false);
			setHorizontalAlignment(SwingConstants.CENTER);
			setBounds(0, 0, 250, 60);
		}

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
				g2.setColor(new Color(0, 0, 0, 25));
				g2.drawString(text, fx + 1, fy + 1);
				g2.drawString(text, fx + 0, fy + 1);
				g2.drawString(text, fx + 1, fy + 0);
				g2.setColor(new Color(255, 255, 255, 25));
				g2.drawString(text, fx - 1, fy - 1);
				g2.drawString(text, fx - 0, fy - 1);
				g2.drawString(text, fx - 1, fy - 0);
				g2.setColor(new Color(255, 255, 255, 50));
				g2.fillOval(2, 2, 22, 22);
			}
			g2.setColor(txtColor);
			g2.drawString(text, fx, fy);
		}
	}
}
