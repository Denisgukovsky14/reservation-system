package com.designhub.Services;

import com.designhub.Tables.Reviews.ReviewDto;
import com.designhub.Tables.Reviews.ReviewEntity;
import com.designhub.Tables.Reviews.ReviewRepository;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public ReviewDto createReview(Long customerId, Long contractorId, String title, String comment, Integer rating, String receiverType, Long dealId) {
        ReviewEntity entity = new ReviewEntity();
        entity.setCustomerId(customerId);
        entity.setContractorId(contractorId);
        entity.setTitle(title);
        entity.setComment(comment);
        entity.setRating(rating);
        entity.setReceiverType(receiverType);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setDealId(dealId);

        ReviewEntity saved = reviewRepository.save(entity);
        return new ReviewDto(saved);
    }

    public Double getAverageRatingByContractorId(Long contractorId) {
        return reviewRepository.getAverageRatingByContractorId(contractorId);
    }

    public Double getAverageRatingByCustomerId(Long customerId) {
        return reviewRepository.getAverageRatingByCustomerId(customerId);
    }

    public List<ReviewDto> getReviewsWrittenByUser(Long userId) {
        return reviewRepository.findReviewsWrittenByUser(userId)
                .stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getReviewsByContractorId(Long contractorId) {
        return reviewRepository.findByContractorIdAndReceiverTypeOrderByCreatedAtDesc(contractorId, "contractor")
                .stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getReviewsByCustomerId(Long customerId) {
        return reviewRepository.findByCustomerIdAndReceiverTypeOrderByCreatedAtDesc(customerId, "customer")
                .stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    public boolean hasReview(Long customerId, Long contractorId) {
        return reviewRepository.existsByCustomerIdAndContractorId(customerId, contractorId);
    }
}