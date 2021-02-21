import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * This class implements a player in the game
 *
 * @author Mohammadhossein Naderi
 * @version 1.0
 * @since 4/3/2018
 */
public class Player {

    /**
     * name of player
     */
    private String name;
    /**
     * random cards of a player at the begining of the game
     */
    private ArrayList<Card> randomCards;
    /**
     * alive cards of a player
     */
    private ArrayList<Card> cards;
    /**
     * dead cards of a player
     */
    private ArrayList<Card> deadCards;
    /**
     * opponent of player
     */
    private Player opponent;
    /**
     * This field shows that the player is CPU or real
     */
    private boolean isPlayer;
    /**
     * color of player
     */
    private String color;
    /**
     * number of used refill chances of player
     */
    private int refills;
    /**
     * this field shows if the player can do his turn or not
     */
    private boolean canMove;

    /**
     * Constructor for player
     * @param name name
     * @param isPlayer isPlayer
     */
    public Player(String name, boolean isPlayer) {
        this.name = name;
        this.isPlayer = isPlayer;
        cards = new ArrayList<Card>();
        randomCards = new ArrayList<Card>();
        deadCards = new ArrayList<Card>();
        refills = 0;
        canMove = true;
    }

    /**
     * override of toString method
     * @return string
     */
    @Override
    public String toString() {
        return name + ": " + cards + " --> " + valueOfAPlayer();
    }

    /**
     * getter method for name field
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * getter method for refills field
     * @return refills
     */
    public int getRefills() {
        return refills;
    }

    /**
     * setter method for opponent field
     * @param opponent opponent
     */
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    /**
     * getter method for cards field
     * @return cards
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * getter method for deadCards field
     * @return deadCards
     */
    public ArrayList<Card> getDeadCards() {
        return deadCards;
    }

    /**
     * getter method for color field
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * getter method for randomCards field
     * @return randomCards
     */
    public ArrayList<Card> getRandomCards() {
        return randomCards;
    }

    /**
     * getter method for opponent field
     * @return opponent
     */
    public Player getOpponent() {
        return opponent;
    }

    /**
     * getter method for isPlayer field
     * @return isPlayer
     */
    public boolean isPlayer() {
        return isPlayer;
    }

    /**
     * setter method for color field
     * @param color color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * This method adds a card to cards of a player
     * @param card card
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * this method removes a card from cards of a player
     * @param card card
     */
    public void removeCard(Card card) {
        cards.remove(card);
    }

    /**
     * getter method for isAlive field
     * @return isAlive
     */
    public boolean isAlive() {
        return !cards.isEmpty() && canMove;
    }

    /**
     * this method prints all cards of a player in a row
     */
    public void printCards() {
        ArrayList<Card> allCards = new ArrayList<Card>(cards);
        allCards.addAll(deadCards);
        Card.print(this, true);
    }

    /**
     * This method gives some random cards to a player and the player can choose between them
     */
    public void drawCards() {
        for (int i = 0; i < Game.NUMBER_OF_RANDOM_CARDS; i++) {
            randomCards.add(Card.randomCard());
        }
        Card.print(this, false);
        for (Animal.Type type : Animal.Type.values()) {
            System.out.print(type.toString().toUpperCase() + ": ");
            for (Card card : randomCards) {
                if (card.getAnimal().getType().equals(type)) {
                    System.out.print(randomCards.indexOf(card) + 1 + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
        cards = selectCards();
        Card.print(this, true);

    }

    /**
     * This method chooses CPU's cards intelligently
     */
    public void drawCPUCards() {
        for (int i = 0; i < Game.NUMBER_OF_RANDOM_CARDS; i++) {
            randomCards.add(Card.randomCard());
        }
        Collections.sort(randomCards);
        Card.print(this, false);
        for (int i = 0; i < randomCards.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (randomCards.get(j).valueOfACard() < randomCards.get(i).valueOfACard()) {
                    Card temp = randomCards.get(i);
                    randomCards.set(i, randomCards.get(j));
                    randomCards.set(j, temp);
                }
            }
        }

        HashMap<Animal.Type, Integer> repeats = new HashMap<Animal.Type, Integer>();
        for (Animal.Type type : Animal.Type.values()) {
            repeats.put(type, 0);
        }
        int index = 0;
        while (cards.size() < Game.NUMBER_OF_CARDS) {
            if (repeats.get(randomCards.get(index).getAnimal().getType()) < 5) {
                repeats.put(randomCards.get(index).getAnimal().getType(), repeats.get(randomCards.get(index).getAnimal().getType()) + 1);
                cards.add(randomCards.get(index));
            }
            index++;
        }
        Collections.sort(cards);
    }

    /**
     * in this method, a player selects his cards
     * @return cards
     */
    private ArrayList<Card> selectCards() {
        ArrayList<Card> selectedCards = new ArrayList<Card>();
        System.out.println("Select " + Game.NUMBER_OF_CARDS + " cards.(enter number of cards you want, seperated by comma)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        input = input.replaceAll(" ", "");
        StringTokenizer tokenizer = new StringTokenizer(input, ",");
        boolean check = false;
        if (tokenizer.countTokens() != Game.NUMBER_OF_CARDS) {
            System.err.println("Please enter " + Game.NUMBER_OF_CARDS + " numbers");
            return selectCards();
        }
        HashSet<Integer> cardNumbers = new HashSet<Integer>();
        while (tokenizer.hasMoreTokens()) {
            int cardNumber = -1;
            try {
                cardNumber = Integer.parseInt(tokenizer.nextToken());
                if (cardNumbers.contains(cardNumber)) {
                    System.err.println("You have entered duplicate numbers");
                    return selectCards();
                }
                if (cardNumber <= randomCards.size() && cardNumber > 0) {
                    selectedCards.add(randomCards.get(cardNumber - 1));
                    cardNumbers.add(cardNumber);
                } else {
                    System.err.println("Numbers should be between 1 and " + Game.NUMBER_OF_RANDOM_CARDS);
                    return selectCards();
                }
            } catch (NumberFormatException e) {
                System.err.println("Please enter input in the correct format");
                return selectCards();
            }
        }
        HashMap<Animal.Type, Integer> repeats = new HashMap<Animal.Type, Integer>();
        for (Animal.Type type : Animal.Type.values()) {
            repeats.put(type, 0);
        }
        for (Card card : selectedCards) {
            repeats.put(card.getAnimal().getType(), repeats.get(card.getAnimal().getType()) + 1);
        }
        for (Animal.Type type : Animal.Type.values()) {
            if (repeats.get(type) > Game.MAXIMUM_NUMBER_OF_CARDS_FROM_A_TYPE) {
                System.err.println("You can't select more than " + Game.MAXIMUM_NUMBER_OF_CARDS_FROM_A_TYPE + " cards from a particular type");
                return selectCards();
            }
        }
        return selectedCards;
    }

    /**
     * This method implements an attack move in the game
     * @param invaders invaders
     * @param prey prey
     * @param style attacking action
     * @param isPrimitive this boolean shows that the attack will be undone or not
     * @return returns true if it was successful
     */
    private boolean groupAttack(HashSet<Card> invaders, Card prey, Animal.AttackingStyle style, boolean isPrimitive) {
        HashSet<Animal> animals = new HashSet<Animal>();
        for (Card card : invaders) {
            animals.add(card.getAnimal());
        }
        boolean result = Animal.groupAttack(animals, prey.getAnimal(), style, isPrimitive);
        if (!prey.getAnimal().isAlive() && !isPrimitive) {
            opponent.deadCards.add(prey);
            opponent.cards.remove(prey);
        }
        return result;
    }

    /**
     * This method undoes an attack. it is used for CPU to choose his move on his turn
     * @param invaders invaders
     * @param prey prey
     * @param style attacking action
     */
    private void undoGroupAttack(HashSet<Card> invaders, Card prey, Animal.AttackingStyle style) {
        int size = invaders.size();
        int totalEnergy = 0;
        for (Card card : invaders) {
            totalEnergy += card.getAnimal().getAttackingStyles().get(style);
        }
        for (Card card : invaders) {
            card.getAnimal().setEnergy(card.getAnimal().getEnergy() + totalEnergy / size);
        }
        prey.getAnimal().setHealth(prey.getAnimal().getHealth() + totalEnergy);
    }

    /**
     * This method refills a card's energy
     * @param cardNumber card number
     * @return returns true if it was successful
     */
    private boolean refillCard(int cardNumber) {
        return cards.get(cardNumber - 1).refillCard();
    }

    /**
     * This method gets a card number from the user
     * @return card number
     */
    private int getCardNumberFromUser() {
        System.out.println("Enter number of card");
        int cardNumber = -1;
        try {
            cardNumber = Integer.parseInt(new Scanner(System.in).next());
            if (cardNumber < 1 || cardNumber > cards.size()) {
                System.err.println("Number should be between 1 and " + cards.size());
                return getCardNumberFromUser();
            }
        } catch (NumberFormatException e) {
            System.err.println("Please enter a number");
            return getCardNumberFromUser();
        }
        return cardNumber;
    }

    /**
     * This method checks if there is a possible move for a player to do on his turn
     * @return result
     */
    public boolean ifCanMove() {
        String[] set = new String[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            set[i] = "" + (i + 1);
        }
        HashMap<AttackingOption, Integer> allOptions = generateAllSubsets();
        if (allOptions.isEmpty()) {
            System.err.println("No possible move for " + name);
            canMove = false;
        }
        return !allOptions.isEmpty();
    }

    /**
     * In this method, CPU chooses his move intelligently. in fact it analyzes all the possible ways and chooses the best one
     */
    public void cpuIntelligentTurn() {
        if (!ifCanMove()) {
            return;
        }
        HashMap<AttackingOption, Integer> allOptions = generateAllSubsets();
        AttackingOption bestOption = null;
        int max = Integer.MIN_VALUE;
        for (AttackingOption option : allOptions.keySet()) {
            if (allOptions.get(option) > max) {
                max = allOptions.get(option);
                bestOption = option;
            }
        }

        if (bestOption.isAttack()) {
            groupAttack(bestOption.getInvaders(), bestOption.getPrey(), bestOption.getStyle(), false);
        } else {
            refillCard(bestOption.getRefillCardNumber());
            refills++;
        }
    }

    /**
     * This method generates all possible moves for a player and assigns a value to each of them and returns it
     * @return result
     */
    private HashMap<AttackingOption, Integer> generateAllSubsets() {
        String[] set = new String[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            set[i] = "" + (i + 1);
        }
        HashMap<AttackingOption, Integer> values = new HashMap<AttackingOption, Integer>();
        int n = set.length;
        int re = 0;
        for (int i = 0; i < (1 << n); i++) {
            HashSet<String> subset = new HashSet<String>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    subset.add(set[j]);
                }
            }
            // subset is ready
            re++;
            if (subset.size() == 0) {
                continue;
            }
            HashSet<Animal.AttackingStyle> commonStyles = checkValidityOfASubset(subset);
            if (commonStyles.size() == 0) {
                continue;
            }
            HashSet<Card> invaders = new HashSet<Card>();
            for (String number : subset) {
                invaders.add(cards.get(Integer.parseInt(number) - 1));
            }
            for (Animal.AttackingStyle style : commonStyles) {
                for (Card prey : opponent.cards) {
                    AttackingOption option = new AttackingOption(true, invaders, prey, style, 0);
                    int oldVal = valueOfAState();
                    boolean isPossible = groupAttack(invaders, prey, style, true);
                    if (isPossible) {
                        int gain = valueOfAState() - oldVal;
                        values.put(option, gain);
                        //System.out.println(option + " --> " + gain);
                        undoGroupAttack(invaders, prey, style);
                    } else continue;
                }
            }

        }

        if (refills < Game.MAXIMUM_NUMBER_OF_REFILLS) {
            for (Card card : cards) {
                AttackingOption option = new AttackingOption(false, null, null, null, cards.indexOf(card) + 1);
                int diff = card.getAnimal().getDefaultEnergy() - card.getAnimal().getEnergy();
                int oldVal = valueOfAPlayer();
                refillCard(cards.indexOf(card) + 1);
                values.put(option, valueOfAPlayer() - oldVal);
                cards.get(cards.indexOf(card)).getAnimal().setEnergy(cards.get(cards.indexOf(card)).getAnimal().getEnergy() - diff);
            }
        }
        return values;
    }

    /**
     * This method checks if a subset of cards can attack in a cooperation
     * @param subset subset
     * @return result
     */
    private HashSet<Animal.AttackingStyle> checkValidityOfASubset(HashSet<String> subset) {
        HashSet<Animal.AttackingStyle> allStyles = new HashSet<Animal.AttackingStyle>();
        for (Animal.AttackingStyle style : Animal.AttackingStyle.values()) {
            allStyles.add(style);
        }
        for (String cardNumber : subset) {
            Card card = cards.get(Integer.parseInt(cardNumber) - 1);
            HashSet<Animal.AttackingStyle> thisCardStyles = new HashSet<Animal.AttackingStyle>();
            for (Animal.AttackingStyle style : card.getAnimal().getAttackingStyles().keySet()) {
                thisCardStyles.add(style);
            }
            allStyles.retainAll(thisCardStyles);
        }
        return allStyles;
    }

    /**
     * This method assigns a value to a player according to it's state
     * @return
     */
    private int valueOfAPlayer() {
        int value = 0;
        for (Card card : cards) {
            value += card.valueOfACard();
        }
        return value;
    }

    /**
     * This method assigns a value to a state in the game by using value of each player
     * @return result
     */
    private int valueOfAState() {
        return valueOfAPlayer() - opponent.valueOfAPlayer();
    }


    /**
     * In this method a non-CPU player does his turn
     */
    public void doTurn() {
        Collections.sort(cards);
        boolean flag = true;
        String turnMode;
        do {
            System.out.println("Enter 'A' to attack or 'R' to refill one of your cards' energy");
            turnMode = new Scanner(System.in).next();
            turnMode = turnMode.toUpperCase();
            if (turnMode.equals("A") || turnMode.equals("R")) {
                flag = false;
            }
        } while (flag);

        switch (turnMode) {
            case "A": {
                HashSet<Animal.AttackingStyle> styles = new HashSet<Animal.AttackingStyle>();
                HashSet<Card> cardNumbers = new HashSet<Card>();
                if (cards.size() == 1) {
                    cardNumbers.add(cards.get(0));
                    for (Animal.AttackingStyle style : cards.get(0).getAnimal().getAttackingStyles().keySet()) {
                        styles.add(style);
                    }
                    System.out.println("The invader was automatically set to card NO.1");
                } else {
                    boolean isValid = false;
                    validityCheck:
                    while (!isValid) {
                        System.out.println("Select invader(s):");
                        System.out.println("Enter number of card(s) seperated by comma");
                        HashSet<Card> invaders = new HashSet<Card>();
                        Scanner scanner = new Scanner(System.in);
                        String input = scanner.nextLine();
                        input = input.replaceAll(" ", "");
                        StringTokenizer tokenizer = new StringTokenizer(input, ",");

                        if (tokenizer.countTokens() < 1 || tokenizer.countTokens() > cards.size()) {
                            System.out.println("Please enter information in the correct format");
                            continue;
                        }
                        while (tokenizer.hasMoreTokens()) {
                            int cardNumber = -1;
                            try {
                                cardNumber = Integer.parseInt(tokenizer.nextToken());
                                if (cardNumber < 1 || cardNumber > cards.size()) {
                                    System.err.println("Numbers should be between 1 and " + cards.size());
                                    cardNumbers.clear();
                                    continue validityCheck;
                                }
                                if (cardNumbers.contains(cards.get(cardNumber - 1))) {
                                    System.err.println("You have entered duplicate numbers");
                                    cardNumbers.clear();
                                    continue validityCheck;
                                }
                                cardNumbers.add(cards.get(cardNumber - 1));
                            } catch (NumberFormatException e) {
                                System.err.println("Please enter information in the correct format");
                                cardNumbers.clear();
                                continue validityCheck;
                            }
                        }
                        for (Animal.AttackingStyle style : Animal.AttackingStyle.values()) {
                            styles.add(style);
                        }
                        for (Card card : cardNumbers) {
                            HashSet<Animal.AttackingStyle> stylesOfThisCard = new HashSet<Animal.AttackingStyle>();
                            for (Animal.AttackingStyle style : card.getAnimal().getAttackingStyles().keySet()) {
                                stylesOfThisCard.add(style);
                            }
                            styles.retainAll(stylesOfThisCard);
                        }
                        if (styles.size() == 0) {
                            System.err.println("Selected cards should have a same action");
                            styles.clear();
                            cardNumbers.clear();
                            continue validityCheck;
                        }
                        isValid = true;
                    }
                }
                Animal.AttackingStyle style = null;
                if (styles.size() == 1) {
                    for (Animal.AttackingStyle attackingStyle : styles) {
                        style = attackingStyle;
                    }
                    System.out.println("The action was automatically set to '" + Card.makeStandard(style.toString()) + "'");
                } else {
                    HashMap<Integer, Animal.AttackingStyle> attackingStyles = new HashMap<Integer, Animal.AttackingStyle>();
                    int counter = 1;
                    System.out.println("Select one of the following actions by entering it's number:");
                    for (Animal.AttackingStyle attackingStyle : styles) {
                        System.out.println(counter + ". " + Card.makeStandard(attackingStyle.toString()));
                        attackingStyles.put(counter, attackingStyle);
                        counter++;
                    }
                    boolean isOk = false;
                    while (!isOk) {
                        String input = new Scanner(System.in).nextLine();
                        input = input.replaceAll(" ", "");
                        int number = -1;
                        try {
                            number = Integer.parseInt(input);
                            if (number < 1 || number > styles.size()) {
                                System.err.println("Please enter one of the numbers");
                                continue;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Please enter a number");
                            continue;
                        }
                        isOk = true;
                        style = attackingStyles.get(number);
                    }

                }
                Card prey;
                if (opponent.cards.size() > 1) {
                    prey = opponent.cards.get(opponent.getCardNumberFromUser() - 1);
                } else {
                    prey = opponent.cards.get(0);
                    System.out.println("The prey was automatically set to card NO.1");
                }

                if (!groupAttack(cardNumbers, prey, style, false)) {
                    System.err.println("Not possible");
                    doTurn();
                }
                break;
            }

            case "R": {
                if (refills < Game.MAXIMUM_NUMBER_OF_REFILLS) {
                    int cardNumber;
                    if (cards.size() > 1) {
                        cardNumber = getCardNumberFromUser();
                    } else {
                        cardNumber = 1;
                        System.out.println("Card No.1 was automatically selected");
                    }
                    boolean result = refillCard(cardNumber);
                    if (!result) {
                        System.err.println("Energy of card NO." + cardNumber + " is already full");
                        doTurn();
                    } else {
                        refills++;
                    }
                } else {
                    System.err.println("You have used all your refill chances");
                    doTurn();
                }
                break;
            }
        }
    }

}
