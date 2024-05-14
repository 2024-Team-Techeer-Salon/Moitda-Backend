package com.techeersalon.moitda.domain.meetings.controller;

import com.techeersalon.moitda.domain.chat.entity.ChatRoom;
import com.techeersalon.moitda.domain.chat.service.ChatRoomService;
import com.techeersalon.moitda.domain.meetings.dto.request.ChangeMeetingInfoReq;
import com.techeersalon.moitda.domain.meetings.dto.request.CreateMeetingReq;
import com.techeersalon.moitda.domain.meetings.dto.response.CreateMeetingRes;
import com.techeersalon.moitda.domain.meetings.dto.response.GetLatestMeetingListRes;
import com.techeersalon.moitda.domain.meetings.dto.response.GetMeetingDetailRes;
import com.techeersalon.moitda.domain.meetings.service.MeetingService;
import com.techeersalon.moitda.global.common.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.techeersalon.moitda.global.common.SuccessCode.*;

@Slf4j
@Tag(name = "MeetingsController", description = "모임 관련 API")
@RestController
@RequestMapping("api/v1/meetings")
@RequiredArgsConstructor
public class MeetingsController {
    @Autowired
    private final MeetingService meetingService;
    @Autowired
    private final ChatRoomService chatRoomService;

    @Operation(summary = "createMeeting", description = "모임 생성")
    @PostMapping
    public ResponseEntity<SuccessResponse> meetingCreated(@Validated @RequestBody CreateMeetingReq dto) {
        CreateMeetingRes response = meetingService.createMeeting(dto);
        ChatRoom chatRoom = chatRoomService.createChatRoom(response.getMeetingId());
        log.info("# create room, roomId = {}", chatRoom.getId());
        return ResponseEntity.ok(SuccessResponse.of(MEETING_CREATE_SUCCESS, response));
    }

    @Operation(summary = "findMeeting", description = "모임 상세 조회")
    @GetMapping("/{meetingId}")
    public ResponseEntity<SuccessResponse> meetingDetail(@PathVariable Long meetingId) {
        GetMeetingDetailRes response = meetingService.findMeetingById(meetingId);
        return ResponseEntity.ok(SuccessResponse.of(MEETING_GET_SUCCESS, response));
    }

    @Operation(summary = "findMeetingsList", description = "모임 조회")
    @GetMapping("/search/latest")
    public Page<GetLatestMeetingListRes> findMeetingsList(@RequestParam(value="page", defaultValue="0")int page){
        Page<GetLatestMeetingListRes>  response = meetingService.findMeetings(page);
        return response;
    }

    @Operation(summary = "cancelMeeting", description = "모임 취소")
    @DeleteMapping("cancel/{meetingId}")
    public String cancelMeeting(@PathVariable Long meetingId){
        meetingService.deleteMeeting(meetingId);
        return "미팅 취소";
    }

    @Operation(summary = "endMeeting", description = "모임 취소")
    @DeleteMapping("end/{meetingId}")
    public String endMeeting(@PathVariable Long meetingId){
        meetingService.endMeeting(meetingId);
        meetingService.deleteMeeting(meetingId);
        return "미팅 종료";
    }

    @Operation(summary = "ChangeMeetingInfo", description = "미팅 수정")
    @PutMapping("/{meetingId}")
    public String ChangeMeetingInfo(@PathVariable Long meetingId, @Validated @RequestBody ChangeMeetingInfoReq dto){
        meetingService.modifyMeeting(meetingId, dto);
        return "미팅 수정";
    }

    //나중에 MeetingParticipantController로 이동
    @Operation(summary = "addParticipantToMeeting", description = "모임 신청")
    @PostMapping("/participant/{meetingId}")
    public ResponseEntity<String> meetingAddParticipant(@PathVariable("meetingId") Long meetingId) {
        meetingService.addParticipantOfMeeting(meetingId);
        return ResponseEntity.created(URI.create("/meetings/" + meetingId)).body("모임 신청 완료");
    }

    @Operation(summary = "ApprovalOfMeetingParticipants", description = "신청 승인 거절")
    @PatchMapping("/participant/{participantId}/{isApproval}")
    public ResponseEntity<String> ApprovalOfMeetingParticipants(@PathVariable("participantId") Long participantId, @PathVariable("isApproval") Boolean isApproval) {
        meetingService.approvalParticipant(participantId, isApproval);

        if (Boolean.TRUE.equals(isApproval)) {
            /*채팅방에 인원 추가하는 로직*/
            return ResponseEntity.ok("모집 승인 완료");
        } else {
            return ResponseEntity.ok("모집 거절 완료");
        }
    }
}
