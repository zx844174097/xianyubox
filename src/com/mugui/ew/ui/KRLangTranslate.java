package com.mugui.ew.ui;

import com.mugui.Dui.DPanel;
import com.mugui.Dui.DTextArea;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;

import com.mugui.Dui.DrawImg;
import com.mugui.ew.EWUIHandel;
import com.mugui.tool.UiTool;
import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import com.mugui.Dui.DVerticalFlowLayout;
import java.awt.Font;

import com.mugui.DataSaveInterface;
import com.mugui.ModelInterface;
import com.mugui.Dui.DButton;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.awt.event.ActionEvent;

public class KRLangTranslate extends DPanel {

	public KRLangTranslate() {
		setBackground(null);
		setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));

		DrawImg drawImg = new DrawImg((DPanel) this);
		JScrollPane scrollPane_1 = new JScrollPane(drawImg);
		panel_2.add(scrollPane_1, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		panel_2.add(panel_1, BorderLayout.NORTH);
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		DButton button = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawImg.lastDraw();
			}
		});
		button.setFont(new Font("Dialog", Font.PLAIN, 13));
		button.setText("上一次");
		panel_1.add(button);

		DButton button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawImg.drawImg(new Tool().截取屏幕(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH, DataSave.SCREEN_HEIGHT));

			}
		});
		button_2.setFont(new Font("Dialog", Font.PLAIN, 13));
		button_2.setText("截取屏幕");
		panel_1.add(button_2);

		JTextArea textArea = new DTextArea();
		textArea.setFont(new Font("Dialog", Font.BOLD, 15));
		JTextArea textArea2 = new DTextArea();
		textArea2.setFont(new Font("Dialog", Font.BOLD, 16));
		DButton button_1 = new DButton((String) null, (Color) null);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage image = drawImg.nowDraw();
				if (image == null || image.getWidth() <= 5 || image.getHeight() <= 5)
					return;
				String text = (String) model.invokeFunction("ImgOCR_KOR", image);
				if (text == null)
					return;
				textArea.setText(text);
				text = (String) model.invokeFunction("RenderKOR", text);
				textArea2.setText(text);
			}
		});
		button_1.setFont(new Font("Dialog", Font.PLAIN, 13));
		button_1.setText("识别并翻译");
		panel_1.add(button_1);
		JPanel panel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(panel);

		add(scrollPane, BorderLayout.WEST);
		panel.setLayout(new DVerticalFlowLayout());
		JLabel label = new JLabel("识别的韩文:");
		label.setFont(new Font("宋体", Font.BOLD, 19));
		panel.add(label);
		panel.add(textArea);
		JLabel label_1 = new JLabel("译文：");
		label_1.setFont(new Font("宋体", Font.BOLD, 19));
		panel.add(label_1);
		panel.add(textArea2);
		textArea.setPreferredSize(new Dimension(400, 250));
		textArea2.setPreferredSize(new Dimension(400, 250));
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7694681310204776548L;
	private com.mugui.ew.DataSave datasave = null;
	private ModelInterface model = null;

	@Override
	public void init() {
		if (datasave == null)
			datasave = (com.mugui.ew.DataSave) EWUIHandel.datasave;
		if (model == null) {
			model = datasave.getModelManager().get("KRLangTranslateModel");
		}
	}

	@Override
	public void quit() {
	}

	@Override
	public void dataInit() {
	}

	@Override
	public void dataSave() {
	}

}
