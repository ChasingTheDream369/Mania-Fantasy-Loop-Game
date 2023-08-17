package unsw.loopmania;

public class WhenZombie implements AlliedSoldierState {
   
    private Zombie whenZombie;

    public WhenZombie(PathPosition position) {
        whenZombie = new Zombie(position);
    }
    /*Changes the allied soldier attack based on where,
    ** the when it is a zombie it attacks like an enemy
    (Although is a deprecated calss in this context as it is no longer used)
    */
     
    public void allyAttack(Players player) {
        whenZombie.enemyAttack(player);
    }

    public Zombie getZombie() {
        return whenZombie;
    } 

}
