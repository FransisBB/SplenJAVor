package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import game.obj.Card;

public class SmallReverse extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6714551417658865817L;
		String path;
		Card card;
		public SmallReverse(Card card) {
			this.card=card;
			if (card.type == "standard") {
				switch (card.level) {
				case 0:
					path = "images/cards/green.jpg";
					break;
				case 1:
					path = "images/cards/yellow.jpg";
					break;
				case 2:
					path = "images/cards/blue.jpg";
					break;
				default:
					path = "images/cards/green.jpg";
				}
			} else if (card.type == "orient") {
				switch (card.level) {
				case 0:
					path = "images/cards/greenOrient.jpg";
					break;
				case 1:
					path = "images/cards/yellowOrient.jpg";
					break;
				case 2:
					path = "images/cards/blueOrient.jpg";
					break;
				default:
					path = "images/cards/greenOrient.jpg";
				}
			}
			setLayout(null);
			setBounds(0, 0, 50, 75);
			setOpaque(false);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2;
			g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(0, 0, 0, 75));
			g2.fillRoundRect(2, 2, 48, 73, 10, 10);
			BufferedImage image = new BufferedImage(48, 73, BufferedImage.TYPE_INT_RGB);
			image.createGraphics().drawImage(getIcon(path).getImage(), 0, 0, 48, 73, null);
			Rectangle2D rectangle = new Rectangle2D.Float(0, 0, 48, 73);
			g2.setPaint(new TexturePaint(image, rectangle));
			// g2.drawImage(getIcon(path).getImage(), 0, 0, 50, 75, null);
			// g2.roun
			g2.fillRoundRect(0, 0, 48, 73, 10, 10);
			g2.setFont(new Font("Verdana", Font.BOLD, 7));
			g2.setColor(new Color(255, 255, 255, 150));
			g2.drawString("SplenJAVor", 2, 20);
			g2.setColor(new Color(255, 255, 255, 100));
			if (card.level == 0) {
				g2.fillOval(24 - 4, 60, 8, 8);
			}
			if (card.level == 1) {
				g2.fillOval(24 - 4 - 6, 60, 8, 8);
				g2.fillOval(24 - 4 + 6, 60, 8, 8);
			}
			if (card.level == 2) {
				g2.fillOval(24 - 4 - 13, 60, 8, 8);
				g2.fillOval(24 - 4, 60, 8, 8);
				g2.fillOval(24 - 4 + 13, 60, 8, 8);
			}
			if (card.border == 2) {
				g2.setStroke(new BasicStroke(1));
				g2.setColor(new Color(0, 255, 0, 200));
				g2.drawRoundRect(0, 0, 48, 73, 10, 10);
			}
		}
		public ImageIcon getIcon(String img) {
			URL imageURL = getClass().getClassLoader().getResource(img);
			return new ImageIcon(imageURL);
		}

	}