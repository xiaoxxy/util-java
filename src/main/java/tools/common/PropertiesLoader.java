package tools.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesLoader {
	
	 private static Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

	private PropertiesLoader() {
    }

    private static Properties env = new Properties();

    public static void init(String... filePaths) throws IOException {

        logger.info("----------init config");

        for (String filePath : filePaths) {
            if(null==filePath){
                continue;
            }
            logger.info("---------read file from {}",filePath);

            File file = new File(filePath);
            if(file.exists()){
                InputStream in = null;
                try {
                    in = new FileInputStream(filePath);
                    env.load(in);
                }catch (IOException e){
                    throw e;
                }finally {
                    if(null!=in){
                        in.close();
                    }
                }
            }
        }
    }

    public static void initClassPath(String... classFilePaths) throws IOException {

        logger.info("----------init classpath config");

        for (String filePath : classFilePaths) {
            if(null==filePath){
                continue;
            }
            logger.info("---------read file from {}",filePath);

            InputStream in = null;
            try {
                in = PropertiesLoader.class.getClassLoader().getResourceAsStream(filePath);
                if(null!=in){
                    env.load(in);
                }
            }catch (IOException e){
                throw e;
            }finally {
                if(null!=in){
                    in.close();
                }
            }
        }
    }

    public static String getProperty(String propName, String defaultValue) {
        return env.getProperty(propName, defaultValue);
    }

    public static String getProperty(String propName) {
        return env.getProperty(propName);
    }
    
}
