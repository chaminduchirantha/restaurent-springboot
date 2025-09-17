package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.CustomerDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.FeedBackDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Feedback;
import lk.ijse.gdse.restaurentspringbootbackend.repo.CustomerRepo;
import lk.ijse.gdse.restaurentspringbootbackend.repo.FeedBackRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.FeedBackService;
import lk.ijse.gdse.restaurentspringbootbackend.service.SentimentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedBackServiceImpl implements FeedBackService {

    private final CustomerRepo customerRepo;
    private final FeedBackRepo feedBackRepo;
    private final ModelMapper modelMapper;
    private final SentimentService sentimentService;


    @Override
    public FeedBackDto submitFeedback(FeedBackDto feedbackDto, String username) {

        Customer customer = customerRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Feedback feedback = new Feedback();
        feedback.setFullname(feedbackDto.getFullname());
        feedback.setEmail(feedbackDto.getEmail());
        feedback.setServices(feedbackDto.getServices());
        feedback.setMessage(feedbackDto.getMessage());
        feedback.setCustomer(customer);

        String sentiment = sentimentService.analyzeSentiment(feedbackDto.getMessage());
        feedback.setSentiment(sentiment);

        feedback.setRatings(String.valueOf(mapSentimentToStar(sentiment)));

        feedBackRepo.save(feedback);

        return feedbackDto;
    }

    private int mapSentimentToStar(String sentiment) {
        switch (sentiment.toUpperCase()) {
            case "VERY POSITIVE": return 5;
            case "POSITIVE": return 4;
            case "NEUTRAL": return 3;
            case "NEGATIVE": return 2;
            case "VERY NEGATIVE": return 1;
            default: return 3; // Neutral default
        }
    }




    @Override
    public List<FeedBackDto> getAllFeedbacks() {
        List<Feedback> feedbacks = feedBackRepo.findAll();

        List<FeedBackDto>feedBackDtos = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            feedBackDtos.add(modelMapper.map(feedback, FeedBackDto.class));

        }
        return feedBackDtos;
    }

    @Override
    public List<FeedBackDto> getFeedbackByPage(int page, int size) {
        int offset = page * size;
        List<Feedback> feedbacks = feedBackRepo.findFeedbackPaginated(size, offset);
        return modelMapper.map(feedbacks, new TypeToken<List<FeedBackDto>>() {}.getType());    }

    @Override
    public int getTotalPages(int size) {
        int feedbackCount = feedBackRepo.getTotalFeedbackCount();
        return (int) Math.ceil((double) feedbackCount / size);
    }

    @Override
    public void deleteFeedback(Long id) {
        Feedback feedback = feedBackRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));
        feedBackRepo.delete(feedback);
    }
}
