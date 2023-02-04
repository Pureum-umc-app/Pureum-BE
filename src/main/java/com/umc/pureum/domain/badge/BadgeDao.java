package com.umc.pureum.domain.badge;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class BadgeDao {

    private final EntityManager em;
}
