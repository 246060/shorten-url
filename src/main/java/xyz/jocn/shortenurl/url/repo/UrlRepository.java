package xyz.jocn.shortenurl.url.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.shortenurl.url.UrlEntity;
import xyz.jocn.shortenurl.url.enums.UrlState;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long>, UrlRepositoryExt {
	Optional<UrlEntity> findByIdAndState(long id, UrlState state);
}
