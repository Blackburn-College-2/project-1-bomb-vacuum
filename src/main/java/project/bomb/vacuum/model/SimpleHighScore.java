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
     *
     * @param name the name associated with the score
     * @param time the time from the game
     */
    SimpleHighScore(String name, long time) {
        this.name = name;
        this.time = time;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getTime() {
        return this.time;
    }

    public String toString() {
        return String.format("%s -- %d", this.name, this.time);
    }

}
