package com.booking.restaurant.controller.api;

import com.booking.restaurant.dto.ApiResponse;
import com.booking.restaurant.dto.ReviewReplyDTO;
import com.booking.restaurant.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/reviews")
public class ReviewApiController {

    @Autowired
    private ReviewService reviewService;

    // 回覆評論
    @PostMapping("/{reviewId}/reply")
    public ResponseEntity<?> replyToReview(
            @PathVariable Integer reviewId,
            @RequestBody ReviewReplyDTO replyDTO) {
        ReviewReplyDTO updatedReview = reviewService.replyToReview(reviewId, replyDTO.getReplyContent());
        return ResponseEntity.ok(new ApiResponse(
                "success",
                "回覆已成功送出",
                1,
                updatedReview
        ));
    }
}

