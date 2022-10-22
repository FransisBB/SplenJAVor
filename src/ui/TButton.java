package ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TButton extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6664101050767582684L;

	private Font font;
	private int width, height, ofsw = 5, ofsh = 5, bevel = 1, tOfs = 0;
	private Color bgColor, actColor, bvBot, bvTop, bvTopOrg = new Color(255, 255, 255, 100),
			bvBotOrg = new Color(0, 0, 0, 50);
	private String txt;
	private boolean exited;
	private boolean disabled = false;

	public TButton(String s, String c, int size, int... params) {
		if (params.length > 0) {
			ofsw = params[0];
			ofsh = params[0];
		}
		switch (c) {
		case "green":
			bgColor = new Color(145, 255, 144);
			break;
		case "blue":
			bgColor = new Color(144, 145, 255);
			break;
		case "red":
			bgColor = new Color(255, 145, 144);
			break;
		case "yellow":
			bgColor = new Color(235, 235, 144);
			break;
		default:
			bgColor = new Color(145, 255, 144);
			break;
		}
		this.font = new Font("Arial", Font.BOLD, size);
		FontMetrics metrics = getFontMetrics(this.font);
		if (params.length > 1) {
			this.width = params[1];
		} else {
			this.width = metrics.stringWidth(s);
		}
		this.height = metrics.getHeight();
		this.txt = s;

		this.actColor = bgColor;
		this.bvTop = bvTopOrg;
		this.bvBot = bvBotOrg;

		this.setPreferredSize(new Dimension(width + ofsw * 2 + 2 * bevel, height + ofsh * 2 + 2 * bevel));

		this.setOpaque(false);

		setListeners();
	}

	public void setListeners() {
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (me.getX() > 0 && me.getX() < getWidth() && me.getY() > 0 && me.getY() < getHeight()) {
					if (!disabled) {
						exited = false;
						bgColor = actColor.darker();
						bvTop = bvBotOrg;
						bvBot = bvTopOrg;
						tOfs = bevel;
						repaint();
					}
				}
			}

			public void mouseReleased(MouseEvent me) {
				if (!exited && !disabled) {
					bgColor = actColor.brighter();
					bvTop = bvTopOrg;
					bvBot = bvBotOrg;
					tOfs = 0;
					repaint();
				}
			}

			public void mouseEntered(MouseEvent me) {
				if (!disabled) {
					bgColor = actColor.brighter();
					bvTop = bvTopOrg;
					bvBot = bvBotOrg;
					tOfs = 0;
					repaint();
				}
			}

			public void mouseDragged(MouseEvent me) {
				if (!disabled) {
					bgColor = actColor.darker();
					bvTop = bvBotOrg;
					bvBot = bvTopOrg;
					if (!disabled)
						tOfs = bevel;
					repaint();
				}
			}

			public void mouseExited(MouseEvent me) {
				if (!disabled) {
					exited = true;
					bgColor = actColor;
					bvTop = bvTopOrg;
					bvBot = bvBotOrg;
					tOfs = 0;
					repaint();
				}
				
			}
		});
	}

	public void paintComponent(Graphics g) {
		removeAll();

		int total_width = ofsw * 2 + width;
		int total_height = ofsh * 2 + height;
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (disabled) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		} else {
			g2.setColor(bvTop);
			g2.fillRoundRect(0, 0, total_width + bevel, total_height + bevel, 10, 10);
			g2.fillRoundRect(0, 0, total_width + bevel, total_height + bevel, 10, 10);
			g2.fillRoundRect(0, 0, total_width + bevel, total_height + bevel, 10, 10);
			g2.setColor(bvBot);
			g2.fillRoundRect(bevel * 2, bevel * 2, total_width, total_height, 10, 10);
			g2.fillRoundRect(bevel * 2, bevel * 2, total_width, total_height, 10, 10);
			g2.fillRoundRect(bevel * 2, bevel * 2, total_width, total_height, 10, 10);
		}
		g2.setColor(bgColor);
		g2.fillRoundRect(bevel, bevel, total_width, total_height, 10, 10);

		// g2.setFont(font);
		// g2.setColor(new Color(0, 0, 0));
		// g2.drawString(txt, bevel + ofs/2 + tOfs, bevel + h_ascend + ofs / 2 + tOfs);
		JLabel label = new JLabel(txt);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setFont(font);
		label.setBounds(ofsw + bevel + tOfs - 1, ofsh + bevel + tOfs - 1, width + 2, height + 2);
		if (disabled) {
			label.setForeground(new Color(0, 0, 0, 125));
		}
		add(label);
		GradientPaint gp1 = new GradientPaint(0, 0, bvTop, 0, total_height, bvBot);
		g2.setPaint(gp1);
		g2.fillRoundRect(bevel, bevel, total_width, total_height, 10, 10);
	}

	public boolean releasedInside() {
		if (disabled)
			return false;
		return !exited;
	}

	public void setActive(boolean s) {
		disabled = !s;
		bgColor = actColor;
		bvTop = bvTopOrg;
		bvBot = bvBotOrg;
		tOfs = 0;
		repaint();
	}

}
