package com.quirkiee.mygame.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.quirkiee.mygame.core.MyGame;
import com.quirkiee.mygame.core.tools.LevelSelector;
import com.quirkiee.mygame.core.tools.OverlapTester;

public class LevelSelectScreen implements Screen {

	private Viewport viewport;
	private Stage stage;
	private OrthographicCamera guiCam;

	LevelSelector levelSelector;

	private float valueR = 208.0f;
	private float valueG = 244.0f;
	private float valueB = 247.0f;
	private float colorR = valueR / 255f;
	private float colorG = valueG / 255f;
	private float colorB = valueB / 255f;

	private Texture backButton;
	private Rectangle backBounds;
	private Vector3 touchPoint;

	private MyGame game;

	public LevelSelectScreen(MyGame game) {
		this.game = game;

		viewport = new StretchViewport(MyGame.V_WIDTH, MyGame.V_HEIGHT,
				new OrthographicCamera());
		stage = new Stage(viewport, ((MyGame) game).batch);

		guiCam = new OrthographicCamera(MyGame.V_WIDTH, MyGame.V_HEIGHT);
		guiCam.position.set(MyGame.V_WIDTH / 2, MyGame.V_HEIGHT / 2, 0);

		backButton = new Texture("images/menu/backButton.png");
		backBounds = new Rectangle(
				(MyGame.V_WIDTH / 2) - (backButton.getWidth() / 2),
				((MyGame.V_HEIGHT / 2) - (backButton.getHeight() / 2))
						- ((MyGame.V_HEIGHT / 2) - (backButton.getHeight() / 2)),
				210, 40);
		touchPoint = new Vector3();

		levelSelector = new LevelSelector(game.batch);

	}

	public void update(float dt) {
		handleLevels();
		handleInput();

	}

	public void handleInput() {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));

			if (OverlapTester.pointInRectangle(backBounds, touchPoint.x,
					touchPoint.y)) {
				MyGame.manager.get("audio/sounds/menuButton.wav", Sound.class)
						.play();
				game.setScreen(new MainMenuScreen(game));
				dispose();
				return;
			}
		}
	}

	public void handleLevels() {
		if (levelSelector.isLevel1()) {
			MyGame.manager.get("audio/sounds/menuButton.wav", Sound.class)
					.play();
			game.setScreen(new PlayScreen(game));
			dispose();
		} else if (levelSelector.isLevel2()) {
			MyGame.manager.get("audio/sounds/menuButton.wav", Sound.class)
					.play();
			game.setScreen(new PlayScreen(game));
			dispose();
		} else if (levelSelector.isLevel3()) {
			MyGame.manager.get("audio/sounds/menuButton.wav", Sound.class)
					.play();
			game.setScreen(new PlayScreen(game));
			dispose();
		}
	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(colorR, colorG, colorB, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		levelSelector.draw();
		stage.draw();

		game.batch.setProjectionMatrix(guiCam.combined);

		game.batch.begin();
		game.batch
				.draw(backButton,
						(MyGame.V_WIDTH / 2) - (backButton.getWidth() / 2),
						((MyGame.V_HEIGHT / 2) - (backButton.getHeight() / 2))
								- ((MyGame.V_HEIGHT / 2) - (backButton
										.getHeight() / 2)));
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		levelSelector.resize(width, height);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
		backButton.dispose();
	}

}
