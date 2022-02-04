package xyz.jocn.shortenurl.user.enums;

public enum RoleType {
	USER("ROLE_USER"), ADMIN("ROLE_ADMIN"), SYSTEM("ROLE_SYSTEM");

	private String fullName;

	RoleType(String fullName) {
		this.fullName = fullName;
	}

	public String getFullName() {
		return fullName;
	}
}
