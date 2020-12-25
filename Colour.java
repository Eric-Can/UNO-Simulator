/*
 *Colour enhanced enum
 *
 * Enhanced enum with the colours that apply in a game of UNO
 *
 */
public enum Colour {
    BLUE(1),RED(2),GREEN(3),YELLOW(4),BLACK(5);
    private int number;
    private Colour (int num) {
        number = num;
    }
    public int getValue(){
        return number;
    }
}