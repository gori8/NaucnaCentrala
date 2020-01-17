package rs.ac.uns.naucnacentrala.service;


import rs.ac.uns.naucnacentrala.dto.LoginResponseDTO;
import rs.ac.uns.naucnacentrala.dto.UserCredentialsDTO;
import rs.ac.uns.naucnacentrala.dto.UserTokenState;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.security.auth.JwtAuthenticationRequest;

import javax.servlet.http.HttpServletRequest;


public interface LoginService {

    public abstract User checkCredentials(JwtAuthenticationRequest request);
    public abstract boolean register(User user, String role) throws Exception;
    public abstract LoginResponseDTO login(JwtAuthenticationRequest request);
    public abstract void changePassword(String oldPassword, String newPassword, String username) throws Exception;
    public abstract UserTokenState refreshAuthenticationToken(HttpServletRequest request);

}
