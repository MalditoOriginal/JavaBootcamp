class HenRunnable implements Runnable {
    private final int count;

    public HenRunnable(int count) {
        this.count = count;
    }

    public void run() {
        for (int i = 0; i < count; ++i) {
            System.out.println("Hen");
        }
    }
}