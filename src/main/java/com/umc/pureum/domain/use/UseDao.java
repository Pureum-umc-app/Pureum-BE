package com.umc.pureum.domain.use;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class UseDao {
    private final EntityManager em;
}
