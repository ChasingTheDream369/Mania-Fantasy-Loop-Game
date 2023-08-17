package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.*;
import org.javatuples.Pair;

import unsw.loopmania.Character;

public class TestSlug {
    

    private static final double CHARACTERHEALTH = 100;
    private static final double CHARACTERDAMAGE = 5;
    private static final double INTIALEXPERIANCE = 0;
    private static final double SLUGHEALTH = 15;
    private static final double SLUGBAT = 1.2;
    private static final double VAMPIREHEALTH = 30;
    private static final double ZOMBIEHEALTH = 17;
    private static final double ZOMBIEDAMAGE = 7;
    private static final double VAMPIREDAMAGE = 9.0;
    private static final double SLUGDAMAGE = 1.5;

    @Test
    public void testBasicAttack() {

        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();


        //PathPosition pos1 = new PathPosition(0, orderedPath);
        PathPosition pos2 = new PathPosition(1, world.getOrderedPath());

        Character character = new Character(pos2);
        Slug slug = new Slug(pos2);

        assertEquals(SLUGHEALTH, slug.getHealth());
        
        assertEquals(SLUGDAMAGE, slug.getDamage());

        assertEquals(SLUGBAT, slug.getBattleRadius());
        
        assertEquals(SLUGBAT, slug.getSupportRadius());

        world.addEnemy(slug);

        world.setCharacter(character);
        
        world.runBattles();

        assertFalse(slug.isAlive());



    }
}
