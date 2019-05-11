package com.mugui.Dui;
import javax.swing.*;

public class DPaneltext extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -517638966593301846L;
	private String s ;
	private DTextArea dta;
	JLabel jl ;
	JScrollPane jsp=null;
	public DPaneltext(String s,int wei,int hei)
	{
		this.s=s;
		jl=new JLabel(s);
		jl.setBounds(0,0,wei,25);
		this.add(jl);
		dta=new DTextArea();
		jsp=new JScrollPane(dta);
		jsp.setBounds(0,28,wei,hei/6-35);
		this.add(jsp);
		this.setLayout(null);
	}
	public String getS() {
		return s+"\r\n"+dta.getText()+"\r\n";
	}
	public void setS(String s) {
		this.s = s;
	}
	public DTextArea getDta() {
		return dta;
	}
	public void setDta(DTextArea dta) {
		this.dta = dta;
	}
}
