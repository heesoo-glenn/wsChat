package vertx.my.wsChat;

import java.util.HashSet;
import java.util.Set;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.Router;


public class WebSocketServer extends AbstractVerticle{

	@Override
	public void start(){
		
		Vertx vertx = Vertx.vertx();
    	HttpServer server = vertx.createHttpServer();
    	
    	Router router = Router.router(vertx);
    	router.route("/").handler(routingContext -> {
    		routingContext.response().putHeader("content-type", "text/html")
    						.sendFile("vertx/my/wsChat/test.html");
    	});
    	server.requestHandler(router::accept);
    	
    	Set<ServerWebSocket> sessions = new HashSet<ServerWebSocket>();
    	
    	server.websocketHandler( ws ->{
    		if(ws.path().equals("/chatWs")){
        		sessions.add(ws);
        		ws.handler(data ->{
        			String msg = data.toString();
    				sessions.forEach(wsSession ->{
    					wsSession.writeTextMessage(msg);
    				});
        		});
    		}else{
    			ws.reject();
    		}
    	});

    	server.listen(8888);
	}

}
