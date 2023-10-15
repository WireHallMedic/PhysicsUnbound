package PhysicsUnlocked;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PUTest extends JPanel implements ActionListener, KeyListener, MovingCollisionListener
{
   private JFrame frame;
   private PhysicsUnlockedEngine engine;
   private BoundingBox box;
   private BoundingBox[] bouncingBox;
   private BoundingBox launchBox;
   private FollowingBB shield1;
   private FollowingBB shield2;
   private int tileSizePixels = 25;
   private int timeCounter;
   private double gravity = 20.0;
   private double terminalVelocity = 15;
   private double jump = -12.0;
   private double walkSpeed = 10.0;
   private int collisionIndicationCounter = 0;
   private MovingBoundingObject lastHitBox = null;
   
   private boolean leftHeld;
   private boolean rightHeld;
   private boolean upHeld;
   private boolean downHeld;
   private boolean topDown = false;
   

   public PUTest()
   {
      super();
      engine = new PhysicsUnlockedEngine();
      box = new BoundingBox(.95, .95);
      box.setLoc(6.0, 3.0);
      box.setXMaxSpeed(walkSpeed);
      engine.add(box, PhysicsUnlockedEngine.PLAYER);
      
      bouncingBox = new BoundingBox[100];
      for(int i = 0; i < bouncingBox.length; i++)
      {
         bouncingBox[i] = new BoundingBox(.75, .75);
         double x = 12.0 + (i % 20);
         double y = 3.0 + (i / 20);
         bouncingBox[i].setLoc(x, y);
         bouncingBox[i].setAffectedByGravity(false);
         bouncingBox[i].setPushedByGeometry(false);
         bouncingBox[i].setSpeed(5.0 + (Math.random() * 5.0), 5.0 + (Math.random() * 5.0));
         bouncingBox[i].addCollisionListener(this);
         engine.add(bouncingBox[i], PhysicsUnlockedEngine.ENEMY);
      }
      boolean[][] geometry = getGeometry();
      int floorLevel = 100;
      for(int i = 2; i < geometry[0].length; i++)
      {
         if(geometry[12][i])
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
      shield1.setLoc(0.0, -1.0);
      shield1.addCollisionListener(this);
      engine.add(shield1, PhysicsUnlockedEngine.ENVIRONMENT);
      shield2 = new FollowingBB(.25, .25, box);
      shield2.setLoc(0.0, 1.0);
      shield2.addCollisionListener(this);
      engine.add(shield2, PhysicsUnlockedEngine.ENVIRONMENT);
      
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
      double rotation = (timeCounter % 30) / 30.0;
      double angle = DoublePair.FULL_CIRCLE - (DoublePair.FULL_CIRCLE * rotation);
      shield1.setRelativeLoc(DoublePair.getFromAngle(angle));
      shield2.setRelativeLoc(DoublePair.getFromAngle(DoublePair.simplifyAngle(angle + DoublePair.HALF_CIRCLE)));
      
      if(collisionIndicationCounter > 0)
         collisionIndicationCounter--;
   }
   
   @Override
   public void paint(Graphics g)
   {
      super.paint(g);
      Graphics2D g2d = (Graphics2D)g;
      
      boolean[][] geometry = engine.getGeometry();
      
      // geometry
      for(int x = 0; x < geometry.length; x++)
      for(int y = 0; y < geometry[0].length; y++)
      {
         if(geometry[x][y])
         {
            g2d.setColor(Color.WHITE);
         }
         else
         {
            g2d.setColor(Color.BLACK);
         }
         g2d.fillRect(x * tileSizePixels, y * tileSizePixels, tileSizePixels, tileSizePixels);
      }
      
      // grid
      g2d.setColor(Color.CYAN);
      int maxWidth = geometry.length * tileSizePixels;
      int maxHeight = geometry[0].length * tileSizePixels;
      for(int x = 0; x < geometry.length + 1; x++)
      {
         g2d.drawLine(x * tileSizePixels, 0, x * tileSizePixels, maxHeight);
      }
      for(int y = 0; y < geometry[0].length + 1; y++)
      {
         g2d.drawLine(0, y * tileSizePixels, maxWidth, y * tileSizePixels);
      }
      
      // objects
      
      g2d.setColor(Color.ORANGE);
      g2d.drawString("CPS: " + engine.getCPS(), 5, maxHeight + 15);
      g2d.drawString("Position: " + box.getLoc().serialize(), 5, maxHeight + 30);
      g2d.drawString("Acceleration: " + box.getAcceleration().serialize(), 5, maxHeight + 45);
      g2d.drawString("Speed: " + box.getSpeed().serialize(), 5, maxHeight + 60);
      g2d.drawString("On ground: " + engine.touchingFloor(box), 200, maxHeight + 15);
      g2d.drawString("Touching left: " + engine.touchingLeftWall(box), 200, maxHeight + 30);
      g2d.drawString("Touching right: " + engine.touchingRightWall(box), 200, maxHeight + 45);
      
      
      int x;
      int y;
      int width;
      int height;
      for(BoundingBox bBox : bouncingBox)
      {
         x = (int)(bBox.getDrawOriginX() * tileSizePixels);
         y = (int)(bBox.getDrawOriginY() * tileSizePixels);
         width = (int)(bBox.getWidth() * tileSizePixels);
         height = (int)(bBox.getHeight() * tileSizePixels);
         if(collisionIndicationCounter > 0 && bBox == lastHitBox)
            g2d.setColor(Color.BLUE);
         else
            g2d.setColor(Color.GREEN);
         g2d.fillRect(x, y, width, height);
         g2d.setColor(Color.BLACK);
         g2d.drawRect(x, y, width, height);
      }
      
      // launch box
      x = (int)(launchBox.getDrawOriginX() * tileSizePixels);
      y = (int)(launchBox.getDrawOriginY() * tileSizePixels);
      width = (int)(launchBox.getWidth() * tileSizePixels);
      height = (int)(launchBox.getHeight() * tileSizePixels);
      g2d.setColor(Color.YELLOW);
      g2d.fillRect(x, y, width, height);
      g2d.setColor(Color.BLACK);
      g2d.drawRect(x, y, width, height);
      
      // shield
      x = (int)(shield1.getDrawOriginX() * tileSizePixels);
      y = (int)(shield1.getDrawOriginY() * tileSizePixels);
      width = (int)(shield1.getWidth() * tileSizePixels);
      height = (int)(shield1.getHeight() * tileSizePixels);
      g2d.setColor(Color.ORANGE);
      g2d.fillRect(x, y, width, height);
      g2d.setColor(Color.BLACK);
      g2d.drawRect(x, y, width, height);
      x = (int)(shield2.getDrawOriginX() * tileSizePixels);
      y = (int)(shield2.getDrawOriginY() * tileSizePixels);
      g2d.setColor(Color.ORANGE);
      g2d.fillRect(x, y, width, height);
      g2d.setColor(Color.BLACK);
      g2d.drawRect(x, y, width, height);
      
      // player last to be in front
      x = (int)(box.getDrawOriginX() * tileSizePixels);
      y = (int)(box.getDrawOriginY() * tileSizePixels);
      width = (int)(box.getWidth() * tileSizePixels);
      height = (int)(box.getHeight() * tileSizePixels);
      if(collisionIndicationCounter > 0)
         g2d.setColor(Color.RED);
      else
         g2d.setColor(Color.ORANGE);
      g2d.fillRect(x, y, width, height);
      g2d.setColor(Color.BLACK);
      g2d.drawRect(x, y, width, height);
      
   }
   
   public boolean[][] getGeometry()
   {
      int width = 40;
      int height = 30;
      boolean[][] geometry = new boolean[width][height];
      for(int x = 0; x < width; x++)
      {
         geometry[x][0] = true;
         geometry[x][height - 2] = true;
      }
      for(int y = 0; y < height - 1; y++)
      {
         geometry[0][y] = true;
         geometry[geometry.length - 1][y] = true;
      }
      
      for(int i = 0; i < 5; i++)
      {
         geometry[2][geometry[0].length - 5 - (i * 4)] = true;
         geometry[3][geometry[0].length - 5 - (i * 4)] = true;
         geometry[6][geometry[0].length - 7 - (i * 4)] = true;
         geometry[7][geometry[0].length - 7 - (i * 4)] = true;
      }
      return geometry;
   }
   
   public void movingCollisionOccured(MovingCollision mc)
   {
      MovingBoundingObject obj = mc.getSource();
      MovingBoundingObject subj = mc.getMovingBoundingObject();
      
      // bouncing boxes
      DoublePair normalPair = engine.getOrthoGeometryCollisionNormals(obj);
      if(normalPair.x != 0.0)
         obj.setXSpeed(Math.abs(obj.getXSpeed()) * normalPair.x);
      if(normalPair.y != 0.0)
         obj.setYSpeed(Math.abs(obj.getYSpeed()) * normalPair.y);
      if(subj == box)
      {
         collisionIndicationCounter = 10;
         lastHitBox = obj;
      }
      
      // launch box
      if(obj == launchBox && subj == box)
      {
         box.setYSpeed(-20.0);
      }
      
      // shields
      if(obj == shield1 || obj == shield2)
      {
         if(subj != null)
         {
            double xDist = Math.abs(obj.getXLoc() - subj.getXLoc());
            double yDist = Math.abs(obj.getYLoc() - subj.getYLoc());
            
            if(obj.getYLoc() - subj.getYLoc() < 0.0)  // subject is below
               subj.setYSpeed(Math.abs(subj.getYSpeed()));
            else  // subject is above
               subj.setYSpeed(Math.abs(subj.getYSpeed()) * -1);
               
            if(obj.getXLoc() - subj.getXLoc() < 0.0)  // subject is right
               subj.setXSpeed(Math.abs(subj.getXSpeed()));
            else  // subject is left
               subj.setXSpeed(Math.abs(subj.getXSpeed()) * -1);
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
}