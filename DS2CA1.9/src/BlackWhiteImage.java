import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class BlackWhiteImage {
	
	// method to change image to B/W scale	
	public static WritableImage blackWhiteScale(javafx.scene.image.Image image)
    {
    	PixelReader pixelReader = image.getPixelReader();

    	// declares width and height of image
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        // makes new writable image to return
        WritableImage blackWhiteImage = new WritableImage(width, height);
        
     // for loop to check every pixel in image starting at top left and going row by row
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
            	
            	// get color of current pixel
                int pixel = pixelReader.getArgb(x, y);
                
                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);
                int avg = (int)(red+blue+green)/3;
                
                
                if(avg > 90)
                	// changes pixels below threshold of 90 to white
                	blackWhiteImage.getPixelWriter().setArgb(x, y, 0);
                else
                	// change rest of pixels to black
                	blackWhiteImage.getPixelWriter().setArgb(x, y, -16777216);
                

                
            }
        }
        // returns new updated image
		return blackWhiteImage;
    }
}
