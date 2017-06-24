package boot;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import viewModel.ServerWindowController;

public class MainApp extends Application {
	
	// TODO: Ask jersey server if level is already planned, yes -> return it,
	//no -> plan it and send to both the server (PUT) and the client 
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Platform.runLater(()->{
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ServerWindow.fxml"));
				loader.setController(new ServerWindowController());
				Parent root = (Parent) loader.load();
		        Stage stage = new Stage();
		        Scene scene = new Scene(root);      
		        stage.setTitle("Server Management");
		        stage.setScene(scene);
		        stage.setResizable(false);
		        stage.show();

	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
		});
	}
	
	public static void main(String[] args) {//receive user input before starting
		launch(args);
	}

}
