package logintest.Service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import logintest.Domain.LoginType;
import logintest.Domain.Users;
import logintest.Repository.UserRepository;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getName();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // provider를 loginType으로 변환하여 출력
        String loginType = provider.toUpperCase();
        System.out.println("Login Type: " + loginType);
        System.out.println("Provider ID: " + providerId);
        System.out.println("Email: " + email);
        System.out.println("Name: " + name);

        if (email == null || name == null) {
            throw new OAuth2AuthenticationException("Email or Name not found from OAuth2 provider");
        }

        Users user = userRepository.findByEmail(email)
                .orElseGet(() -> createUser(email, name, loginType, providerId));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), 
                oAuth2User.getAttributes(), 
                "email");
    }

    private Users createUser(String email, String name, String loginType, String providerId) {
        Users user = new Users();
        user.setEmail(email);
        user.setName(name);

        try {
            user.setLoginType(LoginType.valueOf(loginType));
        } catch (IllegalArgumentException e) {
            throw new OAuth2AuthenticationException("Unsupported loginType: " + loginType);
        }

        user.setProviderId(providerId);
        user.setPhone(null);
        user.setPhoneVerifiy(0);
        user.setZipcode(null);
        user.setAddress1(null);
        user.setAddress2(null);
        user.setPoint(0);

        return userRepository.save(user);
    }
}