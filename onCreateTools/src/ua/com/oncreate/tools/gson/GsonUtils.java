package ua.com.oncreate.tools.gson;

import com.google.gson.Gson;

/**
 * Gson library controller
 * @author onCreate
 */
public class GsonUtils{

	/**
	 * Gson single object
	 */
	private static Gson gsonInstance = new Gson();
	
	/**
	 * Gson getter
	 * @return Gson object
	 */
	public static Gson gson(){
		return gsonInstance;
	}
}
