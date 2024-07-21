package application.java;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerFX {
	public ControllerFX() {}
	
	@FXML
	private TextField searchText;
	@FXML
	private Label humidityLabel;
	@FXML
	private Label windSpeedLabel;
	@FXML
	private Label temperaturaLabel;
	@FXML
	private Label descriptionLabel;
	@FXML
	private ImageView weatherImage;
	
	
	@FXML
	private void getWeatherInfos() {
		String input = searchText.getText();
		if(input.isEmpty()) {return;}
		
		setWeatherInfos(WeatherApi.getWeatherInfos(input));
	}
	
	private void setWeatherInfos(JSONObject infos) {
        double celsius 		= (double) infos.get("temperature_2m");
        long   humidity 	= (long)   infos.get("relative_humidity_2m");
        long   weatherCode	= (long)   infos.get("weather_code");
        double windSpeed 	= (double) infos.get("wind_speed_10m");
        double fahrenheit = celsiusToFahrenheit(celsius);
        
        String humidityText = humidity+"%";
        String windSpeedText = windSpeed+"Km/h";
        String temperatureText = String.format("%.1f%s%.1f%s", celsius,"°C | ", fahrenheit,"°F");
        String weatherCondition = convertWeatherCode(weatherCode);
        
        weatherImage.setImage(new Image("/application/resources/images/"+weatherCondition+".png"));
        temperaturaLabel.setText(temperatureText);
        descriptionLabel.setText(weatherCondition);
        humidityLabel.setText(humidityText);
        windSpeedLabel.setText(windSpeedText);
	}
	private String convertWeatherCode(long code) {
	/*
		Weather variable documentation:
		https://open-meteo.com/en/docs
	*/
	String weatherCondition="Nothing";
		 if(code == 0L) 			  {weatherCondition = "Clear";}
	else if(code >  0L && code <= 3L) {weatherCondition = "Cloudy";}
	else if(code >= 51L&& code <= 67L||
			code >= 80L&& code <= 99L){weatherCondition= "Rain";}
	else if(code >= 71L&& code <= 77L){weatherCondition= "Snow";}
		 
	return weatherCondition;
	}
	private double celsiusToFahrenheit(double C) {return (C * (9.0 / 5.0)) + 32;}
}
