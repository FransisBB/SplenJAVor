package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import game.obj.Card;

public class STRPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -121911024633707504L;
	Timer timer;
	ActionListener al;
	boolean fr=false;
	public STRPanel(int n, Card cardA, Card cardB) {
		setLayout(null);
		setOpaque(false);
		setPreferredSize(new Dimension(208, 153));
		STRMove graph = new STRMove(n);
		cardA.setBounds(0, 0, 103, 153);
		cardB.setBounds(105, 0, 103, 153);
		graph.setBounds(66, 74, 75, 34);
		add(cardA, 0);
		add(cardB, 0);
		add(graph, 0);
		
		al = new ActionListener() {
			int tick = 0;
			public void actionPerformed(ActionEvent evt) {
				System.out.println(evt.getSource());
				if (tick == 0) {
					cardA.addStronghold(n);
					cardA.repaint();

					cardB.removeStronghold();
					cardB.repaint();
					tick = 1;
				} else if (tick==1){
					cardA.removeStronghold();
					cardA.repaint();

					cardB.addStronghold(n);
					cardB.repaint();
					tick = 2;
				} else if (tick>1) {
					tick=(tick+1)%4;
				}
			}
		};

	}

	public STRPanel(int n, Card card, int type) {
		setLayout(null);
		setOpaque(false);
		setPreferredSize(new Dimension(103, 153));
		card.setBounds(0, 0, 103, 153);
		add(card, 0);
		switch (type) {
		case 0:
			STRAdd graph = new STRAdd(n);
			graph.setBounds(33, 2, 34, 54);
			add(graph, 0);
			al = new ActionListener() {
				int tick = 0;
				public void actionPerformed(ActionEvent evt) {
					System.out.println(evt.getSource());
					if (tick == 0) {
						card.removeStronghold();
						card.repaint();
						tick = 1;
					} else if (tick==1){
						card.addStronghold(n);
						card.repaint();
						tick = 2;
					} else if (tick>1) {
						tick=(tick+1)%4;
					}
				}
			};
			break;
		case 2:
			STRUp graph2 = new STRUp(n);
			graph2.setBounds(33, 2, 34, 52);
			add(graph2, 0);
			al = new ActionListener() {
				int tick = 0;
				public void actionPerformed(ActionEvent evt) {
					System.out.println(evt.getSource());
					if (tick == 0) {
						card.addStronghold(n);
						card.repaint();
						tick = 1;
					} else if (tick==1) {
						card.removeStronghold();
						card.repaint();
						tick = 2;
					} else if (tick>1) {
						tick=(tick+1)%4;
					}
				}
			};
			break;
		default:
			break;
		}

	}

	public void timerRun() {
		fr=true;
		timer = new Timer(500,al);
		timer.setInitialDelay(500);
		timer.setRepeats(true);
		timer.start();
	}
	public void timerStop() {
		timer.stop();
	}
}
