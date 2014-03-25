package com.ladinc.html;

import com.ladinc.core.CarPartA;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class CarPartAHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new CarPartA();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(1280, 720);
	}
}
