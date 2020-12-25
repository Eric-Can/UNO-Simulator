/* Player Class
 *
 *Class that has the attributes of a player in the card game UNO
 *
 */

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Card> hand;

    public Player(String name, ArrayList<Card> h) { //constructor
        hand = h;
        this.name = name;
    }

    public String getName() {//getters and setters
        return name;
    }

    public ArrayList<Card> getHand() {
        return this.hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void addCard(Card c) { //add a card to the player's hand
        hand.add(c);
    }

    public void removeCard(Card c) { //remove a card from the player's hand
        hand.remove(c);
    }

    public String toString() {
        return name + "'s hand: " + hand;
    }
}
