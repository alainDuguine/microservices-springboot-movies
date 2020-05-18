package org.alain.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.alain.moviecatalogservice.models.Rating;
import org.alain.moviecatalogservice.models.UserRatings;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class UserRatingInfo {

    private final RestTemplate restTemplate;

    public UserRatingInfo(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "getFallbackUserRating",
        commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
        })
    public UserRatings getUserRating(@PathVariable("userId") String userId) {
        return restTemplate.getForObject(
                "http://ratings-data-service/ratingsdata/users/" + userId,
                UserRatings.class);
    }

    private UserRatings getFallbackUserRating(@PathVariable("userId") String userId) {
        UserRatings ratings = new UserRatings();
        ratings.setUserId(userId);
        ratings.setUserRating(Arrays.asList(
                new Rating("0",0)
        ));
        return ratings;
    }
}
