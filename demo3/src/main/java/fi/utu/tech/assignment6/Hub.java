package fi.utu.tech.assignment6;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Hub extends Thread {

    private Map<Integer, Light> lights = Collections.synchronizedMap(new HashMap<>());
    // If your terminal does not work with carriage return for some reason (output
    // looks garbage)
    private boolean ALTERNATE_OUTPUT = false;

    public Hub() {
        // Create 5 lights (ids 0-4) for testing
        for (int i = 0; i < 5; i++) {
            addLight(i);
        }
        this.setDaemon(true);
        this.start();
    }

    /**
     * 
     * @param id Id of the light-to-be-generated
     * @return The id of the newly-created light
     */
    public int addLight(int id) {
        lights.put(id, new Light(id));
        return id;
    }

    /**
     * Turn the light with id of "id" on
     * 
     * @param id The id of light to be turned on
     */
    public void turnOnLight(int id) {
        Light l = lights.get(id);
        l.turnOn();
    }

    /**
     * Turn the light with id of "id" off
     * 
     * @param id The id of light to be turned off
     */
    public void turnOffLight(int id) {
        Light l = lights.get(id);
        l.turnOff();
    }

    /**
     * Get the id numbers of the currently available lights
     * 
     * @return The set of ids
     */
    public Set<Integer> getLightIds() {
        return lights.keySet();
    }

    /**
     * Get the currently available lights
     * 
     * @return The collection of currently available lights
     */
    public Collection<Light> getLights() {
        return lights.values();
    }

    /**
     * Get the string representation of the current state of the lights
     */
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        List<Integer> lightIds;
        synchronized (lights) {
            // The lights need to be in a List since the Set is not ordered
            // (we want our lights to be in the same order on each invocation)
            lightIds = new ArrayList<>(lights.keySet());
            Collections.sort(lightIds);
            for (int id : lightIds) {
                tmp.append(String.format("%s ", lights.get(id).isPowerOn() ? "ON" : "OF"));
            }
        }
        return tmp.toString();
    }

    /**
     * Status monitoring, should NOT require any changes
     */
    public void run() {
        while (true) {
            final int LIMIT = 80;
            var str = this.toString();
            int padding = str.length() < LIMIT ? LIMIT - str.length() : 0;
            var output = str + " ".repeat(padding);
            if (!ALTERNATE_OUTPUT) {
                System.out.printf("\r %s", output.substring(0, LIMIT));
            } else {
                System.out.printf("%n %s", output.substring(0, LIMIT));
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
