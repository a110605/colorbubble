import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Ball extends Thread
{
  private int r,ballcolorrgb1,ballcolorrgb2,height,width,size;//宣告 半徑、 顏色一 、顏色二 、 高 、寬、 粗細
  private double xDirection,yDirection,x,y,origin_x, origin_y;//x方向變數、Y方向變數、左上角球的位置、圓心位置
  private Color ballcolor1,ballcolor2;//球顏色
  public boolean stopst=false,runst=true;//停止 運作 狀態

  
  public Ball(int X,int Y,JumpAreaPanel board)
  {  
	  x = X;//初始化球的左上位置
	  y = Y;//初始化球的左上位置
	  origin_x=x+r;//初始話圓心位置
	  origin_y=y+r;//初始話圓心位置
	  //隨機產生該變數數值
	  r=(int)(Math.random() * 50 + 10);
	  size=(int)(Math.random() *7 +3);
	  
	  xDirection = -20 + ((int)(Math.random() * 31));
	  yDirection = -20 + ((int)(Math.random() * 31));
	  ballcolor1=new Color((int)(255*Math.random()),(int)(255*Math.random()),(int)(255*Math.random()));

	  ballcolorrgb1=ballcolor1.getRGB();//取得ballColor1的顏色
	  
	  height=board.getHeight();//取得畫面的高
	  width=board.getWidth();//取得畫面的寬
  }//end Ball()
  
  public void run()
  {
	  while(runst)
	  {
		  try
		  {
			  synchronized(this)
			  {
				  while(stopst)
					  wait();
				  this.move();
				  this.sleep(40);
			  }
		  }
		  catch(Exception e)
		  {
			  e.printStackTrace();
		  }
		  
	  }
  }//end run()
  
  private synchronized void move()
  { 
	  if(x <= 0)//撞到左邊畫面
	  {
		  xDirection = -xDirection;
	  }
	  else if((x+2*r)>= width)//撞到右邊畫面
	  {
		  x=(width-2*r);
		  xDirection = -xDirection;
	  }
	  x += xDirection;//調整X的數值
	  origin_x += xDirection;//調整origin_x數值
		
	  if(y<=0)//撞到上邊畫面
	  {
		  yDirection = -yDirection;
	  }
	  else if((y+2*r)>=height)//撞到下邊畫面
	  {
		  y=(height-2*r);
		  yDirection = -yDirection;
	  } 
	  y += yDirection;	//調整y的數值
	  origin_y += yDirection;//調整origin_y數值
	  
  }//end move()
  
	  public void draw(Graphics g)
	  {    
		  if(ballcolorrgb1 >= ballcolorrgb2 && stopst==false)
		  {
			  ballcolorrgb1--;
			  ballcolor1 = new Color(ballcolorrgb1);
		  }		  
		  if(ballcolorrgb1 < ballcolorrgb2 && stopst==false)
		  {
			  ballcolorrgb1++;
			  ballcolor1 = new Color(ballcolorrgb1);
		  }
		  Graphics2D g2d= (Graphics2D) g;
		  g2d.setStroke(new BasicStroke(size));//設定粗細
		  g2d.setColor(ballcolor1);//設定顏色
		  g2d.draw( new Ellipse2D.Double( this.getBallx() , this.getBally() 
				  , this.getBallR() , this.getBallR() ) );//畫空心圓
	  }//end draw()
	    
	  public double getBallx()//取得現在球的左上X座標
	  {
		  return x;
	  }
	  
	  public double getBally()//取得現在球的左上Y座標
	  {
		  return y;
	  }
	  
	  public double getXDir()//取得球的x方向
	  {
		  return xDirection;
	  }
	  
	  public double getYDir()//取得球的y方向
	  {
		  return yDirection;
	  }
	  
	  public Point2D curPosition()//取的現在球心位置
		{
		   return new Point2D.Double(origin_x, origin_y);
		}
	  
	  public Point2D nextPosition()//取得球下一步的位置
		{
		  return new Point2D.Double(origin_x + xDirection,origin_y + yDirection);
		}
		
	  public void setDirection(double newX,double newY)//設定球的方向
		{
			xDirection = newX;
			yDirection = newY;
		}
		
	  public int getBallR()//取得直徑
	  {
		  return 2*r;
	  }
	  public void setBallR(double newSize)//設定半徑
	  {
			r += newSize;
			origin_x = x + r / 2;
			origin_y = y + r / 2;
	  }
	  
	  public void setRunState(boolean state)//設定是否執行
	  {
		  runst=state;
	  }

	  public void setStopState(boolean state)//設定是否停止
	  {
		  stopst=state;
	  }
	  
	  public boolean getStopState()//取得是否停止狀態
      {
    	  return stopst;
      }
	  
	  public synchronized void reStart()//重新開始，叫醒睡覺中的thread
	  {
		  this.notify();
	  }
}//end Ball
