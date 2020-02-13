/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.bomb.vacuum.model;

import project.bomb.vacuum.HighScore;

/**
 *
 * @author rylee.wilson
 */
public class SimpleHighScore implements HighScore {
    private final String name;
    private final long time;

    /**
     * 
     * @param name
     * @param time 
     */
    SimpleHighScore(String name, long time) {
        this.name = name;
        this.time = time;
    }

    /**
     * 
     * @return 
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 
     * @return 
     */
    @Override
    public long getTime() {
        return this.time;
    }
    
    /**
     * 
     * @return 
     */
    public String toString() {
        return String.format("%s -- %d", this.name, this.time);
    }

}
