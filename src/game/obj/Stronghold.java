package game.obj;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class Stronghold extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3227478497202365043L;
	public int d=9,c=9,r=9;
	public Stronghold() {
		this.setPreferredSize(new Dimension(20, 20));
		this.setOpaque(false);
	}

	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(new Color(50, 100, 50, 200));
			g2d.drawRoundRect(1, 1, 18, 18, 15, 15);
	}
	public void putStronghold(int d, int c, int r) {
		this.d=d;
		this.c=c;
		this.r=r;
	}
	public void takeStronghold() {
		d=9;
		c=9;
		r=9;
	}
}
