package com.example.pc.bomb;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    //默认列
    private int col = 6;
    //默认行
    private int row = 6;
    public int arrBomb[][] = new int[row][col];
    //覆盖物宽度
    private int recWidth = 120;
    //覆盖物高度
    private int recHeight = 120;
    //是否打开作弊模式
    private boolean isFlag = false;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initGrid();

    }

    /**
     * 初始化画面
     */
    private void initGrid(){
        //初始化雷阵
        this.initBombArray();
        GridLayout grid = (GridLayout)findViewById(R.id.main);
        for(int row=0;row<this.row;row++) {
            for (int col = 0; col < this.col; col++) {
                //设置它的行和列
//                GridLayout.Spec rowSpec = GridLayout.spec(row);
//                GridLayout.Spec columnSpec = GridLayout.spec(col);
                //指定了当前控件在父布局中的位置
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(col));
                params.height = this.recHeight;
                params.width = this.recWidth;
                Button btn = new Button(this);
//                btn.setBackgroundColor(Color.parseColor("#F5F5DC"));
                btn.setBackgroundResource(R.drawable.bg);
                btn.setTag(R.id.row,row);
                btn.setTag(R.id.col,col);
                //点击事件
                btn.setOnClickListener(clickListener);
                btn.setOnLongClickListener(new OnLongClickListenerImpl());
                grid.addView(btn, params);

            }
        }
    }

    /**
     * 监听长按事件，用红旗标记雷区
     */
    private class OnLongClickListenerImpl implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            try{
                GridLayout grid = (GridLayout)findViewById(R.id.main);
                Button btn = (Button)v;
                int row = (int)btn.getTag(R.id.row);
                int col = (int)btn.getTag(R.id.col);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(col));
                params.height = MainActivity.this.recHeight;
                params.width = MainActivity.this.recWidth;
                ImageView iv = new ImageView(MainActivity.this);
                int resId = MainActivity.this.getResources().getIdentifier("red","drawable",getPackageName());
                iv.setImageResource(resId);
                iv.setOnClickListener(ivClickListener);
//                btn.setOnClickListener(null);
                grid.addView(iv,params);
                Log.d("AAA",row + "+++" + col);
            }catch(Exception e){
                System.out.println(e);
            }
            return true;
        }


    }

    /**
     * 统计周围埋雷数量
     * @param x 第x行
     * @param y 第y列
     * @return
     */
    private int getBombCount(int x,int y){
        int count = 0;
        if(x-1>=0 && y-1>=0 && this.arrBomb[x-1][y-1] == 1){
            count++;
        }
        if(x-1>=0 && this.arrBomb[x-1][y] == 1){
            count++;
        }
        if(x-1>=0 && y+1<this.row && this.arrBomb[x-1][y+1] == 1){
            count++;
        }
        if(y-1>=0 && this.arrBomb[x][y-1] == 1){
            count++;
        }
        if(y+1<this.row && this.arrBomb[x][y+1] == 1){
            count++;
        }
        if(x+1<this.col && y-1>=0 && this.arrBomb[x+1][y-1] ==1){
            count++;
        }
        if(x+1<this.col && this.arrBomb[x+1][y]==1){
            count++;
        }
        if(x+1<this.col && y+1<this.row && this.arrBomb[x+1][y+1] == 1){
            count++;
        }
        return count;
    }

    /**
     * 布雷初始化
     */
    private void initBombArray(){
        Random random=new Random();
        for (int i=0;i<this.col;i++){
            int x = random.nextInt(this.col);
            int y = random.nextInt(this.col);
            this.arrBomb[x][y] = 1;
            Log.d("BBB",x + ":::" + y);
        }
    }

    /**
     * 点击红旗取消插上标记
     */
    public View.OnClickListener ivClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            GridLayout grid = (GridLayout)findViewById(R.id.main);
            ImageView iv = (ImageView)v;
            grid.removeView(iv);
        }
    };


    /**
     * 点击事件，扫雷动作
     */
    public View.OnClickListener clickListener = new View.OnClickListener() {

        public void onClick(View v) {
            GridLayout grid = (GridLayout)findViewById(R.id.main);
            Button btn = (Button)v;
            int row = (int)btn.getTag(R.id.row);
            int col = (int)btn.getTag(R.id.col);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(col));
            params.height = MainActivity.this.recHeight;
            params.width = MainActivity.this.recWidth;
            int resId=0;
            //踩中地雷，游戏结束
            if(MainActivity.this.arrBomb[row][col] == 1){
                ImageView iv = new ImageView(MainActivity.this);
                grid.removeView(btn);
                if(MainActivity.this.isFlag == false) {
                    resId= MainActivity.this.getResources().getIdentifier("bomb","drawable",getPackageName());
                    iv.setImageResource(resId);
                    grid.addView(iv,params);
                    Toast toast = Toast.makeText(getApplicationContext(), "踩中地雷了，游戏结束", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    grid.removeAllViews();
                    MainActivity.this.arrBomb = new int[MainActivity.this.row][MainActivity.this.col];
                    MainActivity.this.initGrid();
                    return;
                }else{
                    resId = MainActivity.this.getResources().getIdentifier("red","drawable",getPackageName());
                    iv.setImageResource(resId);
                    grid.addView(iv,params);
                }
            }else{
                int count = MainActivity.this.getBombCount(row,col);
                grid.removeView(btn);
                if(count>0){
                    ImageView iv = new ImageView(MainActivity.this);
                    switch (count){
                        case 1:
                            resId=MainActivity.this.getResources().getIdentifier("one","drawable",getPackageName());
                            break;
                        case 2:
                            resId=MainActivity.this.getResources().getIdentifier("two","drawable",getPackageName());
                            break;
                        case 3:
                            resId=MainActivity.this.getResources().getIdentifier("three","drawable",getPackageName());
                            break;
                        case 4:
                            resId=MainActivity.this.getResources().getIdentifier("four","drawable",getPackageName());
                            break;
                        case 5:
                            resId=MainActivity.this.getResources().getIdentifier("five","drawable",getPackageName());
                            break;
                        case 6:
                            resId=MainActivity.this.getResources().getIdentifier("six","drawable",getPackageName());
                            break;
                        default:

                        break;
                    }
                    iv.setImageResource(resId);
                    grid.addView(iv, params);
                }else{
                    Button btnBlank = new Button(MainActivity.this);
                    //给按钮加上透明效果
                    btnBlank.setBackgroundColor(Color.parseColor("#00000000"));
                    grid.addView(btnBlank, params);
                }
                //标记翻过了
                MainActivity.this.arrBomb[row][col]=2;
            }
            boolean isRight = true;
            for(int i=0;i<MainActivity.this.row;i++) {
                for (int j = 0; j < MainActivity.this.col; j++) {
                        if(MainActivity.this.arrBomb[i][j]==0){
                            isRight = false;
                        }
                }
            }
            //扫描遍历，检查是否全部翻过
            if(isRight==true){
                Toast toast = Toast.makeText(getApplicationContext(), "恭喜您胜利了", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            Log.d("AAA",row + "===" + col);
        }
    };

    /**
     * 普通模式
     * @param v
     */
    public  void clickMiddle(View v){
        this.row = 6;
        this.col = 6;
        GridLayout grid = (GridLayout)findViewById(R.id.main);
        grid.removeAllViews();
        this.arrBomb = new int[this.row][this.col];
        this.initGrid();
    }

    /**
     * 简单模式
     * @param v
     */
    public  void clickSimple(View v){
        this.row = 3;
        this.col = 3;
        GridLayout grid = (GridLayout)findViewById(R.id.main);
        grid.removeAllViews();
        this.arrBomb = new int[this.row][this.col];
        this.initGrid();
    }

    /**
     * 高手模式（困难模式）
     * @param v
     */
    public  void clickDiff(View v){
        this.row = 9;
        this.col = 9;
        GridLayout grid = (GridLayout)findViewById(R.id.main);
        grid.removeAllViews();
        this.arrBomb = new int[this.row][this.col];
        this.initGrid();
    }

    /**
     * 打开是否作弊
     * @param v
     */
    public  void clickFlag(View v){
        Button btn = (Button) v;
        if(this.isFlag == true){
            this.isFlag = false;
            btn.setText("打开作弊");
        }else{
            this.isFlag = true;
            btn.setText("取消作弊");
        }
    }

}
