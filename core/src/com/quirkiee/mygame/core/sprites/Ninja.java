package com.quirkiee.mygame.core.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.quirkiee.mygame.core.MyGame;
import com.quirkiee.mygame.core.screens.PlayScreen;
import com.quirkiee.mygame.core.sprites.enemies.Enemy;

public class Ninja extends Sprite {
	public enum State {
		FALLING, JUMPING, STANDING, RUNNING, DEAD
	};

	public State currentState;
	public State previousState;

	public World world;
	public Body b2body;

	private TextureRegion ninjaStand;
	private TextureRegion ninjaJump;
	private TextureRegion ninjaDead;
	private Animation ninjaRun;

	private float stateTimer;
	private boolean runningRight;
	private boolean ninjaIsDead;

	public Ninja(PlayScreen screen) {
		this.world = screen.getWorld();
		currentState = State.STANDING;
		previousState = State.STANDING;
		stateTimer = 0;
		runningRight = true;

		Array<TextureRegion> frames = new Array<TextureRegion>();

		for (int i = 1; i < 5; i++)
			frames.add(new TextureRegion(screen.getAtlas().findRegion(
					"ninja_ani"), i * 32, 0, 32, 32));
		ninjaRun = new Animation(0.1f, frames);
		frames.clear();

		ninjaJump = new TextureRegion(
				screen.getAtlas().findRegion("ninja_ani"), 288, 0, 32, 32);
		frames.clear();

		ninjaStand = new TextureRegion(screen.getAtlas()
				.findRegion("ninja_ani"), 0, 0, 32, 32);

		ninjaDead = new TextureRegion(
				screen.getAtlas().findRegion("ninja_ani"), 256, 0, 32, 32);

		// create ninja in box2d
		defineNinja();

		// initial values for ninja's position
		setBounds(0, 0, 32 / MyGame.PPM, 32 / MyGame.PPM);
		setRegion(ninjaStand);
	}

	public void update(float dt) {
		setPosition(b2body.getPosition().x - getWidth() / 2,
				b2body.getPosition().y - getHeight() / 2);
		setRegion(getFrame(dt));
	}

	public void defineNinja() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(32 / MyGame.PPM, 32 / MyGame.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(12 / MyGame.PPM);
		fdef.filter.categoryBits = MyGame.NINJA_BIT;
		fdef.filter.maskBits = MyGame.GROUND_BIT | MyGame.OBJECT_BIT
				| MyGame.COIN_BIT | MyGame.ENEMY_BIT | MyGame.ENEMY_HEAD_BIT;
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);

		// create foot sensor
		EdgeShape foot = new EdgeShape();
		foot.set(new Vector2(-4 / MyGame.PPM, -13 / MyGame.PPM), new Vector2(
				4 / MyGame.PPM, -13 / MyGame.PPM));
		fdef.filter.categoryBits = MyGame.NINJA_FOOT_BIT;
		fdef.shape = foot;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData(this);
	}

	public TextureRegion getFrame(float dt) {
		currentState = getState();
		TextureRegion region;

		// return the correct texture region depending on the current state
		switch (currentState) {
		case DEAD:
			region = ninjaDead;
			break;
		case JUMPING:
			region = ninjaJump;
			break;
		case RUNNING:
			region = ninjaRun.getKeyFrame(stateTimer, true);
			break;
		case FALLING:
		case STANDING:
		default:
			region = ninjaStand;
			break;
		}

		if ((b2body.getLinearVelocity().x < 0 || !runningRight)
				&& !region.isFlipX()) {
			region.flip(true, false);
			runningRight = false;
		} else if ((b2body.getLinearVelocity().x > 0 || runningRight)
				&& region.isFlipX()) {
			region.flip(true, false);
			runningRight = true;
		}

		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		return region;
	}

	public State getState() {
		// if ninja got hit he died
		if (ninjaIsDead)
			return State.DEAD;
		// Checking velocity on the X and Y-Axis
		// if ninja is going positive in Y-Axis he is jumping... or if he just
		// jumped and is falling remain in jump state
		else if ((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING)
				|| (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
			return State.JUMPING;
		// if negative in Y-Axis ninja is falling
		else if (b2body.getLinearVelocity().y < 0)
			return State.FALLING;
		// if ninja is positive or negative in the X axis he is running
		else if (b2body.getLinearVelocity().x != 0)
			return State.RUNNING;
		// if none of these return then he must be standing
		else
			return State.STANDING;
	}

	public void jump() {
		b2body.applyLinearImpulse(new Vector2(0, 6.6f),
				b2body.getWorldCenter(), true);
		currentState = State.JUMPING;
	}

	public void hitByEnemy(Enemy enemy) {
		ninjaIsDead = true;
		Filter filter = new Filter();
		filter.maskBits = MyGame.NOTHING_BIT;
		for (Fixture fixture : b2body.getFixtureList())
			fixture.setFilterData(filter);
		b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(),
				true);

		MyGame.manager.get("audio/sounds/gameOver.wav", Sound.class).play();
	}

	public void fail() {
		if (b2body.getPosition().y < 0 && !ninjaIsDead) {
			ninjaIsDead = true;
			MyGame.manager.get("audio/sounds/gameOver.wav", Sound.class).play();
		}
	}

	public float getStateTimer() {
		return stateTimer;
	}
}
