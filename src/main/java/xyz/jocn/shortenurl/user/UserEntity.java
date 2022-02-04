package xyz.jocn.shortenurl.user;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.jocn.shortenurl.user.enums.ProviderType;
import xyz.jocn.shortenurl.user.enums.RoleType;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(name = "uk_user_email", columnNames = "email")})
@Entity
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long uid;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false, length = 50)
	private String email;

	@Column(length = 400)
	private String picture;

	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private ProviderType providerType;

	@Column(nullable = false, length = 100)
	private String providerUid;

	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private RoleType role;

	@CreatedDate
	private Instant createdAt;

	@LastModifiedDate
	private Instant updatedAt;

	@OneToOne(fetch = FetchType.LAZY)
	@CreatedBy
	@JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_self_user_01"), updatable = false)
	private UserEntity createdBy;

	@OneToOne(fetch = FetchType.LAZY)
	@LastModifiedBy
	@JoinColumn(name = "updated_by", foreignKey = @ForeignKey(name = "fk_self_user_02"))
	private UserEntity updatedBy;

	@Builder
	public UserEntity(long uid, String name, String email, String picture, ProviderType providerType,
		String providerUid, RoleType role) {
		this.uid = uid;
		this.name = name;
		this.email = email;
		this.picture = picture;
		this.providerType = providerType;
		this.providerUid = providerUid;
		this.role = role;
	}

	public void oauthLogin(String name, String picture, ProviderType providerType, String providerUid) {
		this.name = name;
		this.picture = picture;
		this.providerType = providerType;
		this.providerUid = providerUid;
	}
}
