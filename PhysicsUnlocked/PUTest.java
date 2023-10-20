package PhysicsUnlocked;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PUTest extends JPanel implements ActionListener, KeyListener, MovingCollisionListener
{
   private JFrame frame;
   private PhysicsUnlockedEngine engine;
   private BoundingBox box;
   private BouncyBox[] bouncingBox;
   private BoundingBox launchBox;
   private FollowingBB shield1;
   private FollowingBB shield2;
   private FollowingBB shield3;
   private int tileSizePixels = 25;
   private int timeCounter;
   private double gravity = 20.0;
   private double terminalVelocity = 15;
   private double jump = -12.0;
   private double walkSpeed = 10.0;
   private int collisionIndicationCounter = 0;
   private MovingBoundingObject lastHitBox = null;
   private int inset = tileSizePixels;
   
   private boolean leftHeld;
   private boolean rightHeld;
   private boolean upHeld;
   private boolean downHeld;
   
   // play around with toggling these
   private boolean topDown = true;
   private boolean showTerrainChecked = false;
   private int bouncingBlockCount = 0;
   private boolean bouncingBoxesCollide = false;
   

   public PUTest()
   {
      super();
      engine = new PhysicsUnlockedEngine();
      box = new BoundingBox(.90, .90);
      box.setLoc(6.0, 3.0);
      box.setXMaxSpeed(walkSpeed);
      engine.add(box, PhysicsUnlockedEngine.PLAYER);
      
      bouncingBox = new BouncyBox[bouncingBlockCount];
      for(int i = 0; i < bouncingBox.length; i++)
      {
         bouncingBox[i] = new BouncyBox(.75, .75);
         double x = 12.0 + (i % 20);
         double y = 3.0 + (i / 20);
         bouncingBox[i].setLoc(x, y);
         bouncingBox[i].addCollisionListener(this);
         engine.add(bouncingBox[i], PhysicsUnlockedEngine.ENVIRONMENT);
      }
      GeometryType[][] geometry = getGeometry();
      int floorLevel = 100;
      for(int i = 2; i < geometry[0].length; i++)
      {
         if(geometry[12][i] == GeometryType.FULL)
         {
            floorLevel = i;
            break;
         }
      }
      
      launchBox = new BoundingBox(1.0, .2);
      launchBox.setLoc(12.5, floorLevel - (launchBox.getHeight() / 2));
      launchBox.setAffectedByGravity(false);
      launchBox.setPushedByGeometry(false);
      launchBox.setSpeed(0.0, 0.0);
      launchBox.addCollisionListener(this);
      engine.add(launchBox, PhysicsUnlockedEngine.ENVIRONMENT);
      
      shield1 = new FollowingBB(.25, .25, box);
      shield1.addCollisionListener(this);
      engine.add(shield1, PhysicsUnlockedEngine.ENVIRONMENT);
      shield2 = new FollowingBB(.25, .25, box);
      shield2.addCollisionListener(this);
      engine.add(shield2, PhysicsUnlockedEngine.ENVIRONMENT);
      shield3 = new FollowingBB(.25, .25, box);
      shield3.addCollisionListener(this);
      engine.add(shield3, PhysicsUnlockedEngine.ENVIRONMENT);
      
      if(!topDown)
      {
         box.setDeceleration(walkSpeed * 3, 0.0);
         engine.setGravity(gravity);
   	   engine.setTerminalVelocity(terminalVelocity);
      }
      else
      {
         box.setDeceleration(walkSpeed * 3, walkSpeed * 3);
         box.setYMaxSpeed(walkSpeed);
         box.setAffectedByGravity(false);
      }
      engine.setGeometry(geometry);
      setBackground(Color.BLACK);
   }
   
   public void keyPressed(KeyEvent ke)
   {
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_RIGHT :   rightHeld = true; break;
         case KeyEvent.VK_LEFT :    leftHeld = true; break;
         case KeyEvent.VK_UP :      upHeld = true; break;
         case KeyEvent.VK_DOWN :    downHeld = true; break;
         case KeyEvent.VK_SPACE :   box.applyImpulse(0.0, jump); break;
      }
   }
   
   public void keyReleased(KeyEvent ke)
   {
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_RIGHT :   rightHeld = false; break;
         case KeyEvent.VK_LEFT :    leftHeld = false; break;
         case KeyEvent.VK_UP :      upHeld = false; break;
         case KeyEvent.VK_DOWN :    downHeld = false; break;
      }
   }
   
   public void keyTyped(KeyEvent ke){}
   
   
   public void actionPerformed(ActionEvent ae)
   {
      if(rightHeld)
         box.setXAcceleration(walkSpeed * 3.0);
      else if(leftHeld)
         box.setXAcceleration(-walkSpeed * 3.0);
      else
         box.setXAcceleration(0.0);
      if(topDown)
      {
         if(downHeld)
            box.setYAcceleration(walkSpeed * 3.0);
         else if(upHeld)
            box.setYAcceleration(-walkSpeed * 3.0);
         else
            box.setYAcceleration(0.0);
      }
      this.repaint();
      timeCounter++;
      if(timeCounter > 59)
      {
         timeCounter = 0;
      }
         
      // set shield location
      double rotation = (timeCounter % 20) / 20.0;
      double angle = DoublePair.FULL_CIRCLE - (DoublePair.FULL_CIRCLE * rotation);
      shield1.setRelativeLoc(DoublePair.getFromAngle(angle));
      shield2.setRelativeLoc(DoublePair.getFromAngle(DoublePair.simplifyAngle(angle + (DoublePair.FULL_CIRCLE / 3))));
      shield3.setRelativeLoc(DoublePair.getFromAngle(DoublePair.simplifyAngle(angle + (DoublePair.FULL_CIRCLE * 2 / 3))));
      
      if(collisionIndicationCounter > 0)
         collisionIndicationCounter--;
   }
   
   @Override
   public void paint(Graphics g)
   {
      super.paint(g);
      Graphics2D g2d = (Graphics2D)g;
      
      GeometryType[][] geometry = engine.getGeometry();
      int drawOriginPixelsX;
      int drawOriginPixelsY;
      // geometry
      for(int x = 0; x < geometry.length; x++)
      for(int y = 0; y < geometry[0].length; y++)
      {
         drawOriginPixelsX = x * tileSizePixels + inset;
         drawOriginPixelsY = y * tileSizePixels + inset;
         if(geometry[x][y] == GeometryType.FULL)
         {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(drawOriginPixelsX, drawOriginPixelsY, tileSizePixels, tileSizePixels);
         }
         else if(geometry[x][y] == GeometryType.ASCENDING_FLOOR)
         {
            int[] xPoints = {drawOriginPixelsX, drawOriginPixelsX + tileSizePixels, drawOriginPixelsX + tileSizePixels};
            int[] yPoints = {drawOriginPixelsY + tileSizePixels, drawOriginPixelsY + tileSizePixels, drawOriginPixelsY};
            g2d.setColor(Color.WHITE);
            g2d.fillPolygon(xPoints, yPoints, 3);
         }
         else if(geometry[x][y] == GeometryType.DESCENDING_FLOOR)
         {
            int[] xPoints = {drawOriginPixelsX, drawOriginPixelsX, drawOriginPixelsX + tileSizePixels};
            int[] yPoints = {drawOriginPixelsY, drawOriginPixelsY + tileSizePixels, drawOriginPixelsY + tileSizePixels};
            g2d.setColor(Color.WHITE);
            g2d.fillPolygon(xPoints, yPoints, 3);
         }
         else if(geometry[x][y] == GeometryType.DESCENDING_CEILING)
         {
            int[] xPoints = {drawOriginPixelsX, drawOriginPixelsX + tileSizePixels, drawOriginPixelsX + tileSizePixels};
            int[] yPoints = {drawOriginPixelsY, drawOriginPixelsY, drawOriginPixelsY + tileSizePixels};
            g2d.setColor(Color.WHITE);
            g2d.fillPolygon(xPoints, yPoints, 3);
         }
         else if(geometry[x][y] == GeometryType.ASCENDING_CEILING)
         {
            int[] xPoints = {drawOriginPixelsX, drawOriginPixelsX, drawOriginPixelsX + tileSizePixels};
            int[] yPoints = {drawOriginPixelsY, drawOriginPixelsY + tileSizePixels, drawOriginPixelsY};
            g2d.setColor(Color.WHITE);
            g2d.fillPolygon(xPoints, yPoints, 3);
         }
         else // empty
         {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(drawOriginPixelsX, drawOriginPixelsY, tileSizePixels, tileSizePixels);
         }
      }
      
      // terrain checked
      int[] geoColCheckOrigin = box.getPotentialCollisionOrigin(.01);
      int[] geoColCheckEnd = box.getPotentialCollisionEnd(.01);
      g2d.setColor(Color.GRAY);
      int checked = 0;
      for(int x = geoColCheckOrigin[0]; x <= geoColCheckEnd[0]; x++)
      for(int y = geoColCheckOrigin[1]; y <= geoColCheckEnd[1]; y++)
      {
         checked++;
         if(showTerrainChecked)
            g2d.fillRect(x * tileSizePixels + inset, y * tileSizePixels + inset, tileSizePixels, tileSizePixels);
      }
      
      // grid
      g2d.setColor(Color.CYAN);
      int maxWidth = geometry.length * tileSizePixels;
      int maxHeight = geometry[0].length * tileSizePixels;
      for(int x = 0; x < geometry.length + 1; x++)
      {
         g2d.drawLine(x * tileSizePixels + inset, 0 + inset, x * tileSizePixels + inset, maxHeight + inset);
      }
      for(int y = 0; y < geometry[0].length + 1; y++)
      {
         g2d.drawLine(0 + inset, y * tileSizePixels + inset, maxWidth + inset, y * tileSizePixels + inset);
      }
      
      // text
      g2d.setColor(Color.ORANGE);
      g2d.drawString("CPS: " + engine.getCPS(), 5 + inset, maxHeight + 15 + inset);
      g2d.drawString("Position: " + box.getLoc().serialize(), 5 + inset, maxHeight + 30 + inset);
      g2d.drawString("Acceleration: " + box.getAcceleration().serialize(), 5 + inset, maxHeight + 45 + inset);
      g2d.drawString("Speed: " + box.getSpeed().serialize(), 5 + inset, maxHeight + 60 + inset);
      g2d.drawString("Touching down: " + engine.touchingFloor(box), 200 + inset, maxHeight + 15 + inset);
      g2d.drawString("Touching left: " + engine.touchingLeftWall(box), 200 + inset, maxHeight + 30 + inset);
      g2d.drawString("Touching right: " + engine.touchingRightWall(box), 200 + inset, maxHeight + 45 + inset);
      g2d.drawString("Touching up: " + engine.touchingCeiling(box), 200 + inset, maxHeight + 60 + inset);
      g2d.drawString("Tiles Checked: " + checked, 400 + inset, maxHeight + 15 + inset);
      g2d.drawString("Tiles Check Origin: " + geoColCheckOrigin[0] + ", " + geoColCheckOrigin[1], 400 + inset, maxHeight + 30 + inset);
      g2d.drawString("Tiles Check End: " + geoColCheckEnd[0] + ", " + geoColCheckEnd[1], 400 + inset, maxHeight + 45 + inset);
      
      // objects
      int x;
      int y;
      int width;
      int height;
      for(BoundingBox bBox : bouncingBox)
      {
         x = bBox.getDrawOriginX(tileSizePixels) + inset;
         y = bBox.getDrawOriginY(tileSizePixels) + inset;
         width = (int)(bBox.getWidth() * tileSizePixels);
         height = (int)(bBox.getHeight() * tileSizePixels);
         if(collisionIndicationCounter > 0 && bBox == lastHitBox)
            g2d.setColor(Color.BLUE);
         else
            g2d.setColor(Color.GREEN);
         g2d.fillRect(x, y, width, height);
      }
      
      // launch box
      x = launchBox.getDrawOriginX(tileSizePixels) + inset;
      y = launchBox.getDrawOriginY(tileSizePixels) + inset;
      width = (int)(launchBox.getWidth() * tileSizePixels);
      height = (int)(launchBox.getHeight() * tileSizePixels);
      g2d.setColor(Color.YELLOW);
      g2d.fillRect(x, y, width, height);
      
      // shields
      x = shield1.getDrawOriginX(tileSizePixels) + inset;
      y = shield1.getDrawOriginY(tileSizePixels) + inset;
      width = (int)(shield1.getWidth() * tileSizePixels);
      height = (int)(shield1.getHeight() * tileSizePixels);
      g2d.setColor(Color.ORANGE);
      g2d.fillRect(x, y, width, height);
      x = shield2.getDrawOriginX(tileSizePixels) + inset;
      y = shield2.getDrawOriginY(tileSizePixels) + inset;
      g2d.fillRect(x, y, width, height);
      x = shield3.getDrawOriginX(tileSizePixels) + inset;
      y = shield3.getDrawOriginY(tileSizePixels) + inset;
      g2d.fillRect(x, y, width, height);
      
      // player last to be in front
      x = box.getDrawOriginX(tileSizePixels) + inset;
      y = box.getDrawOriginY(tileSizePixels) + inset;
      width = (int)(box.getWidth() * tileSizePixels);
      height = (int)(box.getHeight() * tileSizePixels);
      if(collisionIndicationCounter > 0)
         g2d.setColor(Color.RED);
      else
         g2d.setColor(Color.ORANGE);
      g2d.fillRect(x, y, width, height);
      
   }
   
   public GeometryType[][] getGeometry()
   {
      int width = 40;
      int height = 30;
      GeometryType[][] geometry = new GeometryType[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         geometry[x][y] = GeometryType.EMPTY;
      for(int x = 0; x < width; x++)
      {
         geometry[x][0] = GeometryType.FULL;
         geometry[x][height - 1] = GeometryType.FULL;
      }
      for(int y = 0; y < height - 1; y++)
      {
         geometry[0][y] = GeometryType.FULL;
         geometry[geometry.length - 1][y] = GeometryType.FULL;
      }
      
      for(int i = 0; i < 5; i++)
      {
         geometry[2][geometry[0].length - 4 - (i * 4)] = GeometryType.FULL;
         geometry[3][geometry[0].length - 4 - (i * 4)] = GeometryType.FULL;
         geometry[6][geometry[0].length - 5 - (i * 4)] = GeometryType.FULL;
         geometry[7][geometry[0].length - 5 - (i * 4)] = GeometryType.FULL;
      }
      
      // slope playground
      int xStart = width / 2;
      int y = geometry[0].length - 2;
      geometry[xStart][y] = GeometryType.ASCENDING_FLOOR;
      geometry[xStart + 1][y] = GeometryType.FULL;
      geometry[xStart + 2][y] = GeometryType.FULL;
      geometry[xStart + 3][y] = GeometryType.DESCENDING_FLOOR;
      
      geometry[xStart][y - 4] = GeometryType.FULL;
      geometry[xStart + 1][y - 4] = GeometryType.FULL;
      geometry[xStart + 2][y - 4] = GeometryType.FULL;
      geometry[xStart + 3][y - 4] = GeometryType.FULL;
      
      geometry[xStart][y - 7] = GeometryType.DESCENDING_CEILING;
      geometry[xStart + 1][y - 7] = GeometryType.FULL;
      geometry[xStart + 2][y - 7] = GeometryType.FULL;
      geometry[xStart + 3][y - 7] = GeometryType.ASCENDING_CEILING;
      geometry[xStart][y - 8] = GeometryType.ASCENDING_FLOOR;
      geometry[xStart + 1][y - 8] = GeometryType.FULL;
      geometry[xStart + 2][y - 8] = GeometryType.FULL;
      geometry[xStart + 3][y - 8] = GeometryType.DESCENDING_FLOOR;
      
      geometry[10][5] = GeometryType.ASCENDING_FLOOR;
      geometry[11][5] = GeometryType.DESCENDING_FLOOR;
      geometry[10][6] = GeometryType.DESCENDING_CEILING;
      geometry[11][6] = GeometryType.ASCENDING_CEILING;
      
      
      xStart += 6;
      geometry[xStart][y] = GeometryType.ASCENDING_FLOOR;
      geometry[xStart + 1][y] = GeometryType.FULL;
      geometry[xStart + 1][y - 1] = GeometryType.FULL;
      
      return geometry;
   }
   
   public void movingCollisionOccured(MovingCollision mc)
   {
      MovingBoundingObject obj = mc.getSource();
      MovingBoundingObject subj = mc.getOtherObject();
      
      // bouncing boxes
      if(subj == null)
      {
         DoublePair normalPair = engine.getOrthoGeometryCollisionNormals(obj);
         if(normalPair.x != 0.0)
            obj.setXSpeed(Math.abs(obj.getXSpeed()) * normalPair.x);
         if(normalPair.y != 0.0)
            obj.setYSpeed(Math.abs(obj.getYSpeed()) * normalPair.y);
      }
      else
      {
         if(subj == box)
         {
            collisionIndicationCounter = 10;
            lastHitBox = obj;
         }
         else if(bouncingBoxesCollide && obj instanceof BouncyBox && subj instanceof BouncyBox)
         {
            // we only need to handle one, because two collisions are generated
            double speed = subj.getSpeed().getMagnitude();
            DoublePair diff = DoublePair.difference(subj.getLoc(), obj.getLoc());
            subj.setSpeed(DoublePair.getFromAngle(diff.getAngle()));
            subj.setXSpeed(subj.getXSpeed() * speed);
            subj.setYSpeed(subj.getYSpeed() * speed);
         }
      }
      
      // launch box
      if(obj == launchBox && subj == box)
      {
         box.setYSpeed(-20.0);
      }
      
      // shields
      if(obj == shield1 || obj == shield2 || obj == shield3)
      {
         if(subj != null)
         {
            double speed = subj.getSpeed().getMagnitude();
            DoublePair diff = DoublePair.difference(subj.getLoc(), box.getLoc());
            subj.setSpeed(DoublePair.getFromAngle(diff.getAngle()));
            subj.setXSpeed(subj.getXSpeed() * speed);
            subj.setYSpeed(subj.getYSpeed() * speed);
         }
      }
   }
   
   public static void main(String[] args)
   {
      JFrame frame = new JFrame();
      frame.setSize(1200, 900);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      PUTest testPanel = new PUTest();
      frame.add(testPanel);
      
      frame.setVisible(true);
      frame.addKeyListener(testPanel);
      
      javax.swing.Timer timer = new javax.swing.Timer(1000 / 60, testPanel);
      timer.start();
      
      testPanel.engine.setRunFlag(true);
   }
   
   private class BouncyBox extends BoundingBox
   {
      public BouncyBox(double w, double h)
      {
         super(w, h);
         setAffectedByGravity(false);
         setPushedByGeometry(false);
         setSpeed(5.0 + (Math.random() * 5.0), 5.0 + (Math.random() * 5.0));
      }
   }
}