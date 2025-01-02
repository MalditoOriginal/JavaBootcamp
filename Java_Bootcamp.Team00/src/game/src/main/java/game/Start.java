package game;

import com.beust.jcommander.JCommander;
import game.config.GameConfig;
import game.exceptions.IllegalParametersException;
import game.field.GameField;

public class Start {
    public static void main(String[] args) {
        GameConfig config = new GameConfig();

        try {
            JCommander.newBuilder()
                    .addObject(config)
                    .build()
                    .parse(args);
            config.validate();

            System.setProperty("profile", config.getProfile());

            GameField gameField = new GameField(
                    config.getEnemiesCount(),
                    config.getWallsCount(),
                    config.getSize()
            );
            gameField.generateField();
            gameField.playGame();
        } catch (Exception e) {
            throw new IllegalParametersException(e.getMessage());
        }
    }
}
