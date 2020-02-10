package rs.ac.uns.naucnacentrala.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Link {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", unique = false, nullable = false)
    private String url;

    @Column(name = "completed", unique = false, nullable = false)
    private Boolean completed;

    @ManyToOne(fetch = FetchType.EAGER)
    private Casopis casopis;

    @Column(name = "nacinPlacanja", unique = false, nullable = false)
    private Long nacinPlacanja;
}
