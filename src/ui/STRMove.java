package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import game.main.Utils;

public class STRMove extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6481461963245289673L;
	int n;
	public STRMove(int n) {
		this.n=n;
		//arrow 120x200 -> 18x30
		//str 26x30
		//2+18+2+26+2+18+2
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(75,34));
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Image arrow = Utils.scaleIcon(getIcon("images/STRMove.png"), 18, 30).getImage();
		g2.setColor(new Color(255,255,255,200));
		g2.fillRoundRect(0,0,75,34,10,10);
		g2.drawImage(arrow, 4, 2, this);
		g2.drawImage(getIcon("images/tower"+n+".png").getImage(), 24, 2, this);
		g2.drawImage(arrow, 55, 2, this);

	}
	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

}
