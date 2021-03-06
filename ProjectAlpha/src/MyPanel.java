import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 9;
	private static final int TOTAL_ROWS = 9;   
	private static final int TOTAL_MINES = 10;
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS]; //array to control colors
	public int[][] mineArray = new int[TOTAL_COLUMNS][TOTAL_ROWS]; //array to know where are the mines
	public int[][] mineDetectorArray = new int[TOTAL_COLUMNS][TOTAL_ROWS]; //array to detect nearby mines
	public int[][] flaggedArray = new int[TOTAL_COLUMNS][TOTAL_ROWS];
	public int redFlagCount = TOTAL_MINES; 
	public int coveredCount = TOTAL_COLUMNS * TOTAL_ROWS - TOTAL_MINES - 1; //the amount of panels that the player has to uncover
	public boolean mineFound = false; //it is use to know when a mine exploded
	public ArrayList<Integer>  zerosCountlistX = new ArrayList<Integer>(coveredCount);
	public ArrayList<Integer>  zerosCountlistY = new ArrayList<Integer>(coveredCount);
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		Random generator = new Random();
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   
			for (int y = 0; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
				mineArray[x][y] = 0;
				mineDetectorArray[x][y] = 0;
				flaggedArray[x][y] = 0;
			}
		}
		for (int i = 0; i < TOTAL_MINES; i++) { //Mine generator
			int randomX;
			int randomY;
			do{
				randomX = generator.nextInt(9);
				randomY = generator.nextInt(9);
			}while(mineArray[randomX][randomY] == 1);
			mineArray[randomX][randomY] = 1;
//			colorArray[randomX][randomY] = Color.BLACK; //DON'T ERASE: it is for debugging purpose, just comment the line
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //gaining the amount of nearby mines of each label
			for (int y = 0; y < TOTAL_ROWS; y++) {
				if (mineArray[x][y] == 0){
					int counter = 0;
					if (x == 0 && y == 0) {
						for (int k = x; k <= x+1; k++) {
							for (int j = y; j <= y+1; j++) {
								if (mineArray[k][j] == 1){
								counter++;
								}
							}
						}
					} 
					else if(x == 8 && y ==0) {
						for (int k = x-1; k <= x; k++) {
							for (int j = y; j <= y+1; j++) {
								if (mineArray[k][j] == 1){
								counter++;
								}
							}
						}
					}
					else if (x == 0 && y == 8) {
						for (int k = x; k <= x+1; k++) {
							for (int j = y-1; j <= y; j++) {
								if (mineArray[k][j] == 1){
									counter++;
								}
							}
						}
					}
					else if (x == 8 && y == 8) {
						for (int k = x-1; k <= x; k++) {
							for (int j = y-1; j <= y; j++) {
								if (mineArray[k][j] == 1){
								counter++;
								}
							}
						}
					}
					else if (x == 0) {
						for (int k = x; k <= x+1; k++) {
							for (int j = y-1; j <= y+1; j++) {
								if (mineArray[k][j] == 1){
								counter++;
								}
							}
						}
					}
					else if (x == 8) {
						for (int k = x-1; k <= x; k++) {
							for (int j = y-1; j <= y+1; j++) {
								if (mineArray[k][j] == 1){
								counter++;
								}
							}
						}
					}
						else if (y == 0) {
							for (int k = x-1; k <= x+1; k++) {
								for (int j = y; j <= y+1; j++) {
									if (mineArray[k][j] == 1){
									counter++;
									}
								}
							}
						}
						else if (y == 8) {
							for (int k = x-1; k <= x+1; k++) {
								for (int j = y-1; j <= y; j++) {
									if (mineArray[k][j] == 1){
									counter++;
									}
								}
							}
						}
						else {
							for (int k = x-1; k <= x+1; k++) {
								for (int j = y-1; j <= y+1; j++) {
									if (mineArray[k][j] == 1){
									counter++;
									}
								}
							}
						}
					mineDetectorArray[x][y] = counter;
			} 
		}
	}
	}
	public static int getTotalColumns() {
		return TOTAL_COLUMNS;
	}
	public static int getTotalRows() {
		return TOTAL_ROWS;
	}
	public static int getTotalMines() {
		return TOTAL_MINES;
	}
	public static int getGridX() {
		return GRID_X;
	}
	public static int getGridY() {
		return GRID_Y;
	}
	public static int getInnerCellSize() {
		return INNER_CELL_SIZE;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x1, y1, width + 1, height + 1);

		//Draw the grid minus the bottom row (which has only one cell)
		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS)));
		}

		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
					Color c = colorArray[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
					if(mineArray[x][y] == 0 && mineDetectorArray[x][y] != 0){
						if(flaggedArray[x][y] == 1){
							g.setColor(Color.RED);
							g.drawString("" + mineDetectorArray[x][y] + "", x1 + GRID_X + 10 + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + 20 + (y * (INNER_CELL_SIZE + 1)) + 1);
						}else{
							g.setColor(Color.WHITE);
							g.drawString("" + mineDetectorArray[x][y] + "", x1 + GRID_X + 10 + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + 20 + (y * (INNER_CELL_SIZE + 1)) + 1);
						}
					}
			}
		}
	}
	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return x;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return y;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}


public boolean isZero (int arrayPos) { // 
	return (arrayPos == 0);	
	}

public void zeroActuation (int col, int row) {

	
	if (col == 0 && row == 0) {
		for (int x = col; x <= col+1; x++) {
			for (int y = row; y <= row+1; y++) {
				colorArray[x][y] = Color.GRAY;
			}
		}
	}
	else if (col == 8 && row ==0) {
		for (int x = col-1; x <= col; x++) {
			for (int y =row; y <= row+1; y++) {
				colorArray[x][y] = Color.GRAY;	
			}
		}
	}
	else if (col == 0 && row == 8) {
		for (int x = col; x <= col+1; x++) {
			for (int y = row-1; y <= row; y++) {
				colorArray[x][y] = Color.GRAY;		
			}
		}
	}
	else if (col == 8 && row == 8) {
		for (int x = col-1; x <= col; x++) {
			for (int y = row-1; y <= row; y++) {
				colorArray[x][y] = Color.GRAY;		
			}
		}
	}
	else if (col == 0) {
		for (int x = col; x <= col+1; x++) {
			for (int y = row-1; y <= row+1; y++) {
				colorArray[x][y] = Color.GRAY;	
			}
		}
	}
	else if (col == 8) {
		for (int x = col-1; x <= col; x++) {
			for (int y = row-1; y <= row+1; y++) {
				colorArray[x][y] = Color.GRAY;				
			}
		}
	}
	else if (row == 0) {
		for (int x = col-1; x <= col+1; x++) {
			for (int y = row; y <= row+1; y++) {
				colorArray[x][y] = Color.GRAY;	
			}
		}
	}
	else if (row == 8) {
		for (int x = col-1; x <= col+1; x++) {
			for (int y = row-1; y <= row; y++) {
				colorArray[x][y] = Color.GRAY;
			}
		}
	}
	else {
		for(int x = col-1; x <= col+1; x++){
			for (int y =row-1; y <= row+1; y++) {
				colorArray[x][y] = Color.GRAY;
				}
			}
		}
	
		}
//
//public void zerosMovement (int col, int row) {
//	int limit = 0;
//	for(y = row; y <= 8-limit; y++){
//		for(x = col; x <= 8-limit; x++){
//			if (mineDetectorArray[x][y]==0){ 
////				zerosCountlistX.add(x);
////				zerosCountlistY.add(y);
//				zeroActuation(x,y);
//				} 
//				else { limit =8;
//				}
//			if (mineDetectorArray[x][y]!=0){
//				break;
//			}
//			}
//		for(y = row; y >= 0; y--){
//			for(x = col; x >= 0; x--){
//				if (isZero(mineDetectorArray[x][y])){ 
////					zerosCountlistX.add(x);
////					zerosCountlistY.add(y);
//					zeroActuation(x,y);
//					} 
//					else {
//						break;
//					}
//				}
//			}
//		for(y = row; y <= 8; y++){
//			for(x = col; x >= 0; x--){
//				if (isZero(mineDetectorArray[x][y])){ 
////					zerosCountlistX.add(x);
////					zerosCountlistY.add(y);
//					zeroActuation(x,y);
//					} 
//					else {
//						break;
//					}
//				}
//			}
//		for(y = row; y >= 0; y--){
//			for(x = col; x <= 8; x++){
//				if (isZero(mineDetectorArray[x][y])){ 
////					zerosCountlistX.add(x);
////					zerosCountlistY.add(y);
//					zeroActuation(x,y);
//					} 
//					else {
//						break;
//					}
//				}
//			}
//		}
//	for (x=0; x<= zerosCountlistX.size(); x++){
//		zeroActuation(zerosCountlistX.get(x),zerosCountlistY.get(x));
//		
//		}
//	}
}





