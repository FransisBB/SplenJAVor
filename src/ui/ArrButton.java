package ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ArrButton extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6664101050767582684L;

	private int width, height, bevel = 1, tOfs = 0;
	private Color bgColor, actColor, bvBot, bvTop, bvTopOrg, bvBotOrg;
	private boolean exited, enabled;
	ImageIcon image;

	public ArrButton(int c, boolean enabled) {
		this.enabled = enabled;
		int alpha = 255;
		if (!enabled)
			alpha = 50;

		bvTopOrg = new Color(255, 255, 255, alpha / 3);
		bvBotOrg = new Color(0, 0, 0, alpha / 5);
		switch (c) {
		case 0:
			// up
			image = getIcon("images/arrowUP.png");
			bgColor = new Color(145, 255, 144, alpha);
			this.width = 27;
			this.height = 18;
			break;
		case 1:
			// down
			image = getIcon("images/arrowDOWN.png");
			bgColor = new Color(255, 145, 144, alpha);
			this.width = 27;
			this.height = 18;
			break;
		case 2:
			// left
			image = getIcon("images/arrowLEFT.png");
			bgColor = new Color(235, 235, 144, alpha);
			this.width = 20;
			this.height = 26;
			break;
		case 3:
			// right
			image = getIcon("images/arrowRIGHT.png");
			bgColor = new Color(235, 235, 144, alpha);
			this.width = 20;
			this.height = 26;
			break;
		default:
			image = getIcon("images/arrowUP.png");
			bgColor = new Color(145, 255, 144, alpha);
			this.width = 27;
			this.height = 18;
			break;
		}
		this.actColor = bgColor;
		this.bvTop = bvTopOrg;
		this.bvBot = bvBotOrg;

		this.setPreferredSize(new Dimension(width + 2 * bevel, height + 2 * bevel));
		this.setOpaque(false);
		if (enabled) {
			setListeners();
		}

	}

	public void setListeners() {
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (me.getX() > 0 && me.getX() < getWidth() && me.getY() > 0 && me.getY() < getHeight()) {
					exited = false;
					bgColor = actColor.darker();
					bvTop = bvBotOrg;
					bvBot = bvTopOrg;
					tOfs = bevel;
					repaint();
				}
			}

			public void mouseReleased(MouseEvent me) {
				if (!exited) {
					bgColor = actColor.brighter();
					bvTop = bvTopOrg;
					bvBot = bvBotOrg;
					tOfs = 0;
					repaint();
				}
			}

			public void mouseEntered(MouseEvent me) {

				bgColor = actColor.brighter();
				bvTop = bvTopOrg;
				bvBot = bvBotOrg;
				tOfs = 0;
				repaint();
			}

			public void mouseDragged(MouseEvent me) {
				bgColor = actColor.darker();
				bvTop = bvBotOrg;
				bvBot = bvTopOrg;
				tOfs = bevel;
				repaint();
			}

			public void mouseExited(MouseEvent me) {
				exited = true;
				bgColor = actColor;
				bvTop = bvTopOrg;
				bvBot = bvBotOrg;
				tOfs = 0;
				repaint();
			}
		});
	}

	public void paintComponent(Graphics g) {
		removeAll();
		int total_width = width;
		int total_height = height;
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(bvTop);
		g2.fillRoundRect(0, 0, total_width + bevel, total_height + bevel, 10, 10);
		g2.fillRoundRect(0, 0, total_width + bevel, total_height + bevel, 10, 10);
		g2.fillRoundRect(0, 0, total_width + bevel, total_height + bevel, 10, 10);
		g2.setColor(bvBot);
		g2.fillRoundRect(bevel * 2, bevel * 2, total_width, total_height, 10, 10);
		g2.fillRoundRect(bevel * 2, bevel * 2, total_width, total_height, 10, 10);
		g2.fillRoundRect(bevel * 2, bevel * 2, total_width, total_height, 10, 10);
		g2.setColor(bgColor);
		g2.fillRoundRect(bevel, bevel, total_width, total_height, 10, 10);
		if (enabled) {
			int x = tOfs+(width + bevel * 2 - image.getIconWidth()) / 2;
			int y = tOfs+(height + bevel * 2 - image.getIconHeight()) / 2;
			BufferedImage dark = dye(image, Color.BLACK);
			BufferedImage light = dye(image, Color.WHITE);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			g2.drawImage(dark, x + 1, y + 1, this);
			g2.drawImage(dark, x, y + 1, this);
			g2.drawImage(light, x, y - 1, this);
			g2.drawImage(light, x - 1, y - 1, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g2.drawImage(dye(image, bgColor.darker()), x, y, this);
		}
		GradientPaint gp1 = new GradientPaint(0, 0, bvTop, 0, total_height, bvBot);
		g2.setPaint(gp1);
		g2.fillRoundRect(bevel, bevel, total_width, total_height, 10, 10);
	}

	public boolean releasedInside() {
		return !exited;
	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	private static BufferedImage dye(ImageIcon imageIcon, Color color) {
		int w = imageIcon.getIconWidth();
		int h = imageIcon.getIconHeight();
		BufferedImage dyed = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = dyed.createGraphics();
		g.drawImage(imageIcon.getImage(), 0, 0, null);
		g.setComposite(AlphaComposite.SrcAtop);
		g.setColor(color);
		g.fillRect(0, 0, w, h);
		g.dispose();
		return dyed;
	}

}
