package care.cuddliness.djcarl.command.commands.weather;

import care.cuddliness.djcarl.command.annotation.BaseCommandComponent;
import care.cuddliness.djcarl.command.data.BaseCommandInterface;
import care.cuddliness.djcarl.openweather.OpenWeather;
import care.cuddliness.djcarl.utils.EmbedColor;
import care.cuddliness.djcarl.utils.EmbedUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

@BaseCommandComponent(name = "weathertoday")
public class WeatherTodayCommand implements BaseCommandInterface {

    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        OpenWeather weather = new OpenWeather();
        System.out.println(weather.getTodayWeather());

        JsonObject root =  new Gson().fromJson(weather.getTodayWeather(), JsonObject.class);
        JsonObject currentWeatherJson = root.getAsJsonObject("current");
        Gson gson = new Gson();
        CurrentWeather currentWeather = gson.fromJson(currentWeatherJson, CurrentWeather.class);

        EmbedUtil util = new EmbedUtil();
        util.setTitle("Weather today in Dessel");
        util.addField(":sun_with_face: Temprature", currentWeather.temperature_2m + "°C", true);
        util.addField(":sweat_drops: Humidity", currentWeather.relative_humidity_2m + "%", false);
        util.addField(":cloud: Cloud coverage", currentWeather.cloud_cover + "%", true);
        util.addField(":cloud_rain: Precipitation", currentWeather.precipitation + "%", true);

        util.setColor(EmbedColor.PRIMARY);
        util.setThumbnail(weather.getWeatherIconUrl(currentWeather.weathercode));
        event.replyEmbeds(util.build()).queue();


    }
}
