package bt.edu.gcit.usermicroservice.security.oauth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import bt.edu.gcit.usermicroservice.security.oauth.TouristOAuth2User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import bt.edu.gcit.usermicroservice.service.TouristService;
import bt.edu.gcit.usermicroservice.entity.Tourist;
import bt.edu.gcit.usermicroservice.entity.AuthenticationType;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private TouristService touristService;

    @Autowired
    @Lazy
    public void setTouristService(TouristService touristService) {
        this.touristService = touristService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        // This method is called when a user has been successfully authenticated.
        // Here you can perform any actions you need to upon successful login.

        TouristOAuth2User oauthUser = (TouristOAuth2User) authentication.getPrincipal();
        String name = oauthUser.getName();
        String email = oauthUser.getEmail();
        String clientName = oauthUser.getClientName();

        System.out.println("OAuth2LoginSuccessHandler: " + name + " | " + email);
        System.out.println("Client Name : " + clientName);

        AuthenticationType authenticationType = getAuthenticationType(clientName);

        Tourist tourist = touristService.findByEmail(email);
        if (tourist == null) {
            touristService.addNewTouristUponOAuthLogin(name, email, authenticationType);

        } else {
            touristService.updateAuthenticationType(tourist.getId(), authenticationType);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationType getAuthenticationType(String clientName) {
        if (clientName.equals("Google")) {
            return AuthenticationType.GOOGLE;
        } else if (clientName.equals("Facebook")) {
            return AuthenticationType.FACEBOOK;

        } else {
            return AuthenticationType.DATABASE;
        }
    }
}
