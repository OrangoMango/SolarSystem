package com.orangomango.solarsystem;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;

import java.util.function.Consumer;

public class UiSlider{
	private HBox node;
	private Slider slider;
	private TextField minValue, maxValue;

	public UiSlider(double min, double max, double value, Consumer<Double> onChanged){
		this.minValue = new TextField(Double.toString(min));
		this.minValue.setMaxWidth(50);
		this.maxValue = new TextField(Double.toString(max));
		this.maxValue.setMaxWidth(50);
		this.slider = new Slider(min, max, value);
		this.minValue.textProperty().addListener((ob, oldV, newV) -> this.slider.setMin(Double.parseDouble(newV)));
		this.maxValue.textProperty().addListener((ob, oldV, newV) -> this.slider.setMax(Double.parseDouble(newV)));
		Label currentValue = new Label(Double.toString(value));
		currentValue.setOnMousePressed(e -> {
			TextInputDialog dialog = new TextInputDialog(currentValue.getText());
			dialog.setTitle("Set value");
			dialog.setHeaderText("Set value");
			dialog.showAndWait().ifPresent(v -> this.slider.setValue(Double.parseDouble(v)));
		});
		this.slider.valueProperty().addListener((ob, oldV, newV) -> {
			currentValue.setText(String.format("%.3f", newV));
			onChanged.accept((double)newV);
		});
		this.node = new HBox(3, this.minValue, new VBox(2, this.slider, currentValue), this.maxValue);
	}

	public void setValue(double value){
		setMin(value*0.5);
		setMax(value*1.5);
		this.slider.setValue(value);
	}

	private void setMin(double value){
		this.slider.setMin(value);
		this.minValue.setText(Double.toString(value));
	}

	private void setMax(double value){
		this.slider.setMax(value);
		this.maxValue.setText(Double.toString(value));
	}

	public HBox getNode(){
		return this.node;
	}
}