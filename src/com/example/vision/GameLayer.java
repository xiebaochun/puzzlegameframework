package com.example.vision;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;


import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;

public class GameLayer extends CCColorLayer {
    
	public enum GameState{start,play,over};
	public GameState gamestate=GameState.start;
	public int playerPositionX=0;
	public float timeTick=1;
	public int timeSeconedTick=0;
	public int eachStageTime=60;
	public int stage=1;
	public int level=1;
	public int score=0;
	public int target=4;
	public int question_choose=-1;
	public float result_show_time=1;
	
	public final CGPoint QUESTION_POSITION=CGPoint.make(150,450);
	public final CGPoint TIME_BAR_POSITION=CGPoint.make(280,605);
	//public final CGPoint GAME_START_POSITION=CGPoint.make(430,216);
	public final CGPoint GAME_START_POSITION=CGPoint.make(0,0);
	public final CGPoint CONFIRM_BUTTON_POSITION=CGPoint.make(1090,15);
	public final CGPoint QUESTION_TEXT_POSITION=CGPoint.make(65,370);
	public final CGPoint RESULT_POSITION=CGPoint.make(550,300);
	public final CGPoint FIX_POSITION=CGPoint.make(300,250);
	public final CGPoint CONFIRM_EXIT_POSITION=CGPoint.make(0,0);
	public final CGPoint REPLAY_POSITION=CGPoint.make(430,216);
	public final CGPoint PASS_POSITION=CGPoint.make(430,216);
	
	
	public final int TIME_BAR_HEIGHT=40;
	public final int TIME_BAR_WIDTH=768;
    public final int EACH_CARDS_COUNT=5;
	public CGPoint touchPoint;
	public Boolean isGamePause=true;
	public Boolean goToPrepare=true;
	public Boolean isSortMode=false;
	
	MySprite backGround;
	MySprite backGround_cover;
	MySprite stateBar;
	MySprite timeBar;
	MySprite result_sp;
	MySprite fix_sp;
		
	Button confirm_bt;
	Button gameStart;
	Button replay;
	Button pass;	
	Button confirmExit_bt;
	
	
    Number score_num;
    Number stage_num;
    Number target_num;
    //CCLabel test_label;
    Random gl_rnd;
    
    
    
    
	public static CCScene scene()
	{
	    CCScene scene = CCScene.node();
	    CCColorLayer layer = new GameLayer(ccColor4B.ccc4(255, 255, 255, 0));

	 
	    scene.addChild(layer);
	 
	    return scene;
	}
	protected GameLayer(ccColor4B color)
	{
		
	    super(color);
	    this.setIsTouchEnabled(true);
	    CGSize winSize = CCDirector.sharedDirector().displaySize();
	    CommonItem.SIZE_RATE_Y=(float)(winSize.height/CommonItem.GAME_HEIGHT);
	    CommonItem.SIZE_RATE_X=(float)(winSize.width/CommonItem.GAME_WIDTH);
	    CommonItem.gameLayer=this;
	    gl_rnd=new Random();
	    Log.v("log","winSize.height:"+winSize.height);
	    Log.v("log","SIZE_RATE_Y"+ CommonItem.SIZE_RATE_Y);
	   
	    
	  
	    backGround=new MySprite("bg_c.png",true,CGPoint.zero(),-5);
	    stateBar=new MySprite("StateBar.png",true,CGPoint.make(15, 555),-4);
	    stateBar.collisionRect=new Rectangle(stateBar.collisionRect.x+stateBar.collisionRect.width*(float)6/7,stateBar.collisionRect.y,stateBar.collisionRect.width*(float)1/7,stateBar.collisionRect.height*(float)1/2);
	    fix_sp=new MySprite("correct.png",false,FIX_POSITION,0);
	    
	    score_num=new Number(0,16,CGPoint.make(240, 652));
	    score_num.setNumber(score);
	    stage_num=new Number(0,16,CGPoint.make(600, 652));
	    stage_num.setNumber(stage);
	    target_num=new Number(0,16,CGPoint.make(910, 652));
	    target_num.setNumber(target);
	    timeBar=new MySprite("TimeBar.png",true,TIME_BAR_POSITION,-3);
	    result_sp=new MySprite("answerShow.png",false,RESULT_POSITION,1,2,0);
	    //reorderChild(timeBar.sprite,4);
	    
	    confirm_bt=new Button("confirm.png","confirm.png",CONFIRM_BUTTON_POSITION,true);
	    confirm_bt.fixedSizeRate(CommonItem.fixedSizeRate); 
	    replay=new Button("replay.png",false,REPLAY_POSITION,0);
	    pass=new Button("Continue.png",false,PASS_POSITION,0);
	    confirmExit_bt=new Button("bg_white.png",false,CONFIRM_EXIT_POSITION,0);
	    gameStart=new Button("layout-04.png",true,GAME_START_POSITION,0);
	   // number=new MySprite("stageNumber.png",true,CGPoint.zero(),11,1,-3);
	   
	    
		
	    
	    this.scheduleUpdate();
	}
	
	
	
	public void update(float dt) {	
		
		CommonItem.currenTouchState=CommonItem.touchState;
		touchPoint=CommonItem.touchPoint;
		spriteRectAndeventUpdate(dt);
		touchUpdate();
       //test_label.setString("currenTouch: "+CommonItem.currenTouchState+"  preCurrentTouch:"+CommonItem.preTouchState+"  touchState: "+CommonItem.touchState+"  xy: "+touchPoint);
       
		
//		if(tap())
//		{
//			stage++;
//			stage_num.setNumber(stage);
//		}
		switch(gamestate)
		{
		   case start:
			    gameStart.mySetVisible(true);
			    replay.mySetVisible(false);
			    pass.mySetVisible(false);
				   gamestate=GameState.play;
			    break;
		   case play:
			   if(isGamePause==false)
			   {
				   timeUpdate(dt);
				   if(goToPrepare==true)
				   {
					   goToPrepare=false;
					  
				       
				   }
				   
			   }
			   
			   break;
		   case over:
			   break;
		   
		}
		CommonItem.preTouchState=CommonItem.currenTouchState;
//		playerPositionX+=CommonItem.dt;
//		player.setPosition(playerPositionX, 0);
//		if(playerPositionX>=1500)CommonItem.dt=-1;
//		if(playerPositionX<0)CommonItem.dt=1;
		
		//CommonItem.preTouchState=CommonItem.currenTouchState;
		//CommonItem.touchState=CommonItem.TouchState.none;
	}
	private void touchUpdate() {
		if(isGamePause==false)
		{
			
		}
		if(CommonItem.currenTouchState==CommonItem.TouchState.up&&CommonItem.preTouchState==CommonItem.TouchState.move)
		{
			
		}
		
		
		
	}
	
	
	public void spriteRectAndeventUpdate(float delta) {
		if(tap()&&stateBar.collisionRect.contains(touchPoint))
		{
			confirmExit_bt.mySetVisible(true);
		}
		if(confirmExit_bt.getVisible()==true)
		{
           
		  if(confirmExit_bt.isClicked==true)
		  {
			  confirmExit_bt.isClicked=false;
			  
			  if(touchPoint.x<CCDirector.sharedDirector().displaySize().width/2)
			  {
				  System.exit(0);
			  }
			  else{
				  confirmExit_bt.mySetVisible(false);
			  }
		  }
		}
		if(gameStart.getVisible()==true)
		{

		  if(gameStart.isClicked==true)
		  {
			  gameStart.isClicked=false;
			  gameStart.mySetVisible(false);
			  isGamePause=false;
			  Log.v("log","gameStartEct_Z:"+gameStart.getZOrder()+replay.getZOrder()+pass.getZOrder());
		  }
		}
		if(pass.getVisible()==true)
		{
			
		  if(pass.isClicked==true)
		  {
			  pass.isClicked=false;
			  pass.mySetVisible(false);
			  isGamePause=false;
			  goToPrepare=true;
			  stage++;
			  stage_num.setNumber(stage);
			  score=0;
			  score_num.setNumber(score);
			  target++;
			  target_num.setNumber(target);
			  //goToNextRound();
		  }
		}
		if(replay.getVisible()==true)
		{
			
		  if(replay.isClicked==true)
		  {
			  replay.isClicked=false;
			  replay.mySetVisible(false);
			  isGamePause=false;
			  goToPrepare=true;
			  score=0;
			  score_num.setNumber(score);
			 // goToNextRound();
		  }
		}
		if(confirm_bt.getVisible()==true&&isGamePause==false)
		{
			 if(confirm_bt.isClicked==true)
			 {
				 confirm_bt.isClicked=false;
				 //confirm_bt.setVisible(false);
				 //if all answers place were holding card and those cards,which were holden were the same style to the current answer style
				 //goToNextRound(); 
				 if(isAnswerCorrect())//if answer id right then show the correct image 
				 {
					 result_sp.currentFrameY=0;
				 }
				 else                 //if answer is in incorrect
				 {
					 result_sp.currentFrameY=1;
					 
				 }
				 result_sp.rectUpdate();
				 result_sp.setVisible(true);
			 }
		}
	
		if(result_sp.sprite.getVisible()==true)
		{
			result_show_time-=delta;
			if(result_show_time<0)
			{
				result_show_time=1;
				result_sp.setVisible(false);
				if(result_sp.currentFrameY==0)
				{
					goToPrepare=true;
					score++;
					score_num.setNumber(score);
				}
				else
				{
					fix_sp.setVisible(true);
				}
			}
		}
		if(fix_sp.sprite.getVisible()==true)
		{
			result_show_time-=delta;
			if(result_show_time<0)
			{
				result_show_time=1;
				fix_sp.setVisible(false);
			}
		}
	}
	//juge answer rect is all hold the card,and push the card,which placed in answer's rect,into a array
	private boolean isAnswerCorrect() {
		
		return true;
	}
	
	
	
	//if is tap in screen
	private boolean tap() {
		if(CommonItem.currenTouchState==CommonItem.TouchState.up&&CommonItem.preTouchState==CommonItem.TouchState.down)//&&CommonItem.currenTouchState==CommonItem.TouchState.up)
		{
			Log.v("touchState","tap");
			Log.v("touchState","pisition:"+touchPoint.toString());
			return true;
		}
		return false;
	}
	public void timeUpdate(float dt) {
		timeTick-=dt;
		
		if(timeTick<=0)
		{
			
		   timeTick=1;
		   timeSeconedTick++;
			Log.v("log",""+timeSeconedTick);//log
		   if(timeSeconedTick>=eachStageTime)
		   {
			   timeSeconedTick=0;
			   isGamePause=true;
			   if(score>=target)
			   {
				   pass.mySetVisible(true);
			   }
			   else
			   {
				   replay.mySetVisible(true);
			   }
		   }
		   timeBar.sprite.setTextureRect(0, 0, ((float)(eachStageTime-timeSeconedTick)/eachStageTime)*TIME_BAR_WIDTH,40 , false);
		}
	}
	
		//@Override
        public boolean ccTouchesBegan(MotionEvent event) {
            CGPoint convertedLocation = CCDirector.sharedDirector()
            	.convertToGL(CGPoint.make(event.getX(), event.getY()));
           // CommonItem.touchState=CommonItem.TouchState.down;
           // CCNode s = getChildByTag(kTagSprite);
            //s.stopAllActions();
            //s.runAction(CCMoveTo.action(1.0f, convertedLocation));
           
            //CGPoint pnt = s.getPosition();

           // float at = CGPoint.ccpCalcRotate(pnt, convertedLocation);

            //s.runAction(CCRotateTo.action(1, at));
            
            //progressTimer.setPercentage(10.0f + progressTimer.getPercentage());

            return CCTouchDispatcher.kEventHandled;
        }
        public boolean ccTouchesMoved(MotionEvent event) {
        	//CommonItem.touchState=CommonItem.TouchState.move;
            return CCTouchDispatcher.kEventIgnored;  // TODO Auto-generated method stub
        }

        public boolean ccTouchesEnded(MotionEvent event) {
        	//CommonItem.touchState=CommonItem.TouchState.up;
            return CCTouchDispatcher.kEventIgnored;  // TODO Auto-generated method stub
        }

	
}
