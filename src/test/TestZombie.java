package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.*;
import org.javatuples.Pair;

import unsw.loopmania.Character;


public class TestZombie {
    
    private static final double CHARACTERHEALTH = 100;
    private static final double CHARACTERDAMAGE = 5;
    private static final double INTIALEXPERIANCE = 0;
    private static final double SLUGHEALTH = 15;
    private static final double SLUGBAT = 1.2;
    private static final double VAMPIREHEALTH = 30;
    private static final double ZOMBIEHEALTH = 17;
    private static final double ZOMBIEDAMAGE = 7;
    private static final double ZOMBIEBAT = 1.5;
    private static final double ZOMBIESUP = 2;
    private static final double VAMPIREDAMAGE = 9.0;
    private static final double SLUGDAMAGE = 5;

    @Test
    public void testBasicAttack() {
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();

        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        PathPosition pathPosition = new PathPosition(1, orderedPath);
        PathPosition pathPosition1 = new PathPosition(2, orderedPath);
        PathPosition pathPosition2 = new PathPosition(3, orderedPath);


        Zombie zombie = new Zombie(pathPosition);

        assertEquals(ZOMBIEHEALTH, zombie.getHealth());
        
        assertEquals(ZOMBIEDAMAGE, zombie.getDamage());

        assertEquals(ZOMBIEBAT, zombie.getBattleRadius());
        
        assertEquals(ZOMBIESUP, zombie.getSupportRadius());

        world.addEnemy(zombie);

        Character character = new Character(pathPosition1);

        world.setCharacter(character);
        
        world.runBattles();

        assertFalse(zombie.isAlive());

        Zombie zombie1 = new Zombie(new PathPosition(4, orderedPath),1);

        world.addEnemy(zombie1);
    
        AlliedSoldier soldier = new AlliedSoldier(pathPosition2);

        world.addAlliedSoldier(soldier);

        world.runBattles();

        assertFalse(soldier.isAlive());

        assertTrue(soldier.getIsZombie());

        zombie1.enemyAttack(character);

    }
}
