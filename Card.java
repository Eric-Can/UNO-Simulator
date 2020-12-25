/*
 *Card class
 *
 * Card class that contains all the aspects of a card in the game of UNO
 *
 */
public class Card {
    private Colour colour;
    private Symbol symbol;
    public Card(Colour c, Symbol s) {//constructor
        colour = c;
        symbol = s;
    }
    public Colour getColour() {//getters & setters
        return colour;
    }
    public Symbol getSymbol()  {
        return symbol;
    }
    public String toString(){
        return colour+" "+symbol;
    }
}
