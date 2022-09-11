package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.animation.MyAnimation;

public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img, coinImg;
    private MyAnimation animation, explosionAnim;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("city.png");
        coinImg = new Texture("coin-sprite-animation.png");
        animation = new MyAnimation("kindpng_2838048.png", 5, 6, 10, Animation.PlayMode.LOOP);
        explosionAnim = new MyAnimation("explosion.png", 4, 8, 10, Animation.PlayMode.LOOP);
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1);
        animation.setTime(Gdx.graphics.getDeltaTime());
        explosionAnim.setTime(Gdx.graphics.getDeltaTime());
        float x = Gdx.input.getX() - animation.startAnimation().getRegionWidth() / 2;
        float y = Gdx.graphics.getHeight() - Gdx.input.getY() - animation.startAnimation().getRegionHeight() / 2;

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
    }
}
