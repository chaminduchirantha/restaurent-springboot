package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.CustomerDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.FeedBackDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Feedback;
import lk.ijse.gdse.restaurentspringbootbackend.repo.CustomerRepo;
import lk.ijse.gdse.restaurentspringbootbackend.repo.FeedBackRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.FeedBackService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedBackServiceImpl implements FeedBackService {

    private final CustomerRepo customerRepo;
    private final FeedBackRepo feedBackRepo;
    private final ModelMapper modelMapper;

    @Override
    public FeedBackDto submitFeedback(FeedBackDto feedbackDto, String username) {

        Customer customer = customerRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Feedback feedback = new Feedback();
        feedback.setFullname(feedbackDto.getFullname());
        feedback.setEmail(feedbackDto.getEmail());
        feedback.setServices(feedbackDto.getServices());
        feedback.setRatings(feedbackDto.getRatings());
        feedback.setMessage(feedbackDto.getMessage());
        feedback.setCustomer(customer);

        feedBackRepo.save(feedback);

        return feedbackDto;
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
}
