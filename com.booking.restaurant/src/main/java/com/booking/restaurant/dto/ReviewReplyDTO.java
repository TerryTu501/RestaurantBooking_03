package com.booking.restaurant.dto;

public class ReviewReplyDTO {

    private String replyContent;

    // 無參數構造方法
    public ReviewReplyDTO() {
    }

    // 有參數構造方法
    public ReviewReplyDTO(String replyContent) {
        this.replyContent = replyContent;
    }

    // Getter 和 Setter
    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }


}
