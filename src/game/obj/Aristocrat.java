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

public class Aristocrat extends JPanel {

	private static final long serialVersionUID = 6714475992534606019L;

	public int[] req = new int[5];
	public int reward, resID = 0, border=0;
	public String imgPath, nick;
	public boolean isAvailable = true, highlighted = false, isReserved = false, showFront = true,
			hideAri = false;

	public Aristocrat(String imgPath, int reward, int... req) {
		this.reward = reward;
		this.imgPath = imgPath;
		for (int i = 0; i < 5; i++)
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
		if (!hideAri) {
			g2.setColor(new Color(0, 0, 0, 50));
			g2.fillRoundRect(3, 3, 85, 85, 15, 15);
			// ca³oœæ
			// g2.setColor(new Color(0, 128, 255));
			if (showFront) {
				g2.setPaint(tp("images/aristocrats/" + imgPath));
				g2.fillRoundRect(0, 0, 85, 85, 15, 15);

				// zacienienie
				g2.setColor(new Color(255, 255, 255, 100));
				g2.fillRoundRect(0, 0, 30, 85, 15, 15);

				//// punkty
				g2.setColor(Color.BLACK);
				g2.setFont(new Font("Verdana", Font.PLAIN, 20));
				if (reward != 0)
					g2.drawString(Integer.toString(reward), 9, 19);

				int h = 63;
				int i = 0;
				for (int t : req) {
					if (t != 0) {
						drawRect(g2, 6, h, 18, i, t);
						h -= 20;
					}
					i++;
				}
				
			} else {
				g2.setPaint(tp("images/aristocrats/ariBack.png"));
				g2.fillRoundRect(0, 0, 85, 85, 15, 15);
				g2.setFont(new Font("Verdana", Font.BOLD, 12));
				g2.setColor(new Color(255, 255, 255, 150));
				g2.drawString("SplenJAVor", 5, 30);
			}
			if (border==1) {
				g2.setStroke(new BasicStroke(2));
				g2.setColor(new Color(0, 255, 0, 200));
				g2.drawRoundRect(0, 0, 85, 85, 15, 15);
			}  else if (border == 2 || (isReserved && showFront)) {
				g2.setStroke(new BasicStroke(2));
				g2.setColor(new Color(255, 255, 255, 225));
				g2.drawRoundRect(0, 0, 85, 85, 15, 15);
			}else {
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
			if (isReserved && showFront) {
				g2.drawImage(getIcon("images/hand.png").getImage(), 18, 63, this);
				
			}
		}
	}

	Graphics2D drawRect(Graphics2D g2, int x, int y, int size, int colorh, int costh) {
		int fx, fy;
		Color paintColor1 = Color.YELLOW;
		Color paintColor2 = Color.YELLOW;
		GradientPaint gp1 = new GradientPaint(0, 0, paintColor1, 0, 0, paintColor2);
		Font font = new Font("TimesRoman", Font.BOLD, size - 3);
		FontMetrics metrics = g2.getFontMetrics(font);
		if (colorh == 0) {
			paintColor1 = new Color(0, 255, 0);
			paintColor2 = new Color(0, 200, 0);
		}
		if (colorh == 1) {
			paintColor1 = new Color(255, 255, 255);
			paintColor2 = new Color(200, 200, 200);
		}
		if (colorh == 2) {
			paintColor1 = new Color(0, 0, 255);
			paintColor2 = new Color(0, 0, 200);
		}
		if (colorh == 3) {
			paintColor1 = new Color(50, 50, 50);
			paintColor2 = new Color(0, 0, 0);
		}
		if (colorh == 4) {
			paintColor1 = new Color(255, 0, 0);
			paintColor2 = new Color(200, 0, 0);
		}
		gp1 = new GradientPaint(x, y, paintColor1, x + size, y + size, paintColor2);
		g2.setPaint(gp1);
		g2.fillRect(x, y, size, size);

		if (colorh == 2 || colorh == 3 || colorh == 4) {
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