package com.poly.shoptrangsuc.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/lang")
public class LanguageController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/messages")
    public ResponseEntity<Map<String, String>> getMessages(@RequestParam String lang) {
        Locale locale = new Locale(lang);
        Map<String, String> messages = new HashMap<>();
        messages.put("greeting", messageSource.getMessage("greeting", null, locale));
        messages.put("product.name", messageSource.getMessage("product.name", null, locale));
        messages.put("cart.title", messageSource.getMessage("cart.title", null, locale));
        return ResponseEntity.ok(messages);
    }
}
