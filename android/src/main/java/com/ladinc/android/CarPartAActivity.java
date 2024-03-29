package com.ladinc.android;

import com.ladinc.core.CarPartA;

import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class CarPartAActivity extends AndroidApplication {

	@Override
	public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
			config.useGL20 = true;
			config.hideStatusBar = true;
			initialize(new CarPartA(), config);
	}
}
