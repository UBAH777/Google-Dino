import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import components.Ground;
import components.Dino;
import components.Obstacles;



class GamePanel extends JPanel implements KeyListener, Runnable {

  public static int WIDTH;     // принимают размеры для панели от размеров окна
  public static int HEIGHT;

  private Thread animator;     // поток анимации программы
  
  private boolean running = false;   // параметры состояния игры
  private boolean gameOver = false;
  
  Ground ground;          // земля
  Dino dino;              // динозавр
  Obstacles obstacles;    // препятствия

  private int score;      // очки

  //TODO конструктор
  public GamePanel() {

    WIDTH = UserInterface.WIDTH;
    HEIGHT = UserInterface.HEIGHT;
    
    ground = new Ground(HEIGHT);
    dino = new Dino();
    obstacles = new Obstacles((int)(WIDTH * 1.5));

    score = 0;
    
    setSize(WIDTH, HEIGHT);
    setVisible(true);
  }

  // Стандартный метод для перерисовки изображения при вызове repaint()
  public void paint(Graphics g) {
    /** Класс панели относится к легковесным, поэтому должен вначале
     *  вызывать метод paint() родительского класса, который вызовет перерисовку
     *  всех своих компонентов
     * */
    super.paint(g);

    switch (Dino.getState()) {
      case 1:
        g.setFont(new Font("Courier New", Font.BOLD, 25));
        g.drawString("Press 'Space' to start", (int) (getWidth()*0.28), (int) (getHeight()*0.33));
        break;

      case 4:
        g.setFont(new Font("Courier New", Font.BOLD, 25));
        g.drawString("Game over", (int) (getWidth()*0.4), (int) (getHeight()*0.33));
        break;
    }

    // Задание стиля и шрифта, вывод надписи подсчёта очков в игре
    g.setFont(new Font("Courier New", Font.BOLD, 25));
    g.drawString("score " + score, (int)(getWidth()*0.8 - 5), 25);

    ground.create(g);            // вызов метода перерисовки классов
    obstacles.create(g);         // Ground, Dino и Obstacles
    dino.create(g);              // (т.е. земли, модели игрока и препятствий)
  }

  // метод потока, в котором описываются процессы, исполняемые потоком
  public void run() {

    running = true;

    while(running) {
      updateGame();    // изменение координат и состояний игры
      repaint();       // перерисовка всех компонентов игры

      // приостановка потока для организации анимации
      try {
        Thread.sleep(50);
      } catch(InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  // метод обновления координат и состояний игры
  public synchronized void updateGame() {
    // увеличение кол-ва набранных очков при смене кадра
    score += 1;

    // обновление координат земли и препятствий с помощью
    // описанных в данных классах методов update()
    ground.update();
    obstacles.update();

    // проверка на столкновение с препятствием
    // если столкновение произошло - изменяет флаги параметров игры
    // и вызывает дополнительную перерисовку для
    // анимации конца игры
    if(obstacles.hasCollided()) {
      dino.die();
      repaint();
      running = false;
      gameOver = true;
      System.out.println("collide");
    }
  }

  // метод начала новой игры при перезапуске клавишей
  public void reset() {

    score = 0;
    System.out.println("reset");
    obstacles.resume();
    gameOver = false;

  }


  public void keyTyped(KeyEvent e) {

    if(e.getKeyChar() == ' ') {
      // перезапуск, если игра была завершена
      if(gameOver)
        reset();

      // запуск потока расчета параметров анимации
      if (animator == null || !running) {
        System.out.println("Game starts");        
        animator = new Thread(this);
        animator.start();
        dino.startRunning();
      }

      // прыжок модели, если не находится в прыжке
      else if (Dino.getState() != 3) {
        dino.jump();
      }
    }
  }
  
  public void keyPressed(KeyEvent e) {
    // System.out.println("keyPressed: "+e);
  }
  
  public void keyReleased(KeyEvent e) {
    // System.out.println("keyReleased: "+e);
  }
}