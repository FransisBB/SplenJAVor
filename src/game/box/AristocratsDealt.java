package game.box;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import game.obj.Aristocrat;

public class AristocratsDealt extends JPanel {
	private static final long serialVersionUID = 6911997279129566823L;

	public List<Aristocrat> _arilist = new ArrayList<Aristocrat>();
public int ariNum;
	public AristocratsDealt(int ariNum, int seed, boolean dbl) {
		this.ariNum = ariNum;
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(layout);
		setOpaque(false);
		Integer[] intArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
				24 };

		List<Integer> intList = Arrays.asList(intArray);

		Collections.shuffle(intList, new Random(seed));
		int ia = 0;
		intList.toArray(intArray);
		_arilist.addAll(Arrays.asList(new Aristocrat("animal" + (intArray[ia++]) + ".jpg", 3, 0, 3, 3, 3, 0),
				new Aristocrat("animal" + (intArray[ia++]) + ".jpg", 3, 3, 0, 3, 0, 3),
				new Aristocrat("animal" + (intArray[ia++]) + ".jpg", 3, 0, 3, 0, 3, 3),
				new Aristocrat("animal" + (intArray[ia++]) + ".jpg", 3, 4, 0, 0, 0, 4),
				new Aristocrat("animal" + (intArray[ia++]) + ".jpg", 3, 4, 0, 4, 0, 0),
				new Aristocrat("animal" + (intArray[ia++]) + ".jpg", 3, 0, 0, 0, 4, 4),
				new Aristocrat("animal" + (intArray[ia++]) + ".jpg", 3, 0, 4, 0, 4, 0),
				new Aristocrat("animal" + (intArray[ia++]) + ".jpg", 3, 3, 3, 3, 0, 0),
				new Aristocrat("animal" + (intArray[ia++]) + ".jpg", 3, 3, 0, 0, 3, 3),
				new Aristocrat("animal" + (intArray[ia++]) + ".jpg", 3, 0, 4, 4, 0, 0)));

		Collections.shuffle(_arilist, new Random(seed));
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		// playerNum = 2;
		for (int i = 0; i < ariNum * 2+1; i++) {
			gbc.gridy = i + 1;
			gbc.weighty = 1;
			if (i % 2 == 0) {
				JPanel filler = new JPanel();
				filler.setOpaque(false);
				// filler.setOpaque(false);
				if (dbl && ariNum == 3) {
					if (i == 0) {
						JPanel filler2 = new JPanel();
						filler2.setOpaque(false);
						gbc.gridy = 0;
						add(filler2, gbc);
						gbc.gridy = 1;
						gbc.weighty = 0;
						filler.setPreferredSize(new Dimension(88, 88));
					}
					if (i != 0 && i != 6) {
						gbc.weighty = 0;
						filler.setPreferredSize(new Dimension(88, 60));
					}
				}
				if (ariNum == 4) {
					if (i != 0 && i != 8) {
						gbc.weighty = 0;
						filler.setPreferredSize(new Dimension(88, 40));
					}
				}
				if (ariNum == 5) {
					gbc.weighty = 0;
					filler.setPreferredSize(new Dimension(88, 8));
				}
				add(filler, gbc);
			} else {
				gbc.weighty = 0;
				add(new EmptyAris(), gbc, 0);
				add(_arilist.get((int) Math.floor(i / 2)), gbc, 0);
			}
		}

	}

	public void removeAllActions() {
		for (int i = 0; i < ariNum; i++) {
			_arilist.get(i).removeAllActions();
		}
	}

	public boolean checkIfTake(int[] cards, int i, int id) {
		boolean check = true;
		Aristocrat ari = _arilist.get(i);
		if ((ari.isAvailable && !ari.isReserved) || (ari.isAvailable && ari.isReserved && ari.resID == id)) {
			for (int j = 0; j < 5; j++) {
				if (ari.req[j] > cards[j]) {
					check = false;
					break;
				}
			}
		} else
			check = false;
		// if (check)
		// _arilist.get(i).isAvailable(false);
		return check;
	}

	private class EmptyAris extends JPanel {
		private static final long serialVersionUID = -6778653650877696040L;

		EmptyAris() {
			setPreferredSize(new Dimension(88, 88));
			setBackground(new Color(255, 255, 255, 0));
		}

		public void paintComponent(Graphics g) {

			super.paintComponent(g);
			// test rysowania
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(0, 0, 0, 10));
			g2.fillRoundRect(0, 0, 85, 85, 15, 15);

		}
	}
}
