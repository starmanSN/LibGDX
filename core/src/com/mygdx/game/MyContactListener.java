package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.screens.GameOverScreen;
import com.mygdx.game.screens.GameScreen;

public class MyContactListener implements ContactListener {

    public static int cnt = 0;
    public static int count = 0;
    Game game;


    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData().equals("Hero") && b.getUserData().equals("coins")) {
            GameScreen.bodyToDelete.add(b.getBody());
            Gdx.graphics.setTitle("Your Score: " + (count += 1));
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("coins")) {
            GameScreen.bodyToDelete.add(a.getBody());
            Gdx.graphics.setTitle("Your Score: " + (count += 1));
        }
        if (a.getUserData().equals("legs") && b.getUserData().equals("ground")) {
            cnt++;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("ground")) {
            cnt++;
        }
//        if (b.getUserData().equals("Hero") && a.getUserData().equals("lava")) {
//            game.setScreen(new GameOverScreen(game));
//        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData().equals("legs") && b.getUserData().equals("ground")) {
            cnt--;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("ground")) {
            cnt--;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
