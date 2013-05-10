/*
 *   ngsv
 *   https://github.com/xcoo/ngsv
 *   Copyright (C) 2012, Xcoo, Inc.
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

import java.io.File;
import java.io.IOException;

import org.ini4j.Wini;
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

    private static Config instance = new Config();

    private Wini ini;

    private Config() {}

    public static Config getInstance() {
        return instance;
    }

    public void load(String path) {
        try {
            ini = new Wini(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public String getDatabaseHost() {
        return ini.get("database", "host");
    }

    public String getDatabaseName() {
        return ini.get("database", "database");
    }

    public String getDatabaseUser() {
        return ini.get("database", "user");
    }

    public String getDatabasePassword() {
        return ini.get("database", "password");
    }

    public String getConsoleHost() {
        return ini.get("console", "host");
    }
}
