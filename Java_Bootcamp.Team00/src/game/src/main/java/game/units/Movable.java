package game.units;

import game.field.GameField;

public interface Movable {
    byte move(GameField gameField);
}
