package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class NoFreeCard extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6918460654784464033L;
	int level;

	public NoFreeCard(int s) {
		this.level = s;
		//setLayout(new FlowLayout());
		setOpaque(false);
		setPreferredSize(new Dimension(100,80));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2;
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setFont(new Font("Verdana", Font.PLAIN, 48));
			g2.setColor(Color.black);
			g2.fillRoundRect(30, 2, 54, 74, 14, 14);
			g2.setColor(Color.gray);
			g2.fillRoundRect(32, 4, 50, 70, 14, 14);
			g2.setColor(Color.lightGray);
			g2.fillRoundRect(32, 4, 50, 20, 14, 14);
			g2.setColor(Color.black);
			g2.drawString("+", 2, 48);
			g2.drawString("+", 2, 52);
			g2.drawString("+", 6, 48);
			g2.drawString("+", 6, 52);

			g2.setColor(Color.white);
			g2.drawString("+", 4, 50);
		if (level == 1) {
			g2.fillOval(48, 50, 16, 16);
		}
		if (level == 2) {
			g2.fillOval(38, 50, 16, 16);
			g2.fillOval(58, 50, 16, 16);
		}	
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke(3));
		g2.drawLine(15, 1, 85, 76);
	}
}
