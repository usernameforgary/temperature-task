package org.example;

import com.sun.deploy.util.StringUtils;
import org.example.entity.City;
import org.example.entity.Country;
import org.example.entity.Province;
import org.example.entity.WeatherInfo;
import org.example.exception.DataNotFoundException;
import org.example.exception.FetchDataException;
import org.example.service.IService;
import org.example.service.imp.ServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class Temperatrue {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    //TODO need move those urls to property file
    private static final String provinceUrl = "http://www.weather.com.cn/data/city3jdata/china.html";
    private static final String cityUrlPrefix = "http://www.weather.com.cn/data/city3jdata/provshi/";
    private static final String countryUrlPrefix = "http://www.weather.com.cn/data/city3jdata/station/";
    private static final String weatherUrlPrefix = "http://www.weather.com.cn/data/sk/";
    private static final String urlSuffix = ".html";

    public Optional<String> getTemperature(String province, String city, String country) throws Exception {
        if(province == null || city == null || country == null) {
            throw new IllegalArgumentException("please provide args properly.");
        }
        if(province.trim().length() == 0 || city.trim().length() == 0 || country.trim().length() == 0 ) {
            throw new IllegalArgumentException("please provide args properly.");
        }
        IService service = new ServiceImp();

        // fetch province, and find province with province name
        List<Province> provinceList = service.getCommon(provinceUrl, Province.class);
        Province currentProvince = null;
        if(provinceList != null) {
            currentProvince = provinceList.stream().filter(p -> province.equals(p.getName())).findAny().orElse(null);
        } else {
            throw new FetchDataException("cannot fetch province list.");
        }
        if(currentProvince == null) {
            throw new DataNotFoundException("province not found with name: " + province);
        }

        // fetch city, and find province with province name
        List<City> cityList = service.getCommon(cityUrlPrefix + currentProvince.getCode() + urlSuffix, City.class);
        City currentCity = null;
        if(cityList != null) {
            currentCity = cityList.stream().filter(c -> city.equals(c.getName())).findAny().orElse(null);
        } else {
            throw new FetchDataException("cannot fetch city list of province: " + province);
        }
        if(currentCity == null) {
            throw new DataNotFoundException("city not found city with name: " + city);
        }

        // fetch country, and find country with country name
        List<Country> countryList = service.getCommon(countryUrlPrefix + currentProvince.getCode() + currentCity.getCode() + urlSuffix, Country.class);
        Country currentCountry = null;
        if(countryList != null) {
            currentCountry = countryList.stream().filter(cty -> country.equals(cty.getName())).findAny().orElse(null);
        } else {
            throw new FetchDataException("cannot fetch country list of city: " + city);
        }
        if(currentCountry == null) {
            throw new DataNotFoundException("country not found with name: " + country);
        }

        // get weather information of country
        String weatherInfoUrl = weatherUrlPrefix + currentProvince.getCode() + currentCity.getCode() + currentCountry.getCode()+ urlSuffix;
        WeatherInfo weatherInfo = service.getWeatherInfoByCountryUrl(weatherInfoUrl);
        if(weatherInfo == null) {
            throw new DataNotFoundException("cannot get weather information if country: " + country);
        }

        Optional<String> optionalInteger = Optional.ofNullable(weatherInfo.getTemp());

        return optionalInteger;
    }
}
