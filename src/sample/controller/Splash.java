package sample.controller;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Splash extends Preloader {
    private Label lblProgress;
    private ProgressBar barra;
    private Stage stage;//Este es el marco de la ventana
    private Scene scene;//Esta es la esena de la ventana

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        StateChangeNotification.Type type=info.getType();//Este es el que me indica en que estado se encuantra mi splash, si ya empeso o si ya se termino etc...
        switch (type){
            case BEFORE_START:{
                stage.hide();//Esto es para que se oculte la ventana prinsipal
                break;
            }
        }//llave switch
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {//Esta es la funcion que se acutualizara segun lo vallamos corriendo
        if(info instanceof ProgressNotification){
            lblProgress.setText(((ProgressNotification)info).getProgress()+"%");
            barra.setProgress(((ProgressNotification)info).getProgress()/100);
        }//llave if
    }

    @Override
    public void init() throws Exception {
        Platform.runLater(new Runnable() {//Esta es la creacion del hilo
            @Override
            public void run() {
                Parent root2;
                try{
                    root2 = FXMLLoader.load(getClass().getResource("../views/splash.fxml"));
                    scene=new Scene(root2,600,400);
                }catch (IOException e){
                }//llave try catch
            }
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
     this.stage=stage;
     this.stage.initStyle(StageStyle.UNDECORATED);
     this.stage.setScene(scene);
     this.stage.show();
     lblProgress=(Label)scene.lookup("#lblprog");
     barra=(ProgressBar)scene.lookup("#prog");
    }
}
