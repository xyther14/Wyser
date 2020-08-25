package sample;

import animatefx.animation.*;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.*;
import java.time.Duration;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.scene.control.Tooltip;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Controller implements Initializable {

    String htlink = "http://";

    @FXML
    private TextField searchBox;
    @FXML
    private WebView webView;
    @FXML
    private Label lbl_G, lbl_B,lbl_W,lbl_Y,lbl_D,main_lbl,lbl_T;
    @FXML
    private ImageView google,bing,wikipedia,youtube,duckduckgo,imageView,twitter,menuBar,btnClose;
    @FXML
    private Button goBack,wyser;
    @FXML
    private Pane wiserPaneSites,mainPane,webPane,errorPane,founderPane;

    WebEngine engine;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        engine = webView.getEngine();
//        engine.load(htlink+"www.google.com");


    }

    public void goTo(ActionEvent event) throws IOException {
        if(searchBox.getText().startsWith("http://".toLowerCase()) || searchBox.getText().startsWith("http://".toUpperCase())) {
                resetValues();
                engine.load(searchBox.getText());

        }
        else if(checkDomain()){
            resetValues();
            engine.load(htlink+searchBox.getText());

        }else {
            resetHighlightLabel();
            webView.setVisible(false);
            int range = (int)(Math.random() * 3) + 1;
            String file = "strange"+range+".gif";
            Image image = new Image(file);
            imageView.setImage(image);
            if(file.equals("strange1.gif"))
            main_lbl.setText("Doctor Strange did his magic! Check the side pane for results.");
            else if(file.equals("strange2.gif"))
                main_lbl.setText("I found the search results Watson! Check the left pane.");
            else
                main_lbl.setText("Search results so good even Hermoine studying from it.");
            new ZoomIn(mainPane).play();
            mainPane.toFront();
            goBack.setDisable(true);
            new Tada(wiserPaneSites).play();
            wiserPaneSites.setVisible(true);
        }
    }





    public Boolean checkDomain() throws FileNotFoundException {
        Scanner inputFile = new Scanner(new File("E:\\Projects\\Python\\Wiser\\src\\domains.txt"));
        while (inputFile.hasNext()) {
            String unknown = inputFile.next();
            if (searchBox.getText().endsWith(unknown.toLowerCase()) || searchBox.getText().toUpperCase().endsWith(unknown) ) {
                return true;
            }
        }
        inputFile.close();
        return false;
    }



    public String goBack()
    {
        final WebHistory history=engine.getHistory();
        ObservableList<WebHistory.Entry> entryList=history.getEntries();
        int currentIndex=history.getCurrentIndex();
//    Out("currentIndex = "+currentIndex);
//    Out(entryList.toString().replace("],","]\n"));

        Platform.runLater(new Runnable() { public void run() { history.go(-1); } });
        searchBox.setText(entryList.get(currentIndex>0?currentIndex-1:currentIndex).getUrl());
        return entryList.get(currentIndex>0?currentIndex-1:currentIndex).getUrl();
    }

    public void reload(){
        engine.reload();
    }

    public void handleMouseEvent(javafx.scene.input.MouseEvent event) {
        if(event.getSource()==bing){
            wiserPane(lbl_B);
            engine.load("https://www.bing.com/search?q="+searchBox.getText());
        }
        if(event.getSource()==google){
            wiserPane(lbl_G);
            engine.load("https://www.google.com/search?q="+searchBox.getText());
        }
        if(event.getSource()==wikipedia){
            wiserPane(lbl_W);
            engine.load("https://en.wikipedia.org/wiki/"+searchBox.getText());
        }
        if(event.getSource()==duckduckgo){
            wiserPane(lbl_D);
            engine.load("https://duckduckgo.com/?q="+searchBox.getText());
        }
        if(event.getSource()==youtube){
            wiserPane(lbl_Y);
            engine.load("https://www.youtube.com/results?search_query="+searchBox.getText());
        }
        if(event.getSource()==twitter){
            wiserPane(lbl_T);
            engine.load("https://twitter.com/search?q="+searchBox.getText());
        }
        if(event.getSource()==menuBar){
            if(founderPane.isVisible()){
                new ZoomOut(founderPane).play();
                founderPane.setVisible(false);
            }else {
                new ZoomIn(founderPane).play();
                founderPane.setVisible(true);
            }
        }
        if(event.getSource()==btnClose){
            founderPane.setVisible(false);
        }
    }

    public void resetHighlightLabel(){
        lbl_B.setVisible(false);
        lbl_G.setVisible(false);
        lbl_Y.setVisible(false);
        lbl_D.setVisible(false);
        lbl_W.setVisible(false);

    }

    public void wiserPane(Label label){
        new Wobble(webPane).play();
        webPane.toFront();
        webView.setVisible(true);
        Label lb[] = {lbl_Y,lbl_D,lbl_W,lbl_G,lbl_B,lbl_T};
        for(int i=0;i<lb.length;i++) {
            if ((label.toString().equals(lb[i].toString()))) {
                lb[i].setVisible(true);
            }else {
                lb[i].setVisible(false);
            }
        }
        }

        public void wiserSearch() throws IOException {
            String s = "https://www.google.com/search?q="+searchBox.getText();
            Document doc = Jsoup.connect(s).userAgent("Mozilla/17.0").get();
            Element results = doc.selectFirst("div.kCrYT > a");
//            System.out.println(results);
            String e =results.attr("href");
            System.out.println(e);
            if(e.contains("%"))
                e = e.substring(7,e.indexOf("%"));
            else
            e = e.substring(7,e.indexOf("&"));
            System.out.println(e);
             if(e.startsWith("https://www.youtube.com/")) {
                 e = "https://www.youtube.com/results?search_query=" + searchBox.getText();
             }


            wiserPaneSites.setVisible(false);
            engine.load(e);
            webView.setVisible(true);
            new Wobble(webPane).play();
            webPane.toFront();


        }





    public void resetValues(){
        webView.setVisible(true);
        webPane.toFront();
        wiserPaneSites.setVisible(false);
        goBack.setDisable(false);
    }

}
