package unsw.loopmania;

public class Dragon extends Players implements CharacterStrategy{

    private Character character;

    /**
     * The constructor takes in PathPosition and Character
     * this class increases the damage of character by 4 times.
     */

    public Dragon(PathPosition pathPosition,Character character){
        super(pathPosition);
        this.character = character;
        setDamage(character.getDamage()*4);
        setHealth(character.getFullHealth());
        setType("Dragon");
    }

    /**
     * Just attack the enemy without any weapons items.
     */

    @Override
    public void characterAttack(Enemy player) {
        player.reduceHealth(this);
    }
    
    /**
     * The attack by the enemy is reduce by 30%.
     */

    @Override
    public void reduceHealth(Players player){
        double newHealth = getHealth() - (player.getDamage()-player.getDamage()*0.3);
        if(newHealth <= 0){
            setAlive(false);
        }
        setHealth(newHealth);
        character.setHealth(newHealth);
    }

}
