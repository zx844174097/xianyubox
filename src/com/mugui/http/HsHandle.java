package com.mugui.http;

import com.mugui.http.pack.Bag;
import com.mugui.http.pack.TcpBag;
import com.mugui.http.pack.UdpBag;
import com.mugui.http.tcp.TcpSocketUserBean;
import com.mugui.model.HsListenerModel;
import com.mugui.model.TCPModel;
import com.mugui.model.UIModel;
import com.mugui.tool.Other; 
import com.mugui.ui.DataSave;

public class HsHandle {
	public static void manage(Bag bag, TcpSocketUserBean tcpSocket) {
		TcpBag accpet = (TcpBag) bag;
		if (accpet == null)
			return;
		DataSave.tcpSocket = tcpSocket;
		
		com.mugui.http.DataSave.reStartOutTime();
		if (!Other.isInteger(accpet.getBag_id())) {
			if (accpet.getBag_id().equals(TcpBag.USER_CMD_INFO)) {
				HsListenerModel.cmdInfo(accpet, tcpSocket);
				return;
			} else if (accpet.getBag_id().equals(TcpBag.GET_USER_WINDOWS)) {
				HsListenerModel.getUserWindows(accpet, tcpSocket);
				return;
			} else if (accpet.getBag_id().equals(TcpBag.STOP_USER_WINDOW)) {
				HsListenerModel.stopUserWindow(accpet, tcpSocket);
				return;
			} else if (accpet.getBag_id().equals(TcpBag.SELECT_APP_ID)) {
				TCPModel.selectAppID(accpet, tcpSocket);
				return;
			} else if (accpet.getBag_id().equals(UdpBag.FILE_)) {
				TCPModel.file_(accpet, tcpSocket);
				return;
			} else if (accpet.getBag_id().equals("mouse_info")) {
				HsListenerModel.mouseInfo(accpet, tcpSocket);
				return;
			} else if (accpet.getBag_id().equals(Bag.KEY_INFO)) {
				HsListenerModel.keyInfo(accpet, tcpSocket);
				return;
			}
		} else {
			switch (Integer.parseInt(accpet.getBag_id())) {
			case TcpBag.ERROR:
				TCPModel.error(accpet); 
				break;
			case TcpBag.LOGIN:
				TCPModel.login(accpet, tcpSocket);
				break;
			case TcpBag.REG:
				TCPModel.reg(accpet, tcpSocket);
				break;
			case TcpBag.REG_CODE:
				TCPModel.regCode(accpet, tcpSocket);
				break;
			case TcpBag.OUT_LOGIN:
				TCPModel.outLogin(accpet, tcpSocket);
				break;
			case TcpBag.DY_CODE:
				// TCPModel.dyCode(accpet, tcpSocket);
				break;
			case TcpBag.GET_DYTIME:
				TCPModel.getDyTime(accpet, tcpSocket);
				break;
			case TcpBag.GET_QPTIME:
				TCPModel.getQpTime(accpet, tcpSocket);
				break;
			case TcpBag.GET_DSTIME:
				TCPModel.getDsTime(accpet, tcpSocket);
				break;
			case TcpBag.GET_JGTIME:
				TCPModel.getJgTime(accpet, tcpSocket);
				break;
			case TcpBag.GET_MYTIME:
				TCPModel.getMyTime(accpet, tcpSocket);
				break;
			case TcpBag.GET_LJTIME:
				TCPModel.getLjTime(accpet, tcpSocket);
				break;
			case TcpBag.LOGIN_SEND:
				UIModel.login(accpet);
				break;
			case TcpBag.REG_SEND:
				UIModel.reg(accpet);
				break;
			case TcpBag.REG_CODE_SEND:
				UIModel.reg_code(accpet);
				break;
			case TcpBag.SET_USER_NAME:
				TCPModel.setUserName(accpet);
				break;
			case TcpBag.SAVE_SNAKE_MARK:
				TCPModel.saveSnakeMark();
				break;
			case TcpBag.SNAKE_MARK_ALL:
				TCPModel.snakeMarkAll(accpet);
				break;
			case TcpBag.APP_OLD:
				TCPModel.appOld(accpet);
				break;
			case TcpBag.UPDATE_REG_CODE:
				TCPModel.updateRegCode(accpet);
				return;
			case TcpBag.UPDATE_USER_PAWD:
				TCPModel.updateUserPawd(accpet);
				return;
			case TcpBag.SEND_FISH_LINE_FEATURE:
				TCPModel.sendFishLineFeature(accpet);
				return;
			case TcpBag.SEND_ONE_FISH_LINE_FEATURE:
				TCPModel.sendOneFishLineFeature(accpet);
				return;
			case TcpBag.GET_LINE_ALL_YU_BODY:
				TCPModel.getLineAllYuBody(accpet);
				return;
			case Bag.SEND_ONE_BOLD_LINE:
				TCPModel.sendOneBoldLine(accpet);
				return;
			case Bag.GET_BOLD_LINE:
				TCPModel.getBoldLines(accpet);
				return;
			case Bag.SEND_BOSS_UPDATE_ONE:
				TCPModel.sendBossUpdateOne(accpet);
				return;
			case Bag.GET_ALL_BOSS_UPDATE_TIME:
				TCPModel.getAllBossUpdateTime(accpet);
				return;
			case Bag.APP_SEND_START_LISTENER_1:
				TCPModel.appSendStartListener1(accpet, tcpSocket);
				return;
			case Bag.STOP_MAN_LISTENER:
				TCPModel.stopManListener();
				return;
			case Bag.SEND_START_DY:
				UIModel.updateDyState(accpet.getBody(), true);
				return;
			case Bag.SEND_STOP_DY:
				UIModel.updateDyState(accpet.getBody(), false);
				return;
			case Bag.SEND_PC_SEND_INFO:
				TCPModel.sendPcSendInfo(accpet);
				return;
			case Bag.SEND_RECEIVE_CDK:
				System.out.println("SEND_RECEIVE_CDK:"+accpet.toString()); 
				UIModel.RecoveryCDK(accpet.getBody().toString(), DataSave.ds.cdk_name.getText());
				return ;
			case Bag.GET_DATA:
				TCPModel.getData(accpet);
			}
		}
	}

}
