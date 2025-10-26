package org.pentagone.business.zentracore.hr.controller;

import org.pentagone.business.zentracore.hr.service.impl.MailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private MailServiceImpl mailService;

    @GetMapping
    public String testMail() {
        mailService.sendEmail("judicaeljeanfrancois@gmail.com", "Test Email", "This is a test email from ZentraCore HR module.");
        return "Test email sent!";
    }
}
