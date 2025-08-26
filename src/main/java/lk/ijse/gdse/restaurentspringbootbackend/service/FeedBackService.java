package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.FeedBackDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FeedBackService {
    FeedBackDto submitFeedback(FeedBackDto feedbackDto, String username);
    List<FeedBackDto> getAllFeedbacks();
}
