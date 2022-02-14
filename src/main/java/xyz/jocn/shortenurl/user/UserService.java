package xyz.jocn.shortenurl.user;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.shortenurl.common.exception.ResourceNotFoundException;
import xyz.jocn.shortenurl.user.converter.UserConverter;
import xyz.jocn.shortenurl.user.dto.UserDto;
import xyz.jocn.shortenurl.user.enums.ProviderType;
import xyz.jocn.shortenurl.user.enums.RoleType;
import xyz.jocn.shortenurl.user.repo.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public Long saveOrUpdate(Map<String, Object> userAttributes, ProviderType providerType, String providerUid) {

		String name = (String)userAttributes.get("name");
		String email = (String)userAttributes.get("email");
		String picture = (String)userAttributes.get("picture");
		String finalProviderUid = providerUid;

		UserEntity user = userRepository
			.findByEmail(email)
			.map(userEntity -> {
				userEntity.oauthLogin(name, picture, providerType, finalProviderUid);
				return userEntity;
			})
			.orElse(
				UserEntity.builder()
					.name(name)
					.email(email)
					.picture(picture)
					.providerType(providerType)
					.providerUid(finalProviderUid)
					.role(RoleType.USER)
					.build()
			);

		userRepository.save(user);
		return user.getUid();
	}

	public UserDto getUser(final long uid) {
		return UserConverter.INSTANCE.toDto(
			userRepository.findById(uid).orElseThrow(ResourceNotFoundException::new)
		);
	}

}
