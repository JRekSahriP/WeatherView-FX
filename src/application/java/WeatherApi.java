package application.java;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherApi {

	public static JSONObject getWeatherInfos(String city) {
		JSONObject cityLocationInfos = getLocationInfos(city);
	    if (cityLocationInfos == null) {
	        System.err.println("Location not available :(");
	        return null;
	    }
	    double latitude  = (double)(cityLocationInfos.get("latitude"));
	    double longitude = (double)(cityLocationInfos.get("longitude"));
	    try {
	    	String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m";
            HttpURLConnection Connection = getApiResponse(url);
			/*200 => connection was a success*/
			if(Connection == null || Connection.getResponseCode() != 200) {
				System.err.println("Could not connect to API :(");
				return null;
			}
			String jsonResponse = getApiResponse(Connection);
			
			JSONParser parser = new JSONParser();
			JSONObject resultJson = (JSONObject) parser.parse(jsonResponse);
			
			JSONObject WeatherInfos = (JSONObject) resultJson.get("current");
			
			return WeatherInfos;
	    }catch(Exception e) {
	    	e.printStackTrace();
			return null;
	    }
	}
	private static JSONObject getLocationInfos(String city) {
		city = city.trim().replaceAll(" ", "+");
		
		String url = "https://geocoding-api.open-meteo.com/v1/search?name=" + city + "&count=1&language=en&format=json";
		
		try {
			HttpURLConnection Connection = getApiResponse(url);
			/*200 => connection was a success*/
			if(Connection == null || Connection.getResponseCode() != 200) {
				System.err.println("Could not connect to API :(");
				return null;
			}
			String jsonResponse = getApiResponse(Connection);
			
			JSONParser parser = new JSONParser();
			JSONObject resultJson = (JSONObject) parser.parse(jsonResponse);
			
			JSONArray LocationInfos = (JSONArray) resultJson.get("results");
			
			if(LocationInfos == null || LocationInfos.isEmpty()) {
				System.err.println("No location data found for " + city.replaceAll("\\+", " ") + ":(");
				return null;
			}
			
			return (JSONObject) LocationInfos.get(0);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String getApiResponse(HttpURLConnection Connection) {
		try {
			StringBuilder resultJson = new StringBuilder();
			Scanner scan = new Scanner(Connection.getInputStream());
			while(scan.hasNext()) {
				resultJson.append(scan.nextLine());
			}
			scan.close();
			return resultJson.toString();
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	private static HttpURLConnection getApiResponse(String urlConnection) {
		try {
			URL url = new URL(urlConnection);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			return connection;
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
