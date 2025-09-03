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
@CrossOrigin(origins = "http://localhost:63342")
public class FeedBackController {
    private final FeedBackService feedBackService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto> submitFeedback(
            @RequestBody FeedBackDto feedbackDto,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDto(401, "Please login first", null));
        }

        String username = authentication.getName();
        FeedBackDto savedFeedback = feedBackService.submitFeedback(feedbackDto, username);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto(201, "Feedback Submitted Successfully", savedFeedback));
    }


    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponseDto> getAllFeedbacks() {
        List<FeedBackDto> feedbackList = feedBackService.getAllFeedbacks();
        return ResponseEntity.ok(
                new ApiResponseDto(200, "OK", feedbackList)
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")  // Only admin can delete
    public ResponseEntity<ApiResponseDto> deleteFeedback(@PathVariable Long id) {
        feedBackService.deleteFeedback(id);
        return ResponseEntity.ok(new ApiResponseDto(200, "Feedback deleted successfully", null));
    }


    @GetMapping("paginated")
    public List<FeedBackDto> getPaginatedJobs(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return feedBackService.getFeedbackByPage(page, size);
    }

    @GetMapping("total-pages")
    public int getTotalPages(@RequestParam(defaultValue = "5") int size) {
        return feedBackService.getTotalPages(size);
    }
}
