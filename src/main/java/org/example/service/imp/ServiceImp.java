package org.example.service.imp;

import com.github.rholder.retry.*;
import okhttp3.Response;
import org.example.entity.Common;
import org.example.entity.WeatherInfo;
import org.example.exception.NeedRetryException;
import org.example.service.IService;
import org.example.utils.Converter;
import org.example.utils.HttpUtils;
import org.example.utils.RetryLogListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ServiceImp implements IService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceImp.class);

    //TODO the sleepTime and attemptNumber need put to property file
    @Override
    public <T extends Common> List<T> getCommon(String url, Class<T> clazz) {
        List<T> list = null;
        Retryer<List<T>> retryer = RetryerBuilder.<List<T>>newBuilder()
                .retryIfExceptionOfType(NeedRetryException.class)
                .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(4))
                .withRetryListener(new RetryLogListener())
                .build();
        try {
            list = retryer.call(() -> {
                List<T> listResult = null;
                Response res = HttpUtils.get(url);
                if(res != null)  {
                    try {
                        listResult = Converter.convertCommon(res, clazz);
                    } catch (Exception e) {
                        throw new NeedRetryException("cannot convert ["+ clazz.getName() + "] result, " + e.getMessage());
                    }
                } else {
                    throw new NeedRetryException("can not fetch data");
                }
                return listResult;
            });
        } catch (ExecutionException e) {
            logger.error("Other exception happened, fetch data failed.");
            return null;
        } catch (RetryException e) {
            logger.error("Max retry time reached while trying to get {}, fetch data failed.", clazz.getName());
            return null;
        }

        return list;
    }

    @Override
    public WeatherInfo getWeatherInfoByCountryUrl(String url) {
        WeatherInfo weatherInfo = null;
        Retryer<WeatherInfo> retryer = RetryerBuilder.<WeatherInfo>newBuilder()
                .retryIfExceptionOfType(NeedRetryException.class)
                .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(4))
                .withRetryListener(new RetryLogListener())
                .build();
        try {
            weatherInfo = retryer.call(() -> {
                WeatherInfo weatherInfoRes = null;
                Response res = HttpUtils.get(url);
                if(res != null) {
                    try {
                        weatherInfoRes = Converter.convertWeatherInfo(res);
                    } catch (Exception e) {
                        throw new NeedRetryException("cannot convert weather info result, " + e.getMessage());
                    }
                } else {
                    throw new NeedRetryException("can not fetch data");
                }
                return weatherInfoRes;
            });
        } catch (ExecutionException e) {
            logger.error("Other exception happened, fetch data failed.");
            return null;
        } catch (RetryException e) {
            logger.error("Max retry time reached, fetch data failed.");
            return null;
        }

        return weatherInfo;
    }
}
