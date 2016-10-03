package com.quirkiee.mygame.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quirkiee.mygame.core.screens.MainMenuScreen;

public class MyGame extends Game {
	// Virtual Screen size
	public static final float V_WIDTH = 704;
	public static final float V_HEIGHT = 386;
	
	// b2d variable for scaling objects
	public static final float PPM = 100;

	// b2d variables for collision
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short NINJA_BIT = 2;
	public static final short COIN_BIT = 4;
	public static final short REMOVED_BIT = 8;
	public static final short ENEMY_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_HEAD_BIT = 64;
	public static final short NINJA_FOOT_BIT = 128;
	public static final short WALL_BIT = 256;

	public SpriteBatch batch;
	public BitmapFont font;
	
	public MainMenuScreen mainMenuScreen;
	public static AssetManager manager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();

		manager = new AssetManager();
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/enemyStomp.wav", Sound.class);
		manager.load("audio/sounds/gameOver.wav", Sound.class);
		manager.load("audio/sounds/menuButton.wav", Sound.class);
		manager.finishLoading();

		mainMenuScreen = new MainMenuScreen(this);
		setScreen(mainMenuScreen);
	}

	@Override
	public void render() {
		// fps counter at window title
		Gdx.graphics.setTitle(" -- FPS: " + Gdx.graphics.getFramesPerSecond());

		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		font.dispose();
		manager.dispose();
	}
}
