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
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import game.main.Board;
import game.main.Utils;
import game.obj.Aristocrat;
import game.obj.Card;
import game.obj.Player;
import ui.PointsBadge;
import ui.ResSlot;
import ui.SmallCoinStack;
import ui.SmallReverse;
import ui.TButton;
import ui.StrLocalPanel;
import ui.SumCoinStack;

public class LocalPlayerPanel extends JPanel {

	private static final long serialVersionUID = -7237480553393225110L;
	// Graphics2D g2;
	Player player;
	JPanel[] resCardPaint = new JPanel[3];
	JPanel[] card = new JPanel[3];
	JPanel pc = new JPanel(), rewardsPanel = new JPanel();
	JPopupMenu pop;
	public boolean soundEnabled = true, aris;
	List<Aristocrat> _arilist;
	int id;
	Board board;
	Color bgColor = new Color(255, 255, 245);
	Color bgColor2 = new Color(235, 235, 225);
	Color borderColor = new Color(50, 50, 0, 255);

	public LocalPlayerPanel(int x, Board board) {
		this.board = board;
		if (board.debug)
			soundEnabled=false;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(false);
		this.id = x;
		this.player = board.players[id];
		this.aris = board.optAris;
		if (aris) {
			_arilist = board.aristocratsDealt._arilist;
		}

		this.pop = new JPopupMenu();
		this.pop.setBorder(BorderFactory.createEmptyBorder());
		this.pop.setOpaque(false);
		this.pop.setBorderPainted(false);
		this.pop.setVisible(false);

		for (int i = 0; i < 3; i++) {
			this.resCardPaint[i] = new JPanel();
			this.resCardPaint[i].setLayout(null);
			this.resCardPaint[i].setPreferredSize(new Dimension(50, 75));
			this.resCardPaint[i].setOpaque(false);

			this.card[i] = new JPanel();
			this.card[i].setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			this.card[i].setOpaque(false);
			this.card[i].setPreferredSize(new Dimension(103, 153));
		}

		rewardsPanel.setLayout(new GridBagLayout());
		rewardsPanel.setOpaque(true);
		rewardsPanel.setBorder(BorderFactory.createLineBorder(borderColor));

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

		JLabel help = new JLabel(getIcon("images/help.png"));
		JFrame helpPanel = new JFrame("SplenJAVor - Pomoc");
		helpPanel.setPreferredSize(new Dimension(1160, 665));
		helpPanel.setResizable(false);
		helpPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		helpPanel.setLocation(dim.width / 2 - 575, 0);

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
		pc.setOpaque(false);
		pc.setLayout(new FlowLayout(FlowLayout.CENTER,3,7));
		pc.setPreferredSize(new Dimension(565, 90));
		add(pc);
		draw();

	}

	public void draw() {
		pc.removeAll();
		JPanel ccHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		ccHolder.setOpaque(false);

		for (int i = 0; i < 5; i++) {
			JLabel cc = Utils.cardAndCoinDraw(i, player.production[i], player.ownedCoins[i], 2);
			ccHolder.add(cc);
		}
		ccHolder.add(Utils.coinDraw(5, player.ownedCoins[5], 2,false));

		ccHolder.addMouseListener(new MouseAdapter() {

			public void mouseExited(MouseEvent me) {
				pop.removeAll();
				pop.setVisible(false);
			}

			public void mouseEntered(MouseEvent me) {
				if (!pop.isVisible()) {
					popupCards(ccHolder);
				}
			}
			public void mousePressed(MouseEvent me) {
				pop.setVisible(true);
			}
		});
		ccHolder.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				pop.setVisible(true);
			}
		});

		pc.add(ccHolder);
		pc.add(new SumCoinStack(player.ownedCoins));
		if (board.optStrongholds) {
			pc.add(new StrLocalPanel(id,player.str));
		}
		for (int i = 0; i < 3; i++) {
			resCardPaint[i].removeAll();
			resCardPaint[i].add((player.reservedCards > i) ? new SmallReverse(player.resCard[i]) : new ResSlot());
			pc.add(resCardPaint[i]);
		}
		pc.add(new PointsBadge(player.points));
		refactorRes();
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
					pop.removeAll();
					pop.setVisible(false);
				}

				public void mousePressed(MouseEvent me) {
					if (player.resCard[a].highlighted) {
						pop.removeAll();
						pop.setVisible(false);
					}
				}
			});
			card[i].add(player.resCard[i]);
			card[i].repaint();
			card[i].revalidate();

			resCardPaint[i].addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent me) {
					if (!pop.isVisible())
						popupCard(a);
				}
			});
		}
	}

	public void popupCard(int n) {
		pop.removeAll();
		pop.add(card[n]);
		pop.show(resCardPaint[n], -27, -76);

	}

	public void popupCards(JComponent comp) {
		pop.removeAll();
		JPanel panel = showCards();
		JPanel showPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(0, 0, 0, 0);
		showPanel.setOpaque(false);

		if (panel.getComponentCount() == 0 && player.coinsSum() == 0) {
			JLabel label = new JLabel("Nie posiadasz jeszcze żadnych kart ani żetonów!");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.CENTER);
			Dimension dim = new Dimension(label.getPreferredSize().width + 8, label.getPreferredSize().height + 8);
			panel.setPreferredSize(dim);
			panel.setBackground(bgColor);
			panel.add(label);
		}
		showPanel.add(panel, gbc);
		gbc.insets = new Insets(0, -1, 0, 0);

		if (rewardsPanel.getComponentCount() != 0) {
			showPanel.add(rewardsPanel, gbc);
		}

		if (player.coinsSum() != 0) {
			showPanel.add(showCoins(), gbc);
		}

		pop.add(showPanel);

		int x = 124 - (panel.getComponentCount() > 0 ? panel : showPanel).getPreferredSize().width / 2;
		int y = 0 - (showPanel.getPreferredSize().height + 4);
		pop.show(comp, x, y);

	}

	/*
	 * public int targetX(JComponent comp) { return (int)
	 * comp.getLocationOnScreen().getX(); }
	 * 
	 * public int targetY(JComponent comp) { return (int)
	 * comp.getLocationOnScreen().getY(); }
	 */

	public void removeAllActions() {
		for (int i = 0; i < 3; i++) {
			while (resCardPaint[i].getMouseListeners().length != 0) {
				resCardPaint[i].removeMouseListener(resCardPaint[i].getMouseListeners()[0]);
			}
		}
		for (int i = 0; i < player.reservedCards; i++) {
			player.resCard[i].removeAllActions();
		}
		pop.removeAll();
		pop.setVisible(false);

	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	public JPanel showCoins() {
		JPanel coins = new JPanel();
		coins.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		coins.setBorder(BorderFactory.createLineBorder(borderColor));
		coins.setBackground(bgColor);
		SmallCoinStack smc = new SmallCoinStack(player.ownedCoins, true);
		coins.add(smc);
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
				JLabel label = new JLabel("<html><center>Zarezerwowani<br>arystokraci:</center></html>");
				label.setVerticalAlignment(JLabel.TOP);
				colorPanel.add(label, gbc, 0);
				for (int h = 0; h <= board.players.length; h++) {
					gbc.insets = new Insets(45 + n * 98, 5, 5, 5);
					Aristocrat t = _arilist.get(h);
					if (t.isAvailable && t.isReserved && t.resID == id) {
						Aristocrat nAri = new Aristocrat(t.imgPath, t.reward, t.req);
						height = height < (40 + 98 + n * 98) ? (40 + 98 + n * 98) : height;
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

}
