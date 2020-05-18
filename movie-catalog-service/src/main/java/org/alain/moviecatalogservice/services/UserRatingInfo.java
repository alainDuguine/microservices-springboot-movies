package org.alain.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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

    @HystrixCommand(fallbackMethod = "getFallbackUserRating")
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
