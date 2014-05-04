package demo.matteprl.AndroidClient;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class MyActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    public WebSocketClient ws;

    TextView status;
    Button forwardButton;
    Button backwardButton;
    Button loveButton;

    public MyActivity() {
        try {
            ws = new WebSocketClient(new URI("ws://localhost:9000/socket")) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {

                }

                @Override
                public void onMessage(String s) {

                }

                @Override
                public void onClose(int i, String s, boolean b) {

                }

                @Override
                public void onError(Exception e) {

                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        java.lang.System.setProperty("java.net.preferIPv6Addresses", "false");
        java.lang.System.setProperty("java.net.preferIPv4Stack", "true");

        status = (TextView) findViewById(R.id.textView2);
        forwardButton = (Button) findViewById(R.id.button);
        backwardButton = (Button) findViewById(R.id.button2);
        loveButton = (Button) findViewById(R.id.button3);
        forwardButton.setOnClickListener(this);
        backwardButton.setOnClickListener(this);
        loveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        Log.i("MyActivity", "button id: ".concat(String.valueOf(v.getId())));
        CheckConnection cc = new CheckConnection() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                switch (v.getId()) {
                    case R.id.button: // Forward
                        ws.send("next");
                        break;
                    case R.id.button2: // Backward
                        ws.send("prev");
                        break;
                    case R.id.button3: // "Love" button
                        ws.send("love");
                        break;
                    default:
                        Log.i("MyActivity", "Unknown button id: ".concat(String.valueOf(v.getId())));
                }
            }
        };
        cc.execute();
    }

    private class CheckConnection extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            status.setTextColor(Color.YELLOW);
            status.setText("connecting...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            status.setTextColor(Color.GREEN);
            status.setText("connected");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (ws.getReadyState() != WebSocket.READYSTATE.OPEN) {
                    ws.connectBlocking();
                    ws.send("Iam an Android");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
