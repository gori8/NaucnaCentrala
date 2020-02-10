package rs.ac.uns.naucnacentrala.controller;

import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.dto.FormFieldsDto;
import rs.ac.uns.naucnacentrala.dto.LinkeviDTO;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.Link;
import rs.ac.uns.naucnacentrala.model.NacinPlacanja;
import rs.ac.uns.naucnacentrala.repository.CasopisRepository;
import rs.ac.uns.naucnacentrala.repository.NacinPlacanjaRepository;
import rs.ac.uns.naucnacentrala.utils.CamundaUtils;
import rs.ac.uns.naucnacentrala.utils.ObjectMapperUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/restapi/journal")
@CrossOrigin("*")
public class ScienceJournalController {

    private static final String PROCESS_KEY="dodavanjeCasopisa";


    @Autowired
    CamundaUtils camundaUtils;

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    NacinPlacanjaRepository nacinPlacanjaRepository;


    @PreAuthorize("hasRole('UREDNIK')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<FormFieldsDto> startProcess(){

        Task task = camundaUtils.startProcess(PROCESS_KEY);

        return new ResponseEntity<>(camundaUtils.createFormDTO(task,task.getProcessInstanceId()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('UREDNIK')")
    @RequestMapping(method = RequestMethod.GET, value = "/mine")
    public ResponseEntity getAllMine(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Casopis> casopisList = casopisRepository.findByGlavniUrednik(username);
        List<CasopisDTO> retList=new ArrayList<>();
        for(Casopis casopis : casopisList){
            CasopisDTO casopisDTO = ObjectMapperUtils.map(casopis, CasopisDTO.class);
            for(Link link : casopis.getLinkovi()){
                if(!link.getCompleted()){
                    LinkeviDTO ldto = new LinkeviDTO();
                    NacinPlacanja np = nacinPlacanjaRepository.getOne(link.getNacinPlacanja());
                    ldto.setLabel(np.getName()+" registration");
                    ldto.setUrl(link.getUrl());
                    casopisDTO.getLinkevi().add(ldto);
                }
            }
            retList.add(casopisDTO);
        }
        return ResponseEntity.ok().body(retList);
    }

}
