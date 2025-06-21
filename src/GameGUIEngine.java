package kursach;

import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;

public class GameGUIEngine implements ActionListener{
	JFrame minerFrame;
	JPanel mainPanel;
	JPanel chooseDiffPanel;
	JPanel gamePanel;
	JLabel chooseLabel;
	JButton lightButton;
	JButton mediumButton;
	JButton hardButton;
	JButton rulesButton;
	CardLayout cardLayout;
	Timer timer;
	private FieldEngineHelper gameFieldEngine;
	private int seconds = 0;
    private JLabel timerLabel = new JLabel("Time: 0");
	private JButton[][] cells;
	
	public GameGUIEngine(FieldEngineHelper engine) {
        this.gameFieldEngine = engine;
        createTheField();
        
    }
	
	
	public void createTheField() {
	    try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    minerFrame = new JFrame("Сапер");
	    minerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    minerFrame.setSize(800, 600);
	    minerFrame.setBackground(Color.BLACK);

	    // Инициализация CardLayout
	    cardLayout = new CardLayout();
	    mainPanel = new JPanel(cardLayout);

	    // Создаем панель выбора сложности
	    createDifficultyPanel();
	    mainPanel.add(chooseDiffPanel, "menu");

	    // Создаем игровую панель с BorderLayout
	    gamePanel = new JPanel(new BorderLayout());
	    
	    // Добавляем таймер в верхнюю часть игровой панели
	    timerLabel.setHorizontalAlignment(JLabel.CENTER);
	    timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
	    gamePanel.add(timerLabel, BorderLayout.NORTH);
	    
	    // Панель для игрового поля (будет заполнена позже)
	    JPanel fieldPanel = new JPanel();
	    gamePanel.add(fieldPanel, BorderLayout.CENTER);
	    
	    mainPanel.add(gamePanel, "game");

	    
	    timer = new Timer(1000, new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            seconds++;
	            timerLabel.setText("Time: " + seconds);
	        }
	    });
	    

	    
	    JMenuBar menuBar = new JMenuBar();
    	JMenu fileMenu = new JMenu("File");
    	JMenuItem newMenuItem = new JMenuItem("New");
    	newMenuItem.addActionListener(new NewMenuListener(this));
    	
    	fileMenu.add(newMenuItem);
    	menuBar.add(fileMenu);
    	minerFrame.setJMenuBar(menuBar);
    	
	    minerFrame.add(mainPanel);
	    minerFrame.setVisible(true);
	}

	private void createDifficultyPanel() {
	        chooseDiffPanel = new JPanel(null);
	        chooseDiffPanel.setBackground(Color.LIGHT_GRAY);

	        chooseLabel = new JLabel("Выбери сложность игры");
	        chooseLabel.setFont(new Font("Serif", Font.BOLD, 50));
	        chooseLabel.setBounds(100, 60, 600, 50);
	        chooseDiffPanel.add(chooseLabel);

	        lightButton = new JButton("Легко");
	        lightButton.setFont(new Font("Serif", Font.PLAIN, 40));
	        lightButton.setBounds(30, 190, 220, 240);
	        lightButton.setBackground(Color.GREEN);
	        lightButton.setBorderPainted(false);
	        lightButton.addActionListener(this);
	        chooseDiffPanel.add(lightButton);

	        mediumButton = new JButton("Нормально");
	        mediumButton.setFont(new Font("Serif", Font.PLAIN, 40));
	        mediumButton.setBounds(280, 190, 220, 240);
	        mediumButton.setBackground(Color.ORANGE);
	        mediumButton.setBorderPainted(false);
	        mediumButton.addActionListener(this);
	        chooseDiffPanel.add(mediumButton);

	        hardButton = new JButton("Тяжко");
	        hardButton.setFont(new Font("Serif", Font.PLAIN, 40));
	        hardButton.setBounds(530, 190, 220, 240);
	        hardButton.setBackground(Color.red);
	        hardButton.setBorderPainted(false);
	        hardButton.addActionListener(this);
	        chooseDiffPanel.add(hardButton);
	        
	        rulesButton = new JButton("Ознакомиться с правилами игры");
	        rulesButton.setFont(new Font("Serif", Font.PLAIN, 30));
	        rulesButton.setBounds(100, 460, 550, 50);
	        rulesButton.setBackground(Color.magenta);
	        rulesButton.setBorderPainted(false);
	        rulesButton.addActionListener(this);
	        chooseDiffPanel.add(rulesButton);
	    }
	
	
	
	
 	
	private void createGameField() {
		JPanel fieldPanel = (JPanel)gamePanel.getComponent(1);
		fieldPanel.removeAll();
	    fieldPanel.setLayout(new GridLayout(gameFieldEngine.getRows(), gameFieldEngine.getCols()));
		cells = new JButton[gameFieldEngine.getRows()][gameFieldEngine.getCols()];
		
		for(int i = 0; i < gameFieldEngine.getRows(); i++) {
	        for(int j = 0; j < gameFieldEngine.getCols(); j++) {
	            JButton cell = new JButton();
	            cell.setPreferredSize(new Dimension(50, 50));
	            cells[i][j] = cell;
	            
	            final int row = i;
	            final int col = j;
	            cell.addActionListener(e -> handleCellClick(row, col));
	            
	            cell.addMouseListener(new MouseAdapter() {
	                public void mousePressed(MouseEvent e) {
	                    if (SwingUtilities.isRightMouseButton(e)) {
	                        handleRightClick(row, col);
	                    }
	                }
	            });
	            
	            fieldPanel.add(cell);
	        }
	    }
	    
	    fieldPanel.revalidate();
	    fieldPanel.repaint();
	}
	
	
	private void handleRightClick(int row, int col) {
		JButton cell = cells[row][col];
		if (!cell.isEnabled()) {
	        return;
	    }
		if (cell.getText().isEmpty()) {
	        cell.setText("F");
	        cell.setForeground(Color.RED);
	        cell.setFont(new Font("Arial", Font.BOLD, 14));
	    } else {
	        cell.setText("");
	    }
	}
	
	
	private void handleCellClick(int row, int col) {
	    char[][] matrix = gameFieldEngine.getField();
	    String result = Character.toString(matrix[row][col]);
	    
	    if(matrix[row][col] == '*') {
	        for(int i = 0; i < matrix.length; i++) {
	            for(int j = 0; j < matrix[i].length; j++) {
	                if(matrix[i][j] == '*') {
	                    cells[i][j].setText("*");
	                    cells[i][j].setBackground(Color.RED); 
	                    cells[i][j].setEnabled(false);
	                }
	            }
	        }
	        JOptionPane.showMessageDialog(minerFrame, "Вы проиграли! Нажата мина!", "Конец игры", JOptionPane.ERROR_MESSAGE);
	    }
	    else if(matrix[row][col] == '0') {
	    	openAdjectionEmptyCells(row, col);
	    	
	    }
	    else {
	        // Обработка обычной клетки
	        cells[row][col].setText(result);
	        cells[row][col].setEnabled(false);
	    }
	    
	    if (checkWin()) {
	        JOptionPane.showMessageDialog(minerFrame, 
	            "Поздравляем! Вы победили!\nВремя: " + seconds + " секунд", 
	            "Победа!", 
	            JOptionPane.INFORMATION_MESSAGE);
	    }
	    
	}
	
	private boolean checkWin() {
	    char[][] matrix = gameFieldEngine.getField();
	    for (int i = 0; i < matrix.length; i++) {
	        for (int j = 0; j < matrix[i].length; j++) {
	            if (matrix[i][j] != '*' && cells[i][j].isEnabled()) {
	                return false;
	            }
	        }
	    }
	    return true;
	}
	
	private void openAdjectionEmptyCells(int row, int col) {
		for(int i = Math.max(0, row - 1); i <= Math.min(gameFieldEngine.getRows() - 1, row + 1); i++) {
			for(int j = Math.max(0, col - 1); j <= Math.min(gameFieldEngine.getCols() - 1, col + 1); j++) {
				
				if(!cells[i][j].isEnabled()) {
					continue;
				}
				if(row == i && col ==j){
					continue;
				}
				char value = gameFieldEngine.getField()[i][j];
				
				if(value == '0') {
					cells[i][j].setText("");
				    cells[i][j].setEnabled(false);
					openAdjectionEmptyCells(i,  j);
					
				}
				else if(value != '*' && value != '0') {
					cells[i][j].setText(Character.toString(value));
					cells[i][j].setEnabled(false);
				}
			}
		}	
		
	}
	
	public void resetTimer() {
        seconds = 0;
        timerLabel.setText("Time: 0");
        if (timer != null) {
        	timer.stop();
        }
    }
    
    
    public void showDifficultyMenu() {
        cardLayout.show(mainPanel, "menu");
    }
	
	
	public class NewMenuListener implements ActionListener {
	    private GameGUIEngine gameEngine;
	    
	    
	    public NewMenuListener(GameGUIEngine gameEngine) {
	        this.gameEngine = gameEngine;
	    }
	    
	    public void actionPerformed(ActionEvent e) {
	        
	        gameEngine.resetTimer();
	       
	        gameEngine.showDifficultyMenu();
	    }
	}
	
	public void actionPerformed(ActionEvent e) {
		
		String command = ((JButton)e.getSource()).getText();
        if(command != "Ознакомиться с правилами игры") {
        	switch(command) {
            case "Легко": gameFieldEngine.setDifficulty(1); break;
            case "Нормально": gameFieldEngine.setDifficulty(2); break;
            case "Тяжко": gameFieldEngine.setDifficulty(3); break;
            
        	}
	        timer.start();
	        gameFieldEngine.fillTheField();
	        createGameField();
	        cardLayout.show(mainPanel, "game");
        }
        else {
        	 JOptionPane.showMessageDialog(minerFrame, "Цель игры:\r\n"
        	 		+ "Открыть все клетки поля, не наступив на мины.\r\n"
        	 		+ "\r\n"
        	 		+ "Управление:\r\n"
        	 		+ "Левый клик — открыть клетку\r\n"
        	 		+ "Правый клик — поставить/убрать флажок (метку мины) ⚑\r\n"
        	 		+ "\r\n"
        	 		+ "Что означают цифры:\r\n"
        	 		+ "Число в открытой клетке показывает, сколько мин находится в соседних 8 клетках вокруг неё.\r\n"
        	 		+ "Пример: цифра «3» означает, что рядом есть 3 мины.\r\n"
        	 		+ "\r\n"
        	 		+ "Пустые клетки:\r\n"
        	 		+ "Если открыта клетка без цифры (пустая), автоматически откроются все соседние безопасные клетки.\r\n"
        	 		+ "\r\n"
        	 		+ "Как победить:\r\n"
        	 		+ "Отметить флажками все мины (не обязательно) и открыть все безопасные клетки.\r\n"
        	 		+ "\r\n"
        	 		+ "Проигрыш:\r\n"
        	 		+ "Если открыть клетку с миной — игра заканчивается.", "Правила игры", JOptionPane.INFORMATION_MESSAGE);
        }
        
	}
}
