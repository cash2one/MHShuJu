package com.ai.eduportal.remote;

import java.io.File;

public interface RemoteHttpCallerI {

	public abstract String getResponse(String reqstr);
	String getResponse1(String reqstr);
	
	public String getResponse(String reqstr,String fileName, File file);
}