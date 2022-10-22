package game.box;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import game.main.Utils;
import game.obj.CoinsStack;

public class CoinsDealt extends JPanel {
	private static final long serialVersionUID = 7840983140225480463L;

	public CoinsStack[] coinsStack;

	public CoinsDealt(int coinsNum) {
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(layout);
		setOpaque(false);
		coinsStack = new CoinsStack[6];
		gbc.insets = new Insets(0, 5, 1, 5);
		
		for (int i = 0; i < 5; i++)
			this.coinsStack[i] = new CoinsStack(coinsNum, 0, i);
		this.coinsStack[5] = new CoinsStack(5, 0, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(coinsStack[0], gbc);
		gbc.gridx = 1;
		this.add(coinsStack[1], gbc);
		gbc.gridx = 2;
		this.add(coinsStack[2], gbc);
		gbc.gridx = 0;
		//gbc.insets = new Insets(-50, 5, 5, 5);
		gbc.gridy = 1;
		this.add(coinsStack[3], gbc);
		gbc.gridx = 1;
		this.add(coinsStack[4], gbc);
		gbc.gridx = 2;
		this.add(coinsStack[5], gbc);
	}

	public void takeCoins(int[] coins) {
		for (int i = 0; i < coins.length; i++) {
			coinsStack[i].takeCoin(coins[i]);
		}
	}

	public void addCoins(int[] coins) {
		for (int i = 0; i < coins.length; i++) {
			coinsStack[i].addCoin(coins[i]);
		}
	}

	public void coinsUp(int[] coins) {
		for (int i = 0; i < coins.length; i++) {
			coinsStack[i].coinUp(coins[i]);
		}
	}

	public void allDown(boolean t) {
		for (CoinsStack p : coinsStack)
			p.coinsDown(t);
	}

	public void setActive(int n) {
		coinsStack[n].setActive(true);
		repaint();
	}

	public void removeAllActions() {
		for (CoinsStack p : coinsStack)
			p.removeAllActions();
		repaint();
	}

	public int countUp() {
		int n = 0;
		for (CoinsStack p : coinsStack)
			n += p.numUp;
		return n;
	}

	protected void paintComponent(Graphics g) {

		// Allow super to paint
		super.paintComponent(g);

		// Apply our own painting effect
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// 50% transparent Alpha
		// g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2d.setColor(new Color(0, 0, 0, 30));
		g2d.fillRoundRect((getWidth()-205)/2-1, (getHeight()-275)/2+4, 205, 270, 30, 30);
		g2d.setColor(new Color(255, 255, 240, 180));
		g2d.fillRoundRect((getWidth()-205)/2+1, (getHeight()-275)/2+6, 205, 270, 30, 30);
		g2d.setColor(Color.BLACK);
		// g2d.drawRect(getWidth()/2-x/2, getHeight()/2-y/2+12, x, y);
		boolean draw = false;
		for (int i = 0; i < 5; i++) {
			if (coinsStack[i].isActive) {
				draw = true;
			}
		}
		if (draw) {
			Image gIcon = Utils.scaleIcon(getIcon("images/coinHelp.png"), 133, 35).getImage();
			g2d.drawImage(gIcon, getWidth() / 2 - 133 / 2, getHeight() / 2 - 35 / 2 + 12, this);
		}
		g2d.dispose();

	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}
}
