package com.orangomango.solarsystem;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Util{
	public static final double TIMESTEP = 3600 * 1;
	public static final double G = 6.6743e-11;
	public static final int MAX_ORBIT_SIZE = 100;
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 800;
	public static final double SCALE = 200 / 1.50e11;

	public static String getDate(long timePassed){
		Date date = new Date(timePassed*1000);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		return formatter.format(date);
	}
}