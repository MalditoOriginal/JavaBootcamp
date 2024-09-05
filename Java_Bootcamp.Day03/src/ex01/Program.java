public class Program {
    private static final Object lock = new Object();
    private static boolean isEggTurn = true;

    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith("--count=")) {
            System.err.println("Usage: java Program --count=xx");
            System.exit(1);
        }

        int count = Integer.parseInt(args[0].substring("--count=".length()));

        Thread eggThread = new Thread(new EggRunnable(count));
        Thread henThread = new Thread(new HenRunnable(count));

        eggThread.start();
        henThread.start();
    }

    public static Object getLock() {
        return lock;
    }

    public static boolean isEggTurn() {
        return isEggTurn;
    }

    public static void setEggTurn(boolean eggTurn) {
        isEggTurn = eggTurn;
    }
}