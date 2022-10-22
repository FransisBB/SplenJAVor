package game.obj;

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
import java.awt.TexturePaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import ui.STRButton;
import ui.TButton;

public class City extends JPanel {

	private static final long serialVersionUID = 6714475992534606019L;
	public int reqPoints, tile, border=0;
	public int[] req = new int[6];
	public String imgPath;
	public boolean isAvailable = true, highlighted = false;

	public City(String imgPath, int tile, int reqPoints, int... req) {
		this.tile=tile;
		this.reqPoints = reqPoints;
		this.imgPath = imgPath;
		for (int i = 0; i < 6; i++)
			this.req[i] = req[i];
		setLayout(null);
		setPreferredSize(new Dimension(88, 88));
		setOpaque(false);
		// setVisible(true);

	}

	public void isAvailable(boolean is) {
		this.isAvailable = is;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// test rysowania
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(new Color(0, 0, 0, 50));
		g2.fillRoundRect(3, 3, 85, 85, 15, 15);
		// ca³oœæ
		// g2.setColor(new Color(0, 128, 255));
		g2.setPaint(tp("images/cities/" + imgPath));
		g2.fillRoundRect(0, 0, 85, 85, 15, 15);

		// zacienienie
		g2.setColor(new Color(255, 255, 255, 100));
		int w = 30;
		int c = 0;
		for (int t : req) {
			if (t != 0) {
				c++;
			}
		}
		if (c > 3)
			w = w * 2 - 6;
		g2.fillRoundRect(85 - w, 0, w, 85, 15, 15);

		//// punkty
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Verdana", Font.PLAIN, 20));
		g2.drawString(Integer.toString(reqPoints), 56, 19);

		int y = 63;
		int x = 61;
		int i = 0;
		c = 0;
		for (int t : req) {
			if (t != 0) {
				if (c == 3) {
					y = 63;
					x -= 24;
				}
				drawRect(g2, x, y, 18, i, t);
				y -= 20;
				c++;
			}
			i++;
		}
		if (border==1) {
			g2.setStroke(new BasicStroke(2));
			g2.setColor(new Color(0, 255, 0, 200));
			g2.drawRoundRect(0, 0, 85, 85, 15, 15);
		}  else if (border == 2) {
			g2.setStroke(new BasicStroke(2));
			g2.setColor(new Color(255, 255, 255, 225));
			g2.drawRoundRect(0, 0, 85, 85, 15, 15);
		} else {
			g2.setColor(new Color(0, 0, 0, 100));
			g2.drawRoundRect(0, 0, 85, 85, 15, 15);
		}
		if (highlighted) {
			g2.setColor(new Color(255, 255, 255, 50));
			g2.fillRoundRect(0, 0, 85, 85, 15, 15);
		}
		// g2.setColor(new Color(0, 0, 0, 100));
		// g2.drawRoundRect(0, 0, 85, 85, 15, 15);
		GradientPaint gp1 = new GradientPaint(0, 0, new Color(255, 255, 255, 75), 100, 150, new Color(0, 0, 0, 25));
		g2.setPaint(gp1);
		g2.fillRoundRect(0, 0, 85, 85, 15, 15);
	}

	Graphics2D drawRect(Graphics2D g2, int x, int y, int size, int colorh, int costh) {
		int fx, fy;
		Color paintColor1,paintColor2;

		switch (colorh) {
		
		
		case 0:
			paintColor1 = new Color(0, 255, 0);
			paintColor2 = new Color(0, 200, 0);
			break;
		case 1:
			paintColor1 = new Color(255, 255, 255);
			paintColor2 = new Color(200, 200, 200);
			break;
		case 2:
			paintColor1 = new Color(0, 0, 255);
			paintColor2 = new Color(0, 0, 200);
			break;
		case 3:
			paintColor1 = new Color(50, 50, 50);
			paintColor2 = new Color(0, 0, 0);
			break;
		case 4:
			paintColor1 = new Color(255, 0, 0);
			paintColor2 = new Color(200, 0, 0);
			break;
		case 5:
			paintColor1 = Color.LIGHT_GRAY;
			paintColor2 = Color.GRAY;
			break;
		default:
			paintColor1 = Color.YELLOW;
			paintColor2 = Color.YELLOW;
		}
		Font font = new Font("TimesRoman", Font.BOLD, size - 3);
		FontMetrics metrics = g2.getFontMetrics(font);
		GradientPaint gp1 = new GradientPaint(x, y, paintColor1, x + size, y + size, paintColor2);
		g2.setPaint(gp1);
		g2.fillRect(x, y, size, size);

		if (colorh == 2 || colorh == 3) {
			g2.setColor(new Color(255, 255, 255, 85));
			g2.drawRect(x, y, size, size);
			g2.setColor(new Color(255, 255, 255));
		} else {
			g2.setColor(new Color(0, 0, 0, 85));
			g2.drawRect(x, y, size, size);
			g2.setColor(new Color(0, 0, 0));

		}
		fx = x + (size - metrics.stringWidth(Integer.toString(costh))) / 2;
		fy = y + ((size - metrics.getHeight()) / 2) + metrics.getAscent();
		g2.setFont(font);
		g2.drawString(Integer.toString(costh), fx, fy);
		return g2;
	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	public void removeAllActions() {
		while (getMouseListeners().length != 0) {
			removeMouseListener(getMouseListeners()[0]);
		}
		for (Component c : this.getComponents()) {
			if (c instanceof STRButton || c instanceof TButton) {
				remove(c);
			}
		}
		border = 0;
		highlighted = false;
		repaint();
		revalidate();
	}

	public void setActive() {
		border = 1;
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent me) {
				highlighted = true;
				repaint();
				revalidate();
			}

			public void mouseExited(MouseEvent me) {
				highlighted = false;
				repaint();
				revalidate();
			}
		});
		repaint();
		revalidate();
	}

	public TexturePaint tp(String path) {
		Image displayImage = getIcon(path).getImage();

		BufferedImage image = new BufferedImage(displayImage.getWidth(this), displayImage.getHeight(this),
				BufferedImage.TYPE_INT_RGB);
		image.createGraphics().drawImage(displayImage, 0, 0, this);

		Rectangle2D rectangle = new Rectangle2D.Float(0, 0, displayImage.getWidth(this), displayImage.getHeight(this));

		return new TexturePaint(image, rectangle);
	}

}