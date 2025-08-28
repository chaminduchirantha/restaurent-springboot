package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.FeedBackDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FeedBackService {
    FeedBackDto submitFeedback(FeedBackDto feedbackDto, String username);
    List<FeedBackDto> getAllFeedbacks();
    List<FeedBackDto> getFeedbackByPage(int page, int size);
    int getTotalPages(int size);
}
