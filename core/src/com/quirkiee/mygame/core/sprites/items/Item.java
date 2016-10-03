package com.quirkiee.mygame.core.sprites.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.quirkiee.mygame.core.screens.PlayScreen;

public abstract class Item extends Sprite {
	protected PlayScreen screen;
	protected World world;
	public Body b2body;

	public Item(PlayScreen screen, float x, float y) {
		this.world = screen.getWorld();
		this.screen = screen;
		setPosition(x, y);
		defineItem();
		b2body.setActive(false);
	}

	protected abstract void defineItem();

	public abstract void use();

	public abstract void update(float dt);

}
