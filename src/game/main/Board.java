package game.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicScrollBarUI;

import game.box.AristocratsDealt;
import game.box.CardsDealt;
import game.box.ChatPanel;
import game.box.CitiesDealt;
import game.box.CoinsDealt;
import game.box.InfoPanel;
import game.box.LocalPlayerPanel;
import game.box.PlayersPanel;
import game.obj.Aristocrat;
import game.obj.Card;
import game.obj.Chair;
import game.obj.City;
import game.obj.CoinsStack;
import game.obj.Player;
import game.panels.CardSelectPanel;
import game.panels.ColorSelectPanel;
import game.panels.EndGamePanel;
import ui.ArrButton;
import ui.DoubleCoin;
import ui.LightPanel;
import ui.NoAriRes;
import ui.NoFreeCard;
import ui.STRButton;
import ui.STRPanel;
import ui.SmallCoinStack;
import ui.TButton;

//TODO 
//Info przy wyborze platnosci -> zetony/karty
//EndGame - statystyki
//Napis orient na dole na rezerwacjach
//Ikonki rezerwacji w playersPanel
//sprawdziæ timery w sytuacji rezerwacji i potem zdobycia arystokraty innego - nie znalaz³em b³êdu
//JCheckboxy w Game
public class Board {
	public CardsDealt[] cardsDealt;
	public AristocratsDealt aristocratsDealt;
	CitiesDealt citiesDealt;
	CoinsDealt coinsDealt;

	public Player[] players;

	public ChatPanel chatPanel;
	public PlayersPanel playersPanel;
	public LocalPlayerPanel localPlayerPanel;
	public JPanel actionPanel, actionPanelContent, actionPanelHeader, actionPanelButtons;
	JPanel boardPanel;
	JPanel decks;
	public JPanel paymentInfo;
	JPanel rightPanel;
	public JScrollPane scrollCards;
	public int aPID;
	int ariGet;
	int cityGet;
	int mPos = 0;
	int specialModifier;
	int specialModifierB;
	int dblc = 0;
	int dbld = 0;
	public int fra[] = new int[4], frb[] = new int[4], frc[] = new int[4], frd[] = new int[4];
	int strTypeA, strAD, strAR, strAC, strAD2, strAR2, strAC2, strTypeB, strBD, strBR, strBC;
	int strCardD, strCardR, strCardC, strCoinsNeeded[] = new int[6], strCoinsBack[] = new int[6];
	// str[0, 1, 2 ],D,R,C,D2,R2,C2,[0, 2 ],D,R,C,ifBuy;
	// str[ADD,MOVE,REMOVE],D,R,C,D2,R2,C2,[ADD,REMOVE],D,R,C,ifBuy;
	public JPopupMenu showPayment;
	// Popup showPanel;
	public Game game;
	public ActionBuilder aB;
	String[] thinkText;
	boolean isRecovery, isLastRound = false, readyForRestart = false, strBuy;
	boolean optRandom, optBuyOnly, optMoreAri, optOrient, optMoreCoins;
	public boolean optCities, optAris, optStrongholds = true;

	Timer scrollAnim;

	public boolean debug = false;

	public Board(Game game, boolean isRecovery) {

		this.isRecovery = isRecovery;
		this.game = game;

		this.showPayment = new JPopupMenu();
		this.showPayment.setBorder(BorderFactory.createEmptyBorder());
		this.showPayment.setOpaque(false);
		this.showPayment.setBorderPainted(false);
		this.showPayment.setVisible(false);
		this.showPayment.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent event) {
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent event) {
			}

			public void popupMenuWillBecomeVisible(PopupMenuEvent event) {
				Point p = actionPanel.getLocationOnScreen();
				showPayment.setInvoker(actionPanel);
				showPayment.setLocation(p.x - showPayment.getPreferredSize().width, p.y);
			}
		});

		optRandom = game.options[0].isSelected();
		optBuyOnly = game.options[1].isSelected();
		optMoreAri = game.options[2].isSelected();
		optStrongholds = game.options[3].isSelected();
		optCities = game.options[4].isSelected();
		optOrient = game.options[5].isSelected();
		optAris = !game.options[6].isSelected();
		optMoreCoins = game.options[8].isSelected();

		int playersNumber = 0;
		for (Chair p : game.chairs)
			if (p.taken)
				playersNumber++;
		players = new Player[playersNumber];
		int x = 0;
		for (int i = 0; i < game.maxPlayers; i++) {
			if (game.chairs[i].taken) {
				if (!isRecovery) {
					players[x] = new Player(game.chairs[i].nick, i == game.localID, game.chairs[i].gender, this);
				} else {
					players[x] = new Player(game.chairs[i].nick, false, game.chairs[i].gender, this);
				}
				x++;
			}
		}
		if (optRandom) {
			List<Player> intList = Arrays.asList(players);
			Collections.shuffle(intList, new Random(game.seed));
			intList.toArray(players);
		}
		for (int i = 0; i < playersNumber; i++) {
			if (players[i].isLocal) {
				game.sendAction("ID" + i);
				game.localID = i;
				System.out.println(players[i].playerName + ":" + game.localID);
			}

		}

		this.thinkText = new String[] { "myœli...", "kontempluje...", "próbuje ogarn¹æ...", "zastanawia siê...",
				"obmyœla plan...", "g³ówkuje...", "tworzy strategiê...", "kombinuje...", "(nie)wie co zrobiæ..." };

		makeBoardLayout(game.seed, playersNumber);
		if (isRecovery) {
			startGame();
		} else if (game.readyCheck())
			startGame();
	}

	public void startGame() {
		aPID = 0;
		if (isRecovery) {
			game.isStarted = false;
			game.sendAction("ASKONLINE");
		} else {
			game.isStarted = true;
			startTurn(true);
		}
	}

	public void startTurn(boolean beep) {
		game.localClient.readThread.tContinue();
		if (isLastRound && aPID == 0) {
			playersPanel.setActive(4);
			endGame(true);
		} else {
			playersPanel.setActive(aPID);
			if (isRecovery) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						game.nextRecStep();
					}
				});
			}
			if (players[aPID].isLocal) {
				if (beep) {
					playSound("sounds/beep.wav");
				}
				aB = new ActionBuilder(aPID);
				if (!isLastRound) {
					clearAndAddInfoLabel(lt("your_turn"));
				} else {
					if (optCities) {
						clearAndAddInfoLabel(lt("your_turn_last_cities"));
					} else
						clearAndAddInfoLabel(lt("your_turn_last"));
				}
				TButton confirmPass = new TButton(lt("pass"), "green", 14);
				confirmPass.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent me) {
						if (((TButton) me.getComponent()).releasedInside()) {
								endTurnP1(false);
						}

					}
				});
				TButton cancelPass = new TButton(lt("cancel"), "yellow", 14);
				cancelPass.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent me) {
						if (((TButton) me.getComponent()).releasedInside()) {
							cancelAction(false);
						}

					}
				});
				TButton pass = new TButton(lt("pass"), "yellow", 14);
				pass.addMouseListener(new MouseAdapter() {

					public void mouseReleased(MouseEvent me) {
						if (((TButton) me.getComponent()).releasedInside()) {
							setActionType(3);
							clearAndAddInfoLabel("Jesteœ "+(players[aPID].gender==1?"pewieñ ":"pewna ")+"¿e chcesz spasowaæ i pomin¹æ turê?");
							actionPanelButtons.add(cancelPass);
							actionPanelButtons.add(confirmPass);
						}

					}
				});
				actionPanelContent.add(pass);
				actionPanelContent.repaint();
				actionPanelContent.revalidate();
				setAllCardsActions();
				setAllCoinsStacksActions(true);

				Timer timer = new Timer(500, new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						game.sendAction("click0");
					}
				});
				timer.setRepeats(false);
				timer.start();

			} // else {
				// playersPanel.clearContent(aPID);
				// }
		}
	}

	public void endTurnP1(boolean didBuy) {
		players[aPID].confirmAction();
		if (didBuy)
			removeCardsActions();
		coinsDealt.removeAllActions();
		coinsDealt.allDown(isRecovery);
		if (optStrongholds) {
			strongholdsAction(didBuy, false, false);
		} else {
			endTurnP2();
		}

	}

	public void endTurnP1second(boolean giveback) {
		players[aPID].confirmAction();
		removeCardsActions();
		coinsDealt.removeAllActions();
		coinsDealt.allDown(isRecovery);
		strongholdsAction(true, true, giveback);
	}

	public void endTurnP2() {
		players[aPID].confirmAction();
		boolean check = confirmCardsMove();
		removeCardsActions();
		coinsDealt.removeAllActions();
		coinsDealt.allDown(isRecovery);
		if (!isRecovery && (optAris || optCities) && check) {
			// TODO translate
			clearAndAddInfoLabel("Wyk³adam karty...");
			Timer timer = new Timer(1500, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					endTurnP2second();
				}
			});
			timer.setRepeats(false);
			timer.start();
		} else
			endTurnP2second();

	}

	public void endTurnP2second() {
		if (optAris) {
			ariCheck();
		} else if (optCities) {
			cityCheck(false);
		} else {
			endTurnP3(false);
		}
	}

	public void removeCardsActions() {
		for (CardsDealt t : cardsDealt) {
			t.removeAllActions();
		}
	}

	public boolean confirmCardsMove() {
		boolean ret = false;
		for (CardsDealt t : cardsDealt) {
			if (t.confirmMove(!isRecovery))
				ret = true;
		}
		return ret;
	}

	public void cancelCardsMove() {
		for (CardsDealt t : cardsDealt) {
			t.cancelMove();
		}
	}

	public void ariCheck() {
		aristocratsDealt.removeAllActions();
		boolean doTimer = false;
		int count = 0, n = 0;
		for (int i = 0; i < aristocratsDealt.ariNum; i++) {
			if (aristocratsDealt.checkIfTake(players[aPID].production, i, aPID)) {
				// Dodanie info o akcji
				if (!doTimer) {
					if (players[aPID].isLocal) {
						clearAndAddInfoLabel(optMoreAri ? lt("g_you_got") : lt("ari_choose"));
					} else
						clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_got") + ":");
				}
				// Dodanie ARI
				if (optMoreAri) {
					takeAri(i);
				} else if (players[aPID].isLocal) {
					count++;
					n = i;
					int j = i;
					Aristocrat temp = aristocratsDealt._arilist.get(i);
					temp.setActive();
					temp.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent me) {
							aristocratsDealt.removeAllActions();
							temp.border = 2;
							temp.repaint();
							TButton accept = new TButton(lt("confirm"), "green", 11, 5);
							accept.addMouseListener(new MouseAdapter() {
								public void mouseReleased(MouseEvent me) {
									if (((TButton) me.getComponent()).releasedInside()) {
										if (optCities) {
											rightPanel.removeAll();
											GridBagConstraints gbc = new GridBagConstraints();
											gbc.fill = GridBagConstraints.BOTH;
											gbc.gridx = 0;
											gbc.gridy = 0;
											gbc.weighty = 1;
											gbc.insets = new Insets(0, 0, 0, 30);
											rightPanel.add(aristocratsDealt, gbc, 0);
											gbc.insets = new Insets(0, 30, 0, 0);
											rightPanel.add(citiesDealt, gbc, 0);

										}
										clearAndAddInfoLabel(lt("g_you_got"));
										takeAri(j);
										aB.ariAct = "ARI" + j;
										if (optCities) {
											cityCheck(true);
										} else
											endTurnP3(true);
									}
								}
							});

							TButton back = new TButton(lt("back"), "yellow", 11, 5);
							back.addMouseListener(new MouseAdapter() {
								public void mouseReleased(MouseEvent me) {
									if (((TButton) me.getComponent()).releasedInside()) {
										ariCheck();
									}
								}
							});
							back.setBounds((85 - back.getPreferredSize().width) / 2,
									85 - (back.getPreferredSize().height + 2), back.getPreferredSize().width + 1,
									back.getPreferredSize().height + 1);
							temp.add(back);
							accept.setBounds((85 - accept.getPreferredSize().width) / 2,
									85 - (accept.getPreferredSize().height + 2) * 2,
									accept.getPreferredSize().width + 1, accept.getPreferredSize().height + 1);
							temp.add(accept);
						}
					});
				} else if (ariGet == i) {
					takeAri(i);
				}
				doTimer = true;
			}
		}
		if (players[aPID].isLocal && !optMoreAri) {
			if (count == 1) {
				clearAndAddInfoLabel(lt("g_you_got"));
				takeAri(n);
				aB.ariAct = "ARI" + n;
				if (optCities) {
					cityCheck(true);
				} else
					endTurnP3(true);
			} else if (count > 1) {
				if (optCities) {
					rightPanel.removeAll();
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.fill = GridBagConstraints.BOTH;
					gbc.gridx = 0;
					gbc.gridy = 0;
					gbc.weighty = 1;
					gbc.insets = new Insets(0, 30, 0, 0);
					rightPanel.add(citiesDealt, gbc, 0);
					gbc.insets = new Insets(0, 0, 0, 30);
					rightPanel.add(aristocratsDealt, gbc, 0);

				}
			}
			if (!doTimer) {
				if (optCities) {
					cityCheck(false);
				} else
					endTurnP3(false);
			}
		} else {
			if (optCities) {
				cityCheck(doTimer);
			} else
				endTurnP3(doTimer);
		}
	}

	public void cityCheck(boolean isAri) {
		if (optAris) {
			aristocratsDealt.removeAllActions();
		}
		if (players[aPID].isLocal && isAri) {
			boolean doTimer = false;
			for (int i = 0; i <= 2; i++) {
				if (citiesDealt.checkIfTake(players[aPID].production, players[aPID].points, i)) {
					doTimer = true;
					break;
				}
			}
			Timer timer = new Timer(doTimer ? 3000 : 0, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					cityCheckContinue(isAri);
				}
			});
			timer.setRepeats(false);
			timer.start();
		} else
			cityCheckContinue(isAri);

	}

	public void cityCheckContinue(boolean isAri) {
		citiesDealt.removeAllActions(2);
		boolean doTimer = false;
		int count = 0, n = 0;
		for (int i = 0; i <= 2; i++) {
			if (citiesDealt.checkIfTake(players[aPID].production, players[aPID].points, i)) {
				// Dodanie info o akcji
				if (!doTimer) {
					if (players[aPID].isLocal) {
						clearAndAddInfoLabel(lt("city_choose"));
					} else if (!isAri)
						clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_got"));
				}
				// Dodanie CITY
				if (players[aPID].isLocal) {
					count++;
					n = i;
					int j = i;
					City temp = citiesDealt._citylist.get(i);
					temp.setActive();
					temp.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent me) {
							citiesDealt.removeAllActions(2);
							temp.border = 2;
							temp.repaint();
							TButton accept = new TButton(lt("confirm"), "green", 11, 5);
							accept.addMouseListener(new MouseAdapter() {
								public void mouseReleased(MouseEvent me) {
									if (((TButton) me.getComponent()).releasedInside()) {
										clearAndAddInfoLabel(lt("g_you_got"));
										takeCity(j);
										aB.cityAct = "CITY" + j;
										endTurnP3(true);
									}
								}
							});

							TButton back = new TButton(lt("back"), "yellow", 11, 5);
							back.addMouseListener(new MouseAdapter() {
								public void mouseReleased(MouseEvent me) {
									if (((TButton) me.getComponent()).releasedInside()) {
										cityCheckContinue(isAri);
									}
								}
							});
							back.setBounds((85 - back.getPreferredSize().width) / 2,
									85 - (back.getPreferredSize().height + 2), back.getPreferredSize().width + 1,
									back.getPreferredSize().height + 1);
							temp.add(back);
							accept.setBounds((85 - accept.getPreferredSize().width) / 2,
									85 - (accept.getPreferredSize().height + 2) * 2,
									accept.getPreferredSize().width + 1, accept.getPreferredSize().height + 1);
							temp.add(accept);
						}
					});
				} else if (cityGet == i) {
					takeCity(i);
				}
				doTimer = true;
			}
		}
		if (players[aPID].isLocal) {
			if (count == 1) {
				clearAndAddInfoLabel(lt("g_you_got"));
				takeCity(n);
				aB.cityAct = "CITY" + n;
				endTurnP3(true);
			}
			if (!doTimer)
				endTurnP3(isAri);
		} else
			endTurnP3(doTimer || isAri);
	}

	public void endTurnP3(boolean doTimer) {
		dblc = 0;
		dbld = 0;
		if (players[aPID].isLocal) {
			if (doTimer) {
				if (optAris) {
					aristocratsDealt.removeAllActions();
				}
				if (optCities) {
					citiesDealt.removeAllActions(2);
				}

			}
			System.out.println("FINAL:");
			game.sendAction(aB.doString());
			// TODO Zliczanie
		}
		checkWin();

		Timer timer = new Timer((!doTimer || isRecovery) ? 0 : 3000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (players[aPID].isLocal) {
					clearAndAddInfoLabel(lt("g_you_end_turn"));
				} else if (doTimer)
						addHistory(false);
				aPID = (aPID + 1) % players.length;
				startTurn(true);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

	public void checkWin() {
		if ((players[aPID].points >= 15 && !optCities) || players[aPID].haveCity) {
			isLastRound = true;
			playersPanel.setLastRound();
		}
	}

	public void endGame(boolean ft) {

		if (ft) {
			EndGamePanel egp = new EndGamePanel(this);
			egp.showThis();
		}
		clearAndAddInfoLabel("Gra zakoñczona!");

		TButton closeApp = new TButton(lt("exit"), "red", 14);
		closeApp.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					game.closeTry();
				}
			}
		});

		TButton cancelNewGame = new TButton(lt("cancel"), "yellow", 14);

		Timer timer = new Timer(1000, new ActionListener() {
			int tick = 0;
			boolean listener = true;

			public void actionPerformed(ActionEvent evt) {
				tick++;
				Timer t = (Timer) evt.getSource();
				if (game.serverReady) {
					game.restart();
					t.stop();
				} else {
					String dots = " .";
					for (int i = 0; i < tick % 10; i++)
						dots = dots + " .";
					clearAndAddInfoLabel(lt("wait_server") + "<br>" + dots);
					if (listener) {
						cancelNewGame.addMouseListener(new MouseAdapter() {
							public void mouseReleased(MouseEvent me) {
								if (((TButton) me.getComponent()).releasedInside()) {
									t.stop();
									endGame(false);

								}
							}
						});
						listener = false;
					}
					actionPanelButtons.add(cancelNewGame);
					actionPanelButtons.add(closeApp);
				}

			}

		});
		timer.setRepeats(true);
		timer.setInitialDelay(0);

		TButton newGame = new TButton(lt("new_game"), "green", 14);
		newGame.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					if (game.isServer) {
						game.restart();
					} else if (game.isOwner){
						
					} else {
						timer.start();
					}
				}
			}
		});
		actionPanelButtons.add(newGame);
		actionPanelButtons.add(closeApp);
	}

	public void takeAri(int n) {
		Aristocrat tempAri = aristocratsDealt._arilist.get(n);
		tempAri.isAvailable(false);
		tempAri.isReserved = false;
		tempAri.showFront = true;
		tempAri.hideAri = false;
		players[aPID].addPoints(tempAri.reward);
		aristocratsDealt.remove(tempAri);
		aristocratsDealt.repaint();
		actionPanelContent.add(tempAri);
		actionPanelContent.repaint();
		actionPanelContent.revalidate();
		if (players[aPID].isLocal) {
			Aristocrat rewAri = new Aristocrat(tempAri.imgPath, tempAri.reward, tempAri.req);
			localPlayerPanel.addReward(rewAri);
		}
		Aristocrat rewAriB = new Aristocrat(tempAri.imgPath, tempAri.reward, tempAri.req);
		playersPanel.addReward(rewAriB, aPID);
	}

	public void takeCity(int n) {
		City tempCity = citiesDealt._citylist.get(n);
		tempCity.isAvailable(false);
		// players[aPID].addPoints(20);
		players[aPID].haveCity = true;
		citiesDealt.remove(tempCity);
		citiesDealt.repaint();
		actionPanelContent.add(tempCity);
		actionPanelContent.repaint();
		actionPanelContent.revalidate();
		if (players[aPID].isLocal) {
			City rewCity = new City(tempCity.imgPath, tempCity.tile, tempCity.reqPoints, tempCity.req);
			localPlayerPanel.addReward(rewCity);
		}
		City rewCityB = new City(tempCity.imgPath, tempCity.tile, tempCity.reqPoints, tempCity.req);
		playersPanel.addReward(rewCityB, aPID);
		// isLastRound = true;
		// playersPanel.setLastRound();
	}

	public void setActionType(int i) {
		aB.action = i;
		switch (i) {
		case 0:// karty
			game.sendAction("click1");
			playersRepaint();
			removeCardsActions();
			coinsDealt.removeAllActions();
			coinsDealt.allDown(true);
			break;
		case 1:// ¿etony
			game.sendAction("click2");
			playersRepaint();
			removeCardsActions();
			break;
		case 3:// pass
			playersRepaint();
			removeCardsActions();
			coinsDealt.removeAllActions();
			coinsDealt.allDown(true);
			break;
		}
	}

	public void setAllCardsActions() {
		for (int i = 0; i < cardsDealt.length; i++) {
			for (int r = 0; r < cardsDealt[i].cardsOnBoard.length; r++) {
				for (int c = 0; c < cardsDealt[i].cardsOnBoard[r].length; c++) {
					if (cardsDealt[i].cardsOnBoard[r][c] != 9999)
						setCardAction(r, c, i);
				}
			}
		}
		for (int i = 0; i < players[aPID].reservedCards; i++) {
			setResCardAction(i);
		}
	}

	public void exposeCards(Card... card) {
		boolean setGrayscale;
		for (int o = 0; o < cardsDealt.length; o++) {
			for (int p = 0; p < cardsDealt[o].cardsOnBoard.length; p++) {
				for (int q = 0; q < cardsDealt[o].cardsOnBoard[p].length; q++) {
					if (cardsDealt[o].cardsOnBoard[p][q] != 9999) {
						setGrayscale = true;
						for (Card c : card) {
							if (cardsDealt[o].getCard(cardsDealt[o].cardsOnBoard[p][q]).equals(c)) {
								setGrayscale = false;
								break;
							}
						}
						cardsDealt[o].getCard(cardsDealt[o].cardsOnBoard[p][q]).setGrayscale(setGrayscale);
					}
				}
			}
		}
	}

	public void setCardAction(int r, int c, int d) {
		Card card = getCard(cardsDealt[d].cardsOnBoard[r][c], d);
		if (optStrongholds && (card.strColor != aPID && card.strColor != 9))
			return;
		if (!canBuy(card)) {
			if (players[aPID].reservedCards != 3) {
				// rezerwacja
				card.setBorder(1);
				card.setActive();
				card.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent me) {
						setActionType(0);
						actionReserveCard(r, c, d);
					}
				});
			}
		} else {
			card.setBorder(2);
			card.setActive();
			if (players[aPID].reservedCards != 3) {
				// kupno lub rezerwacja
				card.addMouseListener(new MouseAdapter() {

					public void mousePressed(MouseEvent me) {
						setActionType(0);
						// TODO translate
						clearAndAddInfoLabel(
								"Wybierz jedn¹ z akcji przyciskami na karcie, a potem potwierdŸ swój wybór.");
						if (optBuyOnly) {
							actionBuyCard(r, c, d, false, false);
						} else {
							TButton res = new TButton(lt("res"), "yellow", 13, 3, 70);
							res.addMouseListener(new MouseAdapter() {
								public void mouseReleased(MouseEvent me) {
									if (((TButton) me.getComponent()).releasedInside()) {
										removeCardsActions();
										actionReserveCard(r, c, d);
									}
								}
							});
							TButton buy = new TButton(lt("buy"), "green", 13, 3, 80);
							buy.addMouseListener(new MouseAdapter() {
								public void mouseReleased(MouseEvent me) {
									if (((TButton) me.getComponent()).releasedInside()) {
										removeCardsActions();
										actionBuyCard(r, c, d, false, false);
									}
								}
							});

							TButton back = new TButton(lt("cancel"), "red", 13, 2);
							back.addMouseListener(new MouseAdapter() {
								public void mouseReleased(MouseEvent me) {
									if (((TButton) me.getComponent()).releasedInside()) {
										removeCardsActions();
										cancelAction(false);
									}
								}
							});
							exposeCards(card);
							card.setBorder(3);
							back.setBounds((102 - back.getPreferredSize().width) / 2,
									150 - (back.getPreferredSize().height + 2), back.getPreferredSize().width + 1,
									back.getPreferredSize().height + 1);
							card.add(back);
							res.setBounds((102 - res.getPreferredSize().width) / 2,
									150 - (res.getPreferredSize().height + 2) * 2, res.getPreferredSize().width + 1,
									res.getPreferredSize().height + 1);
							card.add(res);
							buy.setBounds((102 - buy.getPreferredSize().width) / 2,
									150 - (buy.getPreferredSize().height + 2) * 3, buy.getPreferredSize().width + 1,
									buy.getPreferredSize().height + 1);
							card.add(buy);
						}
					}
				});
			} else {
				// kupno
				card.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent me) {
						setActionType(0);
						actionBuyCard(r, c, d, false, false);
					}
				});
			}

		}
	}

	public boolean canBuy(Card card) {
		return (card.show && ((card.special == 5 && players[aPID].ownedCards[card.discardColor] >= 2)
				|| (card.special != 5 && countLack(card) <= players[aPID].ownedCoins[5] + players[aPID].gratisCoins
						&& (((card.special == 1 || card.special == 2) && players[aPID].cardSum() > 0)
								|| (card.special != 1 && card.special != 2)))));

	}

	public void setResCardAction(int n) {
		Card card = players[aPID].resCard[n];
		if (canBuy(card)) {
			card.setBorder(2);
			card.setActive();
			card.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					setActionType(0);
					actionBuyCard(n, n, 0, true, false);
				}
			});
		}
	}

	public void actionBuyCard(int r, int c, int d, boolean reserved, boolean second, int... coinsNeeded) {
		Card card;
		if (reserved) {
			players[aPID].takeResCard(r);
			card = players[aPID].clCard;
		} else {
			cardsDealt[d].takeCard(r, c, 0);
			card = getCard(cardsDealt[d].cardsOnBoard[r][c], d);
		}
		if (players[aPID].isLocal && (card.special == 1 || card.special == 2)) {
			ColorSelectPanel colorPanel = new ColorSelectPanel(this, card);
			colorPanel.setup(r, c, d, reserved, second);
			colorPanel.showThis();
			return;
		}
		actionBuyCardContinue(card, r, c, d, reserved, second, coinsNeeded);

	}

	public void actionBuyCardContinue(Card card, int r, int c, int d, boolean reserved, boolean second,
			int... coinsNeeded) {
		if (players[aPID].isLocal) {
			dblc = 0;
			dbld = 0;
			coinsNeeded = players[aPID].checkCoinsNeeded(card.cost);
		}
		int[] coinsNeededOrigin = new int[6];
		for (int i = 0; i < 6; i++)
			coinsNeededOrigin[i] = coinsNeeded[i];
		int sum = IntStream.of(coinsNeeded).sum();
		JPanel tempPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		tempPanel.setPreferredSize(new Dimension(actionPanelContent.getWidth(), card.getHeight() + 5));
		tempPanel.setOpaque(false);
		tempPanel.add(Utils.copyCard(card, players[aPID].isLocal, true));
		clearActionPanel();
		players[aPID].takeCoins(coinsNeeded);

		if (!players[aPID].isLocal) {
			if (sum > 0 || (!second && dblc > 0) || (second && dbld > 0) || card.special == 5) {
				clearAndAddInfoLabel(players[aPID].playerName + " "
						+ lt("g_bought_card" + (reserved ? "_res" : "") + (second ? "_sec" : "")));
			} else {
				clearAndAddInfoLabel(players[aPID].playerName + " "
						+ lt("g_take_card_free" + (reserved ? "_res" : "") + (second ? "_sec" : "")));
			}
			actionBuyCardFinish(tempPanel, reserved, d, second, coinsNeeded);
		} else {
			if (!second) {
				aB.setRC(r, c, d);
				aB.subAct = reserved ? "2" : "0";
			}
			if (possibleAnotherPayments(coinsNeeded)) {
				actionBuyCardPayChoice(coinsNeeded, coinsNeededOrigin, d, tempPanel, reserved, 0, second);
			} else
				actionBuyCardFinish(tempPanel, reserved, d, second, coinsNeeded);

		}
	}

	public boolean possibleAnotherPayments(int[] coinsNeeded) {
		int plGold = players[aPID].ownedCoins[5] + coinsNeeded[5];
		int sum = IntStream.of(coinsNeeded).sum();
		// sum>0;
		// sum>players[aPID].coinsSum();
		if (sum > 0) {
			if (sum == coinsNeeded[5]) {
				if (players[aPID].gratisCoins == 0) {
					return false;
				} else {
					if (players[aPID].gratisCoins > (sum - plGold)) {
						return (plGold != 0);
						// if (plGold == 0) {
						// return false;
						// } else
						// return true;
					} else
						return false;
				}
			} else {
				if (plGold > coinsNeeded[5]) {
					return true;
				} else {
					return (players[aPID].gratisCoins > coinsNeeded[5] - plGold);
					// if (players[aPID].gratisCoins > coinsNeeded[5] - plGold) {
					// return true;
					// } else
					// return false;
				}
			}
		} else
			return false;
	}

	public void actionBuyCardPayChoice(int[] coinsNeeded, int[] coinsNeededOrigin, int d, JPanel tempPanel,
			boolean reserved, int mode, boolean second) {

		Card actCard = reserved ? players[aPID].clCard : cardsDealt[d].getActCard(0);
		ArrButton[] buttons = new ArrButton[6];
		JPanel panel = new JPanel();
		LightPanel lp1 = new LightPanel(40, 40);
		LightPanel lp2 = new LightPanel(40, 40);
		panel.setLayout(new GridBagLayout());
		panel.setOpaque(false);
		Insets insets = new Insets(1, 1, 1, 1);
		if (players[aPID].gratisCoins > 0) {
			if (mode == 0 && players[aPID].ownedCoins[5] > 0) {
				mode = 1;
			} else if (mode == 0) {
				mode = 2;
			}
		} else
			mode = 3;
		int modeF = mode;
		JPanel botSide = new JPanel();
		botSide.setLayout(new GridBagLayout());
		botSide.setOpaque(false);
		JPanel cn = new JPanel();
		cn.setOpaque(false);

		if (modeF != 3) {
			// zamiana -zloty na karty
			while (coinsNeededOrigin[5] > (players[aPID].ownedCoins[5] + coinsNeededOrigin[5])) {
				coinsNeeded[5]--;
				coinsNeededOrigin[5]--;
				players[aPID].addCoin(5);
				if (second) {
					dbld++;
				} else
					dblc++;
				if ((second ? dbld : dblc) % 2 != 0) {
					if (coinsNeeded[5] > 0) {
						coinsNeeded[5]--;
						coinsNeededOrigin[5]--;
						players[aPID].addCoin(5);
						if (second) {
							dbld++;
						} else
							dblc++;
					}
				}
			}
			cn.add(Utils.coinDraw(5, players[aPID].ownedCoins[5], 1, true));
			JPanel gc = new JPanel();
			gc.setLayout(null);
			gc.setOpaque(false);
			int w = players[aPID].gratisCoins > 2 ? 120 : 64;
			int h = players[aPID].gratisCoins > 4 ? 72 : 38;
			gc.setPreferredSize(new Dimension(w, h));
			lp2.setPreferredSize(new Dimension(w, h));
			cn.setPreferredSize(new Dimension(40, 40));
			// XXX 1
			for (int i = 0; i < players[aPID].gratisCoins; i += 2) {
				DoubleCoin coin;
				if (i + 1 < (second ? dbld : dblc)) {
					coin = new DoubleCoin(2);
				} else if (i < (second ? dbld : dblc)) {
					coin = new DoubleCoin(1);
				} else {
					coin = new DoubleCoin(0);
				}
				if (i == 8) {
					coin.setBounds((w - 56) / 2, (h - 30) / 2, 56, 30);
				} else {
					coin.setBounds(4 + (i % 4 == 2 ? 56 : 0), 4 + (i >= 4 ? 34 : 0), 56, 30);
				}
				gc.add(coin, 0);
			}

			if (players[aPID].ownedCoins[5] + coinsNeeded[5] > 0) {
				if (modeF == 1) {
					lp1.selected = true;
					gc.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent me) {
							showPayment.setVisible(true);
							actionBuyCardPayChoice(coinsNeeded, coinsNeededOrigin, d, tempPanel, reserved, 2, second);
						}

						public void mouseEntered(MouseEvent me) {
							showPayment.setVisible(true);
							lp2.highlighted = true;
							lp2.repaint();
						}

						public void mouseExited(MouseEvent me) {
							lp2.highlighted = false;
							lp2.repaint();
						}
					});
				}
				if (modeF == 2) {
					lp2.selected = true;
					cn.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent me) {
							showPayment.setVisible(true);
							actionBuyCardPayChoice(coinsNeeded, coinsNeededOrigin, d, tempPanel, reserved, 1, second);
						}

						public void mouseEntered(MouseEvent me) {
							showPayment.setVisible(true);
							lp1.highlighted = true;
							lp1.repaint();
						}

						public void mouseExited(MouseEvent me) {
							lp1.highlighted = false;
							lp1.repaint();
						}

					});

				}
				botSide.add(lp1, gbc(0, 0, 1, 1, insets), 0);
				botSide.add(lp2, gbc(3, 0, 1, 1, insets), 0);

				if (coinsNeeded[5] > 0 && players[aPID].gratisCoins > (second ? dbld : dblc)) {
					buttons[5] = new ArrButton(2, true);
					buttons[5].addMouseListener(new MouseAdapter() {
						public void mouseReleased(MouseEvent me) {
							if (((ArrButton) me.getComponent()).releasedInside()) {
								players[aPID].addCoin(5);
								coinsNeeded[5]--;
								if (second) {
									dbld++;
								} else
									dblc++;
								actionBuyCardPayChoice(coinsNeeded, coinsNeededOrigin, d, tempPanel, reserved, modeF,
										second);
							}

						}
					});
					botSide.add(buttons[5], gbc(1, 0, 1, 1, insets));
				} else {
					botSide.add(new ArrButton(2, false), gbc(1, 0, 1, 1, insets));
				}

				// zloty za kartê
				if ((second ? dbld : dblc) > 0 && players[aPID].ownedCoins[5] > 0) {
					buttons[5] = new ArrButton(3, true);
					buttons[5].addMouseListener(new MouseAdapter() {
						public void mouseReleased(MouseEvent me) {
							if (((ArrButton) me.getComponent()).releasedInside()) {
								players[aPID].takeCoin(5);
								coinsNeeded[5]++;
								if (second) {
									dbld--;
								} else
									dblc--;
								actionBuyCardPayChoice(coinsNeeded, coinsNeededOrigin, d, tempPanel, reserved, modeF,
										second);
							}
						}
					});
					botSide.add(buttons[5], gbc(2, 0, 1, 1, insets));

				} else {
					botSide.add(new ArrButton(3, false), gbc(2, 0, 1, 1, insets));
				}
				botSide.add(cn, gbc(0, 0, 1, 1, insets), 0);
			} else {
				lp2.selected = true;
				botSide.add(lp2, gbc(3, 0, 1, 1, insets), 0);
			}
			botSide.add(gc, gbc(3, 0, 1, 1, insets), 0);
		} else {
			lp1.selected = true;
			cn.add(Utils.coinDraw(5, players[aPID].ownedCoins[5], 1, true));
			botSide.add(lp1, gbc(0, 0, 1, 1, insets), 0);
			botSide.add(cn, gbc(0, 0, 1, 1, insets), 0);
		}
		for (Component c : botSide.getComponents()) {
			if (c instanceof ArrButton) {
				c.addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent me) {
						showPayment.setVisible(true);
					}

					public void mousePressed(MouseEvent me) {
						showPayment.setVisible(true);
					}
				});
			}
		}
		panel.add(botSide, gbc(0, 3, 7, 1, insets));

		for (int i = 0; i < 5; i++) {
			int b = i;
			if (coinsNeededOrigin[i] > 0 || actCard.cost[i] - players[aPID].production[i] > 0) {
				if (modeF == 1 || modeF == 3) {
					if (coinsNeeded[i] != 0 && players[aPID].ownedCoins[5] != 0) {
						buttons[i] = new ArrButton(0, true);
						buttons[i].addMouseListener(new MouseAdapter() {
							public void mouseReleased(MouseEvent me) {
								if (((ArrButton) me.getComponent()).releasedInside()) {
									players[aPID].addCoin(b);
									coinsNeeded[b]--;
									players[aPID].takeCoin(5);
									coinsNeeded[5]++;
									actionBuyCardPayChoice(coinsNeeded, coinsNeededOrigin, d, tempPanel, reserved,
											modeF, second);
								}
							}
						});
						panel.add(buttons[i], gbc(i, 1, 1, 1, insets));
					} else {
						panel.add(new ArrButton(0, false), gbc(i, 1, 1, 1, insets));
					}

					if (coinsNeeded[i] < coinsNeededOrigin[i] && coinsNeeded[5] > 0) {
						buttons[i] = new ArrButton(1, true);
						buttons[i].addMouseListener(new MouseAdapter() {
							public void mouseReleased(MouseEvent me) {
								if (((ArrButton) me.getComponent()).releasedInside()) {
									players[aPID].takeCoin(b);
									coinsNeeded[b]++;
									players[aPID].addCoin(5);
									coinsNeeded[5]--;
									actionBuyCardPayChoice(coinsNeeded, coinsNeededOrigin, d, tempPanel, reserved,
											modeF, second);
								}
							}
						});
						panel.add(buttons[i], gbc(i, 2, 1, 1, insets));
					} else {
						panel.add(new ArrButton(1, false), gbc(i, 2, 1, 1, insets));
					}

				} else if (mode == 2) {
					if (coinsNeeded[i] != 0 && (second ? dbld : dblc) < players[aPID].gratisCoins) {
						buttons[i] = new ArrButton(0, true);
						buttons[i].addMouseListener(new MouseAdapter() {
							public void mouseReleased(MouseEvent me) {
								if (((ArrButton) me.getComponent()).releasedInside()) {
									players[aPID].addCoin(b);
									coinsNeeded[b]--;
									if (second) {
										dbld++;
									} else
										dblc++;
									actionBuyCardPayChoice(coinsNeeded, coinsNeededOrigin, d, tempPanel, reserved,
											modeF, second);
								}
							}
						});
						panel.add(buttons[i], gbc(i, 1, 1, 1, insets));
					} else {
						panel.add(new ArrButton(0, false), gbc(i, 1, 1, 1, insets));
					}
					if (coinsNeeded[i] < coinsNeededOrigin[i] && (second ? dbld : dblc) > 0) {
						buttons[i] = new ArrButton(1, true);
						buttons[i].addMouseListener(new MouseAdapter() {
							public void mouseReleased(MouseEvent me) {
								if (((ArrButton) me.getComponent()).releasedInside()) {
									players[aPID].takeCoin(b);
									coinsNeeded[b]++;
									if (second) {
										dbld--;
									} else
										dblc--;
									actionBuyCardPayChoice(coinsNeeded, coinsNeededOrigin, d, tempPanel, reserved,
											modeF, second);
								}
							}
						});
						panel.add(buttons[i], gbc(i, 2, 1, 1, insets));
					} else {
						panel.add(new ArrButton(1, false), gbc(i, 2, 1, 1, insets));
					}
				}

			} else {
				panel.add(new ArrButton(0, false), gbc(i, 1, 1, 1, insets));
				panel.add(new ArrButton(1, false), gbc(i, 2, 1, 1, insets));
			}
			panel.add(Utils.coinDraw(i, coinsNeeded[i], 1, false), gbc(i, 0, 1, 1, insets));

		}

		panel.add(Utils.coinDraw(5, coinsNeeded[5], 1, false), gbc(5, 0, 1, 1, insets));
		if (players[aPID].gratisCoins > 0 || (second ? dbld : dblc) > 0) {
			Image image = getIcon("images/colors/yellow.png").getImage();
			panel.add(Utils.doubleCoinDraw(second ? dbld : dblc, image), gbc(5, 1, 1, 2, insets));
		}
		clearAndAddInfoLabel(lt("use_yellow_coin"));
		for (Component c : panel.getComponents()) {
			if (c instanceof ArrButton) {
				c.addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent me) {
						showPayment.setVisible(true);
					}

					public void mousePressed(MouseEvent me) {
						showPayment.setVisible(true);
					}
				});
			}
		}
		actionPanelContent.add(panel);

		TButton nextStep = new TButton(lt("confirm"), "green", 14);
		nextStep.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					showPaymentDiscard();
					clearActionPanel();
					actionBuyCardFinish(tempPanel, reserved, d, second, coinsNeeded);
				}
			}
		});
		paymentInfo.removeAll();
		JPanel t1 = new JPanel(new FlowLayout());
		JPanel t2 = new JPanel(new FlowLayout());
		t1.setOpaque(false);
		t2.setOpaque(false);
		t1.add(new JLabel(lt("pay_preview")));
		Card tempCard = Utils.copyCard(actCard, true, true);
		t2.add(tempCard);
		// if (actCard.special==1 || actCard.special==2) {
		// t2.add(getCardIcon(actCard.color));
		// }
		JPanel tempStack = new SmallCoinStack(coinsNeeded, false);
		for (int i = 0; i < (second ? dbld : dblc); i += 2) {
			DoubleCoin dbcPanel = new DoubleCoin();
			// dbcPanel.setBackground(Color.RED);
			dbcPanel.setBounds(0, (30 * i) / 2, 56, 30);
			tempStack.add(dbcPanel);
		}
		t2.add(tempStack);
		paymentInfo.add(t1, BorderLayout.NORTH);
		paymentInfo.add(t2, BorderLayout.SOUTH);
		paymentInfo.repaint();
		paymentInfo.revalidate();
		showPayment.add(paymentInfo);
		setActionPanelListener();
		addCancelButton(coinsNeeded, second);
		actionPanelButtons.add(nextStep);
		for (Component c : actionPanelButtons.getComponents()) {
			if (c instanceof TButton) {
				c.addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent me) {
						showPayment.setVisible(true);
					}

					public void mousePressed(MouseEvent me) {
						showPayment.setVisible(true);
					}
				});
			}
		}
		actionPanel.repaint();
		actionPanel.revalidate();
	}

	public JPanel getCardIcon(int color) {
		JPanel info = new JPanel();

		info.setOpaque(false);
		info.add(new JLabel(getIcon("images/colors/bag.png")));
		info.add(new JLabel(getIcon("images/arrowToBag.png")));
		if (color == 0) {
			info.add(new JLabel(getIcon("images/colors/green.png")));
		} else if (color == 1) {
			info.add(new JLabel(getIcon("images/colors/white.png")));
		} else if (color == 2) {
			info.add(new JLabel(getIcon("images/colors/blue.png")));
		} else if (color == 3) {
			info.add(new JLabel(getIcon("images/colors/black.png")));
		} else if (color == 4) {
			info.add(new JLabel(getIcon("images/colors/red.png")));
		}
		info.setPreferredSize(new Dimension(35, 110));
		return info;
	}

	public void setResAri(int n, boolean second, boolean giveback) {
		if (n != 9) {
			Aristocrat tempAri = aristocratsDealt._arilist.get(n);
			tempAri.isReserved = true;
			tempAri.resID = aPID;
			if (!players[aPID].isLocal) {
				clearAndAddInfoLabel(lt("g_do_res"));
				tempAri.hideAri = true;
				aristocratsDealt.repaint();
			} else {
				if (second) {
					aB.specAct2 = "SPED" + n;
				} else
					aB.specAct = "SPEC" + n;
				aristocratsDealt.removeAllActions();
				clearAndAddInfoLabel(lt("g_you_did_res"));
			}
			Aristocrat rewAri = new Aristocrat(tempAri.imgPath, tempAri.reward, tempAri.req);
			JPanel temp = new JPanel();
			temp.setOpaque(false);
			temp.add(new JLabel(getIcon("images/cArrow.png")));
			temp.add(rewAri);
			actionPanelContent.add(temp);
			actionPanelContent.repaint();
			actionPanelContent.revalidate();
		} else {
			if (!players[aPID].isLocal) {
				clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_no_ari_to_res"));
			} else {
				if (second) {
					aB.specAct2 = "SPED9";
				} else
					aB.specAct = "SPEC9";
				clearAndAddInfoLabel(lt("no_ari_to_res"));
			}
			actionPanelContent.add(new NoAriRes());
		}
		Timer timer = new Timer(isRecovery ? 0 : 3000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				addHistory(false);
				if (second) {
					endTurnP1second(giveback);
				} else
					endTurnP1(true);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

	public void fcaSelected(CardsDealt cd, Card card, int r, int c, int[] coinsNeeded, int actionID, int d,
			boolean second) {
		clearActionPanel();
		// if (card.special == 1 || card.special == 2) {
		// actionPanelContent.add(getCardIcon(card.color));
		// }
		addInfoLabel("Bierzesz:");
		actionPanelContent.add(Utils.copyCard(card, true, true));
		// if (card.special == 1 || card.special == 2) {
		// actionPanelContent.add(getCardIcon(card.color));
		// }
		cd.takeCard(r, c, r);
		// addCancelButton(coinsNeeded);
		TButton backBtn = new TButton(lt("back"), "yellow", 14);
		TButton nextBtn = new TButton(lt("next"), "green", 14);
		TButton cBtn = new TButton(lt("back"), "yellow", 14);
		cBtn.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					cardsDealt[1].backCard(1);
					clearAndAddInfoLabel(lt("select_for_free_2"));
				}
				addCancelButton(coinsNeeded, second);
				setFreeCardActions(1, coinsNeeded, actionID, d, second);
			}
		});
		TButton backBtnB = new TButton(lt("back"), "yellow", 14);
		backBtnB.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					clearActionPanel();
					removeCardsActions();
					Card card = cardsDealt[1].getActCard(1);
					addInfoLabel("Bierzesz:");
					actionPanelContent.add(Utils.copyCard(card, true, true));
					// if (card.special == 1 || card.special == 2) {
					// actionPanelContent.add(getCardIcon(card.color));
					// }
					actionPanelButtons.add(cBtn);
					actionPanelButtons.add(nextBtn);
				}
			}
		});
		if (cardsDealt[1].cancelPossible[1]) {
			nextBtn.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						clearAndAddInfoLabel(lt("select_for_free_1"));

						actionPanelButtons.add(backBtnB);

						setFreeCardActions(2, coinsNeeded, actionID, d, second);

					}
				}
			});
		}
		backBtn.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					cd.backCard(r);
					if (r == 2) {
						clearAndAddInfoLabel(lt("select_for_free_1"));
						if (cardsDealt[1].cancelPossible[1]) {
							actionPanelButtons.add(backBtnB);
						} else {
							addCancelButton(coinsNeeded, second);
						}
					} else {
						clearAndAddInfoLabel(lt("select_for_free_2"));
						addCancelButton(coinsNeeded, second);
					}
					setFreeCardActions(r, coinsNeeded, actionID, d, second);
				}
			}
		});
		actionPanelButtons.add(backBtn);
		if (card.special == 2) {
			actionPanelButtons.add(nextBtn);
		} else {
			addConfirmButton(coinsNeeded, actionID, d, second);
		}
		removeCardsActions();
	}

	public void setFreeCardActions(int r, int[] coinsNeeded, int actionID, int d, boolean second) {
		int count = 0;
		for (CardsDealt cd : cardsDealt) {
			for (int i = 0; i < cd.cardsOnBoard[r].length; i++) {
				int t = cd.cardsOnBoard[r][i];
				if (t != 9999) {
					Card card = cd.getCard(t);
					if (card.show && (card.strNum == 0 || card.strNum > 0 && card.strColor == aPID)) {
						count++;
						card.setActive();
						card.setBorder(2);
						int j = i;
						card.addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent me) {
								if (card.special == 1 || card.special == 2) {
									//TODO specialSelectColor
									removeCardsActions();
									ColorSelectPanel colorPanel = new ColorSelectPanel(game.board, card);
									colorPanel.setup(cd, card, r, j, coinsNeeded, actionID, d, second);
									colorPanel.showThis();
									// int n = specialSelectColor(card);
									// if (n != 9) {
									// card.color = n;
									// fcaSelected(cd, card, r, j, coinsNeeded, actionID, d, second);
									// } else {
									// setFreeCardActions(r, coinsNeeded, actionID, d, second);
									// }
								} else {
									fcaSelected(cd, card, r, j, coinsNeeded, actionID, d, second);
								}
							}
						});
					}
				}
			}
		}
		if (count == 0) {
			removeCardsActions();
			if (r == 2) {
				addInfoLabel(lt("no_card_1"));
				actionPanelContent.add(new NoFreeCard(1));
			} else {
				addInfoLabel(lt("no_card_2"));
				actionPanelContent.add(new NoFreeCard(2));
			}
			addConfirmButton(coinsNeeded, actionID, d, second);
		}
	}

	public void actionBuyCardFinish(JPanel tempPanel, boolean reserved, int d, boolean second, int... coinsNeeded) {
		// special 1 = poziom 1 - po³¹czenie kart
		// special 2 = poziom 2 - po³¹czenie kart + wziêcie karty 1 poziomu
		// special 3 = wziêcie karty 2 poziomu
		// special 4 = double coin
		// special 5 = odrzuæ dwie karty
		// special 6 = rezerwacja ari
		Card actCard;
		while (players[aPID].ownedCoins[5] < 0) {
			players[aPID].addCoin(5);
			coinsNeeded[5]--;
			if (second) {
				dbld++;
			} else
				dblc++;
		}
		actCard = reserved ? players[aPID].clCard : cardsDealt[d].getActCard(0);

		if (actCard.special == 1 || actCard.special == 2) {
			if (players[aPID].isLocal) {
				actionBuyCardFinishContinue(actCard, tempPanel, reserved, d, second, coinsNeeded);
			} else {
				if (second) {
					actCard.color = specialModifierB;
				} else
					actCard.color = specialModifier;
				tempPanel.add(getCardIcon(actCard.color));
				actionBuyCardFinishContinue(actCard, tempPanel, reserved, d, second, coinsNeeded);
			}
		} else if (actCard.special == 5 && players[aPID].isLocal) {
			CardSelectPanel cardsPanel = new CardSelectPanel(actCard, this, tempPanel, reserved, d, second,
					coinsNeeded);
			cardsPanel.showThis();
		} else {
			actionBuyCardFinishContinue(actCard, tempPanel, reserved, d, second, coinsNeeded);
		}
	}

	public void actionBuyCardFinishContinue(Card actCard, JPanel tempPanel, boolean reserved, int d, boolean second,
			int... coinsNeeded) {

		if ((coinsNeeded != null && IntStream.of(coinsNeeded).sum() > 0) || (second ? dbld : dblc) != 0) {
			JPanel tempStack = new SmallCoinStack(coinsNeeded, false);
			for (int i = 0; i < (second ? dbld : dblc); i += 2) {
				DoubleCoin dbcPanel = new DoubleCoin();
				// dbcPanel.setBackground(Color.RED);
				dbcPanel.setBounds(0, (30 * i) / 2, 56, 30);
				tempStack.add(dbcPanel);
			}
			tempPanel.add(tempStack);
		}

		actionPanelContent.add(tempPanel);
		actionPanelContent.repaint();
		actionPanelContent.revalidate();

		if (players[aPID].isLocal) {
			addInfoLabel("Kupujesz:");
			if (actCard.special == 2 || actCard.special == 3) {
				// addCancelButton(coinsNeeded);
				TButton nextBtn = new TButton(lt("next"), "green", 14);

				nextBtn.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent me) {
						if (((TButton) me.getComponent()).releasedInside()) {
							int n;
							if (actCard.special == 2) {
								n = 2;
								clearAndAddInfoLabel(lt("select_for_free_1"));
							} else {
								n = 1;
								clearAndAddInfoLabel(lt("select_for_free_2"));
							}
							// addCancelButton(coinsNeeded);
							addCancelButton(coinsNeeded, second);
							if (reserved) {
								setFreeCardActions(n, coinsNeeded, 6, d, second);
							} else
								setFreeCardActions(n, coinsNeeded, 0, d, second);

						}
					}
				});
				addCancelButton(coinsNeeded, second);
				actionPanelButtons.add(nextBtn);
				actionPanel.repaint();
				actionPanel.revalidate();
			} else if (actCard.special == 5) {
				addCancelButton(coinsNeeded, second);
				addConfirmButton(coinsNeeded, reserved ? 6 : 0, d, second);
				for (Component c : actionPanelButtons.getComponents()) {
					if (c instanceof TButton) {
						c.addMouseListener(new MouseAdapter() {
							public void mouseEntered(MouseEvent me) {
								showPayment.setVisible(true);
							}

							public void mousePressed(MouseEvent me) {
								showPayment.setVisible(true);
							}
						});
					}
				}
			} else {
				addCancelButton(coinsNeeded, second);
				addConfirmButton(coinsNeeded, reserved ? 6 : 0, d, second);
			}
		} else {
			remoteAction(coinsNeeded, reserved ? 5 : 0, d, second && players[aPID].coinsSum() > 10,
					actCard.special == 2 || actCard.special == 3, actCard.special == 5, second);
		}
	}

	public void actionReserveCard(int r, int c, int d) {
		Card card = getCard(cardsDealt[d].cardsOnBoard[r][c], d);
		cardsDealt[d].takeCard(r, c, 0);
		JPanel tempPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		tempPanel.setPreferredSize(new Dimension(actionPanelContent.getWidth(), card.getHeight() + 5));
		tempPanel.setOpaque(false);
		tempPanel.add(card);
		clearActionPanel();
		if (players[aPID].isLocal) {
			addInfoLabel("Rezerwujesz:");
			addCancelButton(null, false);
			aB.setRC(r, c, d);
			aB.subAct = "1";
			if (coinsDealt.coinsStack[5].num > 0) {
				coinsDealt.coinsStack[5].coinUp(1);
				JPanel scsp = new JPanel(new BorderLayout(0, -20));
				scsp.setOpaque(false);
				JLabel plus = new JLabel("+", JLabel.CENTER);
				plus.setFont(new Font("Arial", Font.BOLD, 40));
				plus.setForeground(new Color(0, 0, 0, 175));
				scsp.add(plus, BorderLayout.CENTER);
				scsp.add(new SmallCoinStack(new int[] { 0, 0, 0, 0, 0, 1 }, true), BorderLayout.SOUTH);
				tempPanel.add(scsp);
				addConfirmButton(null, 1, d, false);
				aB.subAct += "1";
			} else {
				addConfirmButton(null, 2, d, false);
				aB.subAct += "2";
			}

		} else {
			clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_reserve_card"));
			if (coinsDealt.coinsStack[5].num > 0) {
				coinsDealt.coinsStack[5].coinUp(1);
				JPanel scsp = new JPanel(new BorderLayout(0, -20));
				scsp.setOpaque(false);
				JLabel plus = new JLabel("+", JLabel.CENTER);
				plus.setFont(new Font("Arial", Font.BOLD, 40));
				plus.setForeground(new Color(0, 0, 0, 175));
				scsp.add(plus, BorderLayout.CENTER);
				scsp.add(new SmallCoinStack(new int[] { 0, 0, 0, 0, 0, 1 }, true), BorderLayout.SOUTH);
				tempPanel.add(scsp);
			}
		}
		actionPanelContent.add(tempPanel);
		actionPanelContent.repaint();
		actionPanelContent.revalidate();

	}

	public void handleCoinsStackActions() {
		clearAndAddInfoLabel("Bierzesz ¿etony:");
		coinsDealt.removeAllActions();
		setAllCoinsStacksActions(false);
		int[] coinsToTake = new int[6];
		String putAction = "";
		for (int i = 0; i < 6; i++) {
			coinsToTake[i] = coinsDealt.coinsStack[i].numUp;
			putAction += coinsToTake[i];
		}
		aB.subAct = putAction;

		JPanel playerCoins = new JPanel();
		playerCoins.setLayout(new GridBagLayout());
		playerCoins.setOpaque(false);
		Insets insets = new Insets(1, 1, 1, 1);
		for (int i = 0; i < 5; i++) {
			playerCoins.add(Utils.cardAndCoinDraw(i, players[aPID].production[i],
					players[aPID].ownedCoins[i] + coinsToTake[i], 1), gbc(i, 0, 1, 1, insets));

		}
		playerCoins.add(Utils.cardAndCoinDraw(5, 0, players[aPID].ownedCoins[5] + coinsToTake[5], 1),
				gbc(5, 0, 1, 1, insets));
		actionPanelContent.add(playerCoins);
		int sum = players[aPID].coinsSum() + IntStream.of(coinsToTake).sum();
		if (sum > 10) {
			String text;
			if (sum - 10 == 1) {
				text = lt("g_too_much_one_coin");
			} else {
				text = lt("g_too_much") + " " + (sum - 10) + " " + lt("coins");
			}
			String labelText = String.format("<html><div style=\"width:%dpx;\"><center>%s</center></div></html>", 160,
					text);
			JLabel label = new JLabel(labelText);
			label.setFont(new Font("Verdana", Font.BOLD, 13));
			actionPanelContent.add(label);
			// clearAndAddInfoLabel(lt("g_too_much") + " " + (sum - 10) + " " +
			// lt("coins"));
		}
		addCancelButton(null, false);
		addConfirmButton(coinsToTake, 3, 0, false);
	}

	public void setAllCoinsStacksActions(boolean start) {
		for (int i = 0; i < 5; i++) {
			CoinsStack coinsStack = coinsDealt.coinsStack[i];
			boolean allow = true;
			if (!start) {
				for (CoinsStack p : coinsDealt.coinsStack)
					if (p.numUp == 2)
						allow = false;
				if (coinsDealt.countUp() == 3
						|| (coinsStack.numUp == 1 && (coinsDealt.countUp() != 1 || coinsStack.num < 4)))
					allow = false;
			}
			if (allow && coinsStack.num != 0) {
				coinsStack.setActive(true);
				coinsStack.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent me) {
						if (SwingUtilities.isLeftMouseButton(me)) {
							boolean allow = true;
							for (CoinsStack p : coinsDealt.coinsStack)
								if (p.anim)
									allow = false;
							if (allow) {
								setActionType(1);

								if (coinsStack.numUp == 1) {
									coinsStack.coinUp(2);
								} else
									coinsStack.coinUp(1);
								handleCoinsStackActions();
							}
						}
					}
				});
			}
			if (coinsStack.numUp > 0) {
				coinsStack.setActive(true);
				coinsStack.addMouseListener(new MouseAdapter() {

					public void mousePressed(MouseEvent me) {
						if (SwingUtilities.isRightMouseButton(me)) {
							coinsStack.coinUp(coinsStack.numUp - 1);
							handleCoinsStackActions();
							if (coinsDealt.countUp() == 0)
								cancelAction(false);

						}
					}

				});
			}

		}
	}

	public void remoteAction(int[] coinsAct, int actionID, int d, boolean giveback, boolean freeCard, boolean backCards,
			boolean second, int... coinsBack) {
		// 0 zakup karty
		// 1 - rezerwacja + z³oty coin
		// 2 - rezerwacja bez coina
		// 3 - wziêcie coinów
		// 4 - pass
		// 5 - zakup z rezerwacji
		Card actCard = actionID == 5 ? players[aPID].clCard : cardsDealt[d].getActCard(0);
		if (actionID == 3) {
			coinsDealt.coinsUp(coinsAct);
			clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_take_coins"));
			actionPanelContent.add(new SmallCoinStack(coinsAct, true));

		}
		if (actionID == 4) {
			clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_pass"));
		}
		Timer timer = new Timer(isRecovery ? 0 : 3000, new ActionListener() {
			int tick = 0;
			Card freeCA, freeCB;
			boolean freeCApos = false, freeCBpos = false;

			public void actionPerformed(ActionEvent evt) {
				if (tick == 0) {
					switch (actionID) {
					case 0:
					case 5:
						players[aPID].addCard(actCard);
						coinsDealt.addCoins(coinsAct);
						int usedDbl = (int) (Math.ceil((double) (second ? dbld : dblc) / 2));
						if (usedDbl > 0) {
							players[aPID].removeDBLCards(usedDbl);
						}
						dblc = 0;
						break;
					case 1:
						coinsDealt.coinsStack[5].takeCoin(1);
						players[aPID].addCoin(5);
						players[aPID].addResCard(actCard);
						break;
					case 2:
						players[aPID].addResCard(actCard);
						break;
					case 3:
						coinsDealt.removeAllActions();
						coinsDealt.takeCoins(coinsAct);
						players[aPID].addCoins(coinsAct);
						break;
					}
					if (!second)
						playersPanel.clearContent(aPID);
					addHistory(false);
					if ((!giveback || (giveback && strBuy)) && !freeCard && !backCards) {
						if (giveback && !second) {
							strCoinsBack = coinsBack;
						}
						if (actCard.special == 6 && (actionID == 0 || actionID == 5)) {
							setResAri(second ? specialModifierB : specialModifier, second, giveback);
						} else {
							if (second) {
								endTurnP1second(giveback);
							} else
								endTurnP1(actionID == 0 || actionID == 5);
						}
						Timer t = (Timer) evt.getSource();
						t.stop();
					} else {
						if (giveback && !second) {
							clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_takes_too_much"));
							actionPanelContent.add(new SmallCoinStack(coinsBack, true));
						}
						if (freeCard) {
							JPanel tempPanel = new JPanel();
							tempPanel.setPreferredSize(new Dimension(actionPanelContent.getWidth(), 158));
							tempPanel.setOpaque(false);
							clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_take_card_free"));
							if (actCard.special == 3) {
								if (second) {
									if (frc[0] != 9) {
										freeCApos = true;
										freeCA = cardsDealt[frc[0]]
												.getCard(cardsDealt[frc[0]].cardsOnBoard[frc[1]][frc[2]]);
										freeCA.color = frc[3];
										cardsDealt[frc[0]].takeCard(frc[1], frc[2], 1);
										tempPanel.add(freeCA);
										if (freeCA.special == 1 || freeCA.special == 2) {
											tempPanel.add(getCardIcon(frc[3]));
										}
									} else {
										clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_no_free_card_2"));
										tempPanel.add(new NoFreeCard(2));
									}

								} else {
									if (fra[0] != 9) {
										freeCApos = true;
										freeCA = cardsDealt[fra[0]]
												.getCard(cardsDealt[fra[0]].cardsOnBoard[fra[1]][fra[2]]);
										freeCA.color = fra[3];
										cardsDealt[fra[0]].takeCard(fra[1], fra[2], 1);
										tempPanel.add(freeCA);
										if (freeCA.special == 1 || freeCA.special == 2) {
											tempPanel.add(getCardIcon(fra[3]));
										}
									} else {
										clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_no_free_card_2"));
										tempPanel.add(new NoFreeCard(2));
									}
								}
							} else if (actCard.special == 2) {
								if (second) {
									if (frd[0] != 9) {
										freeCBpos = true;
										freeCB = cardsDealt[frd[0]]
												.getCard(cardsDealt[frd[0]].cardsOnBoard[frd[1]][frd[2]]);
										freeCB.color = frd[3];
										cardsDealt[frd[0]].takeCard(frd[1], frd[2], 2);
										tempPanel.add(freeCB);
										if (freeCB.special == 1) {
											tempPanel.add(getCardIcon(frd[3]));
										}
									} else {
										clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_no_free_card_1"));
										tempPanel.add(new NoFreeCard(1));
									}
								} else {
									if (frb[0] != 9) {
										freeCBpos = true;
										freeCB = cardsDealt[frb[0]]
												.getCard(cardsDealt[frb[0]].cardsOnBoard[frb[1]][frb[2]]);
										freeCB.color = frb[3];
										cardsDealt[frb[0]].takeCard(frb[1], frb[2], 2);
										tempPanel.add(freeCB);
										if (freeCB.special == 1) {
											tempPanel.add(getCardIcon(frb[3]));
										}
									} else {
										clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_no_free_card_1"));
										tempPanel.add(new NoFreeCard(1));
									}
								}

							}
							actionPanelContent.add(tempPanel);
						}
						if (backCards) {
							clearAndAddInfoLabel(lt("g_pay_cards"));//
							JPanel tempPanel = new JPanel();
							tempPanel.setPreferredSize(new Dimension(actionPanelContent.getWidth(), 158));
							tempPanel.setOpaque(false);
							for (int i = 0; i < 2; i++) {
								for (Card t : players[aPID]._cardList) {
									if (t.id == (second ? frc[i] : fra[i])) {
										tempPanel.add(t);
									}
								}
							}
							actionPanelContent.add(tempPanel);

						}
						tick = 1;
					}
				} else if (tick == 1) {
					// historyADD
					addHistory(false);
					if (giveback && !second) {
						coinsDealt.addCoins(coinsBack);
						players[aPID].takeCoins(coinsBack);
						endTurnP1(false);
						Timer t = (Timer) evt.getSource();
						t.stop();
					} else if (freeCard) {
						if (actCard.special == 3) {
							if (freeCApos)
								players[aPID].addCard(freeCA);
							if (freeCApos && freeCA.special == 2) {
								JPanel tempPanel = new JPanel();
								tempPanel.setPreferredSize(new Dimension(actionPanelContent.getWidth(), 158));
								tempPanel.setOpaque(false);
								clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_take_card_free"));
								if (second) {
									if (frd[0] != 9) {
										freeCBpos = true;
										freeCB = cardsDealt[frd[0]]
												.getCard(cardsDealt[frd[0]].cardsOnBoard[frd[1]][frd[2]]);
										freeCB.color = frd[3];
										cardsDealt[frd[0]].takeCard(frd[1], frd[2], 2);
										tempPanel.add(freeCB);
										if (freeCB.special == 1) {
											tempPanel.add(getCardIcon(frd[3]));
										}
									} else {
										clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_no_free_card_1"));
										tempPanel.add(new NoFreeCard(1));
									}
								} else {
									if (frb[0] != 9) {
										freeCBpos = true;
										freeCB = cardsDealt[frb[0]]
												.getCard(cardsDealt[frb[0]].cardsOnBoard[frb[1]][frb[2]]);
										freeCB.color = frb[3];
										cardsDealt[frb[0]].takeCard(frb[1], frb[2], 2);
										tempPanel.add(freeCB);
										if (freeCB.special == 1) {
											tempPanel.add(getCardIcon(frb[3]));
										}
									} else {
										clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_no_free_card_1"));
										tempPanel.add(new NoFreeCard(1));
									}
								}
								actionPanelContent.add(tempPanel);
								tick = 2;
							} else {
								if (freeCApos && freeCA.special == 6) {
									setResAri(second ? specialModifierB : specialModifier, second, giveback);
								} else {
									if (second) {
										endTurnP1second(giveback);
									} else
										endTurnP1(true);
								}
								Timer t = (Timer) evt.getSource();
								t.stop();
							}
						} else {
							if (freeCBpos)
								players[aPID].addCard(freeCB);
							if (second) {
								endTurnP1second(giveback);
							} else
								endTurnP1(true);
							Timer t = (Timer) evt.getSource();
							t.stop();
						}
					} else if (backCards) {

						if (second) {
							players[aPID].takeCard(frc[0]);
							players[aPID].takeCard(frc[1]);
							endTurnP1second(giveback);
						} else {
							players[aPID].takeCard(fra[0]);
							players[aPID].takeCard(fra[1]);
							endTurnP1(true);
						}
						Timer t = (Timer) evt.getSource();
						t.stop();
					}
				} else if (tick == 2) {
					addHistory(false);
					if (freeCBpos)
						players[aPID].addCard(freeCB);
					if (second) {
						endTurnP1second(giveback);
					} else
						endTurnP1(true);
					Timer t = (Timer) evt.getSource();
					t.stop();
				}
			}
		});
		timer.setRepeats(true);
		timer.start();
	}

	public void strRemoteTakeCoins() {
		clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_takes_too_much"));
		actionPanelContent.add(new SmallCoinStack(strCoinsBack, true));
		Timer timer = new Timer(isRecovery ? 0 : 3000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				coinsDealt.addCoins(strCoinsBack);
				players[aPID].takeCoins(strCoinsBack);
				addHistory(false);
				strongholdsActionP2(true);
			}
		});
		// timer.setInitialDelay(isRecovery?0:2000);
		timer.setRepeats(false);
		timer.start();
	}

	public void strongholdsRemoteAction(boolean second, boolean giveback) {
		Card cardA;
		Card cardB;
		if (second ? strTypeB == 0 : strTypeA == 0) {
			cardA = cardsDealt[second ? strBD : strAD].getCard(
					cardsDealt[second ? strBD : strAD].cardsOnBoard[second ? strBR : strAR][second ? strBC : strAC]);
			clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_str_set"));
			Card tCard = Utils.copyCard(cardA, false, true);
			tCard.addStronghold(aPID);
			STRPanel strPanel = new STRPanel(aPID, tCard, 0);
			actionPanelContent.add(strPanel);
			if (!isRecovery) {
				Timer timer = new Timer(200, new ActionListener() {
					int tick = 1;

					public void actionPerformed(ActionEvent evt) {
						if (tick <= 10) {
							if (tick % 2 == 1)
								tCard.removeStronghold();
							if (tick % 2 == 0)
								tCard.addStronghold(aPID);
							tCard.repaint();
						}
						if (tick == 12) {
							strPanel.timerRun();
							Timer t = (Timer) evt.getSource();
							t.stop();
						}
						tick++;
					}
				});
				timer.setRepeats(true);
				timer.start();
			} else {
				strPanel.timerRun();
			}
			addStronghold(cardA, second, second ? giveback : false);
		}
		if (second ? strTypeB == 2 : strTypeA == 2) {
			cardA = cardsDealt[second ? strBD : strAD].getCard(
					cardsDealt[second ? strBD : strAD].cardsOnBoard[second ? strBR : strAR][second ? strBC : strAC]);
			clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_str_remove"));
			int c = cardA.strColor;
			Card tCard = Utils.copyCard(cardA, false, true);
			tCard.removeStronghold();
			STRPanel strPanel = new STRPanel(c, tCard, 2);
			actionPanelContent.add(strPanel);
			if (!isRecovery) {
				Timer timer = new Timer(200, new ActionListener() {
					int tick = 1;

					public void actionPerformed(ActionEvent evt) {
						if (tick <= 10) {
							if (tick % 2 == 1)
								tCard.addStronghold(c);
							if (tick % 2 == 0)
								tCard.removeStronghold();
							tCard.repaint();
						}
						if (tick == 12) {
							strPanel.timerRun();
							Timer t = (Timer) evt.getSource();
							t.stop();
						}
						tick++;
					}
				});
				timer.setRepeats(true);
				timer.start();
			} else {
				strPanel.timerRun();
			}
			removeStronghold(cardA, second);
		}
		if (!second && strTypeA == 1) {
			cardA = cardsDealt[strAD].getCard(cardsDealt[strAD].cardsOnBoard[strAR][strAC]);
			cardB = cardsDealt[strAD2].getCard(cardsDealt[strAD2].cardsOnBoard[strAR2][strAC2]);
			clearAndAddInfoLabel(players[aPID].playerName + " " + lt("g_str_move"));
			int c = cardA.strColor;
			Card tCardA = Utils.copyCard(cardA, false, true);
			Card tCardB = Utils.copyCard(cardB, false, true);
			STRPanel strPanel = new STRPanel(aPID, tCardA, tCardB);
			actionPanelContent.add(strPanel);

			tCardA.removeStronghold();
			tCardA.repaint();
			tCardB.addStronghold(aPID);
			tCardB.repaint();
			if (!isRecovery) {
				Timer timer = new Timer(200, new ActionListener() {
					int tick = 1;

					public void actionPerformed(ActionEvent evt) {
						if (tick <= 10) {
							if (tick % 2 == 1) {
								tCardA.addStronghold(c);
								tCardB.removeStronghold();
							} else {
								tCardA.removeStronghold();
								tCardB.addStronghold(aPID);
							}
							tCardA.repaint();
							tCardB.repaint();
						}
						if (tick == 12) {
							strPanel.timerRun();
							Timer t = (Timer) evt.getSource();
							t.stop();
						}
						tick++;
					}
				});
				timer.setRepeats(true);
				timer.start();
			} else {
				strPanel.timerRun();
			}
			remoteMoveStronghold(cardA, cardB);
		}
	}

	public void strongholdsAction(boolean didBuy, boolean second, boolean giveback) {
		if (didBuy) {
			if (players[aPID].isLocal) {
				clearActionPanel();
				setStrActions(false, second, null);
			} else {
				strongholdsRemoteAction(second, giveback);
			}
		} else {
			if (players[aPID].isLocal) {
				aB.str = "STR0000000";
			}
			strongholdsActionP2(second);
		}
	}

	public void remoteMoveStronghold(Card carda, Card cardb) {
		int c = carda.strColor;
		carda.removeStronghold();
		cardb.addStronghold(aPID);
		if (!isRecovery) {
			exposeCards(carda, cardb);
			Timer timer = new Timer(200, new ActionListener() {
				int tick = 1;

				public void actionPerformed(ActionEvent evt) {
					if (tick <= 10) {
						if (tick % 2 == 1) {
							carda.addStronghold(c);
							cardb.removeStronghold();
						} else {
							carda.removeStronghold();
							cardb.addStronghold(aPID);
						}
						carda.repaint();
						cardb.repaint();
					}
					if (tick == 10)
						removeCardsActions();
					if (tick == 12) {

						Timer t = (Timer) evt.getSource();
						t.stop();
						addHistory(true);
						strongholdsActionP2(false);
					}
					tick++;
				}
			});
			timer.setRepeats(true);
			timer.start();
		} else {
			addHistory(true);
			strongholdsActionP2(false);
		}
	}

	public void addStronghold(Card card, boolean second, boolean giveback) {

		if (!players[aPID].isLocal) {
			players[aPID].str--;
			card.addStronghold(aPID);
			if (!isRecovery) {
				exposeCards(card);
				Timer timer = new Timer(200, new ActionListener() {
					int tick = 1;

					public void actionPerformed(ActionEvent evt) {
						if (tick <= 10) {
							if (tick % 2 == 1)
								card.removeStronghold();
							if (tick % 2 == 0)
								card.addStronghold(aPID);
							card.repaint();
						}
						if (tick == 10)
							removeCardsActions();
						if (tick == 12) {
							Timer t = (Timer) evt.getSource();
							t.stop();
							addHistory(true);
							if (giveback) {
								strRemoteTakeCoins();
							} else
								strongholdsActionP2(second);
						}
						tick++;
					}
				});
				timer.setRepeats(true);
				timer.start();
			} else {
				addHistory(true);
				if (giveback) {
					strRemoteTakeCoins();
				} else
					strongholdsActionP2(second);
			}
		} else {
			strongholdsActionP2(second);
		}
	}

	public void removeStronghold(Card card, boolean second) {
		if (!players[aPID].isLocal) {
			int c = card.strColor;
			players[c].str++;
			card.removeStronghold();
			if (!isRecovery) {
				exposeCards(card);
				Timer timer = new Timer(200, new ActionListener() {
					int tick = 1;

					public void actionPerformed(ActionEvent evt) {
						if (tick <= 10) {
							if (tick % 2 == 1)
								card.addStronghold(c);
							if (tick % 2 == 0)
								card.removeStronghold();
							card.repaint();

						}
						if (tick == 10)
							removeCardsActions();
						if (tick == 12) {
							Timer t = (Timer) evt.getSource();
							t.stop();
							addHistory(true);
							strongholdsActionP2(second);
						}
						tick++;
					}
				});
				timer.setRepeats(true);
				timer.start();
			} else {
				addHistory(true);
				strongholdsActionP2(second);
			}
		} else {
			strongholdsActionP2(second);
		}
	}

	public void strongholdsActionP2(boolean second) {
		removeCardsActions();
		if (confirmCardsMove()) {
			clearAndAddInfoLabel("Wyk³adam karty...");
			Timer timer = new Timer(isRecovery ? 0 : 1500, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					strongholdsActionP2(second);
				}
			});
			timer.setRepeats(false);
			timer.start();
			return;
		}
		if (checkStrBuy()) {
			if (players[aPID].isLocal) {
				// removeCardsActions();
				if (players[aPID].coinsSum() <= 10 || (second && strCoinsNeeded[5] == 1)) {
					clearAndAddInfoLabel(lt("three_strongholds"));
					cardsDealt[strCardD].getCard(cardsDealt[strCardD].cardsOnBoard[strCardR][strCardC]).setBorder(3);
					TButton btnNoBuy = new TButton(
							players[aPID].coinsSum() <= 10 ? lt("no_end_turn") : lt("no_back_coins"), "yellow", 14);
					btnNoBuy.addMouseListener(new MouseAdapter() {
						public void mouseReleased(MouseEvent me) {
							if (((TButton) me.getComponent()).releasedInside()) {
								aB.str += "00000";
								if (players[aPID].coinsSum() <= 10) {
									endTurnP2();
								} else {
									checkCoinsLimit(strCoinsNeeded, strAD, false, true, strCoinsNeeded[5] == 1);
								}
							}
						}
					});

					TButton btnBuy = new TButton(lt("yes_go_pay"), "green", 14);
					btnBuy.addMouseListener(new MouseAdapter() {
						public void mouseReleased(MouseEvent me) {
							if (((TButton) me.getComponent()).releasedInside()) {
								actionBuyCard(strCardR, strCardC, strCardD, false, true);
							}
						}
					});
					actionPanelContent.add(btnNoBuy);
					actionPanelContent.add(btnBuy);
					actionPanel.repaint();
					actionPanel.revalidate();
				} else {
					checkCoinsLimit(strCoinsNeeded, strAD, false, false, false);
				}
			} else {

				if (strBuy) {
					actionBuyCard(strCardR, strCardC, strCardD, false, true, strCoinsNeeded);
				} else
					endTurnP2();
			}
		} else {
			if (players[aPID].isLocal && !second) {
				aB.str += "00000";
			}
			if (players[aPID].coinsSum() > 10) {
				if (players[aPID].isLocal) {
					// removeCardsActions();
					checkCoinsLimit(strCoinsNeeded, strAD, second, true, false);
				}
			} else {
				endTurnP2();
			}
		}
	}

	public void setStrActions(boolean moveTo, boolean second, Card exCard) {
		// str[0, 1, 2 ],D,R,C,D2,R2,C2,[0, 2 ],D,R,C,ifBuy;
		// str[ADD,MOVE,REMOVE],D,R,C,D2,R2,C2,[ADD,REMOVE],D,R,C,ifBuy;
		boolean add = false;
		boolean rem = false;
		boolean move = false;
		removeCardsActions();
		for (int i = 0; i < cardsDealt.length; i++)
			for (int r = 0; r < cardsDealt[i].cardsOnBoard.length; r++)
				for (int c = 0; c < cardsDealt[i].cardsOnBoard[r].length; c++)
					if (cardsDealt[i].cardsOnBoard[r][c] != 9999) {
						Card tCard = cardsDealt[i].getCard(cardsDealt[i].cardsOnBoard[r][c]);
						boolean possible = false;
						if (tCard.show && (!moveTo || (moveTo && !tCard.equals(exCard)))) {
							int ti = i;
							int tr = r;
							int tc = c;
							if ((tCard.strColor == aPID || tCard.strColor == 9) && tCard.strNum < 3
									&& players[aPID].str > 0) {
								STRButton addStr = new STRButton(moveTo ? 4 : 1, aPID);
								addStr.addMouseListener(new MouseAdapter() {
									public void mouseReleased(MouseEvent me) {
										if (((STRButton) me.getComponent()).releasedInside()) {
											players[aPID].str--;
											tCard.addStronghold(aPID);
											playersRepaint();
											TButton accept = new TButton(lt("confirm"), "green", 13, 3, 70);
											accept.addMouseListener(new MouseAdapter() {
												public void mouseReleased(MouseEvent me) {
													if (((TButton) me.getComponent()).releasedInside()) {
														if (moveTo) {
															aB.str += "" + ti + tr + tc;
														} else if (second) {
															aB.str += "0" + ti + tr + tc + "1";
															aB.doString();
														} else
															aB.str = "STR0" + ti + tr + tc + "000";

														addStronghold(tCard, second, false);
													}
												}
											});

											TButton back = new TButton(lt("back"), "yellow", 13, 3, 60);
											back.addMouseListener(new MouseAdapter() {
												public void mouseReleased(MouseEvent me) {
													if (((TButton) me.getComponent()).releasedInside()) {
														players[aPID].str++;
														tCard.removeStronghold();
														playersRepaint();
														setStrActions(moveTo, second, exCard);
													}
												}
											});

											removeCardsActions();
											tCard.setBorder(3);
											if (moveTo) {
												exposeCards(tCard, exCard);
												exCard.setBorder(3);
											} else
												exposeCards(tCard);

											back.setBounds((102 - back.getPreferredSize().width) / 2,
													150 - (back.getPreferredSize().height + 2),
													back.getPreferredSize().width + 1,
													back.getPreferredSize().height + 1);
											tCard.add(back);
											accept.setBounds((102 - accept.getPreferredSize().width) / 2,
													150 - (accept.getPreferredSize().height + 2) * 2,
													accept.getPreferredSize().width + 1,
													accept.getPreferredSize().height + 1);
											tCard.add(accept);
											clearAndAddInfoLabel(lt("str_confirm_or_back"));
										}
									}
								});
								addStr.setBounds(68, 77, 30, 30);
								possible = true;
								if (moveTo) {
									exCard.setBorder(3);
								} else
									add = true;
								tCard.add(addStr);
							}
							if (!moveTo) {
								if ((tCard.strColor == aPID || tCard.strColor == 9) && tCard.strNum > 0) {
									STRButton moveStr = new STRButton(3, aPID);
									moveStr.addMouseListener(new MouseAdapter() {
										public void mouseReleased(MouseEvent me) {
											if (((STRButton) me.getComponent()).releasedInside()) {
												aB.str = "STR1" + ti + tr + tc;
												players[aPID].str++;
												tCard.removeStronghold();
												playersRepaint();
												setStrActions(true, false, tCard);
											}
										}
									});
									moveStr.setBounds(68, 109, 30, 30);
									possible = true;

									tCard.add(moveStr);
									move = true;
								}

								if (tCard.strColor != aPID && tCard.strNum > 0) {
									STRButton delStr = new STRButton(2, tCard.strColor);
									delStr.addMouseListener(new MouseAdapter() {
										public void mouseReleased(MouseEvent me) {
											if (((STRButton) me.getComponent()).releasedInside()) {
												int plr = tCard.strColor;
												players[plr].str++;
												tCard.removeStronghold();
												playersRepaint();
												TButton accept = new TButton(lt("confirm"), "green", 13, 3, 70);
												accept.addMouseListener(new MouseAdapter() {
													public void mouseReleased(MouseEvent me) {
														if (((TButton) me.getComponent()).releasedInside()) {
															if (second) {
																aB.str += "2" + ti + tr + tc + "1";
															} else
																aB.str = "STR2" + ti + tr + tc + "000";
															removeStronghold(tCard, second);
														}
													}
												});

												TButton back = new TButton(lt("back"), "yellow", 13, 3, 60);
												back.addMouseListener(new MouseAdapter() {
													public void mouseReleased(MouseEvent me) {
														if (((TButton) me.getComponent()).releasedInside()) {
															players[plr].str--;
															tCard.addStronghold(plr);
															playersRepaint();
															setStrActions(moveTo, second, exCard);
														}
													}
												});

												removeCardsActions();
												exposeCards(tCard);
												tCard.setBorder(3);
												back.setBounds((102 - back.getPreferredSize().width) / 2,
														150 - (back.getPreferredSize().height + 2),
														back.getPreferredSize().width + 1,
														back.getPreferredSize().height + 1);
												tCard.add(back);
												accept.setBounds((102 - accept.getPreferredSize().width) / 2,
														150 - (accept.getPreferredSize().height + 2) * 2,
														accept.getPreferredSize().width + 1,
														accept.getPreferredSize().height + 1);
												tCard.add(accept);
												clearAndAddInfoLabel(lt("str_confirm_or_back"));

											}
										}
									});
									delStr.setBounds(68, 77, 30, 30);
									possible = true;
									tCard.add(delStr);
									rem = true;
								}

							} else {
								TButton back = new TButton(lt("back"), "yellow", 13, 3, 70);
								back.addMouseListener(new MouseAdapter() {
									public void mouseReleased(MouseEvent me) {
										if (((TButton) me.getComponent()).releasedInside()) {
											players[aPID].str--;
											exCard.addStronghold(aPID);
											playersRepaint();
											strongholdsAction(true, false, false);
										}
									}
								});
								back.setBounds((102 - back.getPreferredSize().width) / 2,
										150 - (back.getPreferredSize().height + 2), back.getPreferredSize().width + 1,
										back.getPreferredSize().height + 1);
								exCard.add(back);
							}
							tCard.repaint();
						}
						if (!possible && tCard != exCard)
							tCard.setGrayscale(true);
					}
		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new GridBagLayout());
		tempPanel.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 2, 8, 2);
		if (!moveTo) {
			clearAndAddInfoLabel(lt("do_str_action"));
			game.sendAction("click5");
			if (add) {
				gbc.gridx = 0;
				gbc.gridy = 0;
				tempPanel.add(new STRButton(1, aPID), gbc);
				gbc.gridx = 1;
				tempPanel.add(new JLabel(" - "), gbc);
				gbc.gridx = 2;
				tempPanel.add(new JLabel(lt("do_str_add")), gbc);

			}
			if (move) {
				gbc.gridx = 0;
				gbc.gridy = 1;
				tempPanel.add(new STRButton(3, aPID), gbc);
				gbc.gridx = 1;
				tempPanel.add(new JLabel(" - "), gbc);
				gbc.gridx = 2;
				tempPanel.add(new JLabel(lt("do_str_move")), gbc);
			}
			if (rem) {
				gbc.gridx = 0;
				gbc.gridy = 2;
				tempPanel.add(new STRButton(players), gbc);
				gbc.gridx = 1;
				tempPanel.add(new JLabel(" - "), gbc);
				gbc.gridx = 2;
				tempPanel.add(new JLabel(lt("do_str_remove")), gbc);
			}
		} else if (moveTo) {
			clearAndAddInfoLabel(lt("do_str_move_confirm"));
			tempPanel.add(new STRButton(4, aPID));
		}
		actionPanelContent.add(tempPanel);
	}

	public boolean checkStrBuy() {
		if (players[aPID].str == 0)
			for (int i = 0; i < cardsDealt.length; i++)
				for (int r = 0; r < cardsDealt[i].cardsOnBoard.length; r++)
					for (int c = 0; c < cardsDealt[i].cardsOnBoard[r].length; c++)
						if (cardsDealt[i].cardsOnBoard[r][c] != 9999) {
							Card tCard = cardsDealt[i].getCard(cardsDealt[i].cardsOnBoard[r][c]);
							if (tCard.show && tCard.strColor == aPID && tCard.strNum == 3)
								if (canBuy(tCard)) {
									strCardD = i;
									strCardR = r;
									strCardC = c;
									return true;
								}
						}
		return false;
	}

	public void setAriActions(boolean second) {
		aristocratsDealt.removeAllActions();

		boolean fp = false;
		for (int i = 0; i < aristocratsDealt.ariNum; i++) {
			Aristocrat temp = aristocratsDealt._arilist.get(i);
			int k = i;
			if (temp.isAvailable && !temp.isReserved) {
				if (!fp) {
					fp = true;
					clearAndAddInfoLabel(lt("select_ari"));
				}
				temp.setActive();
				temp.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent me) {
						aristocratsDealt.removeAllActions();
						temp.border = 2;
						temp.repaint();
						TButton accept = new TButton(lt("confirm"), "green", 11, 5);
						accept.addMouseListener(new MouseAdapter() {
							public void mouseReleased(MouseEvent me) {
								if (((TButton) me.getComponent()).releasedInside()) {
									if (optCities) {
										rightPanel.removeAll();
										GridBagConstraints gbc = new GridBagConstraints();
										gbc.fill = GridBagConstraints.BOTH;
										gbc.gridx = 0;
										gbc.gridy = 0;
										gbc.weighty = 1;
										gbc.insets = new Insets(0, 0, 0, 30);
										rightPanel.add(aristocratsDealt, gbc, 0);
										gbc.insets = new Insets(0, 30, 0, 0);
										rightPanel.add(citiesDealt, gbc, 0);

									}
									setResAri(k, second, false);
								}
							}
						});

						TButton back = new TButton(lt("back"), "yellow", 11, 5);
						back.addMouseListener(new MouseAdapter() {
							public void mouseReleased(MouseEvent me) {
								if (((TButton) me.getComponent()).releasedInside()) {
									setAriActions(second);
								}
							}
						});
						back.setBounds((85 - back.getPreferredSize().width) / 2,
								85 - (back.getPreferredSize().height + 2), back.getPreferredSize().width + 1,
								back.getPreferredSize().height + 1);
						temp.add(back);
						accept.setBounds((85 - accept.getPreferredSize().width) / 2,
								85 - (accept.getPreferredSize().height + 2) * 2, accept.getPreferredSize().width + 1,
								accept.getPreferredSize().height + 1);
						temp.add(accept);
					}
				});
			}
		}
		if (!fp) {
			setResAri(9, second, false);
		} else {
			if (optCities) {
				rightPanel.removeAll();
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.fill = GridBagConstraints.BOTH;
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.weighty = 1;
				gbc.insets = new Insets(0, 30, 0, 0);
				rightPanel.add(citiesDealt, gbc, 0);
				gbc.insets = new Insets(0, 0, 0, 30);
				rightPanel.add(aristocratsDealt, gbc, 0);
			}
		}
	}

	public void addConfirmButton(int[] coinsAct, int actionID, int d, boolean second) {
		TButton confirmBtn;
		switch (actionID) {
		case 0: // 0 zakup karty
		case 6:// 6 zakup karty z rezerwacji
				// addInfoLabel("Kupujesz kartê:");
			confirmBtn = new TButton(lt("buy"), "green", 14);
			confirmBtn.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						showPaymentDiscard();
						int j = 0;
						boolean resAri = false;
						for (CardsDealt t : cardsDealt) {
							for (int i = 0; i < 3; i++) {
								if (t.cancelPossible[i]) {
									players[aPID].addCard(t.getActCard(i));
									if (t.getActCard(i).special == 6) {
										resAri = true;
									}
									if (t.getActCard(i).special == 5) {
										if (second) {
											players[aPID].takeCard(frc[0]);
											players[aPID].takeCard(frc[1]);
										} else {
											players[aPID].takeCard(fra[0]);
											players[aPID].takeCard(fra[1]);
										}
									}
									if (i == 1) {
										if (second) {
											aB.frcAct = "FRC" + j + t.clRow[i] + t.clCol[i] + t.getActCard(i).color;
										} else
											aB.fraAct = "FRA" + j + t.clRow[i] + t.clCol[i] + t.getActCard(i).color;
									}
									if (i == 2) {
										if (second) {
											aB.frdAct = "FRD" + j + t.clRow[i] + t.clCol[i] + t.getActCard(i).color;
										} else
											aB.frbAct = "FRB" + j + t.clRow[i] + t.clCol[i] + t.getActCard(i).color;
									}
								}
							}
							j++;
						}
						if (actionID == 6) {
							players[aPID].addCard(players[aPID].clCard);
							if (players[aPID].clCard.special == 5) {
								if (second) {
									players[aPID].takeCard(frc[0]);
									players[aPID].takeCard(frc[1]);
								} else {
									players[aPID].takeCard(fra[0]);
									players[aPID].takeCard(fra[1]);
								}
							}
							if (players[aPID].clCard.special == 6) {
								resAri = true;
							}

						}
						coinsDealt.addCoins(coinsAct);
						String addToSub = "";
						for (int i = 0; i < 6; i++) {
							addToSub = addToSub + coinsAct[i];
						}
						if (second) {
							aB.stcn = "STCN" + addToSub;
						} else
							aB.subAct += addToSub;
						int usedDbl = (int) (Math.ceil((double) (second ? dbld : dblc) / 2));
						if (usedDbl > 0) {
							players[aPID].removeDBLCards(usedDbl);
							if (second) {
								aB.dbld = "DBLD" + (dbld < 10 ? "0" + dbld : dbld);
							} else
								aB.dblc = "DBLC" + (dblc < 10 ? "0" + dblc : dblc);
						}
						dblc = 0;
						if (second)
							dbld = 0;
						if (resAri) {
							setAriActions(second);
						} else if (second) {
							endTurnP1second(false);
						} else
							endTurnP1(true);
					}
				}
			});
			break;
		case 1: // 1 - rezerwacja + z³oty coin
			// addInfoLabel("Rezerwujesz kartê:");
			confirmBtn = new TButton(lt("res"), "green", 14);
			confirmBtn.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						coinsDealt.coinsStack[5].takeCoin(1);
						players[aPID].addCoin(5);

						if (players[aPID].coinsSum() > 10) {
							System.out.println("ConfirmButton1+Coins>10");
							checkCoinsLimit(new int[] { 0, 0, 0, 0, 0, 1 }, d, false, false, false);
						} else {
							players[aPID].addResCard(cardsDealt[d].getActCard(0));
							endTurnP1(false);
						}
					}
				}
			});
			break;
		case 2: // 2 - rezerwacja bez coina
			// addInfoLabel("Rezerwujesz kartê:");
			confirmBtn = new TButton(lt("res"), "green", 14);
			confirmBtn.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						players[aPID].addResCard(cardsDealt[d].getActCard(0));
						endTurnP1(false);

					}
				}
			});
			break;
		case 3: // 3 - wziêcie coinów
			// addInfoLabel("Bierzesz ¿etony:");
			confirmBtn = new TButton(lt("take_coins"), "green", 14);
			confirmBtn.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						coinsDealt.removeAllActions();
						coinsDealt.takeCoins(coinsAct);
						players[aPID].addCoins(coinsAct);
						if (players[aPID].coinsSum() > 10) {
							System.out.println("ConfirmButton3>10");
							checkCoinsLimit(coinsAct, d, false, false, false);
						} else
							endTurnP1(false);

					}
				}
			});
			break;
		case 4: // 4 - wziêcie i zwrot ¿etonów
			// addInfoLabel("Bierzesz ¿etony:");
			confirmBtn = new TButton(lt("confirm"), "green", 14);
			confirmBtn.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						String addToSub = "B";
						for (int i = 0; i < 6; i++) {
							addToSub = addToSub + coinsAct[i];
						}
						aB.subAct += addToSub;
						if (second) {
							endTurnP2();
						} else
							endTurnP1(false);
					}
				}
			});
			break;
		case 5: // 5 - rezerwacja + coin + zwrot
			// addInfoLabel("Rezerwujesz kartê:");
			confirmBtn = new TButton(lt("confirm"), "green", 14);
			confirmBtn.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						String addToSub = "B";
						for (int i = 0; i < 6; i++) {
							addToSub = addToSub + coinsAct[i];
						}
						aB.subAct += addToSub;
						players[aPID].addResCard(cardsDealt[d].getActCard(0));
						if (second) {
							endTurnP2();
						} else
							endTurnP1(false);
					}
				}
			});
			break;
		default:
			confirmBtn = new TButton(lt("error"), "red", 14);
			break;
		}
		actionPanelButtons.add(confirmBtn);
		actionPanel.repaint();
		actionPanel.revalidate();
	}

	public void checkCoinsLimit(int[] coinsOrigin, int d, boolean fin, boolean afterQuestion, boolean afterRes) {
		aB.doString();
		boolean strCanBuy = checkStrBuy();
		if (optStrongholds && strCanBuy && !afterQuestion) {
			// przed pytaniem
			removeCardsActions();
			clearAndAddInfoLabel(lt("three_strongholds"));
			cardsDealt[strCardD].getCard(cardsDealt[strCardD].cardsOnBoard[strCardR][strCardC]).setBorder(3);
			TButton backButton = new TButton(lt("cancel"), "yellow", 14);
			backButton.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						players[aPID].takeCoins(coinsOrigin);
						coinsDealt.addCoins(coinsOrigin);
						cancelAction(false);
					}
				}
			});
			actionPanelContent.add(backButton);

			TButton btnNoBuy = new TButton(lt("no_back_coins"), "yellow", 14);
			btnNoBuy.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						System.out.println("checkCoinsLimitNO");
						checkCoinsLimit(coinsOrigin, d, false, true, false);
					}
				}
			});

			TButton btnBuy = new TButton(lt("yes_go_pay"), "green", 14);
			btnBuy.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						strCoinsNeeded = coinsOrigin;
						strAD = d;
						aB.str = "STR0000000";
						if (coinsOrigin[5] == 1) {
							players[aPID].addResCard(cardsDealt[d].getActCard(0));
							players[aPID].confirmAction();
							// confirmCardsMove();
							// removeCardsActions();

						}
						removeCardsActions();
						if (confirmCardsMove()) {
							clearAndAddInfoLabel("Wyk³adam karty...");
							Timer timer = new Timer(isRecovery ? 0 : 1500, new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									actionBuyCard(strCardR, strCardC, strCardD, false, true);
								}
							});
							timer.setRepeats(false);
							timer.start();
							return;
						} else
							actionBuyCard(strCardR, strCardC, strCardD, false, true);

					}
				}
			});
			actionPanelContent.add(btnNoBuy);
			actionPanelContent.add(btnBuy);
			actionPanel.repaint();
			actionPanel.revalidate();
			return;
		}

		int c = players[aPID].coinsSum();
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setOpaque(false);
		Insets insets = new Insets(1, 1, 1, 1);
		for (int i = 0; i < 6; i++) {
			if (players[aPID].ownedCoins[i] > 0 && c > 10) {
				int b = i;
				ArrButton buttonUp = new ArrButton(0, true);
				buttonUp.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent me) {
						if (((ArrButton) me.getComponent()).releasedInside()) {
							coinsDealt.coinsStack[b].addCoin(1);
							players[aPID].takeCoin(b);
							checkCoinsLimit(coinsOrigin, d, fin, afterQuestion, afterRes);
						}
					}
				});

				panel.add(buttonUp, gbc(i, 0, 1, 1, insets));
			} else if (coinsDealt.coinsStack[i].numUp > 0 || players[aPID].ownedCoins[i] > 0) {
				panel.add(new ArrButton(0, false), gbc(i, 0, 1, 1, insets));
			}
			if (coinsDealt.coinsStack[i].numUp > 0) {
				int b = i;
				ArrButton buttonDown = new ArrButton(1, true);
				buttonDown.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent me) {
						if (((ArrButton) me.getComponent()).releasedInside()) {
							coinsDealt.coinsStack[b].takeCoin(1);
							players[aPID].addCoin(b);
							checkCoinsLimit(coinsOrigin, d, fin, afterQuestion, afterRes);
						}
					}
				});

				panel.add(buttonDown, gbc(i, 1, 1, 1, insets));
			} else if (players[aPID].ownedCoins[i] > 0) {
				panel.add(new ArrButton(1, false), gbc(i, 1, 1, 1, insets));
			}
			if (coinsDealt.coinsStack[i].numUp > 0 || players[aPID].ownedCoins[i] > 0)
				if (i < 5) {
					panel.add(Utils.cardAndCoinDraw(i, players[aPID].production[i], players[aPID].ownedCoins[i], 1),

							gbc(i, 2, 1, 1, insets));
				} else
					panel.add(Utils.cardAndCoinDraw(i, 0, players[aPID].ownedCoins[i], 1),

							gbc(i, 2, 1, 1, insets));

		}
		String text;
		if (c > 10) {
			// TODO
			game.sendAction("click3");

			// clearAndAddInfoLabel(lt("you_have_too_much") + (c - 10) + ":");
			text = lt("you_have_too_much") + (c - 10) + ".";

		} else {
			game.sendAction("click4");
			text = lt("g_you_give_back_enough");
			// clearAndAddInfoLabel(lt("g_you_give_back_enough"));

		}
		String labelText = String.format("<html><div style=\"width:%dpx;\"><center>%s</center></div></html>", 160,
				text);
		JLabel label = new JLabel(labelText);
		label.setFont(new Font("Verdana", Font.BOLD, 13));
		clearAndAddInfoLabel("Zwrot ¿etonów:");
		actionPanelContent.add(panel);
		actionPanelContent.add(label);

		int coinsBack[] = new int[6];
		for (int i = 0; i < 6; i++) {
			coinsBack[i] = coinsDealt.coinsStack[i].numUp;
		}
		if (!fin) {
			TButton backButton = new TButton(strCanBuy ? lt("back") : lt("cancel"), "yellow", 14);
			backButton.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						coinsDealt.takeCoins(coinsBack);
						players[aPID].addCoins(coinsBack);
						if (strCanBuy) {
							System.out.println("checkCoinsLimitBACK");
							if (afterRes) {
								aB.str = "STR0000000";
								cancelAction(true);
							} else
								checkCoinsLimit(coinsOrigin, d, false, false, false);
						} else {
							players[aPID].takeCoins(coinsOrigin);
							coinsDealt.addCoins(coinsOrigin);
							cancelAction(false);
						}

					}
				}
			});

			actionPanelButtons.add(backButton);
		}
		if (c <= 10) {
			if (afterQuestion && !fin) {
				aB.str = "STR000000000000";
			}
			System.out.println(
					"Decyzja: " + ((fin || (!fin && coinsOrigin[5] != 1)) ? "bez rezerwacji" : "z rezerwacj¹"));
			addConfirmButton(coinsBack, (afterRes || fin || (!fin && coinsOrigin[5] != 1)) ? 4 : 5, d, afterQuestion);
		} else {
			TButton disConfirm = new TButton(lt("confirm"), "green", 14);
			disConfirm.setActive(false);
			actionPanelButtons.add(disConfirm);
		}
		// boardPanel.repaint();
		// boardPanel.revalidate();
	}

	public void addCancelButton(int[] coins, boolean second) {
		if (players[aPID].isLocal) {
			TButton cancelBtn = new TButton(second ? lt("back") : lt("cancel"), "yellow", 14);
			cancelBtn.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						if (coins != null) {
							players[aPID].addCoins(coins);
						}
						cancelAction(second);
					}
				}
			});
			actionPanelButtons.add(cancelBtn);
		}
	}

	public void showPaymentDiscard() {
		removeActionPanelListener();
		showPayment.removeAll();
		showPayment.setVisible(false);
	}

	public void cancelAction(boolean second) {
		// playersRepaint();
		showPaymentDiscard();
		dblc = 0;
		dbld = 0;
		players[aPID].cancelAction();
		cancelCardsMove();
		removeCardsActions();
		coinsDealt.removeAllActions();
		coinsDealt.allDown(true);
		if (second) {
			strongholdsActionP2(second);
		} else {
			startTurn(false);
		}
	}

	public void makeBoardLayout(int seed, int playerNum) {

		paymentInfo = new JPanel(new BorderLayout());
		paymentInfo.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 0, 255)));
		paymentInfo.setBackground(new Color(235, 235, 225));
		rightPanel = new JPanel(new GridBagLayout());
		rightPanel.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		int space = 60;
		if (optOrient)
			space = 30;
		if (optAris) {
			int ariNum = (playerNum + 1) > 5 ? 5 : (playerNum + 1);
			if (optCities) {
				gbc.insets = new Insets(0, 0, 0, space);
				aristocratsDealt = new AristocratsDealt(ariNum, seed, true);
			} else {
				aristocratsDealt = new AristocratsDealt(ariNum, seed, false);
			}
			rightPanel.add(aristocratsDealt, gbc, 0);
		}
		if (optCities) {
			if (optAris) {
				gbc.insets = new Insets(0, space, 0, 0);
				citiesDealt = new CitiesDealt(playerNum, seed, true);
			} else
				citiesDealt = new CitiesDealt(playerNum, seed, false);
			rightPanel.add(citiesDealt, gbc, 0);
		}

		playersPanel = new PlayersPanel(this);
		if (!isRecovery) {
			localPlayerPanel = new LocalPlayerPanel(game.localID, this);
			chatPanel = new ChatPanel(game, players[game.localID].playerName, true);
		}
		int c = optMoreCoins ? 8 : 7;
		if (playerNum == 3)
			c = 5;
		if (playerNum == 2)
			c = 4;
		coinsDealt = new CoinsDealt(c);

		decks = new JPanel();
		decks.setLayout(new BorderLayout(0, 0));
		decks.setOpaque(false);
		if (optOrient) {
			cardsDealt = new CardsDealt[2];
			cardsDealt[0] = new CardsDealt("standard", seed, false, optOrient);
			if (optAris) {
				cardsDealt[1] = new CardsDealt("orient", seed, true, optOrient);
			} else {
				cardsDealt[1] = new CardsDealt("orientNoAri", seed, true, optOrient);

			}

			decks.add(cardsDealt[0], BorderLayout.WEST);
			decks.add(cardsDealt[1], BorderLayout.CENTER);
		} else {
			cardsDealt = new CardsDealt[1];
			cardsDealt[0] = new CardsDealt("standard", seed, true, optOrient);
			decks.add(cardsDealt[0]);
		}

		scrollCards = new JScrollPane(decks, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollCards.setPreferredSize(new Dimension(624, 500));
		scrollCards.getViewport().setOpaque(false);
		scrollCards.setOpaque(false);
		scrollCards.setBorder(null);
		// scrollCards.setWheelScrollingEnabled(false);
		scrollCards.getHorizontalScrollBar().setOpaque(false);
		scrollCards.getHorizontalScrollBar().setUI(new MyScrollBarUI());
		scrollCards.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
		scrollCards.getHorizontalScrollBar().setUnitIncrement(1);
		if (optOrient) {
			scrollCards.addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					int pos = scrollCards.getViewport().getViewPosition().x;
					double cardSize = ((MyScrollBarUI) scrollCards.getHorizontalScrollBar().getUI()).cardSize;
					double offset = scrollCards.getViewport().getViewPosition().getX() % cardSize;
					if (e.getWheelRotation() < 0) {
						if (offset > 1) {
							scrollAnim.stop();
							mPos = (int) (pos - offset);
							System.out.println("test" + offset);
							scrollAnim.start();
						}

					} else if ((e.getWheelRotation() > 0)) {
						if (offset > 1) {
							scrollAnim.stop();
							mPos = (int) (pos + cardSize - offset);
							System.out.println("test" + offset);
							scrollAnim.start();
							// }

						}
					}
				}
			});

			scrollCards.getHorizontalScrollBar().addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					scrollAnim.stop();
				}

				public void mouseReleased(MouseEvent me) {
					int pos = scrollCards.getViewport().getViewPosition().x;
					double cardSize = ((MyScrollBarUI) scrollCards.getHorizontalScrollBar().getUI()).cardSize;
					double offset = scrollCards.getViewport().getViewPosition().getX() % cardSize;
					if (offset > cardSize * 0.5) {
						mPos = (int) (pos + cardSize - offset);
					} else {
						mPos = (int) (pos - offset);
					}
					scrollAnim.start();
				}
			});
			scrollAnim = new Timer(20, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {

					int actPos = scrollCards.getViewport().getViewPosition().x;
					if (actPos != mPos) {
						if (actPos > mPos) {
							int move = (actPos - mPos) / 5;
							if (move == 0)
								move = 1;
							scrollCards.getViewport().setViewPosition(new Point(actPos - move, 0));
						}
						if (actPos < mPos) {
							int move = (mPos - actPos) / 5;
							if (move == 0)
								move = 1;
							scrollCards.getViewport().setViewPosition(new Point(actPos + move, 0));
						}
					} else {
						Timer timer = (Timer) evt.getSource();
						timer.stop();
					}

					// game.mainFrame.getLayeredPane().getComponentCount()
				}
			});
			scrollAnim.setRepeats(true);
			scrollCards.getViewport().setViewPosition(new Point(104, 0));
		}

		// mainFrame.add(scrollPane);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		centerPanel.setOpaque(false);
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		JPanel filler1 = new JPanel(), filler2 = new JPanel();
		filler1.setOpaque(false);
		filler1.setPreferredSize(new Dimension(0, 0));
		filler2.setOpaque(false);
		filler2.setPreferredSize(new Dimension(0, 0));
		centerPanel.add(filler1, gbc);
		gbc.gridy = 2;
		centerPanel.add(filler2, gbc);
		gbc.gridy = 1;
		gbc.weighty = 0;
		gbc.weightx = 1;
		centerPanel.add(scrollCards, gbc);
		// boardPanel = new JPanel();
		boardPanel = new BackgroundPane();
		actionPanel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 8129653111528299762L;

			protected void paintComponent(Graphics g) {

				super.paintComponent(g);

				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(new Color(0, 0, 0, 20));
				g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, 15, 15);
				g2d.setColor(new Color(245, 255, 245, 100));
				g2d.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 4, 15, 15);
				g2d.dispose();

			}
		};
		actionPanel.setLayout(new BorderLayout(0, 0));
		actionPanel.setPreferredSize(new Dimension(220, 220));
		actionPanel.setOpaque(false);
		actionPanelHeader = new JPanel(new FlowLayout());
		actionPanelHeader.setOpaque(false);
		actionPanelContent = new JPanel(new FlowLayout());
		actionPanelContent.setOpaque(false);
		actionPanelButtons = new JPanel(new FlowLayout());
		actionPanelButtons.setOpaque(false);
		actionPanel.add(actionPanelHeader, BorderLayout.NORTH);
		actionPanel.add(actionPanelContent, BorderLayout.CENTER);
		actionPanel.add(actionPanelButtons, BorderLayout.SOUTH);
		boardPanel.setLayout(new GridBagLayout());
		boardPanel.setOpaque(true);
		// addComp(0, 0, 4, 1, label);
		if (!isRecovery) {
			addComp(1, 3, 1, 1, 1, 0, 0, 0, localPlayerPanel);
			addComp(2, 3, 2, 1, 1, 0, 0, 0, chatPanel);
		}
		if (players.length < 5) {
			addComp(0, 1, 1, 2, 1, 1, 0, 0, playersPanel);
			addComp(0, 3, 1, 1, 1, 0, 0, 0, new InfoPanel(lt("ver")));
		} else {
			addComp(0, 1, 1, 3, 1, 1, 0, 0, playersPanel);
		}
		addComp(1, 1, 1, 2, 10, 1, 0, 10, centerPanel);
		addComp(2, 1, 1, 2, 1, 1, 4, 0, rightPanel);
		addComp(3, 1, 1, 1, 1, 1, 0, 0, coinsDealt);
		addComp(3, 2, 1, 1, 1, 1, 0, 0, actionPanel);

	}

	public void addComp(int x, int y, int w, int h, int wx, int wy, int px, int py, Component component) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.ipadx = px;
		gbc.ipady = py;
		boardPanel.add(component, gbc);
	}

	public GridBagConstraints gbc(int x, int y, int w, int h, Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.insets = insets;
		return gbc;
	}

	public int countLack(Card card) {
		int lack = 0;
		for (int i = 0; i < 5; i++) {
			int cc = card.cost[i];
			int cp = players[aPID].getTotal(i);
			if (cc > cp)
				lack += cc - cp;
		}
		return lack;
	}

	public void addHistory(boolean ignoreSub) {
		JPanel tp = new JPanel();
		JPanel tpp = new JPanel();
		tpp.setLayout(new FlowLayout());
		tpp.setOpaque(false);
		tp.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0;
		gbc.weightx = 0;
		gbc.insets = new Insets(2, 5, 2, 5);
		int countA = actionPanelContent.getComponentCount();
		for (int i = 0; i < countA; i++) {
			int countB = ((JComponent) actionPanelContent.getComponent(0)).getComponentCount();

			if (countB > 0 && !ignoreSub) {
				for (int j = 0; j < countB; j++) {
					tpp.add(((JComponent) actionPanelContent.getComponent(0)).getComponent(0));
				}
			} else {
				tpp.add(actionPanelContent.getComponent(0));
			}

		}
		gbc.gridy = 1;
		tp.add(tpp, gbc);
		gbc.gridy = 2;
		gbc.weighty = 1;
		JLabel filler = new JLabel();
		tp.add(filler, gbc);

		if (actionPanelHeader.getComponentCount() > 0) {
			String labelText = ((JLabel) actionPanelHeader.getComponent(0)).getText();
			String text = labelText.substring(labelText.indexOf("<center>") + 8, labelText.indexOf("</center>"));
			Font font = ((JLabel) actionPanelHeader.getComponent(0)).getFont();
			int w = tpp.getPreferredSize().width > 125 ? tpp.getPreferredSize().width : 125;
			JLabel label = new JLabel(
					"<html><div style=\"width:" + w + ";\"><center>" + text + "</center></div></html>");
			label.setFont(font);
			gbc.gridy = 0;
			gbc.weighty = 0;
			gbc.weightx = 0;
			tp.add(label, gbc);
			// System.out.println("After W: " + label.getPreferredSize().getWidth());
		}

		playersPanel.addContent(aPID, tp);
	}

	public Card getCard(int i, int d) {
		return cardsDealt[d].deck._cardList.get(i);
	}

	public void setActionPanelListener() {
		actionPanel.addMouseListener(new MouseAdapter() {
			public void mouseExited(MouseEvent me) {
				showPayment.setVisible(false);
			}

			public void mouseEntered(MouseEvent me) {
				showPayment.setVisible(true);
			}

			public void mousePressed(MouseEvent me) {
				showPayment.setVisible(true);
			}
		});
		actionPanel.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				showPayment.setVisible(true);
			}
		});
	}

	public void removeActionPanelListener() {
		while (actionPanel.getMouseListeners().length != 0) {
			actionPanel.removeMouseListener(actionPanel.getMouseListeners()[0]);
		}
		while (actionPanel.getMouseWheelListeners().length != 0) {
			actionPanel.removeMouseWheelListener(actionPanel.getMouseWheelListeners()[0]);
		}
	}

	public void clearActionPanel() {
		removeActionPanelListener();
		actionPanelContent.removeAll();
		actionPanelHeader.removeAll();
		actionPanelButtons.removeAll();
		actionPanel.repaint();
		actionPanel.revalidate();
	}

	public void clearAndAddInfoLabel(String text) {
		clearActionPanel();
		String labelText = String.format("<html><div style=\"width:%dpx;\"><center>%s</center></div></html>", 160,
				text);
		JLabel label = new JLabel(labelText);
		label.setFont(new Font("Verdana", Font.BOLD, 13));
		actionPanelHeader.add(label);
		actionPanelHeader.repaint();
		actionPanelHeader.revalidate();
	}

	public void addInfoLabel(String text) {
		// clearActionPanel();
		actionPanelHeader.removeAll();
		String labelText = String.format("<html><div style=\"width:%dpx;\"><center>%s</center></div></html>", 160,
				text);
		JLabel label = new JLabel(labelText);
		label.setFont(new Font("Verdana", Font.BOLD, 13));
		actionPanelHeader.add(label);
		actionPanelHeader.repaint();
		actionPanelHeader.revalidate();
	}

	public void remoteInfoLabel(String s) {
		if (s == "start") {
			int n = new Random().nextInt(thinkText.length);
			clearAndAddInfoLabel(players[aPID].playerName + " " + thinkText[n]);
		} else
			clearAndAddInfoLabel(players[aPID].playerName + " " + s);
	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	public void lostConnection() {

		String text;
		// TODO translate
		text = "Utrata po³¹czenia!";
		clearAndAddInfoLabel(text);

		TButton closeApp = new TButton(lt("exit"), "red", 14);
		closeApp.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					game.closeTry();
				}
			}
		});
		TButton cancelRec = new TButton(lt("cancel"), "yellow", 14);
		Timer timer = new Timer(1000, new ActionListener() {

			int tick = 0;
			boolean listener = true;

			public void actionPerformed(ActionEvent evt) {
				tick++;
				Timer t = (Timer) evt.getSource();
				String dots = " .";
				for (int i = 0; i < tick % 10; i++)
					dots = dots + " .";
				clearAndAddInfoLabel(lt("trying_connect") + dots);
				if (listener) {
					RecThread rc = new RecThread(game);
					cancelRec.addMouseListener(new MouseAdapter() {
						public void mouseReleased(MouseEvent me) {
							if (((TButton) me.getComponent()).releasedInside()) {
								rc.running.set(false);
								t.stop();
								lostConnection();

							}
						}
					});
					rc.start();
					listener = false;
				}
				actionPanelButtons.add(cancelRec);
				actionPanelButtons.add(closeApp);
			}
		});
		timer.setRepeats(true);
		timer.setInitialDelay(0);
		TButton doRec = new TButton(lt("reconnect"), "green", 14);
		doRec.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					timer.start();
				}
			}
		});
		actionPanelButtons.add(doRec);
		actionPanelButtons.add(closeApp);
		actionPanel.repaint();
		actionPanel.revalidate();
	}

	public void playSound(String sound) {
		if (localPlayerPanel.soundEnabled && !isRecovery) {
			try {
				URL url = this.getClass().getClassLoader().getResource(sound);
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
				Clip clip = AudioSystem.getClip();
				clip.open(audioIn);
				clip.start();
				System.out.println(clip.getMicrosecondLength());
				Timer timer = new Timer((int) (clip.getMicrosecondLength() / 100), new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						clip.close();
					}
				});
				timer.setRepeats(false);
				timer.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void playersRepaint() {

		if (!isRecovery && game.localID != 900)
			localPlayerPanel.draw();
		playersPanel.repaint();
		playersPanel.revalidate();
	}

	public void panelsRepaint() {

	}

	public String lt(String s) {
		if (s.startsWith("g_")) {
			if (players[aPID].gender == 1) {
				s = "m" + s;
			} else if (players[aPID].gender == 2) {
				s = "f" + s;
			}

		}
		return game.lt(s);
	}

	public String lt(int gender, String s) {
		if (s.startsWith("g_")) {
			if (gender == 1) {
				s = "m" + s;
			} else if (gender == 2) {
				s = "f" + s;
			}

		}
		return game.lt(s);
	}

	public class ActionBuilder {
		int player;
		int action;
		String subAct = "", ariAct = "", cityAct = "";
		public String specAct = "";
		public String specAct2 = "";
		public String fraAct = "";
		public String frbAct = "";
		public String frcAct = "";
		public String frdAct = "";
		String dblc = "";
		String str = "";
		String dbld = "";
		String stcn = "";
		int c = 0;
		int r = 0;
		int d = 0;

		public ActionBuilder(int p) {
			this.player = p;
		}

		public void setRC(int a, int b, int c) {
			this.r = a;
			this.c = b;
			this.d = c;
		}

		public String doString() {
			String act = "ACTION" + player + action + subAct + r + c + d + ariAct + cityAct + specAct + specAct2
					+ fraAct + frbAct + frcAct + frdAct + dblc + dbld + str + stcn;
			String act2 = "ACTION: " + action + " SUB: " + subAct + " RCD: " + r + c + d + " OTHER:" + ariAct + cityAct
					+ specAct + specAct2 + fraAct + frbAct + frcAct + frdAct + dblc + dbld + str + stcn;
			System.out.println(act2);
			return act;
		}

	}

	class MyScrollBarUI extends BasicScrollBarUI {
		private final Dimension d = new Dimension();
		int cardNum = optOrient ? 8 : 5;
		int w, gap = 0;
		double gapSize, cardSize;

		@Override
		protected JButton createDecreaseButton(int orientation) {
			return new JButton() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1699373200824136594L;

				@Override
				public Dimension getPreferredSize() {
					return d;

				}
			};
		}

		@Override
		protected JButton createIncreaseButton(int orientation) {
			return new JButton() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 3013708596920469598L;

				@Override
				public Dimension getPreferredSize() {
					return d;
				}
			};
		}

		@Override
		protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// g2.setPaint(new Color(255, 255, 255, 150));
			// g2.fillRoundRect(r.x, r.y, r.width - 1,. , r.height - 1, 10, 10);
			g2.setPaint(new Color(0, 0, 0, 50));
			gapSize = r.getWidth() / scrollCards.getViewport().getViewSize().getWidth() * gap;
			// System.out.println("gapSize "+gapSize);
			double w2 = (r.getWidth() - gapSize) / cardNum;
			w = (int) w2;
			int h = r.height - 1;
			for (int i = 0; i < cardNum; i++) {
				int x = r.x + w * i;
				if (i > 4)
					x += gapSize;
				g2.setPaint(new Color(255, 255, 255, 150));
				g2.fillRoundRect(x + 7, r.y, w - 10, h, h, h);
				g2.setPaint(new Color(0, 0, 0, 50));
				g2.drawRoundRect(x + 7, r.y, w - 10, h, h, h);

			}

			// g2.drawOval(r.x, r.y, r.height - 1, r.height - 1);
		}

		@Override
		protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Color color = null;
			JScrollBar sb = (JScrollBar) c;
			if (!sb.isEnabled()) {
				return;
			} else if (isDragging) {
				color = Color.GRAY;
			} else if (isThumbRollover()) {
				color = Color.GRAY;
			} else {
				color = Color.DARK_GRAY;
			}
			// g2.setPaint(color);
			// int w = r.width / cardNum;
			int h = r.height - 1;
			cardSize = ((scrollCards.getViewport().getViewSize().getWidth() - gap) / cardNum);
			// System.out.println("cardSize "+cardSize);
			double cardVis = (scrollCards.getViewport().getSize().getWidth() - gap) / cardSize;
			// System.out.println("gap " + gap + " cardVis " + cardVis);
			if (cardVis > 6.001) {
				gap++;
				// cardVis = scrollCards.getViewport().getSize().getWidth() /
				// ((scrollCards.getViewport().getViewSize().getWidth()+gap)/cardNum);
				// System.out.println("gap " + gap + " cardVis " + cardVis);
				((BorderLayout) decks.getLayout()).setHgap(gap);
				decks.repaint();
				decks.revalidate();
				scrollCards.repaint();
				scrollCards.revalidate();
			}
			if (gap != 0) {
				if (cardVis < 5.999) {
					gap--;
					// cardVis = scrollCards.getViewport().getSize().getWidth() /
					// ((scrollCards.getViewport().getViewSize().getWidth()+gap)/cardNum);

					((BorderLayout) decks.getLayout()).setHgap(gap);
					decks.repaint();
					decks.revalidate();
					scrollCards.repaint();
					scrollCards.revalidate();
				}
			}
			g2.setPaint(color);
			for (int i = 0; i < cardNum; i++) {
				int x = w * i;
				double rx = r.x;
				if (i > 4) {
					x += gapSize;
					rx = r.x + gapSize;
				}
				if (x >= rx && x <= rx + cardVis * w - w) {
					g2.fillRoundRect(x + 7, r.y, w - 10, h, h, h);
					g2.drawRoundRect(x + 7, r.y, w - 10, h, h, h);

				} else if (x > rx - w && x < rx) {

					double pos = ((rx % w) / (double) w * (w - 10));
					g2.fillRoundRect(x + 7 + (int) pos, r.y, w - (int) pos - 10, h, h, h);
					g2.drawRoundRect(x + 7 + (int) pos, r.y, w - (int) pos - 10, h, h, h);

				} else if (x > rx + cardVis * w - w && x < +rx + cardVis * w - 2) {

					double pos = ((rx + cardVis * w - x) / w * (w - 10));
					g2.fillRoundRect(x + 7, r.y, (int) pos, h, h, h);
					g2.drawRoundRect(x + 7, r.y, (int) pos, h, h, h);

				}

			}
			// g2.setPaint(new Color(255, 255, 255, 50));
			// g2.fillRoundRect(r.x, r.y, r.width, r.height/2, r.height/2, r.height/2);
			g2.dispose();
		}

		@Override
		protected void setThumbBounds(int x, int y, int width, int height) {
			super.setThumbBounds(x, y, width, height);
			scrollbar.repaint();
		}
	}

}
