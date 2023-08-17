package test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.javatuples.Pair;
import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Slug;
import unsw.loopmania.TreeStump;
import unsw.loopmania.Vampire;
import unsw.loopmania.Zombie;
import unsw.loopmania.Character;
import unsw.loopmania.Doggie;
import unsw.loopmania.ElanMuske;
import unsw.loopmania.Enemy;

public class TestTreeStump {
    
    TestingWorld tw = new TestingWorld();
    LoopManiaWorld world =  tw.world();
    List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();

    

    // Run all the unit tests corresponding to it being a rare item.
    @Test
    public void TestIsRareItem() {
        TestRareItems rareItemsTest = new TestRareItems();
        rareItemsTest.runRareItemsUnitTests();
    }
    
    
    @Test
    public void TestTreeStumpDefenceEnemy() {
        
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


        Pair<Integer, Integer> firstAvailableSlot = world.getFirstAvailableSlotForRareItem();
        // now we insert the new sword, as we know we have at least made a slot available...
        TreeStump treeStump = new TreeStump(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));

        // This adds the TreeStump into the list of RareItems inventory,
        // by looking at the correct condtions
        
        double initial_ch_health = c.getHealth();

        c.setDefenceItem(treeStump);

        double zombie_attack = zombie.getDamage();

        zombie.enemyAttack(c);
        assertEquals((int)(zombie_attack-0.3*zombie_attack), (int)(initial_ch_health - c.getHealth()));

        double slug_attack = slug.getDamage();
        double slug_character_health = c.getHealth();

        slug.enemyAttack(c);
        //assertEquals((int)slug_attack -0.3*slug_attack, (int) slug_character_health - c.getHealth());
        assertEquals((int)(slug_attack-0.3*slug_attack), (int)(slug_character_health - c.getHealth()));


        double vampire_attack = vampire.getDamage();
        double vampire_character_health = c.getHealth();

        vampire.enemyAttack(c);
        assertEquals(Math.round(vampire_attack-0.3*vampire_attack), Math.round(vampire_character_health - c.getHealth()));


    }

    @Test
    public void TestTreeStumpDefenceBosses() {

        PathPosition pathPosition = new PathPosition(0, orderedPath);
        PathPosition pathPosition1 = new PathPosition(1, orderedPath);
        PathPosition pathPosition2 = new PathPosition(2, orderedPath);
        PathPosition pathPosition3 = new PathPosition(3, orderedPath);

        Character c = new Character(pathPosition);
        world.setCharacter(c);

        //Create the new Bosses enemies.
        Enemy doggie = new Doggie(pathPosition1);
        Enemy elanMuske = new ElanMuske(pathPosition2);

        world.addEnemy(doggie);  
        world.addEnemy(elanMuske);  

        Pair<Integer, Integer> firstAvailableSlot = world.getFirstAvailableSlotForItem();
        // now we insert the new sword, as we know we have at least made a slot available...
        TreeStump treeStump = new TreeStump(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));

        // This adds the TreeStump into the list of RareItems inventory,
        // by looking at the correct condtions
        
        double initial_ch_health = c.getHealth();

        c.setDefenceItem(treeStump);

        double doggie_attack = doggie.getDamage();

        doggie.enemyAttack(c);
        assertEquals((int)(doggie_attack-0.8*doggie_attack), (int)(initial_ch_health - c.getHealth()));

        double elanMuskeAttack = elanMuske.getDamage();
        double elanMuskeCharacterHealth = c.getHealth();

        elanMuske.enemyAttack(c);
        //assertEquals(elanMuskeAttack-0.8*elanMuskeAttack, elanMuske_character_health - c.getHealth());
        assertEquals((int)(elanMuskeAttack-0.8*elanMuskeAttack), (int)(elanMuskeCharacterHealth - c.getHealth()));

    }

}
