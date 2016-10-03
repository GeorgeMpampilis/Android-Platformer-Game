package com.quirkiee.mygame.core.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.quirkiee.mygame.core.MyGame;
import com.quirkiee.mygame.core.sprites.Ninja;
import com.quirkiee.mygame.core.sprites.enemies.Enemy;
import com.quirkiee.mygame.core.sprites.items.Item;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

		switch (cDef) {
		case MyGame.ENEMY_HEAD_BIT | MyGame.NINJA_BIT:
			if (fixA.getFilterData().categoryBits == MyGame.ENEMY_HEAD_BIT)
				((Enemy) fixA.getUserData()).hitOnHead((Ninja) fixB.getUserData());
			else
				((Enemy) fixB.getUserData()).hitOnHead((Ninja) fixA.getUserData());
			break;
		case MyGame.ENEMY_BIT | MyGame.OBJECT_BIT:
			if (fixA.getFilterData().categoryBits == MyGame.ENEMY_BIT)
				((Enemy) fixA.getUserData()).reverseVelocity(true, false);
			else
				((Enemy) fixB.getUserData()).reverseVelocity(true, false);
			break;
		case MyGame.ENEMY_BIT | MyGame.WALL_BIT:
			if (fixA.getFilterData().categoryBits == MyGame.ENEMY_BIT)
				((Enemy) fixA.getUserData()).reverseVelocity(true, false);
			else
				((Enemy) fixB.getUserData()).reverseVelocity(true, false);
			break;
		case MyGame.COIN_BIT | MyGame.NINJA_BIT:
			if (fixA.getFilterData().categoryBits == MyGame.COIN_BIT)
				((Item) fixA.getUserData()).use();
			else
				((Item) fixB.getUserData()).use();
			break;
		case MyGame.NINJA_BIT | MyGame.ENEMY_BIT:
			if (fixA.getFilterData().categoryBits == MyGame.NINJA_BIT)
				((Ninja) fixA.getUserData()).hitByEnemy((Enemy) fixB.getUserData());
			else
				((Ninja) fixB.getUserData()).hitByEnemy((Enemy) fixA.getUserData());
			break;
		case MyGame.ENEMY_BIT | MyGame.ENEMY_BIT:
			((Enemy) fixA.getUserData()).reverseVelocity(true, false);
			((Enemy) fixB.getUserData()).reverseVelocity(true, false);
			break;
		}
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
