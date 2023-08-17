package unsw.loopmania;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;


/**
 * represents the main character in the backend of the game world
 */
public class Character extends Players implements CharacterStrategy {

    private static final double speed = 20;
    private final double characterDamage = 5;
    private ArrayList<Items> equippedItems;
    private ArrayList<DefenceStrategy> defenciveItemsInUse;
    private WeaponStrategy weaponItemsInUse; 
    private SimpleDoubleProperty experience;
    private int numOfCycles;
    private SimpleIntegerProperty goldItems;
    private boolean killedBosses = false;
    private DoggieCoin doggieCoin = null;
    public int currentAttackNum = 0;
    private boolean invisible;
    private boolean dragon;
    // private String[] weapons = {"Sword","Staff","Stake", };
    // private String[] defend = {"Helmet","Shield","Armour"};
    private int numBattlesWon;


    public Character(PathPosition position) {
        super(position);
        setSpeed(speed);
        setDamage(characterDamage);
        setType("Character");
        setHealth(100);
        this.experience = new SimpleDoubleProperty(0.0);
        this.equippedItems = new ArrayList<Items>();
        this.defenciveItemsInUse = new ArrayList<DefenceStrategy>();
        this.weaponItemsInUse = null;
        this.goldItems = new SimpleIntegerProperty();
        this.goldItems.set(15);
        this.numOfCycles = 0;
        this.numBattlesWon = 0;
        this.invisible = false;
        this.dragon = false;
    }

    /**
     * Changes the character into dragon
     */
    public void changeToDragon(){
        dragon = true;
    }

    
    /** 
     * checks whether the character is dragon.
     * @return boolean
     */
    public boolean isDragon(){
        return dragon;
    }

    /**
     * Changes the character back to dragon.
     */
    public void changeBack(){
        dragon = false;
    }

    
    /** 
     * @return boolean
     */
    public boolean getInvisible(){
        return invisible;
    }

    /**
     * sets the character invisibility to true
     */
    public void setInvisible(){
        invisible = true;
    }

    /**
     * sets the character invisibility to false
     */
    public void removeInvisible(){
        invisible = false;
    }

    public void resetAttackNum(){
        this.currentAttackNum = 0;
    }

    public void updateAttackNum(){
        this.currentAttackNum += 1;
    }

    
    /** 
     * @return double
     */
    public double getExperience() {
        return this.experience.get();
    }

    
    /** 
     * @return DoubleProperty
     */
    public DoubleProperty getExperiencePoints() {
        return this.experience;
    }

    
    /** 
     * @return int
     */
    public int getBattlesWon() {
        return this.numBattlesWon;
    }

    public void winBattles() {
        this.numBattlesWon += 1;
    }
    
    
    /** 
     * Returns all the bosses killed by the character
     * @return boolean
     */
    public boolean killedBosses(){
        return killedBosses;
    }

    public void setKilledBosses(){
        killedBosses = true;
    }

    
    /** 
     * @param numOfCycles
     */
    public void setNumOfCycles(int numOfCycles){
        this.numOfCycles = numOfCycles;
    }

    
    /** 
     * @return int
     */
    public int getNumOfCycles(){
        return numOfCycles;
    }

    
    /** 
     * @return double
     */
    public double getFullHealth() {
        return 100;
    }
    
    
    /** 
     * Change the experience of character throughout the game.
     * @param exp
     */
    public void setExperience(Double exp) {
        Double newExperiance = this.experience.get() + exp;
        this.experience.set(newExperiance);
    }

    
    /** 
     * Handles character's attack in battle
     * @param player
     */
    public void characterAttack(Enemy player) {
        
        updateAttackNum();

        if (weaponItemsInUse != null) {
            double damage = weaponItemsInUse.damageToEnemy(player);
            setDamage(characterDamage+damage);
        }
        else{
            setDamage(characterDamage);
        }

        if(checkHelmet()){
            setDamage(characterDamage-1);
        }

        // stun the character.
        if(player.getType().equals("Doggie") && currentAttackNum < 3){
            return;
        }

        player.reduceHealth(this);
        
    }

    
    /** 
     * Checks whether the character has equipped a helemt or not
     * @return boolean
     */
    private boolean checkHelmet(){

        for(DefenceStrategy d:defenciveItemsInUse){
            if(d instanceof Helmet){
                return true;
            }
        }

        return false;

    }

    
    /** 
     * Make the charcater equip the item.
     * @param item
     */
    public void setEquippedItems(Items item){
        equippedItems.add(item);
        if(item instanceof WeaponStrategy){
            setWeaponItem((WeaponStrategy) item);
        }
        if (item instanceof DefenceStrategy) {
            setDefenceItem((DefenceStrategy) item);
        }
    }
    
    
    /** 
     * @return ArrayList<Items>
     */
    public ArrayList<Items> getEquippedItems() {
        return equippedItems;
    }

    
    /** 
     * @param item
     */
    public void removeEquippedItems(BasicItems item){
        equippedItems.remove(item);
    }

    /**
     * Handles character's health reduction to different enemy attacks.
     */
    @Override
    public void reduceHealth(Players players){
        if (defenciveItemsInUse.size() > 0) {
            for(DefenceStrategy d:defenciveItemsInUse){
                double Enemydamage = d.defendAgainstAttack((Enemy)players);
                double newHealth = getHealth() - (players.getDamage()-players.getDamage()*Enemydamage);
                setHealth((double) Math.round(newHealth * 10) / 10);
            } 
        }
        else {
            double newHealth = getHealth() - players.getDamage();
            setHealth(newHealth);
        }
    }

    
    /** 
     * Sets the charcater's attack weapon
     * @param itemToSet
     */
    public void setWeaponItem(WeaponStrategy itemToSet) {
        this.weaponItemsInUse = itemToSet;
        
    }
    
    
    /** 
     * Sets the character's defence gear
     * @param itemToSet
     */
    public void setDefenceItem(DefenceStrategy itemToSet) { 
        this.defenciveItemsInUse.add(itemToSet);
    }

    
    /** 
     * Remove the character's defence gear
     * @param itemToSet
     */
    public void removeDefenceItem(DefenceStrategy itemToSet) {
        this.defenciveItemsInUse.remove(itemToSet);
    }

    public void refillHealth() {
        if (this.getHealth() < 100) {
            this.setHealth(100);
        } else {
            ReceiveGoldItems(15);
        }
    }

    
    /** 
     * @return int
     */
    public int getGold(){
        return goldItems.get();
    }

    
    /** 
     * @return SimpleIntegerProperty
     */
    public SimpleIntegerProperty getGoldItems(){
        return goldItems;
    }
    
    /** 
     * @param goldItemsAdd
     */
    public void ReceiveGoldItems(int goldItemsAdd) {
        this.goldItems.set(this.goldItems.get() + goldItemsAdd);
    }

    
    /** 
     * @param goldItemsMinus
     */
    public void reduceGoldItems(int goldItemsMinus) {
        this.goldItems.set(this.goldItems.get() - goldItemsMinus);
    }

    
    /** 
     * @return IntegerProperty
     */
    public IntegerProperty goldProperty() {
        return goldItems;
    }

    
    /** 
     * @return DoubleProperty
     */
    public DoubleProperty healthProperty() {
        return getHealthProperty();
    }

    
    /** 
     * @param dc
     */
    public void setDoggieCoin(DoggieCoin dc) {
        this.doggieCoin = dc;
    }

    
    /** 
     * @return DoggieCoin
     */
    public DoggieCoin getDoggieCoin(){
        return doggieCoin;
    }

    
    /** 
     * @return DoubleProperty
     */
    public DoubleProperty experienceProperty() {
        return experience;
    }

    /**
     * Increase character's health accordingly when it buys a health potion
     */
    public void purchaseHealthPotion() {
        if (this.getHealth() <= 80) {
            this.setHealth(this.getHealth() + 20);
        } else if (this.getHealth() < 100) {
            this.setHealth(100);
        }
    }

}
