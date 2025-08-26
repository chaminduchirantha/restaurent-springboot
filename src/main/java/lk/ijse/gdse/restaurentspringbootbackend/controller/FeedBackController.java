package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.dto.ApiResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.FeedBackDto;
import lk.ijse.gdse.restaurentspringbootbackend.service.FeedBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/feedback")
@RequiredArgsConstructor
public class FeedBackController {
    private final FeedBackService feedBackService;

    @PostMapping("/feedback")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto> submitFeedback(@RequestBody FeedBackDto feedbackDto, Authentication authentication) {

        String username = authentication.getName();  // Logged-in user
        FeedBackDto savedFeedback = feedBackService.submitFeedback(feedbackDto, username);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto(201, "Feedback Submitted Successfully", savedFeedback));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> getAllFeedbacks() {
        List<FeedBackDto> feedbackList = feedBackService.getAllFeedbacks();
        return ResponseEntity.ok(
                new ApiResponseDto(200, "OK", feedbackList)
        );
    }
}
