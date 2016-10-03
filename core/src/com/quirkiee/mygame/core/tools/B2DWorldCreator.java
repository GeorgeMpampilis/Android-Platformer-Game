package com.quirkiee.mygame.core.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.quirkiee.mygame.core.MyGame;
import com.quirkiee.mygame.core.screens.PlayScreen;
import com.quirkiee.mygame.core.sprites.enemies.Robot;
import com.quirkiee.mygame.core.sprites.items.Coin;
import com.quirkiee.mygame.core.sprites.tileObjects.InvisibleWalls;

public class B2DWorldCreator {

	private Array<Coin> coins;
	private Array<Robot> robots;

	public B2DWorldCreator(PlayScreen screen) {
		World world = screen.getWorld();
		TiledMap map = screen.getMap();

		// create body and fixture variables
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;

		// create ground bodies/fixtures
		for (MapObject object : map.getLayers().get("Ground").getObjects()
				.getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / MyGame.PPM,
					(rect.getY() + rect.getHeight() / 2) / MyGame.PPM);

			body = world.createBody(bdef);

			shape.setAsBox(rect.getWidth() / 2 / MyGame.PPM, rect.getHeight()
					/ 2 / MyGame.PPM);
			fdef.shape = shape;
			body.createFixture(fdef);

		}

		// create boxes bodies/fixtures
		for (MapObject object : map.getLayers().get("Boxes").getObjects()
				.getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / MyGame.PPM,
					(rect.getY() + rect.getHeight() / 2) / MyGame.PPM);

			body = world.createBody(bdef);

			shape.setAsBox(rect.getWidth() / 2 / MyGame.PPM, rect.getHeight()
					/ 2 / MyGame.PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = MyGame.OBJECT_BIT;
			body.createFixture(fdef);
		}

		// create coins
		coins = new Array<Coin>();
		for (MapObject object : map.getLayers().get("Coins").getObjects()
				.getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			coins.add(new Coin(screen, rect.x / MyGame.PPM, rect.y / MyGame.PPM));
		}

		// create robots
		robots = new Array<Robot>();
		for (MapObject object : map.getLayers().get("Robots").getObjects()
				.getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			robots.add(new Robot(screen, rect.x / MyGame.PPM, rect.y
					/ MyGame.PPM));
		}

		// create invisible walls bodies/fixtures
		for (MapObject object : map.getLayers().get("InvisibleWalls")
				.getObjects().getByType(RectangleMapObject.class)) {
			new InvisibleWalls(screen, object);
		}
	}

	public Array<Coin> getCoins() {
		return coins;
	}

	public void setCoins(Array<Coin> coins) {
		this.coins = coins;
	}

	public void setRobots(Array<Robot> robots) {
		this.robots = robots;
	}

	public Array<Robot> getRobots() {
		return robots;
	}
}
