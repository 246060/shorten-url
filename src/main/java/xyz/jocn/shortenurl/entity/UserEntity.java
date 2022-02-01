package xyz.jocn.shortenurl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.jocn.shortenurl.enums.ProviderType;
import xyz.jocn.shortenurl.enums.RoleType;

@Getter
@Table(name = "user")
@Entity
@NoArgsConstructor
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long uid;

	@Column(nullable = false)
	private String name;

	@Column(unique = true, nullable = false)
	private String email;

	private String picture;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ProviderType providerType;

	@Column(nullable = false)
	private String providerUid;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private RoleType role;

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

	public void oauthLogin(String name, String email, String picture, ProviderType providerType, String providerUid) {
		this.name = name;
		this.email = email;
		this.picture = picture;
		this.providerType = providerType;
		this.providerUid = providerUid;
	}
}
