package com.mygdx.game.persons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Physics;
import com.mygdx.game.animation.MyAtlasAnimation;
import com.mygdx.game.enums.HeroActions;

import java.util.HashMap;

public class Player {
    HashMap<HeroActions, MyAtlasAnimation> manAssets;
    private final float FPS = 1 / 7f;
    private float time;
    public static boolean canJump, isFire;
    private Animation<TextureAtlas.AtlasRegion> baseAnm;
    private boolean loop;
    private Body body;
    private Dir dir;
    private static float dScale = 1;
    private float hitPoints, live;
    private Sound sound;

    public enum Dir {LEFT, RIGHT}

    public Player(Body body) {
        hitPoints = live = 100;
        this.body = body;
        manAssets = new HashMap<>();
        manAssets.put(HeroActions.JUMP, new MyAtlasAnimation("atlas/Fang.atlas", "jump", FPS, true, "sounds/cartoon-spring-boing-03.mp3"));
        manAssets.put(HeroActions.RUN, new MyAtlasAnimation("atlas/Fang.atlas", "run", FPS, true, "sounds/single_on_dirty_stone_step_flip_flop_007_30443.mp3"));
        manAssets.put(HeroActions.STAND, new MyAtlasAnimation("atlas/Fang.atlas", "stand", FPS, true, null));
        manAssets.put(HeroActions.SHOOT, new MyAtlasAnimation("atlas/Fang.atlas", "punch", FPS, true, "sounds/single_on_dirty_stone_step_flip_flop_007_30443.mp3"));
        baseAnm = manAssets.get(HeroActions.STAND).getAnimation();
        loop = true;
        dir = Dir.LEFT;
    }

    public float getHit(float damage) {
        hitPoints -= damage;
        return hitPoints;
    }

    public int getDir() {
        return (dir == Dir.LEFT) ? -1 : 1;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public void setCanJump(boolean isJump) {
        canJump = isJump;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public Body setFPS(Vector2 vector, boolean onGround) {
        if (vector.x > 0.1f) setDir(Dir.RIGHT);
        if (vector.x < -0.1f) setDir(Dir.LEFT);
        float tmp = (float) (Math.sqrt(vector.x * vector.x + vector.y * vector.y)) * 5;
        setState(HeroActions.STAND);
        if (isFire) {
            setState(HeroActions.SHOOT);
            manAssets.get(HeroActions.SHOOT).setTime(FPS);
            return body;
        }
        if (Math.abs(vector.x) > 0.2f && Math.abs(vector.y) < 10 && onGround) {
            setState(HeroActions.RUN);
            baseAnm.setFrameDuration(1 / tmp);
            return null;
        }
        if (Math.abs(vector.y) > 1 && !canJump) {
            setState(HeroActions.JUMP);
            return null;
        }
        return null;
    }

    public float setTime(float deltaTime) {
        time += deltaTime;
        return time;
    }

    public void setState(HeroActions state) {
        baseAnm = manAssets.get(state).getAnimation();
        switch (state) {
            case STAND:
                loop = true;
                baseAnm.setFrameDuration(FPS);
                break;
            case JUMP:
                loop = false;
                break;
            case SHOOT:
                loop = true;
                baseAnm.setFrameDuration(FPS);
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
        this.manAssets.clear();
    }
}
