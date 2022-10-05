/*
package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.animation.MyAtlasAnimation;
import com.mygdx.game.enums.HeroActions;

import java.awt.*;
import java.util.HashMap;

public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture img, coinImg;
    private HashMap<HeroActions, MyAtlasAnimation> manAssets;
    private Music music;
    private Sound sound;
    private MyInputProcessor myInputProcessor;
    private float x, y;
    private int dir = 0, step = 1;
    private Rectangle rectangle, window;
    private OrthographicCamera camera;
    private Physics physics;
    private Body body;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private HeroActions actions;

    @Override
    public void create() {
        map = new TmxMapLoader().load("map/map3.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        physics = new Physics();

        Array<RectangleMapObject> objects = map.getLayers().get("env").getObjects().getByType(RectangleMapObject.class);
        objects.addAll(map.getLayers().get("dyn").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            physics.addObject(objects.get(i));
        }
        body = physics.addObject((RectangleMapObject) map.getLayers().get("hero").getObjects().get("Hero"));
        body.setFixedRotation(true);

        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/stray-the-way-you-compute-tonight.mp3"));
        music.setVolume(0.05f);
        music.setLooping(true);
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/stray-droid-reaction-heart-sound-effect.mp3"));

        batch = new SpriteBatch();
        img = new Texture("Genesis 32X SCD - The Flintstones - Stage 03.png");
        coinImg = new Texture("coin-sprite-animation.png");
        manAssets = new HashMap<>();
        manAssets.put(HeroActions.STAND, new MyAtlasAnimation("atlas/fred.atlas", "stand", 2, false, "sounds/single_on_dirty_stone_step_flip_flop_007_30443.mp3"));
        manAssets.put(HeroActions.RUN, new MyAtlasAnimation("atlas/fred.atlas", "walk", 12, true, "sounds/single_on_dirty_stone_step_flip_flop_007_30443.mp3"));
        manAssets.put(HeroActions.JUMP, new MyAtlasAnimation("atlas/fred.atlas", "jump", 7, true, "sounds/cartoon-spring-boing-03.mp3"));
        actions = HeroActions.STAND;

        camera = new OrthographicCamera();
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);

        camera.position.x = body.getPosition().x;
        camera.position.y = body.getPosition().y;
        camera.zoom = 0.75f;
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        manAssets.get(actions).setTime(Gdx.graphics.getDeltaTime());
        body.applyForceToCenter(myInputProcessor.getVector(), true);

        if (body.getLinearVelocity().len() < 0.6f) {
            actions = HeroActions.STAND;
        } else if (Math.abs(body.getLinearVelocity().x) > 0.6f) {
            actions = HeroActions.RUN;
        }
        manAssets.get(actions).setTime(Gdx.graphics.getDeltaTime());
        if (!manAssets.get(actions).startAnimation().isFlipX() & body.getLinearVelocity().x < -0.6f) {
            manAssets.get(actions).startAnimation().flip(true, false);
        }
        if (manAssets.get(actions).startAnimation().isFlipX() & body.getLinearVelocity().x < 0.6f) {
            manAssets.get(actions).startAnimation().flip(true, false);
        }

        float x = body.getPosition().x - 2.5f / camera.zoom;
        float y = body.getPosition().y - 2.5f / camera.zoom;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        batch.draw(manAssets.get(actions).startAnimation(), x, y);
        batch.end();


        physics.step();
        physics.debugDraw(camera);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        coinImg.dispose();
        music.dispose();
        sound.dispose();
        map.dispose();
        mapRenderer.dispose();
        physics.dispose();
    }
}
*/
