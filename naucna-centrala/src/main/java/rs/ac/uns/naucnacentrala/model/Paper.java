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
public class Paper {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naslov;

    private String apstrakt;

    private String kljucniPojmovi;

    private Long naucnaOblastId;

    private String pdfPath;

    @ManyToOne(fetch = FetchType.EAGER)
    private Casopis casopis;

    private String authorUsername;



}
