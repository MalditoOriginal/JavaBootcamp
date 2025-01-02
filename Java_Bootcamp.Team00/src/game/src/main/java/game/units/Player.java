package game.units;

import game.field.GameField;
import movement.Movement;

import java.util.*;

public class Player extends Unit implements Movable {

    public Player(char chr, String color) {
        super(chr, color);
        this.type = 5;
    }

    @Override
    public byte move(GameField gameField) {
        int[] xy = gameField.getUnitCoords(this);
        int h = xy[0];
        int w = xy[1];
        Unit destinationCell = null;
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;
        byte result = 0;

        while (!validInput) {
            String input = scanner.nextLine();
            boolean outOfBounds = false;

            switch (input) {
                case "s":
                    validInput = true;
                    if (h + 1 < gameField.getGameFieldSize())
                        destinationCell = gameField.getGameFieldCell(h + 1, w);
                    else
                        outOfBounds = true;
                    break;
                case "d":
                    validInput = true;
                    if (w + 1 < gameField.getGameFieldSize())
                        destinationCell = gameField.getGameFieldCell(h, w + 1);
                    else
                        outOfBounds = true;
                    break;
                case "w":
                    validInput = true;
                    if (h - 1 >= 0)
                        destinationCell = gameField.getGameFieldCell(h - 1, w);
                    else
                        outOfBounds = true;
                    break;
                case "a":
                    validInput = true;
                    if (w - 1 >= 0)
                        destinationCell = gameField.getGameFieldCell(h, w - 1);
                    else
                        outOfBounds = true;
                    break;
                case "9":
                    return -1;
            }

            if (!outOfBounds) {
                int condition = Movement.moveCondition(this.getType(), destinationCell == null ? 1 : destinationCell.getType());

                switch (condition) {
                    case 0:
                        break;
                    case 1:
                        gameField.setGameFieldCell(h, w, null);
                        if (input.equals("s")) h++;
                        else if (input.equals("w")) h--;
                        if (input.equals("d")) w++;
                        else if (input.equals("a")) w--;
                        gameField.setGameFieldCell(h, w, this);
                        break;
                    case 2:
                        result = -1;
                        break;
                    case 3:
                        gameField.setGameFieldCell(h, w, null);
                        if (input.equals("s")) h++;
                        else if (input.equals("w")) h--;
                        if (input.equals("d")) w++;
                        else if (input.equals("a")) w--;
                        gameField.setGameFieldCell(h, w, this);
                        result = 1;
                        break;
                }
            }
        }
        if(gameField.isDevMode()) {
            boolean notConfurmed = true;

            while(notConfurmed) {
                System.out.println("Conform your turn and press 8");
                String input = scanner.nextLine();
                if(input.equals("8")) notConfurmed = false;
            }
        }
        return result;
    }
}
