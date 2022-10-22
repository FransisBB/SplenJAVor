package game.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import game.main.Board;
import ui.TButton;

public class DefaultPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -26335609067144658L;

	JScrollPane jf;
	ComponentAdapter ca;
	Board board;

	public DefaultPanel(JScrollPane jf) {
		this.ca = new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				update();
			}
		};
		this.jf = jf;
		setOpaque(false);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.insets = new Insets(2, 2, 2, 2);
		
		TButton close = new TButton("Zamknij", "yellow", 14);
		close.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					clear();
				}
			}
		});
		gbc.insets = new Insets(2, 2, 6, 2);
		add(close,gbc);
	}

		
	public void update() {
		int widthp = getPreferredSize().width;
		int heightp = getPreferredSize().height;
		setBounds(2+(jf.getWidth() - widthp) / 2, (jf.getHeight() - heightp) / 2, widthp, heightp);
		jf.repaint();
		jf.revalidate();
	}

	public void showThis() {
		int widthp = getPreferredSize().width;
		int heightp = getPreferredSize().height;
		setBounds(2+(jf.getWidth() - widthp) / 2, (jf.getHeight() - heightp) / 2, widthp, heightp);
		jf.add(this, 0);
		jf.repaint();
		jf.revalidate();
		jf.addComponentListener(ca);
	}

	public void clear() {
		jf.removeComponentListener(ca);
		jf.remove(this);
		jf.repaint();
		jf.revalidate();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color(0, 0, 0, 50));
		g2.fillRoundRect(2, 2, this.getWidth() - 3, this.getHeight() - 3, 10, 10);
		g2.setColor(new Color(255, 255, 245));
		g2.fillRoundRect(1, 1, this.getWidth() - 4, this.getHeight() - 4, 10, 10);
		g2.setColor(new Color(50, 50, 0, 150));
		g2.drawRoundRect(1, 1, this.getWidth() - 4, this.getHeight() - 4, 10, 10);
	}

}
