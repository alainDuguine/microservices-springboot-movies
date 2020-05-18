package org.alain.moviecatalogservice.resources;

import org.alain.moviecatalogservice.models.CatalogItem;
import org.alain.moviecatalogservice.models.UserRatings;
import org.alain.moviecatalogservice.services.MovieInfo;
import org.alain.moviecatalogservice.services.UserRatingInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private final UserRatingInfo userRatingInfo;
    private final MovieInfo movieInfo;

    public MovieCatalogResource(UserRatingInfo userRatingInfo, MovieInfo movieInfo) {
        this.userRatingInfo = userRatingInfo;
        this.movieInfo = movieInfo;
    }

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
        UserRatings ratings = userRatingInfo.getUserRating(userId);
        return ratings.getUserRating().stream()
                .map(rating -> movieInfo.getCatalogItem(rating))
                .collect(Collectors.toList());
    }

}
