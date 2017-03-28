import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class MyMouseAdapter extends MouseAdapter {
//	private Random generator = new Random();
	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame) c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
			JLabel myLabel = new JLabel();
			myPanel.add(myLabel);
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			myPanel.mouseDownGridX = myPanel.getGridX(x, y);
			myPanel.mouseDownGridY = myPanel.getGridY(x, y);
			myPanel.repaint();
			break;
		case 3:		//Right mouse button
			c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			myFrame = (JFrame) c;
			myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
			myLabel = new JLabel();
			myPanel.add(myLabel);
			myInsets = myFrame.getInsets();
			x1 = myInsets.left;
			y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			x = e.getX();
			y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			myPanel.mouseDownGridX = myPanel.getGridX(x, y);
			myPanel.mouseDownGridY = myPanel.getGridY(x, y);
			myPanel.repaint();
			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame)c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			JLabel myLabel = new JLabel();
			myPanel.add(myLabel);
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			int gridX = myPanel.getGridX(x, y);
			int gridY = myPanel.getGridY(x, y);
			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					}else {
						if (myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(Color.RED) || myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(Color.GRAY)){
						//Released the mouse button on the same cell where it was pressed	
						}
						else if (!myPanel.mineFound){ //asking if a mine exploded or wins
							if(myPanel.mineArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] == 1){ 
								for (int col = 0; col < MyPanel.getTotalColumns(); col++) {   //a mine exploded: showing the rest of mines
									for (int row = 0; row < MyPanel.getTotalRows(); row++) {
										if(myPanel.mineArray[col][row] == 1)
											myPanel.colorArray[col][row] = Color.BLACK;
									}
								}
								myPanel.mineFound = true;
							}
							else if(myPanel.coveredCount > 0){
									myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.GRAY;
									myPanel.coveredCount--; // the amount of covered safe panels to uncover
									
									if (myPanel.isZero(myPanel.mineDetectorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY]) == true) { //zeros detection attemp
										myPanel.zeroActuation(myPanel.mouseDownGridX, myPanel.mouseDownGridY);
//										for (int num = 0; num <= myPanel.zerosCountlistX.size(); num++) {
//											myPanel.zeroActuation(myPanel.zerosCountlistX.get(num),myPanel.zerosCountlistY.get(num));
//										}
									}
							}
							else{
								myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.GRAY;
								for (int col = 0; col < MyPanel.getTotalColumns(); col++) {   
									for (int row = 0; row < MyPanel.getTotalRows(); row++) {
										if (myPanel.mineArray[col][row] == 1) {
											myPanel.colorArray[col][row] = Color.RED;
										}
									}
								}
								myPanel.redFlagCount = 0;
							}
							myPanel.repaint();
						}
					}
				}
			}
			
			myPanel.repaint();
			break;
		case 3:		//Right mouse button
			c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			myFrame = (JFrame)c;
			myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			myInsets = myFrame.getInsets();
			x1 = myInsets.left;
			y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			x = e.getX();
			y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			gridX = myPanel.getGridX(x, y);
			gridY = myPanel.getGridY(x, y);
			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else if (!myPanel.mineFound && myPanel.redFlagCount > 0) { //asking if a mine exploded
						if(myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(Color.GRAY)){
						//Released the mouse button on the same cell where it was pressed
							//do nothing
						}else if(myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(Color.RED)){
								myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.WHITE;
								myPanel.redFlagCount++;
						}else{ 
								myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.RED;
								myPanel.redFlagCount--;
							}
						myPanel.repaint();
						}
					}
				}
			
			myPanel.repaint();
			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
}