package game.obj;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CoinsStack extends JPanel {
	private static final long serialVersionUID = -7669875714973868708L;

	int colNum;
	public int num, numUp;
	public boolean isActive = false;
	boolean highlited = false;

	public boolean anim = false;

	public CoinsStack(int num, int numUp, int colNum) {
		this.num = num;
		this.numUp = numUp;
		this.colNum = colNum;
		setLayout(null);
		setPreferredSize(new Dimension(56, 135));
		setOpaque(false);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		int ychange = 12;
		int ybigchange = 20;
		int size = 55;
		int y = 105;
		for (int i = 0; i < num; i++) {
			if (i == 0 && isActive) {
				g2.setColor(new Color(100, 255, 100));
				g2.fillOval(0, y + ychange + 3, size, size - 40);
			}
			if (i == num - numUp) {
				g2.setColor(new Color(0, 0, 0, 80));
				g2.fillOval(0, y + ychange, size, size - 40);
				y -= ybigchange;
			}
			drawCircle(g2, 0, y, size, colNum);
			y -= ychange;
		}
	}

	public void coinUp(int n) {
		numUp = n;
		repaint();
		revalidate();
	}

	public void setActive(boolean t) {
		isActive = t;
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent me) {
				highlited = true;
				repaint();
				revalidate();
			}

			public void mouseReleased(MouseEvent me) {
				highlited = true;
				repaint();
				revalidate();
			}

			public void mouseExited(MouseEvent me) {
				highlited = false;
				repaint();
				revalidate();
			}
		});
		repaint();
		revalidate();
	}

	public void addCoin(int n) {
		num += n;
		numUp += n;
		repaint();
		revalidate();
	}

	public void takeCoin(int n) {
		num -= n;
		numUp -= n;
		if (numUp < 0)
			numUp = 0;
		repaint();
		revalidate();
	}

	public void coinsDown(boolean inst) {
		int time = 300;
		if (inst) {
			numUp = 0;
			repaint();
		} else if (anim == false) {
			anim = true;
			Timer timer = new Timer(time, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (numUp <= 0) {
						anim = false;
						Timer t = (Timer) evt.getSource();
						numUp = 0;
						t.stop();
					} else {
						numUp -= 1;
						repaint();
						revalidate();
					}
				}
			});
			timer.setRepeats(true);
			timer.start();
		}
	}

	public void removeAllActions() {
		while (getMouseListeners().length != 0) {
			removeMouseListener(getMouseListeners()[0]);
		}
		isActive = false;
		highlited = false;

		repaint();
		revalidate();
	}

	Graphics2D drawCircle(Graphics2D g2, int x, int y, int size, int colorh) {
		Color cDark = Color.black;
		Color cLight = Color.gray;
		Color cRound = new Color(0, 0, 0, 100);
		Image image = getIcon("images/colors/green.png").getImage();
		int imagex = 0;
		if (colorh == 0) {
			image = getIcon("images/colors/green.png").getImage();
			imagex = (int) (x + size / 2 - image.getWidth(this) * 1.5 / 2);
			cDark = new Color(0, 150, 0);
			if (highlited)
				cDark = new Color(0, 200, 0);
			cLight = Color.green;

		}
		if (colorh == 1) {
			image = getIcon("images/colors/white.png").getImage();
			imagex = (int) (x + size / 2 - image.getWidth(this) * 1.5 / 2) + 6;
			cDark = new Color(150, 150, 150);
			if (highlited)
				cDark = new Color(200, 200, 200);
			cLight = Color.white;

		}
		if (colorh == 2) {
			image = getIcon("images/colors/blue.png").getImage();
			imagex = (int) (x + size / 2 - image.getWidth(this) * 1.5 / 2) + 3;
			cDark = new Color(0, 0, 150);
			if (highlited)
				cDark = new Color(50, 50, 200);
			cLight = Color.blue;

		}
		if (colorh == 3) {
			image = getIcon("images/colors/black.png").getImage();
			imagex = (int) (x + size / 2 - image.getWidth(this) * 1.5 / 2) + 3;
			cDark = new Color(50, 50, 50);
			if (highlited)
				cDark = new Color(90, 90, 90);
			cLight = new Color(100, 100, 100);

		}
		if (colorh == 4) {
			image = getIcon("images/colors/red.png").getImage();
			imagex = (int) (x + size / 2 - image.getWidth(this) * 1.5 / 2) + 4;
			cDark = new Color(150, 0, 0);
			if (highlited)
				cDark = new Color(200, 0, 0);
			cLight = new Color(255, 0, 0);

		}
		if (colorh == 5) {
			image = getIcon("images/colors/yellow.png").getImage();
			imagex = (int) (x + size / 2 - image.getWidth(this) * 1.5 / 2)+4;
			cDark = new Color(150, 150, 0);
			if (highlited)
				cDark = new Color(200, 200, 0);
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

		return g2;
	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}
}
