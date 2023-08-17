package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.Sword;
import unsw.loopmania.PathPosition;

/**
 * this class is a dummy class demonstrating how to setup tests for the project
 * you should setup additional test classes in a similar fashion, aiming to achieve high coverage.
 * A clickable "Run Test" link should appear if you have installed the Java Extension Pack properly.
 */

public class TrivialTest {
    
    @Test
    public void blahTest(){
        assertEquals("a", "a");
    }
    
    // @Test
    // public void blahTest2(){
    //     LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>(), null, null);
    //     assertEquals(d.getWidth(), 1);
    // }

}
