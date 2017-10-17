/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;



class Field extends Thread
{
    static final int SNAKE=2;
    static final int END_FIELD=1;
    int speed=200;
    public int points=0;
    public int state=0;
    
    int x=0;
     int y=0;
     int[][] field=null;//new int[100][100];
     
     static MyFrame.MyPanel p=null;
    
     MySnake snake=new MySnake();
     
     boolean isBonus=false;//для яблочек
     int countBonus=0;
     int endBonus=0;
     int xBonus=0;
     int yBonus=0;
      
    public static Image imgApple=null;
     public static Image imgHead=null;
      public static Image imgBall=null;
    
    class MySnake
    {
        int state=0;
       static final int nMove=1;
       static final int sMove=2;
       static final int wMove=3;
       static final int eMove=4;
        ArrayList<Point> body= new ArrayList<Point>();
    }//END CLASS MYSNAKE
    
    private void Bonus()
     {
         while(true)
        {
            endBonus=(int)((Math.random()*50)+10);
            countBonus=0;
            yBonus=(int)(Math.random()*this.y);
            xBonus=(int)(Math.random()*this.x);
            if(field[yBonus][xBonus]==0)
            {
                field[yBonus][xBonus]=4;
                break;
            }
        }
     }//END method BONUS()
     
     public Field(int x,int y,MyFrame.MyPanel p)
     {
         try{
                //File imgFile=new File("C:\\Users\\dd\\Documents\\NetBeansProjects\\Snake\\src\\Resources\\apple.png");
                File fileApple=new File("src/Resources/apple.png");
                imgApple =ImageIO.read(fileApple);
              // File fileHead=new File("src/Resources/head.png");
                 File fileHead=new File("src/Resources/Gomez.png");
                imgHead =ImageIO.read(fileHead);
                
                File fileBall=new File("src/Resources/ball.png");
                imgBall =ImageIO.read(fileBall);
                //img=Resources.getBundle('apple.png');
               //img=ImageIO.read(ImageIcon.class.getResourceAsStream("Resources/apple.png"));
                System.out.println("FILE  HEAD= "+fileHead);
         }
         catch(IOException ioe)
         {
            System.out.println("IMAGEFILE = "+ioe.getMessage());
         }
          
         Field.p=p;
         this.x=x;
         this.y=y;
         field=new int [y][x];
         
     }//END CONSTRUCTOR FIELD
     
     
     public void newGame()
     {
         System.out.println("New GAme");
        // field=new int [y][x];
         for(int i=0;i<y;i++)
         for(int j=0;j<x;j++)
         {
             if(i==0||i==y-1||j==0||j==x-1)
                 field[i][j]=Field.END_FIELD;
             else 
             field[i][j]=0;
         }
         
          field[y/2][x/2]=Field.SNAKE;
          field[y/2+1][x/2]=Field.SNAKE;
          field[y/2+2][x/2]=Field.SNAKE;
          field[y/2+3][x/2]=Field.SNAKE;
          field[y/2+4][x/2]=Field.SNAKE;
          
          snake.body.clear();
          snake.body.add(new Point(x/2,y/2));
          snake.body.add(new Point(x/2,y/2+1));
          snake.body.add(new Point(x/2,y/2+2));
          snake.body.add(new Point(x/2,y/2+3));
          snake.body.add(new Point(x/2,y/2+4));
          isBonus=false;
          snake.state=MySnake.nMove;
          points=0;
          state=1;
          this.speed=200;
    }//END NewGame()
     
     public void show()
     {
         for(int i=0;i<y;i++)
         for(int j=0;j<x;j++)
         {
             System.out.print(field[i][j]);
             if(j==x-1)
                 System.out.print("\n");
         }
     }//End SHOW()
     
     public   void step()// throws InterruptedException synchronized
     {
         synchronized(MyFrame.lock)
         {
             int newX=0;//возможное положение 
             int newY=0;
             int size=0;
             countBonus++;
             if(countBonus==endBonus)
             {
                 
                 field[yBonus][xBonus]=0;
                 xBonus=0;
                 yBonus=0;
                 isBonus=false;
             }
     // System.out.println("GOGO count = "+countBonus+"  End = "+endBonus);
        switch(snake.state)
         {
             case MySnake.nMove:
                 newX=snake.body.get(0).x;
                 newY=snake.body.get(0).y-1;
                 break;
             case MySnake.sMove:
                 newX=snake.body.get(0).x;
                 newY=snake.body.get(0).y+1;
                break;
             case MySnake.wMove:
                  newX=snake.body.get(0).x-1;
                  newY=snake.body.get(0).y;
                 break;
             case MySnake.eMove:
                  newX=snake.body.get(0).x+1;
                  newY=snake.body.get(0).y;
                 break;
         }
         
        if(field[newY][newX]==Field.END_FIELD||field[newY][newX]==Field.SNAKE)
          {
              snake.state=0;
              this.state=2;
              System.out.println("Game Over");
               return;
          }
          size=snake.body.size();
        
         if(field[newY][newX]==0)
         {

             field[newY][newX]=2;
             System.out.println("Y="+snake.body.get(size-1).y);
             field[snake.body.get(size-1).y][snake.body.get(size-1).x]=0;
             snake.body.remove(size-1);
             snake.body.add(0,new Point(newX,newY));
          }
         
         if(field[newY][newX]==4)
         {

             field[newY][newX]=2;
             System.out.println("Y="+snake.body.get(size-1).y);
             field[snake.body.get(size-1).y][snake.body.get(size-1).x]=0;
            // snake.body.remove(size-1);
             snake.body.add(0,new Point(newX,newY));
             points+=(endBonus-countBonus);
             isBonus=false;
             countBonus=0;
           //  System.out.println("New body");
         }
        }
     }
    
     public  void run()//synchronized
     {
         try
         {
             while(true)
             {
                 //System.out.println("GAME STATE "+this.state);
                 if(this.state==1)
                 {
                  step();
                 
                  javax.swing.SwingUtilities.invokeLater(new Runnable()//Код для выполнения в первичном потоке
					{
					 @Override 
                                         public void run()
					 {      Field.p.repaint();
                                          
					 }
					});
                synchronized (this){
                  if(this.state==2)
                 {
                            try
                     {
                       this.wait();
                     }
                     catch(Exception ex)
                     {

                         System.out.println("interrupt"+ex.getMessage());
                     }
                 }
                }
                  if(!isBonus)
                {
                  Bonus();
                  isBonus=true;
                 // countBonus=0;
               
                  if(speed<150)
                  speed-=2;
                  else
                  if(speed<180)
                  speed-=5;
                  else
                  speed-=10;
                }
               
               Thread.sleep(speed);
             }
                 System.out.println("RR-  RUN");
                  Thread.sleep(50);
            }
         }
         catch(InterruptedException ex)
         {
             
         }
     }
}//END CLASS FIELD

class MyFrame extends JFrame
{
    static final Integer lock=0;
    Field f=null;
    boolean start=true;
     
    class MyPanel extends JPanel
    {
        float cell_x=0;
        float cell_y=0;
        @Override
        public void paintComponent(Graphics g)
        {
           System.out.println("PAINT");
           Graphics2D g2d=(Graphics2D)g; 
          AlphaComposite A=AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f);//Прозрачность элементов
          g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);//Сглаживание
          
          float w=this.getWidth();
          float h=this.getHeight();
          
         // g.setColor(Color.getHSBColor(100, 100, 100));
         
          int sizeFont=(int)w/5;
          Font font = new Font("Tahoma", Font.BOLD|Font.ITALIC, sizeFont);
          g.setFont(font);
          if(f.state==0)
          {
              //System.out.println("MyPAnel say "+f.state+"  start = "+start);
               g.setColor(Color.WHITE);
              if(start)
              {
                 
                   g.fillRect(0,0,(int)w,(int) h);
                 // System.out.println("CHANGE");
                  g.setColor(Color.RED);
                  g.fillRect(20,20,(int)(w-40),(int)(h/3));
                  g.setColor(Color.white);
                  g.drawString("PLAY", (int)(w/4),(int)(h/4));//(int)(h/2+(sizeFont/2-sizeFont/4)));
                  
                  
                  g.setColor(Color.GRAY);
                  g.fillRect(20,(int)(h/3)+60,(int)(w-40),(int)(h/3));
                  g.setColor(Color.white);
                  g.drawString("BYE", (int)(w/4),(int)(h/2+h/5));
              }
              else
              {
                  // g.setColor(Color.getHSBColor(100, 100, 100));
                   g.fillRect(0,0,(int)w,(int) h);
                //System.out.println("CHANGE");
                g.setColor(Color.GRAY);
                g.fillRect(20,20,(int)(w-40),(int)(h/3));
                g.setColor(Color.white);
                g.drawString("PLAY", (int)(w/4),(int)(h/4));//(int)(h/2+(sizeFont/2-sizeFont/4)));

                  
                g.setColor(Color.RED);
                g.fillRect(20,(int)(h/3)+60,(int)(w-40),(int)(h/3));
                g.setColor(Color.white);
                g.drawString("BYE", (int)(w/4),(int)(h/2+h/5));
              }
              
              if(f.points!=0)
              {
                  font = new Font("Tahoma", Font.BOLD|Font.ITALIC, sizeFont/4);
                  g.setFont(font);  
                  g.setColor(Color.RED);
                  g.drawString("POINTS : "+f.points, (int)(w/3),(int)(h/2+h/3+20));
              }
              
              return;
            }
            
         //   System.out.println("Width = "+w);
          //  System.out.println("Height = "+h);
             this.cell_x=(w/f.x) ;
            this.cell_y=(h/f.y);
            
             g.setColor(Color.getHSBColor(100, 100, 100));
             g.fillRect(0,0,(int)w,(int) h);
            
            g.setColor(Color.lightGray);
          //  g.fillRect(0,0,w, h);
            float k_x=0,k_y=0;
            synchronized(lock)
            {
              font = new Font("Tahoma", Font.BOLD|Font.ITALIC, sizeFont/4);
              g.setFont(font);  
              g.setColor(Color.RED); 
              g2d.setComposite(A);
              g.drawString(""+f.points,(int)w/10 ,(int)h/10);
              A=AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f);
              g2d.setComposite(A);
            for(int i=0;i<f.y;i++)
            {
                for(int j=0;j<f.x;j++)
                {
                    if(f.field[i][j]==1)
                    {
                        g.setColor(Color.lightGray);
                        g.fillRect((int)k_x,(int)k_y,(int)cell_x+1,(int)cell_y+1); 
                    }
                  
                    
                   if(f.field[i][j]==2)
                   {  
                       if(f.imgHead!=null&&(i==f.snake.body.get(0).y&&j==f.snake.body.get(0).x))
                       {
                       //g.drawImage(f.img,(int)k_x,(int)k_y,null);
                           
                        g2d.drawImage(f.imgHead, (int)k_x-3,(int)k_y-3,(int)cell_x+4,(int)cell_y+4, this);	
                        //continue;
                       }
                       else
                       {
                         g2d.drawImage(f.imgBall, (int)k_x-2,(int)k_y-2,(int)cell_x+2,(int)cell_y+2, this);
                       // g.setColor(Color.GREEN);
                       // g.fillRoundRect((int)k_x,(int)k_y-1,(int)cell_x+1,(int)cell_y+1,(int)cell_x,(int)cell_y); 
                       //g.fillRect((int)k_x,(int)k_y,(int)cell_x+1,(int)cell_y+1); 
                       }                   }
                   if(f.field[i][j]==4)//Рисуем Яблочко
                   { 
                        // g.setColor(Color.RED);
                        // g.fillRoundRect((int)k_x,(int)k_y-1,(int)cell_x+1,(int)cell_y+1,(int)cell_x,(int)cell_y); 
                        // g.fillRect((int)k_x,(int)k_y,(int)cell_x+1,(int)cell_y+1); 
                       if(f.imgApple!=null)
                       {
                       //g.drawImage(f.img,(int)k_x,(int)k_y,null);
                           
                        g2d.drawImage(f.imgApple, (int)k_x-3,(int)k_y-3,(int)cell_x+4,(int)cell_y+4, this);	
                       }
                       else
                           System.out.println("NOT IMAGE");
                   }
                    k_x+=cell_x;
                }
                k_y+=cell_y;
                k_x=0;
            }
            if(f.state==2)
            {
                 g.setColor(Color.RED);
                 g.drawString("GAME OVER",(int)(w/3) ,(int)(h/2-h/5));
                 g.drawString(" PRESS  ANY  KEY",(int)w/4 ,(int)(h/2));
            }
            }
        }
    }//END CLASS MYPANEL
    
  
    public MyFrame()
    {
        MyPanel p=new MyPanel();
        this.setContentPane(p);
        this.setSize(500,500);
        this.setTitle("JAVASNAKE ");
        p.setLayout(new GridLayout());
        p.setBackground(Color.YELLOW);
        
        
        f= new Field(30,30,p);
        f.setDaemon(true);
        f.start();
        
        
         this.addKeyListener(
                 new KeyAdapter()
                 {
                     @Override
                     public void keyReleased(KeyEvent e)// synchronized
                     {
                        //System.out.println(e.getKeyCode());
                         synchronized(lock)
                         {
                             if(f.state==2)
                             {
                                 f.state=0;
                                 p.repaint();
                                 //System.out.println("NEW GAME");
                                 return;
                             }
                            switch(e.getKeyCode())
                            {
                                case 10://ENTER
                                    System.out.println("enter");
                                 if(start&&f.state!=1)
                                 {
                                     //f.state=1;
                                     f.newGame();
                                     synchronized(f){
                                     try
                                     {
                                        f.notify();
                                     }
                                     catch(Exception ex)
                                     {
                                         System.out.println("notify - "+ex.getMessage());
                                     }
                                 }
                                     System.out.println("REPAINT");
                                     p.repaint();
                                     //System.out.println("New Game");
                                 }
                                 else
                                     if(f.state!=1)
                                     System.exit(0);
                                    //if(!f.isAlive())
                                    //f.start();
                                    break;
                             case 37://left
                                 System.out.println("left");
                                 if(f.state!=1)return;
                                 switch(f.snake.state)
                                 {
                                     case Field.MySnake.nMove:
                                         f.snake.state=Field.MySnake.wMove;
                                        break;
                                    case Field.MySnake.sMove:
                                           f.snake.state=Field.MySnake.wMove;
                                       break;
                                    case Field.MySnake.wMove:
                                       // f.snake.state=Field.MySnake.sMove;
                                        break;
                                    case Field.MySnake.eMove:
                                       // f.snake.state=Field.MySnake.nMove;
                                        break;
                                 }
                                 break;
                                 
                             case 38://Up
                                     
                                       //System.out.println("Up");
                                     // System.out.println("UUUPP:"+f.state+" ; "+start);
                                      if(f.state!=1 && start)
                                      {
                                        // System.out.println("new state:"+f.state+" ; "+start);
                                          start=false;
                                         
                                      }
                                      else if(f.state!=1&&!start)
                                      {
                                          start=true;
                                         
                                      }
                                      else
                                      {
                                        switch(f.snake.state)
                                      {
                                     case Field.MySnake.nMove:
                                       //  f.snake.state=Field.MySnake.wMove;
                                        break;
                                    case Field.MySnake.sMove:
                                          // f.snake.state=Field.MySnake.eMove;
                                       break;
                                    case Field.MySnake.wMove:
                                        f.snake.state=Field.MySnake.nMove;
                                        break;
                                    case Field.MySnake.eMove:
                                        f.snake.state=Field.MySnake.nMove;
                                        break;
                                 }
                                      
                                      }
                                break;//END UP
                               
                             case 39://right
                                          System.out.println("rigth");
                                         if(f.state!=1)return;
                                         switch(f.snake.state)
                                        {
                                            case Field.MySnake.nMove:
                                                f.snake.state=Field.MySnake.eMove;
                                               break;
                                           case Field.MySnake.sMove:
                                                  f.snake.state=Field.MySnake.eMove;
                                              break;
                                           case Field.MySnake.wMove:
                                              // f.snake.state=Field.MySnake.nMove;
                                               break;
                                           case Field.MySnake.eMove:
                                              // f.snake.state=Field.MySnake.sMove;
                                               break;
                                        }
                                 break;//END RIGHT
                             
                             case 40://down
                               //System.out.println("down");
                                if(f.state!=1&&start)
                                {
                                   // System.out.println("new state:"+f.state+" ; "+start);
                                    start=false;
                                  
                                }
                                else if(f.state!=1&&!start)
                                {
                                    start=true;
                                }
                                else
                                {
                                      switch(f.snake.state)
                                      {
                                          case Field.MySnake.nMove:
                                             // f.snake.state=Field.MySnake.wMove;
                                             break;
                                         case Field.MySnake.sMove:
                                               // f.snake.state=Field.MySnake.eMove;
                                            break;
                                         case Field.MySnake.wMove:
                                             f.snake.state=Field.MySnake.sMove;
                                             break;
                                         case Field.MySnake.eMove:
                                             f.snake.state=Field.MySnake.sMove;
                                             break;
                                      }
                                 break;
                                }
                         }
                          
                         }
                         
                        
                         if(f.state==1)
                         f.step();
                         p.repaint();
                     }
                 }
         );//Обработка Клавиши
 
      
        this.addWindowListener(
			new WindowAdapter()
			{
			  @Override
			  public void windowClosing(WindowEvent we)
			  {
				System.exit(0);
			  }
			});  
    }//END CONSTRUCTOR MYFRAME

  
}//END CLASS MYFRAME

public class Snake {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //Field f= new Field(50,50);
       // f.show();
        MyFrame F=new MyFrame();
        F.setVisible(true);
    }
    
}
