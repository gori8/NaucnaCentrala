package rs.ac.uns.naucnacentrala.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.naucnacentrala.model.Authority;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.NaucnaOblast;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message="Username cannot be blank")
    @NotNull(message="Username cannot be null")
    private String username;

    @NotBlank(message="Username cannot be blank")
    @NotNull(message="Username cannot be null")
    private String password;

    private String ime;

    private String prezime;

    @Email(message="Invalid email format.")
    private String email;

    private String grad;

    private String drzava;

    private String titula;

    private List<NaucnaOblastDTO> naucneOblasti = new ArrayList<NaucnaOblastDTO>();

    private String taskId;

}
