package org.instagram.layers.Repositories;

import org.instagram.layers.Models.Posts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostFeedRepository extends MongoRepository<Posts, Integer> {
}
