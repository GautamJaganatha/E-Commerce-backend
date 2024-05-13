package com.example.ecom.repository;

import com.example.ecom.entity.category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<category, Long> {

    category findByName(String name);
}
