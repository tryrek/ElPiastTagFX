package application;
	
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class Main extends Application {
		String nazwaPliku = "";
		String folderZapisu = "";
		   @Override
		    public void start(Stage primaryStage) {
			   	MenuBar menuBar = new MenuBar();
			   	DirectoryChooser chooser = new DirectoryChooser();
		        Label label = new Label("Upuść plik do konwersji");
		        Label dropped = new Label("");
		        Label droppedExtract = new Label("");
		        Label error = new Label("");
		        Button buttonConvert = new Button("Konwertuj!");
		        Label allrights = new Label("Allrights reserved to Piotr Krajewski.");
		        Alert alert = new Alert(AlertType.NONE);
		        VBox dragTarget = new VBox();
		        dragTarget.getChildren().addAll(menuBar,label,dropped,droppedExtract,error,buttonConvert,allrights);
		        //menu bar
		        Menu plikMenu = new Menu("Plik");
		        MenuItem plikMenuItemZamknij = new MenuItem("Zamknij");
		        MenuItem plikMenuItemSave = new MenuItem("Zmień folder zapisu");
		        plikMenu.getItems().add(plikMenuItemZamknij);
		        plikMenu.getItems().add(plikMenuItemSave);
		        Menu infoMenu = new Menu("Info");
		        MenuItem infoMenuItemHelp = new MenuItem("About");
		        infoMenu.getItems().add(infoMenuItemHelp);
		        menuBar.getMenus().add(plikMenu);
		        menuBar.getMenus().add(infoMenu);
		        
		        //akcje do menu
		        infoMenuItemHelp.setOnAction(e -> {
					alert.setAlertType(AlertType.INFORMATION);
					alert.setTitle("Informacje");
					alert.setHeaderText("Informacje o programie ElPiastTagFX v0.0.0.1");
					alert.setContentText("W celu skonwertowania pliku najpierw należy przeciągnąć plik w formacie TXT na okno aplikacji. Następnie należy wcisnąć przycisk Konwertuj!. Informacja o ukończonej konwersji zostanie wyświetlona w wyskakującym oknie"
							+ "\nAutor programu Piotr Krajewski.");

					alert.showAndWait();	
		        });
		        
		        plikMenuItemZamknij.setOnAction(e -> {
		        	Platform.exit();
		        });
		        plikMenuItemSave.setOnAction(e -> {
		        	if(folderZapisu.equals("")) {
		        		chooser.setInitialDirectory(new File("c:\\"));
		        	}
		        	else {
		        		chooser.setInitialDirectory(new File(folderZapisu));
		        	}
		        	File selectedFile = chooser.showDialog(primaryStage);
		        	folderZapisu=selectedFile.toString()+"\\";
		        	droppedExtract.setText("Folder zapisu: "+folderZapisu);
		        });
		        
		        
		        
		        dragTarget.setOnDragOver(new EventHandler<DragEvent>() {
		        

		        	
		            @Override
		            public void handle(DragEvent event) {
		                if (event.getGestureSource() != dragTarget
		                        && event.getDragboard().hasFiles()) {
		                    /* allow for both copying and moving, whatever user chooses */
		                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		                }
		                event.consume();
		            }
		        });
		        
		        dragTarget.setOnDragDropped(new EventHandler<DragEvent>() {

		            @Override
		            public void handle(DragEvent event) {
		                Dragboard db = event.getDragboard();
		                boolean success = false;
		                if (db.hasFiles()) {
		                	nazwaPliku=db.getFiles().toString();
		                	if(nazwaPliku.substring(nazwaPliku.length()-5,nazwaPliku.length()-1).equals(".txt")) {
		                	nazwaPliku =db.getFiles().toString().substring(1,nazwaPliku.length()-1);
		                    dropped.setText("Wybrany plik: \n"+nazwaPliku);
							String spl[]=nazwaPliku.split("\\\\");
							folderZapisu="";
							for(int c=0;c<spl.length-1;c++) {
								folderZapisu=folderZapisu+spl[c]+"\\";
								}
		                    droppedExtract.setText("Folder zapisu: "+folderZapisu);
		                    success = true;
		                }
		                }
		                /* let the source know whether the string was successfully 
		                 * transferred and used */
		                event.setDropCompleted(success);

		                event.consume();
		            }
		        });
		        
		        //event uruchamiający przetwarzanie pliku
		        buttonConvert.setOnAction(new EventHandler<ActionEvent>() {
		            @Override public void handle(ActionEvent e) {
		            	
		                if(nazwaPliku.equals("")) {
		                	alert.setAlertType(AlertType.ERROR);
		                	alert.setTitle("Error Dialog");
		                	alert.setHeaderText("Brak pliku do konwersji");
		                	alert.setContentText("Proszę podać podać plik do konwersji!");

		                	alert.show();	
		                }
		                else {
		                	if(nazwaPliku.substring(nazwaPliku.length()-4,nazwaPliku.length()).equals(".txt")) { 
		                		error.setText("Progress: 0%");
		                		Plik plik = new Plik(nazwaPliku);
		                		ArrayList<String> lista = plik.czytajTxtDoListy();
		                		ArrayList<piastTag> tags =plik.makePiastTagList(lista);
		                		error.setText("Progress: 40%");
		                		try {
		                			Excel exc = new Excel(folderZapisu+"\\zmienneExcel.xls");
		                			error.setText("Progress: 60%");
		                			boolean stan = exc.createSheet(tags);
		                			error.setText("Progress: 100%");
		                			if(stan==true) {
		                				alert.setAlertType(AlertType.INFORMATION);
		                				alert.setTitle("Sukces!!");
		                				alert.setHeaderText(null);
		                				alert.setContentText("Operacja wykonana pomyślnie. Utworzono plik");

		                				alert.showAndWait();
		                				error.setText("Complete!!");
		                			}
		                			else {
		        	                	alert.setAlertType(AlertType.ERROR);
		        	                	alert.setTitle("Error Dialog");
		        	                	alert.setHeaderText("Niepowodzenie");
		        	                	alert.setContentText("Nie udało się utworzyć plików :(. Spróbuj ponownie!");

		        	                	alert.show();	
		        	                	error.setText("Failed!!");
		        	                }
		                			
		                		}catch (Exception e2) {
		                			alert.setAlertType(AlertType.ERROR);
		                			alert.setTitle("Exception Dialog");
		                			alert.setHeaderText("Exception error");

		                			
		                				
		                			// Create expandable Exception.
		                			StringWriter sw = new StringWriter();
		                			PrintWriter pw = new PrintWriter(sw);
		                			e2.printStackTrace(pw);
		                			String exceptionText = sw.toString();

		                			Label label = new Label("The exception stacktrace was:");

		                			TextArea textArea = new TextArea(exceptionText);
		                			textArea.setEditable(false);
		                			textArea.setWrapText(true);

		                			textArea.setMaxWidth(Double.MAX_VALUE);
		                			textArea.setMaxHeight(Double.MAX_VALUE);
		                			GridPane.setVgrow(textArea, Priority.ALWAYS);
		                			GridPane.setHgrow(textArea, Priority.ALWAYS);

		                			GridPane expContent = new GridPane();
		                			expContent.setMaxWidth(Double.MAX_VALUE);
		                			expContent.add(label, 0, 0);
		                			expContent.add(textArea, 0, 1);

		                			// Set expandable Exception into the dialog pane.
		                			alert.getDialogPane().setExpandableContent(expContent);

		                			alert.showAndWait();
		                			error.setText("FAILED!");
		                		}
		                		
		                		
		                	}
		                	else {
			                	alert.setAlertType(AlertType.ERROR);
			                	alert.setTitle("Error Dialog");
			                	alert.setHeaderText("Zły format pliku");
			                	alert.setContentText("Proszę podać plikw formacie TXT!");

			                	alert.show();
			                	
		                	}
		                }
		            }
		        });



		        StackPane root = new StackPane();
		        
		        root.getChildren().add(dragTarget);



		        Scene scene = new Scene(root, 500, 200);

		        primaryStage.setTitle("ElPiastTagFX v0.0.0.1");
		        primaryStage.setScene(scene);
		        primaryStage.setResizable(false);
		        primaryStage.show();
		    }

		    /**
		     * @param args the command line arguments
		     */
		    public static void main(String[] args) {
		        launch(args);
		    }

		}
