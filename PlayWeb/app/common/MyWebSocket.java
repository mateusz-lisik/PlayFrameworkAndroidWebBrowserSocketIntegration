package common;

import play.Logger;
import play.libs.F;
import play.mvc.WebSocket;

public class MyWebSocket extends WebSocket<String> {
    public SocketClientType type;
    public WebSocket.In<String> in;
    public WebSocket.Out<String> out;
    private MyWebSocket instance;

    // Called when the Websocket Handshake is done.
    public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
        this.in = in;
        this.out = out;
        this.instance = this;

        ClientsList.getInstance().add(this);
        // For each event received on the socket,
        in.onMessage(new F.Callback<String>() {
            public void invoke(String event) {
                if (type == null) {
                    if (event.contentEquals("Iam an Android")) {
                        type = SocketClientType.MOBILE;
                    } else {
                        type = SocketClientType.DESKTOP;
                    }
                }

                Logger.info("Sending Message");
                if (event.contentEquals("next")) {
                    ClientsList.sendMessageToEveryClientInGroup(SocketClientType.DESKTOP, "next");
                }
                if (event.contentEquals("prev")) {
                    ClientsList.sendMessageToEveryClientInGroup(SocketClientType.DESKTOP, "prev");
                }
                if (event.contentEquals("love")) {
                    ClientsList.sendMessageToEveryClientInGroup(SocketClientType.DESKTOP, "love");
                }

                Logger.debug(String.format("Items count: %d", ClientsList.getInstance().list.size()));
                Logger.info(event);

            }
        });

        // When the socket is closed.
        in.onClose(new F.Callback0() {
            public void invoke() {
                Boolean wasRemoved = ClientsList.getInstance().list.remove(instance);
                Logger.info("Disconnected");

            }
        });
    }
}
