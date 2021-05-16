package com.value.backend_sg.Repository;

import com.value.backend_sg.Models.Medicament;
import com.value.backend_sg.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentRepository extends MongoRepository<Medicament,String> {

}
