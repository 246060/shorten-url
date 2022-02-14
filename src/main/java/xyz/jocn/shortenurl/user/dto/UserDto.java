package xyz.jocn.shortenurl.user.dto;

import java.time.Instant;

import lombok.Data;
import xyz.jocn.shortenurl.user.enums.ProviderType;
import xyz.jocn.shortenurl.user.enums.RoleType;

@Data
public class UserDto {

	private long uid;
	private String name;
	private String email;
	private String picture;
	private ProviderType providerType;
	private String providerUid;
	private RoleType role;
	private Instant createdAt;
	private Instant updatedAt;
	private long createdBy;
	private long updatedBy;
}
