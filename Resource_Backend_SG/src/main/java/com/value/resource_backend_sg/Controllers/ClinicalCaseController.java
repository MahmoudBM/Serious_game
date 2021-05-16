package com.value.resource_backend_sg.Controllers;

import com.value.resource_backend_sg.Models.ClinicalCase;
import com.value.resource_backend_sg.Services.ClincalCaseService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
public class ClinicalCaseController {

    @Autowired
    private ClincalCaseService clincalCaseService;

    @PostMapping("/addCC")
    public ResponseEntity<ClinicalCase> add_Clinical_Case(@RequestParam("statement") String statement, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        return clincalCaseService.addClinicalCase(statement,imageFile);
    }





}
