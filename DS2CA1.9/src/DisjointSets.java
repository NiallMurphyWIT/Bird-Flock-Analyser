import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class DisjointSets {

	
	  // takes in B/W image and makes 2D array marking white pixels as 0 as black as -1
	  public static int[][] disjointArray(WritableImage blackWhiteImage) {
		  
		  
		  	// declares pixel reader for B/W image
	    	PixelReader pixelReader = blackWhiteImage.getPixelReader();
	    	
	    	// declares width/height of image
	    	int width = (int) blackWhiteImage.getWidth();
	    	int height = (int) blackWhiteImage.getHeight();
	    	
	    	// declares 2d image to be returned
	    	int[][] flock = new int [height][width];
	    	
	    	// for loop to check every pixel in image starting at top left and going row by row
	    	for (int y = 0; y < height; y++) {
	    		for (int x = 0; x < width; x++) {
	    			
	    			// sets color to that of current pixel
	    			int color = pixelReader.getArgb(x, y);
	    			
	    			
	    			if(color !=  0)
	    			{
	    				//0
	    				// mark black if color is not equal to Argb white value
	    				flock[y][x] = 0;
	    			}
	    			else
	    			{
	    				//-1
	    				// else mark color as white
	    				flock[y][x] = -1;
	    			}
	    		}
	    		
	    	
	    			
	    	
	    	}
	    	
	    	// returns 2D array
	    	return flock;
	  	
	    }
	
	 
	// method that counts amount of birds, takes in 2d array
	static BufferedImage markBirds(int arrayBirds[][], WritableImage bwImage, BufferedImage image) 
	{ 
		int n = arrayBirds.length; 
		int m = arrayBirds[0].length; 
		
		// declares graphics to draw rects and numbers on image
		Graphics imageGraphics = image.getGraphics();

		// declares new DisjointUnionSet(Union Find)
		DisjointUnionSets dus = new DisjointUnionSets(n*m); 


		// loop to check if neighbors and unions if both equal to 0 (black)
		for (int i=0; i<n; i++) 
		{ 
			for (int k=0; k<m; k++) 
			{ 
				// If cell is -1, nothing to do as pixel white
				if (arrayBirds[i][k] == -1) 
					continue; 

				// Check neighbor infront and below and do a union 
				// with neighbors set if neighbor is  
				// also 0
				if (i+1 < n && arrayBirds[i+1][k]==0) 
					dus.join(i*(m)+k, (i+1)*(m)+k); 
          
				if (k+1 < m && arrayBirds[i][k+1]==0) 
					dus.join(i*(m)+k, (i)*(m)+k+1); 
          
			} 
		}
		
		// Declares string and int to print amount the number of birds in image
		String birdNumber = "1";
		int birdCounter = 1;
		
		// calls get roots with noise reduction set to 1
		Set<Integer> roots = dus.getRoots(1);
		Iterator<Integer> iter = roots.iterator();
		while(iter.hasNext()) {
			int root = iter.next();
			// calls getConnectedNodes
			List<Integer> nodes = dus.getConnectedNodes(root);
			
			// declares ints as -1 to start
			int leftmost = -1;
			int rightmost = -1;
			int topmost = -1;
			int bottommost = -1;
			
			for(int i = 0; i < nodes.size(); i++) {
				
				// nodeX is for horizontal right/left
				// nodeY is for vertical top/bottom
				int nodeX = (int) (nodes.get(i) % bwImage.getWidth());
				int nodeY = (int) (nodes.get(i) / bwImage.getWidth());
			
				
				if(leftmost == -1 || nodeX < leftmost) {
					leftmost = nodeX;
				}
				if(rightmost == -1 || nodeX > rightmost) {
					rightmost = nodeX;
				}
				if(topmost == -1 || nodeY < topmost) {
					topmost = nodeY;
				}
				if(bottommost == -1 || nodeY > bottommost) {
					bottommost = nodeY;
				}
			}
			
			// gets width of rect to be drawn with drawRect
			int width = rightmost - leftmost;
			// gets height of rect to be drawn with drawRect
			int height = bottommost - topmost;
			// draws cyan rect around bird
			imageGraphics.setColor(Color.CYAN);
			imageGraphics.drawRect(leftmost, topmost, width, height);
			imageGraphics.setColor(Color.MAGENTA);
			// changes color to magenta and draws number at top left of rect
			imageGraphics.drawString(birdNumber, leftmost, topmost);
			// incremnets birdCounter and updates birdNumber string for next bird
			birdCounter++;
			birdNumber = Integer.toString(birdCounter);
		}
		
		// prints out total birds a top right of image 
		// birdCounter has to be decreased as is set for next bird but are no more
		String birdTotal = "Their are " + --birdCounter + " birds present in the image";
		imageGraphics.drawString(birdTotal, 5, 15);
		imageGraphics.dispose();
		
		// returns updated image
		return image;
	}
} 

	//Class to represent Disjoint Set Data structure 
	
class DisjointUnionSets 
{ 
	int[] connected, sizes; 
	int n; 

	public DisjointUnionSets(int n) 
	{ 
		
		connected = new int[n]; 
		sizes = new int[n]; 
		
		for(int i = 0; i < n; i++)
		{
			connected[i] = i;
			sizes[i] = 1;
		}
	} 

	// joins one node to another
	public void join(int nodeOne, int nodeTwo) {
		int nodeOneRoot = root(nodeOne);
		int nodeTwoRoot = root(nodeTwo);
		
		// does nothing if roots are the same
		if(nodeOneRoot == nodeTwoRoot) {
			return;
		}
		
		// smaller node root is added to larger node root
		if(sizes[nodeOneRoot] > sizes[nodeTwoRoot]) {
			connected[nodeTwoRoot] = nodeOneRoot;
			sizes[nodeOneRoot] += sizes[nodeTwoRoot];
			sizes[nodeTwoRoot] = 1;
		} else {
			connected[nodeOneRoot] = nodeTwoRoot;
			sizes[nodeTwoRoot] += sizes[nodeOneRoot];
			sizes[nodeOneRoot] = 1;
		}
	}
	
	
	public int root(int node) {
		while(connected[node] != node) {
			node = connected[node];
		}
		return node;
	}
	
	// get Root using noise reduction
	public Set<Integer> getRoots(int noiseReduction) {
		Set<Integer> roots = new HashSet<>();
		for(int i = 0; i < sizes.length; i++) {
			if(sizes[i] > noiseReduction) {
				roots.add(i);
			}
		}
		return roots;
	}
	
	// gets connected nodes
	public List<Integer> getConnectedNodes(int node) {
		List<Integer> nodes = new ArrayList<>();
		for(int i = 0; i < connected.length; i++) {
			if(root(node) == root(i)) {
				nodes.add(i);
			}
		}
		return nodes;
	}

}
