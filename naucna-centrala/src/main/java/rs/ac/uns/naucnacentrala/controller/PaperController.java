package rs.ac.uns.naucnacentrala.controller;

import org.apache.commons.io.IOUtils;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rs.ac.uns.naucnacentrala.camunda.service.RegistrationService;
import rs.ac.uns.naucnacentrala.dto.FormFieldsDto;
import rs.ac.uns.naucnacentrala.utils.CamundaUtils;
import rs.ac.uns.naucnacentrala.utils.uploadingfiles.storage.StorageFileNotFoundException;
import rs.ac.uns.naucnacentrala.utils.uploadingfiles.storage.StorageService;

import java.io.IOException;

@Controller
@RequestMapping("/restapi/paper")
@CrossOrigin("*")
public class PaperController {

    private static final String PROCESS_KEY="obradaNaucnogRada";


    @Autowired
    CamundaUtils camundaUtils;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    TaskService taskService;

    @Autowired
    StorageService storageService;


    @PreAuthorize("hasRole('AUTHORS')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<FormFieldsDto> startProcess(){

        Task task = camundaUtils.startProcess(PROCESS_KEY);

        return new ResponseEntity<>(camundaUtils.createFormDTO(task,task.getProcessInstanceId()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('AUTHORS')")
    @RequestMapping(method = RequestMethod.POST,consumes = "multipart/form-data")
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {

        String name = storageService.store(file);

        return ResponseEntity.ok(name);
    }

    @PreAuthorize("hasRole('AUTHORS') or hasRole('UREDNIK') or hasRole('USER') or hasRole('RECEZENT')")
    @GetMapping("/files/{filename}")
    public ResponseEntity<byte[]> serveFile(@PathVariable String filename) throws IOException {
        System.out.println("Filename: "+filename);
        String realFilename=filename.replace("+"," ");
        Resource file = storageService.loadAsResource(realFilename);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream")).body(IOUtils.toByteArray(file.getInputStream()));
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
