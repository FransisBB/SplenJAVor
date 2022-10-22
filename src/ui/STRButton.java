package ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JLabel;
import javax.swing.Timer;

import game.main.Utils;
import game.obj.Player;

public class STRButton extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6664101050767582684L;

	private Color bgColor, actColor;
	private boolean exited;
	int t, strColor;

	public STRButton(int t, int strColor) {
		this.t = t;
		this.strColor = strColor;
		switch (t) {
		case 1:
		case 4:
			actColor = new Color(145, 255, 144, 225);
			break;
		case 2:
			actColor = new Color(255, 105, 104, 225);
			break;
		case 3:
			actColor = new Color(235, 235, 144, 225);
			break;
		case 5:
			actColor = new Color(255, 105, 104, 225);
			break;
		default:
			actColor = new Color(145, 255, 144);
			break;
		}
		bgColor = actColor;
		// this.font = new Font("Arial", Font.BOLD, size);
		this.setPreferredSize(new Dimension(30, 30));
		this.setOpaque(false);
		setListeners();
	}

	public STRButton(Player[] players) {
		t = 2;
		actColor = new Color(255, 105, 104, 225);
		bgColor = actColor;
		this.setPreferredSize(new Dimension(30, 30));
		this.setOpaque(false);
		int c = 0;
		for (int i = 0; i < players.length; i++) {
			if (!players[i].isLocal && players[i].str < 3)
				c++;
		}
		int d[] = new int[c];

		c = 0;
		for (int i = 0; i < players.length; i++) {
			if (!players[i].isLocal && players[i].str < 3) {
				if (c == 0)
					strColor = i;
				d[c] = i;
				c++;
			}

		}
		int e = c;
		Timer timer = new Timer(1000, new ActionListener() {
			int tick = 0;

			public void actionPerformed(ActionEvent evt) {
				System.out.println("TICK:" + tick);
				if (isDisplayable()) {
				strColor = d[tick % e];
				repaint();
				tick++;
				} else {
					Timer t = (Timer) evt.getSource();
					t.stop();
				}

			}
		});
		timer.setRepeats(true);
		timer.setInitialDelay(0);
		timer.start();
		setListeners();
	}

	public void setListeners() {
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (me.getX() > 0 && me.getX() < getWidth() && me.getY() > 0 && me.getY() < getHeight()) {
					exited = false;
					bgColor = actColor.darker();
					repaint();
				}
			}

			public void mouseReleased(MouseEvent me) {
				if (!exited) {
					bgColor = actColor.brighter();
					repaint();
				}
			}

			public void mouseEntered(MouseEvent me) {

				bgColor = actColor.brighter();
				repaint();
			}

			public void mouseDragged(MouseEvent me) {
				bgColor = actColor.darker();
				repaint();
			}

			public void mouseExited(MouseEvent me) {
				exited = true;
				bgColor = actColor;
				repaint();
			}
		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setFont(new Font("Arial", Font.BOLD, 18));
		int offs = 0;
		if (bgColor.equals(actColor.darker())) {
			offs = 1;
		}
		g2.setColor(new Color(0, 0, 0, 75));
		g2.fillRoundRect(1 - offs, 1 - offs, 28, 28, 10, 10);
		g2.setColor(bgColor);
		g2.fillRoundRect(offs, offs, 28, 28, 10, 10);

		g2.setColor(bgColor.darker().darker());
		if (t == 1) {
			g2.drawString("+", 2 + offs, 20 + offs);
			Image gIcon = Utils.scaleIcon(getIcon("images/tower" + strColor + ".png"), 17, 19).getImage();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
			g2.drawImage(gIcon, 10 + offs, 4 + offs, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		} else if (t == 2) {
			g2.drawString("-", 2 + offs, 18 + offs);
			Image gIcon = Utils.scaleIcon(getIcon("images/tower" + strColor + ".png"), 17, 19).getImage();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
			g2.drawImage(gIcon, 10 + offs, 4 + offs, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		} else if (t == 3) {
			Image gIcon = Utils.scaleIcon(getIcon("images/tower" + strColor + ".png"), 17, 19).getImage();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.40f));
			g2.drawImage(getIcon("images/arrowsOut.png").getImage(), 1 + offs, 1 + offs, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
			g2.drawImage(gIcon, 5 + offs, 4 + offs, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		} else if (t == 4) {
			Image gIcon = Utils.scaleIcon(getIcon("images/tower" + strColor + ".png"), 17, 19).getImage();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.40f));
			g2.drawImage(getIcon("images/arrowsIn.png").getImage(), 1 + offs, 1 + offs, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
			g2.drawImage(gIcon, 5 + offs, 4 + offs, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}

	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	public boolean releasedInside() {
		return !exited;
	}

}
