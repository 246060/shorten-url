package xyz.jocn.shortenurl.url.dto;

import lombok.Data;

@Data
public class DeleteUrlDto {
	private Long uid;
	private Long urlId;
}
