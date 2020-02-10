package rs.ac.uns.naucnacentrala.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.url.UrlClass;
import rs.ac.uns.naucnacentrala.dto.ItemDTO;
import rs.ac.uns.naucnacentrala.dto.LinkDTO;
import rs.ac.uns.naucnacentrala.dto.MappingClass;
import rs.ac.uns.naucnacentrala.dto.ReturnLinksDTO;
import rs.ac.uns.naucnacentrala.model.*;
import rs.ac.uns.naucnacentrala.repository.CasopisRepository;
import rs.ac.uns.naucnacentrala.repository.LinkRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import org.springframework.boot.configurationprocessor.json.JSONObject;



import java.util.ArrayList;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserRepository korisnikRepository;

    @Autowired
    TaskService taskService;

    @Override
    public void createLinks(Long casopisId){
        Casopis casopis = casopisRepository.getOne(casopisId);

        ItemDTO dto = new ItemDTO();
        dto.setNaziv(casopis.getNaziv());
        dto.setAmount(casopis.getCena());
        dto.setRedirectUrl(UrlClass.REDIRECT_URL_REGISTRATION_IGOR);
        dto.setNaciniPlacanja(new ArrayList<>());
        User glavniUrednik = korisnikRepository.findByUsername(casopis.getGlavniUrednik());
        dto.setEmail(glavniUrednik.getEmail());
        for (NacinPlacanja np:casopis.getNaciniPlacanja()) {
            dto.getNaciniPlacanja().add(np.getId());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ItemDTO> entity = new HttpEntity<ItemDTO>(dto, headers);

        ResponseEntity<ReturnLinksDTO> response =
                restTemplate.postForEntity(UrlClass.USER_AND_PAYMENT_URL+"register",entity,ReturnLinksDTO.class);

        ReturnLinksDTO responseBody = response.getBody();

        casopis.setUuid(UUID.fromString(responseBody.getUuid()));

        if(response.getStatusCode().value()==200) {
            for (LinkDTO linkDTO : responseBody.getLinks()) {
                Link link = new Link();
                link.setCasopis(casopis);
                link.setUrl(linkDTO.getLink());
                link.setCompleted(false);
                link.setNacinPlacanja(linkDTO.getNacinPlacanjaId());
                link = linkRepository.save(link);

                casopis.getLinkovi().add(link);
            }
        }

        casopisRepository.save(casopis);
    }

    @Override
    public String completeRegistration(String uuid, Long nacinPlacanjaId){
        Casopis casopis = casopisRepository.findByUuid(UUID.fromString(uuid));
        Link link = linkRepository.findOneByCasopisUuidAndNacinPlacanja(UUID.fromString(uuid),nacinPlacanjaId);
        link.setCompleted(true);
        linkRepository.save(link);

        boolean flag=true;
        for(Link l : casopis.getLinkovi()){
            if(!l.getCompleted()){
                flag=false;
                break;
            }
        }

        if(flag){
            casopis.setCasopisStatus(CasopisStatus.WAITING_FOR_APPROVAL);
            casopisRepository.save(casopis);
        }

        return UrlClass.FRON_WEBSHOP+"urednik/journals";
    }


    @Override
    public String getRedirectUrl(String uapId,String username,String processInstanceId){
        ObjectMapper mapper = new ObjectMapper();

        MappingClass mc = new MappingClass();
        mc.setRedirectUrl(UrlClass.REDIRECT_URL_PAYMENT_IGOR+username+"/"+uapId+"/"+processInstanceId);
        mc.setId(uapId);

        String json="";

        try {
            json = mapper.writeValueAsString(mc);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json, headers);

        ResponseEntity<String> response
                = restTemplate.postForEntity(UrlClass.DOBAVI_KP_FRONT_URL_SA_NACINIMA_PLACANJA_FROM_PAYMENT_INFO,entity,String.class);

        JSONObject actualObj=null;
        String ret = "";

        try {
            actualObj = new JSONObject(response.getBody());
            ret = actualObj.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    public String changePayed(String uuid, Boolean success, String username, String processInstanceId) {
        if (success) {
            UUID realUuid = UUID.fromString(uuid);
            Casopis casopis = casopisRepository.findByUuid(realUuid);

            if(casopis==null){
                return UrlClass.FRON_WEBSHOP+"paymentresponse/failed";
            }

            User user = korisnikRepository.findByUsername(username);

            user.getKupljeniCasopisi().add(casopis);

            korisnikRepository.save(user);

            Task task = taskService.createTaskQuery().active().processInstanceId(processInstanceId).taskDefinitionKey("placanje_pretplate").singleResult();

            if(task!=null){
                taskService.complete(task.getId());
            }

            return UrlClass.FRON_WEBSHOP+"input/paper/"+processInstanceId;
        } else {
            return UrlClass.FRON_WEBSHOP+"paymentresponse/failed";
        }
    }
}
