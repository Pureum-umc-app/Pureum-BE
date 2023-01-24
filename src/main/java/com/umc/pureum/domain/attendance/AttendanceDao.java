package com.umc.pureum.domain.attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class AttendanceDao {
    private final EntityManager em;
}
