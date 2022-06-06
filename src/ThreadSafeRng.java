import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.BlockingQueue;

/**
 *  A Runnable to generate random integers and populate a BlockingQueue with it.
 *  You can explicitly set the number of random integers it generates.
 *  Pass -1 to indicate an infinite loop.
 *  * */
public abstract class ThreadSafeRng implements Runnable{

  protected int lower;
  protected int higher;
  protected int numberOfInts;
  protected int produced;

  protected final BlockingQueue<Integer> bq;

  protected ThreadLocalRandom currentRandom;

  public ThreadSafeRng(int lower,int higher,int numberOfInts,BlockingQueue<Integer> bq){
    this.lower=lower;
    this.higher=higher;
    this.bq=bq; // should come from outside,since duh!
    this.numberOfInts=numberOfInts;
    this.produced=0;
    this.currentRandom=ThreadLocalRandom.current(); 
  }
  
  @Override
  public void run(){
    
    while( produced++<numberOfInts || numberOfInts==-1 ){
      try{
        this.generate();
      }catch(NullPointerException npe){
        System.err.println("[ThreadSafeRng] - "+npe.getMessage());
      }catch(IllegalArgumentException iae){
        System.err.println("[ThreadSafeRng] - "+iae.getMessage());
      }catch(ClassCastException cce){
        System.err.println("[ThreadSafeRng] - "+cce.getMessage());
      }
    }

  }
  
  /*
   *  Method that populates the BlockingQueue.
   *  NullPointerException is thrown where the input of 
   *  bq.offer is null.
   *  @return void
   *  @param void
   * */
  public void generate(){
    this.bq.offer(
        new Integer(
          this.higher-(this.currentRandom.nextInt(higher))+lower));
  }

}
