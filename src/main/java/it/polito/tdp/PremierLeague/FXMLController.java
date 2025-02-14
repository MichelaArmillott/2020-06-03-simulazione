/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Battuti;
import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	double media=0.0;
    	String sMedia=txtGoals.getText();
    	try {media=Double.parseDouble(sMedia);}catch(NumberFormatException e) {
    		txtResult.setText("deve essre un numero");
    		return;
    	}
    	model.creaGrafo(media);
    	txtResult.appendText("grafo creato \n");
    	txtResult.appendText("i vertici sono  "+model.getNVertici()+"\n");
    	txtResult.appendText("gli archi sono  "+model.getNArchi()+"\n");

    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	txtResult.clear();
    	

    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	txtResult.clear();
    	 if(model.getGrafo()==null) {
     		txtResult.setText("devi prima creare un grafo");
     		return;
     	}
    	 Player best=model.best();
    	 if(best==null) {
    		 txtResult.appendText("non ci sono giocatori");
    		 return;
    	 }
    	 txtResult.appendText("TOP PLAYER: "+best.toString()+"\n\n");
    	 List<Battuti>stampa=model.battuti(best);
    	 if(stampa.isEmpty()) {
    		 txtResult.appendText("non ha battuto nessuno");
    		 return;
    	 }
    	for(Battuti b:stampa)
    		txtResult.appendText(b.toString()+"\n");

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
