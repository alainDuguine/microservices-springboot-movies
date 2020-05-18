package org.alain.moviecatalogservice.resources;

import org.alain.moviecatalogservice.models.CatalogItem;
import org.alain.moviecatalogservice.models.Movie;
import org.alain.moviecatalogservice.models.UserRatings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private final RestTemplate restTemplate;
    private final WebClient.Builder webClientbuilder;

    private DiscoveryClient discoveryClient;

    public MovieCatalogResource(RestTemplate restTemplate, @Qualifier("webClientBuilder") WebClient.Builder webClientbuilder) {
        this.restTemplate = restTemplate;
        this.webClientbuilder = webClientbuilder;
    }

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        UserRatings ratings = restTemplate.getForObject(
                "http://ratings-data-service/ratingsdata/users/" + userId,
                UserRatings.class);

        return ratings.getUserRating().stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
            return new CatalogItem(movie.getName(), "Test",rating.getRating());
        }).collect(Collectors.toList());
    }
}
