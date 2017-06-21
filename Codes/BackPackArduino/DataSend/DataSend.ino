/**
 *Create by:Zhang Yunpeng
 *Date:2017/04/20
 *Modify by:
 *Date:
 *Modify by:
 *Date:
 *describe:模拟背包，发送监测数据
 */

#include <Wire.h> 
#include <LiquidCrystal_I2C.h> //引用I2C库

LiquidCrystal_I2C lcd(0x3F,16,2); 
char data[100] = {0}; //接收与发送的缓存区
int index = -1;
int MAX = 2000;  //random最大值为2000
int MIN = 1000;  //random最小值为1000
int A;  //探头A的测量值
int B;  //探头B的测量值
int C;  //探头C的测量值
int D;  //探头D的测量值
void setup()
{
  pinMode(13,OUTPUT);
  Serial.begin(9600);
  lcd.init();                  // 初始化LCD  
  lcd.backlight();             //设置LCD背景等亮   
  lcd.print("Aloha!");         //初始显示
}

//循环接收指令
void loop()
{
  if(Serial.available()>0)
  {
      char cur = Serial.read(); 
      switch(cur)
      {
        case 0x0A:{
          //接收消息完成时交给处理函数去处理
          todo();
          //清空缓存区和位置索引
          index = -1;
          memset(data, 0, sizeof(data));
          break;
          }
        case 0x0D:{
          break;
        }
        default:{
          //循环接收消息内容
          index++;
          data[index] = cur;
          break;
          }
      }
  }
}
//发送测量数据
void todo() {
  if(data[0] == 'G') {  //Get消息
    if(data[1] == 'M') {  //智能监测页面
      A = random(MIN, MAX);
      B = random(MIN, MAX);
      C = random(MIN, MAX);
      D = random(MIN, MAX);
      //将所有探头值写入缓存区
      data[3] = 'A';
      setvalue(A, 8);
      data[9] = 'B';
      setvalue(B, 14);
      data[15] = 'C';
      setvalue(C, 20);
      data[21] = 'D';
      setvalue(D, 26);
      //在屏幕上显示测量值
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("A:");
      lcd.setCursor(2, 0);
      lcd.print(A);
      lcd.setCursor(8, 0);
      lcd.print("B:");
      lcd.setCursor(10, 0);
      lcd.print(B);
      lcd.setCursor(0, 1);
      lcd.print("C:");
      lcd.setCursor(2, 1);
      lcd.print(C);
      lcd.setCursor(8, 1);
      lcd.print("D:");
      lcd.setCursor(10, 1);
      lcd.print(D);
      //lcd.print(data);
      //发送消息，注意必须有ln，不可用Serial.print（）
      Serial.println(data);
    }
  }else if(data[0] == 'S') {
    
  }else {
    
  }
}

//将4个探头测量数据写入缓存区
void setvalue(int val, int index) {
  int num = val;
  while(val != 0) {
    int temp = val % 10;
    data[index--] = temp + 48;
    val /= 10;
  }
  if(num < 1000 || num > 2000) {
    data[index] = 'F';
  }else if(num >= 1000 && num <= 1930) {
    data[index] = 'N'; 
  }else if(num > 1930 && num <= 1970) {
    data[index] = 'A';
  }else if(num > 1970 && num <= 2000) {
    data[index] = 'S';
  }else {
    
  }
}

