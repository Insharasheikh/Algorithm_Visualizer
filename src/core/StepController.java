package core;

public class StepController {
    private boolean paused = false;

    // --- Pause / Resume ---
    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notifyAll();
    }

    public synchronized void waitIfPaused() {
        try {
            while (paused) {
                wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
