package com.value.backend_sg.Controllers;

import com.value.backend_sg.Models.MedicDetails;
import com.value.backend_sg.Models.Medicament;
import com.value.backend_sg.Models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


public class MedicItemProcess implements ItemProcessor<MedicDetails,Medicament> {
    private final Logger log = LoggerFactory.getLogger(MedicItemProcess.class);



    @Override
    public Medicament process(MedicDetails med) throws Exception {
        log.info("processing medicaments data.....{}", med);
        Medicament transformMedic = new Medicament();
        transformMedic.setNom(med.getNom());
        transformMedic.setReference(med.getReference());
        return transformMedic;
    }


}
