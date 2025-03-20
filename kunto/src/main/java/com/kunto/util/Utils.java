package com.kunto.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

public class Utils{
	
	
	public static ArrayList<LocalDate> getPrevDays()  {
		
		boolean isDayExist=true;
		ArrayList<LocalDate> days=new ArrayList<>();
		LocalDate today=LocalDate.now();
		
		while(today.getDayOfWeek() != DayOfWeek.MONDAY) {
			days.add(today);
			today=today.minusDays(1);
		}
		
		days.add(today);
		return days;
	}
	
	public static JSONObject getJsonObject(HttpServletRequest request) {
        BufferedReader reader;
		try {
			reader = request.getReader();
	        StringBuilder jsonString = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            jsonString.append(line);
	        }
	        
	        return new JSONObject(jsonString.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}