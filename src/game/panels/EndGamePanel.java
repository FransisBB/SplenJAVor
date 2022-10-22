package game.panels;

import java.awt.Color;
import java.awt.Font;
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
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import game.main.Board;
import game.obj.Player;
import ui.TButton;

public class EndGamePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -26335609067144658L;

	JScrollPane jf;
	ComponentAdapter ca;
	Board board;

	public EndGamePanel(Board board) {
		this.ca = new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				update();
			}
		};
		this.board=board;
		this.jf = board.scrollCards;
		setOpaque(false);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.insets = new Insets(6, 2, 2, 2);
		
		gbc.gridwidth=6;
		JLabel top = new JLabel("<html><center><b>"+lt("game_score")+"</b></center></html>");
		top.setFont(new Font("Arial", Font.PLAIN, 16));
		add(top,gbc);
			
		ArrayList<Player> list = new ArrayList<>();
		for (Player p : board.players)
			list.add(p);
		Collections.sort(list, Collections.reverseOrder());
		boolean winner = true;
		
		gbc.gridy=1;
		for (Player p : list) {
			String text="";
			if (winner) {
					text += "<b>" + p.playerName + "</b> " + lt(p.gender,"g_win") + " " + lt("and") + " " + lt(p.gender,"g_got");
				winner = false;
			} else
				text += "<b>" + p.playerName + "</b> " + lt(p.gender,"g_got");
			if (p.haveCity)
				text += " " + lt("city_and");

			String points;
			switch (p.points) {
			case 1:
				points = lt("point");
				break;
			case 2:
			case 3:
			case 4:
				points = lt("points2to4");
				break;
			default:
				points = lt("points5more");
			}
			String cards;
			switch (p.cardSum()) {
			case 1:
				cards = lt("card");
				break;
			case 2:
			case 3:
			case 4:
				cards = lt("cards2to4");
				break;
			default:
				cards = lt("cards5more");
			}

			text += " <b>" + p.points + "</b> " + points + " (<b>" + p.cardSum() + "</b> " + cards + ")<br><br>";
			String labelText = String.format("<html><div style=\"width:%dpx;\"><center>%s</center></div></html>", 300,
					 text);
			JLabel label = new JLabel(labelText);
			label.setFont(new Font("Arial", Font.PLAIN, 16));
			
			add(label,gbc);
			gbc.gridy++;
		}
		TButton close = new TButton(lt("close"), "yellow", 14);
		close.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					clear();
				}
			}
		});
		gbc.insets = new Insets(2, 2, 6, 2);
		add(close,gbc);
		//JPanel endGame = new JPanel();
		//endGame.setOpaque(false);
		 
		
		
		//endGame.add(label);
		//add(endGame,gbc);
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
	private String lt (String s) {
		return board.lt(s);
	}
	private String lt (int g, String s) {
		return board.lt(g,s);
	}
}
