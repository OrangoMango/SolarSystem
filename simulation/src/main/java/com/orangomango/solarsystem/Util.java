package com.orangomango.solarsystem;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;

import org.json.JSONObject;

public class Util{
	public static double TIMESTEP = 3600 * 24;
	public static double G = 6.6743e-11;
	public static int MAX_ORBIT_SIZE = 100;
	public static double SCALE = 200 / 1.50e11;

	public static final int WIDTH = 1000;
	public static final int HEIGHT = 800;

	public static String getDate(long timePassed){
		Date date = new Date(timePassed*1000);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		return formatter.format(date);
	}

	public static void loadConfig(String configName, ArrayList<Planet> planets){
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(Util.class.getResourceAsStream(configName)));
			StringBuilder builder = new StringBuilder();
			reader.lines().forEach(builder::append);
			reader.close();

			JSONObject object = new JSONObject(builder.toString());
			TIMESTEP = object.getDouble("timestep");
			G = object.getDouble("g");
			MAX_ORBIT_SIZE = object.getInt("maxOrbitSize");
			SCALE = object.getDouble("scale");

			planets.clear();
			for (Object o : object.getJSONArray("planets")){
				JSONObject p = (JSONObject)o;
				Planet planet = new Planet(p.getString("name"), Color.color(p.getJSONArray("color").getDouble(0), p.getJSONArray("color").getDouble(1), p.getJSONArray("color").getDouble(2)), p.getDouble("radius"), p.getDouble("mass"), p.getDouble("distance"), p.getDouble("velocity"));
				planets.add(planet);
			}
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}
}