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

package genome.net;

import genome.config.Config;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author T. Takeuchi
 */
public class WebSocket extends WebSocketClient {

    static Logger logger = LoggerFactory.getLogger(WebSocket.class);

    // private static final String WS_HOST = "ws://127.0.0.1:5000/api/ws/connect";
    private static final String WS_HOST =
        String.format("ws://%s:5000/api/ws/connect", Config.getInstance().getConsoleHost());

    private DataSelectionListener listener;

    public WebSocket(DataSelectionListener listener) throws URISyntaxException {
        super(new URI(WS_HOST));
        this.listener = listener;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        logger.info("Open WebSocket connection: " + handshake.getHttpStatusMessage());
    }

    @Override
    public void onClose(int i, String str, boolean bool) {
        logger.info("Close WebSocket");
    }

    @Override
    public void onMessage(String message) {
        logger.debug("WebSocket Message: " + message);
        listener.finishedDataSelection(message);
    }

    @Override
    public void onError(Exception e) {
        logger.warn(e.getMessage());
        e.printStackTrace();
    }
}
