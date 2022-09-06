package org.instagram.layers.Repositories;

import org.instagram.layers.Models.Accounts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends MongoRepository<Accounts,String> {

}
