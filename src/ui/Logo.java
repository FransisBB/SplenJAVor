package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class Logo extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8161591347465618585L;
	int size;

	public Logo(int s) {
		this.size = s;
		//setLayout(new FlowLayout());
		setOpaque(false);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2;
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Font font = new Font("Arial", Font.BOLD, size);
		FontMetrics metrics = g.getFontMetrics(font);
		
		//int fx = metrics.stringWidth("SplenJAVor");
		//int fy = 0;
		int fx=(this.getWidth()-metrics.stringWidth("SplenJAVor"))/2;
		int fy= (this.getHeight()-metrics.getHeight())/2+metrics.getAscent();
		g2.setFont(font);
		g2.setColor(new Color(0, 0, 0, 125));
		g2.drawString("SplenJAVor", fx + 1, fy + 1);
		g2.drawString("SplenJAVor", fx + 0, fy + 1);
		g2.drawString("SplenJAVor", fx + 1, fy + 0);
		g2.setColor(new Color(255, 255, 255, 125));
		g2.drawString("SplenJAVor", fx - 1, fy - 1);
		g2.drawString("SplenJAVor", fx - 0, fy - 1);
		g2.drawString("SplenJAVor", fx - 1, fy - 0);
		g2.setColor(new Color(255, 245, 0, 150));
		g2.drawString("Splen", fx+1, fy+1);
		fx+=metrics.stringWidth("Splen");
		g2.setColor(new Color(255, 145, 0, 150));
		g2.drawString("JAV", fx+1, fy+1);
		fx+=metrics.stringWidth("JAV");
		g2.setColor(new Color(255, 205, 0, 150));
		g2.drawString("or", fx+1, fy+1);
		//int h=metrics.getHeight();
		//setPreferredSize(new Dimension(metrics.stringWidth("SplenJAVor"),metrics.getHeight()));		
	}
}
