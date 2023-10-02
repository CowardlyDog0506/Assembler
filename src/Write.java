import java.io.FileWriter;
import java.io.IOException;

public class Write {
  public void writeFile(Read read, String fileName) throws IOException {

    FileWriter obj = new FileWriter(fileName.replaceFirst(".asm", "")+ ".obj");
    int totalLength = read.location - read.start;
    System.out.printf("H%-6s%06X%06X\n", read.codeName, read.start, totalLength);
    obj.write(String.format("H%-6s%06X%06X\n", read.codeName, read.start, totalLength));

    for(int i = 0; i<=read.cardTotal; i++){
      if(read.cardLength[i] != 0){
        obj.write(String.format("T%06X%02X%s\n", read.tStart[i] , read.cardLength[i], read.cards[i]));
        System.out.printf("T%06X%02X%s\n", read.tStart[i] , read.cardLength[i], read.cards[i]);

      }
    }

    obj.write(String.format("E%06X\n", read.startPosition));
    System.out.printf("E%06X\n", read.startPosition);
    obj.flush();
    obj.close();
  }
}
