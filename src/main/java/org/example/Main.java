package org.example;


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
import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        logger.info("Please input province, then click enter key.");
        String province = in.nextLine();
        logger.info("Please input city, then click enter key.");
        String city = in.nextLine();
        logger.info("Please input country, then click enter key.");
        String country = in.nextLine();

        Optional<String> optionalInteger = Optional.empty();
        try {
            Temperatrue temperatrue = new Temperatrue();
            optionalInteger = temperatrue.getTemperature(province, city, country);
        } catch (Exception e) {
            logger.error("Cannot get temperature of {}, {}, {}. With exception of: {}", province, city, country, e.getMessage());
        }
        if(optionalInteger.isPresent()) {
            logger.info("The temperature of {}, {}, {} is : {}", province, city, country, optionalInteger.get());
        } else {
            logger.info("Cannot get temperature of {}, {}, {}", province, city, country);
        }
    }
}
