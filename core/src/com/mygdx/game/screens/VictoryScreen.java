package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class VictoryScreen implements Screen {
    private Game game;
    private Texture background, icon;
    private SpriteBatch batch;
    private int x, y;
    private Rectangle rectangle;
    private Music music;

    public VictoryScreen(Game game) {
        batch = new SpriteBatch();
        this.game = game;
        background = new Texture("flintstone-background.png");
        icon = new Texture("pngegg.png");
        x = Gdx.graphics.getWidth() / 2 - icon.getWidth() / 2;
        y = Gdx.graphics.getWidth() / 2 - icon.getWidth() / 2;
        rectangle = new Rectangle(x, y, icon.getWidth(), icon.getHeight());

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/win.mp3"));
        music.setVolume(0.1f);
        music.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(icon, x, y);
        Gdx.graphics.setTitle("Victory!!!");
        batch.end();

        if (Gdx.input.isTouched()) {
            if (rectangle.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                dispose();
                game.setScreen(new MenuScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.background.dispose();
        this.icon.dispose();
        this.batch.dispose();
        this.game.dispose();
        this.music.dispose();
    }
}
