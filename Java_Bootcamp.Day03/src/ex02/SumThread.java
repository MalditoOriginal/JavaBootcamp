class SumThread extends Thread {
    private final int threadId;
    private final int[] array;
    private final int start;
    private final int end;
    private int sum;

    public SumThread(int threadId, int[] array, int start, int end) {
        this.threadId = threadId;
        this.array = array;
        this.start = start;
        this.end = end;
        this.sum = 0;
    }

    public int getSum() {
        return sum;
    }

    public int getThreadId() {
        return threadId;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            sum += array[i];
        }
    }
}