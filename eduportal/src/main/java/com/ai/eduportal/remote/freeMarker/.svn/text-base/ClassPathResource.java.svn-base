package com.ai.eduportal.remote.freeMarker;

import java.io.Closeable;
import java.io.InputStream;

public class ClassPathResource implements Resource{
	private String path;
    private ClassLoader classLoader;
    public ClassPathResource(String path) {
        this(path, (ClassLoader) null);
    }
    public String getFilePath(){
    	return path;
    }
    public ClassPathResource(String path, ClassLoader classLoader) {
        if (path != null && path.length() > 0) {
            path = path.replace("\\", "/");
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            this.path = path;
            ClassLoader cl = null;
            try {
                cl = Thread.currentThread().getContextClassLoader();
            } catch (Throwable ex) {
            }
            if (cl == null) {
                cl = Resource.class.getClassLoader();
            }
            this.classLoader = (classLoader != null ? classLoader : cl);
        }

    }
    public InputStream getInputStream() {
        InputStream in = null;
        try {
            in = this.classLoader.getResourceAsStream(this.path);
        } catch (Throwable ex) {
            in = null;
            System.err.println("read config file["+this.path+"] error:"+ex.getMessage());
        }

        return in;
    }
    
    public void closeCloseable(Closeable closeable){
    	if(closeable!=null){
    		try {
				closeable.close();
			} catch (Exception e) {
				System.err.println("close error:"+e.getMessage());
			}
    	}
    }
}
