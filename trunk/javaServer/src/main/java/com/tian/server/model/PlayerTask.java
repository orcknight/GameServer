package com.tian.server.model;

import com.tian.server.entity.PlayerTrackActionEntity;
import com.tian.server.entity.PlayerTrackEntity;

/**
 * Created by PPX on 2017/9/7.
 */
public class PlayerTask {

    private PlayerTrackEntity track;
    private PlayerTrackActionEntity action;

    public PlayerTrackEntity getTrack() {
        return track;
    }

    public void setTrack(PlayerTrackEntity track) {
        this.track = track;
    }

    public PlayerTrackActionEntity getAction() {
        return action;
    }

    public void setAction(PlayerTrackActionEntity action) {
        this.action = action;
    }
}
