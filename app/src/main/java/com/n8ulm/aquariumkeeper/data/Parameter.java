package com.n8ulm.aquariumkeeper.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Parameter {

	private String id;
	private String paramTitle;
	private String paramDate;
	private String paramResult;

	public Parameter() {
	}

	public Parameter(String id, String paramTitle, String paramDate, String paramResult) {
		this.id = id;
		this.paramTitle = paramTitle;
		this.paramDate = paramDate;
		this.paramResult = paramResult;
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


}
