package Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import Domain.LoginType;
import Domain.Users;
import Dto.RegisterRequest;
import Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void registerUser(RegisterRequest request) {
        Users user = new Users();
        user.setId(request.getUserId()); 
        user.setPassword(new BCryptPasswordEncoder().encode(request.getUserPw()));
        user.setEmail(request.getUserEmail());
        user.setName(request.getUserName());
        user.setPhone(request.getPhone());
        user.setPhoneVerifiy(request.getPhoneVerifiy());
        user.setZipcode(request.getUserZipcode());
        user.setAddress1(request.getUserAddress1());
        user.setAddress2(request.getUserAddress2());
        user.setLoginType(LoginType.LOCAL);
        user.setProviderId(request.getProviderId());
        user.setPoint(0);
        userRepository.save(user);
    }

    public Optional<Users> findByUserNo(Long userNo) {
        return userRepository.findByUserNo(userNo);
    }

    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public void updateUser(Users user) {
        userRepository.save(user);
    }

    public void deleteUser(Long userNo) {
        userRepository.deleteById(userNo);
    }
}