package com.nemo9955.garden_revolution.utility.stage;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public enum IconType {

	VANATA("aubergin"), //
	MAR("shiny-apple"), //
	GHINDA("acorn"), //
	TINTA("targeting"), //
	CIUPERCA("spotted-mushroom"), //
	SAGETI("radial-balance"), //
	ATINTIT("profit"), //
	ARC_FULGER("lightning-bow"), //
	CROSBOW("cross-bow"), //
	TUN("cannon"), //
	RAZA("beam-wake"), //
	TINTA_ARC("archery-target");//

	String	nume;

	IconType(String nume) {
		this.nume = nume;
	}

	public String getName() {
		return nume;
	}

	public Drawable getAsDrawable( Skin skin ) {
		return skin.getDrawable(nume);
	}

	public Drawable getAsDrawable( Skin skin, float width, float height ) {
		Drawable image = skin.getDrawable(nume);
		image.setMinHeight(height);
		image.setMinWidth(width);
		return image;
	}

}
