package ui;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import game.main.Utils;

public class StrLocalPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6481461963245289673L;
	int n, id;
	//26x30
	public StrLocalPanel(int id, int n) {
		this.n = n;
		this.id = id;
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(20, 72));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Image twr = Utils.scaleIcon(getIcon("images/tower" + id + ".png"), 20, 23).getImage();
		for (int i = 3; i > 0; i--) {
			int x = i+(23*(i-1)); //1,30,1,30,1,30
			if (i>3-n) {
			g2.drawImage(twr, 0, x, this);
			} else {
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
				g2.drawImage(twr, 0, x, this);
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			}
		}

	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

}
