package components;

import utility.Resource;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Dino {

  private static int dinoBaseY,     // нижняя координата Y (ноги)
                     dinoTopY,      // верхняя координата Y (мокушка)
                     dinoStartX,    // координата Х динозавра
                     dinoEndX;      // координата Х правой точки динозавра

  private static int dinoTop,
                     dinoBottom,
                     topPoint;      // верхняя точка прыжка

  private static boolean topPointReached;   // флаг достижения верхней точки прыжка

  private static int jumpFactor = 20;       // "сила" прыжка


  public static final int STAND_STILL = 1,   // стоит на месте
                          RUNNING = 2,       // бежит
                          JUMPING = 3,       // находится в прыжке
                          DIE = 4;           // погиб


  private final int LEFT_FOOT = 1,          // 1 - стоит на левой
                    RIGHT_FOOT = 2,         // 2 - стоит на правой
                    NO_FOOT = 3;            // 3 - нога не выбрана

  public static int getState() {
    return state;
  }

  public static int state;    // параметр состояния динозавра ( Стоит, Бежит, Прыгает, Умер )

  private int foot;            // на какой ноге стоит

  static BufferedImage image;
  BufferedImage leftFootDino;
  BufferedImage rightFootDino;
  BufferedImage deadDino;


  public Dino() {

    // изображения модельки в разных положениях
    image = new Resource().getResourceImage("../images/Dino-stand.png");
    leftFootDino = new Resource().getResourceImage("../images/Dino-left-up.png");
    rightFootDino = new Resource().getResourceImage("../images/Dino-right-up.png");
    deadDino = new Resource().getResourceImage("../images/Dino-big-eyes.png");

    dinoBaseY = Ground.GROUND_Y + 5;                      // нижняя координата Y (ноги)
    dinoTopY = Ground.GROUND_Y - image.getHeight() + 5;   // верхняя координата Y (мокушка)
    dinoStartX = 100;                                     // координата Х динозавра
    dinoEndX = dinoStartX + image.getWidth();             // координата Х правой точки динозавра
    topPoint = dinoTopY - 120;                            // верхняя граница прыжка

    state = 1;          // состояние: вначале стоит на месте
    foot = NO_FOOT;     // нога не выбрана
  }




  public void create(Graphics g) {
    dinoBottom = dinoTop + image.getHeight();

    // g.drawLine(0, topPoint, 800, topPoint);  // - верхняя линия прыжка
    // g.drawRect(getDino().x, getDino().y, getDino().width, getDino().height);   // - хитбокс

    switch(state) {

      case STAND_STILL:
        System.out.println("stand");
        g.drawImage(image, dinoStartX, dinoTopY, null);
        break;

      case RUNNING:
        if(foot == NO_FOOT) {
          foot = LEFT_FOOT;
          g.drawImage(leftFootDino, dinoStartX, dinoTopY, null);
        } else if(foot == LEFT_FOOT) {
          foot = RIGHT_FOOT;
          g.drawImage(rightFootDino, dinoStartX, dinoTopY, null);
        } else {
          foot = LEFT_FOOT;
          g.drawImage(leftFootDino, dinoStartX, dinoTopY, null);
        }
        break;

      case JUMPING:
        if(dinoTop > topPoint && !topPointReached) {
          g.drawImage(image, dinoStartX, dinoTop -= jumpFactor, null);
          break;
        } 
        if(dinoTop >= topPoint && !topPointReached) {
          topPointReached = true;
          g.drawImage(image, dinoStartX, dinoTop += jumpFactor, null);
          break;
        }         
        if(dinoTop > topPoint && topPointReached) {      
          if(dinoTopY == dinoTop && topPointReached) {
            state = RUNNING;
            topPointReached = false;
            break;
          }    
          g.drawImage(image, dinoStartX, dinoTop += jumpFactor, null);          
          break;
        }
      case DIE: 
        g.drawImage(deadDino, dinoStartX, dinoTop, null);    
        break;     
    }
  }

  public void die() {
    state = DIE;
  }

  public static Rectangle getDino() {
    Rectangle dino = new Rectangle();
    dino.x = dinoStartX + 4;    // настройка хитбокса

    if(state == JUMPING && !topPointReached)
      dino.y = dinoTop - jumpFactor;
    else if(state == JUMPING && topPointReached)
      dino.y = dinoTop + jumpFactor;
    else if(state != JUMPING)
      dino.y = dinoTop;

    dino.width = image.getWidth() - 18;    // настройка хитбокса
    dino.height = image.getHeight() - 9;    // настройка хитбокса

    return dino;
  }

  public void startRunning() {
    dinoTop = dinoTopY;
    state = RUNNING;
  }

  public void jump() {
    dinoTop = dinoTopY;
    topPointReached = false;
    state = JUMPING;
  }

  private class DinoImages {

  }

}