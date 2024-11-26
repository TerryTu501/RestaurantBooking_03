package com.booking.restaurant.service;

import com.booking.restaurant.dto.ReviewReplyDTO;
import com.booking.restaurant.model.Review;
import com.booking.restaurant.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public ReviewReplyDTO replyToReview(Integer reviewId, String replyContent) {
        // 查找 Review
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("評論不存在"));

        // 更新回覆內容
        review.setReplyContent(replyContent);
        reviewRepository.save(review);

        // 發送郵件通知
        String emailSubject = "餐廳已回覆您的評論！";
        String emailBody = String.format("""
          親愛的 %s:

          感謝您抽出寶貴的時間留下評論！ 
          我們非常重視每一位客人的反饋，並將其作為提升服務的重要依據。
                                 
          關於您的評論：
          「%s」

          餐廳於 %s 回覆：
          「%s」
        		
          如還有任何建議或進一步的協助，也請隨時與我們聯繫~
 
          Best Regards,
          XYZ 餐廳管理團隊
          """,
                review.getUser().getUsername(),
                review.getComment(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                replyContent);

        emailService.sendEmail(review.getUser().getEmail(), emailSubject, emailBody);

        // 返回 DTO 而非實體
        return new ReviewReplyDTO(replyContent);
    }
}

