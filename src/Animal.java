import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * This class implements an animal in the game.
 *
 * @author Mohammadhossein Naderi
 * @version 1.0
 * @since 4/3/2018
 */
public class Animal {

    /**
     * This enum specifies type of an animal.
     * Using this, prevents creating many classes in this project
     */
    public static enum Type {
        lion, bear, tiger, vulture, fox, elephant, wolf, hog, hippo, cow, rabbit, turtle
    }

    /**
     * This enum specifies attacking actions of animals
     */
    public static enum AttackingStyle {
        wound, kill, injure, attack, bite
    }

    /**
     * Type of animal
     */
    private Type type;
    /**
     * Maximum energy of animal
     */
    private int defaultEnergy;
    /**
     * Maximum health of animal
     */
    private int defaultHealth;
    /**
     * Health of animal
     */
    private int health;
    /**
     * Energy of animal
     */
    private int energy;
    /**
     * This field shows that the animal is alive or not
     */
    private boolean isAlive;
    /**
     * This hashmap includes attacking actions of an animal and damage of each action
     */
    private HashMap<AttackingStyle, Integer> attackingStyles;

    /**
     * Constructor for animal
     * @param type type of animal
     */
    public Animal(Type type) {
        this.type = type;
        attackingStyles = new HashMap<AttackingStyle, Integer>();
        initializeDefaults();
        health = defaultHealth;
        energy = defaultEnergy;
        isAlive = true;
    }

    /**
     * getter method for type field
     * @return
     */
    public Type getType() {
        return type;
    }

    /**
     * getter method for defaultHealth field
     * @return defaultHealth
     */
    public int getDefaultHealth() {
        return defaultHealth;
    }

    /**
     * getter method for defaultEnergy field
     * @return defaultEnergy
     */
    public int getDefaultEnergy() {
        return defaultEnergy;
    }

    /**
     * getter method for health field
     * @return health
     */
    public int getHealth() {
        return health;
    }

    /**
     * getter method for energy field
     * @return energy
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * getter method for isAlive field
     * @return returns true if isAlive
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * getter method for getAttackingStyles field
     * @return attackingStyles
     */
    public HashMap<AttackingStyle, Integer> getAttackingStyles() {
        return attackingStyles;
    }

    /**
     * setter method for energy field
     * @param energy energy of animal
     */
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    /**
     * setter method for health field
     * @param health health of animal
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * This method executes an attack move in the game
     * @param invaders array list of invaders in the attack
     * @param prey prey animal
     * @param style attacking action
     * @param isPrimitive this shows that the attack is final or will be undone later
     * @return returns true if it was successful
     */
    public static boolean groupAttack(HashSet<Animal> invaders, Animal prey, AttackingStyle style, boolean isPrimitive) {
        int totalInjure = 0, groupSize = invaders.size(), energyCost;
        for (Animal invader : invaders) {
            if (!invader.attackingStyles.containsKey(style)) {
                return false;
            }
            totalInjure += invader.attackingStyles.get(style);
        }
        energyCost = totalInjure / groupSize;
        for (Animal invader : invaders) {
            if (invader.energy < energyCost) {
                return false;
            }
        }
        for (Animal invader : invaders) {
            invader.energy -= energyCost;
        }
        prey.health -= totalInjure;
        if (prey.health <= 0 && !isPrimitive) {
            prey.health = 0;
            prey.isAlive = false;
        }
        return true;
    }

    /**
     * This method refiils animal's energy
     * @return returns true if it was successful
     */
    public boolean refillEnergy() {
        if (energy == defaultEnergy) {
            return false;
        }
        energy = defaultEnergy;
        return true;
    }

    /**
     * This method creates a random animal and returns it
     * @return random animal
     */
    public static Animal randomAnimal() {
        List<Type> values = Collections.unmodifiableList(Arrays.asList(Type.values()));
        int size = values.size();
        Random random = new Random();
        return new Animal(values.get(random.nextInt(size)));
    }

    /**
     * Override of toString method
     * @return string
     */
    @Override
    public String toString() {
        return "Type: " + type.toString();
    }

    /**
     * This method initializes information of all animal types
     */
    private void initializeDefaults() {
        switch (type) {
            case lion: {
                defaultHealth = 900;
                defaultEnergy = 1000;
                attackingStyles.put(AttackingStyle.wound, 150);
                attackingStyles.put(AttackingStyle.kill, 500);
                break;
            }
            case bear: {
                defaultHealth = 850;
                defaultEnergy = 900;
                attackingStyles.put(AttackingStyle.wound, 130);
                attackingStyles.put(AttackingStyle.kill, 600);
                break;
            }
            case tiger: {
                defaultHealth = 850;
                defaultEnergy = 850;
                attackingStyles.put(AttackingStyle.wound, 120);
                attackingStyles.put(AttackingStyle.kill, 650);
                break;
            }
            case vulture: {
                defaultHealth = 350;
                defaultEnergy = 600;
                attackingStyles.put(AttackingStyle.wound, 100);
                break;
            }
            case fox: {
                defaultHealth = 400;
                defaultEnergy = 600;
                attackingStyles.put(AttackingStyle.wound, 90);
                break;
            }
            case elephant: {
                defaultHealth = 1200;
                defaultEnergy = 500;
                attackingStyles.put(AttackingStyle.injure, 70);
                attackingStyles.put(AttackingStyle.attack, 50);
                break;
            }
            case wolf: {
                defaultHealth = 450;
                defaultEnergy = 700;
                attackingStyles.put(AttackingStyle.kill, 700);
                break;
            }
            case hog: {
                defaultHealth = 1100;
                defaultEnergy = 500;
                attackingStyles.put(AttackingStyle.injure, 80);
                break;
            }
            case hippo: {
                defaultHealth = 1000;
                defaultEnergy = 360;
                attackingStyles.put(AttackingStyle.attack, 110);
                break;
            }
            case cow: {
                defaultHealth = 750;
                defaultEnergy = 400;
                attackingStyles.put(AttackingStyle.attack, 90);
                attackingStyles.put(AttackingStyle.wound, 100);
                break;
            }
            case rabbit: {
                defaultHealth = 200;
                defaultEnergy = 350;
                attackingStyles.put(AttackingStyle.bite, 80);
                break;
            }
            case turtle: {
                defaultHealth = 350;
                defaultEnergy = 230;
                attackingStyles.put(AttackingStyle.bite, 200);
                break;
            }
        }
    }
}
