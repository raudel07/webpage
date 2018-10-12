package sample.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;
import sample.Constantes.Configs;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static sample.Constantes.Configs.*;

public class Controller extends Application{
    private Stage stage;
    @FXML HBox paneSote;
    @FXML TextArea txtConsola;

    CodeArea codeArea = new CodeArea();
    @FXML protected void initialize(){
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.replaceText(0, 0, sampleCode);
        codeArea.setPrefSize(800,500);
        Subscription cleanupWhenNoLongerNeedIt = codeArea
                .multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));
        HBox.setHgrow(codeArea, Priority.ALWAYS);
        paneSote.getChildren().add(codeArea);
    }//llave load
    public void evtSalir(ActionEvent evento){
        System.exit(0);
    }
    public void evtAbrir(ActionEvent evento){
        FileChooser of=new FileChooser();
        of.setTitle("Abrir Archivo Comilador");
        FileChooser.ExtensionFilter Filtro=new FileChooser.ExtensionFilter("Archivos.compilador","*.compilador");
        of.getExtensionFilters().add(Filtro);
        File file=of.showOpenDialog(stage);
    }
    @Override
    public void start (Stage primaryStage) throws Exception {
        this.stage=primaryStage;
    }
    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
    public void Ejecutar(ActionEvent event){
        compilar();
    }
    public void compilar(){
        txtConsola.setText("");
        long tInicial=System.currentTimeMillis();

        String texto=codeArea.getText();
        String[] renglones=texto.split("\\n");


        for(int x=0;x<renglones.length;x++){
            boolean bandera=false;
            if (!renglones[x].trim().equals("")){
                for(int y=0;y< Configs.EXPRECIONES.length && bandera==false;y++){
                   Pattern patron=Pattern.compile(Configs.EXPRECIONES[y]);
                   Matcher matcher= patron.matcher(renglones[x]);
                   if(matcher.matches()) {
                       bandera = true;
                       // txtConsola.setText(txtConsola.getText()+"\n"+ "Error de sintaxys en la linea"+(x+1));
                   }
                }
                if(bandera==false){
                    txtConsola.setText(txtConsola.getText()+"\n"+ "Error de sintaxys en la linea"+(x+1));
                }
            }

        }
        long tFinal=System.currentTimeMillis()-tInicial;
        txtConsola.setText(txtConsola.getText()+"\n"+"Compilador en: "+tFinal+" milisegundos");
    }
}
