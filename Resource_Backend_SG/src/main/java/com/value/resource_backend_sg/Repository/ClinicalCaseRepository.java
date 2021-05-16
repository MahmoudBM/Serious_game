package com.value.resource_backend_sg.Repository;

import com.value.resource_backend_sg.Models.ClinicalCase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicalCaseRepository extends MongoRepository<ClinicalCase,String> {
}
