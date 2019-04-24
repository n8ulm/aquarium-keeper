package com.n8ulm.aquariumkeeper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class Parameter {

	private String id;
	private String paramTitle;
	private String paramSafeRange;

	private String parameterChart;

	private String paramLastTestDate;
	private String paramLastTestResult;

	public Parameter() {
	}

	public Parameter(String paramTitle, String paramSafeRange, String parameterChart,
					 String paramLastTestResult) {
		this.paramTitle = paramTitle;
		this.paramSafeRange = paramSafeRange;
		this.parameterChart = parameterChart;
		this.paramLastTestDate = getDate();
		this.paramLastTestResult = paramLastTestResult;
	}

	private String getDate() {
		String pattern = "MMMMM dd, yyyy";

		SimpleDateFormat sdf = new SimpleDateFormat(pattern, new Locale("en", "US"));

		return sdf.format(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParamTitle() {
		return paramTitle;
	}

	public void setParamTitle(String paramTitle) {
		this.paramTitle = paramTitle;
	}

	public String getParamSafeRange() {
		return paramSafeRange;
	}

	public void setParamSafeRange(String paramSafeRange) {
		this.paramSafeRange = paramSafeRange;
	}

	public String getParameterChart() {
		return parameterChart;
	}

	public void setParameterChart(String parameterChart) {
		this.parameterChart = parameterChart;
	}

	public String getParamLastTestDate() {
		return paramLastTestDate;
	}

	public void setParamLastTestDate(String paramLastTestDate) {
		this.paramLastTestDate = paramLastTestDate;
	}

	public String getParamLastTestResult() {
		return paramLastTestResult;
	}

	public void setParamLastTestResult(String paramLastTestResult) {
		this.paramLastTestResult = paramLastTestResult;
	}
}
