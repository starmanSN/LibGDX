package com.mygdx.game.persons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Physics;
import com.mygdx.game.animation.MyAtlasAnimation;

public class Fireball {
    private final Body body;
    private final static float SPD = 20;
    private float time;
    private MyAtlasAnimation animation;

    public Fireball(Physics physics, float x, float y, int dir, MyAtlasAnimation animation) {
        this.animation = animation;
        body = physics.addFireball(x, y);
        body.setBullet(true);
        body.setLinearVelocity(new Vector2(SPD * dir, 0));
        time = 5;
    }

    public Body update(float dTime) {
        time -= dTime;
        animation.setTime(dTime);
        return (time <= 0) ? body : null;
    }

    public void draw (SpriteBatch batch) {
        batch.draw(animation.startAnimation(), body.getPosition().x * Physics.PPM, body.getPosition().y * Physics.PPM);
    }

    public void dispose() {
        this.animation.dispose();
    }
}
