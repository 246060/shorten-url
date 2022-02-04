package xyz.jocn.shortenurl.user.repo;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.shortenurl.user.QUserEntity;

@Repository
public class UserRepositoryImpl implements UserRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QUserEntity user;

	public UserRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		user = QUserEntity.userEntity;
	}
}
