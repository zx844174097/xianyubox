package com.mugui.Dui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.lang.reflect.Field;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class DTextField extends JTextField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9074513222366556397L;

	private String infoText = null;
	private boolean listener_bool = false;

	private KeyAdapter KeyListener = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			if (option == OPTION_KEY_LISTENER) {
				Field[] fields = KeyEvent.class.getFields();
				for (Field f : fields) {
					try {
						if (f.get(null) instanceof Integer)
							if (f.getInt(null) == e.getKeyCode() && f.getName().startsWith("VK_")) {
								((DTextField) e.getSource()).setText(f.getName().substring(3, f.getName().length()));
							}
					} catch (IllegalArgumentException e1) {
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
					}
				}
			}
			if (codeListener != null) {
				codeListener.callBack((DTextField) e.getSource());
			}
			document.setInsertBool(false);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			document.setInsertBool(true);
		}

	};
	private MouseAdapter mouseListener = new MouseAdapter() {
		@Override
		public void mouseReleased(java.awt.event.MouseEvent e) {
			((DTextField) e.getSource()).setText(e.getButton() + "");
			if (codeListener != null) {
				codeListener.callBack((DTextField) e.getSource());
			}
			document.setInsertBool(false);

		};

		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {
			document.setInsertBool(true);
		};

	};

	private CodeListener codeListener = null;

	public void setKeyCodeListener(boolean bool) {
		option = OPTION_KEY_LISTENER;
		setCodeListener(bool);
	}

	public void setCodeListener(boolean bool) {
		listener_bool = bool;
		if (listener_bool) {
			removeMouseListener(mouseListener);
			addKeyListener(KeyListener);
		} else {
			removeKeyListener(KeyListener);
		}
	}

	public void addCodeListener(CodeListener codeListener) {
		this.codeListener = codeListener;
		if (!listener_bool)
			setCodeListener(true);

	}

	public void addKeyCodeListener(CodeListener keyCodeListener) {
		option = OPTION_KEY_LISTENER;
		addCodeListener(keyCodeListener);
	}

	public void setMouseCodeListener(boolean bool) {
		option = OPTION_MOUSE_LISTENER;
		listener_bool = bool;
		if (listener_bool) {
			removeKeyListener(KeyListener);
			addMouseListener(mouseListener);
		} else {
			removeMouseListener(mouseListener);
		}
	}

	public void addMouseCodeListener(CodeListener keyCodeListener) {
		option = OPTION_MOUSE_LISTENER;
		this.codeListener = keyCodeListener;
		if (!listener_bool)
			setMouseCodeListener(true);
	}

	public static interface CodeListener {
		public void callBack(DTextField dTextField);
	}

	public DTextField(int len) {
		this(len, null);
	}

	public DTextField(String text) {
		this(32, text);
	}

	public String getText() {
		String text = super.getText();
		if (infoText == null)
			return text;
		if (infoText.equals(text))
			return "";
		return text;
	}

	private DDocument document = null;
	public static final int OPTION_INT = DDocument.OPTION_INT;
	public static final int OPTION_KEY_LISTENER = 88;
	public static final int OPTION_MOUSE_LISTENER = 89;
	public int option = 0;

	public void setOption(int option) {
		this.option = option;
		switch (option) {
		case OPTION_KEY_LISTENER:
			setKeyCodeListener(true);
			break;
		case OPTION_MOUSE_LISTENER:
			setMouseCodeListener(true);
			break;
		default:
			document.setOption(option);
			break;
		}
	}

	private final static String[] lstr = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

	public DTextField(int len, String text) {
		super();
		this.setDocument(document = new DDocument(len));
		setText(text);
		this.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				Font font = DTextField.this.getFont();
				String text = DTextField.this.getText();
				if (font.canDisplayUpTo(text) != -1) {
					for (String str : lstr) {
						Font f = new Font(str, font.getStyle(), font.getSize());
						if (f.canDisplayUpTo(text) == -1) {
							DTextField.this.setFont(f);

							return;
						}
					}
				}
			}
		});
	}

	@Override
	public void setText(String text) {
		if (text == null || text.length() == 0)
			return;
		Font font = DTextField.this.getFont();
		if (font.canDisplayUpTo(text) != -1) {
			for (String str : lstr) {
				Font f = new Font(str, font.getStyle(), font.getSize());
				if (f.canDisplayUpTo(text) == -1) {
					DTextField.this.setFont(f);
					super.setText(text); 
					return;
				}
			}
		}
		super.setText(text);

	}
}
