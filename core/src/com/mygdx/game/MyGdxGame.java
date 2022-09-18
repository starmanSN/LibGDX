package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.animation.MyAtlasAnimation;

import java.awt.*;

public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture img, coinImg;
    private MyAtlasAnimation stand, walk, jump, tmpA;
    private Music music;
    private Sound sound;
    MyInputProcessor myInputProcessor;
    private float x, y;
    int dir = 0, step = 1;
    Rectangle rectangle, window;

    @Override
    public void create() {

        shapeRenderer = new ShapeRenderer();
        rectangle = new Rectangle();
        window = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);

        music = Gdx.audio.newMusic(Gdx.files.internal("stray-the-way-you-compute-tonight.mp3"));
        music.setVolume(0.05f);
        music.setLooping(true);
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("stray-droid-reaction-heart-sound-effect.mp3"));

        batch = new SpriteBatch();
        img = new Texture("Genesis 32X SCD - The Flintstones - Stage 03.png");
        coinImg = new Texture("coin-sprite-animation.png");
        stand = new MyAtlasAnimation("atlas/fred.atlas", "stand", 1, Animation.PlayMode.LOOP);
        walk = new MyAtlasAnimation("atlas/fred.atlas", "walk", 10, Animation.PlayMode.LOOP);
        jump = new MyAtlasAnimation("atlas/fred.atlas", "jump", 7, Animation.PlayMode.LOOP);
        tmpA = stand;
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1);
        tmpA = stand;
        dir = 0;

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            sound.play(0.1f, 1, 0);
        }
        if (myInputProcessor.getOutString().contains("A")) {
            dir = -1;
            tmpA = walk;
        }
        if (myInputProcessor.getOutString().contains("D")) {
            dir = 1;
            tmpA = walk;
        }
        if (myInputProcessor.getOutString().contains("W")) {
            y++;
        }
        if (myInputProcessor.getOutString().contains("S")) {
            y--;
        }
        if (myInputProcessor.getOutString().contains("Space")) {
            x = Gdx.graphics.getWidth() / 2;
            y = Gdx.graphics.getHeight() / 2;
        }

        if (dir == -1) {
            x -= step;
        }
        if (dir == 1) {
            x += step;
        }

        TextureRegion tmp = tmpA.startAnimation();
        tmpA.setTime(Gdx.graphics.getDeltaTime());
        if (!tmpA.startAnimation().isFlipX() & dir == -1) {
            tmpA.startAnimation().flip(true, false);
        }
        if (tmpA.startAnimation().isFlipX() & dir == 1) {
            tmpA.startAnimation().flip(true, false);
        }
        rectangle.x = (int) x;
        rectangle.y = (int) y;
        rectangle.width = tmp.getRegionWidth();
        rectangle.height = tmp.getRegionHeight();


        System.out.println(myInputProcessor.getOutString());

        batch.begin();
        shapeRenderer.setColor(Color.BLACK);
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(tmpA.startAnimation(), x, y);
        batch.end();

        if (!window.contains(rectangle)) {
            Gdx.graphics.setTitle("Out");
        } else {
            Gdx.graphics.setTitle("In");
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        coinImg.dispose();
        stand.dispose();
        walk.dispose();
        jump.dispose();
        music.dispose();
        sound.dispose();
    }
}
