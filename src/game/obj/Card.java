package game.obj;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import ui.STRButton;
import ui.TButton;

public class Card extends JPanel {

	private static final long serialVersionUID = -8785177698481239840L;

	public int id, level, color, value, special, points, cost[] = new int[5], discardColor = 0, strColor = 9,
			strNum = 0;
	public String imgPath, type;
	public boolean show = true, grayscale = false, highlighted = false, taken = false;
	int size = 24, padding = 3; // temp
	Graphics2D g2;
	public int border = 0;

	public Card(String imgPath, String type, int id, int level, int color, int value, int special, int points,
			int... cost) {
		this.id = id;
		this.level = level;
		this.color = color;
		this.points = points;
		this.value = value;
		this.special = special;
		this.show = true;
		for (int i = 0; i < 5; i++)
			this.cost[i] = cost[i];
		if (cost.length == 6) {
			discardColor = cost[5];
		}
		this.imgPath = imgPath;
		System.out.println(imgPath);
		this.type = type;
		setLayout(null);
		setPreferredSize(new Dimension(103, 153));
		setOpaque(false);

	}

	public void addStronghold(int i) {
		strColor = i;
		strNum++;
		repaint();
	}

	public void removeStronghold() {
		strNum--;
		if (strNum == 0) {
			strColor = 9;
		}
		repaint();
	}

	public void clearStr() {
		strNum = 0;
		strColor = 9;
		repaint();
	}

	public void setGrayscale(boolean s) {
		grayscale = s;
		repaint();
		// revalidate();
	}

	public void showFront(boolean s) {
		show = s;
	}

	// public void setActive (boolean s) {
	// highlighted = s;
	// }
	public void anim() {
		Timer timer = new Timer(200, new ActionListener() {
			int tick = 0;

			public void actionPerformed(ActionEvent evt) {
				if (tick <= 4) {
					if (tick % 2 == 1) {
						highlighted = false;
						repaint();
					}
					if (tick % 2 == 0) {
						highlighted = true;
						repaint();
					}
				} else {
					highlighted = false;
					repaint();
					Timer t = (Timer) evt.getSource();
					t.stop();
				}
				tick++;
			}
		});
		timer.setRepeats(true);
		timer.start();
	}

	// @Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// test rysowania
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// g2.setRenderingHint(RenderingHints. KEY_INTERPOLATION,
		// RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		// g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
		// RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		// g2.setColor(new Color(0, 0, 0, 75));
		// g2.fillRoundRect(2, 2, 100, 150, 15, 15);
		if (grayscale)
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		// cieñ
		g2.setColor(new Color(0, 0, 0, 75));
		g2.fillRoundRect(3, 3, 100, 150, 15, 15);
		if (show) {
			// ca³oœæ
			// g2.setColor(new Color(0, 128, 255));
			g2.setPaint(tp("images/cards/" + imgPath, 0, 0));
			g2.fillRoundRect(0, 0, 100, 150, 15, 15);

			// nag³ówek
			//if (type == "orient") {
			//	g2.setColor(new Color(255, 245, 245, 175));
			//} else
			g2.setColor(new Color(255, 255, 255, 175));
			g2.fillRoundRect(0, 0, 100, 30, 15, 15);

			// punkty
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("Verdana", Font.PLAIN, 24));
			if (grayscale)
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
			if (points != 0)
				g2.drawString(Integer.toString(points), 5, 24);

			// kolor karty

			if (special == 1 || special == 2) {
				if (!taken) {
					g2.drawImage(getIcon("images/colors/bag.png").getImage(), 78, 3, this);
				} else {
					g2.drawImage(getIcon("images/colors/bag.png").getImage(), 23, 3, this);
					g2.drawImage(getIcon("images/arrowToBagVert.png").getImage(), 45, 8, this);
					if (color == 0) {
						g2.drawImage(getIcon("images/colors/green.png").getImage(), 73, 3, this);
					} else if (color == 1) {
						g2.drawImage(getIcon("images/colors/white.png").getImage(), 65, 2, this);
					} else if (color == 2) {
						g2.drawImage(getIcon("images/colors/blue.png").getImage(), 68, 3, this);
					} else if (color == 3) {
						g2.drawImage(getIcon("images/colors/black.png").getImage(), 71, 2, this);
					} else if (color == 4) {
						g2.drawImage(getIcon("images/colors/red.png").getImage(), 68, 2, this);
					}
				}
			} else if (special == 4) {
				// g2.drawImage(getIcon("images/colors/yellow.png").getImage(), 68, 2, this);
				// g2.drawImage(getIcon("images/colors/yellow.png").getImage(), 68-25, 2, this);
				Image image;
				image = getIcon("images/colors/yellow.png").getImage();
				g2.setColor(new Color(0, 0, 0, 200));
				g2.fillOval(72, 2, 26, 26);
				g2.fillOval(72 - 27, 2, 26, 26);
				g2.setColor(Color.YELLOW);
				g2.fillOval(73, 3, 24, 24);
				g2.fillOval(73 - 27, 3, 24, 24);
				// g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
				g2.drawImage(image, 76, 6, (int) (image.getWidth(this) / 1.6), (int) (image.getHeight(this) / 1.6),
						this);
				g2.drawImage(image, 76 - 27, 6, (int) (image.getWidth(this) / 1.6), (int) (image.getHeight(this) / 1.6),
						this);
				// g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			} else {
				if (value > 0) {
					if (color == 0) {
						g2.drawImage(getIcon("images/colors/green.png").getImage(), 73, 3, this);
					} else if (color == 1) {
						g2.drawImage(getIcon("images/colors/white.png").getImage(), 65, 2, this);
					} else if (color == 2) {
						g2.drawImage(getIcon("images/colors/blue.png").getImage(), 68, 3, this);
					} else if (color == 3) {
						g2.drawImage(getIcon("images/colors/black.png").getImage(), 71, 2, this);
					} else if (color == 4) {
						g2.drawImage(getIcon("images/colors/red.png").getImage(), 68, 2, this);
					}
					if (value == 2) {
						if (color == 0) {
							g2.drawImage(getIcon("images/colors/green.png").getImage(), 73 - 27, 3, this);
						} else if (color == 1) {
							g2.drawImage(getIcon("images/colors/white.png").getImage(), 65 - 34, 2, this);
						} else if (color == 2) {
							g2.drawImage(getIcon("images/colors/blue.png").getImage(), 68 - 29, 3, this);
						} else if (color == 3) {
							g2.drawImage(getIcon("images/colors/black.png").getImage(), 71 - 25, 2, this);
						} else if (color == 4) {
							g2.drawImage(getIcon("images/colors/red.png").getImage(), 68 - 31, 2, this);
						}
					}
				}
			}
			if (special == 2 || special == 3) {
				g2.setColor(Color.black);
				g2.fillRoundRect(69, 34, 27, 37, 7, 7);
				g2.setColor(Color.gray);
				g2.fillRoundRect(70, 35, 25, 35, 7, 7);
				g2.setColor(Color.lightGray);
				g2.fillRoundRect(70, 35, 25, 10, 7, 7);
				g2.setColor(Color.black);
				g2.drawString("+", 55, 57);
				g2.drawString("+", 55, 59);
				g2.drawString("+", 57, 57);
				g2.drawString("+", 57, 59);

				g2.setColor(Color.white);
				g2.drawString("+", 56, 58);
			}
			if (special == 2) {
				g2.fillOval(78, 58, 8, 8);
			}
			if (special == 3) {
				g2.fillOval(73, 58, 8, 8);
				g2.fillOval(83, 58, 8, 8);
			}
			if (special == 6) {
				// g2.drawImage(getIcon("images/aristocrats/ariBack.png").getImage(), 68, 3,
				// this);
				g2.setColor(Color.BLACK);
				g2.fillRoundRect(59, 34, 37, 37, 7, 7);
				g2.setPaint(tp("images/aristocrats/ariBackSmall.png", 60, 35));
				// g2.setColor(Color.gray);
				g2.fillRoundRect(60, 35, 35, 35, 7, 7);
				g2.drawImage(getIcon("images/cArrow.png").getImage(), 51, 48, this);
				// g2.fillRoundRect(0, 0, 100, 150, 15, 15);
			}
			if (grayscale)
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			// cena karty - prostok¹t
			int h = 0;
			for (int t : cost) {
				if (t != 0)
					h++;
			}

			h = h * 27 + 6;
			g2.setColor(new Color(255, 255, 255, 125));
			g2.fillRoundRect(3, 150 - h, 32, h - 3, 15, 15);

			// cena karty - kó³ka

			h = 150 - size - 2 * padding;

			for (int i = 4; i >= 0; i--) {
				if (cost[i] != 0) {
					drawCircle(2 * padding, h, size, i, cost[i]);
					h = h - size - padding;
				}
			}
			if (special == 5) {
				// g2.fillRoundRect(3, 150 - h, 32, h - 3, 15, 15);
				drawDiscardRect(5, 70, 25, discardColor);
				drawDiscardRect(5, 110, 25, discardColor);
			}
			// GradientPaint gp1 = new GradientPaint(0, 0, new Color(255, 255, 255, 75),
			// 100, 150,
			// new Color(255, 255, 255, 25));
			// g2.setPaint(gp1);
			// g2.fillRoundRect(0, 0, 100, 150, 15, 15);
			if (strColor != 9) {
				if (strNum == 1) {

					g2.setColor(new Color(255, 255, 255, 175));
					g2.fillRoundRect(37, 74, 27, 34, 15, 15);

					g2.setPaint(tp("images/tower" + strColor + ".png", 37, 75));
					g2.fillRect(37, 75, 26, 30);
				} else if (strNum == 2) {

					g2.setColor(new Color(255, 255, 255, 125));
					g2.fillRoundRect(37, 59, 27, 64, 15, 15);

					g2.setPaint(tp("images/tower" + strColor + ".png", 37, 60));
					g2.fillRect(37, 60, 26, 60);

				} else if (strNum == 3) {

					g2.setColor(new Color(255, 255, 255, 200));
					g2.fillRoundRect(37, 45, 27, 94, 15, 15);

					g2.setPaint(tp("images/tower" + strColor + ".png", 37, 46));
					g2.fillRect(37, 46, 26, 90);
				}
			}
			if (type == "orient") {
				g2.setColor(new Color(255, 255, 255, 125));
				g2.fillOval(72, 122, 25, 25);
				g2.setPaint(tp("images/dragon.png", 74, 124));
				g2.fillRect(74, 124, 20, 20);
				//g2.drawImage(getIcon("images/dragon.png").getImage(), 100-35, 150-35, this);
			}
		} else {
			if (type == "standard") {
				if (level == 0)
					g2.setPaint(tp("images/cards/green.jpg", 0, 0));
				if (level == 1)
					g2.setPaint(tp("images/cards/yellow.jpg", 0, 0));
				if (level == 2)
					g2.setPaint(tp("images/cards/blue.jpg", 0, 0));
			} else if (type == "orient") {
				if (level == 0)
					g2.setPaint(tp("images/cards/greenOrient.jpg", 0, 0));
				if (level == 1)
					g2.setPaint(tp("images/cards/yellowOrient.jpg", 0, 0));
				if (level == 2)
					g2.setPaint(tp("images/cards/blueOrient.jpg", 0, 0));
			}
			g2.fillRoundRect(0, 0, 100, 150, 15, 15);
			g2.setFont(new Font("Verdana", Font.BOLD, 14));
			// g2.setColor(new Color(255, 255, 255, 150));
			// g2.drawString("SplenJAVor", 5, 40);
			// g2.setColor(new Color(255, 255, 255, 100));
			if (type == "standard") {
				g2.setColor(new Color(255, 255, 255, 150));
				g2.drawString("SplenJAVor", 5, 40);
				g2.setColor(new Color(255, 255, 255, 100));
			} else if (type == "orient") {
				g2.setColor(new Color(255, 255, 255, 175));
				g2.drawString("SplenJAVor", 5, 40);
				g2.drawString("Orient", 26, 85);
				g2.setColor(new Color(255, 255, 255, 125));
			}
			if (level == 0) {
				g2.fillOval(50 - 8, 120, 16, 16);
			}
			if (level == 1) {
				g2.fillOval(50 - 8 - 13, 120, 16, 16);
				g2.fillOval(50 - 8 + 13, 120, 16, 16);
			}
			if (level == 2) {
				g2.fillOval(50 - 8 - 26, 120, 16, 16);
				g2.fillOval(50 - 8, 120, 16, 16);
				g2.fillOval(50 - 8 + 26, 120, 16, 16);
			}

		}

		if (border == 1) {
			g2.setStroke(new BasicStroke(2));
			g2.setColor(new Color(255, 255, 0, 200));
			g2.drawRoundRect(0, 0, 100, 150, 15, 15);
		} else if (border == 2) {
			g2.setStroke(new BasicStroke(2));
			g2.setColor(new Color(0, 255, 0, 200));
			g2.drawRoundRect(0, 0, 100, 150, 15, 15);
		} else if (border == 3) {
			g2.setStroke(new BasicStroke(2));
			g2.setColor(new Color(255, 255, 255, 225));
			g2.drawRoundRect(0, 0, 100, 150, 15, 15);
		} else {
			g2.setColor(new Color(0, 0, 0, 100));
			g2.drawRoundRect(0, 0, 100, 150, 15, 15);
		}
		if (highlighted) {
			g2.setColor(new Color(255, 255, 255, 50));
			g2.fillRoundRect(0, 0, 100, 150, 15, 15);
		}
		// if (grayscale) {
		// g2.setColor(new Color(0, 0, 0, 150));
		// g2.fillRoundRect(0, 0, 100, 150, 15, 15);
		// }
		// g2.setColor(new Color(255, 255, 255));
		GradientPaint gp1 = new GradientPaint(0, 0, new Color(255, 255, 255, 75), 100, 150, new Color(0, 0, 0, 25));
		if (type == "standard") {
			gp1 = new GradientPaint(0, 0, new Color(255, 255, 255, 75), 100, 150, new Color(0, 0, 0, 25));
		} else if (type == "orient") {
			gp1 = new GradientPaint(0, 0, new Color(255, 255, 255, 100), 100, 150, new Color(0, 0, 0, 50));
		}

		g2.setPaint(gp1);
		g2.fillRoundRect(0, 0, 100, 150, 15, 15);
	}

	public void setBorder(int a) {
		border = a;
		repaint();
		// revalidate();
	}

	public void setActive() {
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent me) {
				highlighted = true;
				repaint();
				// revalidate();
			}

			public void mouseExited(MouseEvent me) {
				highlighted = false;
				repaint();
				// revalidate();
			}
		});
	}

	public void removeAllActions() {
		while (getMouseListeners().length != 0) {
			removeMouseListener(getMouseListeners()[0]);
		}
		border = 0;
		highlighted = false;
		grayscale = false;
		removeButtons();
	}

	public void removeButtons() {
		for (Component c : this.getComponents()) {
			if (c instanceof STRButton || c instanceof TButton) {
				remove(c);
			}
		}
		repaint();
	}

	Graphics2D drawCircle(int x, int y, int size, int colorh, int costh) {

		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		int fx, fy;
		Color paintColor1 = Color.YELLOW;
		Color paintColor2 = Color.YELLOW;
		GradientPaint gp1 = new GradientPaint(0, 0, paintColor1, 0, 0, paintColor2);
		if (colorh == 0) {
			paintColor1 = new Color(50, 255, 50);
			paintColor2 = new Color(0, 150, 0);
		}
		if (colorh == 1) {
			paintColor1 = new Color(255, 255, 255);
			paintColor2 = new Color(175, 175, 175);
		}
		if (colorh == 2) {
			paintColor1 = new Color(50, 50, 255);
			paintColor2 = new Color(0, 0, 150);
		}
		if (colorh == 3) {
			paintColor1 = new Color(75, 75, 75);
			paintColor2 = new Color(0, 0, 0);
		}
		if (colorh == 4) {
			paintColor1 = new Color(255, 50, 50);
			paintColor2 = new Color(150, 0, 0);
		}
		gp1 = new GradientPaint(x, y, paintColor1, x + size, y + size, paintColor2);
		g2.setPaint(gp1);
		g2.fillOval(x, y, size, size);

		if (grayscale)
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));

		String s = Integer.toString(costh);
		Font font = new Font("TimesRoman", Font.PLAIN, size - 3);
		FontMetrics metrics = g2.getFontMetrics(font);
		fx = x + (size - metrics.stringWidth(s)) / 2;
		fy = y + ((size - metrics.getHeight()) / 2) + metrics.getAscent() - 1;
		g2.setFont(font);
		if (colorh == 2 || colorh == 3 || colorh == 4) {
			g2.setColor(Color.WHITE);
		} else {
			g2.setColor(Color.BLACK);
		}
		g2.drawString(s, fx, fy);
		if (grayscale)
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
		return g2;

	}

	Graphics2D drawDiscardRect(int x, int y, int size, int colorh) {
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		if (colorh == 0) {
			g2.setColor(new Color(50, 255, 50));
		}
		if (colorh == 1) {
			g2.setColor(new Color(255, 255, 255));
		}
		if (colorh == 2) {
			g2.setColor(new Color(50, 50, 255));
		}
		if (colorh == 3) {
			g2.setColor(new Color(75, 75, 75));
		}
		if (colorh == 4) {
			g2.setColor(new Color(255, 50, 50));
		}
		g2.fillRoundRect(x, y, size, (int) (size * 1.5), 10, 10);
		if (colorh == 2 || colorh == 3) {
			g2.setColor(new Color(255, 255, 255));
		} else {
			g2.setColor(new Color(0, 0, 0));
		}
		Stroke s = g2.getStroke();

		g2.drawRoundRect(x, y, size, (int) (size * 1.5), 10, 10);
		g2.setStroke(new BasicStroke(2));
		g2.drawLine(x + size + 2, y + 3, x - 2, y + (int) (size * 1.5) - 3);
		g2.setStroke(s);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
		return g2;

	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	public TexturePaint tp(String path, int x, int y) {
		Image displayImage = getIcon(path).getImage();

		BufferedImage image = new BufferedImage(displayImage.getWidth(this), displayImage.getHeight(this),
				BufferedImage.TYPE_INT_ARGB);
		image.createGraphics().drawImage(displayImage, 0, 0, this);

		Rectangle2D rectangle = new Rectangle2D.Float(x, y, displayImage.getWidth(this), displayImage.getHeight(this));

		return new TexturePaint(image, rectangle);
	}

//	public int discardColor() {
//		for (int i=0; i<5; i++) {
//			if (cost[i]>0) return i;
////		}
	// return 0;
//	}
}
