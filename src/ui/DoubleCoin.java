package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class DoubleCoin extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7947186589971729494L;
	int used;

	public DoubleCoin(int used) {
		this.used = used;
		setPreferredSize(new Dimension(56, 30));
		setSize(new Dimension(56, 30));
		setOpaque(false);
	}

	public DoubleCoin() {
		this.used = 0;
		setPreferredSize(new Dimension(56, 30));
		setSize(new Dimension(56, 30));
		setOpaque(false);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// test rysowania
		Graphics2D g2;
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		Image image;
		image = getIcon("images/colors/yellow.png").getImage();
		// g2.setColor(new Color(255,255,255,150));
		g2.setColor(new Color(255, 255, 175, 150));
		g2.fillRoundRect(1, 1, 54, 28, 24, 24);
		g2.setColor(new Color(0, 0, 0, 75));
		g2.drawRoundRect(1, 1, 54, 28, 24, 24);
		// g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		g2.setColor(new Color(0, 0, 0, 50));
		g2.fillOval(3, 3, 24, 24);
		g2.fillOval(29, 3, 24, 24);
		g2.setColor(new Color(255, 255, 0, 50));
		g2.fillOval(4, 4, 22, 22);
		g2.fillOval(30, 4, 22, 22);
		// g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
		//g2.drawImage(image, 6, 7, (int) (image.getWidth(this) / 1.8), (int) (image.getHeight(this) / 1.8), this);
		//g2.drawImage(image, 32, 7, (int) (image.getWidth(this) / 1.8), (int) (image.getHeight(this) / 1.8), this);
		if (used < 1) {
			//g2.setColor(new Color(255, 220, 220, 200));
			//g2.fillOval(3, 3, 24, 24);
			g2.setColor(new Color(0, 0, 0, 200));
			g2.fillOval(3, 3, 24, 24);
			g2.setColor(new Color(255, 255, 0, 255));
			g2.fillOval(4, 4, 22, 22);
			g2.drawImage(image, 6, 7, (int) (image.getWidth(this) / 1.8), (int) (image.getHeight(this) / 1.8), this);
			//g2.drawImage(image, 32, 7, (int) (image.getWidth(this) / 1.8), (int) (image.getHeight(this) / 1.8), this);
		}
		if (used < 2) {
			//g2.setColor(new Color(255, 220, 220, 200));
			//g2.fillOval(29, 3, 24, 24);
			//g2.drawImage(image, 6, 7, (int) (image.getWidth(this) / 1.8), (int) (image.getHeight(this) / 1.8), this);
			g2.setColor(new Color(0, 0, 0, 200));
			g2.fillOval(29, 3, 24, 24);
			g2.setColor(new Color(255, 255, 0, 255));
			g2.fillOval(30, 4, 22, 22);
			g2.drawImage(image, 32, 7, (int) (image.getWidth(this) / 1.8), (int) (image.getHeight(this) / 1.8), this);
		}
		if (used == 1) {
			g2.setFont(new Font("ARIAL", Font.BOLD, 33));
			g2.setColor(Color.RED);
			g2.drawString("!", 22, 27);
		}

	}
	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}
}
