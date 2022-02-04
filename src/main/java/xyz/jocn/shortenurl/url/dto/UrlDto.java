package xyz.jocn.shortenurl.url.dto;

import lombok.Data;

@Data
public class UrlDto {
	private long id;
	private String originUrl;
	private String shortenUrl;
	private int clickCnt;
}
