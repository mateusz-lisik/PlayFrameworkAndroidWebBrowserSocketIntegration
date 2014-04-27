package controllers;

import common.MyWebSocket;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }

    public static WebSocket<String> socket() {
        return new MyWebSocket();
    }

}
