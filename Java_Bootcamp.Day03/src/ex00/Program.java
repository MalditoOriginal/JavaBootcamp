public class Program {
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

        try {
            eggThread.join();
            henThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < count; i++) {
            System.out.println("Human");
        }
    }
}