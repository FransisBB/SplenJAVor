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
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import game.main.Board;
import game.main.Utils;
import game.obj.Aristocrat;
import game.obj.Card;
import game.obj.Player;
import ui.SmallCoinStack;

public class PlayersPanelBackup extends JPanel {
	private static final long serialVersionUID = 8793901114737663002L;

	Player[] player;
	Graphics2D g2;
	int activeP = 0;
	boolean isLastRound = false;
	List<Aristocrat> _arilist;
	JLabel[] history;
	JPanel[] historyPanel, rewardsPanel;
	Popup res, res2, res3;
	JPopupMenu pop;
	boolean resVis = false, resVis2 = false, resVis3 = false, cities, aris, strongholds;

	public PlayersPanelBackup(Board board) {
		
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
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		for (int i = 0; i < player.length; i++) {
			historyPanel[i] = new JPanel();
			historyPanel[i].setLayout(new GridBagLayout());
			historyPanel[i].setOpaque(true);
			historyPanel[i].setBorder(BorderFactory.createLineBorder(Color.black));
			rewardsPanel[i] = new JPanel();
			rewardsPanel[i].setLayout(new GridBagLayout());
			rewardsPanel[i].setOpaque(true);
			rewardsPanel[i].setBorder(BorderFactory.createLineBorder(Color.black));
			JLabel def = new JLabel("Gracz nie wykona³ jeszcze ¿adnego ruchu.");
			historyPanel[i].add(def, gbc);

			int j = i;

			history[i] = new JLabel(getIcon("images/historyOff.png"));
			history[i].setOpaque(false);
			history[i].addMouseListener(new MouseAdapter() {

				public void mouseEntered(MouseEvent me) {
					history[j].setIcon(getIcon("images/historyOn.png"));
					// repaint();
					if (!resVis)
						popupHistory(j);
				}

				public void mouseExited(MouseEvent me) {
					history[j].setIcon(getIcon("images/historyOff.png"));
					// repaint();
					if (resVis) {
						res.hide();
						resVis = false;
					}
				}
			});

		}

		setLayout(null);
		setPreferredSize(new Dimension(299, 465));
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
		int y = (this.getHeight() - (player.length == 5 ? 5 : 4) * 118) / 2;
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
					if (resVis) {
						res.hide();
						resVis = false;
					}
					if (resVis2) {
						res2.hide();
						resVis2 = false;
					}
					if (resVis3) {
						res3.hide();
						resVis3 = false;
					}
				}

				public void mouseEntered(MouseEvent me) {
					if (!resVis && !resVis2 && !resVis3)
						popupCards(c);
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
			//System.out.println("X: " + x);
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
			y += 118;
			b++;
		}
		for (int n = b; n < 4; n++) {
			int x = (this.getWidth() - 295) / 2;

			g2.setColor(new Color(0, 0, 0, 10));
			g2.fillRoundRect(x, y, 295, 113, 15, 15);
			y += 118;
		}
	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	public void popupHistory(int n) {

		int targetY = targetY(history[n]);
		int targetX = targetX(history[n]);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		boolean tooLow = false;
		if (targetY + historyPanel[n].getHeight() > (dim.height - 45)) {
			targetY = dim.height - historyPanel[n].getHeight() - 45;
			tooLow = true;
		}
		if (targetY + historyPanel[n].getHeight() + 115 > (dim.height - 45)) {
			tooLow = true;
		}
		if (targetX + historyPanel[n].getWidth() > dim.width - 5) {
			targetX = dim.width - historyPanel[n].getWidth() - 5;
			targetX = targetX(history[n]) - 300;
			if (targetX + historyPanel[n].getWidth() > dim.width - 5) {
				targetX = dim.width - historyPanel[n].getWidth() - 5;
			}
			if (tooLow) {
				targetY = targetY(history[n]) - historyPanel[n].getHeight();
			} else {
				targetY += 115;
			}
		}
		if (targetX<5)
			targetX=5;
		resVis = true;
		res = PopupFactory.getSharedInstance().getPopup(this, historyPanel[n], targetX, targetY);
		res.show();
	}

	public void popupCards(int b) {
		/*
		 * if (player[b].coinsSum()>0) { gbc.insets = new Insets(5+30*maxN, 5, 5, 5);
		 * gbc.gridx = 5; gbc.anchor = GridBagConstraints.SOUTH; userPanel.add(new
		 * SmallCoinStack(player[b].ownedCoins,false),gbc); }
		 */

		JPanel panel = showCards(b);
		GridBagConstraints gbc = new GridBagConstraints();
		if (panel.getComponentCount() == 0 && player[b].coinsSum() == 0) {

			gbc.insets = new Insets(5, 5, 5, 5);
			if (player[b].isLocal) {
				panel.add(new JLabel("Nie posiadasz jeszcze ¿adnych kart ani ¿etonów."), gbc);
			} else {
				panel.add(new JLabel("Gracz nie posiada jeszcze ¿adnych kart ani ¿etonów."), gbc);
			}

			panel.setBorder(BorderFactory.createLineBorder(Color.black));
		}
		int mod = 1;
		int targetY = targetY(history[b]);
		int targetX = targetX(history[b]);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		boolean tooLow = false;
		int totalWidth = panel.getWidth();
		int maxHeight = panel.getHeight();

		if (rewardsPanel[b].getComponentCount() > 0) {
			totalWidth += rewardsPanel[b].getPreferredSize().width;
			if (rewardsPanel[b].getPreferredSize().height > maxHeight)
				maxHeight = rewardsPanel[b].getPreferredSize().height;
		}
		SmallCoinStack sct = new SmallCoinStack(player[b].ownedCoins, true);
		if (player[b].coinsSum() > 0) {
			totalWidth += sct.getPreferredSize().width + 10;
			if (sct.getPreferredSize().getHeight() + 10 > maxHeight)
				maxHeight = sct.getPreferredSize().height + 10;
		}

		if (targetY + maxHeight > (dim.height - 45)) {
			targetY = dim.height - maxHeight - 45;
			tooLow = true;
		}
		if (targetY + maxHeight + 115 > (dim.height - 45)) {
			tooLow = true;
		}

		if (targetX + totalWidth > dim.width - 5) {
			// targetX = dim.width - totalWidth - 5;
			targetX = targetX(history[b]) - 300;
			if (targetX + totalWidth > dim.width - 5) {
				targetX = dim.width - totalWidth - 5;
			}
			if (tooLow) {
				targetY = targetY(history[b]) - maxHeight;
			} else {
				targetY += 115;
			}
		}
		if (targetX<5)
			targetX=5;
		if (panel.getComponentCount() != 0) {
			panel.setBorder(BorderFactory.createLineBorder(Color.black));
			resVis = true;
			res = PopupFactory.getSharedInstance().getPopup(this, panel, targetX, targetY);
			res.show();

		}
		if (rewardsPanel[b].getComponentCount() != 0) {
			resVis3 = true;
			res3 = PopupFactory.getSharedInstance().getPopup(this, rewardsPanel[b], targetX + panel.getWidth() - mod,
					targetY);
			res3.show();
			mod++;
		}
		if (player[b].coinsSum() > 0) {
			JPanel coins = new JPanel();
			coins.setLayout(new GridBagLayout());
			gbc.insets = new Insets(5, 5, 5, 5);
			coins.add(sct, gbc);
			coins.setBorder(BorderFactory.createLineBorder(Color.black));
			resVis2 = true;
			res2 = PopupFactory.getSharedInstance().getPopup(this, coins,
					targetX + panel.getWidth() + rewardsPanel[b].getWidth() - mod, targetY);
			res2.show();
		}

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
		// panel.setPreferredSize(new Dimension(comp.getPreferredSize().width,
		// comp.getPreferredSize().height));
		switch (rewardsPanel[n].getComponentCount() % 4) {
		case 0:
		case 3:
			panel.setBackground(new Color(255, 255, 255));
			break;
		case 1:
		case 2:
			panel.setBackground(new Color(235, 235, 235));
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
					colorPanel.setBackground(new Color(255, 255, 255));
					break;
				case 1:
					colorPanel.setBackground(new Color(235, 235, 235));
					break;
				default:
					colorPanel.setBackground(new Color(0, 0, 0));
					break;

				}
				userPanel.add(colorPanel, gbc);
			}

		}
		userPanel.setSize(width, height+2);
		return userPanel;

	}

	public int targetX(JLabel label) {
		return (int) label.getLocationOnScreen().getX() + 30;
	}

	public int targetY(JLabel label) {
		return (int) label.getLocationOnScreen().getY() - 4;
	}

	public void addContent(int n, Component panel) {

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;

		switch ((historyPanel[n].getComponentCount()) % 2) {
		case 0:
			panel.setBackground(new Color(255, 255, 255));
			break;
		case 1:
			panel.setBackground(new Color(235, 235, 235));
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
		historyPanel[n].setSize(w, h+2);
	}

	public void clearContent(int n) {
		historyPanel[n].removeAll();
	}

}
