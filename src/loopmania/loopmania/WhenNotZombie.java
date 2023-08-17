package unsw.loopmania;

public class WhenNotZombie implements AlliedSoldierState{
    
    private AlliedSoldier ally;

    public WhenNotZombie(AlliedSoldier soldier) {
        this.ally = soldier;
    }

    /**The players health is reduced based on the 
     * allied soldier current state (again is deprecated as it is 
     * not presenly used)
     */
    public void allyAttack(Players player) {
        player.reduceHealth(ally);
        
    }
    
    public Zombie getZombie() {
        return null;
    } 

}
