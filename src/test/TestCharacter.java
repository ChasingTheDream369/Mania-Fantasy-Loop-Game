package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.*;
import org.javatuples.Pair;

import unsw.loopmania.Character;

public class TestCharacter {

    private static final double CHARACTERHEALTH = 100;
    private static final double CHARACTERDAMAGE = 5;
    private static final double INTIALEXPERIANCE = 0;
    private static final double SLUGHEALTH = 15;
    private static final double VAMPIREHEALTH = 30;
    private static final double ZOMBIEHEALTH = 17;
    private static final double ZOMBIEDAMAGE = 7;
    private static final double VAMPIREDAMAGE = 9.0;
    private static final double SLUGDAMAGE = 1.5;


    @Test
    public void testBasicFunctionality(){

        Pair<Integer, Integer> pair1 = new Pair<Integer, Integer>(0,0); 
        Pair<Integer, Integer> pair2 = new Pair<Integer, Integer>(0,1);
        Pair<Integer, Integer> pair3 = new Pair<Integer, Integer>(1,1);
        Pair<Integer, Integer> pair4 = new Pair<Integer, Integer>(2,1);
        Pair<Integer, Integer> pair5 = new Pair<Integer, Integer>(2,2);

        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();

        orderedPath.add(pair1);
        orderedPath.add(pair2);
        orderedPath.add(pair3);
        orderedPath.add(pair4);
        orderedPath.add(pair5);

        PathPosition pos1 = new PathPosition(0, orderedPath);
        PathPosition pos2 = new PathPosition(1, orderedPath);
        PathPosition pos3 = new PathPosition(2, orderedPath);
        PathPosition pos4 = new PathPosition(3, orderedPath);
        PathPosition pos5 = new PathPosition(4, orderedPath);


        Character character = new Character(pos1);

        assertEquals(CHARACTERHEALTH, character.getHealth());
        assertEquals(CHARACTERDAMAGE, character.getDamage());
        assertEquals(INTIALEXPERIANCE, character.getExperience());

        Vampire vampire = new Vampire(pos5);
        Zombie zombie = new Zombie(pos2);
        Slug slug = new Slug(pos3);

        character.characterAttack(vampire);

        assertEquals(VAMPIREHEALTH-CHARACTERDAMAGE, vampire.getHealth());

        character.characterAttack(zombie);

        assertEquals(ZOMBIEHEALTH-CHARACTERDAMAGE, zombie.getHealth());

        character.characterAttack(slug);

        assertEquals(SLUGHEALTH-CHARACTERDAMAGE, slug.getHealth());

        SimpleIntegerProperty x = new SimpleIntegerProperty();
        x.set(0);
        SimpleIntegerProperty y = new SimpleIntegerProperty();
        y.set(0);

        WeaponStrategy sword = new Sword(x, y);

        vampire.setHealth(VAMPIREHEALTH);
        zombie.setHealth(ZOMBIEHEALTH);
        slug.setHealth(SLUGHEALTH);

        character.setWeaponItem(sword);
        
        character.characterAttack(vampire);

        assertEquals(VAMPIREHEALTH-(CHARACTERDAMAGE+3), vampire.getHealth());

        character.characterAttack(zombie);

        assertEquals(ZOMBIEHEALTH-(CHARACTERDAMAGE+3), zombie.getHealth());

        character.characterAttack(slug);

        assertEquals(SLUGHEALTH-(CHARACTERDAMAGE+3), slug.getHealth());

        DefenceStrategy armour = new Armour(x, y);
        
        character.setDefenceItem(armour);

        vampire.enemyAttack(character);

        final double newCharacterHealth = CHARACTERHEALTH-VAMPIREDAMAGE/2;

        assertEquals(newCharacterHealth, character.getHealth());

        zombie.enemyAttack(character);

        final double newCharacterHealth1 = newCharacterHealth-ZOMBIEDAMAGE/2;

        assertEquals(newCharacterHealth1, character.getHealth());

        slug.enemyAttack(character);

        final double newCharacterHealth2 = newCharacterHealth1-SLUGDAMAGE/2;

        assertEquals((int) newCharacterHealth2, (int) character.getHealth());

        character.removeDefenceItem(armour);


        // ================================================

        WeaponStrategy stake = new Stake(x,y);

        vampire.setHealth(VAMPIREHEALTH);
        zombie.setHealth(ZOMBIEHEALTH);
        slug.setHealth(SLUGHEALTH);

        character.setWeaponItem(stake);

        character.characterAttack(vampire);

        assertEquals(VAMPIREHEALTH-(CHARACTERDAMAGE+5), vampire.getHealth());

        character.characterAttack(zombie);

        assertEquals(ZOMBIEHEALTH-(CHARACTERDAMAGE+3), zombie.getHealth());

        character.characterAttack(slug);

        assertEquals(SLUGHEALTH-(CHARACTERDAMAGE+3), slug.getHealth());



        // ================================================

        WeaponStrategy staff = new Staff(x,y);

        vampire.setHealth(VAMPIREHEALTH);
        zombie.setHealth(ZOMBIEHEALTH);
        slug.setHealth(SLUGHEALTH);

        character.setWeaponItem(staff);

        character.characterAttack(vampire);

        assertEquals(VAMPIREHEALTH-(CHARACTERDAMAGE+1), vampire.getHealth());

        character.characterAttack(zombie);

        assertEquals(ZOMBIEHEALTH-(CHARACTERDAMAGE+1), zombie.getHealth());

        character.characterAttack(slug);

        assertEquals(SLUGHEALTH-(CHARACTERDAMAGE+1), slug.getHealth());


        // ================================================

        DefenceStrategy shield = new Shield(x,y);

        character.setDefenceItem(shield);

        vampire.enemyAttack(character);

        double newCharacterHealth3 =  (newCharacterHealth2-(VAMPIREDAMAGE-VAMPIREDAMAGE*0.3));

        assertEquals( Math.round(newCharacterHealth3), character.getHealth());

        zombie.enemyAttack(character);

        final double newCharacterHealth4 = newCharacterHealth3-(ZOMBIEDAMAGE-ZOMBIEDAMAGE*0.2);

        assertEquals((int) newCharacterHealth4, (int) character.getHealth());

        slug.enemyAttack(character);

        final double newCharacterHealth5 = newCharacterHealth4-(SLUGDAMAGE-SLUGDAMAGE*0.1);

        assertEquals((int)newCharacterHealth5, (int)character.getHealth());

        character.removeDefenceItem(shield);


        //============================================================

        DefenceStrategy helmet = new Helmet(x,y);

        character.setDefenceItem(helmet);

        vampire.enemyAttack(character);

        final double newCharacterHealth6 = newCharacterHealth5-(VAMPIREDAMAGE-VAMPIREDAMAGE*0.4);

        assertEquals((int) newCharacterHealth6, (int) character.getHealth());

        zombie.enemyAttack(character);

        final double newCharacterHealth7 = newCharacterHealth6-(ZOMBIEDAMAGE-ZOMBIEDAMAGE*0.3);

        assertEquals((int)newCharacterHealth7, (int)character.getHealth());

        slug.enemyAttack(character);

        final double newCharacterHealth8 = newCharacterHealth7-(SLUGDAMAGE-SLUGDAMAGE*0.2);

        assertEquals((int)newCharacterHealth8, (int)character.getHealth());

        // Health
        character.refillHealth();

        assertEquals(character.getHealth(), 100.0);
        character.setHealth(90);
        character.purchaseHealthPotion();

        assertEquals(character.getHealth(), 100.0);

        character.setHealth(70);
        character.purchaseHealthPotion();
        assertEquals(character.getHealth(), 90.0);

        character.reduceGoldItems(10);
        assertEquals(character.getGold(), 5);
        

        // =========================WITH HELMET=====================

        vampire.setHealth(VAMPIREHEALTH);
        zombie.setHealth(ZOMBIEHEALTH);
        slug.setHealth(SLUGHEALTH);

        character.characterAttack(vampire);

        assertEquals(VAMPIREHEALTH-(CHARACTERDAMAGE-1), vampire.getHealth());

        character.characterAttack(zombie);

        assertEquals(ZOMBIEHEALTH-(CHARACTERDAMAGE-1), zombie.getHealth());

        character.characterAttack(slug);

        assertEquals(SLUGHEALTH-(CHARACTERDAMAGE-1), slug.getHealth());

    }

    @Test
    public void testDeath() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world = tw.world();
        Character c = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(c);

        Enemy vamp = new Vampire (new PathPosition(0, world.getOrderedPath()));
        vamp.setDamage(99);
        vamp.setHealth(100);
        world.addEnemy(vamp);

        world.runBattles();

        assertEquals(c.isAlive(), false);
    }

    @Test
    public void testPurchaseHealthPotion() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world = tw.world();
        Character c = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(c);

        assertEquals(c.getHealth(),  100);
        c.setHealth(90);
        c.purchaseHealthPotion();

        assertEquals(c.getHealth(), 100);

        c.setHealth(70);
        c.purchaseHealthPotion();
        assertEquals(c.getHealth(), 90);
    }
}
