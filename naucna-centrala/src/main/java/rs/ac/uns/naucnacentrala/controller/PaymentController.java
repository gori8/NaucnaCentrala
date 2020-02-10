package rs.ac.uns.naucnacentrala.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.naucnacentrala.service.PaymentService;

import java.util.List;


@Controller
@RequestMapping("/payment")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    PaymentService kpService;

    @PostMapping(path = "/payments/complete/{uuid}/{nacinPlacanjaId}", produces = "application/json")
    public ResponseEntity completeRegistration(@PathVariable("uuid") String uuid,@PathVariable("nacinPlacanjaId") Long nacinPlacanjaId) {
        return new ResponseEntity(kpService.completeRegistration(uuid,nacinPlacanjaId),HttpStatus.OK);
    }


    @RequestMapping(value = "/payed/{username}/{uuid}/{processInstanceId}/{success}", method = RequestMethod.POST)
    public ResponseEntity<String> changePayed(@PathVariable("username")String username, @PathVariable("uuid")String uuid,@PathVariable("processInstanceId") String processInstanceId, @PathVariable("success")Boolean success) {

        return new ResponseEntity<>(kpService.changePayed(uuid,success,username,processInstanceId), HttpStatus.OK);
    }


    @RequestMapping(value = "/payed/{username}/{uuid}/{processInstanceId}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> getPayed(@PathVariable("username")String username, @PathVariable("uuid")String uuid,@PathVariable("processInstanceId")String processInstanceId) {
        return ResponseEntity.ok(true);
    }

    @RequestMapping(value = "/pay/{uapId}/{username}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER') or hasRole('AUTHORS')")
    public ResponseEntity<String> pay(@PathVariable("uapId") String uapId, @PathVariable("username") String username ) {

        String url = kpService.getRedirectUrl(uapId,username,"nebitno");
        return new ResponseEntity<>("\""+url+"\"",HttpStatus.OK);
    }

}
