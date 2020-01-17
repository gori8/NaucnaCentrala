package rs.ac.uns.naucnacentrala.service;

import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.naucnacentrala.dto.LoginResponseDTO;
import rs.ac.uns.naucnacentrala.dto.UserCredentialsDTO;
import rs.ac.uns.naucnacentrala.dto.UserTokenState;
import rs.ac.uns.naucnacentrala.model.Authority;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.AuthorityRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.security.TokenUtils;
import rs.ac.uns.naucnacentrala.security.auth.JwtAuthenticationRequest;
import rs.ac.uns.naucnacentrala.utils.ObjectMapperUtils;

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
    PasswordEncoder passwordEncoder;

    @Autowired
    IdentityService identityService;


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


    @Override
    public boolean register(User user, String role) throws Exception{
        Authority authority = authorityRepository.findOneByName(role);
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authority);
        user.setAuthorities(authorities);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        org.camunda.bpm.engine.identity.User userCamunda=identityService.newUser(user.getUsername());
        userCamunda.setEmail(user.getEmail());
        userCamunda.setFirstName(user.getIme());
        userCamunda.setLastName(user.getPrezime());
        userCamunda.setPassword(user.getPassword());
        identityService.saveUser(userCamunda);
        String processedRole=processRoleString(role).toLowerCase();
        identityService.createMembership(userCamunda.getId(),processedRole);


        return true;
    }

    @Override
    public LoginResponseDTO login(JwtAuthenticationRequest request) {
        User user=userRepository.findByUsername(request.getUsername());
        if(user!=null){
            if(passwordEncoder.matches(request.getPassword(),user.getPassword())&&user.isEnabled()){
                String jwt = tokenUtils.generateToken(request.getUsername());
                long expiresIn = tokenUtils.getExpiredIn();
                LoginResponseDTO token=new LoginResponseDTO(jwt,expiresIn,user.getUsername(),((Authority)user.getAuthorities().toArray()[0]).getName());
                // Vrati user-a sa tokenom kao odgovor na uspesnu autentifikaciju
                return token;
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
    public UserTokenState refreshAuthenticationToken(HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = this.tokenUtils.getUsernameFromToken(token);
        User user = (User) userRepository.findByUsername(username);
        if (this.tokenUtils.canTokenBeRefreshed(token, user.getLastSifraResetDate())) {
            String refreshedToken = tokenUtils.refreshToken(token);
            long expiresIn = tokenUtils.getExpiredIn();
            UserTokenState newToken=new UserTokenState(refreshedToken,expiresIn);
            // Vrati user-a sa tokenom kao odgovor na uspesnu autentifikaciju
            return newToken;
        } else {
            return null;
        }
    }

    private String processRoleString(String s){
        return s.replace("ROLE_","");
    }

}
