package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;

import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

import java.util.EnumMap;

import java.io.File;
import java.io.IOException;


/**
 * the draggable types.
 * If you add more draggable types, add an enum value here.
 * This is so we can see what type is being dragged.
 */
enum DRAGGABLE_TYPE{
    CARD,
    ITEM
}

/**
 * A JavaFX controller for the world.
 * 
 * All event handlers and the timeline in JavaFX run on the JavaFX application thread:
 *     https://examples.javacodegeeks.com/desktop-java/javafx/javafx-concurrency-example/
 *     Note in https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Application.html under heading "Threading", it specifies animation timelines are run in the application thread.
 * This means that the starter code does not need locks (mutexes) for resources shared between the timeline KeyFrame, and all of the  event handlers (including between different event handlers).
 * This will make the game easier for you to implement. However, if you add time-consuming processes to this, the game may lag or become choppy.
 * 
 * If you need to implement time-consuming processes, we recommend:
 *     using Task https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Task.html by itself or within a Service https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Service.html
 * 
 *     Tasks ensure that any changes to public properties, change notifications for errors or cancellation, event handlers, and states occur on the JavaFX Application thread,
 *         so is a better alternative to using a basic Java Thread: https://docs.oracle.com/javafx/2/threads/jfxpub-threads.htm
 *     The Service class is used for executing/reusing tasks. You can run tasks without Service, however, if you don't need to reuse it.
 *
 * If you implement time-consuming processes in a Task or thread, you may need to implement locks on resources shared with the application thread (i.e. Timeline KeyFrame and drag Event handlers).
 * You can check whether code is running on the JavaFX application thread by running the helper method printThreadingNotes in this class.
 * 
 * NOTE: http://tutorials.jenkov.com/javafx/concurrency.html and https://www.developer.com/design/multithreading-in-javafx/#:~:text=JavaFX%20has%20a%20unique%20set,in%20the%20JavaFX%20Application%20Thread.
 * 
 * If you need to delay some code but it is not long-running, consider using Platform.runLater https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Platform.html#runLater(java.lang.Runnable)
 *     This is run on the JavaFX application thread when it has enough time.
 */
public class LoopManiaWorldController {

    /**
     * squares gridpane includes path images, enemies, character, empty grass, buildings
     */
    @FXML
    private GridPane squares;

    /**
     * cards gridpane includes cards and the ground underneath the cards
     */
    @FXML
    private GridPane cards;

    /**
     * anchorPaneRoot is the "background". It is useful since anchorPaneRoot stretches over the entire game world,
     * so we can detect dragging of cards/items over this and accordingly update DragIcon coordinates
     */
    @FXML
    private AnchorPane anchorPaneRoot;

    /**
     * equippedItems gridpane is for equipped items (e.g. swords, shield, axe)
     */
    @FXML
    private GridPane equippedItems;

    @FXML
    private GridPane unequippedInventory;

    @FXML
    private GridPane unequippedRareInventory;

    @FXML
    private ImageView alliedSoldierImage;

    @FXML
    private ImageView heartView;

    @FXML
    private ImageView goldView;

    @FXML
    private Label numberOfAlliedSoldiers;

    @FXML
    private Label goldValue;

    @FXML
    private Label experienceText;

    @FXML
    private Label charXp;

    @FXML
    private ProgressBar healthBar;

    @FXML
    private ImageView DoggieCoinImage;

    @FXML
    private Label DoggieCoinPrice;

    // all image views including tiles, character, enemies, cards... even though cards in separate gridpane...
    private List<ImageView> entityImages;

    /**
     * when we drag a card/item, the picture for whatever we're dragging is set here and we actually drag this node
     */
    private DragIcon draggedEntity;

    private boolean isPaused;
    private LoopManiaWorld world;

    /**
     * runs the periodic game logic - second-by-second moving of character through maze, as well as enemies, and running of battles
     */
    private Timeline timeline;

    /**
     * the image currently being dragged, if there is one, otherwise null.
     * Holding the ImageView being dragged allows us to spawn it again in the drop location if appropriate.
     */
    private ImageView currentlyDraggedImage;
    
    /**
     * null if nothing being dragged, or the type of item being dragged
     */
    private DRAGGABLE_TYPE currentlyDraggedType;

    //private TYPE currentType;

    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dropped over its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged over the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragOver;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dropped in the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged into the boundaries of its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragEntered;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged outside of the boundaries of its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragExited;

    /**
     * object handling switching to the main menu
     */
    private MenuSwitcher mainMenuSwitcher;

    private MenuSwitcher shoppingMenuSwitcher;
    private MenuSwitcher survivalMenuSwitcher;
    private MenuSwitcher berserkerMenuSwitcher;
    private MenuSwitcher gameMainMenuSwitcher;
    private MenuSwitcher gameOverSwitcher;


    
    private int currentCycle = 0;


    private Image vampireCastleCardImage;
    private Image zombiePitCardImage;
    private Image vampireImage;
    private Image vampireCastleBuilding;
    private Image healthPotion;
    private Image gold;
    private Image towerCardImage;
    private Image villageCardImage;
    private Image barracksCardImage;
    private Image trapCardImage;
    private Image campfireCardImage;
    private Image stakeImage;
    private Image staffImage;
    private Image swordImage;
    private Image shieldImage;
    private Image armourImage;
    private Image helmetImage;
    private Image zombieImage;
    private Image doggieImage;
    private Image elanMuskeImage;
    private Image doggieCoinImage;
    private Image heart;
    private Image experiencePoints;
    private Image oneRingImage;
    private Image TreeStumpimage;
    private Image AndurilFlameOfTheWestImage;
    private Image allySoldierImage;
    private Image missileImage;
    private Image chickenWingsImage;
    private Image ramenImage;
    private Image dragonImage;
    private Image characterImage;
    private Image slug;

    private Image trapBuildingImage;
    private Image towerBuildingImage;
    private Image zombieBuildingImage;
    private Image villageBuildingImage;
    private Image CampfireBuildingImage;
    private Image barracksBuildingImage;

    private Image heartImage;
    private Image goldImage;
    private Image xpImage;

    private DoubleProperty doggieCoinCurrentPrice;
    private StringProperty gameMode;


    /**
     * @param world world object loaded from file
     * @param initialEntities the initial JavaFX nodes (ImageViews) which should be loaded into the GUI
     */
    public LoopManiaWorldController(LoopManiaWorld world, List<ImageView> initialEntities) {
        this.world = world;
        entityImages = new ArrayList<>(initialEntities);

        currentlyDraggedImage = null;
        currentlyDraggedType = null;
        gameMode = new SimpleStringProperty("");

        // initialize them all...
        gridPaneSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragOver = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragEntered = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragExited = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);

        vampireCastleCardImage = new Image((new File("src/images/vampire_castle_card.png")).toURI().toString());
        zombiePitCardImage = new Image((new File("src/images/zombie_pit_card.png")).toURI().toString());
        vampireImage = new Image((new File("src/images/vampire.png")).toURI().toString());
        doggieImage = new Image((new File("src/images/doggie.png")).toURI().toString());
        elanMuskeImage = new Image((new File("src/images/ElanMuske.png")).toURI().toString());
        doggieCoinImage = new Image((new File("src/images/doggiecoin.png")).toURI().toString());
        slug = new Image((new File("src/images/slug.png")).toURI().toString());
        vampireCastleBuilding = new Image((new File("src/images/vampire_castle_building_purple_background.png")).toURI().toString());
        healthPotion = new Image((new File("src/images/brilliant_blue_new.png")).toURI().toString());
        gold = new Image((new File("src/images/gold_pile.png")).toURI().toString());
        towerCardImage = new Image((new File("src/images/tower_card.png")).toURI().toString());
        villageCardImage = new Image((new File("src/images/village_card.png")).toURI().toString());
        barracksCardImage = new Image((new File("src/images/barracks_card.png")).toURI().toString());
        trapCardImage = new Image((new File("src/images/trap_card.png")).toURI().toString());
        campfireCardImage = new Image((new File("src/images/campfire_card.png")).toURI().toString());
        stakeImage = new Image((new File("src/images/stake.png")).toURI().toString());
        staffImage = new Image((new File("src/images/staff.png")).toURI().toString());
        swordImage = new Image((new File("src/images/basic_sword.png")).toURI().toString());
        shieldImage = new Image((new File("src/images/shield.png")).toURI().toString());
        armourImage = new Image((new File("src/images/armour.png")).toURI().toString());
        helmetImage = new Image((new File("src/images/helmet.png")).toURI().toString());
        zombieImage = new Image((new File("src/images/zombie.png")).toURI().toString());
        trapBuildingImage = new Image((new File("src/images/trap.png")).toURI().toString());
        towerBuildingImage = new Image((new File("src/images/tower.png")).toURI().toString());
        zombieBuildingImage = new Image((new File("src/images/zombie_pit.png")).toURI().toString());
        villageBuildingImage = new Image((new File("src/images/village.png")).toURI().toString());
        CampfireBuildingImage = new Image((new File("src/images/campfire.png")).toURI().toString());
        barracksBuildingImage = new Image((new File("src/images/barracks.png")).toURI().toString());
        heart = new Image((new File("src/images/heart.png")).toURI().toString());
        experiencePoints = new Image((new File("src/images/experienePoints.png")).toURI().toString());
        allySoldierImage = new Image((new File("src/images/deep_elf_master_archer.png")).toURI().toString());
        oneRingImage = new Image((new File("src/images/the_one_ring.png")).toURI().toString());;
        TreeStumpimage = new Image((new File("src/images/tree_stump.png")).toURI().toString());;
        AndurilFlameOfTheWestImage = new Image((new File("src/images/anduril_flame_of_the_west.png")).toURI().toString());
        missileImage = new Image((new File("src/images/missile.png")).toURI().toString());
        chickenWingsImage = new Image((new File("src/images/chicken_wings.png")).toURI().toString());
        ramenImage = new Image((new File("src/images/ramen.png")).toURI().toString());
        dragonImage = new Image((new File("src/images/dragon.png")).toURI().toString());
        characterImage = new Image((new File("src/images/human_new.png")).toURI().toString());

        numberOfAlliedSoldiers = new Label();
        goldValue = new Label();
        experienceText = new Label("XP");
        charXp = new Label();
        heartImage = new Image((new File("src/images/heart.png")).toURI().toString());
        goldImage = new Image((new File("src/images/gold_pile.png")).toURI().toString());
        xpImage = new Image((new File("src/images/deep_elf_master_archer.png")).toURI().toString());

        doggieCoinCurrentPrice = new SimpleDoubleProperty(0);
    }

    /**
     * Set the game mode
     * @param mode
     */
    public void setGameMode(String mode) {
        gameMode.set(mode);
    }

    /**
     * Get inventory slot image
     * @return image of blank inventory slot
     */
    public Image getInventorySlotImage() {
        Image inventorySlotImage = new Image((new File("src/images/empty_slot.png")).toURI().toString());
        return inventorySlotImage;
    }

    public StringProperty getGameMode() {
        return gameMode;
    }

    /**
     * Get character from world
     * @return character
     */
    public Character getCharacter() {
        return world.getCharacter();
    }

    /**
     * Get unequipped items from world
     * @return unerquippedItems
     */
    public List<BasicItems> getUnequippedItems() {
        return world.getUnequippedItems();
    }

    /**
     * Get heart image
     * @return image of heart
     */
    public Image getHeartImage() {
        return heartImage;
    }

    /**
     * Get heart image
     * @return image of heart
     */
    public Image getGoldImage() {
        return goldImage;
    }

    /**
     * Get doggieCoin image
     * @return image of doggieCoin
     */
    public Image getDoggieCoinImage() {
        return doggieCoinImage;
    }

    /**
     * Destroy doggie coin
     */
    public void destroyDoggieCoin() {
        DoggieCoinImage.setVisible(false);
        DoggieCoinPrice.setVisible(false);
    }


    @FXML
    public void initialize() {
        Image pathTilesImage = new Image((new File("src/images/32x32GrassAndDirtPath.png")).toURI().toString());
        Image inventorySlotImage = new Image((new File("src/images/empty_slot.png")).toURI().toString());

        Image HerosCastleImage = new Image((new File("src/images/heros_castle.png")).toURI().toString());

        Rectangle2D imagePart = new Rectangle2D(0, 0, 32, 32);

        // Add the ground first so it is below all other entities (inculding all the twists and turns)
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                ImageView groundView = new ImageView(pathTilesImage);
                groundView.setViewport(imagePart);
                squares.add(groundView, x, y);
            }
        }

        // load entities loaded from the file in the loader into the squares gridpane
        for (ImageView entity : entityImages){
            squares.getChildren().add(entity);
        }
        
        // add the ground underneath the cards
        for (int x=0; x<world.getWidth(); x++){
            ImageView groundView = new ImageView(pathTilesImage);
            groundView.setViewport(imagePart);
            cards.add(groundView, x, 0);
        }

        // add the empty slot images for the unequipped inventory
        for (int x=0; x<LoopManiaWorld.unequippedInventoryWidth; x++){
            for (int y=0; y<LoopManiaWorld.unequippedInventoryHeight; y++){
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        // add the empty slot images for the unequipped inventory
        for (int x=0; x<LoopManiaWorld.rareunEquippedInventoryWidth; x++){
            for (int y=0; y<LoopManiaWorld.rareunEquippedInventoryHeight; y++){
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                unequippedRareInventory.add(emptySlotView, x, y);
            }
        }

        // Load Heros Castle's
        ImageView HerosCastleView = new ImageView(HerosCastleImage);
        HerosCastleView.setViewport(imagePart);
        squares.add(HerosCastleView, 0, 0);

        // create the draggable icon
        draggedEntity = new DragIcon();
        draggedEntity.setVisible(false);
        draggedEntity.setOpacity(0.7);
        anchorPaneRoot.getChildren().add(draggedEntity);

        // Load allied soldier image
        alliedSoldierImage.setImage(allySoldierImage);
        heartView.setImage(heartImage);
        goldView.setImage(goldImage);

        // Get number of allied soldiers
        numberOfAlliedSoldiers.textProperty().bindBidirectional(world.getNumAllies(), new NumberStringConverter());
        numberOfAlliedSoldiers.setTextFill(Color.BLACK);
        numberOfAlliedSoldiers.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));

        // Get progressbar for health
        healthBar.progressProperty().bind(getCharacter().healthProperty().divide(100d));

        goldValue.textProperty().bindBidirectional(getCharacter().goldProperty(), new NumberStringConverter());
        goldValue.setTextFill(Color.DARKGOLDENROD);
        goldValue.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));

        experienceText.setTextFill(Color.PURPLE);
        experienceText.setFont(Font.font("Helvetica", FontPosture.ITALIC, 28));

        charXp.textProperty().bindBidirectional(getCharacter().experienceProperty(),  new NumberStringConverter());
        charXp.setTextFill(Color.PURPLE);
        charXp.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
    }

    /**
     * create and run the timer
     */
    public void startTimer(){
        System.out.println("starting timer");
        isPaused = false;

        SimpleIntegerProperty castlHeroCoordinates = new SimpleIntegerProperty();
        castlHeroCoordinates.add(0);
        Buildings herosCastle = new HerosCastleBuilding(castlHeroCoordinates, castlHeroCoordinates);
        onLoad(herosCastle);

        // trigger adding code to process main game logic to queue. JavaFX will target framerate of 0.3 seconds
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), event -> {
            
            world.runTickMoves();
            world.updateNumAllies();
            try {
                switchToShoppingMenu();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            // check goals
            GoalCondtions checkGoals = world.getCharacterGoals();
            if(checkGoals.evaluateOverallGoal(world.getCharacter())){
                System.out.println("==========================CHARACTER WIN==========");
                // Character
            }

            Missile missile = world.spawnMissile();
            if(missile != null){
                onLoad(missile);
            }

            ChickenWings chickenWings = world.spawnChickenWings(currentCycle);
            if(chickenWings != null){
                onLoad(chickenWings);
            }

            world.takeChickenWings();

            Ramen ramen = world.spawnRamen(currentCycle);
            if(ramen != null){
               onLoad(ramen); 
            }

            Dragon dragon = world.takeRamen();
            if(dragon != null){
                onLoad(dragon);
            }

            List<Enemy> defeatedEnemies = world.runBattles();
            checkGameOver();
            for (Enemy e: defeatedEnemies){
                reactToEnemyDefeat(e);
            }

            //checkLoadRareItems();
            checkDoggieCoin();

            List<Enemy> newEnemies = world.possiblySpawnEnemies(currentCycle);
            for (Enemy newEnemy: newEnemies){
                onLoad(newEnemy);
            }

            List<Gold> essentialItems = world.takeGold();
            for(EssentialItems item: essentialItems){
                onLoad(item);
            }
            List<HealthPotion> healthItems = world.takeHealthPotions();
            for(EssentialItems item:healthItems){
                onLoad(item);
            }

            world.buildingsRole();

            printThreadingNotes("HANDLED TIMER");
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * pause the execution of the game loop
     * the human player can still drag and drop items during the game pause
     */
    public void pause(){
        isPaused = true;
        System.out.println("pausing");
        timeline.stop();
    }

    /**
     * Check if the game is over
     */
    public void checkGameOver() {
        if (world.getCharacter().getHealth() <= 0) {
            timeline.stop();
            this.gameOverSwitcher.switchMenu();

        }
    }    
    
    /**
     * Terminate the game
     */
    public void terminate(){
        pause();
    }

    /**
     * Check which rare item is to be loaded
     */
    public void checkLoadRareItems() {
        
        int battlesWon = world.getCharacter().getBattlesWon();
        System.out.print(battlesWon);
        int sizeRareItems = world.getRareItems().size();
        System.out.print(sizeRareItems);

        int numUnequipeedRareItems = world.getUnequippedRareItems().size();
        System.out.print(numUnequipeedRareItems);

        if (battlesWon > 0 && sizeRareItems > 0 && (numUnequipeedRareItems < sizeRareItems)) {
            int rareItemCheck = (battlesWon % world.getRareItems().size());
            if (rareItemCheck == 0) {
                RareItems rareItem = loadRareItem();
                if (getGameMode().get().equals("Confusing Mode")) {
                    RareItems confusionRareItem = world.getRandomRareItem();
                    rareItem.setConfusingModeItem(confusionRareItem);
                }
            }
        }
    }

    // Loading the One ring using the load rare item semantics and probably loading 
    // the other rare items are normal items os that they can be added in the other 
    // invenotry but loaded in the rare item unequipped inventory.
    public RareItems loadRareItem() {
        
        List<RareItems> rareItems = new ArrayList<RareItems>(); 
        List<RareItems> rareItemsLoaded = world.getRareItems();
        
        TheOneRing to = new TheOneRing (new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        TreeStump ts = new TreeStump( new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        AndurilFlameOfTheWest af = new AndurilFlameOfTheWest( new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));

        for ( int i = 0 ; i < rareItemsLoaded.size(); i++) {
            if (rareItemsLoaded.get(i) instanceof TheOneRing) {
                rareItems.add(to);   
            } else if (rareItemsLoaded.get(i) instanceof TreeStump) {
                rareItems.add(ts);
            } else if (rareItemsLoaded.get(i) instanceof TheOneRing) {
                rareItems.add(af);
            }
        }
        
        int rand = (int)Math.floor(Math.random()*(rareItems.size()-0+0)+0);   
        RareItems rareItem = rareItems.get(rand);
        world.addUnequippedRareItem(rareItem);
        onLoad(rareItem);
        return rareItem;              
    }


    /**
     * pair the entity an view so that the view copies the movements of the entity.
     * add view to list of entity images
     * @param entity backend entity to be paired with view
     * @param view frontend imageview to be paired with backend entity
     */
    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entityImages.add(view);
    }

    /**
     * Check if doggieCoin is available
     */
    private void checkDoggieCoin() {
        DoggieCoin dg = DoggieCoin.getDoggieCoin();
        if (dg != null) {
            doggieCoinCurrentPrice.set(dg.getPrice().get());
            DoggieCoinImage.setImage(doggieCoinImage);
            DoggieCoinPrice.textProperty().bindBidirectional(dg.getPrice(), new NumberStringConverter());
            DoggieCoinPrice.setTextFill(Color.BLACK);
            DoggieCoinPrice.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
        }
    }

    /**
     * Get doggie coin
     * @return
     */
    public DoubleProperty getDoggieCoinValue() {
        return doggieCoinCurrentPrice;
    }

    /**
     * run GUI events after an enemy is defeated, such as spawning items/experience/gold
     * @param enemy defeated enemy for which we should react to the death of
     */
    private void reactToEnemyDefeat(Enemy enemy){
        // react to character defeating an enemy
        // in starter code, spawning extra card/weapon...

        GainsOnDefeat god = enemy.getGainsOnDefeat();
        Character character = world.getCharacter();
        character.setExperience(god.getExperiencePoint());
        character.ReceiveGoldItems(god.getGold());

        Items item = god.getItem();
        Card card = god.getCard();

        addUnequippedItem(item);
        addUnequippedCard(card);
        checkLoadRareItems();
       
    }

    /**
     * Equip items
     * @param item
     */
    public void addUnequippedItem(Items item) {
        world.addUnequippedItem(item);
        onLoad(item);
    }


    private void addUnequippedCard(Card card) {
        world.addUnequippedCard(card);
        onLoad(card);
    }


    /**
     * load a vampire castle card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * @param vampireCastleCard
     */
    private void onLoad(Card card) {
        ImageView view = new ImageView(getCardImage(card.getType()));

        // FROM https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, card, cards, squares);

        addEntity(card, view);
        cards.getChildren().add(view);
    }

    /**
     * Load item
     */
    private void onLoad(Items item) {
        
        ImageView view = new ImageView(getItemImage(item.getType()));
        GridPane inventoryReq = (item instanceof BasicItems) ? unequippedInventory : unequippedRareInventory;
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, item, inventoryReq, equippedItems);

        addEntity(item, view);
        if (item instanceof RareItems) {
            unequippedRareInventory.getChildren().add(view);
        } else {
            unequippedInventory.getChildren().add(view);
        }
    }

    
    // // Need to have a look at it and change it.
    // private void onLoad(TheOneRing oneRing) {
    //     ImageView view = new ImageView(oneRingImage);
    //     addEntity(oneRing, view);
    //     unequippedRareInventory.getChildren().add(view);
    // }

    private void onLoad(Dragon dragon){
        ImageView view = new ImageView(dragonImage);
        addEntity(dragon, view);
        squares.getChildren().add(view);
    }

    private void onLoad(Ramen ramen){
        ImageView view = new ImageView(ramenImage);
        addEntity(ramen, view);
        squares.getChildren().add(view);
    }

    private void onLoad(ChickenWings chickenWings){
        ImageView view = new ImageView(chickenWingsImage);
        addEntity(chickenWings, view);
        squares.getChildren().add(view);
    }

    private void onLoad(Missile missile){
        ImageView view = new ImageView(missileImage);
        addEntity(missile, view);
        squares.getChildren().add(view);
    }
    
    /**
     * load an enemy into the GUI
     * @param enemy
     */
    public void onLoad(Enemy enemy) {
        ImageView view = new ImageView(getEnemyImage(enemy.getType()));
        addEntity(enemy, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * @param building
     */
    private void onLoad(Buildings building){
        ImageView view = new ImageView(getBuildingImage(building.getType()));
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    private void onLoad(EssentialItems items){
        ImageView view = new ImageView(getItemImage(items.getType()));
        addEntity(items, view);
        squares.getChildren().add(view);
    }

    /**
     * Get enemy image
     * @param image
     * @return
     */
    public Image getEnemyImage(String image) {
        switch(image) {
            case "Vampire":
                return vampireImage;

            case "Zombie":
                return zombieImage;
            
            case "Doggie":

                return doggieImage;
            
            case "ElanMuske":
                return elanMuskeImage;
            
            case "Slug":
                return slug;

            default:
                return null;
        }
    }

    /**
     * Get building image
     * @param image
     * @return
     */
    public Image getBuildingImage(String image) {
        switch(image) {

            case "Trap":
                return trapBuildingImage;

            case "Tower":
                return towerBuildingImage;

            case "ZombiePit":
                return zombieBuildingImage;

            case "vampiresCastleBuilding":
                return vampireCastleBuilding;

            case "Village":
                return villageBuildingImage;

            case "Campfire":
                return CampfireBuildingImage;

            case "Barracks":
                return barracksBuildingImage;

            default:
                return null;
        }
    }

    /**
     * add drag event handlers for dropping into gridpanes, dragging over the background, dropping over the background.
     * These are not attached to invidual items such as swords/cards.
     * @param draggableType the type being dragged - card or item
     * @param sourceGridPane the gridpane being dragged from
     * @param targetGridPane the gridpane the human player should be dragging to (but we of course cannot guarantee they will do so)
     */
    private void buildNonEntityDragHandlers(DRAGGABLE_TYPE draggableType, StaticEntity moved, GridPane sourceGridPane, GridPane targetGridPane){
        // for example, in the specification, villages can only be dropped on path, whilst vampire castles cannot go on the path

        gridPaneSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /*
                 *you might want to design the application so dropping at an invalid location drops at the most recent valid location hovered over,
                 * or simply allow the card/item to return to its slot (the latter is easier, as you won't have to store the last valid drop location!)
                 */
                if (currentlyDraggedType == draggableType){
                    // problem = event is drop completed is false when should be true...
                    // https://bugs.openjdk.java.net/browse/JDK-8117019
                    // putting drop completed at start not making complete on VLAB...

                    //Data dropped
                    //If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if(node != targetGridPane && db.hasImage()){
                        Integer cIndex = GridPane.getColumnIndex(node);
                        Integer rIndex = GridPane.getRowIndex(node);
                        int x = cIndex == null ? 0 : cIndex;
                        int y = rIndex == null ? 0 : rIndex;
                        //Places at 0,0 - will need to take coordinates once that is implemented
                        ImageView image = new ImageView(db.getImage());

                        int nodeX = GridPane.getColumnIndex(currentlyDraggedImage);
                        int nodeY = GridPane.getRowIndex(currentlyDraggedImage);
                        switch (draggableType){
                            case CARD:
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                Buildings newBuilding = convertCardToBuildingByCoordinates(moved.getType(), nodeX, nodeY, x, y);
                                        
                                if (newBuilding != null) {
                                    onLoad(newBuilding);
                                } else {
                                    onLoad(Factory.cardFactory(moved.getType(), new SimpleIntegerProperty(nodeX), new SimpleIntegerProperty(nodeY)));
                                }

                                break;

                            case ITEM:
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                // Can be potentially improved and made better..!! without ifs just check the type and instances
                                /// do not send in the type itself, try to do not the strings but something else here probaly just the
                                // items itself..!!
                                if (moved instanceof RareItems) {
                                    removeRareItemByCoordinates(nodeX, nodeY);
                                } else {
                                    removeItemByCoordinates(nodeX, nodeY);
                                }
                                targetGridPane.add(image, x, y, 1, 1);
                                world.equippedInventoryItemsNotify((Items) moved ,x,y);
                                break;
                            default:
                                break;
                        }
                        
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                        printThreadingNotes("DRAG DROPPED ON GRIDPANE HANDLED");
                    }
                }
                event.setDropCompleted(true);
                // consuming prevents the propagation of the event to the anchorPaneRoot (as a sub-node of anchorPaneRoot, GridPane is prioritized)
                // https://openjfx.io/javadoc/11/javafx.base/javafx/event/Event.html#consume()
                // to understand this in full detail, ask your tutor or read https://docs.oracle.com/javase/8/javafx/events-tutorial/processing.htm
                event.consume();
            }
        });

        // this doesn't fire when we drag over GridPane because in the event handler for dragging over GridPanes, we consume the event
        anchorPaneRootSetOnDragOver.put(draggableType, new EventHandler<DragEvent>(){
            // https://github.com/joelgraff/java_fx_node_link_demo/blob/master/Draggable_Node/DraggableNodeDemo/src/application/RootLayout.java#L110
            @Override
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType){
                    if(event.getGestureSource() != anchorPaneRoot && event.getDragboard().hasImage()){
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                if (currentlyDraggedType != null){
                    draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                event.consume();
            }
        });

        // this doesn't fire when we drop over GridPane because in the event handler for dropping over GridPanes, we consume the event
        anchorPaneRootSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType){
                    //Data dropped
                    //If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if(node != anchorPaneRoot && db.hasImage()){
                        //Places at 0,0 - will need to take coordinates once that is implemented
                        currentlyDraggedImage.setVisible(true);
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        removeDraggableDragEventHandlers(draggableType, targetGridPane);
                        
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                    }
                }
                //let the source know whether the image was successfully transferred and used
                event.setDropCompleted(true);
                event.consume();
            }
        });
    }

    /**
     * remove the card from the world, and spawn and return a building instead where the card was dropped
     * @param cardNodeX the x coordinate of the card which was dragged, from 0 to width-1
     * @param cardNodeY the y coordinate of the card which was dragged (in starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card, where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card, where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private Buildings convertCardToBuildingByCoordinates(String cardType, int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {

        return world.convertCardToBuildingByCoordinates(cardType, cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    public void removeRareItemByCoordinates(int nodeX, int nodeY) {
        world.removeUnequippedInventoryRareItemByCoordinates(nodeX, nodeY);
    }
    /**
     * remove an item from the unequipped inventory by its x and y coordinates in the unequipped inventory gridpane
     * @param nodeX x coordinate from 0 to unequippedInventoryWidth-1
     * @param nodeY y coordinate from 0 to unequippedInventoryHeight-1
     */
    public void removeItemByCoordinates(int nodeX, int nodeY) {
        world.removeUnequippedInventoryItemByCoordinates(nodeX, nodeY);
    }

    /**
     * add drag event handlers to an ImageView
     * @param view the view to attach drag event handlers to
     * @param draggableType the type of item being dragged - card or item
     * @param sourceGridPane the relevant gridpane from which the entity would be dragged
     * @param targetGridPane the relevant gridpane to which the entity would be dragged to
     */
    private void addDragEventHandlers(ImageView view, DRAGGABLE_TYPE draggableType, StaticEntity moved, GridPane sourceGridPane, GridPane targetGridPane){
        view.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                currentlyDraggedImage = view; // set image currently being dragged, so squares setOnDragEntered can detect it...
                currentlyDraggedType = draggableType;
                //Drag was detected, start drap-and-drop gesture
                //Allow any transfer node
                Dragboard db = view.startDragAndDrop(TransferMode.MOVE);
    
                //Put ImageView on dragboard
                ClipboardContent cbContent = new ClipboardContent();
                cbContent.putImage(view.getImage());
                db.setContent(cbContent);
                view.setVisible(false);

                buildNonEntityDragHandlers(draggableType, moved, sourceGridPane, targetGridPane);

                draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));

                switch (draggableType){
                    case CARD:
                        draggedEntity.setImage(getCardImage(moved.getType()));
                        break;
                    case ITEM:
                        draggedEntity.setImage(getItemImage(moved.getType()));
                        break;
                    default:
                        break;
                }
                
                draggedEntity.setVisible(true);
                draggedEntity.setMouseTransparent(true);
                draggedEntity.toFront();

                // IMPORTANT!!!
                // to be able to remove event handlers, need to use addEventHandler
                // https://stackoverflow.com/a/67283792
                targetGridPane.addEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

                for (Node n: targetGridPane.getChildren()){
                    // events for entering and exiting are attached to squares children because that impacts opacity change
                    // these do not affect visibility of original image...
                    // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
                    gridPaneNodeSetOnDragEntered.put(draggableType, new EventHandler<DragEvent>() {
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType){
                            //The drag-and-drop gesture entered the target
                            //show the user that it is an actual gesture target
                                if(event.getGestureSource() != n && event.getDragboard().hasImage() && checkValidTile(moved.getType(), (int)n.getLayoutX(), (int)n.getLayoutY())){
                                    n.setOpacity(0.7);
                                }
                            }
                            event.consume();
                        }
                    });
                    gridPaneNodeSetOnDragExited.put(draggableType, new EventHandler<DragEvent>() {
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType){
                                n.setOpacity(1);
                            }
                
                            event.consume();
                        }
                    });
                    n.addEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
                    n.addEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
                }
                event.consume();
            }
            
        });
    }

    private boolean checkValidTile(String cardType, int x, int y) {
        if (x == 0 && y == 0) {
            return false;
        }

        x = x / 32;
        y = y / 32;
        Integer xInteger = Integer.valueOf((x));
        Integer yInteger = Integer.valueOf((y));

        return world.buildingConditions(cardType, xInteger, yInteger);
    }

    /**
     * remove drag event handlers so that we don't process redundant events
     * this is particularly important for slower machines such as over VLAB.
     * @param draggableType either cards, or items in unequipped inventory
     * @param targetGridPane the gridpane to remove the drag event handlers from
     */
    private void removeDraggableDragEventHandlers(DRAGGABLE_TYPE draggableType, GridPane targetGridPane){
        // remove event handlers from nodes in children squares, from anchorPaneRoot, and squares
        targetGridPane.removeEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));

        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

        for (Node n: targetGridPane.getChildren()){
            n.removeEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
            n.removeEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
        }
    }

    /**
     * handle the pressing of keyboard keys.
     * Specifically, we should pause when pressing SPACE
     * @param event some keyboard key press
     */
    @FXML
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
        case SPACE:
            if (isPaused){
                startTimer();
            }
            else{
                pause();
            }
            break;
        default:
            break;
        }
    }

    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher){
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    public void setGameMainMenuSwitcher(MenuSwitcher gameMainMenuSwitcher){
        this.gameMainMenuSwitcher = gameMainMenuSwitcher;
    }

    public void setShoppingMenuSwitcher(MenuSwitcher shoppingMenuSwitcher){
        this.shoppingMenuSwitcher = shoppingMenuSwitcher;
    }

    public void setBerserkerMenuSwitcher(MenuSwitcher berserkerMenuSwitcher){
        this.berserkerMenuSwitcher = berserkerMenuSwitcher;
    }

    public void setSurvivalMenuSwitcher(MenuSwitcher survivalMenuSwitcher){
        this.survivalMenuSwitcher = survivalMenuSwitcher;
    }

    public void setGameOverSwticher(MenuSwitcher gameOverSwitcher){
        this.gameOverSwitcher = gameOverSwitcher;
    }
    /**
     * this method is triggered when click button to go to main menu in FXML
     * @throws IOException
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        pause();
        mainMenuSwitcher.switchMenu();
    }

    @FXML
    private void switchToGameMainMenu() throws IOException {
        pause();
        gameMainMenuSwitcher.switchMenu();
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     * @throws IOException
     */
    private void switchToShoppingMenu() throws IOException {
        if (world.getCharacterX() == 0 && world.getCharacterY() == 0) {
            this.currentCycle = this.currentCycle + 1;
            world.getCharacter().setNumOfCycles(currentCycle);
            int nextMenuCycle = generateSequence(currentCycle);

            if (nextMenuCycle == this.currentCycle) {
                pause();


                switch(getGameMode().get()) {
                    case "Survival Mode":
                        survivalMenuSwitcher.switchMenu();
                        break;
        
                    case "Berserker Mode":
                        berserkerMenuSwitcher.switchMenu();
                        break;
                    
                    default:
                        shoppingMenuSwitcher.switchMenu();
                        break;
                }
            }
        }
    }

    private int generateSequence(int cycle) {
        int nextMenuCycle = 0;
        for(int i = 0; nextMenuCycle < cycle; i++) {
            nextMenuCycle = (((i * i) + i)/2);
        }

        return nextMenuCycle;
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the world.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * 
     * note that this is put in the controller rather than the loader because we need to track positions of spawned entities such as enemy
     * or items which might need to be removed should be tracked here
     * 
     * NOTE teardown functions setup here also remove nodes from their GridPane. So it is vital this is handled in this Controller class
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());

        ChangeListener<Number> xListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        };
        ChangeListener<Number> yListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        };

        // if need to remove items from the equipped inventory, add code to remove from equipped inventory gridpane in the .onDetach part
        ListenerHandle handleX = ListenerHandles.createFor(entity.x(), node)
                                               .onAttach((o, l) -> o.addListener(xListener))
                                               .onDetach((o, l) -> {
                                                    o.removeListener(xListener);
                                                    entityImages.remove(node);
                                                    squares.getChildren().remove(node);
                                                    cards.getChildren().remove(node);
                                                    equippedItems.getChildren().remove(node);
                                                    unequippedInventory.getChildren().remove(node);
                                                    unequippedRareInventory.getChildren().remove(node);
                                                })
                                               .buildAttached();
        ListenerHandle handleY = ListenerHandles.createFor(entity.y(), node)
                                               .onAttach((o, l) -> o.addListener(yListener))
                                               .onDetach((o, l) -> {
                                                   o.removeListener(yListener);
                                                   entityImages.remove(node);
                                                   squares.getChildren().remove(node);
                                                   cards.getChildren().remove(node);
                                                   equippedItems.getChildren().remove(node);
                                                   unequippedInventory.getChildren().remove(node);
                                                   unequippedRareInventory.getChildren().remove(node);
                                                })
                                               .buildAttached();
        handleX.attach();
        handleY.attach();

        // this means that if we change boolean property in an entity tracked from here, position will stop being tracked
        // this wont work on character/path entities loaded from loader classes
        entity.shouldExist().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> obervable, Boolean oldValue, Boolean newValue) {
                handleX.detach();
                handleY.detach();
            }
        });
    }

    /**
     * we added this method to help with debugging so you could check your code is running on the application thread.
     * By running everything on the application thread, you will not need to worry about implementing locks, which is outside the scope of the course.
     * Always writing code running on the application thread will make the project easier, as long as you are not running time-consuming tasks.
     * We recommend only running code on the application thread, by using Timelines when you want to run multiple processes at once.
     * EventHandlers will run on the application thread.
     */
    private void printThreadingNotes(String currentMethodLabel){
        System.out.println("\n###########################################");
        System.out.println("current method = "+currentMethodLabel);
        System.out.println("In application thread? = "+Platform.isFxApplicationThread());
        System.out.println("Current system time = "+java.time.LocalDateTime.now().toString().replace('T', ' '));
        System.out.println("Character items"+ world.getCharacterItems());
        System.out.println("Character health: "+world.getCharacter().getHealth());
      
    }

    /**
     * Get card image
     * @param cardType
     * @return
     */
    public Image getCardImage(String cardType) {
        switch (cardType) {
            case "VampireCastleCard":
                return vampireCastleCardImage;
            
            case "ZombiePitCard":
                return zombiePitCardImage;

            case "TowerCard":
                return towerCardImage;

            
            case "VillageCard":
                return villageCardImage;


            case "BarracksCard":
                return barracksCardImage;


            case "TrapCard":
                return trapCardImage;


            case "CampfireCard":
                return campfireCardImage;

            case "Staff":
                return staffImage;

            case "Stake":
                return stakeImage;

            case "Sword":
                return swordImage;

            case "Vampire":
                return vampireImage;

            case "vampiresCastleBuilding":
                return vampireCastleBuilding;

            case "HealthPotion":
                return healthPotion;

            case "Gold":
                return gold;

            default:
                break;
        }

        return null;
    }

    /**
     * Get item image
     * @param itemType
     * @return
     */
    public Image getItemImage(String itemType) {
        switch (itemType) {

            case "Staff":
                return staffImage;

            case "Stake":
                return stakeImage;

            case "Sword":
                return swordImage;

            case "HealthPotion":
                return healthPotion;

            case "Shield":
                return shieldImage;

            case "Armour":
                return armourImage;

            case "Helmet":
                return helmetImage;

            case "Gold":
                return gold;
            
            case "DoggieCoin":
                return doggieCoinImage;

            case "AndurilFlameOfTheWest":
                return AndurilFlameOfTheWestImage;

            case "TreeStump":
                return TreeStumpimage;

            case "TheOneRing":
                return oneRingImage;

            default:
                break;
        }

        return null;
    }
}
