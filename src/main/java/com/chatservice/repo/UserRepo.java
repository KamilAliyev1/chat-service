package com.chatservice.repo;

import com.chatservice.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    @EntityGraph(value = "user.chats",type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findById(Long aLong);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE user u SET u.active = :b WHERE u.id = :id")
    int setUserActive(@Param("b") Boolean b ,@Param("id") Long id);

}
