package com.sarcofuckusLesson.designhub.Tables.Contractors;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ContractorRepository extends JpaRepository<ContractorEntity, Long> {

    Optional<ContractorEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
