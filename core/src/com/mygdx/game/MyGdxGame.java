package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
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
    private MyInputProcessor myInputProcessor;
    private float x, y;
    private int dir = 0, step = 1;
    private Rectangle rectangle, window;
    private OrthographicCamera camera;
    private Physics physics;
    private Body body;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    @Override
    public void create() {
        map = new TmxMapLoader().load("map/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        physics = new Physics();
        BodyDef def = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        def.type = BodyDef.BodyType.StaticBody;
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 1;

        MapLayer env = map.getLayers().get("env");
        Array<RectangleMapObject> rect = env.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < rect.size; i++) {
            setCoordinates(def, shape, rect, i);
            physics.world.createBody(def).createFixture(fixtureDef).setUserData("Kubik");
        }

        def.type = BodyDef.BodyType.DynamicBody;
        def.gravityScale = 4;
        env = map.getLayers().get("dyn");
        rect = env.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < rect.size; i++) {
            setCoordinates(def, shape, rect, i);
            fixtureDef.density = (float) rect.get(i).getProperties().get("density");
            fixtureDef.friction = 0;
            fixtureDef.restitution = 1;
            physics.world.createBody(def).createFixture(fixtureDef).setUserData("Kubik");
        }

        env = map.getLayers().get("hero");
        RectangleMapObject hero = (RectangleMapObject) env.getObjects().get("Hero");
        float x = hero.getRectangle().x + hero.getRectangle().width / 2;
        float y = hero.getRectangle().y + hero.getRectangle().height / 2;
        float w = hero.getRectangle().width / 2;
        float h = hero.getRectangle().height / 2;
        def.position.set(x, y);
        shape.setAsBox(w, h);
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 1;
        body = physics.world.createBody(def);
        body.createFixture(fixtureDef).setUserData("Kubik");

        shape.dispose();

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
        stand = new MyAtlasAnimation("atlas/fred.atlas", "stand", 2, false, "");
        walk = new MyAtlasAnimation("atlas/fred.atlas", "walk", 10, true, "single_on_dirty_stone_step_flip_flop_007_30443.mp3");
        jump = new MyAtlasAnimation("atlas/fred.atlas", "jump", 7, true, "cartoon-spring-boing-03.mp3");
        tmpA = stand;

        camera = new OrthographicCamera();
    }

    private void setCoordinates(BodyDef def, PolygonShape shape, Array<RectangleMapObject> rect, int i) {
        float x = rect.get(i).getRectangle().x + rect.get(i).getRectangle().width / 2;
        float y = rect.get(i).getRectangle().y + rect.get(i).getRectangle().height / 2;
        float w = rect.get(i).getRectangle().width / 2;
        float h = rect.get(i).getRectangle().height / 2;
        def.position.set(x, y);
        shape.setAsBox(w, h);
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

        tmpA = stand;
        dir = 0;

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            sound.play(0.1f, 1, 0);
        }
        if (myInputProcessor.getOutString().contains("A")) {
            dir = -1;
            tmpA = walk;
            body.applyForceToCenter(new Vector2(-10000, 0f), true);
        }
        if (myInputProcessor.getOutString().contains("D")) {
            dir = 1;
            tmpA = walk;
            body.applyForceToCenter(new Vector2(10000, 0f), true);
        }
        if (myInputProcessor.getOutString().contains("W")) {
            y++;
        }
        if (myInputProcessor.getOutString().contains("S")) {
            y--;
        }
        if (myInputProcessor.getOutString().contains("Space")) {
            body.applyForceToCenter(new Vector2(0, 1000f), true);
        }

        if (dir == -1) {
            x -= step;
        }
        if (dir == 1) {
            x += step;
        }

        tmpA.setTime(Gdx.graphics.getDeltaTime());
        TextureRegion tmp = tmpA.startAnimation();
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

        float x = body.getPosition().x - 2.5f / camera.zoom;
        float y = body.getPosition().y - 2.5f / camera.zoom;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(tmpA.startAnimation(), x, y);
        batch.end();

        if (!window.contains(rectangle)) {
            Gdx.graphics.setTitle("Out");
        } else {
            Gdx.graphics.setTitle("In");
        }
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
        stand.dispose();
        walk.dispose();
        jump.dispose();
        music.dispose();
        sound.dispose();
        map.dispose();
        mapRenderer.dispose();
        physics.dispose();
    }
}
