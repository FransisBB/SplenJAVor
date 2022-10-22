package game.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.box.CardsDealt;
import game.main.Board;

public class ColorSelectPanel2 extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2633538609067144658L;
	public int selColor = 0;
	// JTextField inputNick = new JTextField(13);

	public ColorSelectPanel2(Board board) {
		CardsDealt []cardsDealt=board.cardsDealt;
		int[] production=board.players[board.aPID].ownedCards;
		setOpaque(false);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		add(new JLabel(board.lt("you_select_color")), gbc);
		gbc.gridy = 1;
		// gbc.gridwidth = 1;
		JPanel temp = new JPanel();
		gbc.insets = new Insets(2, 2, 2, 2);
		JLabel[] color = new JLabel[5];
		Color cl = Color.red;
		boolean ft = true;
		for (int i = 0; i < 5; i++) {

			if (production[i] > 0 || (cardsDealt[1].cancelPossible[0] && cardsDealt[1].getActCard(0).color == i
					&& cardsDealt[1].getActCard(0).special == 3)) {
				if (i == 0) {
					color[i] = new JLabel(getIcon("images/colors/green.png"));
					cl = Color.green;
				} else if (i == 1) {
					color[i] = new JLabel(getIcon("images/colors/white.png"));
					cl = Color.white;
				} else if (i == 2) {
					color[i] = new JLabel(getIcon("images/colors/blue.png"));
					cl = Color.blue;
				} else if (i == 3) {
					color[i] = new JLabel(getIcon("images/colors/black.png"));
					cl = Color.black;
				} else if (i == 4) {
					color[i] = new JLabel(getIcon("images/colors/red.png"));
					cl = Color.red;
				}
				int k = i;
				Color clT = cl;
				color[i].addMouseListener(new MouseAdapter() {

					public void mousePressed(MouseEvent me) {
						for (int j = 0; j < 5; j++) {
							if (production[j] > 0
									|| (cardsDealt[1].cancelPossible[0] && cardsDealt[1].getActCard(0).color == j
											&& cardsDealt[1].getActCard(0).special == 3)) {
								color[j].setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 75), 2));
							}
						}
						color[k].setBorder(BorderFactory.createLineBorder(clT, 2));
						selColor = k;
						// color[i].setBackground(new Color(200, 200, 255)); // gender = 1; repaint();
						revalidate();
					}
				});
				if (ft) {
					color[i].setBorder(BorderFactory.createLineBorder(clT, 2));
					selColor = i;
					ft = false;
				} else {
					color[i].setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 75), 2));
				}
			} else {
				if (i == 0) {
					color[i] = new JLabel(getIcon("images/colors/greenGS.png"));
				} else if (i == 1) {
					color[i] = new JLabel(getIcon("images/colors/whiteGS.png"));
				} else if (i == 2) {
					color[i] = new JLabel(getIcon("images/colors/blueGS.png"));
				} else if (i == 3) {
					color[i] = new JLabel(getIcon("images/colors/blackGS.png"));
				} else if (i == 4) {
					color[i] = new JLabel(getIcon("images/colors/redGS.png"));
				}
			}

			temp.add(color[i]);
		}
		add(temp, gbc);
	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

}
