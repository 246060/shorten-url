package xyz.jocn.shortenurl.common.dto;

import java.util.List;

import lombok.Data;

@Data
public class PageDto<T> {

	private List<T> content;
	private Paging paging;

	public PageDto() {
		paging = new Paging();
	}

	@Data
	public static class Paging {
		private int page;
		private int size;
		private long totalElements;
		private int totalPages;
		private boolean prev;
		private boolean next;
	}
}
