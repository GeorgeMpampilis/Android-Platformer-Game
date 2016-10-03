package com.quirkiee.mygame.core.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.quirkiee.mygame.core.MyGame;
import com.quirkiee.mygame.core.tools.LevelSelector;

public class Hud implements Disposable {

	public Stage stage;
	private Viewport viewport;
	private static Integer score;

	// Scene2D widgets
	private static Label scoreLabel;
	private Label ninjaLabel;

	LevelSelector levelSelector;

	public Hud(SpriteBatch sb) {
		score = 0;

		// setup the HUD viewport using a new camera seperate from the game
		// camera
		// define the stage using that viewport and the game's spritebatch
		viewport = new FitViewport(MyGame.V_WIDTH, MyGame.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, sb);

		// define a table used to organize the hud's labels
		Table table = new Table();
		// align table to top
		table.top().left();
		// make the table fill the entire stage
		table.setFillParent(true);

		// define the labels using the String, and a Label style consisting of a
		// font and color
		scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.RED));
		ninjaLabel = new Label("Score", new Label.LabelStyle(new BitmapFont(), Color.RED));

		// add the labels to the table, padding the top
		table.add(ninjaLabel).padTop(10).padLeft(10);
		table.row();
		table.add(scoreLabel).padLeft(10);

		// add the table to the stage
		stage.addActor(table);

	}

	public void update(float dt) {
	
	}

	public static void addScore(int value) {
		score += value;
		scoreLabel.setText(String.format("%06d", score));
	}

	public static Integer getScore() {
		return score;
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
