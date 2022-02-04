import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import static javax.swing.SwingUtilities.invokeLater;


/**
 * Главный класс, метод createAndShowGUI() определяет окно программы
 */
class UserInterface {
  // поле главного класса - окно программы
  JFrame mainWindow = new JFrame("T-Rex Run");

  // поля класса: ширина и высота окна программы
  public static int WIDTH = 800;
  public static int HEIGHT = 500;
  
  public void createAndShowGUI() {
    // остановка прораммы при закрытии окна
    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // получение доступа к панели содержимого из структуры фрэйма
    Container container = mainWindow.getContentPane();

    // создание компонента JPanel для дальнейшего размещения объектов
    GamePanel gamePanel = new GamePanel();
    // Добавление интерфейса для приема событий клавиатуры
    gamePanel.addKeyListener(gamePanel);
    gamePanel.setFocusable(true);
    
    container.setLayout(new BorderLayout());
    
    container.add(gamePanel, BorderLayout.CENTER);
    
    mainWindow.setSize(WIDTH, HEIGHT);
    mainWindow.setResizable(false);
    mainWindow.setVisible(true);
  }
  
  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable(){
      public void run() {
        (new UserInterface()).createAndShowGUI();
      }
    });
  }
}