package com.orangomango.solarsystem;

import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.util.function.Consumer;

public class UiSlider{
	private HBox node;
	private TextField textfield;
	private double incAmount = 0.1;

	public UiSlider(String name, Consumer<Double> onChanged){
		this.textfield = new TextField();
		this.textfield.textProperty().addListener((ob, oldV, newV) -> {
			try {
				double v = Double.parseDouble(newV);
				onChanged.accept(v);
			} catch (NumberFormatException ex){}
		});

		TextField incField = new TextField("0.1");
		incField.textProperty().addListener((ob, oldV, newV) -> {
			try {
				double v = Double.parseDouble(newV);
				this.incAmount = v;
			} catch (NumberFormatException ex){}
		});

		Button dec = new Button("-");
		dec.setOnAction(e -> setValue(Double.parseDouble(this.textfield.getText())-this.incAmount));
		Button inc = new Button("+");
		inc.setOnAction(e -> setValue(Double.parseDouble(this.textfield.getText())+this.incAmount));

		this.node = new HBox(3, new Label(name), this.textfield, incField, dec, inc);
	}

	public void setValue(double value){
		this.textfield.setText(String.format("%.4f", value));
	}

	public HBox getNode(){
		return this.node;
	}
}