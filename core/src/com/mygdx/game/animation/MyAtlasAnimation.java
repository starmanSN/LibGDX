package com.mygdx.game.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAtlasAnimation {

    TextureAtlas atlas;
    Animation<TextureAtlas.AtlasRegion> animation;
    private float time;

    public MyAtlasAnimation(String atlas, String name, float fps, Animation.PlayMode playMode) {
        time = 0;
        this.atlas = new TextureAtlas(atlas);
        animation = new Animation<>(1 / fps, this.atlas.findRegions(name));
        animation.setPlayMode(playMode);
    }

    public TextureRegion startAnimation() {
        return animation.getKeyFrame(time);
    }

    public void setTime(float dT) {
        time += dT;
    }

    public void dispose() {
        this.atlas.dispose();
    }

}
