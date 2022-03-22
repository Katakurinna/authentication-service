package me.cerratolabs.smarttech.session.infraestructure.repository;

import me.cerratolabs.smarttech.session.infraestructure.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

    Optional<AccountEntity> findByEmailOrUsername(@Param("email") String email, @Param("username") String username);

}