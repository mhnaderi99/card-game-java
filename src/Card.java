import java.util.ArrayList;
import java.util.Collections;

/**
 * This method implements a card in the game
 *
 * @author Mohammadhossein Naderi
 * @version 1.0
 * @since 4/3/2018
 */
public class Card implements Comparable<Card> {

    /**
     * animal of card
     */
    private Animal animal;

    /**
     * Constructor for card, given an animal type
     * @param type type of animal
     */
    public Card(Animal.Type type) {
        animal = new Animal(type);
    }

    /**
     * Constructor for card, given an animal
     * @param animal animal
     */
    public Card(Animal animal) {
        this.animal = animal;
    }

    /**
     * getter method for animal field
     * @return
     */
    public Animal getAnimal() {
        return animal;
    }

    /**
     * This method recognizes if the card is alive
     * @return returns true if it's alive
     */
    public boolean isAlive() {
        return animal.isAlive();
    }

    /**
     * This method creates a random card and returns it
     * @return random card
     */
    public static Card randomCard() {
        return new Card(Animal.randomAnimal());
    }

    /**
     * This method refills a card's energy
     * @return returns true if it was successful
     */
    public boolean refillCard() {
        return animal.refillEnergy();
    }

    /**
     * Override of compareTo method
     * @param o card
     * @return result
     */
    @Override
    public int compareTo(Card o) {
        return animal.getType().compareTo(o.animal.getType());
    }

    /**
     * This method assigns a value to a card according to it's state
     * @return
     */
    public int valueOfACard() {
        if (!isAlive()) {
            return 0;
        }
        double value = (double) animal.getEnergy() + (double) animal.getDefaultEnergy() + (double) animal.getHealth();
        for (Animal.AttackingStyle style : animal.getAttackingStyles().keySet()) {
            value *= ((double) animal.getAttackingStyles().get(style) / 100.0);
        }
        return (int) value;
    }

    /**
     * This method prints all cards of a player in a row.
     * @param player player
     * @param mode mode of printing
     */
    public static void print(Player player, boolean mode) {
        int width = 18, height = 14, titleHeight = 1, stylesHeight = 5, maxStyles = 2, healthHeight = height - 4, energyHeight = height - 3, indexHeight = height, gapWidth = 1;
        ArrayList<Card> deadCards, cards;
        if (mode) {
            deadCards = player.getDeadCards();
            cards = new ArrayList<Card>(player.getCards());
            cards.addAll(deadCards);
            printAtCenter("CARDS OF " + player.getName().toUpperCase() + " (REFILLS: " + (Game.MAXIMUM_NUMBER_OF_REFILLS - player.getRefills()) + ")", (cards.size() - player.getDeadCards().size()) * (gapWidth + width), player.getColor());
            if (deadCards.size() != 0) {
                printAtCenter("DEAD CARDS", (player.getDeadCards()).size() * (gapWidth + width), ConsoleColors.RED_BOLD);
            }
            System.out.println();
        } else {
            cards = player.getRandomCards();
            deadCards = new ArrayList<Card>();
        }
        String cardColor = player.getColor();
        String reset = ConsoleColors.RESET;
        int size = cards.size();
        String deadColor = ConsoleColors.RED_BOLD, resetColor = cardColor;
        int size1 = cards.size() - deadCards.size();
        Collections.sort(cards.subList(0, size1));
        System.out.print(cardColor);
        for (int i = 0; i < size; i++) {
            if (i >= size1) {
                System.out.print(deadColor);
            }
            System.out.print("╔");
            repeatedPrint("─", width - 2);
            System.out.print("╗");
            repeatedPrint(" ", gapWidth);
        }

        System.out.println(resetColor);

        for (int i = 1; i <= height; i++) {
            for (int j = 0; j < size; j++) {
                if (j >= size1) {
                    cardColor = deadColor;
                } else {
                    cardColor = resetColor;
                }
                System.out.print(cardColor + "│" + reset);
                if (i == titleHeight) {
                    printAtCenter(cards.get(j).animal.getType().toString().toUpperCase(), width - 2, cardColor);
                } else if (i == titleHeight + 1) {
                    repeatedPrint(cardColor + "─" + reset, width - 2);
                } else if (i == stylesHeight - 2) {
                    printAtCenter("ACTIONS:", width - 2, ConsoleColors.RESET);

                } else if (i - stylesHeight < cards.get(j).animal.getAttackingStyles().size() && i - stylesHeight >= 0) {
                    String styles = "";
                    int counter = 0;
                    for (Animal.AttackingStyle style : cards.get(j).animal.getAttackingStyles().keySet()) {
                        if (counter == i - stylesHeight) {
                            styles = makeStandard(style.toString()) + " = " + cards.get(j).animal.getAttackingStyles().get(style).toString();
                            break;
                        }
                        counter++;
                    }
                    printAtCenter(styles, width - 2, ConsoleColors.RESET);
                } else if (i == stylesHeight + maxStyles) {
                    repeatedPrint(cardColor + "_" + reset, width - 2);
                } else if (i == healthHeight - 2) {
                    printAtCenter("STATE:", width - 2, ConsoleColors.RESET);
                } else if (i == healthHeight) {
                    String color = setColor(cards.get(j).animal.getHealth(), cards.get(j).animal.getDefaultHealth());
                    printAtCenter("Health:" + cards.get(j).animal.getHealth() + "/" + cards.get(j).animal.getDefaultHealth(), width - 2, color);
                } else if (i == energyHeight) {
                    String color = setColor(cards.get(j).animal.getEnergy(), cards.get(j).animal.getDefaultEnergy());
                    printAtCenter("Energy:" + cards.get(j).animal.getEnergy() + "/" + cards.get(j).animal.getDefaultEnergy(), width - 2, color);
                } else if (i == indexHeight - 1) {
                    repeatedPrint(cardColor + "─" + reset, width - 2);
                } else if (i == indexHeight) {
                    String index = "" + (j + 1);
                    String color = cardColor;
                    if (deadCards.contains(cards.get(j))) {
                        index = "-";
                    }
                    printAtCenter(index, width - 2, cardColor);

                } else {
                    repeatedPrint(" ", width - 2);
                }
                System.out.print(cardColor + "│" + reset);
                repeatedPrint(" ", gapWidth);
            }
            System.out.println();
        }

        System.out.print(resetColor);

        for (int i = 0; i < size; i++) {
            if (i >= size1) {
                System.out.print(deadColor);
            }
            System.out.print("╚");
            repeatedPrint("─", width - 2);
            System.out.print("╝");
            repeatedPrint(" ", gapWidth);
        }
        System.out.println("\n" + reset);
    }

    /**
     * This method prints a string repeatedly
     * @param string string
     * @param repeat number of repeats
     */
    private static void repeatedPrint(String string, int repeat) {
        for (int i = 0; i < repeat; i++) {
            System.out.print(string);
        }
    }

    /**
     * This method sets color of printing state of a card
     * @param value value of card's state
     * @param defaultValue maximum value of state
     * @return color
     */
    private static String setColor(int value, int defaultValue) {
        if (value == 0) {
            return ConsoleColors.RED_BOLD;
        }
        if (value == defaultValue) {
            return ConsoleColors.GREEN_BOLD;
        }
        if (value <= defaultValue / 3) {
            return ConsoleColors.RED_BRIGHT;
        }
        if (value >= 2 * defaultValue / 3) {
            return ConsoleColors.GREEN_BOLD_BRIGHT;
        }

        return ConsoleColors.YELLOW_BOLD;
    }

    /**
     * This method makes a string standard to print
     * @param string string
     * @return standard string
     */
    public static String makeStandard(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1, string.length());
    }

    /**
     * This method prints a string in the center of an area given the width
     * @param string string
     * @param width width
     * @param color color
     */
    private static void printAtCenter(String string, int width, String color) {
        if (string.length() > width) {
            return;
        }
        string = string.replaceAll("\\{", "");
        string = string.replaceAll("\\}", "");
        string = string.replaceAll(",", "");
        int length = string.length();
        if ((width - length) % 2 == 1) {
            int spaceIndex = string.indexOf(" ");
            if (spaceIndex == -1) {
                string = string + " ";
            } else {
                String newString = string.substring(0, spaceIndex + 1) + " " + string.substring(spaceIndex + 1, string.length());
                string = newString;
            }
        }
        repeatedPrint(" ", (width - length) / 2);
        System.out.print(color + string + ConsoleColors.RESET);
        repeatedPrint(" ", (width - length) / 2);
    }

    /**
     * Override of toString method
     * @return string
     */
    @Override
    public String toString() {
        return animal.toString();
    }
}
