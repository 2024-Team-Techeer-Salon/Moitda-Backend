package com.techeersalon.moitda.domain.meetings.controller;

import com.techeersalon.moitda.domain.meetings.dto.request.CreateMeetingRequest;
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
    public ResponseEntity<String> meetingCreated(@Validated @RequestBody CreateMeetingRequest dto){
        MeetingService.addMeeting(dto);
        return ResponseEntity.created("모임 생성 완료");
    }

    @Operation(summary = "findMeeting", description = "모임 상세 조회")
    @GetMapping("/{meetingId}")
    public ResponseEntity<GetMeetingDetailResponse> meetingDetail(@PathVariable Long meetingId){
        GetMeetingDetailResponse response = meetingService.findMeetingById(meetingId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "findMeetingList", description = "모임 리스트 조회")
    @GetMapping
    public ResponseEntity<GetMeetingsListResponse> getMeeingList(GetMeetingsListRequest dto, PageRequest pageRequest) {
        Pageable pageable = pageRequest.of();
        List<GetProjectResponse> result = meetingService.findProjectList(dto, pageable);
        if (result.isEmpty()) throw new EmptyResultException(ErrorCode.PROJECT_DELETED_OR_NOT_EXIST);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.PROJECT_PAGING_GET_SUCCESS, result));
    }

    @PostMapping("/{meetingId}/{participantId}")
    public ResponseEntity<String> meetingAddParticipant(@Validated @RequestBody CreateMeetingParticipantRequest dto){
        MeetingService.addParticipantOfMeeting(dto,meetingId, participantId);
        return ResponseEntity.ok("모임 신청 완료");
    }
}