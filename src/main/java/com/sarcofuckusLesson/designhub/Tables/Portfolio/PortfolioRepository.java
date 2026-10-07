package com.sarcofuckusLesson.designhub.Tables.Portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Long> {

    // Найти все проекты портфолио по подрядчику
    List<PortfolioEntity> findByContractorIdOrderByCreatedAtDesc(Long contractorId);

}
