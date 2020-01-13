package rs.ac.uns.naucnacentrala.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String username;
    private String password;
    private String ime;
    private String prezime;
    private String email;
    private String grad;
    private String drzava;
    private String titula;
    private String token;
    private int expiresIn;


}
