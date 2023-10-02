import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

  public static void main(String[] args) throws IOException {

    try {
      Scanner key = new Scanner(System.in);
      String fileName = key.nextLine();
      Read reaFile = new Read();
      ReadXE readXE = new ReadXE();
      FileReader fr = new FileReader(fileName);

      System.out.println("========================================");
      System.out.println("(List)");
      /*
      reaFile.pass1(fileName);
      reaFile.line = 1;
      reaFile.pass2(fileName);
      */
      readXE.pass1(fileName);
      readXE.line = 1;
      readXE.pass2(fileName);

      System.out.println("========================================");
      System.out.println("(OBJcode)");
      //Write write = new Write();
      WriteXE writeXE = new WriteXE();
      writeXE.writeFile(readXE, fileName);

    }catch (Exception exception){
      System.out.println("檔案不存在");
    }


  }

}
