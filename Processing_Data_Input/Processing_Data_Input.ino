String message; //string that stores the incoming message
int in1A = 2;
int in2A = 3;
int in1B = 4;
int in2B = 5;
int in1C = 6;
int in2C = 7;
int in1D = 8;
int in2D = 8;
int A = 0;
int B = 1;
int C = 2;
int D = 3;


void setup()
{
  Serial.begin(9600); //set baud rate
  pinMode(A, OUTPUT);
  pinMode(in1A, OUTPUT);
  pinMode(in2A, OUTPUT);
  pinMode(B, OUTPUT);
  pinMode(in1B, OUTPUT);
  pinMode(in2B, OUTPUT);
  pinMode(C, OUTPUT);
  pinMode(in1C, OUTPUT);
  pinMode(in2C, OUTPUT);
  pinMode(D, OUTPUT);
  pinMode(in1D, OUTPUT);
  pinMode(in2D, OUTPUT);
}

void loop()
{
  digitalWrite(in1A, HIGH);
  digitalWrite(in1B, HIGH);
  digitalWrite(in1C, HIGH);
  digitalWrite(in1D, HIGH);
  digitalWrite(in2A, LOW);
  digitalWrite(in2B, LOW);
  digitalWrite(in2C, LOW);
  digitalWrite(in2D, LOW);
  analogWrite(A, 200);
  analogWrite(B, 200);
  analogWrite(C, 200);
  analogWrite(D, 200);
 
  
}
String read(){

  while(Serial.available())
    {
      message+=char(Serial.read());
    }
  if(!Serial.available())
    {
      if(message.length()>=10)
        {  
        message = message.substring(message.length() -10);
        Serial.println(message);
        String s = message;
        message = "";  
        return s;
        }
    }
}

void process(String s){
  
    if(s.startsWith("Auto")){
      digitalWrite(in1A, HIGH);
      digitalWrite(in1B, HIGH);
      digitalWrite(in1C, HIGH);
      digitalWrite(in1D, HIGH);
      digitalWrite(in2A, LOW);
      digitalWrite(in2B, LOW);
      digitalWrite(in2C, LOW);
      digitalWrite(in2D, LOW);
      analogWrite(A, 200);
      analogWrite(B, 200);
      analogWrite(C, 200);
      analogWrite(D, 200);
    }else{
    
     String strX = message.substring(2,5);
     String strY = message.substring(7);
     Serial.println(strX);
     message = "";
     int x = strX.toInt();
     int y = strY.toInt();
     x = x - 492;
     y= y;
     x = x/1.4;
     y = y/1.4;
     int LeftWheels = (y+x)/2;
     int RightWheels = (y-x)/2;
      analogWrite(A, LeftWheels);
      analogWrite(B, LeftWheels);
      analogWrite(C, RightWheels);
      analogWrite(D, RightWheels);   
    }
}
