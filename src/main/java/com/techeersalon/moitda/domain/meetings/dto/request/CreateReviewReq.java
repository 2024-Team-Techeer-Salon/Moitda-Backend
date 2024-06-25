package com.techeersalon.moitda.domain.meetings.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateReviewReq {

    @NotNull(message = "meeting_id cannot be blank")
    private Long meetingId;

    @NotNull(message = "reviews cannot be blank")
    private List<Review> reviews;

    @Getter
    public static class Review {
        @NotNull(message = "userId cannot be blank")
        private Long userId;

        @NotNull(message = "rating cannot be blank")
        private int rating;
    }
}

