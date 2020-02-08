package rs.ac.uns.naucnacentrala.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.naucnacentrala.model.CasopisStatus;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CasopisPV {

    private Long id;

    private String naziv;

    private String issn;

    private Long cena;

    private String koPlaca;

    private String processInstanceId;

    private Boolean enabled;

    private CasopisStatus casopisStatus;

    private String glavniUrednik;

    private LinkedHashMap<String, String> urednici = new LinkedHashMap<>();

    private LinkedHashMap<String, ArrayList<String>> recezenti = new LinkedHashMap<>();

}
