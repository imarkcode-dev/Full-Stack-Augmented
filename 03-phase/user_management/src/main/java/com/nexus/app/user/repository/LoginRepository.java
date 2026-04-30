package com.nexus.app.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexus.app.user.domain.dto.LoginResponseDTO;
import com.nexus.app.user.domain.entity.LoginEntity;
import com.nexus.app.user.domain.projection.LoginProjection;


public interface LoginRepository extends JpaRepository <LoginEntity, Integer> {

   
    @Query(value = "SELECT fn_create_login(:userId, :username, :password)",
           nativeQuery = true)
    Boolean createLogin(
      @Param("userId") Integer userId,
      @Param("username") String username,
      @Param("password") String password
    );


    @Query(value = """
      SELECT 
          user_id as userId,
          name,
          last_login as lastLogin
      FROM fn_login(:username, :password)
      """,
          nativeQuery = true)
    Optional<LoginProjection> login(@Param("username") String username,
                           @Param("password") String password);
    


}
