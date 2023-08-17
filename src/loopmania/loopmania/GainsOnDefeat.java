package unsw.loopmania;

public class GainsOnDefeat {
    
    private Double experiencePoint;
    private int gold;
    private Items item;
    private Card card;

    /**
     * This class is for the loots given to character when it defeats a enemy.
     * @param experiencePoint
     * @param gold
     * @param item
     * @param card
     */

    public GainsOnDefeat(double experiencePoint, int gold, Items item, Card card){
        this.experiencePoint = experiencePoint;
        this.gold = gold;
        this.item = item;
        this.card = card;
    }

    /**
     * get the experience
     * @return
     */

    public double getExperiencePoint() {
        return experiencePoint;
    }

    /**
     * get the Gold to be given
     * @return
     */

    public int getGold() {
        return gold;
    }
    
    /**
     * get the Item to be given.
     * @return
     */

    public Items getItem() {
        return item;
    }
    
    /**
     * get the card to be given.
     * @return
     */

    public Card getCard() {
        return card;
    }

    
    
}
