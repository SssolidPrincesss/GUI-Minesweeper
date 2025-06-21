# GUI-Minesweeper
Это типичный сапер с тремя уровнями сложности в авторском исполнении. В зависимости от уровня сложности изменяются размеры игровго поля и частота генерации мин. Как и в стандартном сапере тут есть таймер и возможность пометить флажком клетку с миной 
  

Давайте посмотрим на то, как выглядит игра:  
Меню выбора сложности  
![Menu](https://github.com/SssolidPrincesss/GUI-Minesweeper/blob/main/MSweeperImages/Menu.png)  

Правила игры  
![Menu](https://github.com/SssolidPrincesss/GUI-Minesweeper/blob/main/MSweeperImages/Rules.png)  

Процесс игры:  
![Menu](https://github.com/SssolidPrincesss/GUI-Minesweeper/blob/main/MSweeperImages/GameField.png)  

Игровое меню(нужно только для возвращения в меню выбора сложности)  
![Menu](https://github.com/SssolidPrincesss/GUI-Minesweeper/blob/main/MSweeperImages/GameMenu.png)

А теперь давайте разберемся из чего состоит программа:  
![Menu](https://github.com/SssolidPrincesss/GUI-Minesweeper/blob/main/MSweeperImages/ShemeOfTheGameCode.png)  
На данной схеме синиме праллелограммамы - классы, зеленые квадраты - методы классов, а желтыйе овалы - вложенные классы 

Теперь разберем код программы  
Класс MinerGame, который является точкой входа программы:  

```java
public class MinerGame {
	public static void main(String[] args) {
		FieldEngineHelper engine = new FieldEngineHelper();
		GameGUIEngine view = new GameGUIEngine(engine);
	}

}
```

Класс GameGUIEngine отвечает за графичесской представление игры и частично за игровую логику  
Для начала необходимо импортировать несколько библеотек:  
```java
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
```
Данный класс реализует интерфейс ActionListener. В следуюшем блоке кода содежатся переменные экземпляра, которые нужны для работы с графичесскими элементами фрейма, менеджером компановки, классом-помошником, таймером, игровым полем:  
```java
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
```
Конструктор класса:  
```java
     public GameGUIEngine(FieldEngineHelper engine) {
        this.gameFieldEngine = engine;
        createTheField();
        
    }
```
Метод createTheField отвечает за создание фрейма игры. В самом начале находятся несколько волшебных строк кода, которые позволяют подстроить внешний вид элементов фрейма под системные настройки. Далее происходит инициализация и настройка фрейма, устанавливается компонофщик(он нужнен для возможности преключения между меню выбора сложности и игровым полем), создается игровое поле, а так же создается и настраивается таймер, и наконец инициализируется игровое меню и его элементы:  
```java 
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

	    
	    cardLayout = new CardLayout();
	    mainPanel = new JPanel(cardLayout);

	    
	    createDifficultyPanel();
	    mainPanel.add(chooseDiffPanel, "menu");

	    
	    gamePanel = new JPanel(new BorderLayout());
	    
	    
	    timerLabel.setHorizontalAlignment(JLabel.CENTER);
	    timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
	    gamePanel.add(timerLabel, BorderLayout.NORTH);
	    
	    
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
```
Метод createDifficultyPanel отвечает за создание и настройку панели выбора уровня сложности:  
```java 
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
```
Метод createGameField помогает нам настроить взамодействие с игровым полем(сделать ход, поставить флг) посредством вызова реализованных далле мкетодлов интерфейсов ActionListener и  MouseListener:  
```java
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
```
Метод handleRightClick отвечает за установку флажка:  
```java
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
```
Метод handleCellClick обрабатывает нажатие левой кнопки мыши, а так же проверяет, есть ли еще доступные ходы при помощи вызова метода checkWin:  
```java
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
```
Метод checkWin проходится по всему игровому полю и проверяет, есть ли на поле неоткрытые ячейки:  
```java
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
```
Метод openAdjectionEmptyCells(вызывается в методе handleCellClick) нужен для обработки случая, когда игрок нажал на клетку, вокруг которой нет мин. Он открывает ячейки вокруг хода игрока, пока не наткнется на мину:  
```java
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
```
Метод resetTimer сбрасывает таймер, когда обновляется игровое поле:   
```java
public void resetTimer() {
        seconds = 0;
        timerLabel.setText("Time: 0");
        if (timer != null) {
        	timer.stop();
        }
    }
```
Метод showDifficultyMenu переключается на меню выбора сложности при помощи менеджера компоновки:   
```java
public void showDifficultyMenu() {
        cardLayout.show(mainPanel, "menu");
    }
```
Класс NewMenuListener помогает настроить взаимодействие с элементами игрового меню:  
```java
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
```  
Метод actionPerformed — это обязательный к реализации метод интерфейса ActionListener. В данном случае он позволяет настроить взаимодействие с кнопками меню выбора сложности:  
```java
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
```

