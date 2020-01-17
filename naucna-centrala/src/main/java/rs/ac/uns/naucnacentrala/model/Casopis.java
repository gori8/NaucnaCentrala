package rs.ac.uns.naucnacentrala.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Casopis {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String naziv;

    private String issn;

    private Long cena;

    private String koPlaca;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JoinTable(name = "casopis_naucneoblasti",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "naucnaoblast_id", referencedColumnName = "id"))
    private List<NaucnaOblast> naucneOblasti=new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JoinTable(name = "casopis_nacinplacanja",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "nacinplacanja_id", referencedColumnName = "id"))
    private List<NacinPlacanja> naciniPlacanja=new ArrayList<NacinPlacanja>();

    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JoinTable(name = "casopis_recezent",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> recezenti=new ArrayList<User>();

    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JoinTable(name = "casopis_urednici",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> urednici=new ArrayList<User>();


    private String glavniUrednik;

    private CasopisStatus casopisStatus;

    private Boolean enabled=false;

    private String processInstanceId;

}
