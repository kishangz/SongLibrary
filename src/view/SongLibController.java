// Eyob Tesfaye and Kishan Zalora

package view;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import app.Song;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SongLibController {
	
	@FXML private ListView<Song> listView;
	
	@FXML private Button OKButton;
    @FXML private Button cancelButton;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
	
	@FXML private TextField songField;
    @FXML private TextField artistField;
    @FXML private TextField albumField;
    @FXML private TextField yearField;

    @FXML private Label songLabel;
    @FXML private Label artistLabel;
    @FXML private Label albumLabel;
    @FXML private Label yearLabel;
    
    private final static String file = "SongList.txt";
    
    Map<String, Song> map = new HashMap<>();

	private ObservableList<Song> obsList = FXCollections.observableArrayList();    
	
	private Stage primaryStage;

	public void start(Stage primaryStage) throws IOException {                
		
		this.primaryStage = primaryStage;
		
		try {
			FileReader fRead = new FileReader(file);
			BufferedReader bRead = new BufferedReader(fRead);
			String line;

			while ((line = bRead.readLine()) != null){
				String [] breaking = line.split("\t");
				
				Song song = new Song(breaking[0], breaking[1], "", "");
				
				if(breaking.length > 2) {
					song.setAlbum(breaking[2]);
				}
				if(breaking.length > 3) {
					song.setYear(breaking[3]);
				}
							
				map.put(song.getKey(), song);
				sortList(song);
			}
			fRead.close();
			bRead.close();
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}

		listView.setItems(obsList);
		
		listView.getSelectionModel().select(0);
		
		Song selected = listView.getSelectionModel().getSelectedItem(); 		
		if(selected != null) {
			songLabel.setText(selected.getSong());
			artistLabel.setText(selected.getArtist());
			albumLabel.setText(selected.getAlbum());
			yearLabel.setText(selected.getYear());
		}
		
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {
			@Override
			public void changed(ObservableValue<? extends Song> obs, Song oldValue, Song newValue) {
				if(newValue != null) {
					songLabel.setText(newValue.getSong());
					artistLabel.setText(newValue.getArtist());
					albumLabel.setText(newValue.getAlbum());
					yearLabel.setText(newValue.getYear());
				} else {
					songLabel.setText("");
					artistLabel.setText("");
					albumLabel.setText("");
					yearLabel.setText("");
					
					deleteButton.setDisable(true);
					editButton.setDisable(true);
				}
			}
		});		

	}
	
	@FXML
	private void add(ActionEvent ae) {
		
		addButton.setUserData("justPressed");
		
		songLabel.setVisible(false);
		artistLabel.setVisible(false);
		albumLabel.setVisible(false);
		yearLabel.setVisible(false);
		
		songField.setVisible(true);
		artistField.setVisible(true);
		albumField.setVisible(true);
		yearField.setVisible(true);
		
		OKButton.setVisible(true);
		cancelButton.setVisible(true);
		deleteButton.setDisable(true);
		editButton.setDisable(true);
		addButton.setDisable(true);	
	}
	
	@FXML
    private void delete(ActionEvent ae) throws IOException {
		
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure that you want to delete this song?");
		alert.initOwner(primaryStage);
				
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {	
			Song song = listView.getSelectionModel().getSelectedItem();
			int index = listView.getSelectionModel().getSelectedIndex();
			map.remove(song.getKey());
			obsList.remove(listView.getSelectionModel().getSelectedItem());
			listView.getSelectionModel().select(index);
			
			Writer writer = new FileWriter(file);
			for(Song s : obsList){
				writer.write(s.saveToFile());
			}
			
			writer.close();	
		}
    }
	
	@FXML
    private void ok(ActionEvent ae) throws IOException {
		
		Song song = new Song(songField.getText().trim(), artistField.getText().trim(), 
							 albumField.getText().trim(), yearField.getText().trim());
		
		Song searched = map.get(song.getKey());
		if(searched != null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Duplicate");
			alert.setHeaderText("Warning");
			alert.setContentText("Cannot add a song that already exists.");
			alert.showAndWait();
			return;
		}
		
		if(song.getSong().equals("") || song.getArtist().equals("")){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Empty Fields");
			alert.setHeaderText("Warning");
			alert.setContentText("A name and artist are both required.");
			alert.showAndWait();
			return;
		}		
		
		if(addButton.getUserData().equals("notJustPressed")) {
			Song selected = listView.getSelectionModel().getSelectedItem(); 
			obsList.remove(selected);
			map.remove(selected.getKey());
			
			Writer writer = new FileWriter(file);
			for(Song s : obsList){
				writer.write(s.saveToFile());
			}
			
			writer.close();				
		}
		
		sortList(song);
		map.put(song.getKey(), song);
		listView.getSelectionModel().select(song);
		
		Writer writer = new FileWriter(file, true);
		writer.write(song.saveToFile());
	
		writer.close();		
		
		songField.setText("");
		artistField.setText("");
		albumField.setText("");
		yearField.setText("");
		
		songLabel.setVisible(true);
		artistLabel.setVisible(true);
		albumLabel.setVisible(true);
		yearLabel.setVisible(true);
		
		songField.setVisible(false);
		artistField.setVisible(false);
		albumField.setVisible(false);
		yearField.setVisible(false);
		
		OKButton.setVisible(false);
		cancelButton.setVisible(false);
		deleteButton.setDisable(false);
		editButton.setDisable(false);
		addButton.setDisable(false);
		
	}
	
	@FXML
	private void cancel(ActionEvent ae) {
		
		songField.setText("");
		artistField.setText("");
		albumField.setText("");
		yearField.setText("");
		
		songLabel.setVisible(true);
		artistLabel.setVisible(true);
		albumLabel.setVisible(true);
		yearLabel.setVisible(true);
		
		songField.setVisible(false);
		artistField.setVisible(false);
		albumField.setVisible(false);
		yearField.setVisible(false);
		
		OKButton.setVisible(false);
		cancelButton.setVisible(false);
		deleteButton.setDisable(false);
		editButton.setDisable(false);
		addButton.setDisable(false);
	}
	
	@FXML
	private void edit(ActionEvent ae) {
		
		addButton.setUserData("notJustPressed");
		
		songField.setText(songLabel.getText());
		yearField.setText(yearLabel.getText());
		artistField.setText(artistLabel.getText());
		albumField.setText(albumLabel.getText());
		
		songLabel.setVisible(false);
		artistLabel.setVisible(false);
		albumLabel.setVisible(false);
		yearLabel.setVisible(false);
		
		songField.setVisible(true);
		artistField.setVisible(true);
		albumField.setVisible(true);
		yearField.setVisible(true);
		
		OKButton.setVisible(true);
		cancelButton.setVisible(true);
		deleteButton.setDisable(true);
		editButton.setDisable(true);
		addButton.setDisable(true);
	}
	
	private void sortList(Song song) {
		if(obsList == null){
			obsList.add(song);
		}
		else {
			for(int i=0; i<obsList.size();i++){
				if(song.Comparable(obsList.get(i)) < 0){
					obsList.add(i,song);
					return;
				}
			}

			obsList.add(song);
		}
	}
	
}
