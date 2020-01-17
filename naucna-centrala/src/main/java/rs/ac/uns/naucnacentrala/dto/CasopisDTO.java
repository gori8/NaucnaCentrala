package rs.ac.uns.naucnacentrala.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.naucnacentrala.model.CasopisStatus;
import rs.ac.uns.naucnacentrala.model.NacinPlacanja;
import rs.ac.uns.naucnacentrala.model.NaucnaOblast;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CasopisDTO {

    private Long id;

    private String naziv;

    private String issn;

    private Long cena;

    private String koPlaca;

    private List<NaucnaOblastDTO> naucneOblasti=new ArrayList<>();

    private List<NacinPlacanjaDTO> naciniPlacanja=new ArrayList<>();

    private String taskId;

    private String processInstanceId;

    private Boolean enabled;

    private CasopisStatus casopisStatus = CasopisStatus.WAITING_FOR_INPUT;


}
