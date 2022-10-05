package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Physics {
    public final MyContactListener contactListener;
    public static final float PPM = 80;
    final World world;
    private final Box2DDebugRenderer debugRenderer;

    public Physics() {
        world = new World(new Vector2(0, -9.81f), true);
        debugRenderer = new Box2DDebugRenderer();
        contactListener = new MyContactListener();
        world.setContactListener(contactListener);
    }

    public Array<Body> getBodies(String name) {
        Array<Body> tmp = new Array<>();
        world.getBodies(tmp);
        Iterator<Body> iterator = tmp.iterator();
        while (iterator.hasNext()) {
            Body body = iterator.next();
            if (!body.getUserData().equals("coins")) {
                iterator.remove();
            }
        }
        return tmp;
    }

    public Body addObject(RectangleMapObject object) {
        Rectangle rect = object.getRectangle();
        String type = (String) object.getProperties().get("BodyType");
        BodyDef def = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        if (type.equals("StaticBody")) def.type = BodyDef.BodyType.StaticBody;
        if (type.equals("DynamicBody")) def.type = BodyDef.BodyType.DynamicBody;

        def.position.set((rect.x + rect.width / 2) / PPM, (rect.y + rect.height / 2) / PPM);
        def.gravityScale = (float) object.getProperties().get("gravityScale");

        polygonShape.setAsBox(rect.width / 2 / PPM, rect.height / 2 / PPM);

        fixtureDef.shape = polygonShape;
        fixtureDef.friction = (float) object.getProperties().get("friction");
        fixtureDef.density = 1;
        fixtureDef.restitution = (float) object.getProperties().get("restitution");

        String name = "";
        if (object.getName() != null) name = object.getName();
        Body body;
        body = world.createBody(def);
        body.setUserData(name);
        body.createFixture(fixtureDef).setUserData(name);

        if (name.equals("Hero")) {
            polygonShape.setAsBox(rect.width / 3 / PPM, rect.height / 10 / PPM, new Vector2(0, -rect.width / 2), 0);
            body.createFixture(fixtureDef).setUserData("legs");
            body.getFixtureList().get(1).setSensor(true);
        }

        polygonShape.dispose();
        return body;
    }

    public void addDmgObjects(RectangleMapObject object) {
        Rectangle rect = object.getRectangle();
        String type = (String) object.getProperties().get("BodyType");
        BodyDef def = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        def.type = BodyDef.BodyType.StaticBody;
        def.position.set((rect.x + rect.width / 2) / PPM, (rect.y + rect.height / 2) / PPM);
        polygonShape.setAsBox(rect.width / 2 / PPM, rect.height / 2 / PPM);
        fixtureDef.shape = polygonShape;

        String name = "damageGround";
        Body body;
        body = world.createBody(def);
        body.setUserData(name);
        body.createFixture(fixtureDef).setUserData(name);
        body.getFixtureList().get(0).setSensor(true);
        body.createFixture(fixtureDef).setUserData(name);

        polygonShape.dispose();
    }


    public void removeBody(Body body) {
        world.destroyBody(body);
    }

    public void debugDraw(OrthographicCamera camera) {
        debugRenderer.render(world, camera.combined);
    }

    public void step() {
        world.step(1 / 60f, 3, 3);
    }

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        MyContactListener.isDamage = false;
        contactListener.dispose();
    }
}
