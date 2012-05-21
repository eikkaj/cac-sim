import java.util.Scanner;
public class cacheSim {

      public static String input;
      public static int address;
      public static int startAddress;
      public static int offset;
      public static int tag;
      public static int slot;
     
      public static SlotNode[] cache = new SlotNode[8];
      public static int cacheSize = 8;
      public static int memSize = 2048;
      public static int[] main_Mem = new int[memSize];
      
      static Scanner keyboard = new Scanner(System.in);

      public static void menu() {

 
              System.out.println("[r] to read   [w] to write   [d] to display");
              input = keyboard.next();
              if (input.equals("r")) {
                      readAddress();
              }
              else if (input.equals("w")) {
                      writeAddress();
              }
              else if (input.equals("d")) {
                      display();
              }
              else {
                      System.out.println("Bad input try again");
                      menu();
              }

      }

      public static void readAddress() {
              System.out.println("What address? ");
            
              address = keyboard.nextInt(16);
              startAddress = address & 0x758;
              tag = (address >> 6) & 0x1F;
              slot = (address >> 3) & 0x7;

              //Valid bit is 0, empty slot--MISS
              if (cache[slot].getValidBit() == 0) {       	     
   
                  cache[slot].setValidBit(1);
                  cache[slot].setTag(tag);
                  cache[slot].setStartAddress(startAddress);
                  //block from main to cache
                  System.arraycopy(main_Mem, main_Mem[startAddress], cache[slot].dataBlock, 0, cacheSize);
            	  
                  System.out.println("Cache Miss");
                  System.out.print("The value at that address is: ");
                  System.out.printf("%X", 0xFF & address);
                  System.out.println();
                  System.out.println();
              }
              //Valid bit 1 but tags don't match--MISS
              else if (cache[slot].getValidBit() == 1 && cache[slot].getTag() != tag) {
                  System.out.println("Cache Miss ");
                  
                  if (cache[slot].getDirty() == 1) {
                	  //copy contents of slot back into main memory before loading new block
                	 System.arraycopy(cache[slot].dataBlock, 0, main_Mem, cache[slot].getStartAddress(), cacheSize);
                     cache[slot].setDirty(0);
                  }
                  //set up new block
                  startAddress = address & 0x7F8;
                  System.out.println("Start Address " +startAddress);
                  cache[slot].setTag(tag);
                  cache[slot].setStartAddress(startAddress);

                  //from main to cache
                //  System.out.println("Trying to copy from main to cache properly");
                //  System.out.println("Length " +main_Mem.length);
                 // System.out.println("Start Index " + main_Mem[startAddress]);
                 // System.out.println("Length of cache " + cache[slot].dataBlock.length);
                 // System.out.println(cacheSize);
                 System.arraycopy(main_Mem, cache[slot].getStartAddress(), cache[slot].dataBlock, 0, cacheSize);
              }
              
              //Valid bit 1 and tags match, hit
              else if (cache[slot].getValidBit() == 1 && tag == cache[slot].getTag()) {
            	  System.out.println("Cache Hit");
            	  System.out.print("The value at that address is: ");
                  System.out.printf("%X", 0xFF & address);
                  System.out.println();
                  System.out.println();
              }

              menu();
      }

      public static void writeAddress() {

    	  	System.out.println("What address do you want to write to? ");
    	  	address = keyboard.nextInt(16);
    	  	System.out.println("And what value do you want to write to it? ");
    	  	int input = keyboard.nextInt(16);
    	  
    	  	startAddress = address & 0x758;
    	  	tag = (address >> 6) & 0x1F;
    	  	slot = (address >> 3) & 0x7;
    	  	//Valid bit 1, tag matches, hit, just modify value
    	  	if (cache[slot].getValidBit() != 0 && cache[slot].getTag() == tag) {
    	  		System.out.println("Cache Hit");
    	  		System.out.printf("%X", 0xFF & address);

    	  		for (int i = 0; i <8; i++) {
    	  			if (cache[slot].dataBlock[i] == (0xFF & address)) {
    	  				cache[slot].dataBlock[i] = input;
    	  				cache[slot].setDirty(1);
    	  			}
    	  		}	
    	  	}
    	  	//Valid bit 1, tags don't match-Check dirty bit and write back first if valid
    	  	else if (cache[slot].getValidBit() == 1 && cache[slot].getTag() != tag) { 
    	  		
    	  		if (cache[slot].getDirty() ==1) {
    	  			//copy from cache to main
    	  			System.arraycopy(cache[slot].dataBlock, 0, main_Mem, cache[slot].getStartAddress(), cacheSize);

    	  			//System.arraycopy(cache[slot].dataBlock, 0, main_Mem, cache[slot].getStartAddress(), cacheSize);
    	  		}
    	  		
    	  		System.out.println("Cache Miss");
                cache[slot].setValidBit(1);
                cache[slot].setTag(tag);
                cache[slot].setDirty(1);
                cache[slot].setStartAddress(startAddress);
                //copy from main to cache
                System.arraycopy(main_Mem, main_Mem[startAddress], cache[slot].dataBlock, 0, cacheSize);
                
                for (int i = 0; i <8; i++) {
    	  			if (cache[slot].dataBlock[i] == (0xFF & address)) {
    	  				cache[slot].dataBlock[i] = input;
    	  			}
    	  		}
                
    	  	}
    	  	//Empty slot, no need to write back
    	  	else if (cache[slot].getValidBit() == 0) {
    	  		System.out.println("Cache Miss");
    	  		
                cache[slot].setValidBit(1);
                cache[slot].setTag(tag);
                cache[slot].setDirty(1);
                cache[slot].setStartAddress(startAddress);
                //copy from main to cache
                System.arraycopy(main_Mem, main_Mem[startAddress], cache[slot].dataBlock, 0, cacheSize);
                
                //writes to selected value in the cache
                for (int i = 0; i <8; i++) {
    	  			if (cache[slot].dataBlock[i] == (0xFF & address)) {
    	  				cache[slot].dataBlock[i] = input;
    	  			}
    	  		}
    	  	}
    	  	menu();
      }

      public static void display() {
    	  System.out.println("Slot  Valid  Tag     Data");
    	  for (int i = 0; i < 8; i++) {
    		  System.out.print(i);
    		  System.out.print("     ");
    		  System.out.print(cache[i].getValidBit());
    		  System.out.print("      ");
    		  System.out.printf("%X", cache[i].getTag());
    		  System.out.print("       ");
    		  for (int j = 0; j <cacheSize; j++) {
    			  System.out.printf("%X", cache[i].dataBlock[j]);
    			  System.out.print(" ");
    		  }
    		  System.out.println();
    	  }
    	  menu();
      }
      
 
      
      public static void main(String[] args) {


              //initialize main memory
              for (int i = 0; i<main_Mem.length; i++) {
                      main_Mem[i] = (0xFF & i);
                    // System.out.printf("%X", 0xFF & i);
                     //System.out.print("      " + i);
                    // System.out.println(" ");

              }

              //initialize cache slots to 0
              for (int i = 0; i<cache.length; i++) {
                      cache[i] = new SlotNode();
                      cache[i].setValidBit(0);
                      cache[i].setTag(0);
                      cache[i].setDirty(0);
              }
              //clears array to 0 that is block of data
              for (int i = 0; i<cache.length; i++) {
              cache[i].clearData();
              }
              //bring up menu
              menu();
      }
}