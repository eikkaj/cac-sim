import java.util.Arrays;

public class SlotNode {
       public int validBit;
       public int tag;
       public int []dataBlock = new int[8];
       public int dirty;
       public int startAddress;

       public int getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(int startAddress) {
		this.startAddress = startAddress;
	}

	public SlotNode() {

       }

       public SlotNode(int validBit, int tag, int data, int dirty){

       }

       public int getValidBit() {
               return validBit;
       }
       public void setValidBit(int validBit) {
               this.validBit = validBit;
       }
       public int getTag() {
               return tag;
       }
       public void setTag(int tag) {
               this.tag = tag;
       }
 
 public int[] getDataBlock() {
		return dataBlock;
	}

	public void setDataBlock(int[] dataBlock) {
		this.dataBlock = dataBlock;
	}

	public int getDirty() {
               return dirty;
       }
       public void setDirty(int dirty) {
               this.dirty = dirty;
       }
       
       public void clearData() {
     	  Arrays.fill(this.dataBlock,0);
       }



}