package game.box;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import game.main.Board;
import game.main.Utils;
import game.obj.Aristocrat;
import game.obj.Card;
import game.obj.Player;
import ui.STRPanel;
import ui.SmallCoinStack;

public class PlayersPanel extends JPanel {
	private static final long serialVersionUID = 8793901114737663002L;

	Player[] player;
	Graphics2D g2;
	int activeP = 0;
	boolean isLastRound = false;
	List<Aristocrat> _arilist;
	JLabel[] history;
	JPanel[] historyPanel, rewardsPanel;
	JPopupMenu pop;
	boolean cities, aris, strongholds;
	Color bgColor = new Color(255, 255, 245);
	Color bgColor2 = new Color(235, 235, 225);
	Color borderColor = new Color(50, 50, 0, 255);
	String ver;

	public PlayersPanel(Board board) {
		this.ver=board.lt("ver");
		this.pop = new JPopupMenu();
		this.pop.setBorder(BorderFactory.createEmptyBorder());
		this.pop.setOpaque(false);
		this.pop.setBorderPainted(false);
		this.pop.setVisible(false);

		this.cities = board.optCities;
		this.aris = board.optAris;
		this.player = board.players;
		this.strongholds = board.optStrongholds;
		if (aris) {
			_arilist = board.aristocratsDealt._arilist;
		}
		history = new JLabel[player.length];
		historyPanel = new JPanel[player.length];
		rewardsPanel = new JPanel[player.length];
		for (int i = 0; i < player.length; i++) {
			historyPanel[i] = new JPanel();
			historyPanel[i].setLayout(new GridBagLayout());
			// historyPanel[i].setOpaque(false);
			historyPanel[i].setBorder(BorderFactory.createLineBorder(borderColor));

			JLabel def = new JLabel("Gracz nie wykona³ jeszcze ¿adnego ruchu.");
			def.setHorizontalAlignment(SwingConstants.CENTER);
			def.setVerticalAlignment(SwingConstants.CENTER);
			Dimension dim = new Dimension(def.getPreferredSize().width + 8, def.getPreferredSize().height + 8);
			historyPanel[i].setBackground(bgColor);
			historyPanel[i].setPreferredSize(dim);
			historyPanel[i].add(def);

			rewardsPanel[i] = new JPanel();
			rewardsPanel[i].setLayout(new GridBagLayout());
			rewardsPanel[i].setOpaque(false);
			rewardsPanel[i].setBorder(BorderFactory.createLineBorder(borderColor));

			int j = i;

			history[i] = new JLabel(getIcon("images/historyOff.png"));
			history[i].setOpaque(false);
			history[i].addMouseListener(new MouseAdapter() {

				public void mouseExited(MouseEvent me) {
					history[j].setIcon(getIcon("images/historyOff.png"));
					pop.removeAll();
					pop.setVisible(false);
				}

				public void mouseEntered(MouseEvent me) {
					history[j].setIcon(getIcon("images/historyOn.png"));
					if (!pop.isVisible())
						popupHistory(j);
				}

				public void mousePressed(MouseEvent me) {
					pop.setVisible(true);
				}
			});
			history[i].addMouseWheelListener(new MouseWheelListener() {
				public void mouseWheelMoved(MouseWheelEvent e) {
					pop.setVisible(true);
				}
			});

		}

		setLayout(null);
		setPreferredSize(new Dimension(299, player.length==5?606:473));
		setOpaque(false);
	}

	public void setActive(int n) {
		activeP = n;
		repaint();
		revalidate();
	}

	public void setLastRound() {
		isLastRound = true;
	}

	// @Override
	public void paintComponent(Graphics g) {

		removeAll();
		super.paintComponent(g);
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// int opaque = 150;
		int y = (this.getHeight() - (player.length==5?606:473)) / 2;
		int b = 0;
		for (Player p : player) {
			// t³o
			int x = (this.getWidth() - 295) / 2;
			if (b == activeP) {
				g2.setColor(new Color(0, 255, 0, 50));
			} else
				g2.setColor(new Color(0, 0, 0, 30));
			g2.fillRoundRect(x, y, 295, 113, 15, 15);

			if (b == activeP) {
				g2.setColor(new Color(240, 255, 240, 180));
			} else
				g2.setColor(new Color(255, 255, 240, 180));
			g2.fillRoundRect(x - 1, y - 1, 295, 113, 15, 15);
			if (p.isOnline) {
				g2.setColor(new Color(0, 255, 0));
			} else
				g2.setColor(new Color(255, 0, 0));
			g2.fillOval(x + 3, y + 3, 8, 8);
			// Nick
			Image gIcon = Utils.scaleIcon(getIcon("images/gender" + p.gender + ".png"), 22, 22).getImage();
			// x += 18;
			g2.setColor(new Color(0, 0, 0, 30));
			g2.fillRoundRect(x, y, 293, 27, 15, 15);
			if (b == activeP) {
				g2.setColor(new Color(180, 255, 180, 180));
			} else {
				g2.setColor(new Color(245, 245, 160, 180));
			}

			g2.fillRoundRect(x - 1, y - 1, 294, 27, 15, 15);
			// g2.setColor(new Color(0,0,0,25));
			// g2.drawRoundRect(x,y,310,25,10,10);
			x += 10;
			g2.drawImage(gIcon, x, y + 1, this);
			g2.setColor(Color.BLACK);

			if (b == activeP) {
				g2.setColor(new Color(0, 100, 0));
			} else {
				g2.setColor(new Color(50, 50, 0));
			}
			g2.setFont(new Font("Arial", Font.PLAIN, 20));
			x += 22;
			g2.drawString(p.playerName, x, y + 20);

			// posiadane karty i ¿etony
			x -= 27;
			JPanel cardsGhost = new JPanel();
			cardsGhost.setOpaque(false);
			cardsGhost.setBounds(x, y + 38, 215, 60);
			if (isLastRound)
				cardsGhost.setBounds(x, y + 30, 215, 60);
			int c = b;
			cardsGhost.addMouseListener(new MouseAdapter() {
				public void mouseExited(MouseEvent me) {
					pop.removeAll();
					pop.setVisible(false);
				}

				public void mouseEntered(MouseEvent me) {
					if (!pop.isVisible())
						popupCards(c);
				}

				public void mousePressed(MouseEvent me) {
					pop.setVisible(true);
				}
			});
			cardsGhost.addMouseWheelListener(new MouseWheelListener() {
				public void mouseWheelMoved(MouseWheelEvent e) {
					pop.setVisible(true);
				}
			});
			add(cardsGhost);
			for (int i = 0; i < 5; i++) {

				JLabel cardAndCoin = new JLabel();
				cardAndCoin = Utils.cardAndCoinDraw(i, p.production[i], p.ownedCoins[i], 1);
				cardAndCoin.setBounds(x, y + 38, 40, 58);
				if (isLastRound)
					cardAndCoin.setBounds(x, y + 30, 40, 58);
				add(cardAndCoin);
				x += 37;
			}
			JLabel yellowCoin = new JLabel();
			yellowCoin = Utils.coinDraw(5, p.ownedCoins[5], 1,false);
			if (strongholds) {
				JLabel str = new JLabel(getIcon("images/tower" + b + ".png"));
				JLabel strNum = new JLabel(Integer.toString(p.str));
				strNum.setForeground(Color.WHITE);
				strNum.setFont(new Font("Arial", Font.BOLD, 22));
				if (isLastRound) {
					str.setBounds(x + 1, y + 29, 26, 30);
					strNum.setBounds(x + 8, y + 30, 26, 30);
				} else {
					str.setBounds(x + 1, y + 34, 26, 30);
					strNum.setBounds(x + 8, y + 35, 26, 30);
				}
				add(str, 0);
				add(strNum, 0);

			}
			if (strongholds) {
				yellowCoin.setBounds(x - 1, y + 65, 31, 31);
			} else
				yellowCoin.setBounds(x - 1, y + 50, 31, 31);
			if (isLastRound) {
				if (strongholds) {
					yellowCoin.setBounds(x - 1, y + 60, 31, 31);
				} else
					yellowCoin.setBounds(x - 1, y + 45, 31, 31);
			}
			add(yellowCoin);
			x += 35;
			int yp = y + 29;

			for (int i = 0; i < 3; i++) {
				g2.setColor(new Color(255, 255, 0));
				if (i >= p.reservedCards)
					g2.setColor(new Color(0, 0, 0, 50));
				g2.fillRoundRect(x, yp, 20, 25, 10, 10);
				if (i < p.reservedCards) {
					g2.setColor(new Color(0, 0, 0, 255));
					if (p.resCard[i].level == 0) {
						g2.fillOval(x + 7, yp + 8, 6, 6);
					} else if (p.resCard[i].level == 1) {
						g2.fillOval(x + 3, yp + 8, 6, 6);
						g2.fillOval(x + 11, yp + 8, 6, 6);
					} else if (p.resCard[i].level == 2) {
						g2.fillOval(x + 3, yp + 6, 6, 6);
						g2.fillOval(x + 11, yp + 6, 6, 6);
						g2.fillOval(x + 7, yp + 14, 6, 6);
					}
				}
				yp += 27;
			}
			// punkty
			x += 25;
			int py = y;
			if (cities) {
				if (p.haveCity) {
					g2.drawImage(getIcon("images/citySmall.png").getImage(), x, y + 85, this);
				} else
					g2.drawImage(getIcon("images/noCitySmall.png").getImage(), x, y + 85, this);
				py = y - 10;
			}

			g2.drawImage(getIcon("images/award.png").getImage(), x, py + 38, this);
			int px = x + 14;
			if (p.points > 9)
				px -= 6;

			// g2.setFont(new Font("Arial", Font.PLAIN, 20));
			g2.setColor(new Color(255, 255, 255, 150));
			g2.drawString(Integer.toString(p.points), px - 1, py + 64);
			g2.setColor(new Color(0, 0, 0, 150));
			g2.drawString(Integer.toString(p.points), px + 1, py + 66);
			g2.setColor(new Color(180, 180, 20));
			g2.drawString(Integer.toString(p.points), px, py + 65);

			// ostatnia akcja
			x += 18;
			// System.out.println("X: " + x);
			if (p.isLocal) {
				history[b].setBounds(x, y + 2, 0, 0);
			} else {
				history[b].setBounds(x, y + 2, 24, 21);

			}
			add(history[b]);

			x -= 265;
			g2.setFont(new Font("Arial", Font.BOLD, 16));
			if (b < activeP && isLastRound) {

				if ((p.points >= 15 && !cities) || p.haveCity) {
					g2.setColor(new Color(0, 150, 0, 255));
				} else
					g2.setColor(new Color(150, 0, 0, 255));
				g2.drawString("Wynik koñcowy: " + Integer.toString(p.points) + " punktów", x, y + 105);
			} else if (isLastRound) {
				g2.setColor(new Color(255, 0, 0, 255));
				g2.drawString("Ostatni ruch!", x, y + 105);
			}
			y += player.length==5?115:118;
			b++;
		}
		for (int n = b; n < 4; n++) {
			int x = (this.getWidth() - 295) / 2;

			g2.setColor(new Color(0, 0, 0, 10));
			g2.fillRoundRect(x, y, 295, 113, 15, 15);
			y += 118;
		}
		if (b==5) {
			//int x = (this.getWidth() - 295) / 2;
			InfoPanelSmall ips = new InfoPanelSmall();
			ips.setBounds(0,y,getWidth(), getHeight()-y);
			ips.setSize(getWidth(), getHeight()-y);
			ips.draw(ver);
			add(ips);
			//g2.setColor(new Color(0, 0, 0, 10));
			//g2.fillRoundRect(x, y, 295, this.getHeight()-y-3, 15, 15);
		}
	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	public void popupHistory(int n) {
		pop.removeAll();
		int histX = targetX(history[n]);
		int histY = targetY(history[n]);
		int targetX = histX + 28;
		int targetY = histY - 4;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int screenHeight = dim.height - 45; // except bar
		int screenWidth = dim.width - 5;
		boolean tooLow = false;

		if (targetY + historyPanel[n].getHeight() > screenHeight) {
			targetY = screenHeight - historyPanel[n].getHeight();
			tooLow = true;
		}
		if (targetY + historyPanel[n].getHeight() + 115 > screenHeight) {
			tooLow = true;
		}
		if (targetX + historyPanel[n].getWidth() > screenWidth) {
			targetX = histX - 272;
			if (targetX + historyPanel[n].getWidth() > screenWidth) {
				targetX = screenWidth - historyPanel[n].getWidth();
			}
			if (tooLow) {
				targetY = histY - historyPanel[n].getHeight() - 4;
			} else {
				targetY += 115;
			}
		}
		if (targetX < 5)
			targetX = 5;

		int x = 0 - histX + targetX;
		int y = 0 - histY + targetY;
		if (historyPanel[n].getWidth() > (screenWidth - 5)) {
			JScrollPane scrollPane = new JScrollPane(historyPanel[n], JScrollPane.VERTICAL_SCROLLBAR_NEVER,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setPreferredSize(new Dimension(screenWidth - 5, historyPanel[n].getHeight()));
			scrollPane.setOpaque(false);
			historyPanel[n].setBorder(null);
			scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
			pop.add(scrollPane);
			Timer scrollAnim = new Timer(20, new ActionListener() {
				int tick = 0;

				public void actionPerformed(ActionEvent evt) {
					if (pop.isVisible()) {
						int actPos = scrollPane.getViewport().getViewPosition().x;
						if (actPos < (historyPanel[n].getWidth() - scrollPane.getWidth()) && tick <= 50) {
							if (tick == 50) {
								scrollPane.getViewport().setViewPosition(new Point(actPos + 1, 0));
							} else
								tick++;
						} else {
							if (tick == 100) {
								if (actPos > 0) {
									scrollPane.getViewport().setViewPosition(new Point(actPos - 1, 0));
								} else {
									tick = 0;
								}
							} else
								tick++;
						}
					} else {
						scrollPane.getViewport().setViewPosition(new Point(0, 0));
						Timer timer = (Timer) evt.getSource();
						timer.stop();
					}
				}
			});
			scrollAnim.setRepeats(true);
			scrollAnim.start();
		} else {
			historyPanel[n].setBorder(BorderFactory.createLineBorder(borderColor));
			pop.add(historyPanel[n]);

		}
		pop.show(history[n], x, y);
	}

	public void popupCards(int b) {
		pop.removeAll();
		JPanel panel = showCards(b);
		JPanel showPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(0, 0, 0, 0);
		showPanel.setOpaque(false);
		if (panel.getComponentCount() == 0 && player[b].coinsSum() == 0) {
			JLabel label;

			if (player[b].isLocal) {
				label = new JLabel("Nie posiadasz jeszcze ¿adnych kart ani ¿etonów!");
			} else {
				label = new JLabel("Gracz nie posiada jeszcze ¿adnych kart ani ¿etonów.");
			}
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.CENTER);
			Dimension dim = new Dimension(label.getPreferredSize().width + 8, label.getPreferredSize().height + 8);
			panel.setBackground(bgColor);
			panel.setPreferredSize(dim);
			panel.add(label);
		}
		if (panel.getComponentCount() != 0) {
			showPanel.add(panel, gbc);
			gbc.insets = new Insets(0, -1, 0, 0);
		}

		if (rewardsPanel[b].getComponentCount() != 0) {
			showPanel.add(rewardsPanel[b], gbc);
		}

		if (player[b].coinsSum() != 0) {
			showPanel.add(showCoins(b), gbc);
		}
		int histX = targetX(history[b]);
		int histY = targetY(history[b]);
		int targetX = histX + 28;
		int targetY = histY - 4;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int screenHeight = dim.height - 45; // except bar
		int screenWidth = dim.width - 5;
		boolean tooLow = false;
		showPanel.setSize(showPanel.getPreferredSize());
		if (targetY + showPanel.getHeight() > screenHeight) {
			targetY = screenHeight - showPanel.getHeight();
			tooLow = true;
		}
		if (targetY + showPanel.getHeight() + 115 > screenHeight) {
			tooLow = true;
		}
		if (targetX + showPanel.getWidth() > screenWidth) {
			targetX = histX - 272;
			if (targetX + showPanel.getWidth() > screenWidth) {
				targetX = screenWidth - showPanel.getWidth();
			}
			if (tooLow) {
				targetY = histY - showPanel.getHeight() - 4;
			} else {
				targetY += 115;
			}
		}
		if (targetX < 5)
			targetX = 5;

		int x = 0 - histX + targetX;
		int y = 0 - histY + targetY;

		pop.add(showPanel);
		pop.show(history[b], x, y);

	}

	public void addReward(Component comp, int n) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridy = rewardsPanel[n].getComponentCount() % 2;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(comp);
		switch (rewardsPanel[n].getComponentCount() % 4) {
		case 0:
		case 3:
			panel.setBackground(bgColor);
			break;
		case 1:
		case 2:
			panel.setBackground(bgColor2);
			break;
		default:
			panel.setBackground(new Color(0, 0, 0));
			break;

		}
		rewardsPanel[n].add(panel, gbc);
		int cols = (rewardsPanel[n].getComponentCount() > 1) ? 2 : 1;
		int rows = (int) Math.ceil((double) rewardsPanel[n].getComponentCount() / 2);
		rewardsPanel[n].setSize(new Dimension(rows * 98, cols * 98));

	}

	public JPanel showCoins(int b) {
		JPanel coins = new JPanel();
		coins.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		coins.setBorder(BorderFactory.createLineBorder(borderColor));
		coins.setBackground(bgColor);
		SmallCoinStack smc = new SmallCoinStack(player[b].ownedCoins, true);
		coins.add(smc);
		return coins;
	}

	public JPanel showCards(int b) {
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		int width = 0, height = 0;
		for (int i = 0; i < 8; i++) {
			gbc.gridx = 0;
			gbc.gridy = 0;

			int n = 0;
			JPanel colorPanel = new JPanel();
			colorPanel.setLayout(new GridBagLayout());
			if (i < 6) {
				for (Card c : player[b]._cardList) {
					if (c.color == i) {
						gbc.insets = new Insets(5 + n * 30, 5, 5, 5);
						colorPanel.add(c, gbc, 0);
						height = height < 163 + n * 30 ? 163 + n * 30 : height;
						n++;
					}
				}
			} else if (i == 6) {
				for (int h = 0; h < player[b].reservedCards; h++) {
					gbc.insets = new Insets(5 + n * 30, 5, 5, 5);
					Card t = player[b].resCard[h];
					Card nCard = Utils.copyCard(t, false, false);
					nCard.showFront(false);
					colorPanel.add(nCard, gbc, 0);
					height = height < 163 + n * 30 ? 163 + n * 30 : height;
					n++;
				}
			} else if (i == 7 && aris) {
				for (int h = 0; h <= player.length; h++) {
					gbc.insets = new Insets(5 + n * 98, 5, 5, 5);
					Aristocrat t = _arilist.get(h);
					if (t.isAvailable && t.isReserved && t.resID == b) {
						Aristocrat nAri = new Aristocrat(t.imgPath, t.reward, t.req);
						height = height < 98 + n * 98 ? 98 + n * 98 : height;
						n++;
						nAri.showFront = false;
						colorPanel.add(nAri, gbc, 0);
					}

				}
			}
			if (n > 0) {
				if (i < 7) {
					width += 113;
				} else if (i == 7) {
					width += 98;
				}
				gbc.gridx = i;
				gbc.insets = new Insets(0, 0, 0, 0);
				switch (userPanel.getComponentCount() % 2) {
				case 0:
					colorPanel.setBackground(bgColor);
					break;
				case 1:
					colorPanel.setBackground(bgColor2);
					break;
				default:
					colorPanel.setBackground(new Color(0, 0, 0));
					break;

				}
				userPanel.add(colorPanel, gbc);
			}

		}
		userPanel.setBorder(BorderFactory.createLineBorder(borderColor));
		userPanel.setSize(width, height);
		return userPanel;

	}

	public int targetX(JLabel label) {
		return (int) label.getLocationOnScreen().getX();
	}

	public int targetY(JLabel label) {
		return (int) label.getLocationOnScreen().getY();
	}

	public void addContent(int n, Component panel) {

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;

		switch ((historyPanel[n].getComponentCount()) % 2) {
		case 0:
			panel.setBackground(bgColor);
			break;
		case 1:
			panel.setBackground(bgColor2);
			break;
		default:
			panel.setBackground(new Color(0, 0, 0));
			break;

		}

		historyPanel[n].add(panel, gbc);

		int h = 0;
		int w = 0;
		for (int i = 0; i < historyPanel[n].getComponentCount(); i++) {
			int compSize = historyPanel[n].getComponent(i).getPreferredSize().height;
			h = h > compSize ? h : compSize;
			w += historyPanel[n].getComponent(i).getPreferredSize().width;
		}
		Dimension dim = new Dimension(w, h);
		historyPanel[n].setPreferredSize(null);
		historyPanel[n].setSize(dim);
	}

	public void clearContent(int n) {
		clearTimers(historyPanel[n]);
		historyPanel[n].removeAll();
	}

	public void clearTimers(JComponent comp) {
		for (int i = 0; i < comp.getComponentCount(); i++) {
			System.out.println("COMP"+i);
			if (comp.getComponent(i) instanceof STRPanel) {
				System.out.println("COMP"+i+" timerStop");
				((STRPanel) comp.getComponent(i)).timerStop();
			} else if (comp.getComponent(i) instanceof JComponent)
				clearTimers((JComponent)comp.getComponent(i));
		}

	}
}
