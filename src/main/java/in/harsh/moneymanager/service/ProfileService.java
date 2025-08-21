package in.harsh.moneymanager.service;

import in.harsh.moneymanager.dto.AuthDTO;
import in.harsh.moneymanager.dto.ProfileDTO;
import in.harsh.moneymanager.entity.ProfileEntity;
import in.harsh.moneymanager.repositry.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ProfileDTO registerProfile(ProfileDTO profileDTO) {

        ProfileEntity newProfile = toEntity(profileDTO);
        newProfile.setActivationToken(UUID.randomUUID().toString());

        newProfile = profileRepository.save(newProfile);

        //Send activation email
        String activationLink = "http://localhost:8080/api/v1.0/activate?token=" + newProfile.getActivationToken();
        String subject = "Activate your money manager acount";
        String body = "Click on the following link to activate your money manager: " + activationLink;

        emailService.sendEmail(newProfile.getEmail(), subject, body);
        return toDTO(newProfile);
    }

    public ProfileEntity toEntity(ProfileDTO profileDTO) {
        return ProfileEntity.builder().
                id(profileDTO.getId()).
                fullName(profileDTO.getFullName()).
                email(profileDTO.getEmail()).
                password(passwordEncoder.encode(profileDTO.getPassword())).
                profileImageUrl(profileDTO.getProfileImageUrl()).
                createdAt(profileDTO.getCreatedAt()).
                updatedAt(profileDTO.getUpdatedAt()).
                build();
    }

    public ProfileDTO toDTO(ProfileEntity profileEntity) {
        return ProfileDTO.builder().
                id(profileEntity.getId()).
                fullName(profileEntity.getFullName()).
                email(profileEntity.getEmail()).
                profileImageUrl(profileEntity.getProfileImageUrl()).
                createdAt(profileEntity.getCreatedAt()).
                updatedAt(profileEntity.getUpdatedAt()).
                build();
    }

    public boolean activateProfile(String activationToken) {
        return  profileRepository.findByActivationToken(activationToken).
                map( profile -> {
                    profile.setIsActive(true);
                    profileRepository.save(profile);
                    return true;
                }).orElse(false);
    }

    public boolean isAccountActive(String email){
        return profileRepository.findByEmail(email).
                map(profileEntity -> profileEntity.getIsActive()).orElse(false);
    }

    public ProfileEntity getCurrentProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return profileRepository.findByEmail(authentication.getName()).
                orElseThrow(()-> new UsernameNotFoundException("Profile not found with email"+authentication.getName()));


    }

    public ProfileDTO getPublicProfile(String email){

        ProfileEntity currentUser = null;
        if(email == null){
            currentUser = getCurrentProfile();
        }
        else{
            currentUser = profileRepository.findByEmail(email)
                    .orElseThrow(()-> new UsernameNotFoundException("Profile not found with email"+email));
        }

        return toDTO(currentUser);

    }

    public Map<String, Object> authenticateAndGenerate(AuthDTO authDTO) {

        return null;
    }
}
