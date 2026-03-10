package core;

/**
 * Controls pause/resume of algorithm threads.
 * Each algorithm calls waitIfPaused() at each step.
 */
public class StepController {
    private volatile boolean paused = false;

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notifyAll();
    }

    public boolean isPaused() {
        return paused;
    }

    /**
     * Called by algorithm threads at each step.
     * Blocks the thread while paused; returns immediately if running.
     * Respects thread interruption for clean stop.
     */
    public synchronized void waitIfPaused() {
        try {
            while (paused) {
                if (Thread.currentThread().isInterrupted()) return;
                wait(100); // wait in 100ms chunks so interrupt is checked regularly
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}