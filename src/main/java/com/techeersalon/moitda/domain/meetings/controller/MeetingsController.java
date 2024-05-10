package com.techeersalon.moitda.domain.meetings.controller;

import com.techeersalon.moitda.domain.meetings.dto.request.CreateMeetingRequest;
import com.techeersalon.moitda.domain.meetings.dto.response.GetMeetingDetailResponse;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.net.URI;
import java.util.List;

@Tag(name = "MeetingsController", description = "모임 관련 API")
@RestController
@RequestMapping("api/v1/meetings")
@RequiredArgsConstructor
public class MeetingsController {
    private final MeetingService meetingService;

    @Operation(summary = "createMeeting", description = "모임 생성")
    @PostMapping
    public ResponseEntity<String> meetingCreated(@Validated @RequestBody CreateMeetingRequest dto){
        Long meetingId = meetingService.addMeeting(dto);
        return ResponseEntity.created(URI.create("/meetings/" + meetingId)).body("모임 생성 완료");
    }

    @Operation(summary = "findMeeting", description = "모임 상세 조회")
    @GetMapping("/{meetingId}")
    public ResponseEntity<GetMeetingDetailResponse> meetingDetail(@PathVariable Long meetingId){
        GetMeetingDetailResponse response = meetingService.findMeetingById(meetingId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "findMeetingsList", description = "모임  조회")
    @GetMapping("/")
    public List<Meeting> findMeetingsList(@PageableDefault(sort="createdAt",direction = Sort.Direction.DESC ,size=5) Pageable pageable){
        List<Meeting> response = meetingService.lastMeetingList(pageable);
        return response;
    }
    //나중에 MeetingParticipantController로 이동
    @Operation(summary = "addParticipantToMeeting", description = "모임 신청")
    @PostMapping("/{meetingId}")
    public ResponseEntity<String> meetingAddParticipant(@PathVariable("meetingId") Long meetingId){
        meetingService.addParticipantOfMeeting(meetingId);
        return ResponseEntity.created(URI.create("/meetings/" + meetingId )).body("모임 신청 완료");
    }
    @Operation(summary = "ApprovalOfMeetingParticipants", description = "신청 승인 거절")
    @PatchMapping("/{userId}/{isApproval}")
    public ResponseEntity<String> ApprovalOfMeetingParticipants(@PathVariable("userId") Long userIdOfparticipant,@PathVariable("isApproval") Boolean isApproval){
        meetingService.approvalParticipant(userIdOfparticipant, isApproval);

        if(isApproval){
            return ResponseEntity.ok("모집 승인 완료");
        }else{
            return ResponseEntity.ok("모집 거절 완료");
        }
    }
}
