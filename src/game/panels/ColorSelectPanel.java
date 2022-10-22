package game.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import game.box.CardsDealt;
import game.main.Board;
import game.main.Utils;
import game.obj.Card;
import game.obj.Player;
import ui.TButton;

public class ColorSelectPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2633538609067144658L;

	int type, r, c, d, actionID;
	boolean reserved, second;
	int[] coinsNeeded;
	CardsDealt cd;
	Card card;
	Board board;
	JScrollPane jf;
	ComponentAdapter ca;
	String tempText;

	public ColorSelectPanel(Board board, Card card) {
		this.ca = new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				update();
			}
		};

		this.card = card;
		this.board = board;
		this.jf = board.scrollCards;

		setLayout(new GridBagLayout());
		// setBorder(BorderFactory.createLineBorder(new Color(50, 50, 0, 255)));
		// setBackground(new Color(255, 255, 245));
		setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridy = 0;
		gbc.gridwidth = 5;
		gbc.insets = new Insets(2, 2, 2, 2);

		add(new JLabel(board.lt("you_select_color")), gbc);

		gbc.gridy = 1;
		gbc.gridwidth = 1;

		Card[] coloredCard = new Card[5];
		CardsDealt cd = board.cardsDealt[1];
		Player player = board.players[board.aPID];
		int[] ownedCards = player.ownedCards;
		
		for (int i = 0; i < 5; i++) {
			gbc.gridx = i;
			coloredCard[i] = Utils.copyCard(card, true, false);
			coloredCard[i].color = i;
			coloredCard[i].setPreferredSize(new Dimension(103, 153));
			if (ownedCards[i] > 0
					|| (cd.cancelPossible[0] && cd.getActCard(0).color == i && cd.getActCard(0).special == 3)
					|| (player.cancelPossible && player.clCard.color == i && player.clCard.special == 3)) {

				int k = i;

				coloredCard[i].setActive();
				coloredCard[i].setBorder(2);
				coloredCard[i].addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent me) {
						card.color = k;
						if (second) {
							board.aB.specAct2 = "SPED" + k;
						} else
							board.aB.specAct = "SPEC" + k;
						runAction();
					}
				});
			} else {
				coloredCard[i].setGrayscale(true);

			}
			add(coloredCard[i], gbc);
			// coloredCard[i].add(shadow,0);
		}
		gbc.gridy = 2;
		gbc.gridwidth = 5;
		gbc.gridx = 0;
		TButton cancel = new TButton(board.lt("cancel"), "yellow", 14);
		cancel.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					cancelAction();
				}
			}
		});
		add(cancel, gbc);
	}

	public void setup(int r, int c, int d, boolean reserved, boolean second) {
		this.type = 1;
		this.r = r;
		this.c = c;
		this.d = d;
		this.reserved = reserved;
		this.second = second;

	}

	public void setup(CardsDealt cd, Card card, int r, int c, int[] coinsNeeded, int actionID, int d, boolean second) {
		this.type = 2;
		this.cd = cd;
		this.r = r;
		this.c = c;
		this.coinsNeeded = coinsNeeded;
		this.actionID = actionID;
		this.d = d;
		this.second = second;
	}

	public void showThis() {
		board.actionPanelButtons.setVisible(false);
		board.actionPanelContent.setVisible(false);
		for (Component c : board.actionPanelHeader.getComponents()) {
			if (c instanceof JLabel) {
				tempText = ((JLabel) c).getText();
			}
		}
		board.exposeCards();
		board.addInfoLabel("Czekam na wybór...");
		setBounds(2 + (jf.getWidth() - 550) / 2, (jf.getHeight() - 230) / 2, 550, 230);
		jf.add(this, 0);
		jf.repaint();
		jf.revalidate();
		jf.addComponentListener(ca);
	}

	public void update() {
		setBounds(2 + (jf.getWidth() - 550) / 2, (jf.getHeight() - 230) / 2, 550, 230);
		jf.repaint();
		jf.revalidate();
	}

	public void clear() {
		board.removeCardsActions();
		board.actionPanelButtons.setVisible(true);
		board.actionPanelContent.setVisible(true);
		jf.removeComponentListener(ca);
		jf.remove(this);
		jf.repaint();
		jf.revalidate();
	}

	public void cancelAction() {
		clear();

		board.addInfoLabel(tempText);
		if (type == 1) {
			board.cancelAction(second);
		}
		if (type == 2) {
			board.setFreeCardActions(r, coinsNeeded, actionID, d, second);
		}

	}

	public void runAction() {
		clear();

		if (type == 1) {
			board.actionBuyCardContinue(card, r, c, d, reserved, second);
		}
		if (type == 2) {
			board.fcaSelected(cd, card, r, c, coinsNeeded, actionID, d, second);
		}

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

}
