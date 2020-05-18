package org.alain.moviecatalogservice.resources;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.alain.moviecatalogservice.models.CatalogItem;
import org.alain.moviecatalogservice.models.Movie;
import org.alain.moviecatalogservice.models.Rating;
import org.alain.moviecatalogservice.models.UserRatings;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private final RestTemplate restTemplate;

    public MovieCatalogResource(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
        UserRatings ratings = getUserRating(userId);
        return ratings.getUserRating().stream()
                .map(rating -> getCatalogItem(rating))
                .collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    private CatalogItem getCatalogItem(Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(), "Test",rating.getRating());
    }

    @HystrixCommand(fallbackMethod = "getFallbackUserRating")
    private UserRatings getUserRating(@PathVariable("userId") String userId) {
        return restTemplate.getForObject(
                "http://ratings-data-service/ratingsdata/users/" + userId,
                UserRatings.class);
    }

    private CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie name not found", "Test", rating.getRating());
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
