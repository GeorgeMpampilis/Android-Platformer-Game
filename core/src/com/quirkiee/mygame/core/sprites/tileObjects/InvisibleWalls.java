package com.quirkiee.mygame.core.sprites.tileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.quirkiee.mygame.core.MyGame;
import com.quirkiee.mygame.core.screens.PlayScreen;

public class InvisibleWalls extends InteractiveTileObject {

	public InvisibleWalls(PlayScreen screen, MapObject object) {
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(MyGame.WALL_BIT);
	}
}
