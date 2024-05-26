package com.techeersalon.moitda.domain.meetings.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetSearchPageRes {
    private List<GetLatestMeetingListRes> meetingList;
    private int totalPage;
    private int currentPage;
    private int totalElements;
    private int elementsPerPage;

    public static GetSearchPageRes from(List<GetLatestMeetingListRes> meetingList, int totalPage, int currentPage, int totalElements, int elementsPerPage) {
        return GetSearchPageRes.builder()
                .meetingList(meetingList)
                .totalPage(totalPage)
                .currentPage(currentPage)
                .totalElements(totalElements)
                .elementsPerPage(elementsPerPage)
                .build();
    }
}
