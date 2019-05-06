package com.n8ulm.aquariumkeeper.ui.log;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.axes.Linear;
import com.anychart.core.cartesian.series.Line;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.n8ulm.aquariumkeeper.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class ParameterViewHolder extends RecyclerView.ViewHolder {

	TextView paramTitle;
	TextView paramSafeRange;
	LineChart parameterChart;
	TextView paramLastTestDate;
	TextView paramLastTestResult;


	public ParameterViewHolder(@NonNull View itemView) {
		super(itemView);
		paramTitle = itemView.findViewById(R.id.parameter_title_textview);
		paramSafeRange = itemView.findViewById(R.id.parameter_safe_range_textview);
		parameterChart = itemView.findViewById(R.id.paramter_chart_view);
		paramLastTestDate = itemView.findViewById(R.id.last_result_date_textview);
		paramLastTestResult = itemView.findViewById(R.id.last_result_parameter_textview);


		parameterChart.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.highlight));

		XAxis xAxis = parameterChart.getXAxis();
		xAxis.setValueFormatter(new ValueFormatter() {
			@Override
			public String getFormattedValue(float value) {
				Log.d("TESTING", String.valueOf(value));


				Date date = new Date(Float.valueOf(value).longValue());
				return new SimpleDateFormat("MMM dd", Locale.US).format(date);
			}
		});
	}

	public void setTitle(String title) {
		paramTitle.setText(title);
	}

}
