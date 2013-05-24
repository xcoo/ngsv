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

package genome.view.updater;

import genome.db.SQLLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import casmi.Mouse;
import casmi.graphics.element.Text;

/**
 * @author T. Takeuchi
 */
public class Updater<T extends UpdateThread> {

    static Logger logger = LoggerFactory.getLogger(Updater.class);

    private final Class<T> cls;

    protected final SQLLoader sqlLoader;
    protected final Text annotationText;
    protected final Mouse mouse;

    private T currentThread;

    public Updater(Class<T> cls, SQLLoader sqlLoader, Text annotationText, Mouse mouse) {
        this.cls = cls;
        this.sqlLoader = sqlLoader;
        this.annotationText = annotationText;
        this.mouse = mouse;
    }

    public void start() {
        if (currentThread != null && currentThread.isAlive())
            stop();

        try {
            currentThread = cls.getConstructor(this.getClass()).newInstance(this);
            currentThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public void stop() {
        if (currentThread != null) {
            currentThread.setStopFlag(true);
        }
    }

    public SQLLoader getSqlLoader() {
        return sqlLoader;
    }

    public Text getAnnotationText() {
        return annotationText;
    }

    public Mouse getMouse() {
        return mouse;
    }
}
