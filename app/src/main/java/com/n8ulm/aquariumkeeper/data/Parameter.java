package com.n8ulm.aquariumkeeper.data;

import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Parameter {

	private String paramTitle;
	private List<Entry> results;
	private Double paramDate;
	private Double paramResult;

	public Parameter() {
	}

	public Parameter(String paramTitle, List<Entry> results) {
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

	public Double getParamDate() {
		return paramDate;
	}

	public void setParamDate(Double paramDate) {
		this.paramDate = paramDate;
	}

	public void setParamResult(Double paramResult) {
		this.paramResult = paramResult;
	}

	public Double getParamResult() {
		return paramResult;
	}

	public List<Entry> getResults() { return results; }

	public void setResults(List<Entry> results) { this.results = results; }




}
