package game.box;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import game.obj.City;

public class CitiesDealt extends JPanel {
	private static final long serialVersionUID = 6911997279129566823L;

	public List<City> _citylist = new ArrayList<City>();

	public CitiesDealt(int playerNum, int seed, boolean dbl) {
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(layout);
		setOpaque(false);
		Integer[] intArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
				24 };

		List<Integer> intList = Arrays.asList(intArray);

		Collections.shuffle(intList, new Random(seed));
		intList.toArray(intArray);
		_citylist.addAll(Arrays.asList(
				// green,white,blue,black,red,gray
				new City("city0.jpg", 0, 11, 0, 3, 3, 3, 3, 0), new City("city0.jpg", 0, 11, 3, 3, 0, 3, 3, 0),

				new City("city1.jpg", 1, 12, 0, 0, 0, 0, 0, 6), new City("city1.jpg", 1, 15, 0, 0, 0, 0, 0, 5),

				new City("city2.jpg", 2, 13, 0, 0, 0, 3, 4, 0), new City("city2.jpg", 2, 13, 0, 3, 4, 0, 0, 0),

				new City("city3.jpg", 3, 13, 4, 0, 3, 0, 0, 0), new City("city3.jpg", 3, 14, 4, 0, 0, 0, 0, 4),

				new City("city4.jpg", 4, 13, 0, 4, 0, 0, 3, 0), new City("city4.jpg", 4, 13, 3, 0, 0, 4, 0, 0),

				new City("city5.jpg", 5, 13, 2, 2, 2, 2, 2, 0), new City("city5.jpg", 5, 14, 1, 2, 1, 2, 2, 0),

				new City("city6.jpg", 6, 16, 1, 1, 1, 1, 1, 0), new City("city6.jpg", 6, 17, 0, 0, 0, 0, 0, 0)
		// green,white,blue,black,red,gray
		));

		Collections.shuffle(_citylist, new Random(seed));

		List<City> _templist = new ArrayList<City>();
		int c = 0;
		int n = 0;
		while (c <= 2) {
			boolean add = true;
			for (int i = 0; i < c; i++) {
				if (_templist.get(i).tile == _citylist.get(n).tile)
					add = false;
			}
			if (add) {
				_templist.add(_citylist.get(n));
				c++;
			}
			n++;
		}
		_citylist = _templist;

		// gbc.gridx = 0;
		// for (int i = 0; i <= 2; i++) {
		// gbc.gridy = i;
		// add(new EmptyCity(), gbc, 0);
		// add(_citylist.get(i), gbc, 0);
		// }
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		//playerNum =2;
		for (int i = 0; i <= 6; i++) {
			gbc.gridy = i+1;
			gbc.weighty = 1;
			if (i % 2 == 0) {
				JPanel filler = new JPanel();
				filler.setOpaque(false);
				if (dbl) {
					if (playerNum == 2) {
						if (i==6) {
							JPanel filler2 = new JPanel();
							filler2.setOpaque(false);
							gbc.gridy = 8;
							add(filler2,gbc);
							gbc.gridy = 7;
						}
						if (i!=0) {
							gbc.weighty = 0;
							filler.setPreferredSize(new Dimension(88,60));
						}
					}
					if (playerNum == 3) {
						if (i!=0 && i!=6) {
							gbc.weighty = 0;
							filler.setPreferredSize(new Dimension(88,40));
						}
					}
					if (playerNum>=4) {
						if (i==0) {
							JPanel filler2 = new JPanel();
							filler2.setOpaque(false);
							gbc.gridy = 0;
							add(filler2, gbc);
							gbc.gridy = 1;
							gbc.weighty = 0;
							filler.setPreferredSize(new Dimension(88,88+8));
						}
						if (i!=0 && i!=6) {
							gbc.weighty = 0;
							filler.setPreferredSize(new Dimension(88,8));
						}
						
					}
				}
				add(filler, gbc);
			} else {
				gbc.weighty = 0;
				add(_citylist.get((int) Math.floor(i / 2)), gbc);
			}
		}

	}

	public void removeAllActions(int n) {
		for (int i = 0; i <= n; i++) {
			_citylist.get(i).removeAllActions();
		}
	}

	public boolean checkIfTake(int[] cards, int points, int i) {
		boolean[] used = new boolean[5];
		for (int k = 0; k < 5; k++)
			used[k] = false;
		boolean check = true;
		if (_citylist.get(i).isAvailable && points >= _citylist.get(i).reqPoints) {
			for (int j = 0; j < 5; j++) {
				if (_citylist.get(i).req[j] > cards[j]) {
					check = false;
					break;
				} else if (_citylist.get(i).req[j] > 0)
					used[j] = true;
			}

			if (check && _citylist.get(i).req[5] > 0) {
				check = false;
				for (int j = 0; j < 5; j++) {
					if (!used[j] && cards[j] >= _citylist.get(i).req[5]) {
						check = true;
						break;
					}
				}
			}
		} else
			check = false;
		// if (check)
		// _arilist.get(i).isAvailable(false);
		return check;
	}

	
}
