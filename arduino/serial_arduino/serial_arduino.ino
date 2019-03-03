#include <Arduino.h>

#define MAGIC_WORD 0x0F02
#define PACKET_SIZE 32

enum RoverRequestId {
  REQ_HELLO,
  REQ_ENGINE_MOVE,
  REQ_KEEP_ALIVE,
  REQ_LED_BLINK
};

// { engine
int ENGINE_IN1 = 7;
int ENGINE_IN2 = 6;
int ENGINE_IN3 = 4;
int ENGINE_IN4 = 5;
int ENGINE_ENA = 9;
int ENGINE_ENB = 3;
// } engine

void engineSetup() {
  pinMode (ENGINE_ENA, OUTPUT);
  pinMode (ENGINE_IN1, OUTPUT);
  pinMode (ENGINE_IN2, OUTPUT);
  pinMode (ENGINE_ENB, OUTPUT);
  pinMode (ENGINE_IN4, OUTPUT);
  pinMode (ENGINE_IN3, OUTPUT);
}

void engineMove(byte engine, byte dir, word speed) {

  int en = 0;
  int fwd = 0;
  int bkw = 0;
  if (engine == 0) {
    en = ENGINE_ENA;
    fwd = ENGINE_IN2;
    bkw = ENGINE_IN1;
  } else if (engine == 1) {
    en = ENGINE_ENB;
    fwd = ENGINE_IN4;
    bkw = ENGINE_IN3;
  } else {
    engineMove(0, dir, speed);
    engineMove(1, dir, speed);
    return;
  }
  if (dir == 0) {
    digitalWrite (fwd, HIGH);
    digitalWrite (bkw, LOW);
  } else {
    digitalWrite (fwd, LOW);
    digitalWrite (bkw, HIGH);
  }

  analogWrite(en, speed);
}

class App {
  private:
    void reset() {
      Serial.end();
      Serial.begin(9600);
      readStub();
      status = S_CREATED;

      digitalWrite(LED_BUILTIN, LOW);   // turn the LED on (HIGH is the voltage level)
    }

    void readStub() {
      while (Serial.available()) {
        Serial.read();
      }
    }
    void sendWord(word data) {
      int len = Serial.write(highByte(data)); // send the high byte
      len += Serial.write(lowByte(data));  // send the low byte
    }

    void sendByte(byte data) {
      Serial.write(data);
    }

    void sendBytes(byte* data, const int size) {
      for (int idx = 0; idx < size; idx++) {
        sendByte(data[idx]);  // send the low byte
      }
    }

    word readWord() {
      word res = 0;
      Serial.readBytes((byte*)&res, sizeof(word));
      return  (word)(((res << 8) & 0xFF00) + ((res >> 8) & 0x00FF));
    }

    enum Status {
      S_CREATED,
      S_CONNECTED,
    };

    Status status;
  public:
    void create() {
      pinMode(LED_BUILTIN, OUTPUT);
      reset();

      engineSetup();
    }

    void process() {
      if (status == S_CREATED) {
        sendWord(MAGIC_WORD);
        while (Serial.available() < 2) {}
        if (readWord() != MAGIC_WORD) {
          return;
        }

        sendWord(MAGIC_WORD + 1);

        status = S_CONNECTED;
      } else {
        if (Serial.available() < PACKET_SIZE) {
          return;
        }

        if (readWord() != MAGIC_WORD) {
          reset();
          return;
        }
        ;
        switch (readWord()) {
          case REQ_ENGINE_MOVE:
            {
              byte engine = Serial.read();
              byte dir = Serial.read();
              short speed = readWord();
              engineMove(engine, dir, speed);
            }
            break;
          case REQ_LED_BLINK:
            {
              digitalWrite(LED_BUILTIN, HIGH);
              delay(1000);
              digitalWrite(LED_BUILTIN, LOW);
              delay(1000);

              digitalWrite(LED_BUILTIN, HIGH);
              delay(2000);
              digitalWrite(LED_BUILTIN, LOW);
              delay(500);
            }
            break;
          case REQ_KEEP_ALIVE:
            {
              digitalWrite(LED_BUILTIN, HIGH);
              delay(1000);
              digitalWrite(LED_BUILTIN, LOW);
              delay(1000);
            }
            break;
        }
        readStub();

        sendWord(MAGIC_WORD - 1);
      }
    }
};

App app;

void setup() {
  app.create();
}

void loop() {
  app.process();
}
