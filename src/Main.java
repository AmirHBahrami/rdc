import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class Main{

  public static void main(String args[]){

    Map<String,Integer> argses=parseArgs(args);

    if(argses.containsKey("HELP_NEEDED")){
      System.out.println(
        "\nRandom Consumer (rdc) - by @AmirHBahrami (github)\n"
        +"  this program generates random numbers and shows them\n"
        +"  (with an optional delay) on the screen. you can bind the\n"
        +"  output to your desired file to have the numbers\n"
        +"  flags:\n"
        +"    -h || --help : show this help and don't run the program\n"
        +"    -a || --accelerate : show the input as if you're in a spaceship, traveling at the speed of light!\n"
        +"    -v || --verbose : show the index of numbers generated\n"
        +"    -n || --number : how many numbers should be generated (default:10) -n -1 mean inifinit numbers\n"
        +"    -i || --interval : how long in milisec before the next input is shown (default:50)\n"
        +"    -l || --lower-limit : lowest number generated (default:100)\n"
        +"    -u || --upper-limit : highest number generated (default:10)\n"
        +"  example:  java Main -u 200 --lower-limit 50 -i 1000 -n -1\n"
        +"cheers!\n"
      );
      System.exit(0);
    }
    
    // setting arguments
    int interval=argses.containsKey("interval")?argses.get("interval"):50;
    int lowerLimit=argses.containsKey("lower-limit")?argses.get("lower-limit"):10;
    int upperLimit=argses.containsKey("upper-limit")?argses.get("upper-limit"):100;
    int number=argses.containsKey("number")?argses.get("number"):10;
    boolean verbose=argses.containsKey("verbose");
    boolean accelerate=argses.containsKey("accelerate");
    
    // check if limits are correct
    int temp;
    if(lowerLimit>upperLimit){
      temp=upperLimit;
      upperLimit=lowerLimit;
      lowerLimit=temp;
    }
      
    Thread rngThread;
    BlockingQueue<Integer> bq;
    try{
      
      bq=new ArrayBlockingQueue(100);
      rngThread=new Thread(
          new ThreadSafeRngInterval(lowerLimit,upperLimit,number,bq,interval));
      rngThread.start();
      
      // only testing, this package only deals with RNG and not logging...
      Integer current=null;
      int i=1;
      int tabCount=0;
      boolean lightSpeed=false;
      while(true){
        Thread.currentThread().sleep(interval);
        if( (current=bq.poll()) !=null){

          // accelerator!
          for(int j=0;accelerate && j<tabCount;++j)
            System.out.print('\f');

          // print index
          if(verbose)
            System.out.print(i+":");

          // change accelerator
          if(accelerate && i>=5)
            tabCount+=1;

          // main functionality
          System.out.print(current);
          if(accelerate && tabCount>=50)
            lightSpeed=true;

          if(lightSpeed)
            System.out.print("\t[SPEED OF LIGHT]");
          
          System.out.print('\n');

          i++; // unless 10 entries are done, this isn't happening
        }
        else break;
      }

    }catch(InterruptedException ie){
      System.err.println("the program is down!"); // you can pipe the System.err to a file? so that it logs...
    }
    
  }

  public static Map<String,Integer> parseArgs(String[] args){
    Map<String,Integer> map=new HashMap<String,Integer>();
    for(int i=0;i<args.length;i++){
      try{

        // System.out.println("checking:"+args[i]);

        /* in milisec */
        if(args[i].equals("-i") || args[i].equals("--interval"))
          map.put("interval",Integer.parseInt(args[i+1]));
        
        else if(args[i].equals("-l") || args[i].equals("--lower-limit"))
          map.put("lower-limit",Integer.parseInt(args[i+1]));

        else if(args[i].equals("-a") || args[i].equals("--accelerate"))
          map.put("accelerate",1);

        else if(args[i].equals("-n") || args[i].equals("--number"))
          map.put("number",Integer.parseInt(args[i+1]));

        else if(args[i].equals("-u") || args[i].equals("--upper-limit"))
          map.put("upper-limit",Integer.parseInt(args[i+1]));

        else if(args[i].equals("-v") || args[i].equals("--verbose"))
          map.put("verbose",1);

        else if(args[i].equals("-h") || args[i].equals("--help"))
          map.put("HELP_NEEDED",1);
        
        /* check input on the spot */
        if(map.containsKey(args[i]) && map.get(args[i])<=0)
          throw new ArithmeticException();

      }catch(ArithmeticException ae){
        System.err.println("incorrect input:"+args[i]);
        System.exit(1);
      }
    }
    return map;
  }

}
