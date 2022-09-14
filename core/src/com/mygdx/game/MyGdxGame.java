package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.animation.MyAtlasAnimation;

import java.awt.*;

public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture img, coinImg;
    private MyAtlasAnimation animation, explosionAnim;
    private Music music;
    private Sound sound;
    MyInputProcessor myInputProcessor;
    private float x, y;
    int dir = 0, step = 1;
    Rectangle rectangle, window;

    @Override
    public void create() {
        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);

        music = Gdx.audio.newMusic(Gdx.files.internal("stray-the-way-you-compute-tonight.mp3"));
        music.setVolume(0.05f);
//        music.setPan(0,1);
        music.setLooping(true);
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("stray-droid-reaction-heart-sound-effect.mp3"));

        batch = new SpriteBatch();
        img = new Texture("city.png");
        coinImg = new Texture("coin-sprite-animation.png");
//        animation = new MyAnimation("kindpng_2838048.png", 5, 6, 10, Animation.PlayMode.LOOP);
//        explosionAnim = new MyAnimation("explosion.png", 4, 8, 10, Animation.PlayMode.LOOP);
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1);
        animation.setTime(Gdx.graphics.getDeltaTime());
        explosionAnim.setTime(Gdx.graphics.getDeltaTime());

        /*float x = Gdx.input.getX() - animation.startAnimation().getRegionWidth() / 2;
        float y = Gdx.graphics.getHeight() - Gdx.input.getY() - animation.startAnimation().getRegionHeight() / 2;*/

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            sound.play();
        }

        if (myInputProcessor.getOutString().contains("A")) {
            x--;
        }
        if (myInputProcessor.getOutString().contains("D")) {
            x++;
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

        System.out.println(myInputProcessor.getOutString());

        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(animation.startAnimation(), x, y);
        batch.draw(explosionAnim.startAnimation(), 1, 90);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        coinImg.dispose();
        animation.dispose();
        explosionAnim.dispose();
        music.dispose();
        sound.dispose();
    }
}
