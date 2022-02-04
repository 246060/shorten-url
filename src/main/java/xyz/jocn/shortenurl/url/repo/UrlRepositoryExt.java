package xyz.jocn.shortenurl.url.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import xyz.jocn.shortenurl.url.UrlEntity;

public interface UrlRepositoryExt {

	Page<UrlEntity> findPageByUserUid(long userUid, Pageable pageable);
}
