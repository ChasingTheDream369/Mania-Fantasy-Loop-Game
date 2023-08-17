package unsw.loopmania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.javatuples.Pair;
import org.json.JSONArray;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * A backend world.
 *
 * A world can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 */
public class LoopManiaWorld {
    
    public static final int unequippedInventoryWidth = 4;
    public static final int unequippedInventoryHeight = 4;
    public static final int rareunEquippedInventoryWidth = 3;
    public static final int rareunEquippedInventoryHeight = 1;
    
    /**
     * width of the world in GridPane cells
     */
    private int width;

    /**
     * height of the world in GridPane cells
     */
    private int height;

    /**
     * generic entitites - i.e. those which don't have dedicated fields
     */
    private List<Entity> nonSpecifiedEntities;

    private Character character;
    public List<AlliedSoldier> ally;
    
    public List<Enemy> enemies;

    public HashMap<AlliedSoldier,Enemy> mapAllytoEnemy;

    private List<HealthPotion> healthPotionOnPath;
    public IntegerProperty numAllies;
    private List<Gold> goldOnPath;
    private List<Card> cardEntities;

    private List<Items> equippedInventoryItems;
    private List<BasicItems> unequippedInventoryItems;
    private List<RareItems> rareItemsGiven;
    private List<RareItems> unequippedRareItems;

    // CHANGED: Buildings
    private List<Buildings> buildings;
    private GoalCondtions characterGoals;
    private boolean hasDoggie;
    private boolean hasElan;
    private boolean loadedOneRing;
    private Missile missile;
    private ChickenWings chickenWings;
    private Ramen ramen;
    private Dragon dragon;

    /**
     * list of x,y coordinate pairs in the order by which moving entities traverse them
     */
    private List<Pair<Integer, Integer>> orderedPath;
    
    /**
     * create the world (constructor)
     * 
     * @param width width of world in number of cells
     * @param height height of world in number of cells
     * @param orderedPath ordered list of x, y coordinate pairs representing position of path cells in world
     */
    public LoopManiaWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath, GoalCondtions characterGoals, List<String> rareItems) {
        this.width = width;
        this.height = height;
        nonSpecifiedEntities = new ArrayList<>();
        character = null;
        ally = new ArrayList<>();
        enemies = new ArrayList<>();
        healthPotionOnPath = new ArrayList<>();
        goldOnPath = new ArrayList<>();
        cardEntities = new ArrayList<>();
        unequippedInventoryItems = new ArrayList<BasicItems>();
        equippedInventoryItems = new ArrayList<>();
        mapAllytoEnemy = new HashMap<>();
        this.orderedPath = orderedPath;
        this.buildings = new ArrayList<>();
        this.ally = new ArrayList<>();
        this.characterGoals = characterGoals;
        this.unequippedRareItems = new ArrayList<>();
        //this.rareItemsWon = new ArrayList<>();
        this.hasDoggie = false;
        this.hasElan = false;
        this.missile = null;
        this.chickenWings = null;
        this.ramen = null;
        this.dragon = null;
        //this.rareItemsWon = new ArrayList<>();
        this.rareItemsGiven = new ArrayList<>();
        getRareItems(rareItems);
        numAllies = new SimpleIntegerProperty(0);
    }


    public Dragon getDragon(){
        return dragon;
    }

    public void getRareItems(List<String> RareItems) {
        
        int rareItemsSent = RareItems.size();
        System.out.print(rareItemsSent);

        for (int i = 0; i < rareItemsSent; i++) {
            if (RareItems.get(i).equals("TreeStump")) {
                rareItemsGiven.add(new TreeStump(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));              
            } else if (RareItems.get(i).equals("AndurilFlameOfTheWest")) {
                rareItemsGiven.add(new AndurilFlameOfTheWest(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));              
            }  else if (RareItems.get(i).equals("TheOneRing")) {
                rareItemsGiven.add(new TheOneRing(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));              
            }
        }

        System.out.print(rareItemsGiven);

    }

    public void setRamen(Ramen ramen){
        this.ramen = ramen;
    }

    public void setChickenWings(ChickenWings chickenWings){
        this.chickenWings = chickenWings;
    }

    public void setMissile(Missile missile){
        this.missile = missile;
    }

    public List<RareItems> getRareItems() {
        return rareItemsGiven;
    }

    public IntegerProperty getNumAllies() {
        return numAllies;
    }

    public void updateNumAllies() {
        this.numAllies.set(ally.size());
    }

    public Character getCharacter() {
        return this.character;
    }

    public List<Enemy> getActiveEnemies() {
        return enemies;
    }

    public GoalCondtions getCharacterGoals(){
        return this.characterGoals;
    }

    public List<BasicItems> getUnequippedItems() {
        return this.unequippedInventoryItems;
    }

    public List<RareItems> getUnequippedRareItems() {
        return this.unequippedRareItems;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Card> getCards() {
        return cardEntities;
    }

    /**
     * set the character. This is necessary because it is loaded as a special entity out of the file
     * @param character the character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }
    
    public int getCharacterX() {
        return character.getX();
    }

    public int getCharacterY() {
        return character.getY();
    }

    public void checkLoadOneRing() {
        if (getCharacter().getBattlesWon() % getRareItems().size() == 0) {
            addTheOneRing();
        }
    }


    public RareItems getRandomRareItem() {
        int rand = (int)Math.floor(Math.random()*(rareItemsGiven.size()-0+0)+0);
        return rareItemsGiven.get(rand);
    }
    /**
     * add a generic entity (without it's own dedicated method for adding to the world)
     * @param entity
     */
    public void addEntity(Entity entity) {
        // for adding non-specific entities (ones without another dedicated list)
        nonSpecifiedEntities.add(entity);
    }

    public List<Gold> getGoldOnPath() {
        return goldOnPath;
    }

    public void addGoldOnPath(Gold g) {
        goldOnPath.add(g);
    }

    public List<Gold> takeGold(){

        List<Gold> goldItem = new ArrayList<Gold>();

        if(goldOnPath.size() == 0){
            goldItem = spawnGoldItems();
            //return goldItem;
        }

        for(Gold i:goldOnPath){

            if(character.getX() == i.getX() && character.getY() == i.getY()){
                i.essentialStrategy(character);
                goldOnPath.remove(i);
                i.destroy();
                goldItem = spawnGoldItems();
                //return goldItem;
                break;
            }
        }

        return goldItem;

    }

    public List<HealthPotion> takeHealthPotions(){

        List<HealthPotion> healthPotionsitem = new ArrayList<HealthPotion>();

        if(healthPotionOnPath.size() == 0){
            healthPotionsitem = spawnOnPathItemsStrategy();
            //return healthPotionsitem;
        }


        for(HealthPotion i:healthPotionOnPath){

            if(character.getX() == i.getX() && character.getY() == i.getY()){
                i.pathItemAdd(character);
                healthPotionOnPath.remove(i);
                i.destroy();
                healthPotionsitem = spawnOnPathItemsStrategy();
                //return healthPotionsitem;
                break;
            }

        }
        return healthPotionsitem;

    }

    public boolean getLoadedOnering() {
        return loadedOneRing;
    }

    public List<Gold> spawnGoldItems(){

        Pair<Integer, Integer> pos = possiblyGetRandomSpawnPosition();
        List<Gold> pathItems = new ArrayList<>();
        if(pos != null){

            int indexInPath = orderedPath.indexOf(pos);
            PathPosition location = new PathPosition(indexInPath,orderedPath);
            Gold item = new Gold(location.getX(),location.getY());
            goldOnPath.add(item);
            pathItems.add(item);

        }        

        return pathItems;

    }


    public List<HealthPotion> spawnOnPathItemsStrategy(){

        Pair<Integer, Integer> pos = possiblyGetRandomSpawnPosition();
        List<HealthPotion> pathItems = new ArrayList<>();
        if(pos != null){

            int indexInPath = orderedPath.indexOf(pos);
            PathPosition location = new PathPosition(indexInPath,orderedPath);
            HealthPotion item = new HealthPotion(location.getX(),location.getY());
            healthPotionOnPath.add(item);
            pathItems.add(item);

        }
        
        return pathItems;

    }


    /**
     * spawns Slugs if the conditions warrant it, adds to world
     * @return list of the enemies to be displayed on screen
     */
    public List<Enemy> possiblySpawnEnemies(int currentCycle) {
        
        Pair<Integer, Integer> pos = possiblyGetRandomSpawnPosition();
        List<Enemy> spawningEnemies = new ArrayList<>();
        if (pos != null){
            int indexInPath = orderedPath.indexOf(pos);
            Enemy enemy = new Slug(new PathPosition(indexInPath, orderedPath));
            enemies.add(enemy);
            spawningEnemies.add(enemy);
        }

        // Spawning the boss Elan on a random path tile after 40 cycles.
        if (currentCycle == 40 && character.getExperience() >= 10000) {
            if (!hasElan) {
                pos = possiblyGetRandomSpawnBossPosition();
                int indexInPath = orderedPath.indexOf(pos);
                Enemy enemy = new ElanMuske(new PathPosition(indexInPath, orderedPath));
                enemies.add(enemy);
                spawningEnemies.add(enemy);
                hasElan = true;
            }  
        }
        
        if (currentCycle == 20) {
            if (!hasDoggie) {
                pos = possiblyGetRandomSpawnBossPosition();
                int indexInPath = orderedPath.indexOf(pos);
                Enemy enemy = new Doggie(new PathPosition(indexInPath, orderedPath));
                enemies.add(enemy);
                spawningEnemies.add(enemy);
                hasDoggie = true;
                
            }   
        }
        
        for (Buildings building : buildings) {
            Enemy enemy = null;
            int index;

            switch(building.getType()) {
                case "vampiresCastleBuilding":
                    VampireCastleBuilding vampBuild = (VampireCastleBuilding) building;
                    index = getAdjacentPathTile(building.getX(), building.getY());
                    enemy = vampBuild.spawn(currentCycle, character, index, orderedPath);

                    break;

                case "ZombiePit":
                    ZombiePitBuilding zombieBuild = (ZombiePitBuilding) building;
                    index = getAdjacentPathTile(building.getX(), building.getY());
                    enemy = zombieBuild.spawn(character, index, orderedPath);
                    
                    break;

                default:
                    break;
            }

            if (enemy != null) {
                enemies.add(enemy);
                spawningEnemies.add(enemy);
            }
        }

        return spawningEnemies;
    }

    public int getAdjacentPathTile (int x, int y) {
        Pair<Integer, Integer> tileLeft = new Pair<Integer, Integer>(x - 1, y);
        Pair<Integer, Integer> tileRight = new Pair<Integer, Integer>(x + 1, y);
        Pair<Integer, Integer> tileUp = new Pair<Integer, Integer>(x, y - 1);
        Pair<Integer, Integer> tileDown = new Pair<Integer, Integer>(x, y + 1);

        for (Pair<Integer, Integer> tiles : orderedPath) {
            if (tiles.getValue0() == tileLeft.getValue0() && tiles.getValue1() == tileLeft.getValue1()) {
                return orderedPath.indexOf(tiles);
            } else if (tiles.getValue0() == tileRight.getValue0() && tiles.getValue1() == tileRight.getValue1()) {
                return orderedPath.indexOf(tiles);
            } else if (tiles.getValue0() == tileUp.getValue0() && tiles.getValue1() == tileUp.getValue1()) {
                return orderedPath.indexOf(tiles);
            } else if (tiles.getValue0() == tileDown.getValue0() && tiles.getValue1() == tileDown.getValue1()) {
                return orderedPath.indexOf(tiles); 
            }
        }

        return -1;
    }

    public int getPathTile (int x , int y) {
        Pair<Integer, Integer> tile = new Pair<Integer, Integer>(x, y);

        int pathTile = -1;
        
        for (Pair<Integer, Integer> tiles: orderedPath) {
            if (tiles.getValue0() == tile.getValue0()) {
                pathTile =  orderedPath.indexOf(tile);
                break;
            }
        }

        return pathTile;
    }


    public boolean isPathTile(Integer x, Integer y) {
        for (Pair<Integer, Integer> tiles : orderedPath) {

            if (tiles.getValue0() == x && tiles.getValue1() == y) {
                return true;
            }
        }

        return false;
    }

    /**
     * kill an enemy
     * @param enemy enemy to be killed
     */
    private void killEnemy(Enemy enemy){
        enemy.destroy();
        enemies.remove(enemy);
    }

    private void changeCriticalBite(Enemy e) {
        if (e.getType().equals("Vampire")) {
            Vampire v = (Vampire) e;
            v.setBattleCritialBite();
        }
    }

    private void updatePLayers(AlliedSoldier a, Enemy b,List<Enemy> defeatedEnemies,List<AlliedSoldier> defeatedAllied) {
        if (!a.isAlive()) {
            Enemy e = mapAllytoEnemy.get(a);
            if(e != null){
                defeatedEnemies.add(e);
            }
            defeatedAllied.add(a);

        } else {
            defeatedEnemies.add(b);
        }
    }

    // need to find a way to add it to the enemy and also a way to randomly add it.
    public void GameOver() {
        if (unequippedRareItems.size() > 0) {
            for (int i = 0; i < unequippedRareItems.size(); i++) {
                RareItems help = unequippedRareItems.get(i);
                if (help instanceof TheOneRing) {
                    help.rareItemsHelp(character);
                    loadedOneRing = false;
                } else if (unequippedRareItems.get(i).getConfusingModeItem() instanceof TheOneRing) {
                    help.rareItemsHelp(character); 
                }
            }
        }
    }

    private void updatePLayers(Character a, Enemy b, List<Enemy> defeatedEnemies) {
        if (!a.isAlive()) {
            GameOver();
        } else {
            if (b != null) {
                defeatedEnemies.add(b);
            }
            a.winBattles();
            if(b.getType().equals("Doggie")){
                DoggieCoin dc = DoggieCoin.getInstance(new SimpleIntegerProperty(330), new SimpleIntegerProperty(410));
                character.setDoggieCoin(dc);
            }
            if(b.getType().equals("ElanMuske")){
                DoggieCoin dc = DoggieCoin.getDoggieCoin();
                if(dc != null){
                    dc.decreasePrice();
                }
            }
        }
    }

    public AlliedSoldier getAlliedSoldier(int i) {
        return ally.get(i);
    }

    public void fightAllyBattle(Enemy e,List<Enemy> defeatedEnemies,List<AlliedSoldier> defeatedAllied) {
        
        changeCriticalBite(e);
        int i = 0;
        int end = ally.size();

        while (i < end) {
            AlliedSoldier a = getAlliedSoldier(i);
            while (a.isAlive() && e.isAlive()) {
                a.attack(e);
                e.enemyAttack(a);
                if (a.getIsZombie()) {
                    Zombie newZombie = new Zombie(a.getPosition());
                    fightCharacterBattle(newZombie, defeatedEnemies,defeatedAllied);  
                }
            }
            updatePLayers(a, e,defeatedEnemies,defeatedAllied);
            i = i + 1;
            end = ally.size();
        }

    }


    private void mapAlliedToEnemy(AlliedSoldier key,Enemy value){
        mapAllytoEnemy.put(key, value);
    }


    private void fightCharacterBattle(Enemy e,List<Enemy> defeatedEnemies,List<AlliedSoldier> defeatedAllied) {
        
        if(character.isDragon()){

            while(dragon != null && dragon.isAlive() && e.isAlive()){
                dragon.characterAttack(e);
                e.attack(dragon);
            }

            // If the dragon died then the game is gameover.
            if(!dragon.isAlive()){
                GameOver();
            }
            character.changeBack();
            dragon.destroy();
            dragon = null;

        } 
        
        else{

            changeCriticalBite(e);
            while (character.isAlive() && e.isAlive()) {
                character.characterAttack(e);
                //character.characterDefence(e); // I have made change in character which replace the use of this function.
                if(e.isAlliedSoldier()){
                    AlliedSoldier a = new AlliedSoldier(character.getPathPosition());
                    this.ally.add(a);
                    mapAlliedToEnemy(a,e);
                    e.destroy();
                    // NEED to MAKE the enemy disapear.
                    // run thread
                    CheckTheTrance check = new CheckTheTrance(a,ally,mapAllytoEnemy);
                    check.start();
                    return;
                }
                e.enemyAttack(character);
            }
        }
        
        updatePLayers(character, e,defeatedEnemies);

    }

    public List<Enemy> checkSupportRadi(Enemy e){

        List<Enemy> enemiesToFight = new ArrayList<Enemy>();

        for(Enemy i:enemies){
            if(i.equals(e)) continue;
            if ((Math.pow((character.getX()-i.getX()), 2) +  Math.pow((character.getY()-i.getY()), 2) < Math.pow((i.getSupportRadius()), 2)) && (!e.isAlliedSoldier())){
                enemiesToFight.add(i);
            }

        }

        return enemiesToFight;

    }

    private void removeAlliedSoldier(List<AlliedSoldier> soldiers){

        for(AlliedSoldier i:soldiers){
            i.destroy();
            ally.remove(i);
        }

    } 
    
    
    public void healingEnemies(ElanMuske enemy){
        for(Enemy e:enemies){
            if ((Math.pow((e.getX()-enemy.getX()), 2) +  Math.pow((e.getY()-enemy.getY()), 2) < Math.pow((enemy.getSupportRadius()), 2)) && (!e.isAlliedSoldier())){
                enemy.heal(e);
            }
        }
    }

    public void attackWithMissile(Enemy e){

        if(missile != null && (Math.pow((missile.getX()-e.getX()), 2) +  Math.pow((missile.getY()-e.getY()), 2) < Math.pow((e.getBattleRadius()), 2))){
            missile.attack(e);
            missile.destroy();
            missile = null;
        }

    }

    public Dragon takeRamen(){

        if(ramen != null){
            if(character.getX() == ramen.getX() && character.getY() == ramen.getY()){
                ramen.pathItemAdd(character);
                ramen.destroy();
                ramen = null;
                if(dragon != null){
                    dragon.destroy();
                    dragon = null;    
                }
                int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
                dragon = new Dragon(new PathPosition((indexPosition+1)%orderedPath.size(),orderedPath),character);
            }
        }

        return dragon;
    }

    public Ramen spawnRamen(int currentCycle){

        if(currentCycle != 0 && currentCycle%7 == 0 && ramen == null){
            Pair<Integer, Integer> pos = possiblyGetRandomSpawnBossPosition();
            if(pos != null){
                int indexInPath = orderedPath.indexOf(pos);
                PathPosition location = new PathPosition(indexInPath,orderedPath);
                ramen = new Ramen(location.getX(), location.getY());
            }
        }

        return ramen;

    }

    public void takeChickenWings(){

        if(chickenWings != null){
            if(character.getX() == chickenWings.getX() && character.getY() == chickenWings.getY()){
                chickenWings.pathItemAdd(character);
                chickenWings.destroy();
                chickenWings = null;
            }
        }

    }

    public ChickenWings spawnChickenWings(int currentCycle){

        ChickenWings chickenWings = null;

        if(currentCycle != 0 && currentCycle%3 == 0 && this.chickenWings == null){
            Pair<Integer, Integer> pos = possiblyGetRandomSpawnBossPosition();
            if(pos != null){
                int indexInPath = orderedPath.indexOf(pos);
                PathPosition location = new PathPosition(indexInPath,orderedPath);
                chickenWings = new ChickenWings(location.getX(), location.getY());
                this.chickenWings = chickenWings;
            }
            
        }

        return chickenWings;
    }
   

    public ChickenWings getChickenWings() {
        return chickenWings;
    }

    public Missile spawnMissile(){

        Missile missile = null;
        // Index out of bound error!!!!!!!! NEED TO FIX
        if(character.getExperience() != 0 && character.getExperience()%3 ==0 && this.missile == null){
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
            missile = new Missile(new PathPosition((indexPosition+1)%orderedPath.size(),orderedPath));
            this.missile = missile;
        }

        return missile;
    }

    public void supportRaddiFightAlly(List<Enemy> enemiesInSupportRadi, List<Enemy> defeatedEnemies, List<AlliedSoldier> defeatedAllied) {
        
        int totalSupportEnemies = enemiesInSupportRadi.size();
        int i = 0;

        while (i < totalSupportEnemies) {
            fightAllyBattle(enemiesInSupportRadi.get(i), defeatedEnemies, defeatedAllied);
            if (defeatedAllied.size() == ally.size()) {
                break;
            }
            i = i + 1;
        }
    }

    public void supportRaddiFightCharacter(List<Enemy> enemiesInSupportRadi, List<Enemy> defeatedEnemies, List<AlliedSoldier> defeatedAllied) {
        
        int totalSupportEnemies = enemiesInSupportRadi.size();
        int i = 0;

        while (i < totalSupportEnemies) {

            fightCharacterBattle(enemiesInSupportRadi.get(i), defeatedEnemies, defeatedAllied);
            if (!character.isAlive()) {
                break;
            }
            i = i + 1;
        }
            
    }

    /**
     * run the expected battles in the world, based on current world state
     * @return list of enemies which have been killed
     */
    public List<Enemy> runBattles() {
        List<Enemy> defeatedEnemies = new ArrayList<Enemy>();
        List<AlliedSoldier> defeatedAllied = new ArrayList<AlliedSoldier>();
        List<Enemy> enemiesInSupportRadi = new ArrayList<Enemy>();

        for (Enemy e: enemies) {

            if(e.getType().equals("ElanMuske")){
                healingEnemies((ElanMuske) e);
            }

            attackWithMissile(e);

            // Pythagoras: a^2+b^2 < radius^2 to see if within radius
            if ((Math.pow((character.getX()-e.getX()), 2) +  Math.pow((character.getY()-e.getY()), 2) < Math.pow((e.getBattleRadius()), 2)) && (!e.isAlliedSoldier())){
                enemiesInSupportRadi = checkSupportRadi(e);
                if(character.getInvisible()) continue;

                fightAllyBattle(e,defeatedEnemies,defeatedAllied);
                supportRaddiFightAlly(enemiesInSupportRadi, defeatedEnemies, defeatedAllied);

                character.resetAttackNum(); // To set the number of attack by the character = 0
                fightCharacterBattle(e, defeatedEnemies, defeatedAllied);

                supportRaddiFightCharacter(enemiesInSupportRadi, enemiesInSupportRadi, defeatedAllied);
                if (!character.isAlive()) {
                    character.destroy();
                    break;
                }
                removeAlliedSoldier(defeatedAllied);
            }
        }
        for (Enemy e: defeatedEnemies){
            // IMPORTANT = we kill enemies here, because killEnemy removes the enemy from the enemies list
            // if we killEnemy in prior loop, we get java.util.ConcurrentModificationException
            // due to mutating list we're iterating over
            killEnemy(e);
        }
        return defeatedEnemies;
    }

    public void addAlliedSoldier(AlliedSoldier a) {
        ally.add(a);
    }

    public List<AlliedSoldier> getAlliedSoldiers() {
        return ally;
    }

    public List<Items> getCharacterItems() {
        return character.getEquippedItems();
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    /**
     * remove card at a particular index of cards (position in gridpane of unplayed cards)
     * @param index the index of the card, from 0 to length-1
     */
    private void removeCard(int index){
        Card c = cardEntities.get(index);
        int x = c.getX();
        c.destroy();
        cardEntities.remove(index);
        character.ReceiveGoldItems(5);
        shiftCardsDownFromXCoordinate(x);
    }

    
    /**
     * remove an item by x,y coordinates
     * @param x x coordinate from 0 to width-1
     * @param y y coordinate from 0 to height-1
     */
    public void removeUnequippedInventoryItemByCoordinates(int x, int y){
        Entity item = getUnequippedInventoryItemEntityByCoordinates(x, y);
        removeUnequippedInventoryItem(item);
    }

    public void addEquippedInventoryItem(Items item){
        equippedInventoryItems.add(item);
    }

    public void removeEquippedInventoryItem(Entity item){
        item.destroy();
        equippedInventoryItems.remove(item);
    }

    private Entity getEquippedInventoryItemEntityByCoordinates(int x, int y){
        Entity ent = null;
        for (Entity e: unequippedInventoryItems){
            if ((e.getX() == x) && (e.getY() == y)){
                ent = e;
                break;
            }
        }
        return ent;
    }

    public List<Items> getEquippedInventory() {
        return equippedInventoryItems;
    }

    private void removeInventoryItemIfAvailable(int x, int y) {
        Entity e = getEquippedInventoryItemEntityByCoordinates(x, y);
        if (e != null) {
            removeEquippedInventoryItem(e);
        }
    }

    public void equippedInventoryItemsNotify(Items item, int x, int y){
        
        removeInventoryItemIfAvailable(x, y);
        addEquippedInventoryItem(item);
        character.setEquippedItems(item);

    }

    /**
     * run moves which occur with every tick without needing to spawn anything immediately
     */
    public void runTickMoves(){
        character.moveDownPath();
        if(dragon != null){
            dragon.moveDownPath();
        }
        moveBasicEnemies();
        if(missile != null){
            missile.moveUpPath();
        }
        
    }
    
    /**
     * remove an item from the unequipped inventory
     * @param item item to be removed
     */
    private void removeUnequippedInventoryItem(Entity item){
        item.destroy();
        unequippedInventoryItems.remove(item);
    }

    /**
     * return an unequipped inventory item by x and y coordinates
     * assumes that no 2 unequipped inventory items share x and y coordinates
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return unequipped inventory item at the input position
     */
    private Entity getUnequippedInventoryItemEntityByCoordinates(int x, int y){
        for (Entity e: unequippedInventoryItems){
            if ((e.getX() == x) && (e.getY() == y)){
                return e;
            }
        }
        return null;
    }

    /**
     * remove item at a particular index in the unequipped inventory items list (this is ordered based on age in the starter code)
     * @param index index from 0 to length-1
     */
    private void removeItemByPositionInUnequippedInventoryItems(int index){
        Entity item = unequippedInventoryItems.get(index);
        item.destroy();
        unequippedInventoryItems.remove(index);
    }

    /**
     * get the first pair of x,y coordinates which don't have any items in it in the unequipped inventory
     * @return x,y coordinate pair
     */

    public Pair<Integer, Integer> getFirstAvailableSlotForItem(){
        // first available slot for an item...
        // IMPORTANT - have to check by y then x, since trying to find first available slot defined by looking row by row
        for (int y=0; y<unequippedInventoryHeight; y++){
            for (int x=0; x<unequippedInventoryWidth; x++){
                if (getUnequippedInventoryItemEntityByCoordinates(x, y) == null){
                    return new Pair<Integer, Integer>(x, y);
                }
            }
        }
        return null;
    }

    /**
     * shift card coordinates down starting from x coordinate
     * @param x x coordinate which can range from 0 to width-1
     */
    private void shiftCardsDownFromXCoordinate(int x){
        for (Card c: cardEntities){
            if (c.getX() >= x){
                c.x().set(c.getX()-1);
            }
        }
    }

    /**
     * move all enemies
     */
    private void moveBasicEnemies() {
        for (Enemy e: enemies){
            e.move();
        }
    }

    public Pair<Integer, Integer> possiblyGetRandomSpawnBossPosition() {

        Random rand = new Random(); 
        
        
            List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));

            // inclusive start and exclusive end of range of positions not allowed
            int startNotAllowed = (indexPosition - 2 + orderedPath.size())%orderedPath.size();

            int endNotAllowed = (indexPosition + 3)%orderedPath.size();
            
            // note terminating condition has to be != rather than < since wrap around...
            for (int i=endNotAllowed; i!=startNotAllowed; i=(i+1)%orderedPath.size()){
                orderedPathSpawnCandidates.add(orderedPath.get(i));
            }
            
            // choose random choice
            Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates.get(rand.nextInt(orderedPathSpawnCandidates.size()));

            return spawnPosition;

    }
    /**
     * get a randomly generated position which could be used to spawn an enemy
     * @return null if random choice is that wont be spawning an enemy or it isn't possible, or random coordinate pair if should go ahead
     */
    public Pair<Integer, Integer> possiblyGetRandomSpawnPosition(){
        
        Random rand = new Random(); 
        int choice = rand.nextInt(2);
        
        if ((choice == 0) && (enemies.size() < 2)){
            List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));

            // inclusive start and exclusive end of range of positions not allowed
            int startNotAllowed = (indexPosition - 2 + orderedPath.size())%orderedPath.size();

            int endNotAllowed = (indexPosition + 3)%orderedPath.size();
            
            // note terminating condition has to be != rather than < since wrap around...
            for (int i=endNotAllowed; i!=startNotAllowed; i=(i+1)%orderedPath.size()){
                orderedPathSpawnCandidates.add(orderedPath.get(i));
            }
            
            // choose random choice
            Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates.get(rand.nextInt(orderedPathSpawnCandidates.size()));

            return spawnPosition;
        }
        return null;
    }

    /**
     * remove a card by its x, y coordinates
     * @param cardNodeX x index from 0 to width-1 of card to be removed
     * @param cardNodeY y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public Buildings convertCardToBuildingByCoordinates(String cardType, int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        
        Buildings newBuilding = null;

        Card card = getCardByCordinates(cardNodeX, cardNodeY);
        assert(card != null);

        if (buildingConditions(cardType, Integer.valueOf(buildingNodeX), Integer.valueOf(buildingNodeY))) {
            newBuilding = Factory.buildingsFactory(cardType, new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
            buildings.add(newBuilding);

            // destroy the card
            card.destroy();
            cardEntities.remove(card);
            // Give rewards to the character.
            shiftCardsDownFromXCoordinate(cardNodeX);   
        }


        return newBuilding;
    }

    private Card getCardByCordinates(int x, int y) {
        Card card = null;
        for (Card c: cardEntities){
            if ((c.getX() == x) && (c.getY() == y)){
                card = c;
                break;
            }
        }

        return card;
    }

    /////////////////////////////////////////////////////////////////////////////////
    /////////////////        UNEQUIPPED ADDING FUNCTIONS       //////////////////////
    /////////////////////////////////////////////////////////////////////////////////


    public RareItems addUnequippedRareItem(RareItems item) {
        
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForRareItem();

        if (firstAvailableSlot == null){
            // eject the oldest unequipped item and replace it... oldest item is that at beginning of items
            removeRareItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForRareItem();
        }
        
        item.setX(firstAvailableSlot.getValue0());
        item.setY(firstAvailableSlot.getValue1());

        unequippedRareItems.add(item);

        return item;
    }

    public void addUnequippedItem(Items item) {
            
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null){
            // eject the oldest unequipped item and replace it... 
            // oldest item is that at beginning of items
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }

        item.setX(firstAvailableSlot.getValue0());
        item.setY(firstAvailableSlot.getValue1());

        unequippedInventoryItems.add((BasicItems) item);    

    }


    public void addUnequippedCard(Card card) {

        if (cardEntities.size() >= getWidth()){
            removeCard(0);
        }

        card.setX(cardEntities.size());
        card.setY(0);

        cardEntities.add(card);

    }

    public boolean buildingConditions(String cardType, Integer x, Integer y) {

        if (cardType.equals("VampireCastleCard") || cardType.equals("ZombiePitCard") || cardType.equals("TowerCard")) {
                
            if (!isPathTile(x, y) && getAdjacentPathTile(x, y) != -1)
                    return true;

        } else if (cardType.equals("VillageCard") || cardType.equals("BarracksCard") || cardType.equals("TrapCard")) {

            if (isPathTile(x, y))
                return true;

        } else if (cardType.equals("CampfireCard")) {

            if (!isPathTile(x, y))
                return true;
        }

        return false;
    }

    
    // making a function to add enemy just for testing
    public void addEnemyForTest(Enemy e){
        enemies.add(e);
    }

    // Need to update this function for sure and need to find another way to use this.
    public void buildingsRole() {
        
        TrapBuilding trapToBeRemoved = null;

        for (Buildings building: buildings) {

            String type = building.getType();
            ArrayList<Enemy> killedEnemies = new ArrayList<Enemy>();

            switch (type) {
                
                case "Tower":
                    TowerBuilding tower = (TowerBuilding) building;
                    
                    killedEnemies = tower.performAction(enemies, killedEnemies);
                    
                    break;
                
                case "Trap":
                    TrapBuilding trap = (TrapBuilding) building;
                    
                    killedEnemies = trap.performAction(enemies, killedEnemies);               
                    
                    if (trap.getDamageDone()) {
                        trapToBeRemoved = trap;
                    }
                    
                    break;
                
                case "Campfire":
                    CampfireBuilding campfire = (CampfireBuilding) building;
                    
                    enemies = campfire.performAction(character, enemies);
    
                    break;
                
                case "Village":
                    VillageBuilding village = (VillageBuilding) building;
                    village.refillCharacterHealth(character);
                    break;
                
                case "Barracks":
                    BarracksBuilding barrack = (BarracksBuilding) building;
                    
                    int index = getPathTile(barrack.getX(), barrack.getY());
                    assert (index != -1);
                    
                    ally = barrack.performAction(character, index, ally, orderedPath);
                    break;
                
                default:
                    break;
            }

            for (Enemy e : killedEnemies) {
                killEnemy(e);
            }
        }
        
        if (trapToBeRemoved != null) {
            trapToBeRemoved.destroy();
            buildings.remove(trapToBeRemoved);
        }
    }

    // Used for testing.
    public List<Pair<Integer, Integer>> getOrderedPath() {
        return this.orderedPath;
    }


    // RARE ITEMS ADD FUNCTIONS

    public void removeUnequippedInventoryRareItemByCoordinates(int x, int y){
        Entity item = getUnequippedRareInventoryItemEntityByCoordinates(x, y);
        removeUnequippedInventoryRareItem(item);
    }

    /**
     * remove an item from the unequipped inventory
     * @param item item to be removed
     */
    private void removeUnequippedInventoryRareItem(Entity item){
        item.destroy();
        unequippedRareItems.remove(item);
    }

    /**
     * return an unequipped inventory item by x and y coordinates
     * assumes that no 2 unequipped inventory items share x and y coordinates
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return unequipped inventory item at the input position
     */
    private Entity getUnequippedRareInventoryItemEntityByCoordinates(int x, int y){
        for (Entity e: unequippedRareItems){
            if ((e.getX() == x) && (e.getY() == y)){
                return e;
            }
        }
        return null;
    }

    /**
     * remove item at a particular index in the unequipped inventory items list (this is ordered based on age in the starter code)
     * @param index index from 0 to length-1
     */
    private void removeRareItemByPositionInUnequippedInventoryItems(int index){
        Entity item = unequippedRareItems.get(index);
        item.destroy();
        unequippedRareItems.remove(index);
    }

    /**
     * get the first pair of x,y coordinates which don't have any items in it in the unequipped inventory
     * @return x,y coordinate pair
     */

    public Pair<Integer, Integer> getFirstAvailableSlotForRareItem(){
        // first available slot for an item...
        // IMPORTANT - have to check by y then x, since trying to find first available slot defined by looking row by row
        for (int y=0; y<rareunEquippedInventoryHeight; y++){
            for (int x=0; x<rareunEquippedInventoryWidth; x++){
                if (getUnequippedRareInventoryItemEntityByCoordinates(x, y) == null){
                    return new Pair<Integer, Integer>(x, y);
                }
            }
        }
        return null;
    }

    public List<RareItems> getRareItemsGiven() {
        return rareItemsGiven;
    }

    public TheOneRing addTheOneRing(){

        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForRareItem();
        if (firstAvailableSlot == null){
            // eject the oldest unequipped item and replace it... oldest item is that at beginning of items
            removeRareItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForRareItem();
        }
        
        // now we insert the new sword, as we know we have at least made a slot available...
        TheOneRing oneRing = new TheOneRing(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedRareItems.add(oneRing);
        loadedOneRing = true;
        return oneRing;
    }


    public List<HealthPotion> getHealthPotionOnPath() {
        return healthPotionOnPath;
    }

    public void addHealthPotionOnPath(HealthPotion hp) {
        healthPotionOnPath.add(hp);
    }
}
