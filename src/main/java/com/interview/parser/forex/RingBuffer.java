package com.interview.parser.forex;

public class RingBuffer {

  public final Object[] data;
  int frontRef, tailRef, currentSize;

  public RingBuffer(int max_buf_size) {
    data = new Object[max_buf_size];
  }

  public synchronized void put(Object object) throws Exception {
    while (currentSize == data.length) {
      System.out.println("Buffer is full wait until there is a empty space");
      wait(100);
    }
    System.out.println("New element added to queue - "
        + object);
    data[frontRef] = object;
    if (++frontRef == data.length) {
      frontRef = 0;
    }
    ++currentSize;
    notifyAll();
    Thread.sleep(1000);

  }

  public synchronized Object get() throws Exception {
    while (currentSize == 0) {
      System.out.println("Buffer is empty wait until there is an element");
      wait(100);
    }
    Object object = data[tailRef];
    if (++tailRef == data.length) {
      tailRef = 0;
    }
    System.out.println("Remove element from queue - "
        + object);
    --currentSize;
    notifyAll();
    Thread.sleep(1000);
    return object;
  }


}
