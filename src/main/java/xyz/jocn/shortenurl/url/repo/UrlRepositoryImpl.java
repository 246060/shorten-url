package xyz.jocn.shortenurl.url.repo;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.shortenurl.url.QUrlEntity;
import xyz.jocn.shortenurl.url.UrlEntity;
import xyz.jocn.shortenurl.url.enums.UrlState;

@Repository
public class UrlRepositoryImpl implements UrlRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QUrlEntity url;

	public UrlRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		url = QUrlEntity.urlEntity;
	}

	@Override
	public Page<UrlEntity> findPageByUserUid(long userUid, Pageable pageable) {

		JPAQuery<UrlEntity> contentQuery =
			queryFactory
				.selectFrom(url)
				.where(url.createdBy().uid.eq(userUid))
				.where(url.state.eq(UrlState.ACTIVE))
				.orderBy(url.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize());

		JPAQuery<Long> countQuery =
			queryFactory
				.select(url.count())
				.from(url)
				.where(url.state.eq(UrlState.ACTIVE));

		return PageableExecutionUtils.getPage(contentQuery.fetch(), pageable, countQuery::fetchOne);
	}
}
