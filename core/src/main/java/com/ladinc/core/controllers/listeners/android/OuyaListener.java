package com.ladinc.core.controllers.listeners.android;

import com.badlogic.gdx.controllers.mappings.Ouya;
import com.ladinc.core.controllers.listeners.GenericControllerListener;
import com.ladinc.core.controllers.mapping.Xbox360WindowsMapper;

public class OuyaListener extends GenericControllerListener
{
	
	public OuyaListener()
	{
		this.AccelerateButton = Ouya.BUTTON_O;
		this.ReverseButton = Ouya.BUTTON_U;
		//this.HandbreakButton = Ouya.BUTTON_R1;
		this.HandbreakButton = 103;
		
		//this.StartButton = Ouya.BUTTON_MENU;82
		this.StartButton = 108;
		this.StartButtonSecondry = 82;
		this.ConfirmButton = Ouya.BUTTON_O;
		this.BackButton = Ouya.BUTTON_A;
		
		this.extraButton1 = Ouya.BUTTON_Y;
		this.extraButton2 = Ouya.BUTTON_A;
		
		this.LeftAxisX = Ouya.AXIS_LEFT_X;
		this.LeftAxisY = Ouya.AXIS_LEFT_Y;
		
		//Fix this!
		this.RightAxisX = Ouya.AXIS_RIGHT_X;
		this.RightAxisY= Ouya.AXIS_RIGHT_Y;
	}
	

}
