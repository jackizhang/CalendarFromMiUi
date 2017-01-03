package utils;

import android.content.Context;
import android.content.res.Resources;

public class ResourceManager {
	private static Resources sResources;
	
	public static void init(Context context) {
		sResources = context.getResources();
	}
	
	public static String[] getStringArray(int id) {
		return sResources.getStringArray(id);
	}
	
	public static Resources getResources() {
		return sResources;
	}
	
}
