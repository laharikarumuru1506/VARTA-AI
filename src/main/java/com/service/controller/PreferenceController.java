package com.service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.service.PreferenceService;
import com.service.vo.PreferenceRequest;
import com.service.vo.UserContext;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/preferences")
public class PreferenceController {

	private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @PostMapping
    public ResponseEntity<Void> savePreferences(@RequestBody PreferenceRequest request) {

        preferenceService.savePreferences(request);

        return ResponseEntity.ok().build();
    }
    
    @PutMapping
    public ResponseEntity<Void> updatePreferences(@RequestBody PreferenceRequest request) {

        preferenceService.savePreferences(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<UserContext> getPreferences() {
    	
    	UserContext userContext = preferenceService.getPreferences();

        return ResponseEntity.ok(userContext);
    }
}
