package com.ai.eduportal.interceptor;

import java.io.File;
import java.util.Map;

import com.ai.frame.bean.InputObject;
import com.ai.frame.util.ControlConstants;
import com.ai.frame.web.interceptor.HttpRequestWrapper;
import com.ai.frame.web.interceptor.RequestInterceptor;
import com.ai.frame.web.interceptor.paramProcess.AbstractParamProcess;
import com.ai.frame.web.interceptor.paramProcess.ParamProcessResult;
import com.ai.frame.web.xml.bean.Parameter;
import common.ai.tools.StringUtil;

public class FileParamProcess extends AbstractParamProcess {

	public FileParamProcess(InputObject inputObject,
			HttpRequestWrapper request, RequestInterceptor reqInterceptor) {
		super(inputObject, request, reqInterceptor);
	}

	public ParamProcessResult excute(Parameter param) {
		Map<String, Object> filepmVals = request.getParameters();
		
		ParamProcessResult result = paramProcessResult(inputObject, filepmVals,
				param.getKey(), param.getToKey());
		result.getInputObject().setMultiParams(true);

		return result;
	}

	private ParamProcessResult paramProcessResult(InputObject inputObject,
			Map<String, Object> filepmVals, String key, String toKey) {
		ParamProcessResult result = new ParamProcessResult(inputObject);

		File[] fileVals = (File[]) filepmVals.get(key);
		if (fileVals != null) {
			int index = 0;
			String[] fileNameVals = (String[]) filepmVals.get(key
					+ ControlConstants.FILE.FILENAME);
			String validateTypes = reqInterceptor
					.getValueFromProperties(ControlConstants.FILE.FILE_TYPEFILTER_NAME);
			
			for (File file : fileVals) {
				String fileNameVal = key;
				if (!StringUtil.isEmpty(toKey)) {
					fileNameVal = toKey;
				}
				String fileTypeVal = getFileType(fileNameVals[index]);
				if (validateFileType(validateTypes, fileTypeVal)) {
					
					InputObjectAdpter inputObjectAdp = new InputObjectAdpter(inputObject);
					inputObjectAdp.addFiles(key, toKey, fileNameVal, fileTypeVal,
							file);
					
				} else {
					result.setRtnCode(ParamProcessResult.RTN_ERROR);
					result.setRtnMsg("file type [" + fileTypeVal
							+ "] does not allow");
					return result;
				}
				index++;
			}
		}
		result.setRtnCode(ParamProcessResult.RTN_SUCESS);
		return result;
	}

	private String getFileType(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index != -1)
			return fileName.substring(index);
		return "";
	}

	private boolean validateFileType(String validateTypes, String fileTypeVal) {
		if (StringUtil.isEmpty(validateTypes)) {
			return true;
		} else {
			if (validateTypes.indexOf(fileTypeVal) > -1) {
				return true;
			} else {
				return false;
			}
		}
	}
}
