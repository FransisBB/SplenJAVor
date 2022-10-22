package game.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import game.obj.Card;

public final class Utils {
	private Utils() {
	}

	public static ImageIcon scaleIcon(ImageIcon srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg.getImage(), 0, 0, w, h, 0, 0, srcImg.getIconWidth(), srcImg.getIconHeight(), null);
		g2.dispose();

		return new ImageIcon(resizedImg);
	}

	public static Card copyCard(Card t, boolean taken, boolean str) {
		Card nCard = new Card(t.imgPath, t.type, t.id, t.level, t.color, t.value, t.special, t.points, t.cost);
		nCard.discardColor = t.discardColor;
		nCard.taken = taken;
		if (str) {
			nCard.strColor = t.strColor;
			nCard.strNum = t.strNum;
		}
		return nCard;
	}

	public static JLabel coinDraw(int color, int num, int sizeSet, boolean alwaysVisible) {
		int size = 0;
		if (sizeSet == 1) {
			size = 31;
		}
		if (sizeSet == 2) {
			size = 41;
		}
		JLabel coin = new JLabel() {
			private static final long serialVersionUID = -4642070724612536946L;

			@Override
			public void paintComponent(Graphics g) {
				int size = 0, fcsize = 0;
				if (sizeSet == 1) {
					size = 29;
					fcsize = 22;

				}
				if (sizeSet == 2) {
					size = 39;
					fcsize = 30;

				}
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
				int opacity = 255;
				int fx, fy;
				Color paintColor1 = Color.YELLOW;
				Color paintColor2 = Color.YELLOW;
				GradientPaint gp1 = new GradientPaint(0, 0, paintColor1, 0, 0, paintColor2);
				Font font = new Font("Arial", Font.PLAIN, fcsize);
				FontMetrics metrics = g.getFontMetrics(font);

				if (num == 0 && !alwaysVisible)
					opacity = 25;
				if (color == 0) {
					paintColor1 = new Color(0, 255, 0, opacity);
					paintColor2 = new Color(0, 200, 0, opacity);
				}
				if (color == 1) {
					paintColor1 = new Color(255, 255, 255, opacity);
					paintColor2 = new Color(200, 200, 200, opacity);
				}
				if (color == 2) {
					paintColor1 = new Color(0, 0, 255, opacity);
					paintColor2 = new Color(0, 0, 200, opacity);
				}
				if (color == 3) {
					paintColor1 = new Color(100, 100, 100, opacity);
					paintColor2 = new Color(0, 0, 0, opacity);
				}
				if (color == 4) {
					paintColor1 = new Color(255, 0, 0, opacity);
					paintColor2 = new Color(200, 0, 0, opacity);
				}
				if (color == 5) {
					paintColor1 = new Color(255, 255, 0, opacity);
					paintColor2 = new Color(200, 200, 0, opacity);
				}
				if (color == 6) {
					paintColor1 = new Color(255, 255, 255, 100);
					paintColor2 = new Color(200, 200, 200, 100);
				}

				gp1 = new GradientPaint(size / 2, size / 2, paintColor1, size, size, paintColor2);
				g2.setPaint(gp1);
				g2.fillOval(1, 1, size, size);

				if (color == 2 || color == 3 || color == 4) {
					g2.setColor(new Color(255, 255, 255, opacity / 2));
					g2.drawOval(1, 1, size, size);
					g2.setColor(new Color(255, 255, 255, opacity));
				} else {
					g2.setColor(new Color(0, 0, 0, opacity / 2));
					g2.drawOval(1, 1, size, size);
					g2.setColor(new Color(0, 0, 0, opacity));
				}

				if (num != 0 || alwaysVisible) {
					fx = 1 + (size - metrics.stringWidth(Integer.toString(num))) / 2;
					fy = 1 + ((size - metrics.getHeight()) / 2) + metrics.getAscent();
					g2.setFont(font);
					g2.drawString(Integer.toString(num), fx, fy);
				}
			}
		};
		coin.setLayout(null);
		coin.setOpaque(false);
		coin.setPreferredSize(new Dimension(size, size));
		return coin;
	}

	public static JLabel doubleCoinDraw(int num, Image image) {
		JLabel coin = new JLabel() {
			private static final long serialVersionUID = -4642070724612536946L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
				Font font = new Font("Arial", Font.BOLD, 21);
				g2.setFont(font);
				FontMetrics metrics = g.getFontMetrics(font);
				g2.setColor(new Color(0, 0, 0, 200));
				g2.fillOval(0, 13, 24, 24);
				g2.setColor(new Color(255, 255, 0, 255));
				g2.fillOval(1, 14, 22, 22);
				g2.drawImage(image, 3, 17, (int) (image.getWidth(this) / 1.8), (int) (image.getHeight(this) / 1.8),
						this);

				String s = "+";
				int x = (25 - metrics.stringWidth(s)) / 2;
				int y = ((14 - metrics.getHeight()) / 2) + metrics.getAscent();
				g2.setColor(Color.WHITE);
				g2.drawString(s, x - 1, y - 1);
				g2.drawString(s, x + 1, y + 1);
				g2.drawString(s, x - 1, y + 1);
				g2.drawString(s, x + 1, y - 1);
				g2.setColor(num % 2 == 0 ? Color.BLACK : Color.RED);
				g2.drawString(s, x, y);

				s = Integer.toString(num);
				x = (25 - metrics.stringWidth(s)) / 2;
				y = 13 + ((25 - metrics.getHeight()) / 2) + metrics.getAscent();
				g2.setColor(Color.WHITE);
				g2.drawString(s, x - 1, y - 1);
				g2.drawString(s, x + 1, y + 1);
				g2.drawString(s, x - 1, y + 1);
				g2.drawString(s, x + 1, y - 1);
				g2.setColor(num % 2 == 0 ? Color.BLACK : Color.RED);
				g2.drawString(s, x, y);
				/*
				 * if (num != 0 || alwaysVisible) { fx = 1 + (size -
				 * metrics.stringWidth(Integer.toString(num))) / 2; fy = 1 + ((size -
				 * metrics.getHeight()) / 2) + metrics.getAscent(); g2.setFont(font);
				 * g2.drawString(Integer.toString(num), fx, fy); }
				 */
			}
		};
		coin.setLayout(null);
		coin.setOpaque(false);
		coin.setPreferredSize(new Dimension(25, 38));
		return coin;
	}

	public static JLabel cardAndCoinDraw(int color, int cardNum, int coinNum, int sizeSet) {
		int width = 0, height = 0;
		if (sizeSet == 1) {
			width = 34;
			height = 58;

		}
		if (sizeSet == 2) {
			width = 44;
			height = 74;

		}
		JLabel cardAndCoin = new JLabel() {
			private static final long serialVersionUID = 4628982726033671510L;

			@Override
			public void paintComponent(Graphics g) {
				int width = 0, height = 0, fsize = 0;
				if (sizeSet == 1) {
					width = 33;
					height = 43;
					fsize = 22;
				}
				if (sizeSet == 2) {
					width = 43;
					height = 53;
					fsize = 30;
				}

				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				Font font = new Font("Arial", Font.PLAIN, fsize);
				FontMetrics metrics = g.getFontMetrics(font);
				g2.setFont(font);
				int opacity = 255;
				int size = width - 4;
				int circleX = (width - size) / 2;
				int circleY = height - (size / 2) - 2;
				int fx, fy;
				Color paintColor1 = Color.YELLOW;
				Color paintColor2 = Color.YELLOW;
				GradientPaint gp1 = new GradientPaint(0, 0, paintColor1, 0, 0, paintColor2);
				if (color != 5) {
					if (cardNum == 0)
						opacity = 25;
					if (color == 0) {
						paintColor1 = new Color(0, 255, 0, opacity);
						paintColor2 = new Color(0, 200, 0, opacity);
					}
					if (color == 1) {
						paintColor1 = new Color(255, 255, 255, opacity);
						paintColor2 = new Color(200, 200, 200, opacity);
					}
					if (color == 2) {
						paintColor1 = new Color(0, 0, 255, opacity);
						paintColor2 = new Color(0, 0, 200, opacity);
					}
					if (color == 3) {
						paintColor1 = new Color(100, 100, 100, opacity);
						paintColor2 = new Color(0, 0, 0, opacity);
					}
					if (color == 4) {
						paintColor1 = new Color(255, 0, 0, opacity);
						paintColor2 = new Color(200, 0, 0, opacity);
					}
					gp1 = new GradientPaint(0, 0, paintColor1, width, height, paintColor2);
					g2.setPaint(gp1);
					g2.fillRoundRect(0, 0, width, height, 10, 10);

					if (color == 2 || color == 3 || color == 4) {
						g2.setColor(new Color(255, 255, 255, opacity / 3));
						g2.drawRoundRect(0, 0, width, height, 10, 10);
						g2.setColor(new Color(255, 255, 255, opacity));
					} else {
						g2.setColor(new Color(0, 0, 0, opacity / 3));
						g2.drawRoundRect(0, 0, width, height, 10, 10);
						g2.setColor(new Color(0, 0, 0, opacity));

					}
					if (cardNum != 0) {
						fx = (width - metrics.stringWidth(Integer.toString(cardNum))) / 2;
						fy = ((circleY - metrics.getHeight()) / 2) + metrics.getAscent();
						g2.drawString(Integer.toString(cardNum), fx, fy);
					}
				}

				opacity = 255;
				if (coinNum == 0)
					opacity = 25;
				if (color == 0) {
					paintColor1 = new Color(0, 255, 0, opacity);
					paintColor2 = new Color(0, 200, 0, opacity);
				}
				if (color == 1) {
					paintColor1 = new Color(255, 255, 255, opacity);
					paintColor2 = new Color(200, 200, 200, opacity);
				}
				if (color == 2) {
					paintColor1 = new Color(0, 0, 255, opacity);
					paintColor2 = new Color(0, 0, 200, opacity);
				}
				if (color == 3) {
					paintColor1 = new Color(100, 100, 100, opacity);
					paintColor2 = new Color(0, 0, 0, opacity);
				}
				if (color == 4) {
					paintColor1 = new Color(255, 0, 0, opacity);
					paintColor2 = new Color(200, 0, 0, opacity);
				}
				if (color == 5) {
					paintColor1 = new Color(255, 255, 0, opacity);
					paintColor2 = new Color(200, 200, 0, opacity);
				}

				g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
				gp1 = new GradientPaint(circleX + size / 2, circleY + size / 2, paintColor1, circleX + size,
						circleY + size, paintColor2);
				g2.setPaint(gp1);
				g2.fillOval(circleX, circleY, size, size);
				if (color == 2 || color == 3 || color == 4) {
					g2.setColor(new Color(255, 255, 255, opacity / 2));
					g2.drawOval(circleX, circleY, size, size);
					g2.setColor(new Color(255, 255, 255, opacity));
				} else {
					g2.setColor(new Color(0, 0, 0, opacity / 2));
					g2.drawOval(circleX, circleY, size, size);
					g2.setColor(new Color(0, 0, 0, opacity));
				}
				if (coinNum != 0) {
					fx = circleX + (size - metrics.stringWidth(Integer.toString(coinNum))) / 2;
					fy = circleY + ((size - metrics.getHeight()) / 2) + metrics.getAscent();
					g2.setFont(font);
					g2.drawString(Integer.toString(coinNum), fx, fy);
				}
			}
		};

		cardAndCoin.setLayout(null);
		cardAndCoin.setOpaque(false);
		cardAndCoin.setPreferredSize(new Dimension(width, height));
		return cardAndCoin;
	}

}
