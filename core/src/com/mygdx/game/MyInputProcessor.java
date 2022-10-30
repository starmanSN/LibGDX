package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.persons.Player;

public class MyInputProcessor implements InputProcessor {

    private Vector2 outForce;
    private float cameraY, cameraX;

    public MyInputProcessor() {
        outForce = new Vector2();
        cameraY = cameraX = 0;
    }

    public Vector2 getVector() {
        return outForce;
    }

    public float getCameraX() {
        return cameraX;
    }

    public float getCameraY() {
        return cameraY;
    }

    @Override
    public boolean keyDown(int keycode) {
        String inKey = Input.Keys.toString(keycode).toUpperCase();

        switch (inKey) {
            case "LEFT":
                outForce.add(-0.08f, 0);
                break;
            case "RIGHT":
                outForce.add(0.08f, 0);
                break;
            case "UP":
                outForce.add(0, 2.15f);
                break;
            case "SPACE":
                Player.isFire = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        String inKey = Input.Keys.toString(keycode).toUpperCase();

        switch (inKey) {
            case "LEFT":
                outForce.set(0, outForce.y);
                break;
            case "RIGHT":
                outForce.set(0, outForce.y);
                break;
            case "UP":
                outForce.set(outForce.x, 0);
                break;
            case "SPACE":
                Player.isFire = false;
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
