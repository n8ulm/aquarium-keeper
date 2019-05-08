package com.n8ulm.aquariumkeeper.data;

import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Parameter {

	private String paramTitle;
	private List<Entry> results;
	private float paramDate;
	private float paramResult;

	private String paramSafeRange;

	public Parameter() {
	}

	public Parameter(String paramTitle, List<Entry> results, float paramDate, float paramResult, String paramSafeRange) {
		this.paramTitle = paramTitle;
		this.results = results;
		this.paramDate = paramDate;
		this.paramResult = paramResult;
		this.paramSafeRange = paramSafeRange;
	}


	public String getParamTitle() { return  paramTitle; }

	public void setParamTitle() { this.paramTitle = paramTitle; }

	public float getParamDate() {
		return paramDate;
	}

	public void setParamDate(float paramDate) {
		this.paramDate = paramDate;
	}

	public void setParamResult(float paramResult) {
		this.paramResult = paramResult;
	}

	public float getParamResult() {
		return paramResult;
	}

	public List<Entry> getResults() { return results; }

	public void setResults(List<Entry> results) { this.results = results; }

	public String getParamSafeRange() {
		return paramSafeRange;
	}

	public void setParamSafeRange(String paramSafeRange) {
		this.paramSafeRange = paramSafeRange;
	}

	public static String getUnits(String parameter) {
		switch(parameter){
			case "pH":
				return "";
			case "Temperature":
				return "degrees";
			case "Alkalinity":
				return "dKH";
			case "General Hardness":
				return "GH";
			case "Specific Gravity":
				return "";
			default:
				return "ppm";
		}
	}


}
