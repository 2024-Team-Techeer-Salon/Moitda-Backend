package com.techeersalon.moitda.domain.meetings.controller;

import com.techeersalon.moitda.domain.chat.entity.ChatRoom;
import com.techeersalon.moitda.domain.chat.service.ChatRoomService;
import com.techeersalon.moitda.domain.meetings.dto.mapper.PointMapper;
import com.techeersalon.moitda.domain.meetings.dto.request.ApprovalParticipantReq;
import com.techeersalon.moitda.domain.meetings.dto.request.ChangeMeetingInfoReq;
import com.techeersalon.moitda.domain.meetings.dto.request.CreateMeetingReq;
import com.techeersalon.moitda.domain.meetings.dto.request.CreateReviewReq;
import com.techeersalon.moitda.domain.meetings.dto.response.*;
import com.techeersalon.moitda.domain.meetings.service.MeetingService;
import com.techeersalon.moitda.global.common.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    private final MeetingService meetingService;
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
        Boolean isOwner = meetingService.determineMeetingOwner(meetingId);
        GetMeetingDetailRes response = meetingService.findMeetingById(meetingId);
        response.setIsOwner(isOwner);
        return ResponseEntity.ok(SuccessResponse.of(MEETING_GET_SUCCESS, response));
    }

//    @Operation(summary = "latestMeetingsPage", description = "최신 모임 리스트 조회")
//    @GetMapping("/search/latest")
//
//    public ResponseEntity<SuccessResponse> latestMeetingsList(@RequestParam(value = "page", defaultValue = "0") int page,
//                                                              @RequestParam(value = "size", defaultValue = "10") int pageSize) {
//
//        List<GetLatestMeetingListRes> response = meetingService.latestAllMeetings(page, pageSize);
//
//        return ResponseEntity.ok(SuccessResponse.of(MEETING_PAGING_GET_SUCCESS, response));
//    }

//    @Operation(summary = "latestCategoryMeetingsPage", description = "카테고리별 최신 모임 리스트 조회")
//    @GetMapping("/search/latest/{categoryId}")
//    public ResponseEntity<SuccessResponse> latestCategoryMeetingsPage( @PathVariable Long categoryId,
//                                                                       @RequestParam(value="page", defaultValue="0")int page,
//                                                                       @RequestParam(value="size", defaultValue="10")int pageSize){
//        List<GetLatestMeetingListRes> response = meetingService.latestCategoryMeetings(categoryId,page, pageSize);
//
//        return ResponseEntity.ok(SuccessResponse.of(MEETING_PAGING_GET_SUCCESS, response));
//    }

    @Operation(summary = "nearMeetingsPage", description = "가까운 모임 리스트 조회")
    @GetMapping("/search/")
    public ResponseEntity<SuccessResponse> getNearMeetings(
            @RequestParam double latitude,
            @RequestParam double longitude,
            Pageable pageable){
        PointMapper pointMapper = PointMapper.from(latitude, longitude);
        //Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("createAt")));
        GetSearchPageRes response= meetingService.getMeetingsNearLocation(pointMapper, pageable);
        return ResponseEntity.ok(SuccessResponse.of(MEETING_PAGING_GET_SUCCESS, response));
    }
    //@PageableDefault(sort = "createAt", direction = Sort.Direction.ASC,page = 0, size = 10)Pageable pageable)

    @Operation(summary = "categoryNearMeetingsPage", description = "카테고리 모임 리스트 조회")
    @GetMapping("/search/category/{categoryId}")
    public ResponseEntity<SuccessResponse> getCategoryMeetings(
            @PathVariable Long categoryId,
            @RequestParam double latitude,
            @RequestParam double longitude,
            Pageable pageable){
        PointMapper pointMapper = PointMapper.from(latitude, longitude);
        //Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("createAt")));
        GetSearchPageRes response= meetingService.getMeetingsCategory(pointMapper, categoryId,pageable);
        return ResponseEntity.ok(SuccessResponse.of(MEETING_PAGING_GET_SUCCESS, response));
    }

    @Operation(summary = "searchMeetingsByKeyword", description = "키워드로 모임 검색")
    @GetMapping("/search/{keyword}")
    public ResponseEntity<SuccessResponse> searchMeetingsByKeyword(
            @PathVariable String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam double latitude,
            @RequestParam double longitude,
            Pageable pageable) {
        PointMapper pointMapper = PointMapper.from(latitude, longitude);
        GetSearchPageRes response = meetingService.searchMeetingsByKeyword(keyword, pointMapper, pageable);
        return ResponseEntity.ok(SuccessResponse.of(MEETING_SEARCH_SUCCESS, response));
    }

    @Operation(summary = "deleteMeeting", description = "모임 삭제")
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<SuccessResponse> deleteMeeting(@PathVariable Long meetingId) {
        meetingService.deleteMeeting(meetingId);
        return ResponseEntity.ok(SuccessResponse.of(MEETING_DELETE_SUCCESS));
        //return ResponseEntity.status(HttpStatus.NO_CONTENT).body(SuccessResponse.of(MEETING_DELETE_SUCCESS));
    }

    @Operation(summary = "endMeeting", description = "모임 종료")
    @PatchMapping("end/{meetingId}")
    public ResponseEntity<SuccessResponse> endMeeting(@PathVariable Long meetingId) {
        meetingService.endMeeting(meetingId);
        return ResponseEntity.ok(SuccessResponse.of(MEETING_END_SUCCESS));
    }

    @Operation(summary = "ChangeMeetingInfo", description = "미팅 수정")
    @PutMapping("/{meetingId}")
    public ResponseEntity<SuccessResponse> ChangeMeetingInfo(
            @PathVariable Long meetingId,
            @Validated @RequestBody ChangeMeetingInfoReq changeMeetingReq
            ) {
        meetingService.modifyMeeting(meetingId, changeMeetingReq);
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

    @Operation(summary = "reviewParticipants", description = "후기 작성")
    @PostMapping("reviews")
    public ResponseEntity<SuccessResponse> createReview(@RequestBody @Valid CreateReviewReq createReviewReq) {

        meetingService.createReview(createReviewReq);
        return ResponseEntity.ok(SuccessResponse.of(REVIEW_CREATE_SUCCESS));
    }

//    @Operation(summary = "distanceMeetingsPage", description = "범위 모임 리스트 조회")
//    @GetMapping("/search/distance")
//    public ResponseEntity<SuccessResponse> distanceMeetingsPage(@RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude,
//                                                                @RequestParam(value="page", defaultValue="0")int page,
//                                                                @RequestParam(value="size", defaultValue="10")int pageSize){
//        PointMapper pointMapper = PointMapper.from(latitude, longitude);
//        List<GetLatestMeetingListRes> response = meetingService.distanceMeetings(pointMapper,page, pageSize);
//
//        return ResponseEntity.ok(SuccessResponse.of(MEETING_PAGING_GET_SUCCESS, response));
//    }

    @Operation(summary = "getMeetingParticipants", description = "모임 신청자 리스트 조회")
    @GetMapping("/{meetingId}/participants")
    public ResponseEntity<SuccessResponse> getMeetingParticipants(@PathVariable Long meetingId) {
        List<GetParticipantListRes> response = meetingService.getParticipantsOfMeeting(meetingId);
        return ResponseEntity.ok(SuccessResponse.of(PARTICIPANT_LIST_GET_SUCCESS, response));
    }


}
