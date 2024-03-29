package com.ladinc.core.controllers.listeners.desktop;

import com.ladinc.core.controllers.listeners.GenericControllerListener;
import com.ladinc.core.controllers.mapping.Xbox360WindowsMapper;


public class XboxListener  extends GenericControllerListener{
	
	public XboxListener()
	{
		this.AccelerateButton = Xbox360WindowsMapper.A_BUTTON;
		this.ReverseButton = Xbox360WindowsMapper.X_BUTTON;
		this.HandbreakButton = Xbox360WindowsMapper.R_BUMPER;
		
		this.StartButton = Xbox360WindowsMapper.START_BUTTON;
		this.ConfirmButton = Xbox360WindowsMapper.A_BUTTON;
		this.BackButton = Xbox360WindowsMapper.B_BUTTON;
		
		this.extraButton1 = Xbox360WindowsMapper.Y_BUTTON;
		this.extraButton2 = Xbox360WindowsMapper.B_BUTTON;
		
		this.LeftAxisX = Xbox360WindowsMapper.LEFT_ANALOG_X;
		this.LeftAxisY = Xbox360WindowsMapper.LEFT_ANALOG_Y;
		
		//Fix this!
		this.RightAxisX = Xbox360WindowsMapper.RIGHT_ANALOG_X;
		this.RightAxisY= Xbox360WindowsMapper.RIGHT_ANALOG_Y;
		
		this.StartButtonSecondry = 222;
	}

}
