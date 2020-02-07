package rs.ac.uns.naucnacentrala.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KoautorDTO {

    private String ime;

    private String prezime;

    private String email;

    private String adresa;

    private String grad;

    private String drzava;

}
