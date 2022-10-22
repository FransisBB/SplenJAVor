package game.obj;

import java.util.*;

public class Deck {

	public List<Card> _cardList = new ArrayList<Card>();
	public int rows, cols;
	public boolean upsidedown = true;
	public int seed;

	public Deck(String name, int seed) {
		if (name == "standard") {
			int w = 0, g = 0, bl = 0, bk = 0, r = 0, id = 0;
			_cardList.addAll(
					Arrays.asList(new Card("black" + (bk++) + ".jpg", "standard", id++, 0, 3, 1, 0, 0, 1, 1, 1, 0, 1),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 0, 3, 1, 0, 0, 1, 1, 2, 0, 1),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 0, 3, 1, 0, 0, 0, 2, 2, 0, 1),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 0, 3, 1, 0, 0, 1, 0, 0, 1, 3),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 0, 3, 1, 0, 0, 2, 0, 0, 0, 1),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 0, 3, 1, 0, 0, 2, 2, 0, 0, 0),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 0, 3, 1, 0, 0, 3, 0, 0, 0, 0),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 0, 3, 1, 0, 1, 0, 0, 4, 0, 0),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 0, 2, 1, 0, 0, 1, 1, 0, 1, 1),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 0, 2, 1, 0, 0, 1, 1, 0, 1, 2),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 0, 2, 1, 0, 0, 2, 1, 0, 0, 2),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 0, 2, 1, 0, 0, 3, 0, 1, 0, 1),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 0, 2, 1, 0, 0, 0, 1, 0, 2, 0),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 0, 2, 1, 0, 0, 2, 0, 0, 2, 0),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 0, 2, 1, 0, 0, 0, 0, 0, 3, 0),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 0, 2, 1, 0, 1, 0, 0, 0, 0, 4),
							new Card("white" + (g++) + ".jpg", "standard", id++, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1),
							new Card("white" + (g++) + ".jpg", "standard", id++, 0, 1, 1, 0, 0, 2, 0, 1, 1, 1),
							new Card("white" + (g++) + ".jpg", "standard", id++, 0, 1, 1, 0, 0, 2, 0, 2, 1, 0),
							new Card("white" + (g++) + ".jpg", "standard", id++, 0, 1, 1, 0, 0, 0, 3, 1, 1, 0),
							new Card("white" + (g++) + ".jpg", "standard", id++, 0, 1, 1, 0, 0, 0, 0, 0, 1, 2),
							new Card("white" + (g++) + ".jpg", "standard", id++, 0, 1, 1, 0, 0, 0, 0, 2, 2, 0),
							new Card("white" + (g++) + ".jpg", "standard", id++, 0, 1, 1, 0, 0, 0, 0, 3, 0, 0),
							new Card("white" + (g++) + ".jpg", "standard", id++, 0, 1, 1, 0, 1, 4, 0, 0, 0, 0),
							new Card("green" + (w++) + ".jpg", "standard", id++, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1),
							new Card("green" + (w++) + ".jpg", "standard", id++, 0, 0, 1, 0, 0, 0, 1, 1, 2, 1),
							new Card("green" + (w++) + ".jpg", "standard", id++, 0, 0, 1, 0, 0, 0, 0, 1, 2, 2),
							new Card("green" + (w++) + ".jpg", "standard", id++, 0, 0, 1, 0, 0, 1, 1, 3, 0, 0),
							new Card("green" + (w++) + ".jpg", "standard", id++, 0, 0, 1, 0, 0, 0, 2, 1, 0, 0),
							new Card("green" + (w++) + ".jpg", "standard", id++, 0, 0, 1, 0, 0, 0, 0, 2, 0, 2),
							new Card("green" + (w++) + ".jpg", "standard", id++, 0, 0, 1, 0, 0, 0, 0, 0, 0, 3),
							new Card("green" + (w++) + ".jpg", "standard", id++, 0, 0, 1, 0, 1, 0, 0, 0, 4, 0),
							new Card("red" + (r++) + ".jpg", "standard", id++, 0, 4, 1, 0, 0, 1, 1, 1, 1, 0),
							new Card("red" + (r++) + ".jpg", "standard", id++, 0, 4, 1, 0, 0, 1, 2, 1, 1, 0),
							new Card("red" + (r++) + ".jpg", "standard", id++, 0, 4, 1, 0, 0, 1, 2, 0, 2, 0),
							new Card("red" + (r++) + ".jpg", "standard", id++, 0, 4, 1, 0, 0, 0, 1, 0, 3, 1),
							new Card("red" + (r++) + ".jpg", "standard", id++, 0, 4, 1, 0, 0, 1, 0, 2, 0, 0),
							new Card("red" + (r++) + ".jpg", "standard", id++, 0, 4, 1, 0, 0, 0, 2, 0, 0, 2),
							new Card("red" + (r++) + ".jpg", "standard", id++, 0, 4, 1, 0, 0, 0, 3, 0, 0, 0),
							new Card("red" + (r++) + ".jpg", "standard", id++, 0, 4, 1, 0, 1, 0, 4, 0, 0, 0),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 1, 3, 1, 0, 1, 2, 3, 2, 0, 0),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 1, 3, 1, 0, 1, 3, 3, 0, 2, 0),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 1, 3, 1, 0, 2, 4, 0, 1, 0, 2),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 1, 3, 1, 0, 2, 5, 0, 0, 0, 3),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 1, 3, 1, 0, 2, 0, 5, 0, 0, 0),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 1, 3, 1, 0, 3, 0, 0, 0, 6, 0),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 1, 2, 1, 0, 1, 2, 0, 2, 0, 3),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 1, 2, 1, 0, 1, 3, 0, 2, 3, 0),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 1, 2, 1, 0, 2, 0, 5, 3, 0, 0),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 1, 2, 1, 0, 2, 0, 2, 0, 4, 1),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 1, 2, 1, 0, 2, 0, 0, 5, 0, 0),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 1, 2, 1, 0, 3, 0, 0, 6, 0, 0),
							new Card("white" + (g++) + ".jpg", "standard", id++, 1, 1, 1, 0, 1, 3, 0, 0, 2, 2),
							new Card("white" + (g++) + ".jpg", "standard", id++, 1, 1, 1, 0, 1, 0, 2, 3, 0, 3),
							new Card("white" + (g++) + ".jpg", "standard", id++, 1, 1, 1, 0, 2, 1, 0, 0, 2, 4),
							new Card("white" + (g++) + ".jpg", "standard", id++, 1, 1, 1, 0, 2, 0, 0, 0, 3, 5),
							new Card("white" + (g++) + ".jpg", "standard", id++, 1, 1, 1, 0, 2, 0, 0, 0, 0, 5),
							new Card("white" + (g++) + ".jpg", "standard", id++, 1, 1, 1, 0, 3, 0, 6, 0, 0, 0),
							new Card("green" + (w++) + ".jpg", "standard", id++, 1, 0, 1, 0, 1, 2, 3, 0, 0, 3),
							new Card("green" + (w++) + ".jpg", "standard", id++, 1, 0, 1, 0, 1, 0, 2, 3, 2, 0),
							new Card("green" + (w++) + ".jpg", "standard", id++, 1, 0, 1, 0, 2, 0, 4, 2, 1, 0),
							new Card("green" + (w++) + ".jpg", "standard", id++, 1, 0, 1, 0, 2, 3, 0, 5, 0, 0),
							new Card("green" + (w++) + ".jpg", "standard", id++, 1, 0, 1, 0, 2, 5, 0, 0, 0, 0),
							new Card("green" + (w++) + ".jpg", "standard", id++, 1, 0, 1, 0, 3, 6, 0, 0, 0, 0),
							new Card("red" + (r++) + ".jpg", "standard", id++, 1, 4, 1, 0, 1, 0, 2, 0, 3, 2),
							new Card("red" + (r++) + ".jpg", "standard", id++, 1, 4, 1, 0, 1, 0, 0, 3, 3, 2),
							new Card("red" + (r++) + ".jpg", "standard", id++, 1, 4, 1, 0, 2, 2, 1, 4, 0, 0),
							new Card("red" + (r++) + ".jpg", "standard", id++, 1, 4, 1, 0, 2, 0, 3, 0, 5, 0),
							new Card("red" + (r++) + ".jpg", "standard", id++, 1, 4, 1, 0, 2, 0, 0, 0, 5, 0),
							new Card("red" + (r++) + ".jpg", "standard", id++, 1, 4, 1, 0, 3, 0, 0, 0, 0, 6),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 2, 3, 1, 0, 3, 5, 3, 3, 0, 3),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 2, 3, 1, 0, 4, 0, 0, 0, 0, 7),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 2, 3, 1, 0, 4, 3, 0, 0, 3, 6),
							new Card("black" + (bk++) + ".jpg", "standard", id++, 2, 3, 1, 0, 5, 0, 0, 0, 3, 7),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 2, 2, 1, 0, 3, 3, 3, 0, 5, 3),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 2, 2, 1, 0, 4, 0, 7, 0, 0, 0),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 2, 2, 1, 0, 4, 0, 6, 3, 3, 0),
							new Card("blue" + (bl++) + ".jpg", "standard", id++, 2, 2, 1, 0, 5, 0, 7, 3, 0, 0),
							new Card("white" + (g++) + ".jpg", "standard", id++, 2, 1, 1, 0, 3, 3, 0, 3, 3, 5),
							new Card("white" + (g++) + ".jpg", "standard", id++, 2, 1, 1, 0, 4, 0, 0, 0, 7, 0),
							new Card("white" + (g++) + ".jpg", "standard", id++, 2, 1, 1, 0, 4, 0, 3, 0, 6, 3),
							new Card("white" + (g++) + ".jpg", "standard", id++, 2, 1, 1, 0, 5, 0, 3, 0, 7, 0),
							new Card("green" + (w++) + ".jpg", "standard", id++, 2, 0, 1, 0, 3, 0, 5, 3, 3, 3),
							new Card("green" + (w++) + ".jpg", "standard", id++, 2, 0, 1, 0, 4, 0, 0, 7, 0, 0),
							new Card("green" + (w++) + ".jpg", "standard", id++, 2, 0, 1, 0, 4, 3, 3, 6, 0, 0),
							new Card("green" + (w++) + ".jpg", "standard", id++, 2, 0, 1, 0, 5, 3, 0, 7, 0, 0),
							new Card("red" + (r++) + ".jpg", "standard", id++, 2, 4, 1, 0, 3, 3, 3, 5, 3, 0),
							new Card("red" + (r++) + ".jpg", "standard", id++, 2, 4, 1, 0, 4, 7, 0, 0, 0, 0),
							new Card("red" + (r++) + ".jpg", "standard", id++, 2, 4, 1, 0, 4, 6, 0, 3, 0, 3),
							new Card("red" + (r++) + ".jpg", "standard", id++, 2, 4, 1, 0, 5, 7, 0, 0, 0, 3)

					));
			rows = 3;
			cols = 5;
			this.seed = seed;
		}
		if (name == "orient") {
			int id = 100;
			int n = 0;
			_cardList.addAll(Arrays.asList(// L, C, V, S, 0, 1, 2, 3, 4, 5, C
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 0, 4, 0, 0, 3, 0, 0, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 0, 4, 0, 0, 0, 3, 0, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 0, 4, 0, 3, 0, 0, 0, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 0, 4, 0, 0, 0, 0, 0, 3),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 0, 4, 0, 0, 0, 0, 3, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 1, 1, 0, 0, 3, 0, 0, 2),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 1, 1, 0, 2, 0, 3, 0, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 1, 1, 0, 3, 0, 0, 2, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 1, 1, 0, 0, 0, 2, 0, 3),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 1, 1, 0, 0, 2, 0, 3, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 1, 1, 6, 1, 2, 0, 2, 2, 2),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 0, 1, 6, 1, 0, 2, 2, 2, 2),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 4, 1, 6, 1, 2, 2, 2, 2, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 0, 1, 2, 0, 0, 4, 3, 1, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 0, 1, 2, 0, 4, 0, 0, 1, 3),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 0, 2, 0, 0, 0, 4, 0, 0, 3),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 1, 2, 0, 0, 3, 0, 4, 0, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 4, 2, 0, 0, 4, 0, 0, 3, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 3, 2, 0, 0, 3, 0, 0, 0, 4),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 2, 2, 0, 0, 0, 3, 0, 4, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 2, 1, 5, 3, 0, 0, 0, 0, 0, 1),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 0, 1, 5, 3, 0, 0, 0, 0, 0, 2),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 4, 1, 5, 3, 0, 0, 0, 0, 0, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 3, 1, 5, 3, 0, 0, 0, 0, 0, 4),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 1, 1, 5, 3, 0, 0, 0, 0, 0, 3),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 3, 1, 3, 0, 0, 6, 3, 1, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 1, 1, 3, 0, 3, 1, 6, 0, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 2, 1, 3, 0, 6, 0, 1, 0, 3),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 0, 1, 3, 0, 1, 0, 0, 3, 6),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 4, 1, 3, 0, 0, 3, 0, 6, 1)

			));
			rows = 3;
			cols = 3;
			this.seed = seed;
		}
		if (name == "orientNoAri") {
			int id = 100;
			int n = 0;
			_cardList.addAll(Arrays.asList(// L, C, V, S, 0, 1, 2, 3, 4, 5, C
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 0, 4, 0, 0, 3, 0, 0, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 0, 4, 0, 0, 0, 3, 0, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 0, 4, 0, 3, 0, 0, 0, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 0, 4, 0, 0, 0, 0, 0, 3),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 0, 4, 0, 0, 0, 0, 3, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 1, 1, 0, 0, 3, 0, 0, 2),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 1, 1, 0, 2, 0, 3, 0, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 1, 1, 0, 3, 0, 0, 2, 0),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 1, 1, 0, 0, 0, 2, 0, 3),
					new Card("orient" + (n++) + ".jpg", "orient", id++, 0, 0, 1, 1, 0, 0, 2, 0, 3, 0)));
			n += 3;
			// new Card("orient"+(n++)+".jpg", "orient",id++, 1, 1, 1, 6, 1, 2, 0, 2, 2, 2),
			// new Card("orient"+(n++)+".jpg", "orient",id++, 1, 0, 1, 6, 1, 0, 2, 2, 2, 2),
			// new Card("orient"+(n++)+".jpg", "orient",id++, 1, 4, 1, 6, 1, 2, 2, 2, 2, 0),
			
			_cardList.addAll(Arrays.asList(// L, C, V, S, 0, 1, 2, 3, 4, 5, C
							new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 0, 1, 2, 0, 0, 4, 3, 1, 0),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 0, 1, 2, 0, 4, 0, 0, 1, 3),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 0, 2, 0, 0, 0, 4, 0, 0, 3),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 1, 2, 0, 0, 3, 0, 4, 0, 0),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 4, 2, 0, 0, 4, 0, 0, 3, 0),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 3, 2, 0, 0, 3, 0, 0, 0, 4),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 1, 2, 2, 0, 0, 0, 3, 0, 4, 0),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 2, 1, 5, 3, 0, 0, 0, 0, 0, 1),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 0, 1, 5, 3, 0, 0, 0, 0, 0, 2),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 4, 1, 5, 3, 0, 0, 0, 0, 0, 0),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 3, 1, 5, 3, 0, 0, 0, 0, 0, 4),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 1, 1, 5, 3, 0, 0, 0, 0, 0, 3),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 3, 1, 3, 0, 0, 6, 3, 1, 0),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 1, 1, 3, 0, 3, 1, 6, 0, 0),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 2, 1, 3, 0, 6, 0, 1, 0, 3),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 0, 1, 3, 0, 1, 0, 0, 3, 6),
							new Card("orient" + (n++) + ".jpg", "orient", id++, 2, 4, 1, 3, 0, 0, 3, 0, 6, 1)

					));
			rows = 3;
			cols = 3;
			this.seed = seed;
		}

	}

	public int[][] splitCards() {
		int[][] piles;
		int[] cardsInRow;
		piles = new int[rows][];
		cardsInRow = new int[rows];
		int bwr = 0;
		if (upsidedown)
			bwr = rows - 1; // tworzenie licznika kart dla ka¿dej z kupek, do u¿ycia potem
		for (int i = 0; i < rows; i++)
			cardsInRow[i] = 0;
		for (Card t : _cardList)
			cardsInRow[Math.abs(bwr - t.level)]++;

		for (int i = 0; i < rows; i++)
			piles[Math.abs(bwr - i)] = new int[cardsInRow[Math.abs(bwr - i)]];

		Collections.shuffle(_cardList, new Random(seed)); // tasowanie

		int i = 0;
		for (Card t : _cardList) { // rozdzielanie kart na kupki, tj. deck[kupka][która to karta]=index karty z
									// _cardList
			cardsInRow[Math.abs(bwr - t.level)]--;
			piles[Math.abs(bwr - t.level)][cardsInRow[Math.abs(bwr - t.level)]] = i;
			i++;
		}
		return piles;
	}

	Card getCard(int i) {
		return _cardList.get(i);
	}
}
