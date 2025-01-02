package movement;

public class Movement {
    public static int moveCondition(int currentType, int destinationType) {
        byte result = -1;

        switch (currentType) {
            case 5:
                switch (destinationType) {
                    case 0:
                        result = 0;
                        break;
                    case 1:
                        result = 1;
                        break;
                    case 2:
                        result = 0;
                        break;
                    case 3:
                        result = 2;
                        break;
                    case 4:
                        result = 3;
                        break;
                }
                break;
            case 3:
                switch (destinationType) {
                    case 0:
                        result = 0;
                        break;
                    case 1:
                        result = 1;
                        break;
                    case 2:
                        result = 0;
                        break;
                    case 3:
                        result = 0;
                        break;
                    case 4:
                        result = 0;
                        break;
                    case 5:
                        result = 3;
                        break;
                }
                break;
        }
        return result;
    }
}
