package tools.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class PlatformProp implements Serializable {

    private static final long serialVersionUID = 2313107748344879601L;
    private static Logger logger = LoggerFactory.getLogger(PlatformProp.class);

    private PlatformProp() {
    }

    private static Properties env = new Properties();
    private static Set<String> fileHistorySet = new HashSet<>();

    public static void init(String... filePaths) throws IOException {

        logger.info("----------init config");
        for (String filePath : filePaths) {
            if (null == filePath) {
                continue;
            }
            if(fileHistorySet.contains(filePath)){
                return;
            }
            fileHistorySet.add(filePath);

            InputStream in = null;
            try {
                File file = new File(filePath);
                if(file.exists()){
                    in = new FileInputStream(filePath);
                }else {
                    in = PlatformProp.class.getClassLoader().getResourceAsStream(filePath);
                    if (null == in) {
                        in = PlatformProp.class.getResourceAsStream(filePath);
                    }
                }
                if(null!=in){
                    env.load(in);
                }else{
                    logger.error("-------file not found");
                }
            } catch (IOException e) {
                throw e;
            } finally {
                if (null != in) {
                    in.close();
                }
            }
        }
    }

    public static void initClassPath(String... classFilePaths) throws IOException {
        init(classFilePaths);
    }
    
    public static Properties getEnv() {
		return env;
	}

	public static Set<Object> keySet() {
        return env.keySet();
    }
    
    public static Set<Entry<Object, Object>> entrySet(){
    	return env.entrySet();
    }

    public static String getProperty(String propName, String defaultValue) {
        return env.getProperty(propName, defaultValue);
    }

    public static String getProperty(String propName) {
        return env.getProperty(propName);
    }

    public static void setProperty(String propName, String value) {
        env.setProperty(propName, value);
    }
}
