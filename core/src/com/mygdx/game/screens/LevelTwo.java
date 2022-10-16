package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Label;
import com.mygdx.game.MyContactListener;
import com.mygdx.game.MyInputProcessor;
import com.mygdx.game.Physics;
import com.mygdx.game.animation.MyAnimation;
import com.mygdx.game.persons.Player;

import java.util.ArrayList;
import java.util.List;

public class LevelTwo implements Screen {

    Game game;
    private SpriteBatch batch;
    private final Texture img;
    private final MyAnimation coinAnimation;
    private Music music;
    private Sound sound;
    private MyInputProcessor myInputProcessor;
    private OrthographicCamera camera;
    private Physics physics;
    private Body body;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private int[] front, tL;
    private final Player player;
    public static List<Body> bodyToDelete;
    private Label font;


    public LevelTwo(Game game) {
        font = new Label(12);

        bodyToDelete = new ArrayList<>();
        coinAnimation = new MyAnimation("coin-sprite-animation.png", 1, 10, 6, Animation.PlayMode.LOOP);
        this.game = game;
        map = new TmxMapLoader().load("map/безымянный.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        front = new int[1];
        front[0] = map.getLayers().getIndex("front");
        tL = new int[1];
        tL[0] = map.getLayers().getIndex("t0");
        physics = new Physics();

        Array<RectangleMapObject> objects = map.getLayers().get("env").getObjects().getByType(RectangleMapObject.class);
        objects.addAll(map.getLayers().get("dyn").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            physics.addObject(objects.get(i));
        }

        objects.clear();
        objects.addAll(map.getLayers().get("damageGround").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            physics.addDmgObjects(objects.get(i));
        }

        body = physics.addObject((RectangleMapObject) map.getLayers().get("hero").getObjects().get("Hero"));
        body.setFixedRotation(true);
        player = new Player(body);

        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/stray-the-way-you-compute-tonight.mp3"));
        music.setVolume(0.05f);
        music.setLooping(true);
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/stray-droid-reaction-heart-sound-effect.mp3"));

        batch = new SpriteBatch();
        img = new Texture("2109.w023.n001.1126B.p1.1126.jpg");

        camera = new OrthographicCamera();
        camera.zoom = 0.45f;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        camera.position.x = body.getPosition().x * Physics.PPM + myInputProcessor.getCameraX();
        camera.position.y = body.getPosition().y * Physics.PPM + myInputProcessor.getCameraY();
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render(tL);

        player.setTime(delta);
        Vector2 vector = myInputProcessor.getVector();
        if (MyContactListener.cnt < 1) {
            vector.set(vector.x, 0);
        }

        body.applyForceToCenter(vector, true);
        player.setFPS(body.getLinearVelocity(), true);

        Rectangle tmp = player.getRect(camera, player.getFrame());
        ((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(tmp.width / 2, tmp.height / 2);
        ((PolygonShape) body.getFixtureList().get(1).getShape()).setAsBox(
                tmp.width / 3, tmp.height / 10,
                new Vector2(0, -tmp.height / 2), 0);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "HP " + player.getHit(0), (int) tmp.x, (int) (tmp.y + tmp.height * Physics.PPM));
//        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(player.getFrame(), tmp.x, tmp.y, tmp.width * Physics.PPM, tmp.height * Physics.PPM);

        Array<Body> bodies = physics.getBodies("coins");
        coinAnimation.setTime(delta);
        TextureRegion tr = coinAnimation.startAnimation();
        float dScale = 6;
        for (Body bd : bodies) {
            float cx = bd.getPosition().x * Physics.PPM - tr.getRegionWidth() / 2 / dScale;
            float cy = bd.getPosition().y * Physics.PPM - tr.getRegionHeight() / 2 / dScale;
            float cW = tr.getRegionWidth() / Physics.PPM / dScale;
            float cH = tr.getRegionHeight() / Physics.PPM / dScale;
            ((PolygonShape) bd.getFixtureList().get(0).getShape()).setAsBox(cW / 5, cH / 5);
            batch.draw(tr, cx, cy, cW * Physics.PPM, cH * Physics.PPM);
        }
        batch.end();

        mapRenderer.render(front);

        for (Body bd : bodyToDelete) {
            physics.removeBody(bd);
        }
        bodyToDelete.clear();

        physics.step();
        physics.debugDraw(camera);

        if (MyContactListener.count >= 15) {
            dispose();
            game.setScreen(new VictoryScreen(game));
        }

        if (MyContactListener.isDamage) {
            if (player.getHit(1) < 1) {
                dispose();
                game.setScreen(new GameOverScreen(game));
                MyContactListener.count = 0;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
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
        batch.dispose();
        img.dispose();
        coinAnimation.dispose();
        music.dispose();
        sound.dispose();
        map.dispose();
        mapRenderer.dispose();
        this.physics.dispose();
        this.player.dispose();
        this.font.dispose();
        game.dispose();

    }
}
