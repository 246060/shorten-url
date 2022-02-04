package xyz.jocn.shortenurl.url;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
import xyz.jocn.shortenurl.core.Base62;
import xyz.jocn.shortenurl.url.enums.UrlState;
import xyz.jocn.shortenurl.user.UserEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "url")
@Entity
public class UrlEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "url_id")
	@Id
	private long id;

	@Column(length = 3000)
	private String originUrl;

	@Column(length = 40)
	private String shortenUrl;
	private int clickCnt;

	@Column(length = 20)
	@Enumerated(value = EnumType.STRING)
	private UrlState state;

	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_user_url_01"), updatable = false)
	private UserEntity createdBy;

	@Column(updatable = false, nullable = false)
	@CreatedDate
	private Instant createdAt;

	@Column(nullable = false)
	@LastModifiedDate
	private Instant updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@LastModifiedBy
	@JoinColumn(name = "updated_by", foreignKey = @ForeignKey(name = "fk_user_url_02"), updatable = false)
	private UserEntity updatedBy;

	@Builder
	public UrlEntity(long id, String originUrl, String shortenUrl, int clickCnt,
		UrlState state, UserEntity createdBy) {
		this.id = id;
		this.originUrl = originUrl;
		this.shortenUrl = shortenUrl;
		this.clickCnt = clickCnt;
		this.state = state;
		this.createdBy = createdBy;
	}

	public void createShortenUrl(String domain) {
		this.shortenUrl = domain + "/" + Base62.encode(id);
	}

	public String route() {
		clickCnt++;
		return originUrl;
	}

	public void delete() {
		this.state = UrlState.DELETED;
	}
}
