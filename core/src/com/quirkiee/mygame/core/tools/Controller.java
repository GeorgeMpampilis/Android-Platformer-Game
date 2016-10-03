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

public class Controller {

	private Viewport viewport;
	public Stage stage;
	boolean upPressed, downPressed, leftPressed, rightPressed;
	private OrthographicCamera cam;

	public Controller(SpriteBatch sb) {
		cam = new OrthographicCamera();
		viewport = new StretchViewport(MyGame.V_WIDTH, MyGame.V_HEIGHT, cam);
		stage = new Stage(viewport, sb);

		Gdx.input.setInputProcessor(stage);

		Table table1 = new Table();
		Table table2 = new Table();
		table1.left().bottom().setDebug(false);
		table2.setFillParent(true);
		table2.right().bottom().setDebug(false);

		Image upImg = new Image(new Texture("images/controller/upArrow.png"));
		upImg.setSize(50, 50);
		upImg.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				upPressed = true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				upPressed = false;
			}

		});

		Image rightImg = new Image(new Texture("images/controller/rightArrow.png"));
		rightImg.setSize(50, 50);
		rightImg.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				rightPressed = true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				rightPressed = false;
			}
		});

		Image leftImg = new Image(new Texture("images/controller/leftArrow.png"));
		leftImg.setSize(50, 50);
		leftImg.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				leftPressed = true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				leftPressed = false;
			}
		});

		table1.row().pad(3, 3, 3, 3);
		table1.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
		table1.add();
		table1.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());
		table1.row().padBottom(3);
		table1.add();

		table2.row().pad(3, 3, 3, 3);
		table2.add(upImg).size(upImg.getWidth(), upImg.getHeight());
		table2.add();
		table2.row().padBottom(3);
		table2.add();

		stage.addActor(table1);
		stage.addActor(table2);
	}

	public void draw() {
		stage.draw();
	}

	public boolean isUpPressed() {
		return upPressed;
	}

	public boolean isLeftPressed() {
		return leftPressed;
	}

	public boolean isRightPressed() {
		return rightPressed;
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}
