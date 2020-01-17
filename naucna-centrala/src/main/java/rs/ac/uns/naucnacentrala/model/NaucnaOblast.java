package rs.ac.uns.naucnacentrala.model;

import lombok.Data;
import org.apache.ibatis.annotations.Many;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class NaucnaOblast implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "naucneOblasti",cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    }, fetch = FetchType.LAZY)
    private List<Casopis> casopisi=new ArrayList<Casopis>();

    @ManyToMany(mappedBy = "naucneOblasti",cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    }, fetch = FetchType.LAZY)
    private List<User> users=new ArrayList<User>();

}
