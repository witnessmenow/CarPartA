package com.ladinc.core.screen.gamemodes.capture;

import com.badlogic.gdx.physics.box2d.Contact;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;
import com.ladinc.core.objects.FloorTileSensor;
import com.ladinc.core.player.PlayerInfo;
import com.ladinc.core.screen.gamemodes.AbstractCollisionHelper;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.vehicles.Vehicle;

public class CaptureTheFlagCollisionHelper extends AbstractCollisionHelper {
	public Vehicle vehicleWithHomeFlag;
	public Vehicle vehicleWithAwayFlag;
	public FloorTileSensor homeFlag;
	public FloorTileSensor awayFlag;
	
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
				resetVehicles((Vehicle) bodyAInfo.object,
						(Vehicle) bodyBInfo.object);
			}
			else if (isVehicleAndFlag(bodyAInfo, bodyBInfo))
			{
				assignVehicleAndFlag((Vehicle) bodyAInfo.object,
						(FloorTileSensor) bodyBInfo.object);
			}
			else if (isVehicleAndFlag(bodyBInfo, bodyAInfo))
			{
				assignVehicleAndFlag((Vehicle) bodyBInfo.object,
						(FloorTileSensor) bodyAInfo.object);
			}
		}
	}
	
	private void assignVehicleAndFlag(Vehicle vehicle, FloorTileSensor flag)
	{
		if (flag.flagPresent)
		{
			// TODO: SEE this section and scores
			if (vehicle.player.team != flag.team)
			{
				if (isHomeTeam(vehicle.player.team))
				{
					vehicleWithAwayFlag = vehicle;
				}
				{
					vehicleWithHomeFlag = vehicle;
				}
				// Let Car Pick it up if someone doesn't have it
				vehicle.player.hasOpponentsFlag = true;
				vehicle.king = true;
				flag.flagPresent = false;
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
	
	private void resetVehicles(Vehicle vehicleA, Vehicle vehicleB)
	{
		// collideCars(bodyAInfo, bodyBInfo);
		resetPlayerInfo(vehicleA.player, vehicleB.player);
		if (vehicleA.player.team != vehicleB.player.team)
		{
			vehicleA.king = false;
			vehicleB.king = false;
		}
	}
	
	private void resetPlayerInfo(PlayerInfo playerInfoA, PlayerInfo playerInfoB)
	{
		if (playerInfoA.hasOpponentsFlag || playerInfoB.hasOpponentsFlag)
		{
			if (playerInfoA.team != playerInfoB.team)
			{
				resetPlayerInfoAfterTwoOpposingPlayerCollide(playerInfoA);
				resetPlayerInfoAfterTwoOpposingPlayerCollide(playerInfoB);
			}
		}
	}
	
	private void resetPlayerInfoAfterTwoOpposingPlayerCollide(
			PlayerInfo playerInfo)
	{
		if (playerInfo.hasOpponentsFlag)
		{
			resetFlagVehicle(playerInfo.team);
			playerInfo.hasOpponentsFlag = false;
		}
	}
	
	private void scoreGoal(Vehicle vehicle, FloorTileSensor flag)
	{
		boolean isHomeFlagVehicleAssigned = (isAwayTeam(flag.team) && vehicleWithHomeFlag == null);
		boolean isAwayFlagVehicleAssigned = (isHomeTeam(flag.team) && vehicleWithAwayFlag == null);
		if (vehicle.player.hasOpponentsFlag && vehicle.player.team == flag.team)
		{
			resetFlagVehicle(vehicle.player.team);
			// It's a score
			increaseScore(vehicle.player.team);
			vehicle.player.hasOpponentsFlag = false;
			vehicle.king = false;
			flag.flagPresent = true;
		}
		else if (isHomeFlagVehicleAssigned || isAwayFlagVehicleAssigned)
		{
			flag.flagPresent = true;
		}
	}
	
	private void increaseScore(Team team)
	{
		if (isHomeTeam(team))
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
		if (isHomeTeam(team))
		{
			vehicleWithAwayFlag = null;
			awayFlag.flagPresent = true;
		}
		else
		{
			vehicleWithHomeFlag = null;
			homeFlag.flagPresent = true;
		}
	}
	
	private boolean isVehicleAndFlag(CollisionInfo bodyAInfo,
			CollisionInfo bodyBInfo)
	{
		return isBodyAVehiclesAndBodyBFlag(bodyAInfo.type, bodyBInfo.type);
	}
	
	private boolean isBodyAVehiclesAndBodyBFlag(CollisionObjectType bodyAType,
			CollisionObjectType bodyBType)
	{
		return bodyAType == CollisionObjectType.Vehicle
				&& bodyBType == CollisionObjectType.FloorSensor;
	}
	
	private boolean isHomeTeam(Team team)
	{
		return team == Team.Home;
	}
	
	private boolean isAwayTeam(Team team)
	{
		return team == Team.Away;
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