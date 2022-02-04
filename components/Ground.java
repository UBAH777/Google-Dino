package components;

import utility.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import javax.imageio.ImageIO;



public class Ground {
  
  private class GroundImage {
    BufferedImage image;
    int x;
  }
  
  public static int GROUND_Y;       // координата У земли
  private BufferedImage image;      // общее изображение для всех компонентов-кусков земли
  private ArrayList<GroundImage> groundImageSet;    // множество частей земли

  // Конструктор
  public Ground(int panelHeight) {
    GROUND_Y = (int)(panelHeight - 0.25 * panelHeight);
    
    try{
      image = new Resource().getResourceImage("../images/Ground.png");
    } catch(Exception e) {e.printStackTrace();}
    
    groundImageSet = new ArrayList<GroundImage>();
    
    // Земля будет состоять из трёх отрезков друг за другом (в виде списка)
    for (int i = 0; i < 3; i++) {
      GroundImage obj = new GroundImage();
      obj.image = image;
      obj.x = 0;
      groundImageSet.add(obj);
    }
  }
  
  public synchronized void update() {
    Iterator<GroundImage> looper = groundImageSet.iterator();
    GroundImage first = looper.next();

    first.x -= 10;

    int previousX = first.x;
    while(looper.hasNext()) {
      GroundImage next = looper.next();
      next.x = previousX + image.getWidth();
      previousX = next.x;
    }

    // Если первый элемент земли полностью вышел за границы окна -
    // удаляем его из массива и добавляем в конец
    // с новыми координатами ( previousX последнего + ширина картинки )
    if(first.x < -image.getWidth()) {
      groundImageSet.remove(first);
      first.x = previousX + image.getWidth();
      groundImageSet.add(first);
    }
  }


  public void create(Graphics g) {
		for(GroundImage img : groundImageSet) {
			g.drawImage(img.image, (int) img.x, GROUND_Y, null);
		}
  }
}