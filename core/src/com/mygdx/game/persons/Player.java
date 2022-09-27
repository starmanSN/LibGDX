package com.mygdx.game.persons;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Physics;
import com.mygdx.game.enums.HeroActions;

import java.util.HashMap;

public class Player {
    HashMap<HeroActions, Animation<TextureRegion>> manAssets;
    private final float FPS = 1 / 7f;
    private float time;
    public static boolean canJump;
    private Animation<TextureRegion> baseAnm;
    private boolean loop;
    private TextureAtlas atl;
    private Body body;
    private Dir dir;
    private static float dScale = 1;

    public enum Dir {LEFT, RIGHT}

    public Player(Body body) {
        this.body = body;
        manAssets = new HashMap<>();
        atl = new TextureAtlas("atlas/fred.atlas");
        manAssets.put(HeroActions.JUMP, new Animation<TextureRegion>(FPS, atl.findRegions("jump")));
        manAssets.put(HeroActions.RUN, new Animation<TextureRegion>(FPS, atl.findRegions("walk")));
        manAssets.put(HeroActions.STAND, new Animation<TextureRegion>(FPS, atl.findRegions("stand")));
        manAssets.put(HeroActions.SHOOT, new Animation<TextureRegion>(FPS, atl.findRegions("shoot")));
        baseAnm = manAssets.get(HeroActions.STAND);
        loop = true;
        dir = Dir.LEFT;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public static void setCanJump(boolean isJump) {
        canJump = isJump;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setFPS(Vector2 vector, boolean onGround) {
        if (vector.x > 0.1f) setDir(Dir.RIGHT);
        if (vector.x < -0.1f) setDir(Dir.LEFT);
        float tmp = (float) (Math.sqrt(vector.x * vector.x + vector.y * vector.y)) * 10;
        setState(HeroActions.STAND);
        if (Math.abs(vector.x) > 0.25f && Math.abs(vector.y) < 10 && onGround) {
            setState(HeroActions.RUN);
            baseAnm.setFrameDuration(1 / tmp);
        }
        if (Math.abs(vector.y) > 1 && canJump) {
            setState(HeroActions.JUMP);
            baseAnm.setFrameDuration(FPS);
        }
    }

    public float setTime(float deltaTime) {
        time += deltaTime;
        return time;
    }

    public void setState(HeroActions state) {
        baseAnm = manAssets.get(state);
        switch (state) {
            case STAND:
                loop = true;
                baseAnm.setFrameDuration(FPS);
                break;
            case JUMP:
                loop = false;
                break;
            default:
                loop = true;
        }
    }

    public TextureRegion getFrame() {
        if (time > baseAnm.getAnimationDuration() && loop) time = 0;
        if (time > baseAnm.getAnimationDuration()) time = 0;
        TextureRegion tr = baseAnm.getKeyFrame(time);
        if (!tr.isFlipX() && dir == Dir.LEFT) tr.flip(true, false);
        if (tr.isFlipX() && dir == Dir.RIGHT) tr.flip(true, false);
        return tr;
    }

    public Rectangle getRect(OrthographicCamera camera, TextureRegion region) {
        TextureRegion tr = baseAnm.getKeyFrame(time);
        float cx = body.getPosition().x * Physics.PPM - tr.getRegionWidth() / 2 / dScale;
        float cy = body.getPosition().y * Physics.PPM - tr.getRegionHeight() / 2 / dScale;
        float cW = tr.getRegionWidth() / Physics.PPM / dScale;
        float cH = tr.getRegionHeight() / Physics.PPM / dScale;
        return new Rectangle(cx, cy, cW, cH);
    }

    public void dispose() {
        atl.dispose();
        this.manAssets.clear();
    }
}
