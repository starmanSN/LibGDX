package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MenuScreen implements Screen {

    Game game;
    Texture background, icon;
    SpriteBatch batch;
    int x, y;
    Rectangle rectangle;

    public MenuScreen(Game game) {
        batch = new SpriteBatch();
        this.game = game;
        background = new Texture("flintstone-background.png");
        icon = new Texture("play-button.png");
        x = Gdx.graphics.getWidth() / 2 - icon.getWidth() / 2;
        y = Gdx.graphics.getWidth() / 2 - icon.getWidth() / 2;
        rectangle = new Rectangle(x, y, icon.getWidth(), icon.getHeight());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(icon, x, y);
        batch.end();

        if (Gdx.input.isTouched()) {
            if (rectangle.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                dispose();
                game.setScreen(new GameScreen(game));
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

    }
}