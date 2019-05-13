package com.n8ulm.aquariumkeeper.ui.log;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.n8ulm.aquariumkeeper.R;
import com.n8ulm.aquariumkeeper.data.Parameter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class ParameterViewHolder extends RecyclerView.ViewHolder {

	TextView paramTitle;
	TextView paramSafeRange;
	LineChart parameterChart;
	TextView paramLastResult;
	Button editResults;
	Button addResult;
	ImageButton overflowMenu;


	public ParameterViewHolder(@NonNull View itemView) {
		super(itemView);
		paramTitle = itemView.findViewById(R.id.parameter_title_textview);
		paramSafeRange = itemView.findViewById(R.id.parameter_safe_range_textview);
		parameterChart = itemView.findViewById(R.id.paramter_chart_view);
		paramLastResult = itemView.findViewById(R.id.last_result_date_textview);
		editResults = itemView.findViewById(R.id.edit_results);
		addResult = itemView.findViewById(R.id.add_new_result);
		overflowMenu = itemView.findViewById(R.id.set_safe_range);

		parameterChart.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.colorLightPurpleHighlight));

		XAxis xAxis = parameterChart.getXAxis();
		xAxis.setValueFormatter(new ValueFormatter() {
			@Override
			public String getFormattedValue(float value) {

				Date date = new Date(Float.valueOf(value).longValue());
				return new SimpleDateFormat("MMM dd", Locale.US).format(date);
			}
		});
		xAxis.setGranularityEnabled(true);

		parameterChart.setNoDataText("No Results Added");

	}

	public void setTitle(String title) {
		paramTitle.setText(title);
	}

	public void setLastResult(float value, float result) {
		Date date = new Date(Float.valueOf(value).longValue());
		String units = Parameter.getUnits((String)paramTitle.getText());
		this.paramLastResult.setText("Last Result: " + new SimpleDateFormat("MMM dd", Locale.US).format(date)
			+ " - " + String.valueOf(result) + " " + units);


	}

    public void setSafeRange(String paramSafeRange) {
		this.paramSafeRange.setText(paramSafeRange);
    }
}




