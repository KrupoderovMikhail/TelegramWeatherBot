package com.KrupoderovMikhail.github.service;

import com.KrupoderovMikhail.github.model.Model;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    //https://api.openweathermap.org/data/2.5/weather?q=Chaykovskiy,RU&units=metric&appid=376b1c6d893122180eb3a004f4dad539

    //376b1c6d893122180eb3a004f4dad539
    public static String getWeather(String message, Model model) throws IOException {
        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=376b1c6d893122180eb3a004f4dad539");

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }

        JSONObject object = new JSONObject(result); // объект json
        model.setName(object.getString("name")); // достаем имя

        JSONObject main = object.getJSONObject("main"); // создаем новый объект, выбирая данные "main" из object
        model.setTemp(main.getDouble("temp")); // достаем температуру
        model.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather"); // создаем массив json
        /* С помощью цикла помещаем данные из массива в объект obj */
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon(obj.get("icon").toString()); // помещаем иконку в модель
            model.setMain(obj.get("main").toString()); // помещаем описание погоды в модель
        }

        return "City: " + model.getName() + "\n" +
                "Temperature: " + model.getTemp() + "°C" + "\n" +
                "Humidity: " + model.getHumidity() + "%" + "\n" +
                "com.KrupoderovMikhail.github.Main: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/w/" + model.getIcon() + ".png";
    }
}
