public class EggRunnable implements Runnable {
    private final int count;

    public EggRunnable (int count) {
        this.count = count;
    }

    public void run() {
        for (int i = 0; i < count; ++i) {
            synchronized (Program.getLock()) {
                while (!Program.isEggTurn()) {
                    try {
                        Program.getLock().wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                System.out.println("Egg");
                Program.setEggTurn(false);
                Program.getLock().notify();
            }
        }
    }
}