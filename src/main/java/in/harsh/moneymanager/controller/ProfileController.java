package in.harsh.moneymanager.controller;

import in.harsh.moneymanager.dto.AuthDTO;
import in.harsh.moneymanager.dto.ProfileDTO;
import in.harsh.moneymanager.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO registeredProfile = profileService.registerProfile(profileDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token) {
        boolean isActivated = profileService.activateProfile(token);

        if(isActivated) {
            return ResponseEntity.status(HttpStatus.OK).body("Activated");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Activated");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDTO authDTO) {

        try {
            if(!profileService.isAccountActive(authDTO.getEmail())) {
                return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Account Not Active"));
            }
            Map<String,Object> response =  profileService.authenticateAndGenerate(authDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }
}
