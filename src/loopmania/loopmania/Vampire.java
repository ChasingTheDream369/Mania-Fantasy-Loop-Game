package unsw.loopmania;

public class Vampire extends Enemy implements EnemyStrategy{
    
    private int criticalBite ;
    private int criticalEndAttack;
    private static final int initialDamage = 5;
    private static final int speed = 8;
    private GainsOnDefeat gains;
    private boolean reverseDirection = false;
    private boolean reversed = false;

    public Vampire(PathPosition position){
        super(position);
        this.criticalEndAttack = -1;
        this.criticalBite = -1;
        setSpeed(speed);
        setType("Vampire");
        setHealth(30);
        setDamage(9);
        setBattleRadius(1.7);
        setSupportRadius(3);
        setExperience(9);
        int rand = (int)Math.floor(Math.random()*(5-0+0)+0);
        int rand1 = (int)Math.floor(Math.random()*(6-0+0)+0);
        this.gains = new GainsOnDefeat(9, 7, items[rand], cards[rand1]);
    }

    /**
     * Retuns the enemy attack to the player given that the enemy is not 
     * dead.
     */
    public void enemyAttack(Players player){
        updateAttackNum();
        attackCrticalCheck(currentAttackNum);
        if (this.getHealth() > 0) {
            attack(player);
        }
    }

    /**
     * 
     */
    public GainsOnDefeat getGainsOnDefeat(){
        return gains;
    }


    /**
     * Checks the the attack number and then finds if it is the 
     * critical attack and if it si then updated the damage.
     */
    private void attackCrticalCheck(int currentAttackNum){
        
        if(currentAttackNum == criticalBite){
            int max = currentAttackNum + 9;
            criticalEndAttack = (int)Math.floor(Math.random()*(max-currentAttackNum+1)+currentAttackNum);
        }
        
        if(currentAttackNum >= criticalBite && currentAttackNum <= criticalEndAttack){
            updateDamage();
        }
    }

    /**
     * Updates the damage the vampire.
     */
    public void updateDamage(){
        double newDamage = initialDamage + (int)Math.floor(Math.random()*(9-3+1)+3);
        setDamage(newDamage);
    }

    /**Update the crirtcal bite(is alled before each battle is run so that it is a 
     * random value each time ) */
    public void updateCrticalBite(double criticalAdd) {
        this.criticalBite = (int) Math.ceil(this.criticalBite + criticalAdd*this.criticalBite);
    }

    /**
     * sets the critical bite to a random value.
     */
    public void setBattleCritialBite() {
        this.criticalBite =  (int) Math.floor(Math.random()*(9-3+1)+3);

    }

    /**
     * Gets the critial bite for vampire
     * @return
     */
    public int getCriticalBite() {
        return criticalBite;
    }

    /**
     * Reverses the direction of the motion of the vampire.
     */
    public void reverse() {
        if (!reverseDirection) {
            reverseDirection = true;
        } else {
            reverseDirection = false;
        }
    }

    /**
     * 
     * @param cb
     */
    public void changeDirection(CampfireBuilding cb) {
        if (Math.pow((this.getX() - cb.getX()), 2) +  Math.pow((this.getY() - cb.getY()), 2) < Math.pow(cb.getSupportRadius(), 2)) {
            if (!reversed) {
                reverse();
                reversed = true;
            }
        } else if (reversed) {
            reversed = false;
        }
    }

    /**
     * Gets the reversed direction(if possible for the vampire)
     * @return
     */
    public boolean getReversed() {
        return reversed;
    }

    public boolean getReverseDirection() {
        return reverseDirection;
    }

    /** Moves the character in a particular direction. */
    @Override
    public void move(){
        if (!reverseDirection) {
            moveUpPath();
        } else {
            moveDownPath();
        }
    }

}
