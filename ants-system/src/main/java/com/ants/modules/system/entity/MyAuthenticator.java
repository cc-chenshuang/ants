package com.ants.modules.system.entity;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
public class MyAuthenticator extends Authenticator {
	private String userName;
	private String password;
	public MyAuthenticator(){

	}

	public MyAuthenticator(String userName, String password){
		this.userName = userName;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication(){
		return new PasswordAuthentication(userName, password);
	}
}
