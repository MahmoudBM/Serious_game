package com.value.resource_backend_sg.Repository;

import com.value.resource_backend_sg.Models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepositoryFeign extends MongoRepository<User,String> {
    User findByUsername(String username);
    boolean existsByUsername(String username);

}
