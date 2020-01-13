package rs.ac.uns.naucnacentrala.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.naucnacentrala.dto.UserDTO;
import rs.ac.uns.naucnacentrala.security.auth.JwtAuthenticationRequest;
import rs.ac.uns.naucnacentrala.service.LoginService;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//Kontroler zaduzen za autentifikaciju korisnika

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {


	@Autowired
	LoginService loginService;



	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest){

		UserDTO user=loginService.login(authenticationRequest);
		if(user!=null){
			// Vrati user-a sa tokenom kao odgovor na uspesno autentifikaciju
			return ResponseEntity.ok(user);
		}else{
			return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
		}

	}



}