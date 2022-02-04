package xyz.jocn.shortenurl.url.dto;

import org.hibernate.validator.constraints.URL;

import lombok.Data;

@Data
public class CreateUrlDto {

	@URL
	private String originUrl;
}
