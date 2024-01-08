package com.orangomango.solarsystem;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.util.function.Consumer;

public class UiSlider{
	private HBox node;
	private Slider slider;

	public UiSlider(double min, double max, double value, Consumer<Double> onChanged){
		TextField minValue = new TextField(Double.toString(min));
		minValue.setMaxWidth(50);
		TextField maxValue = new TextField(Double.toString(max));
		maxValue.setMaxWidth(50);
		this.slider = new Slider(min, max, value);
		minValue.textProperty().addListener((ob, oldV, newV) -> slider.setMin(Double.parseDouble(newV)));
		maxValue.textProperty().addListener((ob, oldV, newV) -> slider.setMax(Double.parseDouble(newV)));
		Label currentValue = new Label(Double.toString(value));
		this.slider.valueProperty().addListener((ob, oldV, newV) -> {
			currentValue.setText(String.format("%.3f", newV));
			onChanged.accept((double)newV);
		});
		this.node = new HBox(3, minValue, new VBox(2, this.slider, currentValue), maxValue);
	}

	public void setValue(double value){
		this.slider.setValue(value);
	}

	public HBox getNode(){
		return this.node;
	}
}