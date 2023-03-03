package com.umc.pureum.domain.user;

import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.domain.user.entity.mapping.UserProfileMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<UserAccount, Long> {
    boolean existsByNicknameAndStatus(String nickname, String status);
    boolean existsByKakaoIdAndStatus(Long kakaoId, String status);
    UserAccount findByKakaoIdAndStatus(Long id, String status);

    Optional<UserAccount> findByIdAndStatus(Long id, String status);

    @Query(value = "select u.nickname as nickname, u.image as image, u.grade as grade from UserAccount u" +
            " where u.id = :id and u.status =:status")
    UserProfileMapping findUserProfile(
            @Param("id")Long id,
            @Param("status") String status);

    boolean existsByIdAndStatus(Long id, String status);


    @Query(value = "select * from user_account " +
            "where id <> :id and status = 'A' order by RAND() limit 20", nativeQuery = true)
    List<UserAccount> findRandomUsersExcludeMe(@Param("id") Long id);

}
