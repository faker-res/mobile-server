package com.lzkj.mobile.vo;

import java.util.List;

public class RecordInsurePageVO {

    private List<RecordInsureVO> recordList;

    private int pageCount;

    private int recordCount;

	public List<RecordInsureVO> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<RecordInsureVO> recordList) {
		this.recordList = recordList;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
    
    
}
