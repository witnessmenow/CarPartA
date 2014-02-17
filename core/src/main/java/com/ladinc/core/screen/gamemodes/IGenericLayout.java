package com.ladinc.core.screen.gamemodes;

import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.StartingPosition;

public interface IGenericLayout {
	StartingPosition getTopStartPoint();
	
	StartingPosition getBottomStartPoint();
	
	void createWorld(World world);
}