package game.box;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import game.main.Board;
import game.main.Utils;
import game.obj.Aristocrat;
import game.obj.Card;
import game.obj.Player;
import ui.SmallCoinStack;
import ui.SumCoinStack;
import ui.TButton;

public class LocalPlayerPanelBackup extends JPanel {

	private static final long serialVersionUID = -7237480553393225110L;
	// Graphics2D g2;
	Player player;
	JPanel[] resCardPaint = new JPanel[3];
	JPanel[] card = new JPanel[3];
	JPanel pc = new JPanel(), rewardsPanel = new JPanel();
	JLabel swap;
	Popup res, res3, res2;
	boolean front = true;
	public boolean soundEnabled = true, aris;
	List<Aristocrat> _arilist;
	int id, pLength;
	Board board;

	public LocalPlayerPanelBackup(int x, Board board) {
		this.board = board;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// setMaximumSize(new Dimension(1100,80));
		setOpaque(false);
		// setBorder(BorderFactory.createRaisedBevelBorder());
		this.id = x;
		this.pLength = board.players.length;
		this.player = board.players[id];
		this.aris = board.optAris;
		if (aris) {
			_arilist = board.aristocratsDealt._arilist;
		}
		for (int i = 0; i < 3; i++) {
			this.resCardPaint[i] = new JPanel();
			this.resCardPaint[i].setLayout(null);
			this.resCardPaint[i].setPreferredSize(new Dimension(50, 75));
			this.resCardPaint[i].setOpaque(false);

			this.card[i] = new JPanel();
			this.card[i].setLayout(new FlowLayout());
			this.card[i].setOpaque(false);
			this.card[i].setPreferredSize(new Dimension(108, 163));
			this.card[i].setBorder(BorderFactory.createLineBorder(Color.black));
		}

		rewardsPanel.setLayout(new GridBagLayout());
		rewardsPanel.setOpaque(true);
		rewardsPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		JPanel top = new JPanel();
		top.setOpaque(false);
		top.setLayout(new GridBagLayout());
		JLabel nick = new JLabel(player.playerName);
		nick.setFont(new Font("Arial", Font.BOLD, 24));
		nick.setForeground(new Color(50, 50, 0));
		JPanel fillerLeft = new JPanel();
		fillerLeft.setPreferredSize(new Dimension(70, 30));
		fillerLeft.setOpaque(false);

		JPanel fillerRight = new JPanel(new FlowLayout());
		fillerRight.setOpaque(false);
		fillerRight.setPreferredSize(new Dimension(70, 30));

		// nick.setPreferredSize(new Dimension(70, 30));
		JLabel sound = new JLabel();
		if (soundEnabled) {
			sound.setIcon(getIcon("images/soundON.png"));
		} else {
			sound.setIcon(getIcon("images/soundOFF.png"));
		}
		sound.setPreferredSize(new Dimension(20, 20));
		sound.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent me) {
				if (soundEnabled) {
					sound.setIcon(getIcon("images/soundONhover.png"));
				} else {
					sound.setIcon(getIcon("images/soundOFFhover.png"));
				}
			}

			public void mouseExited(MouseEvent me) {
				if (soundEnabled) {
					sound.setIcon(getIcon("images/soundON.png"));
				} else {
					sound.setIcon(getIcon("images/soundOFF.png"));
				}
			}

			public void mousePressed(MouseEvent me) {
				if (soundEnabled) {
					soundEnabled = false;
					sound.setIcon(getIcon("images/soundOFFhover.png"));
				} else {
					soundEnabled = true;
					sound.setIcon(getIcon("images/soundONhover.png"));
				}
			}

		});
		// sound.setBackground(new Color(255,255,255,0));
		JLabel help = new JLabel(getIcon("images/help.png"));
		JFrame helpPanel = new JFrame("SplenJAVor - Pomoc");
		helpPanel.setPreferredSize(new Dimension(1160, 665));
		helpPanel.setResizable(false);
		helpPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		helpPanel.setLocation(dim.width / 2 - 575, 0);
		/*
		 * TODO helpPanel
		 */
		JLabel helpPanelImage = new JLabel(getIcon("images/helpPanel.png"));
		TButton closeHelpPanel = new TButton("Zamknij", "red", 16);
		closeHelpPanel.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					helpPanel.dispose();
				}
			}
		});
		JPanel container = new JPanel();
		container.add(helpPanelImage);
		container.add(closeHelpPanel);
		helpPanel.add(container);
		help.setPreferredSize(new Dimension(20, 20));
		help.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent me) {
				help.setIcon(getIcon("images/helpHover.png"));

			}

			public void mouseExited(MouseEvent me) {
				help.setIcon(getIcon("images/help.png"));
			}

			public void mousePressed(MouseEvent me) {
				// if(!helpVis) {
				// helpPopupShow(helpPanel);
				// }
				helpPanel.pack();
				helpPanel.setVisible(true);
			}

		});
		fillerRight.add(sound);
		fillerRight.add(help);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		top.add(fillerLeft, gbc);
		gbc.gridx = 2;
		top.add(fillerRight, gbc);
		gbc.gridx = 1;
		gbc.weightx = 1;
		top.add(nick, gbc);
		add(top);
		pc.setAlignmentX(Component.CENTER_ALIGNMENT);
		pc.setOpaque(false);
		pc.setLayout(new FlowLayout());
		pc.setPreferredSize(new Dimension(565, 90));
		add(pc);

		swap = new JLabel(getIcon("images/swapIcon.png"));
		swap.setPreferredSize(new Dimension(30, 30));
		swap.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent me) {
				swap.setIcon(getIcon("images/swapIconHover.png"));
			}

			public void mouseExited(MouseEvent me) {
				swap.setIcon(getIcon("images/swapIcon.png"));
			}

			public void mousePressed(MouseEvent me) {
				if (front) {
					front = false;
				} else {
					front = true;
				}
				draw();

			}

		});
		draw();

	}

	public void draw() {
		pc.removeAll();
		if (front) {
			// JPanel ccHolder = new JPanel();
			JPanel ccHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
			ccHolder.setOpaque(false);
			// ccHolder.setLayout(new GridLayout(1, 5, 5, 0));
			for (int i = 0; i < 5; i++) {
				JLabel cc = Utils.cardAndCoinDraw(i, player.production[i], player.ownedCoins[i], 2);
				ccHolder.add(cc);
			}
			ccHolder.addMouseListener(new MouseAdapter() {

				public void mouseExited(MouseEvent me) {
					if (res != null) {
						System.out.println("res1:" + res);
						res.hide();
						res = null;
					}
					if (res3 != null) {
						System.out.println("res2" + res3);
						res3.hide();
						res3 = null;
					}
					if (res2 != null) {
						System.out.println("res3" + res2);
						res2.hide();
						res2 = null;

					}
				}

				public void mouseEntered(MouseEvent me) {
					if (res == null && res3 == null && res2 == null) {
						popupCards(ccHolder);
						// popupCards(ccHolder);
					}
				}
			});
			ccHolder.add(Utils.coinDraw(5, player.ownedCoins[5], 2,false));
			pc.add(ccHolder);
			// pc.add(Utils.coinDraw(5, player.ownedCoins[5], 2));
			pc.add(new SumCoinStack(player.ownedCoins));
			// pc.add();

			for (int i = 0; i < 3; i++) {
				resCardPaint[i].removeAll();
				resCardPaint[i].add((player.reservedCards > i) ? new Reverse(i) : new ResSlot());
				pc.add(resCardPaint[i]);
			}
			pc.add(new PointsBadge());
			refactorRes();
			// pc.add(swap);
		} else {
			JLabel label = new JLabel("TEST");
			label.addMouseListener(new MouseAdapter() {
				public void mouseExited(MouseEvent me) {
					if (res != null) {
						res.hide();
						res = null;
					}
				}

				public void mouseEntered(MouseEvent me) {
					if (res == null)
						popupRewards();
				}
			});
			pc.add(label);
		}
		repaint();
		revalidate();

	}

	public void refactorRes() {
		removeAllActions();

		for (int i = 0; i < player.reservedCards; i++) {

			int a = i;
			card[i].removeAll();
			player.resCard[i].addMouseListener(new MouseAdapter() {
				public void mouseExited(MouseEvent me) {
					if (res != null) {
						res.hide();
						res = null;
					}
				}

				public void mousePressed(MouseEvent me) {
					if (res != null && player.resCard[a].highlighted) {
						res.hide();
						res = null;
					}
				}
			});
			card[i].add(player.resCard[i]);
			card[i].repaint();
			card[i].revalidate();

			resCardPaint[i].addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent me) {
					if (res == null)
						popupCard(a);
				}
			});
		}
	}

	public void popupCard(int n) {
		res = PopupFactory.getSharedInstance().getPopup(this, card[n], targetX(resCardPaint[n]) - 29,
				targetY(resCardPaint[n]) - 88);
		// res.show();
		res.show();
	}

	public void popupRewards() {
		res = PopupFactory.getSharedInstance().getPopup(this, rewardsPanel, 10, 10);
		res.show();
	}

	public void popupCards(JComponent comp) {
		JPanel panel = showCards();
		PopupFactory factory = new PopupFactory();

		GridBagConstraints gbc = new GridBagConstraints();
		
		if (panel.getComponentCount() == 0 && player.coinsSum() == 0) {
			gbc.insets = new Insets(5, 5, 5, 5);
			panel.add(new JLabel("Nie posiadasz jeszcze żadnych kart ani żetonów."), gbc);
		}
		
		int offsety = 0, offsetx = 0;

		if (panel.getComponentCount() != 0) {
			
			offsetx = panel.getWidth() > 0 ? 123 - panel.getWidth() / 2 : 0;
			offsety = panel.getHeight() > 0 ? panel.getHeight() + 5 : 31;
			res = factory.getPopup(this, panel, targetX(comp) + offsetx, targetY(comp) - offsety);
			res.show();

		}
		
		if (rewardsPanel.getComponentCount() != 0) {
			res2 = factory.getPopup(this, rewardsPanel, targetX(panel) + panel.getWidth() - 1, targetY(comp) - offsety);
			res2.show();
			
		}
		
		if (player.coinsSum() != 0) {
			JPanel coins = showCoins();
			if (panel.getComponentCount() == 0) {
				offsetx = 123 - coins.getWidth()/2;
				offsety = coins.getHeight()+5;
			} else {
				if (rewardsPanel.getComponentCount() != 0) {
					offsetx += panel.getWidth() + rewardsPanel.getWidth() - 2;
				} else {
					offsetx += panel.getWidth() - 1;
				}
			}

			res3 = factory.getPopup(this, coins, targetX(comp) + offsetx, targetY(comp) - offsety);

			res3.show();

		}

	}

	public int targetX(JComponent comp) {
		return (int) comp.getLocationOnScreen().getX();
	}

	public int targetY(JComponent comp) {
		return (int) comp.getLocationOnScreen().getY();
	}

	public void removeAllActions() {
		for (int i = 0; i < 3; i++) {
			while (resCardPaint[i].getMouseListeners().length != 0) {
				resCardPaint[i].removeMouseListener(resCardPaint[i].getMouseListeners()[0]);
			}
		}
		for (int i = 0; i < player.reservedCards; i++) {
			player.resCard[i].removeAllActions();
		}

		if (res != null) {
			res.hide();
			res = null;
		}
		if (res3 != null) {
			res3.hide();
			res3 = null;
		}
		if (res2 != null) {
			res2.hide();
			res2 = null;
		}
	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	public JPanel showCoins() {
		JPanel coins = new JPanel();
		coins.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		SmallCoinStack smc = new SmallCoinStack(player.ownedCoins, true);
		//smc.setPreferredSize(new Dimension(smc.getPreferredSize().width,140));
		coins.add(smc, gbc);
		coins.setSize(smc.getPreferredSize().width+10, smc.getPreferredSize().height+10);
		coins.setBorder(BorderFactory.createLineBorder(Color.black));
		return coins;
	}

	public JPanel showCards() {
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
				for (Card c : player._cardList) {
					if (c.color == i) {
						gbc.insets = new Insets(5 + n * 30, 5, 5, 5);
						colorPanel.add(c, gbc, 0);
						height = height < 163 + n * 30 ? 163 + n * 30 : height;
						n++;
					}
				}
			} else if (i == 6) {
				for (int h = 0; h < player.reservedCards; h++) {
					gbc.insets = new Insets(5 + n * 30, 5, 5, 5);
					Card t = player.resCard[h];
					Card nCard = Utils.copyCard(t, false, false);
					nCard.showFront(false);
					colorPanel.add(nCard, gbc, 0);
					height = height < 163 + n * 30 ? 163 + n * 30 : height;
					n++;
				}
			} else if (i == 7 && aris) {
				gbc.insets = new Insets(5 + n * 98, 5, 5, 5);
				// String labelText = String.format("<html><div
				// style=\"width:%dpx;\"><center>%s</center></div></html>", 68,
				// "Zarezerwowani<br>arystokraci:");
				JLabel label = new JLabel("<html><center>Zarezerwowani<br>arystokraci:</center></html>");
				label.setVerticalAlignment(JLabel.TOP);
				colorPanel.add(label, gbc, 0);
				for (int h = 0; h <= pLength; h++) {
					gbc.insets = new Insets(45 + n * 98, 5, 5, 5);
					Aristocrat t = _arilist.get(h);
					if (t.isAvailable && t.isReserved && t.resID == id) {
						Aristocrat nAri = new Aristocrat(t.imgPath, t.reward, t.req);
						height = height < 40 + 98 + n * 98 ? 40 + 98 + n * 98 : height;
						n++;
						nAri.showFront = true;
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
		userPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		userPanel.setSize(width, height);
		return userPanel;

	}

	public void addReward(Component comp) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridy = rewardsPanel.getComponentCount() % 2;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(comp);
		switch (rewardsPanel.getComponentCount() % 4) {
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
		rewardsPanel.add(panel, gbc);
		int cols = (int) Math.ceil((double) rewardsPanel.getComponentCount() / 2);
		rewardsPanel.setSize(cols * 88, 88 * 2);
	}

	@Override
	protected void paintComponent(Graphics g) {

		// Allow super to paint
		super.paintComponent(g);

		// Apply our own painting effect
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// 50% transparent Alpha
		// g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2d.setColor(new Color(0, 0, 0, 30));
		g2d.fillRoundRect(6, 1, getWidth() - 11, getHeight() - 3, 30, 30);
		g2d.setColor(new Color(255, 255, 240, 180));
		g2d.fillRoundRect(5, 0, getWidth() - 11, getHeight() - 3, 30, 30);
		g2d.setColor(new Color(0, 0, 0, 30));
		g2d.fillRoundRect(6, 1, getWidth() - 11, 30, 30, 30);
		g2d.setColor(new Color(245, 245, 160, 180));
		g2d.fillRoundRect(5, 0, getWidth() - 11, 30, 30, 30);
		g2d.dispose();

	}

	private class PointsBadge extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7275153942718917577L;

		PointsBadge() {
			setLayout(null);
			setPreferredSize(new Dimension(54, 72));
			setOpaque(false);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2;
			g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setFont(new Font("Arial", Font.PLAIN, 26));
			g2.drawImage(getIcon("images/awardBIG.png").getImage(), 0, 0, this);
			int px = 19;
			int py = 34;
			if (player.points > 9)
				px -= 8;

			// g2.setFont(new Font("Arial", Font.PLAIN, 20));
			g2.setColor(new Color(255, 255, 255, 150));
			g2.drawString(Integer.toString(player.points), px - 1, py + 0);
			g2.setColor(new Color(0, 0, 0, 150));
			g2.drawString(Integer.toString(player.points), px + 1, py + 2);
			g2.setColor(new Color(180, 180, 20));
			g2.drawString(Integer.toString(player.points), px, py + 1);
		}
	}

	private class ResSlot extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4032331948455707947L;

		ResSlot() {
			setLayout(null);
			setBounds(0, 0, 50, 75);
			setOpaque(false);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2;
			g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(0, 0, 0, 25));
			g2.fillRoundRect(0, 0, 48, 73, 10, 10);
		}
	}

	private class Reverse extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6714551417658865817L;
		int n;
		String path;

		Reverse(int n) {
			this.n = n;
			if (player.resCard[n].type == "standard") {
				switch (player.resCard[n].level) {
				case 0:
					path = "images/cards/green.jpg";
					break;
				case 1:
					path = "images/cards/yellow.jpg";
					break;
				case 2:
					path = "images/cards/blue.jpg";
					break;
				default:
					path = "images/cards/green.jpg";
				}
			} else if (player.resCard[n].type == "orient") {
				switch (player.resCard[n].level) {
				case 0:
					path = "images/cards/greenOrient.jpg";
					break;
				case 1:
					path = "images/cards/yellowOrient.jpg";
					break;
				case 2:
					path = "images/cards/blueOrient.jpg";
					break;
				default:
					path = "images/cards/greenOrient.jpg";
				}
			}
			setLayout(null);
			setBounds(0, 0, 50, 75);
			setOpaque(false);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2;
			g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(0, 0, 0, 75));
			g2.fillRoundRect(2, 2, 48, 73, 10, 10);
			BufferedImage image = new BufferedImage(48, 73, BufferedImage.TYPE_INT_RGB);
			image.createGraphics().drawImage(getIcon(path).getImage(), 0, 0, 48, 73, null);
			Rectangle2D rectangle = new Rectangle2D.Float(0, 0, 48, 73);
			g2.setPaint(new TexturePaint(image, rectangle));
			// g2.drawImage(getIcon(path).getImage(), 0, 0, 50, 75, null);
			// g2.roun
			g2.fillRoundRect(0, 0, 48, 73, 10, 10);
			g2.setFont(new Font("Verdana", Font.BOLD, 7));
			g2.setColor(new Color(255, 255, 255, 150));
			g2.drawString("SplenJAVor", 2, 20);
			g2.setColor(new Color(255, 255, 255, 100));
			if (player.resCard[n].level == 0) {
				g2.fillOval(24 - 4, 60, 8, 8);
			}
			if (player.resCard[n].level == 1) {
				g2.fillOval(24 - 4 - 6, 60, 8, 8);
				g2.fillOval(24 - 4 + 6, 60, 8, 8);
			}
			if (player.resCard[n].level == 2) {
				g2.fillOval(24 - 4 - 13, 60, 8, 8);
				g2.fillOval(24 - 4, 60, 8, 8);
				g2.fillOval(24 - 4 + 13, 60, 8, 8);
			}
			if (player.resCard[n].border == 2) {
				g2.setStroke(new BasicStroke(1));
				g2.setColor(new Color(0, 255, 0, 200));
				g2.drawRoundRect(0, 0, 48, 73, 10, 10);
			}
		}

	}

}
