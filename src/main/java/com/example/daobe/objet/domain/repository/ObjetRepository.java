package com.example.daobe.objet.domain.repository;

import com.example.daobe.objet.application.dto.ObjetResponseDto;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjetRepository extends JpaRepository<Objet, Long> {

    @Query("""
            SELECT o FROM Objet o 
            JOIN o.objetSharers s 
            WHERE o.lounge.id = :loungeId
            AND o.status = :status 
            AND s.user.id = :userId 
            ORDER BY o.id DESC
            """)
    List<ObjetResponseDto> findActiveObjetsInLoungeOfSharer(
            @Param("userId") Long userId, @Param("loungeId") Long loungeId, @Param("status") ObjetStatus status
    );

    @Query("""
            SELECT o FROM Objet o
            WHERE o.lounge.id = :loungeId
            AND o.status = :status
            ORDER BY o.id DESC
            """)
    List<ObjetResponseDto> findActiveObjetsInLounge(
            @Param("loungeId") Long loungeId, @Param("status") ObjetStatus status
    );


}
