import java.util.Scanner;

/**
 * This class implements the game and connects all classes to Main class
 *
 * @author Mohammadhossein Naderi
 * @version 1.0
 * @since 4/3/2018
 */
public class Game {

    /**
     * first and second player of game
     */
    private Player player2;
    /**
     * the player who should do his turn now and the opponent player of him
     */
    private Player turn, opp;
    /**
     * number of random cards
     */
    public static final int NUMBER_OF_RANDOM_CARDS = 30;
    /**
     * number of cards
     */
    public static final int NUMBER_OF_CARDS = 10;
    /**
     * maximum allowable number of cards from a particular type
     */
    public static final int MAXIMUM_NUMBER_OF_CARDS_FROM_A_TYPE = 5;
    /**
     * number of refills
     */
    public static final int MAXIMUM_NUMBER_OF_REFILLS = 3;

    public Game() {

    }

    /**
     * this method clears screen by entering some new lines
     */
    private void clearScreen() {
        for (int i = 0; i < 40; i++) {
            System.out.println();
        }
    }

    /**
     * this method checks if the game is over
     * @return result
     */
    private boolean gameIsOver() {
        if (turn.isAlive() && opp.isAlive()) {
            return false;
        }
        return true;
    }

    /**
     * this method changes the turn
     */
    private void changeTurn() {
        Player temp = turn;
        turn = opp;
        opp = temp;
    }

    /**
     * this method initializes players
     */
    public void initializePlayers() {

        boolean flag = true;
        String gameMode;
        do {
            System.out.println("Enter 'F' to play with your friend and 'C' to play against CPU:)");
            gameMode = new Scanner(System.in).next();
            gameMode = gameMode.toUpperCase();
            if (gameMode.equals("F") || gameMode.equals("C")) {
                flag = false;
            }
        } while (flag);

        System.out.println("Enter name of first player:");
        Player player1 = new Player(new Scanner(System.in).nextLine(), true);
        player1.setColor(ConsoleColors.BLUE_BOLD);
        player1.drawCards();

        if (gameMode.equals("F")) {
            System.out.println("Enter name of second player:");
            player2 = new Player(new Scanner(System.in).nextLine(), true);
            player2.setColor(ConsoleColors.CYAN_BOLD);
            player2.drawCards();
        }
        if (gameMode.equals("C")) {
            player2 = new Player("CPU1", false);
            player2.setColor(ConsoleColors.CYAN_BOLD);
            player2.drawCPUCards();
        }

        player1.setOpponent(player2);
        player2.setOpponent(player1);

        turn = player1;
        opp = player2;

        clearScreen();
    }

    /**
     * by calling this method, the game begins
     */
    public void kickoff() {
        if (turn.isPlayer() && opp.isPlayer()) {
            clearScreen();
            while (!gameIsOver()) {
                turn.printCards();
                opp.printCards();
                System.out.println("Turn of " + turn.getName());
                turn.doTurn();
                changeTurn();
                if (!turn.ifCanMove()) {
                    continue;
                }
            }
        }

        if (turn.isPlayer() && !opp.isPlayer()) {
            clearScreen();
            turn.printCards();
            opp.printCards();
            while (!gameIsOver()) {
                if (turn.isPlayer()) {
                    System.out.println("Turn of " + turn.getName());
                    turn.doTurn();
                    turn.printCards();
                    opp.printCards();
                } else {
                    System.out.println("Turn of " + turn.getName());
                    System.out.println("Press 'Enter' to see " + turn.getName() + "'s turn");
                    try {
                        System.in.read();
                    } catch (Exception e) {
                    }
                    turn.cpuIntelligentTurn();
                    opp.printCards();
                    turn.printCards();
                }
                changeTurn();
                if (!turn.ifCanMove()) {
                    continue;
                }
            }
        }

        System.out.println("WINNER: " + winner().getName());
    }

    /**
     * this method returns the winner of the game
     * @return winner
     */
    private Player winner() {
        if (turn.isAlive() && !opp.isAlive()) {
            return turn;
        }
        if (!turn.isAlive() && opp.isAlive()) {
            return opp;
        }
        return null;
    }

}
