package com.n8ulm.aquariumkeeper;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChartView;
import com.anychart.core.cartesian.series.Line;

class ParameterViewHolder extends RecyclerView.ViewHolder {

	TextView paramTitle;
	TextView paramSafeRange;
	AnyChartView parameterChart;
	TextView paramLastTestDate;
	TextView paramLastTestResult;


	public ParameterViewHolder(@NonNull View itemView) {
		super(itemView);
		paramTitle = itemView.findViewById(R.id.parameter_title_textview);
		paramSafeRange = itemView.findViewById(R.id.parameter_safe_range_textview);
		parameterChart = itemView.findViewById(R.id.paramter_chart_view);
		paramLastTestDate = itemView.findViewById(R.id.last_result_date_textview);
		paramLastTestResult = itemView.findViewById(R.id.last_result_parameter_textview);
	}
}
