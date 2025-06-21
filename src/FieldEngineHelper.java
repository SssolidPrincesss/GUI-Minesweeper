package kursach;

import java.util.*;

public class FieldEngineHelper {
	private int fieldComplexity;
	private double mineGenerationKey;
    private int rows;
    private int cols;
    private char[][] matrix;
    
    public void setDifficulty(int comp) {
    	this.fieldComplexity = comp;
    	setFieldSize();
    }
    
    public void setFieldSize() {
    	switch(fieldComplexity) {
    		case 1:
    			rows = 6;
    			cols = 6;
    			mineGenerationKey = 0.15;
    			break;
    		case 2:
    			rows = 12;
    			cols = 12;
    			mineGenerationKey = 0.25;
    			break;
    		case 3:
    			rows = 16;
    			cols = 16;
    			mineGenerationKey = 0.35;
    			break;
    	}
    }
    
    
    public void fillTheField(){
		
		matrix = new char[rows][cols];
		
		Random rnd = new Random(System.currentTimeMillis());
		
		for (int i=0;i<rows;i++){
		    for (int j=0;j<cols;j++){
		    	matrix[i][j] = (rnd.nextDouble() < mineGenerationKey) ? '*' : '.';
		    }
		}
		
		for (int i=0;i<rows;i++){
		    for (int j=0;j<cols;j++){
		    	if(matrix[i][j] == '.') {
		    	matrix[i][j] = adjacentCellsMines(i, j);
		    	}
		    }
		}
		for (int i=0;i<rows;i++){
		    for (int j=0;j<cols;j++){
		    	System.out.print(matrix[i][j] + "    ");
		    }
		    System.out.println();
		    System.out.println();
		}
	}
    
    
    private char adjacentCellsMines(int row, int col) {
        int count = 0;
        
        for (int i = Math.max(0, row-1); i <= Math.min(rows-1, row+1); i++) {
            for (int j = Math.max(0, col-1); j <= Math.min(cols-1, col+1); j++) {
                if (i == row && j == col) continue;
                if (matrix[i][j] == '*') count++;
            }
        }
        
        return Character.forDigit(count, 10);
    }
    
    
    public char[][] getField() {
        return matrix;
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
}
