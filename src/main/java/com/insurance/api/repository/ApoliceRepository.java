package com.insurance.api.repository;

import com.insurance.api.model.Apolice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApoliceRepository extends JpaRepository<Apolice, Long> {
}