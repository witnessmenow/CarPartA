package com.ladinc.core.assets;

import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Font 
{
	public static Hashtable<String, BitmapFont> fontTable = new Hashtable<String, BitmapFont>();
	public static final String CONST_50 = "CONST_50";
	public static final String OCRA_80 = "OCRA_80";
	
	public static void load()
	{
		loadFonts();
	}
	
	private static void loadFonts()
	{
		fontTable.put(CONST_50, new BitmapFont(Gdx.files.internal("Fonts/Const-50.fnt"), Gdx.files.internal("Fonts/Const-50.png"), false));
		fontTable.put(OCRA_80, new BitmapFont(Gdx.files.internal("Fonts/OCR-A-Extended-80.fnt"), Gdx.files.internal("Fonts/OCR-A-Extended-80.png"), false));
	}

}
