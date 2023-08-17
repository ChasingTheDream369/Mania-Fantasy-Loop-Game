package unsw.loopmania;

/**
 * WeaponStrategy: Used by all the weapons, provides an 
 * implementation of the amount of damage it causes to each of the 
 * player when given in an abstract way..!!
 */
public interface WeaponStrategy {

    public double damageToEnemy(Enemy player);

}
