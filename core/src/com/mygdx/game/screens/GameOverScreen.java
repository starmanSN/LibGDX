package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Label;

public class GameOverScreen implements Screen {
    private Game game;
    private Texture background, icon;
    private SpriteBatch batch;
    private int x, y;
    private Rectangle rectangle;
    private Label font;
    private Music music;
    private Sound sound;

    public GameOverScreen(Game game) {
        batch = new SpriteBatch();
        font = new Label(20);
        this.game = game;
        background = new Texture("Death.png");
        icon = new Texture("tryAgain.png");
        x = Gdx.graphics.getWidth() / 2 - icon.getWidth() / 2;
        y = Gdx.graphics.getWidth() / 2 - icon.getWidth() / 2;
        rectangle = new Rectangle(x, y, icon.getWidth(), icon.getHeight());

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/T2.mp3"));
        music.setVolume(0.05f);
        music.setLooping(true);
        music.play();
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/FO1_NAR_3.ogg"));
        sound.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        font.draw(batch, "Game Over", 0, Gdx.graphics.getHeight() / 2);
        batch.draw(icon, x, y);
        Gdx.graphics.setTitle("Game Over:(");
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
        this.font.dispose();
        this.music.dispose();
        this.sound.dispose();
        this.game.dispose();
    }
}
