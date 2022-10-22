package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

	public 	class MPanel extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -6933330694981286617L;
		public MPanel (String t) {
			TitledBorder title;
			Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
			title = BorderFactory.createTitledBorder(loweredetched, t);
			title.setTitleFont(new Font("Arial", Font.BOLD, 16));
			title.setTitleJustification(TitledBorder.CENTER);
			//title.setTitlePosition(TitledBorder.ABOVE_TOP);
			//setLayout(null);
			setOpaque(false);
			setBorder(title);
		}
		public void paintComponent(Graphics g) {
			
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			g2.setColor(new Color(255,255,245,150));
			g2.fillRect(2, 10, this.getWidth()-4, this.getHeight()-12);
			}
	}

