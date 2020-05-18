package org.alain.moviecatalogservice.models;

import java.util.List;

public class UserRatings {

    private String userId;
    private List<Rating> userRating;

    public UserRatings() {
    }

    public UserRatings(List<Rating> userRating) {
        this.userRating = userRating;
    }

    public List<Rating> getUserRating() {
        return userRating;
    }

    public void setUserRating(List<Rating> userRating) {
        this.userRating = userRating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
