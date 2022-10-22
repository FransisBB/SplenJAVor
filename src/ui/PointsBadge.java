package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PointsBadge extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7275153942718917577L;
	int n;
	public PointsBadge(int n) {
		this.n=n;
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
		if (n > 9)
			px -= 8;

		// g2.setFont(new Font("Arial", Font.PLAIN, 20));
		g2.setColor(new Color(255, 255, 255, 150));
		g2.drawString(Integer.toString(n), px - 1, py + 0);
		g2.setColor(new Color(0, 0, 0, 150));
		g2.drawString(Integer.toString(n), px + 1, py + 2);
		g2.setColor(new Color(180, 180, 20));
		g2.drawString(Integer.toString(n), px, py + 1);
	}
	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}
}
