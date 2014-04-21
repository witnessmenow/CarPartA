package com.ladinc.core.ux;

import java.util.ArrayList;

public class MenuOptions 
{
	public String title;
	public ArrayList<String> descriptionText;
	public Options option;
	
	
	public MenuOptions (String t, String d, Options o)
	{
		descriptionText = new ArrayList<String>();
		descriptionText.add(d);
		
		title = t;
		
		option = o;
	}
	
	public MenuOptions (String t, String d1, String d2, Options o)
	{
		descriptionText = new ArrayList<String>();
		descriptionText.add(d1);
		descriptionText.add(d2);
		
		title = t;
		
		option = o;
	}
	
	public static enum Options {
		restart, newGame, quit
	}; 
}
