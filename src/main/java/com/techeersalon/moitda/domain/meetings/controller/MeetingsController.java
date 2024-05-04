package com.techeersalon.moitda.domain.meetings.controller;

import com.techeersalon.moitda.domain.meetings.dto.response.GetMeetingDetailResponse;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.service.MeetingService;
import com.techeersalon.moitda.global.common.SuccessCode;
import com.techeersalon.moitda.global.common.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MeetingsController", description = "모임 관련 API")
@RestController
@RequestMapping("api/v1/meetings")
@RequiredArgsConstructor
public class MeetingsController {
    private final MeetingService meetingService;

    @Operation(summary = "findMeeting", description = "모임 상세 조회")
    @GetMapping("/{meetingId}")
    public ResponseEntity<GetMeetingDetailResponse> meetingDetail(@PathVariable Long meetingId){
        GetMeetingDetailResponse result = meetingService.findMeetingById(meetingId);
        return ResponseEntity.ok(result);
    }
}