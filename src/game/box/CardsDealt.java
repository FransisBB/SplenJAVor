package game.box;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import game.obj.Card;
import game.obj.Deck;

public class CardsDealt extends JPanel {
	private static final long serialVersionUID = 7436180999901726627L;

	public int cardsOnBoard[][];
	int piles[][], count[], rows, cols, lastCol;
	public int[] clRow = new int[3], clCol = new int[3], clCard = new int[3];
	public boolean[] cancelPossible = new boolean[3];
	GridBagConstraints gbc = new GridBagConstraints();
	public Deck deck;

	public CardsDealt(String name, int seed, boolean rightSide, boolean orient) {
		for (int i = 0; i < 3; i++) {
			cancelPossible[i] = false;
		}
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setOpaque(false);
		this.deck = new Deck(name, seed);
		rows = deck.rows;
		cols = deck.cols;
		cardsOnBoard = new int[rows][cols];
		count = new int[rows];
		// gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		if (orient) {
			gbc.insets = new Insets(4, 1, 3, 0);
		} else {
			gbc.insets = new Insets(4, 4, 3, 3);
		}
		this.piles = deck.splitCards();

//pierwsze rozdanie
		if (rightSide) {
			lastCol = cols - 1;
			for (int r = rows - 1; r >= 0; r--) {
				count[r] = 0;
				for (int c = cols - 1; c >= 0; c--) {
					gbc.gridx = c;
					gbc.gridy = r;
					add(new EmptyCard(), gbc, 0);
					putNewCard(r, c,false);

				}

			}
		} else {
			lastCol = 0;
			for (int r = rows - 1; r >= 0; r--) {
				count[r] = 0;
				for (int c = 0; c < cols; c++) {
					gbc.gridx = c;
					gbc.gridy = r;
					add(new EmptyCard(), gbc, 0);
					putNewCard(r, c,false);

				}

			}
		}

		/*
		 * for (int r = rows - 1; r >= 0; r--) { count[r] = 0; for (int c = 0; c <cols;
		 * c++) { gbc.gridx = c; gbc.gridy = r; add(new EmptyCard(), gbc, 0);
		 * putNewCard(r, c);
		 * 
		 * }
		 * 
		 * }
		 */

	}

	public void takeCard(int r, int c, int i) {
		// removeAllActions();
		clRow[i] = r;
		clCol[i] = c;
		clCard[i] = cardsOnBoard[r][c];
		getCard(cardsOnBoard[r][c]).removeAllActions();
		remove(getCard(cardsOnBoard[r][c]));
		repaint();
		revalidate();
		if (c == lastCol)
			putNewCard(r, c,false);
		cancelPossible[i] = true;

	}

	public void backCard(int i) {
		if (cancelPossible[i]) {
			if (clCol[i] == lastCol && cardsOnBoard[clRow[i]][clCol[i]] != 9999) {
				remove(getCard(cardsOnBoard[clRow[i]][clCol[i]]));
				count[clRow[i]]--;
			}
			cardsOnBoard[clRow[i]][clCol[i]] = clCard[i];
			gbc.gridy = clRow[i];
			gbc.gridx = clCol[i];
			add(getCard(clCard[i]), gbc, 0);
			repaint();
			revalidate();
			cancelPossible[i] = false;
		}
	}

	/*
	 * public void takeFreeCard(int r, int c, int i) { // removeAllActions();
	 * clRow[i] = r; clCol[i] = c; clCard[i] = cardsOnBoard[r][c];
	 * getCard(cardsOnBoard[r][c]).removeAllActions();
	 * remove(getCard(cardsOnBoard[r][c])); cancelPossible[i] = true; repaint();
	 * revalidate(); }
	 */

	public void putNewCard(int r, int c, boolean blink) {
		int newCard;
		// int lastCol = cols - 1; // deck card (last card in row - hidden)
		gbc.gridy = r;
		gbc.gridx = c;

		if (count[r] <= piles[r].length && c != lastCol) {
			remove(getCard(cardsOnBoard[r][lastCol]));
			newCard = cardsOnBoard[r][lastCol];
			cardsOnBoard[r][lastCol] = 9999;
			cardsOnBoard[r][c] = newCard;
			Card tCard = getCard(newCard);
			tCard.showFront(true);
			add(tCard, gbc, 0);
			if (blink)
				tCard.anim();


		}

		if (count[r] < piles[r].length) {
			newCard = piles[r][count[r]];
			cardsOnBoard[r][lastCol] = newCard;
			gbc.gridx = lastCol;
			add(getCard(newCard), gbc, 0);
			getCard(newCard).showFront(false);
		}

		count[r]++;
		repaint();
		revalidate();
	}

	public boolean confirmMove(boolean blink) {
		boolean ret=false;
		for (int i = 0; i < 3; i++) {
			if (cancelPossible[i]) {
				cancelPossible[i] = false;
				if (count[clRow[i]] > piles[clRow[i]].length) {
					cardsOnBoard[clRow[i]][clCol[i]] = 9999;
				} else if (clCol[i] != lastCol) {
					putNewCard(clRow[i], clCol[i], blink);
					ret=true;
				}
			}
		}
		return ret;
	}

	public void cancelMove() {
		for (int i = 0; i < 3; i++) {
			backCard(i);
		}
	}

	public void removeAllActions() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (cardsOnBoard[r][c] != 9999) {
					getCard(cardsOnBoard[r][c]).removeAllActions();
				}
			}
		}
	}

	public Card getCard(int i) {
		return deck._cardList.get(i);
	}

	public Card getActCard(int i) {
		return deck._cardList.get(clCard[i]);
	}

	private class EmptyCard extends JPanel {
		private static final long serialVersionUID = 3896794185893672627L;

		EmptyCard() {
			setPreferredSize(new Dimension(103, 153));
			setOpaque(false);
		}

		public void paintComponent(Graphics g) {

			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(0, 0, 0, 10));
			g2.fillRoundRect(0, 0, 100, 150, 15, 15);

		}
	}
}
