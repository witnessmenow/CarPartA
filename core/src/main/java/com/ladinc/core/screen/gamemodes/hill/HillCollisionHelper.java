package com.ladinc.core.screen.gamemodes.hill;

import java.util.Iterator;
import java.util.LinkedHashSet;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;
import com.ladinc.core.objects.FloorTileSensor;
import com.ladinc.core.player.PlayerInfo;
import com.ladinc.core.screen.gamemodes.AbstractCollisionHelper;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.vehicles.Vehicle;

public class HillCollisionHelper extends AbstractCollisionHelper {
	
	/*
	 * Bug:
	 * 
	 * If the hill switches and there is a person in the hill, it won't count
	 * down for that player or switch colour.
	 * 
	 * Bug:
	 * 
	 * If the hill switches and there is two+ people in it and one player
	 * leaves, the other players won't be recorded -> Very small chance of
	 * occuring.
	 * 
	 * Bug:
	 * 
	 * The AI is bad. It goes for the hill but doesn't slow down for it. If a
	 * player is in the hill the computer will attack the other players
	 * 
	 * Bug:
	 * 
	 * Refactor/Redesign how some things are implemented
	 */
	public Team currentHillSide;
	
	public LinkedHashSet<PlayerInfo> carsOnHill = new LinkedHashSet<PlayerInfo>();
	
	@Override
	public void beginContact(Contact contact)
	{
		CollisionInfo bodyAInfo = getCollisionInfoFromFixture(contact
				.getFixtureA());
		CollisionInfo bodyBInfo = getCollisionInfoFromFixture(contact
				.getFixtureB());
		
		if (bodyAInfo != null && bodyBInfo != null)
		{
			if (isTwoVehilesColliding(bodyAInfo, bodyBInfo))
			{
				// collideCars(bodyAInfo, bodyBInfo);
			}
			else
			{
				assignHillToTeam(bodyAInfo, bodyBInfo);
			}
		}
	}
	
	@Override
	public void endContact(Contact contact)
	{
		removeHillIfLastPersonOnHill(contact);
	}
	
	public void handleHillMove()
	{
		this.carsOnHill.clear();
		this.currentHillSide = null;
	}
	
	public Team retrieveTeamOnHill()
	{
		if (currentHillSide != null)
		{
			Iterator<PlayerInfo> itr = carsOnHill.iterator();
			
			if (!itr.hasNext())
			{
				currentHillSide = null;
			}
			else
			{
				while (itr.hasNext())
				{
					PlayerInfo playerInfoOnHill = itr.next();
					if (currentHillSide.equals(playerInfoOnHill.team))
					{
						// Don't need to switch Hill team
						break;
					}
					if (!itr.hasNext())
					{
						currentHillSide = playerInfoOnHill.team;
					}
				}
			}
		}
		
		return currentHillSide;
	}
	
	private void removeHillIfLastPersonOnHill(Contact contact)
	{
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		CollisionInfo bodyAInfo = getCollisionInfoFromFixture(fixtureA);
		CollisionInfo bodyBInfo = getCollisionInfoFromFixture(fixtureB);
		if (bodyAInfo != null && bodyBInfo != null)
		{
			if (isVehicleAndFloorSensor(bodyAInfo, bodyBInfo))
			{
				unassignFloorTileSensors((FloorTileSensor) bodyAInfo.object,
						(Vehicle) bodyBInfo.object);
			}
			else if (isVehicleAndFloorSensor(bodyBInfo, bodyAInfo))
			{
				unassignFloorTileSensors((FloorTileSensor) bodyBInfo.object,
						(Vehicle) bodyAInfo.object);
			}
		}
	}
	
	private void assignHillToTeam(CollisionInfo bodyAInfo,
			CollisionInfo bodyBInfo)
	{
		if (isVehicleAndFloorSensor(bodyAInfo, bodyBInfo))
		{
			assignFloorTileSensors((Vehicle) bodyBInfo.object,
					(FloorTileSensor) bodyAInfo.object);
		}
		else if (isVehicleAndFloorSensor(bodyBInfo, bodyAInfo))
		{
			assignFloorTileSensors((Vehicle) bodyAInfo.object,
					(FloorTileSensor) bodyBInfo.object);
		}
	}
	
	private void assignFloorTileSensors(Vehicle vehicle,
			FloorTileSensor floorTileSensor)
	{
		// the assigned floor till is the hill
		if (floorTileSensor.assigned)
		{
			if (currentHillSide == null)
			{
				setTeamForHillTile(vehicle, floorTileSensor);
			}
			addCarToCarsOnHill(vehicle);
		}
		else
		{
			// assignFloorTileSensor(floorTileSensor, vehicle.player);
		}
	}
	
	private boolean isVehicleAndFloorSensor(CollisionInfo bodyAInfo,
			CollisionInfo bodyBInfo)
	{
		return bodyAInfo.type == CollisionObjectType.FloorSensor
				&& bodyBInfo.type == CollisionObjectType.Vehicle;
	}
	
	private void addCarToCarsOnHill(Vehicle vehicle)
	{
		carsOnHill.add(vehicle.player);
	}
	
	private void removeCarFromCarsOnHill(PlayerInfo player)
	{
		carsOnHill.remove(player);
	}
	
	private void setTeamForHillTile(Vehicle vehicle,
			FloorTileSensor floorTileSensor)
	{
		floorTileSensor.setTeam(vehicle.player.team);
		currentHillSide = vehicle.player.team;
	}
	
	private void unassignFloorTileSensors(FloorTileSensor floorTileSensor,
			Vehicle vehicle)
	{
		unassignHill(floorTileSensor, vehicle.player);
	}
	
	private void assignFloorTileSensor(FloorTileSensor floorTileSensor,
			PlayerInfo playerInfo)
	{
		floorTileSensor.setTeam(playerInfo.team);
	}
	
	private void unassignFloorTileSensor(FloorTileSensor floorTileSensor)
	{
		floorTileSensor.setTeam(null);
	}
	
	private void unassignHill(FloorTileSensor floorTileSensor,
			PlayerInfo playerInfo)
	{
		if (floorTileSensor.assigned)
		{
			removeCarFromCarsOnHill(playerInfo);
			floorTileSensor.setTeam(retrieveTeamOnHill());
		}
		else
		{
			// unassignFloorTileSensor(floorTileSensor);
		}
	}
	
	private void collideCars(CollisionInfo bodyAInfo, CollisionInfo bodyBInfo)
	{
		Vehicle v1 = (Vehicle) bodyAInfo.object;
		Vehicle v2 = (Vehicle) bodyBInfo.object;
		
		if (v1.getSpeedKMH() > v2.getSpeedKMH())
		{
			spinAndMoveBackVehicle(v2);
		}
		else
		{
			spinAndMoveBackVehicle(v1);
		}
	}
	
	private void spinAndMoveBackVehicle(Vehicle vehicle)
	{
		if (vehicle.spinoutTimeRemaining <= 0f)
		{
			vehicle.spinoutTimeRemaining = 1.5f;
		}
	}
}