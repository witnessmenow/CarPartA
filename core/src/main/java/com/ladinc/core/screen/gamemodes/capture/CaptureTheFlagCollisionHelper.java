package com.ladinc.core.screen.gamemodes.capture;

import com.badlogic.gdx.physics.box2d.Contact;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;
import com.ladinc.core.objects.FloorTileSensor;
import com.ladinc.core.screen.gamemodes.AbstractCollisionHelper;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.vehicles.Vehicle;

public class CaptureTheFlagCollisionHelper extends AbstractCollisionHelper {
	public Vehicle vehicleWithHomeFlag;
	public Vehicle vehicleWithAwayFlag;
	
	public int homeTeamScore = 0;
	public int awayTeamScore = 0;
	
	@Override
	public void beginContact(Contact contact)
	{
		CollisionInfo bodyAInfo = getCollisionInfoFromFixture(contact
				.getFixtureA());
		CollisionInfo bodyBInfo = getCollisionInfoFromFixture(contact
				.getFixtureB());
		
		if (bodyAInfo != null && bodyBInfo != null)
		{
			if (isTwoVehiclesColliding(bodyAInfo, bodyBInfo))
			{
				// Reset Flag if Vehicle has it
				// collideCars(bodyAInfo, bodyBInfo);
				Vehicle vehicleA = (Vehicle) bodyAInfo.object;
				Vehicle vehicleB = (Vehicle) bodyBInfo.object;
				if (vehicleA.player.hasOpponentsFlag
						|| vehicleB.player.hasOpponentsFlag)
				{
					if (vehicleA.team != vehicleB.team)
					{
						if (vehicleA.player.hasOpponentsFlag)
						{
							resetFlagVehicle(vehicleA.team);
						}
						else
						{
							resetFlagVehicle(vehicleB.team);
						}
					}
					vehicleA.player.hasOpponentsFlag = false;
					vehicleB.player.hasOpponentsFlag = false;
				}
			}
			else if (isBodyAVehicleAndBodyBFlag(bodyAInfo, bodyBInfo))
			{
				Vehicle vehicle = (Vehicle) bodyAInfo.object;
				FloorTileSensor flag = (FloorTileSensor) bodyBInfo.object;
				if (!flag.assigned)
				{
					if (vehicle.player.team != flag.team)
					{
						if (vehicle.player.team == Team.Home)
						{
							vehicleWithAwayFlag = vehicle;
						}
						else
						{
							vehicleWithHomeFlag = vehicle;
						}
						// Let Car Pick it up if someone doesn't have it
						vehicle.player.hasOpponentsFlag = true;
						flag.assigned = true;
					}
					else
					{
						scoreGoal(vehicle, flag);
					}
				}
				else
				{
					scoreGoal(vehicle, flag);
				}
			}
			else if (isBodyAVehicleAndBodyBFlag(bodyBInfo, bodyAInfo))
			{
				FloorTileSensor flag = (FloorTileSensor) bodyAInfo.object;
				Vehicle vehicle = (Vehicle) bodyBInfo.object;
				if (!flag.assigned)
				{
					// TODO: SEE this section and scores
					if (vehicle.player.team != flag.team)
					{
						if (vehicle.player.team == Team.Home)
						{
							vehicleWithAwayFlag = vehicle;
						}
						else
						{
							vehicleWithHomeFlag = vehicle;
						}
						// Let Car Pick it up if someone doesn't have it
						vehicle.player.hasOpponentsFlag = true;
						flag.assigned = true;
					}
					else
					{
						scoreGoal(vehicle, flag);
					}
				}
				else
				{
					scoreGoal(vehicle, flag);
				}
			}
		}
	}
	
	private void scoreGoal(Vehicle vehicle, FloorTileSensor flag)
	{
		if (vehicle.player.hasOpponentsFlag && vehicle.player.team == flag.team)
		{
			resetFlagVehicle(vehicle.player.team);
			// It's a score
			increaseScore(vehicle.player.team);
			vehicle.player.hasOpponentsFlag = false;
			flag.assigned = false;
		}
	}
	
	private void increaseScore(Team team)
	{
		if (team == Team.Home)
		{
			homeTeamScore++;
		}
		else
		{
			awayTeamScore++;
		}
	}
	
	private void resetFlagVehicle(Team team)
	{
		if (team == Team.Home)
		{
			vehicleWithHomeFlag = null;
		}
		else
		{
			vehicleWithAwayFlag = null;
		}
	}
	
	private boolean isBodyAVehicleAndBodyBFlag(CollisionInfo bodyAInfo,
			CollisionInfo bodyBInfo)
	{
		return isTheTwoBodyInfoAVehiclesAndFlag(bodyAInfo.type, bodyBInfo.type);
	}
	
	private boolean isTheTwoBodyInfoAVehiclesAndFlag(
			CollisionObjectType bodyAType, CollisionObjectType bodyBType)
	{
		return bodyAType == CollisionObjectType.Vehicle
				&& bodyBType == CollisionObjectType.FloorSensor;
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