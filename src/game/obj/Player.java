package game.obj;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import game.main.Board;
import game.main.Utils;

public class Player implements Comparable<Player> {

	public int points = 0, reservedCards = 0, clN, gender, gratisCoins = 0, str=3;
	public int[] ownedCoins = new int[6], production = new int[5], ownedCards = new int[5];
	public Card[] resCard = new Card[3];
	public boolean isLocal, isOnline = true, haveCity = false;
	public String playerName;
	Board board;
	public Card clCard, clCard2;
	public boolean cancelPossible = false;
	public List<Card> _cardList = new ArrayList<Card>();

	public Player(String playerName, boolean isLocal, int gender, Board board) {
		this.board = board;
		this.isLocal = isLocal;
		this.gender = gender;
		this.playerName = playerName;
		for (int i = 0; i < 6; i++)
			ownedCoins[i] = board.debug?2:0;
		for (int i = 0; i < 5; i++)
			production[i] = board.debug?2:0;
	}

	public int getTotal(int i) {
		int a = ownedCoins[i] + production[i];
		return a;
	}

	public void takeCoin(int n) {
		ownedCoins[n] -= 1;
		board.playersRepaint();

	}

	public void addCoin(int n) {
		ownedCoins[n] += 1;
		board.playersRepaint();
	}

	public int coinsSum() {
		int n = IntStream.of(ownedCoins).sum();
		return n;
	}

	public int[] checkCoinsNeeded(int cost[]) {
		int payCoins[] = new int[6];
		payCoins[5] = 0;
		for (int i = 0; i < 5; i++) {
			payCoins[i] = 0;
			if (production[i] < cost[i]) {
				if (cost[i] > getTotal(i)) {
					payCoins[i] = ownedCoins[i];
					payCoins[5] += cost[i] - getTotal(i);
				} else
					payCoins[i] = cost[i] - production[i];
			}
		}
		return payCoins;
	}

	public void addPoints(int n) {
		points += n;
		board.playersRepaint();
	}

	public void takeCoins(int[] n) {
		for (int i = 0; i < n.length; i++) {
			ownedCoins[i] -= n[i];
		}
		board.playersRepaint();
	}

	public void addCoins(int[] n) {
		for (int i = 0; i < n.length; i++) {
			ownedCoins[i] += n[i];
		}
		board.playersRepaint();
	}

	public void addResCard(Card card) {
		System.out.println(card);
		str+=card.strNum;
		card.clearStr();
		resCard[reservedCards] = card;
		reservedCards += 1;
		if (isLocal)
			card.showFront(true);
		board.playersRepaint();
	}

	public void takeResCard(int n) {
		clCard = resCard[n];
		clCard.showFront(true);
		clN = n;
		for (int i = 0; i < reservedCards; i++) {
			if (i > n) {
				resCard[i - 1] = resCard[i];
			}
		}
		reservedCards -= 1;
		cancelPossible = true;
		board.playersRepaint();
	}

	public void cancelAction() {
		if (cancelPossible) {
			for (int i = reservedCards; i > clN; i--) {
				resCard[i] = resCard[i - 1];
			}
			resCard[clN] = clCard;
			reservedCards += 1;
			cancelPossible = false;
		}
		board.playersRepaint();
	}

	public void confirmAction() {
		cancelPossible = false;
	}

	/*
	 * public void addCard(int n) { ownedCards[n] += 1; board.playersRepaint(); }
	 */
	public void addCard(Card card) {
		str+=card.strNum;
		card.clearStr();
		
		if (card.special == 4) {
			gratisCoins += 2;
			card.color = 5;
		} else {
			production[card.color] += card.value;
			points += card.points;
			ownedCards[card.color]+=1;
		}
		Card tempCard = Utils.copyCard(card,true,false);
		_cardList.add(tempCard);
		// if (isLocal) board.localPlayerPanel.addReward(tempCard);
		board.playersRepaint();
	}

	public void takeCard(int id) {
		//Card card;
		for (Card t : _cardList) {
			if (t.id==id) {
				production[t.color] -= t.value;
				points -= t.points;
				ownedCards[t.color]-=1;
				_cardList.remove(t);
				break;
			}
		}
		board.playersRepaint();
	}

	public void removeDBLCards(int n) {
		for (int i = 0; i < n; i++) {
			for (Card t : _cardList) {
				if (t.special == 4) {
					gratisCoins -= 2;
					_cardList.remove(t);
					break;
				}
			}
		}
	}

	public Integer getPoints() {
		return points;
	}

	public Integer cardSum() {
		return IntStream.of(ownedCards).sum();
	}

	public Integer haveCity() {
		return haveCity ? 1 : 0;
	}

	@Override
	public int compareTo(Player p) {
		if (this.haveCity != p.haveCity) {
			return this.haveCity().compareTo(p.haveCity());
		} else if (this.getPoints() != p.getPoints()) {
			return this.getPoints().compareTo(p.getPoints());
		} else {
			return p.cardSum().compareTo(this.cardSum());
		}
	}
}
