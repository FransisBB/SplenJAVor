package game.box;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ui.Logo;

public class InfoPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1879496925616583524L;

	public InfoPanel(String ver) {
		setLayout(new BorderLayout());
		//setPreferredSize(new Dimension(playersPanel.getWidth(), localPlayerPanel.getHeight()));
		setOpaque(false);
		Logo logo = new Logo(50);
		logo.setPreferredSize(new Dimension(getWidth(), 62));
		add(logo, BorderLayout.CENTER);
		JLabel copyr = new JLabel("<html><center>Copyright © 2020 Fransis<br>fransis1989@gmail.com<br>"+ver+"</center><html>");
		copyr.setFont(new Font("Arial", Font.BOLD, 12));
		copyr.setForeground(new Color (180,180,100));
		copyr.setBorder(new EmptyBorder(0,0,15,0));
		copyr.setHorizontalAlignment(SwingConstants.CENTER);
		copyr.setVerticalAlignment(SwingConstants.TOP);
		add(copyr, BorderLayout.SOUTH);
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
