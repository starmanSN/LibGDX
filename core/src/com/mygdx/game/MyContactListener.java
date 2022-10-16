package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.screens.LevelOne;
import com.mygdx.game.screens.LevelTwo;

public class MyContactListener implements ContactListener {

    public static int cnt = 0;
    public static int count = 0;
    public static boolean isDamage;
    private Sound sound;
    public static boolean onGround;


    public MyContactListener() {
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/7d32ae33c98b0c7.mp3"));
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData().equals("legs") && b.getUserData().equals("ground")) {
            cnt++;
            onGround = true;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("ground")) {
            cnt++;
            onGround = true;
        }
        if (a.getUserData().equals("legs") && b.getUserData().equals("damageGround")) {
            isDamage = true;
            sound.play();
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("damageGround")) {
            isDamage = true;
            sound.play();
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData().equals("legs") && b.getUserData().equals("ground")) {
            cnt--;
            onGround = false;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("ground")) {
            cnt--;
            onGround = false;
        }
        if (a.getUserData().equals("legs") && b.getUserData().equals("damageGround")) {
            sound.pause();
            isDamage = false;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("damageGround")) {
            sound.pause();
            isDamage = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if (a.getUserData().equals("Hero") && b.getUserData().equals("coins")) {
            LevelOne.bodyToDelete.add(b.getBody());
//            LevelTwo.bodyToDelete.add(b.getBody());
            Gdx.graphics.setTitle("Your Score: " + (count += 1));
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("coins")) {
            LevelOne.bodyToDelete.add(a.getBody());
//            LevelTwo.bodyToDelete.add(a.getBody());
            Gdx.graphics.setTitle("Your Score: " + (count += 1));
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public void dispose() {
        this.sound.dispose();
    }
}
