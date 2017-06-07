import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class JumpAreaPanel extends JPanel implements Runnable 
{
  private int x,y,i,j;//宣告變數 滑鼠點選位置(x y)
  private String tool;//宣告選取功能時的變數
  private Ball ball;
  private JLabel ballcount;
  private JumpAreaPanel board;
  private Vector v=new Vector();//宣告一Vector來存放球的控制權
  boolean stop=false, stopall=false;//宣告一boolean值 暫停，暫停全部
  private boolean collisionMode,collisionSmallMode,combineMode;//碰撞 結合 變小mode的boolean值
  private int ballspeed = 40;//畫面重畫的speed
  
  public JumpAreaPanel(JLabel label)
  {
	  board =  this;
	  tool = "addball";//預設為addball
	  ballcount = label;//球數
	  
	  addMouseListener(
			  new MouseAdapter()
			  {
				  public synchronized void mouseReleased(MouseEvent event)
				  {
					  x = event.getX();//取的現在滑鼠點選的位置
					  y = event.getY();
					  
					  if(tool=="addball")
					  {
						  Ball balls=new Ball(x,y,board);
					      v.add(balls);//把球加入vector中
						  balls.start();//開始球的run
						  ballcount.setText("球數 :"+v.size());
						  stopall=false;
					  }		  
				     else if(tool=="stopchoose")
				     {
					  ball=getBallSelect(x,y,v);//選取ball
					  if(ball != null)
					  {
						  if(ball.getStopState()==false)
							  stopBall(ball);
						  else
							  startBall(ball);
					  }
				  }
				  else if(tool=="delchoose")
				  {
					  ball=getBallSelect(x,y,v);
					  v.removeElement(ball);//從vector中移除選取的Ball
					  ballcount.setText("球數 : "+v.size());
				  }
			     }//end mouseReleased
            }//end inner class
	 );
			  
  }//end JumpAreaPanel()
  
  public void paintComponent(Graphics g)
  {
	  super.paintComponent(g);
	  for(int i=0;i < v.size();i++)
	  {
		ball = (Ball)v.elementAt(i); // vector 內的每一顆球
		if(ball != null)
		{
			ball.draw(g);//畫球
		}
	  }
  }//paintComponent()
  
  public void changeSpeed(int i)
  {
  	ballspeed += i;
  	
  	if( ballspeed <= 1 )
  		ballspeed = 1;
  	else if(ballspeed > 1500)
  		ballspeed = 1500;
  }

public void run() // thread
{
  	while(true)
  	{ 
  		if(collisionMode == true)
		{
			Collision();
		}
  		
  		if(combineMode == true)
		{
			Combinsion();
		}
  		
       	try
    		{
          	Thread.sleep(ballspeed); // put thread to sleep
	     	}
			// if exception occur, print stack trace
	     	catch(Exception exception)
	     	{
				exception.printStackTrace();	     		
	     	}
       	repaint(); // 畫每一個 thread 的球	
    	}	//end while
	}//end run

 public int getspeed()
 {
	return ballspeed;
 }
 
  public void set( String chose ) // 目前的按鈕狀態
	{
		tool = chose;
	}//end set()
  
  public void stopAll()
  {
	  if(stopall== false)
		  stopAllBall(v);
	  else 
		  startAllBall(v);
	  tool=null;
  }//end stopAll()
  
  public void rmallball() // 移除全部球
 	{
 		v.removeAllElements(); // 移除掉所有球
 		ballcount.setText( "球數 :"+v.size()); // 系統訊息(球數)
 		tool = null; 
 	}
  
  public void stopBall( Ball ball ) // 停止球
	{ 
		ball.setStopState( true );
	}//end stopBall()

	private void startBall( Ball ball ) // 啟動球
	{
		ball.setStopState( false ); 
		ball.reStart();	// 重新啟動球	
		stopall = false;
	}
  
	public void setCollisionMode(boolean c)
	{
		this.collisionMode = c;
	}
	
	public void setCollisionSmallMode(boolean c)
	{
		this.collisionSmallMode = c;
	}
	
	public void setCombineMode(boolean c)
	{
		this.combineMode = c;
	}
  
  public void Combinsion()
  {
		for(i=0;i<v.size();i++)//i控制第一顆球
		{  		
			for(j=0;j<i;j++)//J控制第二顆球
			{
				if(v.elementAt(i).equals(v.elementAt(j)))//如果第一顆球等於第二顆球
				{	
					continue;
				}
				else if(
						((Ball)v.elementAt(i)).curPosition().distance(((Ball) v.elementAt(j)).curPosition()) >=  ((Ball) v.elementAt(i)).nextPosition().distance(((Ball) v.elementAt(j)).nextPosition())-4
						&&
						((Ball)v.elementAt(i)).curPosition().distance(((Ball) v.elementAt(j)).curPosition())-4 <= (((Ball) v.elementAt(i)).getBallR() + ((Ball) v.elementAt(j)).getBallR())/2 
					    )//判斷碰撞
		        {   	
					double tempX =  ((Ball) v.elementAt(i)).getXDir();
					double tempY =  ((Ball) v.elementAt(i)).getYDir();
					((Ball) v.elementAt(j)).setDirection(tempX,tempY);//重設第二顆球的方向，第一顆球方向不變
		        }
			}//end for j
		}//end for i
  
  }//end Collision 
  
  public synchronized void Collision()
  {
		for(i=0;i<v.size();i++)
		{  		
			for(j=0;j<i;j++)
			{
				if(v.elementAt(i).equals(v.elementAt(j)))
				{	
					continue;
				}
				else if(
						((Ball)v.elementAt(i)).curPosition().distance(((Ball) v.elementAt(j)).curPosition()) >=  ((Ball) v.elementAt(i)).nextPosition().distance(((Ball) v.elementAt(j)).nextPosition())-4
						&&
						((Ball)v.elementAt(i)).curPosition().distance(((Ball) v.elementAt(j)).curPosition())-4 <= (((Ball) v.elementAt(i)).getBallR() + ((Ball) v.elementAt(j)).getBallR())/2 
					    )//判斷碰撞
		        {   
					double tempX =  ((Ball) v.elementAt(i)).getXDir();
					double tempY =  ((Ball) v.elementAt(i)).getYDir();
					((Ball) v.elementAt(i)).setDirection(((Ball) v.elementAt(j)).getXDir(), ((Ball) v.elementAt(j)).getYDir());//重設第一顆球的方向
					((Ball) v.elementAt(j)).setDirection(tempX,tempY);//重設第二顆球的方向
					
					if(collisionSmallMode==true)//如果勾選縮小
					{
					((Ball) v.elementAt(i)).setBallR( -(2+Math.random()*4) );
					((Ball) v.elementAt(j)).setBallR( -(2+Math.random()*4) );
					}
					
		        }
			}//end for j
		}//end for i
  }//end Collision
/**************************此method用來從畫面中點選球來執行動作*************************************************/
  private Ball getBallSelect(int xaxis,int yaxis,Vector totalBalls)//滑鼠點的位置
  {
	  for(int i=0 ; i < totalBalls.size(); i++)
	  {
		  ball=(Ball)totalBalls.elementAt(i);
		  if(ball != null)
		  {
			  if(((xaxis >= ball.getBallx())
					&&(xaxis <= (ball.getBallx()+ball.getBallR())))
					&&(yaxis >= ball.getBally())
					&& (yaxis <= (ball.getBally()+ball.getBallR())))//只要在半徑範圍內都會被選到
					{
				     return ball;//回傳選到的球
					}
		  }//end if
	  }//end for
	  return null;
  }//end getBallSelect()

	private void stopAllBall(Vector totalBalls)//停止全部的球 method
	{
		for(int i=0;i<totalBalls.size();i++)
		{
			ball=(Ball)totalBalls.elementAt(i);// vector 內的每一顆球 
			if(ball!=null)
			{
				ball.setStopState(true);//停止球
			}
		}
		stopall=true;
	}//end stopAllBal
  
	private void startAllBall( Vector totalBalls ) // 啟動全部的球 method
	{
		for(int i=0; i<totalBalls.size(); i++) // 迴圈    將每顆球重新啟動
		{
			ball = (Ball)totalBalls.elementAt(i); // vector 內的每一顆球 
			if(ball !=null)
			{
				ball.setStopState(false); // 停止球
				ball.reStart(); // 重新啟動球
			}
		}
		stopall = false;
	}
	
}//end JumpAreaPanel
