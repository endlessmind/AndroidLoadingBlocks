package com.example.androidloadingblocks;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

@SuppressLint("DrawAllocation")
public class LoadingBlockView extends View {

	//private String TAG = "LoadingBlockView";
	int mSpace = Dip(4);
	int mBlockWidth = Dip(7);
	ArrayList<Block> mBlocks;
	int mDefaultColor = Color.LTGRAY;
	int mMidLightColor = Color.GRAY;
	int mHighLightColor = Color.DKGRAY;
	int mHighLighted = -2;
	private Handler h;
    Timer t = null;
    TimerTask task = null;
    
    private Mode mMode;
    private Direction mSelectionDirection = Direction.Forward;
    
    private enum Direction {
    	Forward,
    	Backward
    }
    
    public enum Mode {
    	CONTINUES,
    	PINGPONG,
    	PROGRESS
    }
	
	public LoadingBlockView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		h = new Handler();

		
	}
	
	public LoadingBlockView(Context context, AttributeSet attrs ) {
		this(context, attrs, 0);
	}
	
	public LoadingBlockView(Context context ) {
		super(context);
	}
	
	
	public Mode getMode() {
		return mMode;
	}
	
	public void setMode(Mode value) {
		mMode = value;
		if (mMode != Mode.PROGRESS) {
			final int TIMER_ONE_SECOND = 100;
	        
	        if (t != null) {
	        	return;	
	        }
	        
	        t = new Timer();
	        
	        if (task != null) {
	        	return;	
	        }
	        
	        task = new TimerTask(){
				public void run() {

					h.post(new Runnable() {
						@Override
						public void run() {
							invalidate();
						}
						
					});


				}};
				t.scheduleAtFixedRate(task, 0, TIMER_ONE_SECOND);
		} else {
        	if (task != null)
        		task.cancel();
        		task = null;
        	if (t != null) {
        		t.cancel();
        		t.purge();
        		t = null;
        	}
		}
		
		
	}
	
	//Calculated the number of blocks needed to fill the view
	private int CalculateBlockCount() {
		int w = this.getWidth();
		return Math.round((w/ (mBlockWidth + mSpace)));
	}
	
	//Creates the number of blocks specified by the parameter.
	private ArrayList<Block> createBlocks(int count) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		for (int i = 0; i < count; i++) {
			Block b = new Block();
			b.setColor(Color.LTGRAY);
			blocks.add(b);
		}
		return blocks;
	}
	
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       if (mBlocks == null) {
    	   mBlocks = createBlocks(CalculateBlockCount());
       }
       
       //We need to keep track of where the left side of the next block will be.
       int blockLeft = 0;
       //We need to draw each block
       for (int i = 0; i < mBlocks.size() ; i++) {
           Paint p = new Paint();
           if (i == mHighLighted) {
           p.setColor(Color.DKGRAY);
           } else if (i == mHighLighted -1 || i == mHighLighted +1) {
        	   p.setColor(Color.GRAY);
           } else {
        	   p.setColor(Color.LTGRAY);
           }
           p.setStyle(Paint.Style.FILL);
           p.setStrokeWidth(5f);
           
           Rect r = new Rect();
           r.bottom = this.getHeight();
           r.top = 0;
           r.left = blockLeft;
           r.right = blockLeft + mBlockWidth;
           
           canvas.drawRect(r,p);
           //The left side of the next block.
           blockLeft += mBlockWidth + mSpace;
           
       }
       
       //More to be added later
       if (mMode == Mode.PINGPONG) {
    	   PingPongMode();
       } else if (mMode == Mode.CONTINUES) {
    	   ContinuesMode();
       }
       

       
	}
	
	
	private void PingPongMode() {
		if (mSelectionDirection == Direction.Forward) {
		       if (mHighLighted < mBlocks.size())
		    	   mHighLighted++;
		       else {
		    	   mHighLighted--;
		    	   mSelectionDirection = Direction.Backward;
		       }
		       
		} else if (mSelectionDirection == Direction.Backward) {
		       if (mHighLighted > -1)
		    	   mHighLighted--;
		       else {
		    	   mHighLighted++;
		    	   mSelectionDirection = Direction.Forward;
		       }
		}
	}
	
	private void ContinuesMode() {
		mSelectionDirection = Direction.Forward;
		if (mHighLighted < mBlocks.size())
			mHighLighted++;
	    else {
	    	mHighLighted = 0;
	    }
	}
	
	
    private int Dip(int value) {
    	Resources r = LoadingBlockView.this.getResources();
    	float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
    	return (int) px;

    }

}
