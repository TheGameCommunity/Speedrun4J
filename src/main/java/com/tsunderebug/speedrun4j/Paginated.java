package com.tsunderebug.speedrun4j;

public interface Paginated<T> extends LinkedJson, Iterable<T> {
	
	/**
	 * @param index
	 * @return the element at the specified index on this page, or null if no element exists
	 */
	public T get(int index);
	
	/**
	 * @return the maximum page size
	 */
	public int getMax();
	
	/**
	 * @return the offset of the first element in this page in relation
	 * to the first element on the first page
	 */
	public int getOffset();
	
	/**
	 * @return the size of this page
	 */
	public int getSize();
	
	/**
	 * @return the current page number
	 */
	public default int getPage() {
		return getOffset() / getMax();
	}
	
	/**
	 * Returns a new page which starts at the beginning of the current page
	 * and has a maximum size of `max`
	 * @param max the new page size
	 */
	public Paginated<T> setMaxSize(int max);
	
	/**
	 * Returns a new page which starts at the end of the current page,
	 * and has a maximum size of `max`
	 * 
	 * @param the new page size
	 */
	public Paginated<T> next();
	
	
	/**
	 * Returns a new page which ends at the start of the current page, 
	 * (or the first element if the start would be negative)
	 * and has a maximum size of `max`
	 * 
	 * @param the new page size
	 */
	public Paginated<T> prev();
	
	/**
	 * 
	 * @param offset
	 * @return a new page that starts at the provided offset which has
	 * a maximum size of the current page.
	 */
	public Paginated<T> seek(int offset);
	
	/**
	 * @param page
	 * @return a page with the offset of `page` * `getMax()`
	 */
	public Paginated<T> seekPage(int page);
	
	public boolean hasNextPage();
	
	public boolean hasPreviousPage();
	
}
