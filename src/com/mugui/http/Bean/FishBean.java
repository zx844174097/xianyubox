package com.mugui.http.Bean;

import java.awt.image.BufferedImage;

public class FishBean {
	private String fish_name;
	private BufferedImage fish_img;
	private BufferedImage fish_name_img;
	private long fish_price;

	public String getFish_name() {
		return fish_name;
	}

	public void setFish_name(String fish_name) {
		this.fish_name = fish_name;
	}

	public BufferedImage getFish_img() {
		return fish_img;
	}

	public void setFish_img(BufferedImage fish_img) {
		this.fish_img = fish_img;
	}

	public BufferedImage getFish_name_img() {
		return fish_name_img;
	}

	public void setFish_name_img(BufferedImage fish_name_img) {
		this.fish_name_img = fish_name_img;
	}

	public long getFish_price() {
		return fish_price;
	}

	public void setFish_price(long bold_time) {
		this.fish_price = bold_time;
	}

}
