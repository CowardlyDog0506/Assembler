import java.io.*;
import java.nio.charset.StandardCharsets;

public class Read {

  String codeName = "";
  int location = 0;
  int start = 0;

  String[] words = new String[3];
  int[] cardLength = new int[99];
  Database Database = new Database();
  int line = 1;


  public void pass1(String fileName){
    String text = "";
    try{

      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);
      while (br.ready() ) {
        text = br.readLine();
        words = text.replace("\t", "    ").trim().split("\\s+");
        if (text.replace("\t", "    ").trim().isEmpty() || words[0].charAt(0) == '.'){
          continue;
        }

        if(!Database.order.containsKey(words[0]) ){
          Database.symTab.put(words[0], location);

        }

        if(words.length > 1) {
          //建立symTab
          switch (words[1]) {
            case "START" -> {
              codeName = words[0];
              location = Integer.parseInt(words[2], 16);
              start = location;
              cardLength[0] = location;
            }
            case "RESB" -> location = location + (Integer.parseInt(words[2]));
            case "BYTE" -> {
              String wordString = words[2].substring(words[2].indexOf("'")+1, words[2].lastIndexOf("'"));
              if(words[2].charAt(0) =='C'){
                byte[] bytes = wordString.getBytes(StandardCharsets.US_ASCII);
                location = location +bytes.length;
              }else {
                location = location +wordString.length()/2;
              }
            }
            case "RESW" -> location = location + 3 * (Integer.parseInt(words[2]));
            default -> location = location + 3;
          }

        } else{
          location = location + 3;
        }
        line += 1;
      }

      fr.close();

    }catch (Exception exception){
      System.out.println("Line " + line + "\t\"" + text + "\"  WRONG!!");
    }
  }
  //
  int[] tStart = new int[99];
  String[] cards = new  String[99];

  int  startPosition = 0;
  int cardTotal = 0;

  public void pass2(String fileName){
    String text = "";
    try {
      String obj;
      location = start;
      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);
      FileWriter list = new FileWriter(fileName.replaceFirst(".asm", "")+ ".lst");
      //讀取資料
      while (br.ready() ) {

        text = br.readLine();

        words = text.replace("\t", "    ").trim().split("\\s+");
        //判斷是否為註解
        if(text.replace("\t", "    ").trim().isEmpty() || words[0].charAt(0) == '.'){
          System.out.println(text);
          list.write(text+"\n");
          continue;
        } else if(Database.order.containsKey(words[0])) {
          if(words[1].split(",").length == 2 && words[1].split(",")[1].equals("X")){
            obj = String.format("%06X", Integer.parseInt(Database.order.get(words[0]), 16)*65536 + Database.symTab.get(words[1].split(",")[0]) + 32768);
          }else{
            obj = String.format("%06X", Integer.parseInt(Database.order.get(words[0]), 16)*65536 + Database.symTab.get(words[1]));
          }
          Database.objCode.put(location, obj);
        } else if(Database.order.containsKey(words[1])) {
          if(words[2].split(",").length == 2 && words[2].split(",")[1].equals("X")){
            obj = String.format("%06X",Integer.parseInt(Database.order.get(words[1]), 16)*65536 + Database.symTab.get(words[2].split(",")[0]) + 32768);

          }else{
            obj = String.format("%06X", Integer.parseInt(Database.order.get(words[1]), 16)*65536  + Database.symTab.get(words[2]));
          }
          Database.objCode.put(location, obj);
        }

        StringBuilder exception = new StringBuilder();
        //判斷是否為WORD和BYTE，再判斷是X還是C
        if(words[1].equals("WORD")) {
          switch (words[2].charAt(0)) {
            case 'C' -> {
              String wordString = words[2].substring(words[2].indexOf("'")+1, words[2].lastIndexOf("'"));
              byte[] bytes = wordString.getBytes(StandardCharsets.US_ASCII);
              for(byte oneByte : bytes){
                exception.append(String.format("%X", oneByte));
              }
              Database.objCode.put(location, exception.toString());
            }
            case 'X' -> {

              exception = new StringBuilder(words[2].substring(words[2].indexOf("'") + 1, words[2].lastIndexOf("'")));
              if(exception.length() % 2 !=0 || exception.length() > 6){
                System.out.println("Wrong");
                break;
              }
              exception.append("0".repeat(Math.max(0, 6 - exception.length())));
              Database.objCode.put(location, exception.toString());
            }

            default -> {
              exception = new StringBuilder(String.format("%06X", Integer.valueOf(words[2])));
              exception = new StringBuilder(exception.substring(exception.length() - 6));
              Database.objCode.put(location, exception.toString());
            }
          }
          putObjCode();
          System.out.printf("%05X\t", location);
          System.out.printf("%6S\t", Database.objCode.get(location));
          System.out.println(text);

          list.write(String.format("%05X\t", location));
          list.write(String.format("%6S\t", Database.objCode.get(location)));
          list.write(text+"\n");
        }else if(words[1].equals("BYTE")) {
          switch (words[2].charAt(0)) {
            case 'C' -> {
              String wordString = words[2].substring(words[2].indexOf("'")+1, words[2].lastIndexOf("'"));
              byte[] bytes = wordString.getBytes();
              for(byte oneByte : bytes){
                exception.append(Integer.toHexString(oneByte));
              }
              Database.objCode.put(location, exception.toString());
            }
            case 'X' -> {

              exception = new StringBuilder(words[2].substring(words[2].indexOf("'") + 1, words[2].lastIndexOf("'")));
              Database.objCode.put(location, exception.toString());
            }
            default -> {
              exception.append(String.format("%X", Integer.valueOf(words[2])));
              Database.objCode.put(location, exception.toString());
            }

          }
          putObjCode();
          System.out.printf("%05X\t", location);
          System.out.printf("%-6S\t", Database.objCode.get(location));
          System.out.println(text);

          list.write(String.format("%05X\t", location));
          list.write(String.format("%-6S\t", Database.objCode.get(location)));
          list.write(text+"\n");
        }else{
          putObjCode();

          System.out.printf("%05X\t", location);
          list.write(String.format("%05X\t", location));
          if(Database.objCode.containsKey(location)){
            System.out.printf("%6S\t", Database.objCode.get(location));
            list.write(String.format("%6S\t", Database.objCode.get(location)));
          }else {
            System.out.print("      \t");
            list.write("      \t");
          }
          System.out.println(text);
          list.write(text+"\n");
        }

        if(words[0].equals("END") ) {
          if(Database.symTab.containsKey(words[1])){
            startPosition = Database.symTab.get(words[1]);
          }else{
            startPosition = start;
          }
          continue;
        }else if(words[1].equals("END")){
          if(Database.symTab.containsKey(words[2])){
            startPosition = Database.symTab.get(words[2]);
          }else{
            startPosition = start;
          }
          continue;
        }
        //計算location
        switch (words[1]) {
          case "START" -> location = Integer.parseInt(words[2], 16);
          case "RESB" -> location = location + (Integer.parseInt(words[2]));
          case "BYTE" -> {
            String wordString = words[2].substring(words[2].indexOf("'")+1, words[2].lastIndexOf("'"));
            if(words[2].charAt(0) =='C'){
              byte[] bytes = wordString.getBytes(StandardCharsets.US_ASCII);
              location = location +bytes.length;
            }else {
              location = location +wordString.length()/2;
            }
          }
          case "RESW" -> location = location + 3 * (Integer.parseInt(words[2]));

          default -> location = location + 3;

        }
        line += 1;
      }
      list.close();
      fr.close();
    }catch (Exception exception){
      System.out.println("Line " + line + "\t\"" + text + "\"  WRONG!!");
    }
  }
  //建立卡片
  public void putObjCode(){
    if(cards[cardTotal] == null){
      cards[cardTotal] = "";
    }
    if((cards[cardTotal]+String.format("%S", Database.objCode.get(location))).length()<=60){
      if (Database.objCode.containsKey(location)){
        if(cards[cardTotal].equals("")){
          tStart[cardTotal] =location;
        }
        cards[cardTotal] = cards[cardTotal]+String.format("%S", Database.objCode.get(location));
      }else if(cardTotal==0){
        tStart[cardTotal] = start;
        cardLength[cardTotal] = (cards[cardTotal].length())/2;
        cardTotal += 1;
        cards[cardTotal] = "";
      }else{
        cardLength[cardTotal] =  (cards[cardTotal].length())/2;
        cardTotal += 1;
        cards[cardTotal] = "";
      }
    }else{
      if (Database.objCode.containsKey(location) && cardTotal==0){
        cardLength[cardTotal] = (cards[cardTotal].length()) / 2;
        cardTotal += 1;
        tStart[cardTotal] =location;
        cards[cardTotal] = "";
        cards[cardTotal] = cards[cardTotal]+String.format("%S", Database.objCode.get(location));
      }else if(Database.objCode.containsKey(location)){
        cardLength[cardTotal] = cardLength[cardTotal] = (cards[cardTotal].length())/2;
        cardTotal += 1;
        tStart[cardTotal] =location;
        cards[cardTotal] = "";
        cards[cardTotal] = cards[cardTotal]+String.format("%S", Database.objCode.get(location));
      }

    }
  }
}
