package com.interview.parser.forex;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RingBufferTest {

  private RingBuffer ringBuffer;

  @Before
  public void setUp() {
    ringBuffer = new RingBuffer(4);
  }

  @After
  public void tearDown() {
    ringBuffer = null;
  }

  @Test
  public void putTestOneElementWithoutThread() throws Exception {
    ringBuffer.put(5);
    ringBuffer.put(6);

    assertEquals(2, ringBuffer.currentSize);
  }

  @Test
  public void getTestOneElementWithoutThread() throws Exception {
    ringBuffer.put(5);
    ringBuffer.put(6);

    //it should return five by FIFO rule
    assertEquals(5, ringBuffer.get());
  }

  @Test
  public void putTestWithMultipleThreads() throws Exception {
    // Buffer size with 1
    ringBuffer = new RingBuffer(1);
    //Put one value into queue
    Thread t1 = new Thread(() -> {
      try {
        ringBuffer.put(3);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    //Get one value from queue
    Thread t2 = new Thread(() -> {
      try {
        ringBuffer.put(7);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    //Get one value from queue
    Thread t3 = new Thread(() -> {
      try {
        ringBuffer.get();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    // Start all threads
    t1.start();
    t2.start();
    t3.start();

    // 3 threads are trying to put element and one gets
    t1.join();
    t2.join();
    t3.join();

    assertEquals(1, ringBuffer.currentSize);
  }

  @Test
  public void getTestWithMultipleThreads() throws Exception {
    // Buffer size with 1
    ringBuffer = new RingBuffer(1);
    //Put one value into queue
    Thread t1 = new Thread(() -> {
      try {
        ringBuffer.get();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    //Get one value from queue
    Thread t2 = new Thread(() -> {
      try {
        ringBuffer.put(7);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    // Start all threads
    t1.start();
    t2.start();

    // 1 thread are trying to get element, blocked until there is an element
    t1.join();
    t2.join();

    assertEquals(0, ringBuffer.currentSize);
  }

}
