package org.instagram.layers.Repositories;

import org.instagram.layers.Models.Comments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comments, Integer> {
}
