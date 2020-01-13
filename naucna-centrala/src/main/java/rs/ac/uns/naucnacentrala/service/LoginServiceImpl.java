package rs.ac.uns.naucnacentrala.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.naucnacentrala.dto.UserDTO;
import rs.ac.uns.naucnacentrala.model.Authority;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.AuthorityRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.security.TokenUtils;
import rs.ac.uns.naucnacentrala.security.auth.JwtAuthenticationRequest;
import rs.ac.uns.naucnacentrala.utils.ObjectMapperUtils;

import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class LoginServiceImpl implements  LoginService{

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    public User checkCredentials(JwtAuthenticationRequest request) {
        User user=userRepository.findByUsername(request.getUsername());
        if(user!=null){
            if(passwordEncoder.matches(request.getPassword(),user.getPassword())){
                return user;
            }
        }
        return null;
    }



    public UserDTO register(UserDTO userDTO) throws RollbackException {
        User user = new User();

        if(userRepository.findByUsername(userDTO.getUsername()) != null){
            return userDTO;
        }

        user.setPrezime(userDTO.getPrezime());
        user.setEnabled(true);
        user.setIme(userDTO.getIme());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        Authority authority = authorityRepository.findOneByName("ROLE_USER");
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authority);
        user.setAuthorities(authorities);

        user = userRepository.save(user);

        return userDTO;
    }

    @Override
    public UserDTO login(JwtAuthenticationRequest request) {
        User user=userRepository.findByUsername(request.getUsername());
        if(user!=null){
            if(passwordEncoder.matches(request.getPassword(),user.getPassword())){
                String jwt = tokenUtils.generateToken(request.getUsername());
                int expiresIn = tokenUtils.getExpiredIn();
                UserDTO userDTO= ObjectMapperUtils.map(user,UserDTO.class);
                userDTO.setExpiresIn(expiresIn);
                userDTO.setToken(jwt);
                // Vrati user-a sa tokenom kao odgovor na uspesnu autentifikaciju
                return userDTO;
            }
        }
        return null;
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String username) throws Exception{
        User user= userRepository.findByUsername(username);
        if(passwordEncoder.matches(oldPassword,user.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }else{
            throw new Exception();
        }
    }

    @Override
    public UserDTO refreshAuthenticationToken(HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = this.tokenUtils.getUsernameFromToken(token);
        User user = (User) userRepository.findByUsername(username);
        if (this.tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = tokenUtils.refreshToken(token);
            int expiresIn = tokenUtils.getExpiredIn();
            UserDTO userDTO= ObjectMapperUtils.map(user,UserDTO.class);
            userDTO.setExpiresIn(expiresIn);
            userDTO.setToken(refreshedToken);
            return userDTO;
        } else {
            return null;
        }
    }

}
