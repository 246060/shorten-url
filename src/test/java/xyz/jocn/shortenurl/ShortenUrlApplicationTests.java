package xyz.jocn.shortenurl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import xyz.jocn.shortenurl.enums.ProviderType;

@SpringBootTest
class ShortenUrlApplicationTests {

	@Test
	void contextLoads() {
		ProviderType providerType = ProviderType.valueOf("naver");
	}

}
