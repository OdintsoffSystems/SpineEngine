/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.odintsofftware.gameapi.media.audio;

/**
 *
 * @author Ivan
 */
public interface ISoundSample {

    public void play(boolean loop);
    public void pause();
    public void stop();

}
