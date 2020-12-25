/* GameSim Class
 *
 * Game simulator for the UNO card game
 *
 */


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class GameSim {
    private static CircularDoublyLinkedList<Player> players;
    private Deck cards = new Deck();
    private static Card lastCard; //tracks the last card
    private boolean gameOver = false; //checks if game is over
    public GameSim(String... names) {//variable arguments, makes an array of players
        players = new CircularDoublyLinkedList<>();
        if (names.length > 1 && names.length < 11) {// 2-10 players in UNO
            for (String name : names) {//adding players
                ArrayList<Card> hand = new ArrayList<>();
                for (int i = 0; i < 7; i++) {//dealing 7 cards to each player
                    hand.add(cards.dealCard());
                }
                Player pl = new Player(name, hand);
                players.addLast(pl);//adding players to game
            }
        } else System.out.println("Insignificant player amount");
        lastCard = cards.dealCard();//flip the first card
    }
    //on the first draw, if the card is special, it will be activated on teh first player. If teh card is wild draw 4, the first player picks up 4 and misses their turn
    //the second player chooses the colour
    public void playGame() {
        boolean usedTurn = false; //checks if the user used their turn or not
        System.out.println("The first card is " + lastCard);
        if (lastCard.getColour().equals(Colour.BLACK)) {//first player chooses color if first drawn card is wild or wild draw 4.
            if(lastCard.getSymbol().equals(Symbol.WildDraw4)) {
                for (int i = 0; i < 4; i++) {//making player draw 4 cards
                    players.first().addCard(cards.dealCard());
                }
                System.out.println("Sucks to be "+players.first()+" they will draw 4 and miss their first turn");
                players.rotate();//first player misses turn and second player gets to pick the colour
            }
            Colour wildColour = this.pickWildColour(players.first());
            System.out.println(players.first() + "\n" + players.first().getName() + " chooses the colour as " + wildColour);
            if (lastCard.getSymbol().equals(Symbol.WILD)) lastCard = new Card(wildColour, Symbol.WILD); //adjusting the colour of first card to chosen colour
            else {
                lastCard = new Card(wildColour, Symbol.WildDraw4); //adjusting the colour of first card to chosen colour
            }
        } else if (lastCard.getSymbol().equals(Symbol.DRAW2) || lastCard.getSymbol().equals(Symbol.SKIP) || lastCard.getSymbol().equals(Symbol.REVERSE)) { //if first card is special, first player is effected by it
            this.checkSpecialCard(players.first(), lastCard, true);
        }
        ArrayList<Card> blackCards = new ArrayList<>();//keeps track of the # of black cards that a user can have
        boolean playableCard = false; //checks to see if the user played a card or not
        Player activePlayer;//who's turn it is
        while (!gameOver) {//continue until the game is over
            activePlayer = players.first();
            ArrayList<Card> Hand = activePlayer.getHand();//getting active player's hand
            for (Card c : Hand) { //checking each card in player's hand to see if it's playable and playing it
                playableCard = this.isPlayableCard(c);
                if (playableCard && !usedTurn) {//if you have a playable card, play it
                    if (!c.getColour().equals(Colour.BLACK)) {//play if normal card, but save black cards for last if you don't have anything else
                        this.playCard(c);
                        usedTurn = true;//user has used their turn
                        break;//don't count the black cards
                    }
                    blackCards.add(c); //counting special cards
                }
            }
            if (blackCards.size() > 0 && !usedTurn) {//if you have a black card and didn't use your turn, play the black card
                playCard(blackCards.get(0));
                usedTurn = true;
            }
            if (!playableCard && !usedTurn) {//draw a card if none of yours work
                if (cards.cardsLeft() == 0) cards = new Deck(); //create a new deck if out of cards
                Card newCard = cards.dealCard();
                System.out.println("\n"+players.first() + "\n" + activePlayer.getName() + " doesn't have a playable card, they draw a " + newCard);
                playableCard = this.isPlayableCard(newCard); //check if the drawn card is playable or not
                if (playableCard) { //play if drawn card is playable, add to hand if not
                    this.playCard(newCard);
                } else {
                    activePlayer.addCard(newCard);//add card to hand
                    System.out.println("can't play it\n");
                }
            }
            if (!gameOver) {//reset these variables if game isn't over
                players.rotate(); //next player's turn
                playableCard = false;//reset the playable card
                usedTurn = false; //reset turn for next player
                blackCards.clear();//clearing black cards for next player
            }
        }
    }

    public Boolean isPlayableCard(Card c) {//checks if the card is playable
        Colour lastColour = lastCard.getColour();
        Symbol lastSymbol = lastCard.getSymbol();
        if (c.getColour().equals(lastColour)) { //if card has same colour or symbol as last card, it's playable
            return true;
        } else if (c.getSymbol().equals(lastSymbol)) {
            return true;
        } else return c.getColour().equals(Colour.BLACK); //if you have a black card, its playable
        //only returns false if the card isn't playable
    }

    public void playCard(Card c) {//play the given card
        Player activePlayer = players.first();
        System.out.println("\n" + activePlayer + "\n" + activePlayer.getName() + " plays " + c + "\n");
        if (c.getSymbol().equals(Symbol.DRAW2) || c.getSymbol().equals(Symbol.SKIP) || c.getSymbol().equals(Symbol.REVERSE)) { //checking if the card is special
            this.checkSpecialCard(activePlayer, c, false);
        } else if (!c.getColour().equals(Colour.BLACK)) { //if card is not black or special, its a basic card. so play it
            activePlayer.removeCard(c); //play the card
            gameOver = checkIfWon(activePlayer);//checking for someone with UNO or if the game is over
            lastCard = c;
        } else if (c.getColour().equals(Colour.BLACK)) {//playing special black card
            Colour wildColour = this.pickWildColour(activePlayer);//pick which colour to make the wild
            System.out.println("The colour is now " + wildColour);
            activePlayer.removeCard(c);//removing card from hand
            gameOver = checkIfWon(activePlayer);//checking for someone with UNO or if the game is over
            lastCard = new Card(wildColour, Symbol.WILD); //changing colour of wild card
            if (c.getSymbol().equals(Symbol.WildDraw4)) {
                players.rotate();//next player misses turn
                if(!gameOver) System.out.println(players.first().getName() + " misses a turn & draws 4");//only show if game isn't over
                for (int i = 0; i < 4; i++) {//making player draw 4 cards
                    if (cards.cardsLeft() == 0) cards = new Deck();
                    players.first().addCard(cards.dealCard());//next player will pick up 4 and miss turn
                }
                lastCard = new Card(wildColour, Symbol.WildDraw4);//changing colour of wild card
            }
        }
    }

    public void checkSpecialCard(Player pl, Card c, boolean firstCard) { //checks if the given card is special, who has the card and is it the first card of the game
        if (c.getSymbol().equals(Symbol.DRAW2)) { //checking if the card is a draw 2
            if (!firstCard) {//only remove card from hand if it's the first card of the game
                pl.removeCard(c);//removing card from hand
                gameOver = checkIfWon(pl);//checking for someone with UNO or if the game is over
            }
            players.rotate();//skip next player's turn
            if(!gameOver) System.out.println((firstCard ? pl.getName() : players.first().getName()) + " misses a turn & draws 2");//only show if game isn't over
            for (int i = 0; i < 2; i++) {//next player picks up 2 cards
                if (cards.cardsLeft() == 0) cards = new Deck();
                players.first().addCard(cards.dealCard());//next player will pick up 2 and miss turn
            }
            lastCard = c;//update last card
        } else if (c.getSymbol().equals(Symbol.SKIP)) { //check if the card is a skip card
            if (!firstCard) {//don't remove card if this is the first drawn card
                pl.removeCard(c);//removing card from hand
                gameOver = checkIfWon(pl);//checking for someone with UNO or if the game is over
            }
            players.rotate();//skip next player's turn
            if(!gameOver) System.out.println((firstCard ? pl.getName() : players.first().getName()) + " misses a turn"); //only show if game isn't over
            lastCard = c;//update last card
        } else if (c.getSymbol().equals(Symbol.REVERSE)) { //check if the card is a reverse card
            if (!firstCard) {//don't remove card if this is the first drawn card
                pl.removeCard(c);//removing card from hand
                gameOver = checkIfWon(pl);//checking for someone with UNO or if the game is over
            }
            players.reverse();//reverse game
            if(!gameOver) System.out.println("Game reverses direction");//only show if game isn't over
            lastCard = c;//update last card
        }
    }

    public boolean checkIfWon(Player activePlayer) {//checks if the player has UNO or won the game
        if (activePlayer.getHand().size() == 1) //if player has 1 card, call UNO
            System.out.println(activePlayer.getName() + " yells \"UNO!!\"");
        if (activePlayer.getHand().size() == 0) { //if player has no cards, they win
            System.out.println("GAME OVER, " + activePlayer.getName() + " WINS!!");
            return true; //end game when out of cards
        }
        return false;
    }

    //decides which colour to make the wild card
    public Colour pickWildColour(Player activePlayer) {
        int yellowCards = 0;
        int blueCards = 0;
        int redCards = 0;
        int greenCards = 0;
        Colour wildColour = Colour.BLACK; //default is black
        int mostCards;
        for (Card ca : activePlayer.getHand()) { //finding which colour is most common in the hand. This will be the chosen colour for the wild
            if (ca.getColour().equals(Colour.YELLOW)) yellowCards++;
            if (ca.getColour().equals(Colour.RED)) redCards++;
            if (ca.getColour().equals(Colour.GREEN)) greenCards++;
            if (ca.getColour().equals(Colour.BLUE)) blueCards++;
            mostCards = yellowCards;
            wildColour = Colour.YELLOW;
            if (mostCards < redCards) {//finding which count is the highest
                mostCards = redCards;
                wildColour = Colour.RED;
            }
            if (mostCards < greenCards) {
                mostCards = greenCards;
                wildColour = Colour.GREEN;
            }
            if (mostCards < blueCards) {
                mostCards = blueCards;
                wildColour = Colour.BLUE;
            }
        }
        return wildColour;//return the most common colour
    }
}

