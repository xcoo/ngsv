/*
 *   ngs View
 *   http://casmi.github.com/
 *   Copyright (C) 2011-2012, Xcoo, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package genome.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration class.
 * <p>
 * This class is singleton.
 * It reads a configuration file (e.g. config.properties).
 * <p>
 * Usage:
 * <code>
 * Config.getInstance().getHost()
 * </code>
 * 
 * @author T. Takeuchi
 */
public class Config {

    static Logger logger = LoggerFactory.getLogger(Config.class);
    
    private static final String CONFIG_FILENAME = "config.properties";
    
    private static final String DB_HOST_KEY     = "db.host";
    private static final String DB_DATABASE_KEY = "db.database";
    private static final String DB_USER_KEY     = "db.user";
    private static final String DB_PASSWORD_KEY = "db.password";
  
    private static final String CYTOBAND_DATA_URL_KEY = "cytoband.data_url";
    private static final String GENE_DATA_URL_KEY = "gene.data_url";
      
    private static Config instance = new Config();
    
    private Properties properties = new Properties();
    
    private Config() {
        InputStream is = Config.class.getResourceAsStream(CONFIG_FILENAME);
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
    
    public static Config getInstance() {
        return instance;
    }
    
    public String getHost() {
        return properties.getProperty(DB_HOST_KEY);
    }
    
    public String getDatabase() {
        return properties.getProperty(DB_DATABASE_KEY);
    }
    
    public String getUser() {
        return properties.getProperty(DB_USER_KEY);
    }
    
    public String getPassword() {
        return properties.getProperty(DB_PASSWORD_KEY);
    }
    
    public String getCytobandDataURL() {
        return properties.getProperty(CYTOBAND_DATA_URL_KEY);
    }
    
    public String getGeneDataURL() {
        return properties.getProperty(GENE_DATA_URL_KEY);
    }
}
