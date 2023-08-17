package unsw.loopmania;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.converter.NumberStringConverter;

public class SurvivalMenuController extends ShopMenuController {

    int numHealthPotionsBought;

    public SurvivalMenuController(Character character, LoopManiaWorldController worldController) {
        super(character, worldController);
        this.numHealthPotionsBought = 0;
    }

    
    /**
     * facilitates switching to main game upon button click
     * @throws IOException
     */
    @FXML
    private void switchToGame() throws IOException {
        numHealthPotionsBought = 0;
        this.getGameSwitcher().switchMenu();
    }


    @FXML
    public void handleBuy(ActionEvent event) {
        if (getItemSelected() == null)
            return;
        
        if (getItemPrice() > character.getGold()) {
            return;
        }

        int purchaseSuccessful = 1;
        Items selctedItem = getItemSelected();

        if (selctedItem instanceof HealthPotion) {
            if (numHealthPotionsBought > 0) {
                setModeNotification();
                return;
            }
            if (!(character.getHealth() < 100)) return;
            character.purchaseHealthPotion();
            numHealthPotionsBought += 1; 
            purchaseSuccessful = 0;
        }

        if (purchaseSuccessful == 1) {
            worldController.addUnequippedItem(selctedItem);
        }

        character.reduceGoldItems(getItemPrice());

        updatingInvenotory();
        
    }

    public void setModeNotification() {
        modeNotification.setText("Survival Mode: Can't buy more than one health potion !");
        modeNotification.setTextFill(Color.RED);
        modeNotification.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
    }
    
}
