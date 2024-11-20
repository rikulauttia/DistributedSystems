package fi.utu.tech.assignment6;

/**
 * Class for light, no modifications nor direct invocation required in this assignment
 * Use via the Hub objects
 */
public class Light {

    private boolean powerOn = false;
    private final int id;

    public Light(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public synchronized void turnOn() {
        this.powerOn = true;
    }

    public synchronized void turnOff() {
        this.powerOn = false;
    }

    public synchronized void toggle() {
        // We could have gotten away with just using volatile boolean
        // but because of this here, everybody gets synchronization
        powerOn = !powerOn;
    }

    public synchronized boolean isPowerOn() {
        return powerOn;
    }

    public synchronized String toString() {
        return String.format("Light %d is set %s", id, isPowerOn() ? "ON": "OFF");
    }
    
}
