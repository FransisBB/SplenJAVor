package game.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import game.main.Board;
import game.main.Utils;
import game.obj.Card;
import ui.TButton;

public class CardSelectPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2633538609067144658L;
	public int[] cardIds = new int[2];
	public int selCards = 0;
	int lastID = 0;
	public List<Card> _cardList = new ArrayList<Card>();

	JScrollPane jf;
	ComponentAdapter ca;
	Board board;

	public CardSelectPanel(Card card, Board board, JPanel tempPanel, boolean reserved, int d, boolean second,
			int[] coinsNeeded) {
		this.ca = new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				update();
			}
		};
		this.board = board;
		this.jf = board.scrollCards;

		List<Card> _playerCardList = board.players[board.aPID]._cardList;
		int clr = card.discardColor;
		//setBorder(BorderFactory.createLineBorder(new Color(50, 50, 0, 255)));
		//setBackground(new Color(255, 255, 245));
		setOpaque(false);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;

		gbc.insets = new Insets(2, 2, 2, 2);

		boolean onlySpecial = true;

		TButton confirm = new TButton(board.lt("confirm"), "green", 14);
		confirm.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					if (selCards == 2) {
						clear();
						setCards(second);
						board.actionBuyCardFinishContinue(card, tempPanel, reserved, d, second, coinsNeeded);
					}
				}
			}
		});

		for (int i = 1; i < 3; i++) {
			// special 1->2
			for (Card t : _playerCardList) {
				if (t.special == i && t.color == clr) {
					Card nCard = Utils.copyCard(t, true, false);
					_cardList.add(nCard);
				}
			}
		}
		if (_cardList.size() < 2) {
			onlySpecial = false;
			for (int i = 0; i < 3; i++) {
				for (Card t : _playerCardList) {
					if (t.special != 1 && t.special != 2 && t.level == i && t.color == clr) {
						Card nCard = Utils.copyCard(t, true, false);
						_cardList.add(nCard);
					}
				}
			}
		}
		// JPanel temp = new JPanel(new FlowLayout(FlowLayout.CENTER,5,0));
		JPanel temp = new JPanel();
		temp.setLayout(new GridBagLayout());
		temp.setOpaque(false);
		
		GridBagConstraints gbcs = new GridBagConstraints();
		gbcs.insets = new Insets(5, 5, 5, 5);

		if (_cardList.size() == 2) {
			add(new JLabel("Te dwie karty musisz oddaæ:"), gbc);
			gbc.gridy = 1;
			for (Card t : _cardList) {
				cardIds[selCards] = t.id;
				temp.add(t,gbcs);
				selCards++;
			}
		} else {
			// TODO translate
			add(new JLabel("Wybierz karty do oddania:"), gbc);
			gbc.gridy = 1;
			MouseAdapter meAd = new MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					Card tCard = ((Card) me.getComponent());
					if (tCard.border == 2) {
						tCard.setBorder(0);
						selCards--;
						for (int i = 0; i < 2; i++) {
							if (cardIds[i] == tCard.id) {
								cardIds[i] = 999;
								break;
							}
						}
					} else if (tCard.border == 0 && !tCard.grayscale) {
						if (selCards < 2) {
							tCard.setBorder(2);
							selCards++;
							for (int i = 0; i < 2; i++) {
								if (cardIds[i] == 999) {
									cardIds[i] = tCard.id;
									lastID = tCard.id;
									break;
								}
							}
						}
					} else if (tCard.border == 0 && tCard.grayscale) {
						int toRem = 0;
						for (int i : cardIds) {
							if (i != lastID) {
								toRem = i;
								break;
							}
						}
						for (Card t : _cardList) {
							if (t.id == toRem && t.border == 3) {
								toRem = lastID;
							}
						}
						for (Card t : _cardList) {
							if (t.id == toRem) {
								for (int i = 0; i < 2; i++) {
									if (cardIds[i] == t.id) {
										cardIds[i] = tCard.id;
										lastID = tCard.id;
										break;
									}
								}
								t.setBorder(0);
								t.setGrayscale(true);
								tCard.setBorder(2);
								tCard.setGrayscale(false);

							}
						}
					}
					if (selCards == 2) {
						for (Card t : _cardList) {
							if (t.border == 3 || t.border == 2) {
								t.setGrayscale(false);
							} else {
								t.setGrayscale(true);
								// t.removeAllActions();
							}
						}
						confirm.setActive(true);
					} else {
						for (Card t : _cardList) {
							t.setGrayscale(false);
						}
						confirm.setActive(false);
					}
				}

			};
			
			int n = 0;
			boolean isSpecial = false;
			for (Card t : _cardList) {
				if (onlySpecial) {
					if (selCards < 2) {
						t.setBorder(2);
					} else {
						t.setGrayscale(true);
					}
					t.setActive();
					t.addMouseListener(meAd);
					gbcs.gridx = n % 5;
					gbcs.gridy = (int) Math.floor(n / 5);
					temp.add(t, gbcs);
					n++;
				} else if (!onlySpecial) {
					if (t.special == 1 || t.special == 2) {
						gbcs.gridy = 0;
						gbcs.gridx = 0;
						t.setBorder(3);
						JPanel filler = new JPanel();
						filler.setOpaque(false);
						filler.setPreferredSize(new Dimension(30, 150));
						temp.add(t, gbcs);
						n++;
						gbcs.gridx = 1;
						temp.add(filler);
						n++;
						isSpecial = true;
					} else {

						if (isSpecial) {
							if (n < 6) {
								gbcs.gridx = n;
								gbcs.gridy = 0;
							} else {
								gbcs.gridx = 2 + ((n - 6) % 4);
								gbcs.gridy = 1 + (int) Math.floor((n - 6) / 4);
							}
						} else {
							gbcs.gridx = n % 5;
							gbcs.gridy = (int) Math.floor(n / 5);
						}

						if (selCards < 2) {
							t.setBorder(2);
						} else {
							t.setGrayscale(true);
						}
						t.setActive();
						t.addMouseListener(meAd);
						temp.add(t, gbcs);
						n++;
					}

				}
				if (selCards < 2) {
					cardIds[selCards] = t.id;
					lastID = t.id;
					selCards++;
				}

			}

		}
		add(temp, gbc);

		JPanel buttons = new JPanel();
		buttons.setOpaque(false);

		TButton cancel = new TButton(board.lt("cancel"), "yellow", 14);
		cancel.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					clear();
					board.cancelAction(second);

				}
			}
		});
		if (selCards != 2)
			confirm.setActive(true);
		buttons.add(cancel);
		buttons.add(confirm);
		gbc.gridy = 2;
		add(buttons, gbc);
	}

	public void setCards(boolean second) {
		String str[] = new String[2];
		int[] fra = board.fra;
		int[] frc = board.frc;
		if (second) {
			frc[0] = cardIds[0];
			frc[1] = cardIds[1];
			for (int i = 0; i < 2; i++) {
				if (frc[i] < 10) {
					str[i] = "00" + frc[i];
				} else if (frc[i] < 100) {
					str[i] = "0" + frc[i];
				} else
					str[i] = "" + frc[i];
			}
			board.aB.frcAct = "BCC" + str[0];
			board.aB.frdAct = "BCD" + str[1];
		} else {
			fra[0] = cardIds[0];
			fra[1] = cardIds[1];
			for (int i = 0; i < 2; i++) {
				if (fra[i] < 10) {
					str[i] = "00" + fra[i];
				} else if (fra[i] < 100) {
					str[i] = "0" + fra[i];
				} else
					str[i] = "" + fra[i];
			}
			board.aB.fraAct = "BCA" + str[0];
			board.aB.frbAct = "BCB" + str[1];
		}
		board.paymentInfo.removeAll();
		JPanel t1 = new JPanel(new FlowLayout());
		JPanel t2 = new JPanel(new FlowLayout());
		t1.setOpaque(false);
		t2.setOpaque(false);
		t1.add(new JLabel(board.lt("pay_this_cards")));

		for (Card t : _cardList) {
			if (t.id == cardIds[0] || t.id == cardIds[1]) {
				t2.add(Utils.copyCard(t, true, false));
			}
		}
		board.paymentInfo.add(t1, BorderLayout.NORTH);
		board.paymentInfo.add(t2, BorderLayout.SOUTH);
		board.showPayment.add(board.paymentInfo);
		board.setActionPanelListener();
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
		board.exposeCards();
		board.clearAndAddInfoLabel("Czekam na wybór...");
		jf.add(this, 0);
		jf.repaint();
		jf.revalidate();
		jf.addComponentListener(ca);
	}

	public void clear() {
		board.removeCardsActions();
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
