import java.io.FileWriter;
import java.io.IOException;

public class WriteXE {
  public void writeFile(ReadXE readxe, String fileName) throws IOException {

    FileWriter obj = new FileWriter(fileName.replaceFirst(".asm", "")+ ".obj");
    int totalLength = readxe.location - readxe.start;
    System.out.printf("H%-6s%06X%06X\n", readxe.codeName, readxe.start, totalLength);
    obj.write(String.format("H%-6s%06X%06X\n", readxe.codeName, readxe.start, totalLength));
    for(int i = 0; i<=readxe.cardTotal; i++){
      if(readxe.cardLength[i] != 0){
        obj.write(String.format("T%06X%02X%s\n", readxe.tStart[i] , readxe.cardLength[i], readxe.cards[i]));
        System.out.printf("T%06X%02X%s\n", readxe.tStart[i] , readxe.cardLength[i], readxe.cards[i]);

      }
    }
    for(int i = 0; i<=readxe.mCardTotal; i++){
      if(readxe.mCards[i] != null){
        obj.write(String.format("M%s\n", readxe.mCards[i]));
        System.out.printf("M%s\n", readxe.mCards[i]);

      }
    }
    obj.write(String.format("E%06X\n", readxe.startPosition));
    System.out.printf("E%06X\n", readxe.startPosition);
    obj.flush();
    obj.close();
  }
}
