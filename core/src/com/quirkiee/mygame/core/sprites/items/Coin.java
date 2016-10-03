package com.quirkiee.mygame.core.sprites.items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.quirkiee.mygame.core.MyGame;
import com.quirkiee.mygame.core.scenes.Hud;
import com.quirkiee.mygame.core.screens.PlayScreen;

public class Coin extends Item {

	private float stateTime;
	private Animation spinAnimation;
	private Array<TextureRegion> frames;
	private boolean setToDestroy;
	private boolean destroyed;

	public Coin(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		frames = new Array<TextureRegion>();

		for (int i = 1; i < 4; i++)
			frames.add(new TextureRegion(screen.getAtlas().findRegion("coin"), i * 31, 0, 32, 32));

		spinAnimation = new Animation(0.15f, frames);
		stateTime = 0;
		setBounds(getX(), getY(), 32 / MyGame.PPM, 32 / MyGame.PPM);
		setToDestroy = false;
		destroyed = false;
	}

	@Override
	public void update(float dt) {
		stateTime += dt;
		if (setToDestroy && !destroyed) {
			world.destroyBody(b2body);
			destroyed = true;
			setRegion(new TextureRegion(screen.getAtlas().findRegion("coin"), 32, 0, 32, 32));
			stateTime = 0;
		} else if (!destroyed) {
			setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
			setRegion(spinAnimation.getKeyFrame(stateTime, true));
		}
	}

	@Override
	public void defineItem() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(getX(), getY());
		bdef.type = BodyDef.BodyType.StaticBody;
		b2body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(11 / MyGame.PPM);
		fdef.filter.categoryBits = MyGame.COIN_BIT;
		fdef.filter.maskBits = MyGame.NINJA_BIT;

		fdef.shape = shape;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData(this);

		shape.dispose();
	}

	@Override
	public void draw(Batch batch) {
		if (!destroyed || stateTime < 0.01f) {
			super.draw(batch);
		}
	}

	@Override
	public void use() {
		setToDestroy = true;
		Hud.addScore(100);

		// sound stuff
		MyGame.manager.get("audio/sounds/coin.wav", Sound.class).play();
	}

}
