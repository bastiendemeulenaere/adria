package be.howest.ti.adria.logic.listeners;

import be.howest.ti.adria.logic.domain.adria.World;

public interface WeatherListener {
    void onWeatherChange(World world);
}
