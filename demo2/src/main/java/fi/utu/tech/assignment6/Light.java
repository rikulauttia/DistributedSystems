package fi.utu.tech.assignment6;


public class Light {

    private boolean powerOn = false;
    private int id;

    /**
     * 
     * @param id The id of the lamp (should be the same as in the hub for now)
     */
    public Light(int id) {
        this.id = id;
    }

    /**
     * Turn lamp on
     */
    public synchronized void turnOn() {
        this.powerOn = true;
    }

    /**
     * Turn lamp off
     */
    public synchronized void turnOff() {
        this.powerOn = false;
    }

    /**
     * Toggle the lamp on/off depending on the current state
     */
    public synchronized void toggle() {
        powerOn = !powerOn;
    }
    /**
     * 
     * @return Is the lamp currently powered on?
     */
    public synchronized boolean isPowerOn() {
        return powerOn;
    }

    public String toString() {
        return String.format("Light %d is set %s", id, isPowerOn() ? "ON": "OFF");
    }
    
}
