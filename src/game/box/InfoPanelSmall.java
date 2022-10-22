package game.box;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ui.Logo;

public class InfoPanelSmall extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1879496925616583524L;

	public InfoPanelSmall() {
		setLayout(null);
		setOpaque(false);
	}
	public void draw(String ver) {
		Logo logo = new Logo(30);
		logo.setBounds(0,0,180,getHeight()-3);
		add(logo);
		JLabel copyr = new JLabel("<html>©2020 by Fransis<br>"+ver+"<html>");
		copyr.setBounds(185,0,getWidth()-185, getHeight()-3);
		copyr.setFont(new Font("Arial", Font.BOLD, 12));
		copyr.setForeground(new Color (180,180,100));
		copyr.setHorizontalAlignment(SwingConstants.LEFT);
		copyr.setVerticalAlignment(SwingConstants.CENTER);
		add(copyr);
	}
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(new Color(0,0,0, 30));
		g2d.fillRoundRect(-30, 1, getWidth()+30, getHeight()-3, 30,30);
		g2d.setColor(new Color(255,255,240,180));
		g2d.fillRoundRect(-31, 0, getWidth()+30, getHeight()-3, 30,30);
		g2d.dispose();

	}
}
