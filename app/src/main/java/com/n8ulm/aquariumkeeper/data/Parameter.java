package com.n8ulm.aquariumkeeper.data;

import com.anychart.chart.common.dataentry.DataEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Parameter {

	private String paramTitle;
	private List<DataEntry> results;
	private String paramDate;
	private String paramResult;

	public Parameter() {
	}

	public Parameter(String paramTitle, List<DataEntry> results) {
		this.paramTitle = paramTitle;
		this.results = results;
	}

	private String getDate() {
		String pattern = "MMMMM dd, yyyy";

		SimpleDateFormat sdf = new SimpleDateFormat(pattern, new Locale("en", "US"));

		return sdf.format(new Date());
	}

	public String getParamTitle() { return  paramTitle; }

	public void setParamTitle() { this.paramTitle = paramTitle; }

	public String getParamDate() {
		return paramDate;
	}

	public void setParamDate(String paramDate) {
		this.paramDate = paramDate;
	}

	public void setParamResult(String paramResult) {
		this.paramResult = paramResult;
	}

	public String getParamResult() {
		return paramResult;
	}

	public List<DataEntry> getResults() { return results; }

	public void setResults(List<DataEntry> results) { this.results = results; }




}
