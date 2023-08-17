package unsw.loopmania;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

public abstract class ShopMenuController {

    public Character character;
    public LoopManiaWorldController worldController;
    public MenuSwitcher gameSwitcher;
    private Items itemSelected;


    public ShopMenuController(Character character, LoopManiaWorldController worldController) {
        this.character = character;
        this.worldController = worldController;
        goldValue = new Label();
        healthValue = new Label();
        boom = new Label();
        swordPrice = new Label();
        stakePrice = new Label();
        staffPrice = new Label();
        helmetPrice = new Label();
        armourPrice = new Label();
        doggieCoinPrice = new Label();
        shieldPrice = new Label();
        healthPotionPrice = new Label();
        modeNotification = new Label();
        charXp = new Label();
        gameMode = new Label();

        swordImageOn = new ImageView();
        stakeImageOn = new ImageView();
        staffImageOn = new ImageView();
        shieldImageOn = new ImageView();
        helmetImageOn = new ImageView();
        armourImageOn = new ImageView();
        healthPotionImageOn = new ImageView();
        goldView = new ImageView();
        heartView = new ImageView();
        doggieCoinView = new ImageView();

        inventory = new GridPane();
        itemSelected = null;
    }

    @FXML
    private ImageView swordImageOn;

    @FXML
    private ImageView staffImageOn;

    @FXML
    private ImageView stakeImageOn;

    @FXML
    private ImageView shieldImageOn;

    @FXML
    private ImageView helmetImageOn;

    @FXML
    private ImageView armourImageOn;

    @FXML
    private ImageView healthPotionImageOn;

    @FXML
    private ImageView goldImageOn;

    @FXML
    private Label goldValue;
    
    @FXML
    private Label healthValue;

    @FXML
    private Label swordPrice;

    @FXML
    private Label stakePrice;

    @FXML
    private Label staffPrice;

    @FXML
    private Label helmetPrice;

    @FXML
    private Label armourPrice;

    @FXML
    private Label shieldPrice;

    @FXML
    private Label healthPotionPrice;

    @FXML
    public Label modeNotification;

    @FXML
    private ProgressBar healthBar;

    @FXML
    private Label experienceText;

    @FXML
    private Label charXp;

    @FXML
    private ImageView heartView;

    @FXML
    private ImageView goldView;

    @FXML
    private ImageView doggieCoinView;

    @FXML
    private Label doggieCoinPrice;

    @FXML
    private Label gameMode;

    @FXML
    private GridPane inventory;

    @FXML
    private Label boom;

    @FXML
    public abstract void handleBuy(ActionEvent event);

    @FXML
    public void handleSell(ActionEvent event) {

        if (getItemSelected() == null)
            return;
       
        Items itemToRemove = null;
        for (BasicItems item : worldController.getUnequippedItems()) {
            if (item.getClass() == itemSelected.getClass()) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove != null) {
            character.ReceiveGoldItems((int)itemToRemove.getPrice().get());
            worldController.removeItemByCoordinates(itemToRemove.getX(), itemToRemove.getY());
        }

        updatingInvenotory();

    }

    public void updatingInvenotory() {
        
        for (int x=0; x<LoopManiaWorld.unequippedInventoryWidth; x++){
            for (int y=0; y<LoopManiaWorld.unequippedInventoryHeight; y++){
                ImageView emptySlotView = new ImageView(worldController.getInventorySlotImage());
                inventory.add(emptySlotView, x, y);
            }
        }

        int x = 0;
        int y = 0;
        List<BasicItems> unequippedInventory = worldController.getUnequippedItems();
        for (BasicItems item : unequippedInventory) {
            if (x == 4) {
                y++;
                x = 0;
            }
            
            ImageView view = new ImageView(worldController.getItemImage(item.getType()));
            inventory.add(view, x, y);
            x++;
        }

    }

    @FXML
    public void updateInventory(ActionEvent event) {
        updatingInvenotory();

        if (worldController.getGameMode().get().equals("Confusing Mode")) {
            RotateTransition rotateHeart = new RotateTransition(Duration.seconds(50), heartView);
            rotateHeart.setByAngle(3600);

            RotateTransition rotateGold = new RotateTransition(Duration.seconds(50), goldView);
            rotateGold.setByAngle(3600);

            RotateTransition rotateDoggie = new RotateTransition(Duration.seconds(50), doggieCoinView);
            rotateDoggie.setByAngle(3600);

            RotateTransition rotateXP = new RotateTransition(Duration.seconds(50), experienceText);
            rotateXP.setByAngle(3600);

            rotateHeart.play();
            rotateGold.play();
            rotateXP.play();
            rotateDoggie.play();

        } else if (worldController.getGameMode().get().equals("Berserker Mode")) {
            boom.setVisible(true);
            

            TranslateTransition boomTrans = new TranslateTransition();
            boomTrans.setDuration(Duration.seconds(3));
            boomTrans.setNode(boom);
            boomTrans.setToY(-100);

            ScaleTransition scaleTrans = new ScaleTransition((Duration.seconds(3)), boom);
            scaleTrans.setToX(2);
            scaleTrans.setToY(2);

            RotateTransition rotateXP = new RotateTransition(Duration.seconds(3), boom);
            rotateXP.setByAngle(3600);

            boomTrans.play();
            scaleTrans.play();
            rotateXP.play();
        }
    }

    private void destroyDoggieCoin() {
        doggieCoinView.setVisible(false);
        doggieCoinPrice.setVisible(false);
        worldController.destroyDoggieCoin();
    }
    @FXML
    public void sellDoggieCoin(ActionEvent event) {
        DoggieCoin dg = DoggieCoin.getDoggieCoin();
        if (dg != null) {
            character.ReceiveGoldItems((int)dg.getPrice().get());
            destroyDoggieCoin();
            dg.removeDoggieCoin();
        } else {
            setDoggieCoinUnavailable();
        }
    }

    public void setGameSwitcher(MenuSwitcher gameSwitcher){
        this.gameSwitcher = gameSwitcher;
    }

    public MenuSwitcher getGameSwitcher() {
        return this.gameSwitcher;
    }

    @FXML
    public void initialize() {
        
        boom.setTextFill(Color.DARKGREEN);
        boom.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
        boom.setVisible(false);

        goldValue.textProperty().bindBidirectional(character.goldProperty(), new NumberStringConverter());
        goldValue.setTextFill(Color.DARKGOLDENROD);
        goldValue.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
        healthValue.textProperty().bindBidirectional(character.healthProperty(), new NumberStringConverter());
        healthValue.setTextFill(Color.DARKGOLDENROD);
        healthValue.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));

        gameMode.textProperty().bind(worldController.getGameMode());
        gameMode.setTextFill(Color.DARKGREEN);
        gameMode.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));

        experienceText.setTextFill(Color.PURPLE);
        experienceText.setFont(Font.font("Helvetica", FontPosture.ITALIC, 28));


        swordPrice.setText("$" + Factory.getItemPrice("Sword").toString());
        stakePrice.setText("$" + Factory.getItemPrice("Stake").toString());
        staffPrice.setText("$" + Factory.getItemPrice("Staff").toString());
        helmetPrice.setText("$" + Factory.getItemPrice("Helmet").toString());
        armourPrice.setText("$" + Factory.getItemPrice("Armour").toString());
        shieldPrice.setText("$" + Factory.getItemPrice("Shield").toString());
        healthPotionPrice.setText("$" + Factory.getItemPrice("HealthPotion").toString());

        swordImageOn.setImage(worldController.getItemImage("Sword"));
        stakeImageOn.setImage(worldController.getItemImage("Stake"));
        staffImageOn.setImage(worldController.getItemImage("Staff"));
        shieldImageOn.setImage(worldController.getItemImage("Shield"));
        helmetImageOn.setImage(worldController.getItemImage("Helmet"));
        armourImageOn.setImage(worldController.getItemImage("Armour"));
        healthPotionImageOn.setImage(worldController.getItemImage("HealthPotion"));

        healthBar.progressProperty().bind(worldController.getCharacter().healthProperty().divide(100d));

        charXp.textProperty().bindBidirectional(worldController.getCharacter().experienceProperty(),  new NumberStringConverter());
        charXp.setTextFill(Color.PURPLE);
        charXp.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));

        heartView.setImage(worldController.getHeartImage());
        goldView.setImage(worldController.getGoldImage());
        doggieCoinView.setImage(worldController.getDoggieCoinImage());
        doggieCoinPrice.setTextFill(Color.BLACK);
        doggieCoinPrice.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
        
        doggieCoinPrice.textProperty().bindBidirectional(worldController.getDoggieCoinValue(), new NumberStringConverter());

        inventory.setMaxHeight(LoopManiaWorld.unequippedInventoryHeight);
        inventory.setMaxWidth(LoopManiaWorld.unequippedInventoryWidth);

        // add the empty slot images for the unequipped inventory
        for (int x=0; x<LoopManiaWorld.unequippedInventoryWidth; x++){
            for (int y=0; y<LoopManiaWorld.unequippedInventoryHeight; y++){
                ImageView emptySlotView = new ImageView(worldController.getInventorySlotImage());
                inventory.add(emptySlotView, x, y);
            }
        }
    }

    public Items getItemSelected() {
        return this.itemSelected;
    }

    @FXML
    public void setItemSelected(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        this.itemSelected = (Items) Factory.getItem(sourceButton.getId(), 0, 0);
    }

    public int getItemPrice() {
        return (int) itemSelected.getPrice().get();
    }

    public void setDoggieCoinUnavailable() {
        modeNotification.setText("Doggie coin unavialble");
        modeNotification.setTextFill(Color.RED);
        modeNotification.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
    }

}