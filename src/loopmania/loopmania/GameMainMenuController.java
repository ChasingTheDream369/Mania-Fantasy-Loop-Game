package unsw.loopmania;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameMainMenuController {

    private MenuSwitcher gameSwitcher;
    private MenuSwitcher restartSwitcher;
    LoopManiaWorldController mainController;

    @FXML
    Button resumeGame;

    @FXML
    Button gameFinish;

    @FXML
    Label getGameMode;

    // Implement endGame in same way we end the game!

    public GameMainMenuController(LoopManiaWorldController mainController){
        this.mainController = mainController;
        resumeGame = new Button();
        gameFinish = new Button();
        getGameMode = new Label();
    }

    public void setGameSwitcher(MenuSwitcher gameSwitcher) {
        this.gameSwitcher = gameSwitcher;
    }

    public void setRestartSwitcher(MenuSwitcher gameSwitcher) {
        this.restartSwitcher =  gameSwitcher;    
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
    private void restartGame() throws IOException {
        restartSwitcher.switchMenu();
    }

    @FXML
    public void endGame(ActionEvent event) {
        Stage stage = (Stage) gameFinish.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        getGameMode.textProperty().bind(mainController.getGameMode());

    }
}
