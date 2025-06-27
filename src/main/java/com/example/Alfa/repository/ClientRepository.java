package com.example.Alfa.repository;

import com.example.Alfa.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByContactIdAndIdNot(Long contactId, Long id);
}
