package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import game.main.Utils;

public class NoAriRes extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6430746760543435640L;

	public NoAriRes() {
		//setLayout(new FlowLayout());
		setOpaque(false);
		setPreferredSize(new Dimension(74,70));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2;
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		/*g2.setFont(new Font("Verdana", Font.PLAIN, 48));
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
		}	*/
		g2.setColor(Color.BLACK);
		g2.fillRoundRect(12, 3, 50, 50, 9, 9);
		g2.setPaint(tpDouble("images/aristocrats/ariBackSmall.png", 14, 5));
		//Utils.scaleIcon(getIcon("images/aristocrats/ariBackSmall.png"),70,70);
		//g2.setPaint(Utils.scaleIcon(getIcon("images/aristocrats/ariBackSmall.png"),70,70).getImage(), 20, 4, this);
		// g2.setColor(Color.gray);
		g2.fillRoundRect(14, 5, 46, 46, 9, 9);
		g2.drawImage(Utils.scaleIcon(getIcon("images/cArrow.png"),37,49).getImage(), 1, 20, this);
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke(3));
		g2.drawLine(60, 3, 3, 67);
	}
	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	public TexturePaint tpDouble(String path, int x, int y) {
		Image displayImage = getIcon(path).getImage();
		displayImage = Utils.scaleIcon(getIcon("images/aristocrats/ariBackSmall.png"),displayImage.getWidth(this)*4/3,displayImage.getHeight(this)*4/3).getImage();

		BufferedImage image = new BufferedImage(displayImage.getWidth(this), displayImage.getHeight(this),
				BufferedImage.TYPE_INT_ARGB);
		image.createGraphics().drawImage(displayImage, 0, 0, this);

		Rectangle2D rectangle = new Rectangle2D.Float(x, y, displayImage.getWidth(this), displayImage.getHeight(this));

		return new TexturePaint(image, rectangle);
	}
}
