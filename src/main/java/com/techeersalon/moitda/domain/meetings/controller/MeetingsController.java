package com.techeersalon.moitda.domain.meetings.controller;

import com.techeersalon.moitda.domain.meetings.dto.response.CreateMeetingResponse;
import com.techeersalon.moitda.domain.meetings.dto.response.GetMeetingDetailResponse;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.service.MeetingService;
import com.techeersalon.moitda.global.common.SuccessCode;
import com.techeersalon.moitda.global.common.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "MeetingsController", description = "모임 관련 API")
@RestController
@RequestMapping("api/v1/meetings")
@RequiredArgsConstructor
public class MeetingsController {
    private final MeetingService meetingService;

    @Operation(summary = "createMeeting", description = "모임 생성")
    @PostMapping
    public ResponseEntity<GetMeetingDetailResponse> meetingDetail(@Validated @RequestBody CreateMeetingResponse dto ,@CurrentUser User loginUser){
        CreateMeetingResponse response = MeetingService.addMeeting(dto, loginUser);
        return ResponseEntity.created(response);
    }

    @Operation(summary = "findMeeting", description = "모임 상세 조회")
    @GetMapping("/{meetingId}")
    public ResponseEntity<GetMeetingDetailResponse> meetingDetail(@PathVariable Long meetingId){
        GetMeetingDetailResponse response = meetingService.findMeetingById(meetingId);
        return ResponseEntity.ok(response);
    }


}