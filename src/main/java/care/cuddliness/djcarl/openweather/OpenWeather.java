package care.cuddliness.djcarl.openweather;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class OpenWeather {

    private static final double LAT = 51.2383; // Dessel
    private static final double LON = 5.1142;

    private final HttpClient client = HttpClient.newHttpClient();

    /**
     * Today's live weather
     */
    public String getTodayWeather()  {

        String url =
                "https://api.open-meteo.com/v1/forecast"
                        + "?latitude=51.2383"
                        + "&longitude=5.1142"
                        + "&current=temperature_2m,relative_humidity_2m,cloud_cover,precipitation,weather_code"
                        + "&hourly=precipitation_probability"
                        + "&timezone=Europe%2FBerlin";

        try {
            return fetch(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * June 2026 weather forecast
     * (limited by API future availability)
     */
    public String getJuneData() throws Exception {

        String url =
                "https://api.open-meteo.com/v1/forecast"
                        + "?latitude=51.2383"
                        + "&longitude=5.1142"
                        + "&current=temperature_2m,relative_humidity_2m,cloud_cover,precipitation,weather_code"
                        + "&hourly=precipitation_probability"
                        + "&timezone=Europe%2FBerlin";

        return fetch(url);

    }

    /**
     * Combined response:
     * today's weather + june data
     */
    public String getCombinedWeatherData() throws Exception {

        String today = getTodayWeather();
        String june = getJuneData();

        return "{"
                + "\"today\":" + today + ","
                + "\"juneForecast\":" + june
                + "}";
    }

    private String fetch(String url) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "DJCarlWeatherBot/1.0")
                .timeout(java.time.Duration.ofSeconds(10))
                .GET()
                .build();

        int retries = 3;

        for (int i = 0; i < retries; i++) {

            try {

                HttpResponse<String> response =
                        client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    return response.body();
                }

                // Retry on temporary server errors
                if (response.statusCode() >= 500) {

                    System.out.println(
                            "Temporary API failure (" +
                                    response.statusCode() +
                                    "), retrying..."
                    );

                    Thread.sleep(1000);
                    continue;
                }

                throw new RuntimeException(
                        "API error (" +
                                response.statusCode() +
                                "): " +
                                response.body()
                );

            } catch (Exception e) {

                if (i == retries - 1) {
                    throw e;
                }

                Thread.sleep(1000);
            }
        }

        throw new RuntimeException("Failed after retries");
    }

    public String getWeatherIconUrl(int code) {

        return switch (code) {

            case 0 -> "https://openweathermap.org/img/wn/01d.png"; // clear sky
            case 1, 2 -> "https://openweathermap.org/img/wn/02d.png"; // partly cloudy
            case 3 -> "https://openweathermap.org/img/wn/04d.png"; // overcast

            case 45, 48 -> "https://openweathermap.org/img/wn/50d.png"; // fog

            case 51, 53, 55 -> "https://openweathermap.org/img/wn/09d.png"; // drizzle
            case 61, 63, 65 -> "https://openweathermap.org/img/wn/10d.png"; // rain

            case 71, 73, 75 -> "https://openweathermap.org/img/wn/13d.png"; // snow

            case 80, 81, 82 -> "https://openweathermap.org/img/wn/09d.png"; // showers

            case 95, 96, 99 -> "https://openweathermap.org/img/wn/11d.png"; // thunderstorm

            default -> "https://openweathermap.org/img/wn/03d.png";
        };
    }

}
