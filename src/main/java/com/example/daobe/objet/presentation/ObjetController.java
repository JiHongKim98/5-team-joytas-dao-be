package com.example.daobe.objet.presentation;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.objet.application.ObjetService;
import com.example.daobe.objet.application.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.application.dto.ObjetCreateResponseDto;
import com.example.daobe.objet.application.dto.ObjetDetailResponseDto;
import com.example.daobe.objet.application.dto.ObjetMeResponseDto;
import com.example.daobe.objet.application.dto.ObjetResponseDto;
import com.example.daobe.objet.application.dto.ObjetUpdateRequestDto;
import com.example.daobe.objet.application.dto.ObjetUpdateResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/objets")
@RequiredArgsConstructor
@Slf4j
public class ObjetController {

    private static final String OBJET_CREATED_SUCCESS = "OBJET_CREATED_SUCCESS";
    private static final String OBJET_UPDATED_SUCCESS = "OBJET_UPDATED_SUCCESS";
    private static final String OBJET_DELETED_SUCCESS = "OBJET_DELETED_SUCCESS";

    private final ObjetService objetService;

    @PostMapping
    public ResponseEntity<ApiResponse<ObjetCreateResponseDto>> generateObjet(
            @AuthenticationPrincipal Long userId,
            @RequestBody ObjetCreateRequestDto request
    ) {
        ObjetCreateResponseDto ObjetCreateResponse = objetService.createNewObjet(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(OBJET_CREATED_SUCCESS, ObjetCreateResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ObjetResponseDto>>> getAllObjets(
            @AuthenticationPrincipal Long userId,
            @RequestParam("lounge_id") Long loungeId,
            @RequestParam Boolean sharer
    ) {
        ApiResponse<List<ObjetResponseDto>> response = new ApiResponse<>(
                "OBJET_LIST_LOADED_SUCCESS", objetService.getAllObjetsInLounge(userId, loungeId, sharer)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{objetId}")
    public ResponseEntity<ApiResponse<ObjetDetailResponseDto>> getObjetDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "objetId") Long objetId
    ) {
        ApiResponse<ObjetDetailResponseDto> response = new ApiResponse<>(
                "OBJET_DETAIL_LOADED_SUCCESS",
                objetService.getObjetDetail(userId, objetId)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<ObjetMeResponseDto>>> getMyObjets(
            @AuthenticationPrincipal Long userId
    ) {
        ApiResponse<List<ObjetMeResponseDto>> response = new ApiResponse<>(
                "USER_OBJET_LIST_LOADED_SUCCESS", objetService.getMyObjetList(userId)
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{objetId}")
    public ResponseEntity<ApiResponse<ObjetUpdateResponseDto>> updateObjet(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "objetId") Long objetId,
            @RequestBody ObjetUpdateRequestDto request
    ) {
        ObjetUpdateResponseDto objetUpdateResponse = objetService.updateObjet(request, objetId, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(OBJET_UPDATED_SUCCESS, objetUpdateResponse));
    }

    @DeleteMapping("/{objetId}")
    public ResponseEntity<ApiResponse<Void>> deleteObjet(
            @PathVariable(name = "objetId") Long objetId,
            @AuthenticationPrincipal Long userId
    ) {
        objetService.deleteObjet(objetId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(OBJET_DELETED_SUCCESS, null));
    }
}
