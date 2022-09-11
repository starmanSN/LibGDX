package com.mygdx.game.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAnimation {

    Texture img;
    Animation<TextureRegion> animation;
    private float time;

    public MyAnimation(String name, int row, int col, float fps, Animation.PlayMode playMode) {
        time = 0;
        img = new Texture(name);
        TextureRegion region = new TextureRegion(img);
        TextureRegion[][] regions = region.split(img.getWidth() / col, img.getHeight() / row);
        TextureRegion[] tmp = new TextureRegion[regions.length * regions[0].length];
        int cnt = 0;
        for (int i = 0; i < regions.length; i++) {
            for (int j = 0; j < regions.length; j++) {
                tmp[cnt++] = regions[i][j];
            }
        }
        animation = new Animation<>(1 / fps, regions[0]);
        animation.setPlayMode(playMode);
    }

    public TextureRegion startAnimation() {
        return animation.getKeyFrame(time);
    }

    public void setTime(float dT) {
        time += dT;
    }

    public void dispose() {
        this.img.dispose();
    }
}
