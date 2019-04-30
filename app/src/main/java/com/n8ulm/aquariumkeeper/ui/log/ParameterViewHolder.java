package com.n8ulm.aquariumkeeper.ui.log;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.axes.Linear;
import com.anychart.core.cartesian.series.Line;
import com.n8ulm.aquariumkeeper.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	public void setTitle(String title) {
		paramTitle.setText(title);
	}

	public void setChart(List<DataEntry> results) {
		Cartesian line = AnyChart.line();
		line.data(results);

		AnyChartView anyChartView = (AnyChartView) parameterChart;
		anyChartView.setChart(line);

	}
}
