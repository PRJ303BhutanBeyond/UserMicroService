package bt.edu.gcit.usermicroservice.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import bt.edu.gcit.usermicroservice.entity.Tourist;
import bt.edu.gcit.usermicroservice.entity.User;
import org.springframework.context.annotation.Lazy;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final TouristService touristService;

    public AuthServiceImpl(@Lazy AuthenticationManager authenticationManager,
            @Lazy UserDetailsService userDetailsService, UserService userService, TouristService touristService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.touristService = touristService;
    }

    @Override
    public UserDetails login(String email, String password) {
        // Check if the user's enable status is true before authenticating
        User user = userService.findByEmail(email);
        System.err.println("hii :"+user);
        if (user != null && user.isEnabled()) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return userDetailsService.loadUserByUsername(email);
        } 
            
        Tourist tourist = touristService.findByEmail(email); 
        System.err.println("hii :"+tourist);
        if (tourist != null && tourist.isEnabled()){
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return userDetailsService.loadUserByUsername(email);    
        }

        throw new UserNotEnabledException("User not enabled or not found.");
    }

}