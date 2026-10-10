package com.designhub.Controllers;

import com.designhub.Services.ReviewService;
import com.designhub.Tables.Reviews.ReviewDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.designhub.Tables.Reviews.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkReviewExists(
            @RequestParam Long customerId,
            @RequestParam Long contractorId) {
        boolean exists = reviewService.hasReview(customerId, contractorId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @GetMapping("/average/{userId}")
    public ResponseEntity<?> getAverageRating(@PathVariable Long userId, @RequestParam String userType) {
        Double avg;
        if ("contractor".equals(userType)) {
            avg = reviewService.getAverageRatingByContractorId(userId);
        } else {
            avg = reviewService.getAverageRatingByCustomerId(userId);
        }
        return ResponseEntity.ok(Map.of("average", avg != null ? avg : 0.0));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReviewsByUser(@PathVariable Long userId, @RequestParam String userType) {
        List<ReviewDto> reviews;
        if ("contractor".equals(userType)) {
            reviews = reviewService.getReviewsByContractorId(userId);
        } else {
            reviews = reviewService.getReviewsByCustomerId(userId);
        }
        return ResponseEntity.ok(reviews);
    }


    @GetMapping("/written/{userId}")
    public ResponseEntity<?> getReviewsWrittenByUser(@PathVariable Long userId) {
        List<ReviewDto> reviews = reviewService.getReviewsWrittenByUser(userId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Map<String, Object> body) {
        try {
            Long customerId = Long.valueOf(body.get("customerId").toString());
            Long contractorId = Long.valueOf(body.get("contractorId").toString());
            String title = (String) body.get("title");
            String comment = (String) body.get("comment");
            Integer rating = Integer.valueOf(body.get("rating").toString());
            String receiverType = (String) body.get("receiverType");
            Long dealId = Long.valueOf(body.get("dealId").toString());

            ReviewDto review = reviewService.createReview(customerId, contractorId, title, comment, rating, receiverType, dealId);
            return ResponseEntity.ok(Map.of("success", true, "reviewId", review.id()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}