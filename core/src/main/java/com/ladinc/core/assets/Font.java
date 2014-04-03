package com.ladinc.core.assets;

import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Font 
{
	public static HashMap<String, BitmapFont> fontTable = new HashMap<String, BitmapFont>();
	public static final String CONST_50 = "CONST_50";
	public static final String OCRA_80 = "OCRA_80";
	
	public static final String STENCIL_75_BOLD = "STENCIL_75_BOLD";
	public static final String STENCIL_50_BOLD = "STENCIL_50_BOLD";
	public static final String STENCIL_50 = "STENCIL_50";
	
	public static final String PLAYBILL_75_BOLD = "PLAYBILL_75_BOLD";
	public static final String PLAYBILL_50_BOLD = "PLAYBILL_50_BOLD";
	public static final String PLAYBILL_50 = "PLAYBILL_50";
	
	
	public static void load()
	{
		loadFonts();
	}
	
	private static void loadFonts()
	{
		fontTable.put(CONST_50, new BitmapFont(Gdx.files.internal("Fonts/Const-50.fnt"), Gdx.files.internal("Fonts/Const-50.png"), false));
		fontTable.put(OCRA_80, new BitmapFont(Gdx.files.internal("Fonts/OCR-A-Extended-80.fnt"), Gdx.files.internal("Fonts/OCR-A-Extended-80.png"), false));
		
		fontTable.put(STENCIL_75_BOLD, new BitmapFont(Gdx.files.internal("Fonts/Stencil-75-Bold.fnt"), Gdx.files.internal("Fonts/Stencil-75-Bold.png"), false));
		fontTable.put(STENCIL_50_BOLD, new BitmapFont(Gdx.files.internal("Fonts/Stencil-50-Bold.fnt"), Gdx.files.internal("Fonts/Stencil-50-Bold.png"), false));
		fontTable.put(STENCIL_50, new BitmapFont(Gdx.files.internal("Fonts/Stencil-50.fnt"), Gdx.files.internal("Fonts/Stencil-50.png"), false));
		
		fontTable.put(PLAYBILL_75_BOLD, new BitmapFont(Gdx.files.internal("Fonts/Playbill-75-Bold.fnt"), Gdx.files.internal("Fonts/Playbill-75-Bold.png"), false));
		fontTable.put(PLAYBILL_50_BOLD, new BitmapFont(Gdx.files.internal("Fonts/Playbill-50-Bold.fnt"), Gdx.files.internal("Fonts/Playbill-50-Bold.png"), false));
		fontTable.put(PLAYBILL_50, new BitmapFont(Gdx.files.internal("Fonts/Playbill-50.fnt"), Gdx.files.internal("Fonts/Playbill-50.png"), false));
	}

}
