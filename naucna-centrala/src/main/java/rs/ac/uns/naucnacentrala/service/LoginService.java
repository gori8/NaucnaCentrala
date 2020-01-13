package rs.ac.uns.naucnacentrala.service;


import rs.ac.uns.naucnacentrala.dto.UserDTO;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.security.auth.JwtAuthenticationRequest;

import javax.servlet.http.HttpServletRequest;


public interface LoginService {

    public abstract User checkCredentials(JwtAuthenticationRequest request);
    public abstract UserDTO register(UserDTO userDTO);
    public UserDTO login(JwtAuthenticationRequest request);
    public void changePassword(String oldPassword, String newPassword, String username) throws Exception;
    public UserDTO refreshAuthenticationToken(HttpServletRequest request);

}
