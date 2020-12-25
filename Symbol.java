/*
 *Symbol enhanced enum
 *
 * Enhanced enum with the actions or numbers that apply in a game of UNO
 *
 */
public enum Symbol {
    ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), DRAW2(10), REVERSE(11), SKIP(12), WILD(13), WildDraw4(14);
    private int number;

    private Symbol(int num) {
        number = num;
    }

    public int getValue() {
        return number;
    }
}
