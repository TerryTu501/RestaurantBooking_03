//package com.booking.restaurant;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Component;
//
//@Component
//public class EmailTestRunner implements CommandLineRunner {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    @Override
//    public void run(String... args) throws Exception {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo("terry85501@gmail.com"); // 替換為測試收件者的 Email 地址
//            message.setSubject("測試郵件");
//            message.setText("這是一封測試郵件。");
//            message.setFrom("terry85501@gmail.com");
//
//            mailSender.send(message);
//            System.out.println("郵件已成功發送！");
//        } catch (Exception e) {
//            System.err.println("郵件發送失敗：" + e.getMessage());
//        }
//    }
//}
