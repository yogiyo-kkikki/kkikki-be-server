package com.example.kkikki_be_server.domain.combination.repository;

import com.example.kkikki_be_server.domain.combination.document.CombinationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CombinationMongoRepository extends MongoRepository<CombinationDocument, String> {
    // String 타입의 ObjectId를 기반으로 조작합니다.
}