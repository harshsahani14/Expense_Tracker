package in.harsh.moneymanager.service;

import in.harsh.moneymanager.entity.ProfileEntity;
import in.harsh.moneymanager.repositry.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        ProfileEntity profileEntity = profileRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("Profile not found for email" + email));

        return  User.builder().
                username(profileEntity.getFullName()).
                password(profileEntity.getPassword()).
                authorities(Collections.emptyList()).
                build();

    }
}
