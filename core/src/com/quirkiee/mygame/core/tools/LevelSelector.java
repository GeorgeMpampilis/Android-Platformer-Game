package com.quirkiee.mygame.core.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.quirkiee.mygame.core.MyGame;
import com.quirkiee.mygame.core.screens.PlayScreen;

public class LevelSelector {

	private Viewport viewport;
	private Stage stage;
	private OrthographicCamera cam;

	boolean chooseLevel1, chooseLevel2, chooseLevel3;

	public LevelSelector(SpriteBatch sb) {
		cam = new OrthographicCamera();
		viewport = new StretchViewport(MyGame.V_WIDTH, MyGame.V_HEIGHT, cam);
		stage = new Stage(viewport, sb);

		Gdx.input.setInputProcessor(stage);

		Table table = new Table();
		table.center().top().setDebug(false);
		table.setFillParent(true);

		Image level1 = new Image(new Texture("images/menu/level1.png"));
		level1.setSize(80, 50);
		level1.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				chooseLevel1 = true;
				PlayScreen.level = 1;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				chooseLevel1 = false;
			}

		});

		Image level2 = new Image(new Texture("images/menu/level2.png"));
		level2.setSize(80, 50);
		level2.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				chooseLevel2 = true;
				PlayScreen.level = 2;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				chooseLevel2 = false;
			}

		});

		Image level3 = new Image(new Texture("images/menu/level3.png"));
		level3.setSize(80, 50);
		level3.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				chooseLevel3 = true;
				PlayScreen.level = 3;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				chooseLevel3 = false;
			}

		});

		Image level4 = new Image(new Texture("images/menu/level4.png"));
		level4.setSize(80, 50);

		Image level5 = new Image(new Texture("images/menu/level5.png"));
		level5.setSize(80, 50);

		Image level6 = new Image(new Texture("images/menu/level6.png"));
		level6.setSize(80, 50);

		table.add().pad(100, 50, 0, 50);
		table.add(level1).size(level1.getWidth(), level1.getHeight())
				.padTop(100);
		table.add().padRight(80);
		table.add(level2).size(level2.getWidth(), level2.getHeight())
				.padTop(100);
		table.add().padRight(80);
		table.add(level3).size(level3.getWidth(), level3.getHeight())
				.padTop(100);
		table.add().pad(100, 50, 0, 50);
		table.row();
		table.add().pad(100, 50, 0, 50);
		table.add(level4).size(level4.getWidth(), level4.getHeight())
				.padTop(100);
		table.add().padRight(80);
		table.add(level5).size(level5.getWidth(), level5.getHeight())
				.padTop(100);
		table.add().padRight(80);
		table.add(level6).size(level6.getWidth(), level6.getHeight())
				.padTop(100);
		table.add().pad(100, 50, 0, 50);

		stage.addActor(table);
	}

	public void draw() {
		stage.draw();
	}

	public boolean isLevel1() {
		return chooseLevel1;
	}

	public boolean isLevel2() {
		return chooseLevel2;
	}

	public boolean isLevel3() {
		return chooseLevel3;
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}
