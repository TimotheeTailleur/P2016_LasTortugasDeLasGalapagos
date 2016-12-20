package fr.tse.info4.project.model.datarecovery;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


/**
 * 
 * Handles end user authentication using Oauth2
 *
 */
@SuppressWarnings("restriction")
public class Authenticate extends Application {

    static final String APP_ID = "8456";
    static final String REDIRECT_URL = "https://stackexchange.com/oauth/login_success";
    static final String RESPONSE_TYPE = "token";
    static final String SCOPE = "no_expiry";

    private Scene scene;
    
    public static String accessToken;
        
  
    public void start(final Stage stage) throws Exception {
        final String url = "https://stackexchange.com/oauth/dialog?response_type="+RESPONSE_TYPE+
        		"&client_id="+APP_ID+"&redirect_uri="+REDIRECT_URL+"&scope="+SCOPE;
        BorderPane borderPane = new BorderPane();

        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();

        webEngine.load(url);
        borderPane.setCenter(browser);

        webEngine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
            public void handle(WebEvent<String> event) {
                if (event.getSource() instanceof WebEngine) {
                    WebEngine we = (WebEngine) event.getSource();
                    String location = we.getLocation();
                    if (location.startsWith(REDIRECT_URL) && location.contains("access_token")) {
                        try {
                            URL url = new URL(location);
                            String[] params = url.getRef().split("&");
                            Map<String, String> map = new HashMap<String, String>();
                            for (String param : params) {
                                String name = param.split("=")[0];
                                String value = param.split("=")[1];
                                map.put(name, value);
                            }
                            accessToken = map.get("access_token");
                            stage.hide();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        stage.setTitle("Las Tortugas - Projet informatique");
        scene = new Scene(borderPane, 750, 500);
        stage.setScene(scene);
        stage.show();
        
    }
    
    
   /**
    * 
    * Open a window asking the user his username and password.
    * Uses JavaFx library
    * @return access token
    */
   public static String getAcessToken(){
	   String[] args = {};
	   launch(args);
	   return accessToken;
	   
   }
   
   public static void main(String[] args) {
	  System.out.println(getAcessToken());
}
   

}