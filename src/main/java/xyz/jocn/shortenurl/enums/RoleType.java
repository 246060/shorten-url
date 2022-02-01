package xyz.jocn.shortenurl.enums;

public enum RoleType {
	USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

	private String fullName;

	RoleType(String fullName) {
		this.fullName = fullName;
	}

	public String getFullName() {
		return fullName;
	}
}
