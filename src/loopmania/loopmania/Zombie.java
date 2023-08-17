package unsw.loopmania;

public class Zombie extends Enemy implements EnemyStrategy{
    
    private static final int speed = 3;
    private static final int max_bite = 9;
    private static final int min_bite = 2;

    private GainsOnDefeat gains;
    private int criticalBite;

    /**Initialises the fields with respect to zombie */
    public void init_zombie() {
        setSpeed(speed);
        setType("Zombie");
        setHealth(17);
        setDamage(7);
        setBattleRadius(1.5);
        setSupportRadius(2);
        setExperience(7);
        int rand = (int)Math.floor(Math.random()*(5-0+0)+0);
        int rand1 = (int)Math.floor(Math.random()*(7-0+0)+0);
        this.gains = new GainsOnDefeat(7, 7, items[rand],cards[rand1]);
    }

    public Zombie(PathPosition position){
        super(position);
        criticalBite = (int)Math.floor(Math.random()*(max_bite-min_bite+1)+min_bite);
        init_zombie();
    }

    public Zombie(PathPosition position,int cur){
        super(position);
        criticalBite = 1;
        init_zombie();
    }

    /**
     * decreases the player health based on the what the plaer type is,
     * given the health of enemy is higher.
     */
    public void enemyAttack(Players player){
        updateAttackNum();
        attackCrticalCheck(currentAttackNum, player);
        if (this.getHealth() > 0) {
            attack(player);
        }
    }

    /**
     * Gets the gains on defeat by the chracter by beating this enemy.
     */
    public GainsOnDefeat getGainsOnDefeat(){
        return gains;
    }

    /**
     * Check if it is a critical byte and if it is then sets the allied soldier
     * to False adn the make him a zombie implicity.
     * @param currentAttackNum
     * @param player
     */
    private void attackCrticalCheck(int currentAttackNum, Players player){
        
        if(currentAttackNum == criticalBite) {
            if (player.getType().equals("AlliedSoldier")) {
                AlliedSoldier allySoldier = (AlliedSoldier) player;
                allySoldier.setAlive(false);
                allySoldier.setIsZombie(true);
            }
        }

    }

    /**
     * Moves the zombie up the path(based on the assumption)
     */
    @Override
    public void move(){
        
        moveUpPath();
        
    }


}
