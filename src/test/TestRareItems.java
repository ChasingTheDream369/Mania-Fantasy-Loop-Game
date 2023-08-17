package test;
import static org.junit.Assert.assertEquals;

import java.util.List;
import org.javatuples.Pair;
import org.junit.Test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.RareItems;
import unsw.loopmania.Character;

public class TestRareItems {

    TestingWorld tw = new TestingWorld();
    LoopManiaWorld world =  tw.world();
    List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath(); 

    //////////////////////////////////////////////////////////////////////////////
    ///////////////////////////     UNIT TESTS     ///////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    /* Check whether all the rare items have been loaded correctly in the 
    Loop Mania World correctly */
    @Test
    public void TestRareItemsLoadedLoopManiaWorld() {
        assertEquals(tw.getLoadedRareItems().size(), world.getRareItemsGiven().size());
    }

    /* Check whether when the chracter wins a battle based on a random seed value the 
    character wins a random item.
    Assumption: The character wins a rareItem on every nth battle win with a enemy where 'n' is the 
    number of rare items sent uplloaded to the world.*/

    @Test
    public void TestRareItemsWonRandom() {
        
        int rareItemsWorldLength = tw.getLoadedRareItems().size();
        int rareItemsUnequippedInvetoryLen = world.getUnequippedRareItems().size();
        PathPosition pathPosition = new PathPosition(0, orderedPath);

        Character c = new Character(pathPosition);
        world.setCharacter(c);


        for (int i = 0; i <= rareItemsWorldLength; i++) {
            c.winBattles();
            world.checkLoadOneRing();
        }

        int rareItemsUnequippedInvetoryLenWon = world.getUnequippedRareItems().size();
        assertEquals(rareItemsUnequippedInvetoryLen+1, rareItemsUnequippedInvetoryLenWon);

    }



    /* Check whether the Rare item lists in the Inventory 
    satisfy the limit levied by us.
    Assumption is that the RareItems inventory limit is 3.*/
    @Test
    public void TestRareItemsInventoryLimit() {

        int rareItemsWorldLength = tw.getLoadedRareItems().size();

        int rareItemsInventoryMax = world.rareunEquippedInventoryWidth;
        PathPosition pathPosition = new PathPosition(0, orderedPath);

        Character c = new Character(pathPosition);
        world.setCharacter(c);

        for (int i = 0; i < rareItemsWorldLength; i++) {
            c.winBattles();
            world.checkLoadOneRing();

        }
        // find the oldestRareItem.
        RareItems oldestRareItem = world.getUnequippedRareItems().get(0);

        // win the battle for about 3 times more so that the oldes item in the
        // equipped rare items inventory gets replaced. 
        for (int i = 0; i < rareItemsInventoryMax*rareItemsWorldLength; i++) {
            c.winBattles();
            world.checkLoadOneRing();
        }

        assert(!world.getUnequippedRareItems().contains(oldestRareItem));
        
    }
    

    /*Runs all the unit tests corresponding to all the 
    rare items( to e called in the
    individual are items test files)*/

    public void runRareItemsUnitTests() {
        this.TestRareItemsLoadedLoopManiaWorld();
        this.TestRareItemsWonRandom();
        this.TestRareItemsInventoryLimit();
    }


    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////    INTEGRATION TESTS     //////////////////////////
    //////////////////////////////////////////////////////////////////////////////




}
