/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.bomb.vacuum.model;

import project.bomb.vacuum.HighScore;

public class SimpleHighScore implements HighScore {
    private final String name;
    private final long time;

    /**
     * Creates a new SimpleHighScore
     * @param name the name associated with the score
     * @param time the time from the game
     */
    SimpleHighScore(String name, long time) {
        this.name = name;
        this.time = time;
    }

    /**
     * Gets the SimpleHighScore's name
     * @return the name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Gets the SimpleHighScore's time
     * @return the time
     */
    @Override
    public long getTime() {
        return this.time;
    }
    
    /**
     * Formats the HighScore as a string
     * @return the formatted HighScore including name and time as a string
     */
    public String toString() {
        return String.format("%s -- %d", this.name, this.time);
    }

}
