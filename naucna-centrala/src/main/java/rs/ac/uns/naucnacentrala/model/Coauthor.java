package rs.ac.uns.naucnacentrala.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Coauthor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ime;

    private String prezime;

    private String email;

    private String adresa;

    private String grad;

    private String drzava;

    @Column(name = "username", unique = true, nullable = true)
    private String username;

    private Long paperId;

    private String authorUsername;

    private float lat;

    private float lng;

}
