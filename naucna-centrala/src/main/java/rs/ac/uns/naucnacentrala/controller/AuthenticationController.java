package rs.ac.uns.naucnacentrala.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.naucnacentrala.dto.LoginResponseDTO;
import rs.ac.uns.naucnacentrala.dto.UserCredentialsDTO;
import rs.ac.uns.naucnacentrala.dto.UserTokenState;
import rs.ac.uns.naucnacentrala.security.auth.JwtAuthenticationRequest;
import rs.ac.uns.naucnacentrala.service.LoginService;


//Kontroler zaduzen za autentifikaciju korisnika

@RestController
@RequestMapping(value = "/restapi/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {


	@Autowired
	LoginService loginService;



	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest){

		LoginResponseDTO token=loginService.login(authenticationRequest);
		if(token!=null){
			// Vrati user-a sa tokenom kao odgovor na uspesno autentifikaciju
			return ResponseEntity.ok(token);
		}else{
			return ResponseEntity.badRequest().body("Incorrect username or password");
		}

	}



}