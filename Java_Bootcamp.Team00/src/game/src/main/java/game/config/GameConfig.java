package game.config;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import game.exceptions.IllegalParametersException;

@Parameters(separators = "=")
public class GameConfig {
    @Parameter(names = "--enemiesCount", required = true, description = "Number of enemies")
    private int enemiesCount;

    @Parameter(names = "--wallsCount", required = true, description = "Number of walls")
    private int wallsCount;

    @Parameter(names = "--size", required = true, description = "Size of the game field")
    private int size;

    @Parameter(names = "--profile", required = true, description = "Game profile (production/dev)")
    private String profile;

    public void validate() {
        int maxElements = size * size - 3; // Space for player, enemies, walls, and goal
        if (enemiesCount + wallsCount > maxElements) {
            throw new IllegalParametersException("Too many elements for the given field size");
        }
        if (size < 3) {
            throw new IllegalParametersException("Field size must be at least 3");
        }
        if (enemiesCount < 1) {
            throw new IllegalParametersException("Must have at least 1 enemy");
        }
        if (wallsCount < 0) {
            throw new IllegalParametersException("Cannot have negative number of walls");
        }
    }

    // Getters
    public int getEnemiesCount() { return enemiesCount; }
    public int getWallsCount() { return wallsCount; }
    public int getSize() { return size; }
    public String getProfile() { return profile; }
}