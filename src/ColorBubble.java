import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.*;

public class ColorBubble extends JFrame
{
  private GridBagLayout layout;//宣告一個gridbaglayout
  private GridBagConstraints constraints;
  private JButton addball=new JButton("新增球");
  private JButton stopall=new JButton("停止/繼續全部");
  private JButton stopchoose=new JButton("停止/繼續選取");
  private JButton delall=new JButton("移除全部");
  private JButton delchoose=new JButton("移除選取");
  private JButton backcolorchange=new JButton("背景色");
  private JButton speedup=new JButton("加速");
  private JButton speeddown=new JButton("減速");
  private JCheckBox collide=new JCheckBox("偵測碰撞");
  private JCheckBox combine=new JCheckBox("碰撞結合");
  private JCheckBox collidesmall=new JCheckBox("碰撞變小");
  private JLabel label1,label2,label3;
  private Color back=Color.BLACK,backchange;//宣告背景顏色
  private JPanel status,right;//宣告狀態列和CheckBox的panel，
  private JumpAreaPanel jumpareapanel;
  
  public ColorBubble()
  {
	  super("彩色泡泡");
	  layout=new GridBagLayout();
	  setLayout(layout);
	  constraints =new GridBagConstraints();
	 
	  addball.addActionListener(
			  new ActionListener()
			  {
				  public void actionPerformed(ActionEvent event)
				  {
					  jumpareapanel.set("addball");
					  label1.setText("目前功能: 新增球");
					  label3.setText("速度："+jumpareapanel.getspeed());
				  }
			  }
	  );
	  
	  stopall.addActionListener(
			  new ActionListener()
			  {
				  public void actionPerformed(ActionEvent event)
				  {
					  jumpareapanel.stopAll();
					  label1.setText("目前功能: 停止/暫停全部 ");
				  }
			  }
	  );
	    
	  stopchoose.addActionListener(
			  new ActionListener()
			  {
				  public void actionPerformed(ActionEvent event)
				  {
					  jumpareapanel.set("stopchoose");
					  label1.setText("目前功能: 停止/暫停選取");
				  }
			  }
	  );

	  delchoose.addActionListener(
			  new ActionListener()
			  {
				  public void actionPerformed(ActionEvent event)
				  {
					  jumpareapanel.set("delchoose");
					  label1.setText("目前功能: 移除選取");
				  }
			  }
	  );
	 
	  delall.addActionListener(
			  new ActionListener()
			  {
				  public void actionPerformed(ActionEvent event)
				  {
					  jumpareapanel.rmallball();
					  label1.setText("目前功能: 移除全部");
					  
				  }
			  }
	  );
	  
	  speedup.addActionListener(
			  new ActionListener()
			  {
				  public void actionPerformed(ActionEvent event)
				  {
					  jumpareapanel.changeSpeed(-10);
					  label1.setText("目前功能: 加速");
					  label3.setText("速度 "+jumpareapanel.getspeed());
				  }
			  }
	  );
	  
	  speeddown.addActionListener(
			  new ActionListener()
			  {
				  public void actionPerformed(ActionEvent event)
				  {
					  jumpareapanel.changeSpeed(+10);
					  label1.setText("目前功能: 減速");
					  label3.setText("速度 "+jumpareapanel.getspeed());
				  }
			  }
	  );
	  
	  backcolorchange.addActionListener(
			  new ActionListener()
			  {
				  public void actionPerformed(ActionEvent event)
				  {
					 label1.setText("目前功能: 背景色");
					 backchange=back;
					 back=JColorChooser.showDialog(ColorBubble.this
							 ,"Choose background's color",back);
					 if(back==null)
					 {
						 back=backchange;
					 }
					 jumpareapanel.setBackground(back);
				  }
			  }
	  );

	  collide.setSelected(false);
	  collide.addItemListener(
			  new ItemListener()
			  {  
				  public void itemStateChanged(ItemEvent event)
				  {
						if(collide.isSelected())
						{
							jumpareapanel.setCollisionMode(true);//設定CollisionMode為true
							label1.setText("目前功能: 偵測碰撞");
							collidesmall.setEnabled(true);//打開collidesmall的選項
							combine.setSelected(false);//關掉combine的選項
							jumpareapanel.setCombineMode(false);//關閉combine的功能
						}
						else
						{
							jumpareapanel.setCollisionMode(false);//設定CollisionMode為false
							label1.setText("目前功能:");
							collidesmall.setSelected(false);//關閉collidesmall的選項
							collidesmall.setEnabled(false);//使碰撞變小不能被選取
							jumpareapanel.setCollisionSmallMode(false);
						}
				}
			  }
	  );
	  
	  collidesmall.setSelected(false);
	  collidesmall.addItemListener(
			  new ItemListener()
			  {
				  public void itemStateChanged(ItemEvent event)
				  {
					  if(collidesmall.isSelected())
						{
						  jumpareapanel.setCollisionSmallMode(true);
						  label1.setText("目前功能: 碰撞變小");
						}
						else
						{
							 jumpareapanel.setCollisionSmallMode(false);
							 label1.setText("目前功能: 取消碰撞變小模式");
						}
						
					 
					 
				}
			  }
	  );
	  
	  combine.setSelected(false);
	  combine.addItemListener(
			  new ItemListener()
			  {
				  public void itemStateChanged(ItemEvent event)
				  {
					  if(combine.isSelected())
						{
						  jumpareapanel.setCombineMode(true);
						  label1.setText("目前功能: 碰撞結合");
						  collidesmall.setSelected(false);
						  collidesmall.setEnabled(false);
						  jumpareapanel.setCollisionSmallMode(false);
						  collide.setSelected(false);
						  jumpareapanel.setCollisionMode(false);
						}
						else
						{
							 jumpareapanel.setCombineMode(false);
							 label1.setText("目前功能:");
						}
					  
						
				  }
			  }
	  );
	  
	  right=new JPanel(new GridLayout(2,2));
	  status=new JPanel(new GridLayout(1,2));
	  status.setBackground(Color.GRAY);
	  label1=new JLabel("目前功能 : 新增球");
	  label2=new JLabel("球數 :");   
	  label3= new JLabel("速度 ");
	  label1.setForeground(Color.WHITE);
	  label2.setForeground(Color.WHITE);
	  label3.setForeground(Color.WHITE);
	  status.add(label1);
	  status.add(label2);
	  status.add(label3);
	  right.add(collide);  
	  right.add(collidesmall);
	  right.add(combine);
	  
	  collidesmall.setSelected(false);
	  collidesmall.setEnabled(false);
	  
	  jumpareapanel= new JumpAreaPanel(label2,label3);
	  constraints.fill = GridBagConstraints.BOTH; 
	  addComponent(addball,0,0,1,2);
	  addComponent(delchoose,0,1,1,1);
	  addComponent(delall,1,1,1,1);
	  addComponent(stopchoose,0,2,1,1);
	  addComponent(stopall,1,2,1,1);
	  addComponent(speedup,0,3,1,1);
	  addComponent(speeddown,1,3,1,1);
	  addComponent(backcolorchange,0,4,1,2);
 
	  constraints.weightx=1000;
	  addComponent(right,0,5,1,2);
	  addComponent(status,2,0,6,1);
	  
	  constraints.weighty=1000;
	  jumpareapanel.setBackground( Color.BLACK );
	  addComponent(jumpareapanel,3,0,7,1);
      
	  ExecutorService threadExecutor=Executors.newFixedThreadPool(1);
	  threadExecutor.execute(jumpareapanel);
	  threadExecutor.shutdown();
  }//end ColorBubble()
  
   /*************************自己寫的method，可以在此FRAME中加入Object*********************/
  private void addComponent(Component component,
		  int row,int column,int width,int height)
  {
	  constraints.gridx=column;
	  constraints.gridy=row;
	  constraints.gridwidth=width;
	  constraints.gridheight=height;
	  layout.setConstraints(component,constraints);
	  add(component);
  }//end addComponent

}//end ColorBubble
