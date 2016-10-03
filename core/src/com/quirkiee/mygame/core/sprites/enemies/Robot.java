package com.quirkiee.mygame.core.sprites.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.quirkiee.mygame.core.MyGame;
import com.quirkiee.mygame.core.scenes.Hud;
import com.quirkiee.mygame.core.screens.PlayScreen;
import com.quirkiee.mygame.core.sprites.Ninja;

public class Robot extends Enemy {

	private float stateTime;
	private Animation walkAnimation;
	private Array<TextureRegion> frames;
	private boolean setToDestroy;
	private boolean destroyed;

	public Robot(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		frames = new Array<TextureRegion>();

		for (int i = 1; i < 4; i++)
			frames.add(new TextureRegion(screen.getAtlas().findRegion(
					"enemy_walk_ani"), i * 32, 0, 32, 32));

		walkAnimation = new Animation(0.1f, frames);
		stateTime = 0;
		setBounds(getX(), getY(), 32 / MyGame.PPM, 32 / MyGame.PPM);
		setToDestroy = false;
		destroyed = false;
	}

	public void update(float dt) {
		stateTime += dt;
		setRegion(getFrame(dt));
		if (setToDestroy && !destroyed) {
			world.destroyBody(b2body);
			destroyed = true;
			setRegion(new TextureRegion(screen.getAtlas().findRegion(
					"enemy_walk_ani"), 32, 0, 32, 32));
			stateTime = 0;
		} else if (!destroyed) {
			b2body.setLinearVelocity(velocity);
			setPosition(b2body.getPosition().x - getWidth() / 2,
					b2body.getPosition().y - getHeight() / 2);
			setRegion(walkAnimation.getKeyFrame(stateTime, true));
		}
	}

	@Override
	protected void defineEnemy() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(getX(), getY());
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(13 / MyGame.PPM);
		fdef.filter.categoryBits = MyGame.ENEMY_BIT;
		fdef.filter.maskBits = MyGame.GROUND_BIT | MyGame.OBJECT_BIT
				| MyGame.ENEMY_BIT | MyGame.NINJA_BIT | MyGame.WALL_BIT;

		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);

		// create the head of the enemy
		PolygonShape head = new PolygonShape();
		Vector2[] vertice = new Vector2[4];
		vertice[0] = new Vector2(-12, 14).scl(1 / MyGame.PPM);
		vertice[1] = new Vector2(12, 14).scl(1 / MyGame.PPM);
		vertice[2] = new Vector2(-3, 8).scl(1 / MyGame.PPM);
		vertice[3] = new Vector2(3, 8).scl(1 / MyGame.PPM);
		head.set(vertice);

		fdef.shape = head;
		fdef.restitution = 0.5f;
		fdef.filter.categoryBits = MyGame.ENEMY_HEAD_BIT;
		b2body.createFixture(fdef).setUserData(this);
		
		shape.dispose();
	}

	@Override
	public void draw(Batch batch) {
		if (!destroyed || stateTime < 0.2f) {
			super.draw(batch);
		}
	}

	@Override
	public void hitOnHead(Ninja ninja) {
		setToDestroy = true;
		Hud.addScore(200);	
		MyGame.manager.get("audio/sounds/enemyStomp.wav", Sound.class).play();
	}
	
	public TextureRegion getFrame(float dt){
		TextureRegion region = walkAnimation.getKeyFrame(stateTime, true);
		
		if ((b2body.getLinearVelocity().x > 0)
				&& !region.isFlipX()) {
			region.flip(true, false);
		} else if ((b2body.getLinearVelocity().x < 0)
				&& region.isFlipX()) {
			region.flip(true, false);
		}
        
        return region;
	}

}
