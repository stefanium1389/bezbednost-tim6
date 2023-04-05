package bezbednosttim6.dto;

import java.util.ArrayList;
import java.util.List;

public class DTOList<T> {

	int totalCount;
	List<T> results;
	
	public DTOList(int totalCount, List<T> results) {
		super();
		this.totalCount = totalCount;
		this.results = results;
	}

	public DTOList() {
		super();
		this.totalCount = 0;
		this.results = new ArrayList<T>();
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public void add(T element) {
		this.totalCount++;
		this.results.add(element);
	}
}
