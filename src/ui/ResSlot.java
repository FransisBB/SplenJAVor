package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class ResSlot extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4032331948455707947L;

	public ResSlot() {
		setLayout(null);
		setBounds(0, 0, 50, 75);
		setOpaque(false);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2;
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color(0, 0, 0, 25));
		g2.fillRoundRect(0, 0, 48, 73, 10, 10);
	}
}
