package xyz.jocn.shortenurl.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.shortenurl.user.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, UserRepositoryExt {
	Optional<UserEntity> findByEmail(String email);
}
