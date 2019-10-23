// Eyob Tesfaye and Kishan Zalora

package app;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import view.SongLibController;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;


public class SongLib extends Application {
	@Override
	public void start(Stage primaryStage)
	throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/SongLib.fxml"));
		GridPane root = (GridPane)loader.load();
		
		SongLibController songLibController = loader.getController();
		songLibController.start(primaryStage);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("SongLib");
		primaryStage.setResizable(false);  
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
