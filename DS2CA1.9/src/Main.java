import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
 
public class Main extends Application {

	ImageView myImageView, myBWView;
	
	Image image;
	
	WritableImage bWImage;
	
	BufferedImage bufferedImage;
	
	File file;
	
	// declares primary stage
	public void start (Stage ps) {
		
		// drop down menu option names
		MenuItem menuItem1 = new MenuItem("Open Image");
        MenuItem menuItem2 = new MenuItem("Black/White Image");
        MenuItem menuItem3 = new MenuItem("Image Details");
        MenuItem menuItem4 = new MenuItem("Mark Location of Birds");

        // declares drop down menu button
        MenuButton f = new MenuButton("File", null, menuItem1, menuItem2, menuItem3, menuItem4);
    	
   
        
        // main image view declared
        myImageView = new ImageView();
        
        // sets borderpane with menubutton at top and image view in middle/bottom
        BorderPane bp = new BorderPane();
        bp.setTop(f);
        bp.setBottom(myImageView);
        
        // title, size of scene
		ps.setTitle("Bird Counting Application");
        ps.setScene(new Scene(bp, 600, 600));
        ps.show();
    
        // first option(Open Image)
        menuItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
        
            	OpenImage();
            	
     
            }
        });
        
        // Change image to  black/white scale Option
        menuItem2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	// checks that image has been loaded into system
            	checkImage();
            	
            	
            	
            	// opens new window which B/W image shown in
            	final Stage bw = new Stage();
                bw.initModality(Modality.NONE);
                bw.initOwner(ps);
                VBox rgbVbox = new VBox(20);
                bw.setTitle("Black White Image Displayer");
                
                
                
                myBWView = new ImageView();
                myBWView.setImage(bWImage);
                
                myBWView.setFitHeight(550);
                myBWView.setFitWidth(550);
                
                rgbVbox.getChildren().add(myBWView);
                Scene detailsScene = new Scene(rgbVbox, 550,550);
                bw.setScene(detailsScene);
                bw.show();
               
            	
            	
            	
            	
     
            }
        });
        
        // Image Details Option
        menuItem3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	// checks that image has been loaded into system

            	checkImage();
            	
                final Stage details = new Stage();
                //dialog.initModality(Modality.APPLICATION_MODAL);
                details.initModality(Modality.NONE);
                details.initOwner(ps);
                VBox detailsVbox = new VBox(20);
                details.setTitle("Image Details");
                
                // displays file name and size in pop up window
                String FileDetails;
                String FileName = ("Image File Name: " + file.getName());
                String FileSize = ("Image File Size: " + file.length() + " bytes");
    
                FileDetails = (FileName + "\n" + FileSize);
                	
                
               
                detailsVbox.getChildren().add(new Text(FileDetails));
                Scene detailsScene = new Scene(detailsVbox, 350, 150);
                details.setScene(detailsScene);
                details.show();
            }
         });
        
        
        // Mark location of birds in image
        menuItem4.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	
        	// checks that image has been loaded into system

        	  checkImage();
        	
        	  
        	  // declares 2d array and runs disjointArray method
        	  int[][] flock = DisjointSets.disjointArray(bWImage); 
        	
        	  final Stage details = new Stage();
              //dialog.initModality(Modality.APPLICATION_MODAL);
              details.initModality(Modality.NONE);
              details.initOwner(ps);
              VBox detailsVbox = new VBox(20);
              details.setTitle("Bird Count");
              
              // runs method to mark original image
              BufferedImage markedImage = DisjointSets.markBirds(flock, bWImage, bufferedImage);
              
              // sets original image view with newly marked image
              myImageView.setImage(SwingFXUtils.toFXImage(markedImage, null));
              	
              // pop up stating original image marked
              detailsVbox.getChildren().add(new Text("Birds have been marked on Original Image"));
              Scene detailsScene = new Scene(detailsVbox, 350, 150);
              details.setScene(detailsScene);
              details.show();
          
        }
     });
		
	}
	
	// main method to open up original image
	public void OpenImage()
    {
    	
    	
        
        FileChooser fileChooser = new FileChooser();
        
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
          
        //Show open file dialog
        file = fileChooser.showOpenDialog(null);
        
        // takes in image chosen by user and converts into BW Image by calling method blackWhiteScale
        try {
            bufferedImage = ImageIO.read(file);
            image = SwingFXUtils.toFXImage(bufferedImage, null);
            bWImage = BlackWhiteImage.blackWhiteScale(image);
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // size of original image view
        myImageView.setFitHeight(600);
        myImageView.setFitWidth(600);
        myImageView.setImage(image);
        
    }
	
	
	// checks that image has been loaded into system and runs open image if not

	public void checkImage() {
    	if(image == null)
    	{
    		OpenImage();
    	}
    	else {
    		
    	}
    }
	
	
	
    public static void main(String[] args) {
        launch(args);
    }
     
        
}
