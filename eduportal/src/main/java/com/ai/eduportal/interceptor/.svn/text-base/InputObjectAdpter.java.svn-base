package com.ai.eduportal.interceptor;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ai.frame.bean.InputObject;
import common.ai.tools.ClassUtil;
import common.ai.tools.StringUtil;

public class InputObjectAdpter {
	private InputObject inputObj;
	public InputObjectAdpter(InputObject inputObject){
		this.inputObj = inputObject;
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public File getFile(int index){
		Object fields = ClassUtil.getFieldVal(inputObj, "files");
		if(fields instanceof List){
			List<Map> files = (List<Map>)fields;
			if(index >= 0 && index < files.size()){
				Map upfile  = files.get(index);
				Iterator<Map.Entry> entries = upfile.entrySet().iterator();
				while(entries.hasNext()){
					Map.Entry entry = entries.next();
					
					Object uploadFile = entry.getValue();
					Object filePath   = ClassUtil.getFieldVal(uploadFile, "file");
					String strpath    = StringUtil.obj2Str(filePath);
					
					File file = new File(strpath);
					if(file.exists()){
						return file;
					}else{
						return null;
					}
					
				}
			}
		}
		
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addFiles(String paramKey,String toKey,String fileName,String fileType,File file){
		inputObj.addFiles(paramKey, toKey, fileName, fileType, null);
        
        String mapKey = paramKey;
        if(!StringUtil.isEmpty(toKey)){
            mapKey = toKey;
        }
        
        Object fields = ClassUtil.getFieldVal(inputObj, "files");
        if(fields instanceof List){
        	List<Map> fieldLs = (List<Map>)fields;
        	for(Map upfile :fieldLs){
        		if(upfile.containsKey(mapKey)){
        			Object mapfile = upfile.get(mapKey);
        			
        			try{
        				String filepath = file.getCanonicalPath();
        				ClassUtil.setFieldVal("file", mapfile, filepath);
        			}catch(Exception e){}
        		}
        		
        	}
        }
    }
}
