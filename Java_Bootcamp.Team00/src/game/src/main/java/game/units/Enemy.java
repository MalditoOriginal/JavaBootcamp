package game.units;

import game.field.GameField;
import movement.Movement;

import java.util.*;

public class Enemy extends Unit implements Movable {
    public Enemy(char chr, String color) {
        super(chr, color);
        this.type = 3;
    }

    @Override
    public byte move(GameField gameField) {
        int[] xy = gameField.getUnitCoords(this);
        int h = xy[0];
        int w = xy[1];
        List<Integer> directions = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(directions);

        for (int direction : directions) {
            boolean outOfBounds = false;
            Unit destinationCell = null;

            switch (direction) {
                case 0:
                    if (h + 1 < gameField.getGameFieldSize())
                        destinationCell = gameField.getGameFieldCell(h + 1, w);
                    else
                        outOfBounds = true;
                    break;
                case 1:
                    if (w + 1 < gameField.getGameFieldSize())
                        destinationCell = gameField.getGameFieldCell(h, w + 1);
                    else
                        outOfBounds = true;
                    break;
                case 2:
                    if (h - 1 >= 0)
                        destinationCell = gameField.getGameFieldCell(h - 1, w);
                    else
                        outOfBounds = true;
                    break;
                case 3:
                    if (w - 1 >= 0)
                        destinationCell = gameField.getGameFieldCell(h, w - 1);
                    else
                        outOfBounds = true;
                    break;
            }

            if (!outOfBounds) {
                int condition = Movement.moveCondition(this.getType(), destinationCell == null ? 1 : destinationCell.getType());

                switch (condition) {
                    case 0: // can't move
                        break;
                    case 1: //can move
                        gameField.setGameFieldCell(h, w, null);

                        if (direction == 0) h++;
                        else if (direction == 2) h--;
                        if (direction == 1) w++;
                        else if (direction == 3) w--;

                        gameField.setGameFieldCell(h, w, this);
                        return 0;
                    case 3: //enemy win
                        return -1;
                }
            }
        }
        return 0;
    }
}
