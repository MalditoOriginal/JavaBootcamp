package game.field;

import game.exceptions.IllegalParametersException;
import game.units.*;

import java.io.*;
import java.util.*;

import static com.diogonunes.jcolor.Ansi.colorize;

public class GameField {
    private Player player;
    private Goal goal;
    private Unit empty;
    private Enemy[] enemies;
    private Wall[] walls;
    private Unit[][] gameField;
    private boolean devMode;

    public GameField(int enemiesCount, int wallsCount, int fieldSize) {
        char playerMark;
        char enemyMark;
        char wallMark;
        char goalMark;
        char emptyMark;
        String playerColor;
        String enemyColor;
        String wallColor;
        String goalColor;
        String emptyColor;

        try {
            this.devMode = false;
            Properties properties = new Properties();
            String profile = System.getProperty("profile", "production");
            InputStream input = GameField.class.getClassLoader()
                    .getResourceAsStream("application-" + profile + ".properties");

            if (input == null) throw new RuntimeException("Cannot find application-" + profile + ".properties");
            if(profile.equals("dev")) devMode = true;

            properties.load(input);
            playerMark = getArgFromProperties(properties.getProperty("player.char")).charAt(0);
            enemyMark = getArgFromProperties(properties.getProperty("enemy.char")).charAt(0);
            wallMark = getArgFromProperties(properties.getProperty("wall.char")).charAt(0);
            goalMark = getArgFromProperties(properties.getProperty("goal.char")).charAt(0);
            emptyMark = getArgFromProperties(properties.getProperty("empty.char")).charAt(0);

            playerColor = properties.getProperty("player.color");
            enemyColor = properties.getProperty("enemy.color");
            wallColor = properties.getProperty("wall.color");
            goalColor = properties.getProperty("goal.color");
            emptyColor = properties.getProperty("empty.color");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.player = new Player(playerMark, playerColor);
        this.enemies = new Enemy[enemiesCount];

        for (int i = 0; i < enemies.length; i++)
            enemies[i] = new Enemy(enemyMark, enemyColor);

        this.walls = new Wall[wallsCount];

        for (int i = 0; i < walls.length; i++)
            walls[i] = new Wall(wallMark, wallColor);

        this.goal = new Goal(goalMark, goalColor);
        this.gameField = new Unit[fieldSize][fieldSize];
        this.empty = new Wall(emptyMark, emptyColor);
    }

    public void generateField() {
        if (2 + walls.length + enemies.length > gameField.length * gameField.length)
            throw new IllegalParametersException("Count of game objects is bigger, then game game.field cells.");

        Random random = new Random();
        int playerPosX = 0;
        int playerPosY = 0;
        int goalPosX = 0;
        int goalPosY = 0;

        while (playerPosX == goalPosX && playerPosY == goalPosY) {
            playerPosX = random.nextInt(gameField.length);
            playerPosY = random.nextInt(gameField.length);
            goalPosX = random.nextInt(gameField.length);
            goalPosY = random.nextInt(gameField.length);
        }

        Set<String> occupiedCells = generatePath(playerPosX, playerPosY, goalPosX, goalPosY);

        if (occupiedCells.size() < Math.abs(playerPosX - goalPosX) + Math.abs(playerPosY - goalPosY))
            throw new IllegalParametersException("Player can`t reach the goal in this conditions.");

        generateWalls(occupiedCells);
        generateEnemies(occupiedCells);
        gameField[playerPosX][playerPosY] = player;
        gameField[goalPosX][goalPosY] = goal;
    }

    private Set<String> generatePath(int playerPosX, int playerPosY, int goalPosX, int goalPosY) {
        Random random = new Random();
        Set<String> path = new HashSet<>();

        while (playerPosX != goalPosX || playerPosY != goalPosY) {
            path.add(playerPosX + "," + playerPosY);

            List<int[]> possibleMoves = new ArrayList<>();
            if (playerPosX > 0 && !path.contains((playerPosX - 1) + "," + playerPosY))
                possibleMoves.add(new int[]{playerPosX - 1, playerPosY});
            if (playerPosX < gameField[0].length - 1 && !path.contains((playerPosX + 1) + "," + playerPosY))
                possibleMoves.add(new int[]{playerPosX + 1, playerPosY});
            if (playerPosY > 0 && !path.contains(playerPosX + "," + (playerPosY - 1)))
                possibleMoves.add(new int[]{playerPosX, playerPosY - 1});
            if (playerPosY < gameField.length - 1 && !path.contains(playerPosX + "," + (playerPosY + 1)))
                possibleMoves.add(new int[]{playerPosX, playerPosY + 1});

            if (!possibleMoves.isEmpty() && random.nextDouble() < 0.7) {
                int[] nextMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
                playerPosX = nextMove[0];
                playerPosY = nextMove[1];
            } else {
                if (playerPosX != goalPosX) playerPosX += playerPosX < goalPosX ? 1 : -1;
                else if (playerPosY != goalPosY) playerPosY += playerPosY < goalPosY ? 1 : -1;
            }
            gameField[playerPosY][playerPosX] = null;
        }
        return path;
    }

    private void generateWalls(Set<String> occupiedCells) {
        Random random = new Random();
        int wallsCount = walls.length;

        while (wallsCount != 0) {
            int x = random.nextInt(gameField.length);
            int y = random.nextInt(gameField.length);
            boolean unic = true;

            for (String cell : occupiedCells) {
                if (Objects.equals(cell, x + "," + y)) unic = false;
            }

            if (unic) {
                gameField[x][y] = walls[wallsCount - 1];
                occupiedCells.add(x + "," + y);
                wallsCount--;
            }
        }
    }

    private void generateEnemies(Set<String> occupiedCells) {
        Random random = new Random();
        int enemiesCount = enemies.length;

        while (enemiesCount != 0) {
            int x = random.nextInt(gameField.length);
            int y = random.nextInt(gameField.length);
            boolean unic = true;

            for (String cell : occupiedCells) {
                if (Objects.equals(cell, x + "," + y)) {
                    unic = false;
                    break;
                }
            }

            if (unic) {
                gameField[x][y] = enemies[enemiesCount - 1];
                occupiedCells.add(x + "," + y);
                enemiesCount--;
            }
        }
    }

    private String getArgFromProperties(String arg) {
        return arg.isEmpty() ? " " : arg;
    }

    public int getGameFieldSize() {
        return gameField.length;
    }

    public Unit getGameFieldCell(int h, int w) {
        return gameField[h][w];
    }

    public void setGameFieldCell(int h, int w, Unit unit) {
        gameField[h][w] = unit;
    }

    public int[] getUnitCoords(Unit unit) {
        for (int x = 0; x < gameField.length; x++) {
            for (int y = 0; y < gameField.length; y++) {
                if (gameField[x][y] != null && gameField[x][y].equals(unit)) return new int[]{x, y};
            }
        }
        throw new NullPointerException("Unit not found on the game.field");
    }

    public boolean isDevMode() { return devMode; }

    public void printGameField() {
        if(!devMode) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
        for (int h = 0; h < gameField.length; h++) {
            for (int w = 0; w < gameField[h].length; w++)
                printGameFieldCell(h, w);
            System.out.println();
        }
        System.out.println("\nUse WASD to move, 9 to exit");
    }

    public void printGameFieldCell(int h, int w) {
        Unit printed = gameField[h][w];

        if (printed == null)
            System.out.print(colorize(empty.getChrToString(), empty.getColor()));
        else
            System.out.print(colorize(printed.getChrToString(), printed.getColor()));
    }

    public void playGame() {
        boolean isRunning = true;

        while (isRunning) {
            printGameField();
            byte playerEvent = player.move(this);

            if (playerEvent == -1) {
                System.out.println("Game Over! You quit.");
                isRunning = false;
            } else if(playerEvent == 1) {
                System.out.println("Game over. You win!");
                isRunning = false;
            }

            if (isRunning) {
                for (Enemy enemy : enemies) {
                    byte enemyEvent = enemy.move(this);

                    if (enemyEvent == -1) {
                        System.out.println("Game Over! Enemy caught you.");
                        isRunning = false;
                        break;
                    }
                }
            } else break;
        }
    }
}

