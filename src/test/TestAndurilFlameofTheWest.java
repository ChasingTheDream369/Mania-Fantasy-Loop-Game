package test;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.javatuples.Pair;
import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Slug;
import unsw.loopmania.Vampire;
import unsw.loopmania.Zombie;
import unsw.loopmania.AndurilFlameOfTheWest;
import unsw.loopmania.Character;
import unsw.loopmania.Doggie;
import unsw.loopmania.ElanMuske;
import unsw.loopmania.Enemy;

public class TestAndurilFlameofTheWest {
    
    TestingWorld tw = new TestingWorld();
    LoopManiaWorld world =  tw.world();
    List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
    
    @Test
    public void TestIsRareItem() {
        TestRareItems rareItemsTest = new TestRareItems();
        rareItemsTest.runRareItemsUnitTests();
    }

    @Test
    public void TestAndurilAttackEnemy() {
  
        PathPosition pathPosition = new PathPosition(0, orderedPath);
        PathPosition pathPosition1 = new PathPosition(1, orderedPath);
        PathPosition pathPosition2 = new PathPosition(2, orderedPath);
        PathPosition pathPosition3 = new PathPosition(3, orderedPath);

        Character c = new Character(pathPosition);
        world.setCharacter(c);
        
        Enemy zombie = new Zombie(pathPosition2);        
        world.addEnemy(zombie);  

        Enemy vampire = new Vampire(pathPosition1);        
        world.addEnemy(vampire);  

        Enemy slug = new Slug(pathPosition3);        
        world.addEnemy(slug);   


        Pair<Integer, Integer> firstAvailableSlot = world.getFirstAvailableSlotForItem();
        // now we insert the new sword, as we know we have at least made a slot available...
        AndurilFlameOfTheWest afoWest = new AndurilFlameOfTheWest(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));

        // This adds the TreeStump into the list of RareItems inventory,
        // by looking at the correct condtions
        
        double characterDamage = c.getDamage();
        c.setWeaponItem(afoWest);

        double zombie_health = zombie.getHealth();

        c.characterAttack(zombie);

        assertEquals(zombie_health-(characterDamage+3), zombie.getHealth());

        double vampire_health = vampire.getHealth();
        double slug_health = slug.getHealth();

        c.characterAttack(vampire);
        c.characterAttack(slug);

        assertEquals(vampire_health-(characterDamage+3), vampire.getHealth());
        assertEquals(slug_health-(characterDamage+3), slug.getHealth());

    }

    @Test
    public void TestAndurilAttackBosses() {

        PathPosition pathPosition = new PathPosition(0, orderedPath);
        PathPosition pathPosition1 = new PathPosition(1, orderedPath);
        PathPosition pathPosition2 = new PathPosition(2, orderedPath);
        PathPosition pathPosition3 = new PathPosition(3, orderedPath);

        Character c = new Character(pathPosition);
        world.setCharacter(c);

        
        Enemy doggie = new Doggie(pathPosition1);
        world.addEnemy(doggie);
        Enemy elanMuske = new ElanMuske(pathPosition2);
        world.addEnemy(elanMuske);

        Pair<Integer, Integer> firstAvailableSlot = world.getFirstAvailableSlotForItem();
        // now we insert the new sword, as we know we have at least made a slot available...
        AndurilFlameOfTheWest afoWest = new AndurilFlameOfTheWest(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        double characterDamage = c.getDamage();
        c.setWeaponItem(afoWest);

        double doggieHealth = doggie.getHealth();

        // Character is stun the firt time   
        c.characterAttack(doggie);
        assertEquals(doggieHealth, doggie.getHealth());

        // Character is stun the second time   
        c.characterAttack(doggie);
        assertEquals(doggieHealth, doggie.getHealth());
        
        c.characterAttack(doggie);
        assertEquals(doggieHealth-(characterDamage+afoWest.getBossesDamage()), doggie.getHealth());

        double elanMuskeHealth = elanMuske.getHealth();

        c.characterAttack(elanMuske);

        assertEquals(elanMuskeHealth-(characterDamage+afoWest.getBossesDamage()), elanMuske.getHealth());

    }

}
