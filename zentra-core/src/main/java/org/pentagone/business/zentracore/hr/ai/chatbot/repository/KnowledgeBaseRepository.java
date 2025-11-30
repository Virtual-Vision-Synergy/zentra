package org.pentagone.business.zentracore.hr.ai.chatbot.repository;

import org.pentagone.business.zentracore.hr.ai.chatbot.entity.KnowledgeBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeBaseRepository extends JpaRepository<KnowledgeBase, Long> {
    List<KnowledgeBase> findByActiveTrue();
    List<KnowledgeBase> findByCategory(String category);

    @Query("SELECT k FROM KnowledgeBase k WHERE k.active = true AND " +
           "(LOWER(k.question) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(k.keywords) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<KnowledgeBase> findByKeyword(@Param("keyword") String keyword);
}

