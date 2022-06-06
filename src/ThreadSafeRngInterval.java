import java.util.concurrent.BlockingQueue;

public class ThreadSafeRngInterval extends ThreadSafeRng{
  
  private long interval;

  public ThreadSafeRngInterval(int lower,int higher,int numberOfInts,BlockingQueue<Integer> bq,long milisecInterval){
    super(lower,higher,numberOfInts,bq);
    this.interval=interval;
  }

  @Override
  public void run(){
    Thread current=Thread.currentThread(); // not calling a function more than once...
    while(this.produced++<this.numberOfInts || numberOfInts==-1){
      try{
        super.generate();
        current.sleep(this.interval);
      }catch(InterruptedException ie){
        System.err.println("[ThreadSafeRngInterval] - "+ie.getMessage());
      }
    }
  }
 
}
