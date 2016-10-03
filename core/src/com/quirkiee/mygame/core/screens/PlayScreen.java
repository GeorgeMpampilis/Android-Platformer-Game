package com.quirkiee.mygame.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.quirkiee.mygame.core.MyGame;
import com.quirkiee.mygame.core.scenes.Hud;
import com.quirkiee.mygame.core.sprites.Ninja;
import com.quirkiee.mygame.core.sprites.enemies.Enemy;
import com.quirkiee.mygame.core.sprites.items.Item;
import com.quirkiee.mygame.core.tools.B2DWorldCreator;
import com.quirkiee.mygame.core.tools.Controller;
import com.quirkiee.mygame.core.tools.WorldContactListener;

public class PlayScreen implements Screen {

	private MyGame game;
	private TextureAtlas atlas;

	private OrthographicCamera camera;
	private Viewport viewport;
	private Hud hud;

	Controller controller;

	// Tiled map variables
	private TiledMap map;
	private float tileSize;
	private int tileMapWidth;
	// private int tileMapHeight;
	private OrthogonalTiledMapRenderer tiledMapRenderer;

	// Box2d variables
	private World world;
	private Box2DDebugRenderer b2ddr;
	private B2DWorldCreator creator;

	// background color values
	private float valueR = 208.0f;
	private float valueG = 244.0f;
	private float valueB = 247.0f;
	private float colorR = valueR / 255f;
	private float colorG = valueG / 255f;
	private float colorB = valueB / 255f;

	public static int level;

	// debug flag
	private boolean debug = false;

	private Ninja player;

	private Music music;

	public PlayScreen(MyGame game) {
		atlas = new TextureAtlas("Ninja_and_Enemy.pack");
		this.game = game;

		// create the main camera that follows the player
		camera = new OrthographicCamera();
		viewport = new StretchViewport(MyGame.V_WIDTH / MyGame.PPM,
				MyGame.V_HEIGHT / MyGame.PPM, camera);

		// create the hud
		hud = new Hud(game.batch);

		// create the map
		createMap();

		// initially set the camera to be centered correctly at the start of the
		// map
		camera.position.set(MyGame.V_WIDTH / 2 / MyGame.PPM, MyGame.V_HEIGHT
				/ 2 / MyGame.PPM, 0);

		world = new World(new Vector2(0, -20), true);
		b2ddr = new Box2DDebugRenderer();

		creator = new B2DWorldCreator(this);

		player = new Ninja(this);

		world.setContactListener(new WorldContactListener());

		controller = new Controller(game.batch);
	}

	public void update(float dt) {
		handleInput(dt);
		player.fail();
		world.step(1 / 60f, 6, 2);
		player.update(dt);
		hud.update(dt);

		for (Item item : creator.getCoins()) {
			item.update(dt);
			if (item.getX() < player.getX() + 448 / MyGame.PPM)
				item.b2body.setActive(true);
		}
		for (Enemy enemy : creator.getRobots()) {
			enemy.update(dt);
			if (enemy.getX() < player.getX() + 448 / MyGame.PPM)
				enemy.b2body.setActive(true);
		}

		if (player.currentState != Ninja.State.DEAD)
			camera.position.x = player.b2body.getPosition().x;

		camera.update();

		// renderer draws only what the camera can see
		tiledMapRenderer.setView(camera);
	}

	public void handleInput(float dt) {
		if (player.currentState != Ninja.State.DEAD) {
			if ((controller.isUpPressed() || Gdx.input
					.isKeyJustPressed(Input.Keys.UP))
					&& (player.b2body.getLinearVelocity().y == 0))
				player.jump();
			if ((controller.isRightPressed() || Gdx.input
					.isKeyPressed(Input.Keys.RIGHT))
					&& player.b2body.getLinearVelocity().x <= 2)
				player.b2body.applyLinearImpulse(new Vector2(0.2f, 0),
						player.b2body.getWorldCenter(), true);
			if ((controller.isLeftPressed() || Gdx.input
					.isKeyPressed(Input.Keys.LEFT))
					&& player.b2body.getLinearVelocity().x >= -2)
				player.b2body.applyLinearImpulse(new Vector2(-0.2f, 0),
						player.b2body.getWorldCenter(), true);
		}
	}

	@Override
	public void render(float delta) {
		update(delta);

		// Clear the game screen
		Gdx.gl.glClearColor(colorR, colorG, colorB, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// render the game map
		tiledMapRenderer.render();

		// render the b2d debug lines
		if (debug) {
			b2ddr.render(world, camera.combined);
		}

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		player.draw(game.batch);
		for (Item item : creator.getCoins())
			item.draw(game.batch);
		for (Enemy enemy : creator.getRobots())
			enemy.draw(game.batch);
		game.batch.end();

		// set the batch to draw what the Hud camera sees
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
		controller.draw();

		if (gameOver()) {
			game.setScreen(new GameOverScreen(game));
			dispose();
		} else if (gameWin()) {
			game.setScreen(new LevelSelectScreen(game));
			dispose();
		}

	}

	public void createMap() {
		// load the map and setup the map renderer
		try {
			map = new TmxMapLoader().load("maps/level" + level + ".tmx");
		} catch (Exception e) {
			System.out.println("Cannot find file: maps/level" + level + ".tmx");
			Gdx.app.exit();
		}
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MyGame.PPM);
		tileMapWidth = map.getProperties().get("width", Integer.class);
		// tileMapHeight = map.getProperties().get("height", Integer.class);
		tileSize = map.getProperties().get("tilewidth", Integer.class);
	}

	public boolean gameOver() {
		if (player.currentState == Ninja.State.DEAD
				&& player.getStateTimer() > 1.5f) {
			return true;
		}
		return false;
	}

	public boolean gameWin() {
		if (player.b2body.getPosition().x * MyGame.PPM > (tileMapWidth - 1)
				* tileSize)
			return true;
		return false;
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		controller.resize(width, height);

	}

	public TextureAtlas getAtlas() {
		return atlas;
	}

	public TiledMap getMap() {
		return map;
	}

	public World getWorld() {
		return world;
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		map.dispose();
		tiledMapRenderer.dispose();
		world.dispose();
		b2ddr.dispose();
		hud.dispose();
		atlas.dispose();
	}

}
