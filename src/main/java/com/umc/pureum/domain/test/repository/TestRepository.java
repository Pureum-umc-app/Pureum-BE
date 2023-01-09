package com.umc.pureum.domain.test.repository;

import com.umc.pureum.domain.test.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
