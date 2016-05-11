package com.starterkit.javafx.model;

import com.starterkit.javafx.dataprovider.data.BookStatusVO;

/**
 * Sex values used in GUI.
 *
 * @author Leszek/Jakub
 */
public enum BookStatus {

	FREE, LOAN, MISSING;

	/**
	 * Converts {@link BookStatusVO} to corresponding {@link BookStatus}.
	 *
	 * @param status
	 *            {@link BookStatusVO} value
	 * @return {@link BookStatus} value
	 */
	public static BookStatus fromStatusVO(BookStatusVO status) {
		return BookStatus.valueOf(status.name());
	}

	/**
	 * Converts this {@link BookStatus} to corresponding {@link BookStatusVO}. For values that
	 * do not have corresponding value {@code null} is returned.
	 *
	 * @return {@link BookStatusVO} value or {@code null}
	 */
	public BookStatusVO toStatusVO() {
		
		return BookStatusVO.valueOf(this.name());
	}
}
