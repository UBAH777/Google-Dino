package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import utility.*;

public class Obstacles {

  private class Obstacle {
    BufferedImage image;
    int x;
    int y;

    Rectangle getObstacle() {

      //TODO настройка хитбокса
      Rectangle obstacle = new Rectangle();
      obstacle.x = x + 5;
      obstacle.y = y + 6;
      obstacle.width = image.getWidth() - 10;
      obstacle.height = image.getHeight() - 6;

      return obstacle;
    }
  }
  
  private int firstX;
  private int obstacleInterval;
  private int movementSpeed;
  
  private ArrayList<BufferedImage> imageList;
  private ArrayList<Obstacle> obList;

  private Obstacle blockedAt;
  
  // imageList.add(new Resource().getResourceImage("../images/Cactus-3.png"));     другие кактусы
  // imageList.add(new Resource().getResourceImage("../images/Cactus-4.png"));
  public Obstacles(int firstPos) {
    obList = new ArrayList<Obstacle>();
    imageList = new ArrayList<BufferedImage>();
    
    firstX = firstPos;
    obstacleInterval = 400;
    movementSpeed = 11;
    
    imageList.add(new Resource().getResourceImage("../images/Cactus-1.png"));
    imageList.add(new Resource().getResourceImage("../images/Cactus-2.png"));
    imageList.add(new Resource().getResourceImage("../images/Cactus-2.png"));
    imageList.add(new Resource().getResourceImage("../images/Cactus-1.png"));

    imageList.add(new Resource().getResourceImage("../images/Cactus-5.png"));
    
    int x = firstX;
    
    for(BufferedImage bi : imageList) {
      
      Obstacle ob = new Obstacle();
      
      ob.image = bi;
      ob.x = x;
      ob.y = Ground.GROUND_Y - bi.getHeight() + 5;
      x += obstacleInterval;
      
      obList.add(ob);
    }
  }
  
  public synchronized void update() {
    Iterator<Obstacle> looper = obList.iterator();
    
    Obstacle firstOb = looper.next();
    firstOb.x -= movementSpeed;
    
    while(looper.hasNext()) {
      Obstacle ob = looper.next();
      ob.x -= movementSpeed;
    }

    // чтобы не придумывать новый кактус, просто закинем удаленный первый в конец
    // но с новой координатой ( (новый кактус).х + интервал )
    if(firstOb.x < -firstOb.image.getWidth()) {
      obList.remove(firstOb);
      firstOb.x = obList.get(obList.size() - 1).x + obstacleInterval;
      obList.add(firstOb);
    }
  }

//g.drawRect(ob.getObstacle().x, ob.getObstacle().y, ob.getObstacle().width, ob.getObstacle().height);

  public void create(Graphics g) {
    for(Obstacle ob : obList) {
      g.setColor(Color.black);
      g.drawImage(ob.image, ob.x, ob.y, null);
    }
  }
  
  public boolean hasCollided() {
    for(Obstacle ob : obList) {
      if(Dino.getDino().intersects(ob.getObstacle())) {
        //System.out.println("Dino = " + Dino.getDino() + "\nObstacle = " + ob.getObstacle() + "\n\n");
        //blockedAt = ob;
        return true;
      }   
    }
    return false;
  }

  public void resume() {
    // this.obList = null;
    int x = firstX;                         // начинаем новую игру
    obList = new ArrayList<Obstacle>();
    
    for(BufferedImage bi : imageList) {
      
      Obstacle ob = new Obstacle();
      
      ob.image = bi;
      ob.x = x;
      ob.y = Ground.GROUND_Y - bi.getHeight() + 5;
      x += obstacleInterval;
      
      obList.add(ob);
    }
  }
  
}