package com.value.resource_backend_sg.Services;

import com.value.resource_backend_sg.Models.ClinicalCase;
import com.value.resource_backend_sg.Repository.ClinicalCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ClincalCaseService {

    @Autowired
    private ClinicalCaseRepository clinicalCaseRepository;

    public ResponseEntity<ClinicalCase> addClinicalCase(String statement,MultipartFile imgFile) throws IOException {
        String folder = "/Users/Mahmoud/Desktop/Value DS/PFE/Resource_Backend_SG/src/main/resources/photo/";
        byte[] bytes = imgFile.getBytes();
        Path path = Paths.get(folder +imgFile.getOriginalFilename());
        Files.write(path,bytes);

        ClinicalCase clinicalCase= new ClinicalCase();
        clinicalCase.setStatement(statement);
        clinicalCase.setImage(imgFile.getOriginalFilename());
        clinicalCase = clinicalCaseRepository.insert(clinicalCase);
        return ResponseEntity.ok(ClinicalCase.builder()
                .statement(clinicalCase.getStatement())
        .image(clinicalCase.getImage())
        .build());
    }

}
