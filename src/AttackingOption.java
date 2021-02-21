/**
 * This class implements an attacking option of a player on his turn
 *
 * @author Mohammadhossein Naderi
 * @version 1.0
 * @since 4/3/2018
 */

import java.util.HashSet;

public class AttackingOption {

    /**
     * This field shows that the option is an attack move or a refill move
     */
    private boolean isAttack;
    /**
     * Set of invaders
     */
    private HashSet<Card> invaders;
    /**
     * Prey card
     */
    private Card prey;
    /**
     * Attacking style
     */
    private Animal.AttackingStyle style;
    /**
     * The refill card
     */
    private int refillCardNumber;

    /**
     * Constructor for attacking option
     * @param isAttack isAttack
     * @param invaders invaders
     * @param prey prey
     * @param style style
     * @param refillCardNumber refillCardNumber
     */
    public AttackingOption(boolean isAttack, HashSet<Card> invaders, Card prey, Animal.AttackingStyle style, int refillCardNumber) {
        this.isAttack = isAttack;
        if (isAttack) {
            this.invaders = new HashSet<Card>(invaders);
            this.style = style;
            this.prey = prey;
        } else {
            this.refillCardNumber = refillCardNumber;
        }
    }

    /**
     * getter method for isAttack field
     * @return isAttack
     */
    public boolean isAttack() {
        return isAttack;
    }

    /**
     * getter method for invaders field
     * @return invaders
     */
    public HashSet<Card> getInvaders() {
        return invaders;
    }

    /**
     * getter method for prey field
     * @return prey
     */
    public Card getPrey() {
        return prey;
    }

    /**
     * getter method for style field
     * @return style
     */
    public Animal.AttackingStyle getStyle() {
        return style;
    }

    /**
     * getter method for refillCardNumber field
     * @return refillCardNumber
     */
    public int getRefillCardNumber() {
        return refillCardNumber;
    }

    /**
     * override of toString method
     * @return string
     */
    @Override
    public String toString() {
        return "Invaders: " + invaders.toString() + ", Prey: " + prey.toString() + ", Style: " + style.toString();
    }
}
