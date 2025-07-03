package bt.edu.gcit.usermicroservice.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.User;

import java.util.List;
import java.util.stream.Collectors;
import bt.edu.gcit.usermicroservice.dao.TouristDAO; // import the new DAO for Customer entities
import bt.edu.gcit.usermicroservice.entity.Tourist; // import the new Customer entity
import java.util.Collections; // import the Collections class

@Service
public class BhutanBeyondDetailsService implements UserDetailsService {

    private final UserDAO userDAO;
    private final TouristDAO touristDAO; // new DAO for Customer entities

    public BhutanBeyondDetailsService(UserDAO userDAO, TouristDAO touristDAO) {
        this.userDAO = userDAO;
        this.touristDAO = touristDAO; // initialize the new DAO
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDAO.findByEmail(email);
        if (user != null) {
            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                    authorities);
        }

        Tourist tourist = touristDAO.findByEmail(email); 
        System.out.println("tourit:"+ tourist);
        if (tourist != null) {
            // since Authorites entities don't have authorities, we just pass an empty list of
            return new org.springframework.security.core.userdetails.User(tourist.getEmail(), tourist.getPassword(),
                    Collections.emptyList());
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
