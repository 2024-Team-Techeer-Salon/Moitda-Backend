package com.techeersalon.moitda.domain.meetings.controller;

import com.techeersalon.moitda.domain.chat.entity.ChatRoom;
import com.techeersalon.moitda.domain.chat.service.ChatRoomService;
import com.techeersalon.moitda.domain.meetings.dto.request.ApprovalParticipantReq;
import com.techeersalon.moitda.domain.meetings.dto.request.ChangeMeetingInfoReq;
import com.techeersalon.moitda.domain.meetings.dto.request.CreateMeetingReq;
import com.techeersalon.moitda.domain.meetings.dto.response.CreateMeetingRes;
import com.techeersalon.moitda.domain.meetings.dto.response.CreateParticipantRes;
import com.techeersalon.moitda.domain.meetings.dto.response.GetLatestMeetingListRes;
import com.techeersalon.moitda.domain.meetings.dto.response.GetMeetingDetailRes;
import com.techeersalon.moitda.domain.meetings.service.MeetingService;
import com.techeersalon.moitda.global.common.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> meetingCreated(
            @Validated @RequestPart CreateMeetingReq createMeetingReq,
            @RequestPart(name = "meeting_images", required = false) @Valid List<MultipartFile> meetingImages
    ) throws IOException {
        CreateMeetingRes response = meetingService.createMeeting(createMeetingReq, meetingImages);
        ChatRoom chatRoom = chatRoomService.createChatRoom(response.getMeetingId());
        log.info("# create room, roomId = {}", chatRoom.getId());
        return ResponseEntity.ok(SuccessResponse.of(MEETING_CREATE_SUCCESS, response));
    }

    @Operation(summary = "findMeeting", description = "모임 상세 조회")
    @GetMapping("/{meetingId}")
    public ResponseEntity<SuccessResponse> meetingDetail(@PathVariable Long meetingId) {
        Boolean isOwner= meetingService.determineMeetingOwner(meetingId);
        GetMeetingDetailRes response = meetingService.findMeetingById(meetingId);
        response.setIsOwner(isOwner);
        return ResponseEntity.ok(SuccessResponse.of(MEETING_GET_SUCCESS, response));
    }

    @Operation(summary = "latestMeetingsList", description = "최신 모임 리스트 조회")
    @GetMapping("/search/latest")
    public ResponseEntity<SuccessResponse> latestMeetingsList(@RequestParam(value="page", defaultValue="0")int page,
                                                            @RequestParam(value="size", defaultValue="10")int pageSize){
        List<GetLatestMeetingListRes> response = meetingService.latestAllMeetings(page, pageSize);

        return ResponseEntity.ok(SuccessResponse.of(MEETING_PAGING_GET_SUCCESS, response));
    }

    @Operation(summary = "cancelMeeting", description = "모임 취소")
    @DeleteMapping("cancel/{meetingId}")
    public ResponseEntity<SuccessResponse> cancelMeeting(@PathVariable Long meetingId) {
        //과연 이게 좋은 코드일까?
        meetingService.deleteMeeting(meetingId);
        return ResponseEntity.ok(SuccessResponse.of(MEETING_DELETE_SUCCESS));
        //return ResponseEntity.status(HttpStatus.NO_CONTENT).body(SuccessResponse.of(MEETING_DELETE_SUCCESS));
    }

    @Operation(summary = "endMeeting", description = "모임 종료")
    @DeleteMapping("end/{meetingId}")
    public ResponseEntity<SuccessResponse> endMeeting(@PathVariable Long meetingId) {
        meetingService.endMeeting(meetingId);
        meetingService.deleteMeeting(meetingId);
        return ResponseEntity.ok(SuccessResponse.of(MEETING_DELETE_SUCCESS));
    }

    @Operation(summary = "ChangeMeetingInfo", description = "미팅 수정")
    @PutMapping(value = "/{meetingId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> ChangeMeetingInfo( @PathVariable Long meetingId,
            @Validated @RequestPart ChangeMeetingInfoReq changeMeetingReq,
            @RequestPart(name = "meeting_images", required = false) @Valid List<MultipartFile> meetingImages) throws IOException {
        meetingService.modifyMeeting(meetingId, changeMeetingReq, meetingImages);
        return ResponseEntity.ok(SuccessResponse.of(MEETING_UPDATE_SUCCESS));
    }

    //나중에 MeetingParticipantController로 이동
    @Operation(summary = "addParticipantToMeeting", description = "모임 신청")
    @PostMapping("/participant/{meetingId}")
    public ResponseEntity<SuccessResponse> meetingAddParticipant(@PathVariable("meetingId") Long meetingId) {
        CreateParticipantRes response = meetingService.addParticipantOfMeeting(meetingId);
        return ResponseEntity.ok(SuccessResponse.of(PARTICIPANT_CREATE_SUCCESS, response));
    }

    @Operation(summary = "ApprovalOfMeetingParticipants", description = "신청 승인 거절")
    @PatchMapping("/participant")
    public ResponseEntity<SuccessResponse> ApprovalOfMeetingParticipants(@Validated @RequestBody ApprovalParticipantReq dto) {
        meetingService.approvalParticipant(dto);

        return ResponseEntity.ok(SuccessResponse.of(PARTICIPANT_APPROVAL_OR_REJECTION_SUCCESS));
    }
}
