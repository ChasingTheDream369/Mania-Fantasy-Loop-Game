package unsw.loopmania;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * controller for the main menu.
 */
public class MainMenuController {
    /**
     * facilitates switching to main game
     */
    private MenuSwitcher gameSwitcher;
    LoopManiaWorldController mainController;

    @FXML
    private Label startGameLabel;

    public MainMenuController(LoopManiaWorldController mainController) {
        this.mainController = mainController;
    }

    public void setGameSwitcher(MenuSwitcher gameSwitcher){
        this.gameSwitcher = gameSwitcher;
    }

    

    /**
     * facilitates switching to main game upon button click
     * @throws IOException
     */
    @FXML
    private void switchToGame() throws IOException {
        gameSwitcher.switchMenu();
    }

    @FXML
    public void setStandardMode(ActionEvent event) {
        this.mainController.setGameMode("Standard Mode");
    }

    @FXML
    public void setSurvivalMode(ActionEvent event) {
        this.mainController.setGameMode("Survival Mode");
    }

    @FXML
    public void setBerserkerMode(ActionEvent event) {
        this.mainController.setGameMode("Berserker Mode");
    }

    @FXML
    public void setConfusionMode(ActionEvent event) {
        this.mainController.setGameMode("Confusing Mode");
    }

    @FXML
    public void initialize() {
        this.mainController.setGameMode("Standard");
        startGameLabel.setTextFill(Color.BLACK);
        startGameLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
    }
}
