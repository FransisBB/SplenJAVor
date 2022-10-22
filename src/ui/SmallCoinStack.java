package ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.net.URL;
import java.util.stream.IntStream;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class SmallCoinStack extends JPanel {
	private static final long serialVersionUID = 980646207639325446L;
	
	int cost[];
	int starty = 120;
	public SmallCoinStack(int cost[], boolean smart) {
		this.cost=cost;
		setLayout(null);
		setPreferredSize(new Dimension(56, 150));
		setOpaque(false);
		if (smart) {
			this.starty=12*IntStream.of(cost).sum()-10;
			setPreferredSize(new Dimension(56, starty+30));
			
		}
	}
	
	public void paintComponent(Graphics g) {
      	super.paintComponent(g);
      	//test rysowania
      	Graphics2D g2 = (Graphics2D) g;
      	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      	g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

      	int ychange = 12;
      	int size = 55;
      	int y=starty;
      	for (int i=0; i<cost.length; i++) {
      		for (int j=0; j<cost[i]; j++) {
      		drawCircle(g2, 0, y, size, i);
      		y-=ychange;
      		}
      	}
  	}
	void drawCircle(Graphics2D g2, int x, int y, int size, int colorh) {
		Color cDark = Color.black;
		Color cLight = Color.gray;
		Color cRound = new Color(0, 0, 0, 100);
		Image image = getIcon("images/colors/green.png").getImage();
		int imagex = 0;
		if (colorh == 0) {
			image = getIcon("images/colors/green.png").getImage();
			imagex = (int) (x + size / 2 - image.getWidth(this) * 1.5 / 2);
			cDark = new Color(0, 150, 0);
			cLight = Color.green;

		}
		if (colorh == 1) {
			image = getIcon("images/colors/white.png").getImage();
			imagex = (int) (x + size / 2 - image.getWidth(this) * 1.5 / 2) + 6;
			cDark = new Color(150, 150, 150);
			cLight = Color.white;

		}
		if (colorh == 2) {
			image = getIcon("images/colors/blue.png").getImage();
			imagex = (int) (x + size / 2 - image.getWidth(this) * 1.5 / 2) + 3;
			cDark = new Color(0, 0, 150);
			cLight = Color.blue;

		}
		if (colorh == 3) {
			image = getIcon("images/colors/black.png").getImage();
			imagex = (int) (x + size / 2 - image.getWidth(this) * 1.5 / 2) + 3;
			cDark = new Color(50, 50, 50);
			cLight = new Color(100, 100, 100);

		}
		if (colorh == 4) {
			image = getIcon("images/colors/red.png").getImage();
			imagex = (int) (x + size / 2 - image.getWidth(this) * 1.5 / 2) + 4;
			cDark = new Color(150, 0, 0);
			cLight = new Color(255, 0, 0);

		}
		if (colorh == 5) {
			image = getIcon("images/colors/yellow.png").getImage();
			imagex = (int) (x + size / 2 - image.getWidth(this) * 1.5 / 2)+4;
			cDark = new Color(150, 150, 0);
			cLight = new Color(255, 255, 0);

		}

		g2.setColor(cDark);
		g2.fillOval(x, y + 12, size, size - 40);
		g2.setPaint(
				new GradientPaint(x, y + 12, new Color(255, 255, 255, 75), x + size, y + 12, new Color(0, 0, 0, 100)));
		g2.fillOval(x, y + 12, size, size - 40);
		g2.setColor(cRound);
		g2.drawOval(x, y + 12, size, size - 40);
		g2.setColor(cDark);
		g2.fillRect(x, y + (size - 40) / 2, size + 1, 12);
		g2.setPaint(new GradientPaint(x, y + (size - 40) / 2, new Color(255, 255, 255, 75), x + size,
				y + (size - 40) / 2, new Color(0, 0, 0, 100)));
		g2.fillRect(x, y + (size - 40) / 2, size, 12);
		g2.setColor(cRound);
		// if (active) g2.setColor(Color.yellow);
		g2.drawLine(x, y + 12 + ((size - 40) / 2), x, y + (size - 40) / 2);
		g2.drawLine(x + size, y + 12 + ((size - 40) / 2), x + size, y + (size - 40) / 2);
		g2.setColor(cLight);
		g2.fillOval(x, y, size, size - 40);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
		g2.drawImage(image, imagex, y + 2, (int) (image.getHeight(this) * 1.5), size - 45, this);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		g2.setColor(cRound);
		g2.drawOval(x, y, size, size - 40);

		//return g2;
	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}
}
