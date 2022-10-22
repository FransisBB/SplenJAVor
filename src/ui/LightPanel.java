package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class LightPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1523138164369496545L;
	//int w, h;
	public boolean highlighted = false,selected = false;

	public LightPanel(int w, int h) {
		//this.w = w;
		//this.h = h;
		this.setPreferredSize(new Dimension(w, h));
		this.setOpaque(false);
	}

	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color bvTop,bvBot,bgColor;
		int alpha;
		if (selected) {
			alpha=200;
			//g2d.setColor(new Color(50, 100, 50, 200));
			//g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);
			//g2d.setColor(new Color(240, 255, 240, 225));
		} else if (highlighted) {
			alpha=125;
			//g2d.setColor(new Color(0, 0, 0, 75));
			//g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);
			//g2d.setColor(new Color(240, 255, 240, 150));
		} else {
			alpha=50;
			//g2d.setColor(new Color(0, 0, 0, 50));
			//g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);
			//g2d.setColor(new Color(255, 255, 255, 75));
		}
		//g2d.fillRoundRect(2, 2, getWidth() - 3, getHeight() - 3, 15, 15);
		bgColor = new Color(255, 255, 0,alpha);
		bvTop = new Color(255, 255, 255, alpha / 4);
		bvBot = new Color(0, 0, 0, alpha / 6);
		int total_width = getWidth()-2;
		int total_height = getHeight()-2;
		int bevel=1;
		int round=40;
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(bvTop);
		g2.fillRoundRect(0, 0, total_width + bevel, total_height + bevel, round, round/2);
		g2.fillRoundRect(0, 0, total_width + bevel, total_height + bevel, round, round/2);
		g2.fillRoundRect(0, 0, total_width + bevel, total_height + bevel, round, round/2);
		g2.setColor(bvBot);
		g2.fillRoundRect(bevel * 2, bevel * 2, total_width, total_height, round, round/2);
		g2.fillRoundRect(bevel * 2, bevel * 2, total_width, total_height, round, round/2);
		g2.fillRoundRect(bevel * 2, bevel * 2, total_width, total_height, round, round/2);
		g2.setColor(bgColor);
		g2.fillRoundRect(bevel, bevel, total_width, total_height, round, round/2);
		GradientPaint gp1 = new GradientPaint(0, 0, bvTop, 0, total_height, bvBot);
		g2.setPaint(gp1);
		g2.fillRoundRect(bevel, bevel, total_width, total_height, round, round/2);
		g2d.dispose();

	}
}
