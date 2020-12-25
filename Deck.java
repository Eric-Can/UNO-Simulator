/* Deck Class
 *
 *Class that creates all the cards in an UNO deck and shuffles it
 *
 */




import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> Deck = new ArrayList<>();

    public Deck() {//constructor
        createDeck();
    }
    public Card dealCard(){ //deals a card from the deck
        if(cardsLeft() == 0) {
            Deck = new ArrayList<>();
            createDeck(); //create new deck if empty (add this later)
        }
        Card c = Deck.get(0);//pick card from the deck
        Deck.remove(0);//remove card from the deck
        return c;
    }
    public int cardsLeft() {
        return Deck.size();
    }
    private void createDeck() {
        Colour[] colours = Colour.values();
        Symbol[] symbols = Symbol.values();
        //making cards with colour yellow, green, blue, red
        for (int i = 0; i < symbols.length-2; i++) {//ignoring wild & draw4 symbols for now, making all other cards
            for (int j = 0; j < colours.length-1; j++) {//ignoring black for now
                if (i == 0) {
                    Card c = new Card(colours[j],symbols[i]);//1 of each colour for 0
                    Deck.add(c);
                } else {
                    for (int t = 0; t < 2; t++) {//2 of each symbol per colour
                        Card c = new Card(colours[j],symbols[i]);
                        Deck.add(c);
                    }
                }
            }
        }
            for(int i = symbols.length-2; i < symbols.length; i++) {//making black cards (Wild & Wild draw 4
                for (int j = 0; j < 4; j++) {//need 4 cards
                    Card c = new Card(Colour.BLACK, symbols[i]);
                    Deck.add(c);
                }
            }
            Collections.shuffle(Deck);//shuffle the deck
        }
    }
