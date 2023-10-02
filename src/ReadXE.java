import java.io.*;
import java.nio.charset.StandardCharsets;
public class ReadXE {

  String codeName = "";
  int location = 0;
  int start = 0;
  int pc = 0;
  int base = 0;
  int isBase = 0;
  int m = 0;
  String[] words = new String[3];
  int[] cardLength = new int[999];
  Database Database = new Database();
  int line = 1;

  public boolean isNum(String str){
    boolean isNumeric = true;
    for (int i = 0; i < str.length(); i++) {
      if (!Character.isDigit(str.charAt(i))) {
        isNumeric = false;
      }
    }
    return isNumeric;
  }
  public void pass1(String fileName) throws IOException {
    String text = "";

    try {
      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);
      while (br.ready()) {
        text = br.readLine();
        words = text.replace("\t", "    ").trim().split("\\s+");
        if (text.replace("\t", "    ").trim().isEmpty() || words[0].charAt(0) == '.') {
          continue;
        }

        if (!Database.order.containsKey(words[0]) && !Database.order.containsKey("+" + words[0]) && !Database.order2.containsKey(words[0])) {
          Database.symTab.put(words[0], location);

        }

        if (words.length > 1) {
          //建立symTab
          if (Database.order2.containsKey(words[0]) || Database.order2.containsKey(words[1])) {
            location = location + 2;
          } else if (Database.order.containsKey(words[0]) || words[0].equals("RESB") || words[0].equals("RESW") || words[0].equals("BYTE") || words[0].equals("WORD")) {
            switch (words[0]) {
              case "START" -> {
                codeName = "";
                location = Integer.parseInt(words[1]);
                start = location;
                cardLength[0] = location;
              }
              case "RESB" -> location = location + (Integer.parseInt(words[1]));
              case "BYTE" -> {
                String wordString = words[1].substring(words[1].indexOf("'") + 1, words[2].lastIndexOf("'"));
                if (words[2].charAt(0) == 'C') {
                  byte[] bytes = wordString.getBytes(StandardCharsets.US_ASCII);
                  location = location + bytes.length;
                } else {
                  location = location + wordString.length() / 2;
                }
              }
              case "RESW" -> location = location + 3 * (Integer.parseInt(words[1]));
              default -> {
                location = location + 3;
              }
            }
          } else if (Database.order.containsKey(words[1]) || words[1].equals("RESB") || words[1].equals("RESW") || words[1].equals("BYTE") || words[1].equals("WORD")) {
            switch (words[1]) {
              case "START" -> {
                codeName = words[0];
                location = Integer.parseInt(words[2]);
                start = location;
                cardLength[0] = location;
              }
              case "RESB" -> location = location + (Integer.parseInt(words[2]));
              case "BYTE" -> {
                String wordString = words[2].substring(words[2].indexOf("'") + 1, words[2].lastIndexOf("'"));
                if (words[2].charAt(0) == 'C') {
                  byte[] bytes = wordString.getBytes(StandardCharsets.US_ASCII);
                  location = location + bytes.length;
                } else {
                  location = location + wordString.length() / 2;
                }
              }
              case "RESW" -> location = location + 3 * (Integer.parseInt(words[2]));
              default -> {
                location = location + 3;
              }
            }
          } else if (Database.order4.containsKey(words[0]) || Database.order4.containsKey(words[1])) {
            location = location + 4;
          } else if (words[1].equals("START")) {
            codeName = words[0];
            location = Integer.parseInt(words[2]);
            start = location;
            cardLength[0] = location;

          } else if (words[0].equals("START")) {
            codeName = "";
            location = Integer.parseInt(words[1]);
            start = location;
            cardLength[0] = location;
          }

        } else {
          if (words[0].equals("BASE")) {
            System.out.println("AAAAAAAAAAAAAA");
          }
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

  int[] tStart = new int[999];
  String[] cards = new  String[999];
  String[] mCards = new  String[999];
  int  startPosition = 0;
  int cardTotal = 0;
  int mCardTotal = 0;
  public void pass2(String fileName) throws IOException {
    String text = "";
    try {

      String obj = null;
      location = start;
      pc = start;
      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);
      FileWriter list = new FileWriter(fileName.replaceFirst(".asm", "") + ".lst");
      //讀取資料
      while (br.ready()) {
        isBase = 0;
        m = 0;
        obj = null;
        text = br.readLine();
        words = text.replace("\t", "    ").trim().split("\\s+");
        //System.out.println(words.length);
        //判斷是否為註解
        if (text.replace("\t", "    ").trim().isEmpty() || words[0].charAt(0) == '.') {
          System.out.println(text);
          list.write(text + "\n");
          continue;
        }
        if (words.length > 1) {
          if (Database.order2.containsKey(words[0])) {
            pc = location + 2;
            if (words[0].equals("TIXR")) {
              obj = String.format("%04X", Integer.parseInt(Database.order2.get(words[0]), 16) * 256 + Database.register.get(words[1].split(",")[0]) * 16);
            } else if (words[1].indexOf(",") == 1 && words[1].length() == 3) {
              obj = String.format("%04X", Integer.parseInt(Database.order2.get(words[0]), 16) * 256 + Database.register.get(words[1].split(",")[0]) * 16 + Database.register.get(words[1].split(",")[1]));
            } else if (words[1].indexOf(",") == 1 && words[1].length() == 2) {
              obj = String.format("%04X", Integer.parseInt(Database.order2.get(words[0]), 16) * 256 + Database.register.get(words[1].split(",")[0]) * 16 + Database.register.get(words[2]));
            }

            Database.objCode.put(location, obj);
          } else if (Database.order2.containsKey(words[1])) {
            pc = location + 2;
            if (words[1].equals("TIXR")) {
              obj = String.format("%04X", Integer.parseInt(Database.order2.get(words[1]), 16) * 256 + Database.register.get(words[2].split(",")[0]) * 16);
            } else if (words[2].indexOf(",") == 1 && words[2].length() == 3) {
              obj = String.format("%04X", Integer.parseInt(Database.order2.get(words[1]), 16) * 256 + Database.register.get(words[2].split(",")[0]) * 16 + Database.register.get(words[2].split(",")[1]));
            } else if (words[2].indexOf(",") == 1 && words[2].length() == 2) {
              obj = String.format("%04X", Integer.parseInt(Database.order2.get(words[1]), 16) * 256 + Database.register.get(words[2].split(",")[0]) * 16 + Database.register.get(words[3]));
            }
            Database.objCode.put(location, obj);
          } else if (Database.order.containsKey(words[0]) || words[0].equals("RESB") || words[0].equals("RESW") || words[0].equals("BYTE") || words[0].equals("WORD")) {
            switch (words[0]) {
              /*
              case "START" -> {
                codeName = "";
                pc = Integer.parseInt(words[1], 16);
                start = location;
                cardLength[0] = location;
              }
              */
              case "RESB" -> pc = location + (Integer.parseInt(words[1]));
              case "BYTE" -> {
                String wordString = words[1].substring(words[1].indexOf("'") + 1, words[2].lastIndexOf("'"));
                if (words[2].charAt(0) == 'C') {
                  byte[] bytes = wordString.getBytes(StandardCharsets.US_ASCII);
                  pc = location + bytes.length;
                } else {
                  pc = location + wordString.length() / 2;
                }
              }
              case "RESW" -> pc = location + 3 * (Integer.parseInt(words[1]));
              default -> {
                pc = location + 3;
              }
            }

            switch (words[1].charAt(0)) {
              case '#' -> {
                if (isNum(words[1].split("#")[1])) {
                  obj = String.format("%06X", Integer.parseInt(Database.order.get(words[0]), 16) * 65536 + 65536 + Integer.parseInt(words[1].split("#")[1]));
                } else {
                  int pcRelative = Database.symTab.get(words[1].split("#")[1]) - pc;
                  int baseRelative = Database.symTab.get(words[1].split("#")[1]) - base;
                  if (pcRelative >= -2048 && pcRelative <= 2047) {
                    if (pcRelative < 0) {
                      pcRelative = 4096 + pcRelative;
                    }
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[0]), 16) * 65536 + 65536 + 8192 + Integer.parseInt(String.valueOf(pcRelative)));
                  } else if (baseRelative > 0 && baseRelative < 4096) {
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[0]), 16) * 65536 + 65536 + 16384 + Integer.parseInt(String.valueOf(baseRelative)));
                  }
                }
              }
              //1049008
              case '@' -> {
                if (isNum(words[1].split("@")[1])) {
                  obj = String.format("%06X", Integer.parseInt(Database.order.get(words[0]), 16) * 65536 + 131072 + Integer.parseInt(words[1].split("@")[1]));
                } else {
                  int pcRelative = Database.symTab.get(words[1].split("@")[1]) - pc;
                  int baseRelative = Database.symTab.get(words[1].split("@")[1]) - base;
                  if (pcRelative >= -2048 && pcRelative <= 2047) {
                    if (pcRelative < 0) {
                      pcRelative = 4096 + pcRelative;
                    }
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[0]), 16) * 65536 + 131072 + 8192 + Integer.parseInt(String.valueOf(pcRelative)));
                  } else if (baseRelative > 0 && baseRelative < 4096) {
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[0]), 16) * 65536 + 131072 + 16384 + Integer.parseInt(String.valueOf(baseRelative)));
                  }
                }
              }
              default -> {
                if (words[1].contains(",")) {
                  int pcRelative = Database.symTab.get(words[1].split(",")[0]) - pc;
                  int baseRelative = Database.symTab.get(words[1].split(",")[0]) - base;
                  if (pcRelative >= -2048 && pcRelative <= 2047) {
                    if (pcRelative < 0) {
                      pcRelative = 4096 + pcRelative;
                    }
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[0]), 16) * 65536 + 131072 + 32768 + 65536 + 8192 + Integer.parseInt(String.valueOf(pcRelative)));
                  } else if (baseRelative > 0 && baseRelative < 4096) {
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[0]), 16) * 65536 + 131072 + 32768 + 65536 + 16384 + Integer.parseInt(String.valueOf(baseRelative)));
                  }
                } else {
                  int pcRelative = Database.symTab.get(words[1]) - pc;
                  int baseRelative = Database.symTab.get(words[1]) - base;
                  if (pcRelative >= -2048 && pcRelative <= 2047) {
                    if (pcRelative < 0) {
                      pcRelative = 4096 + pcRelative;
                    }
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[0]), 16) * 65536 + 131072 + 65536 + 8192 + Integer.parseInt(String.valueOf(pcRelative)));
                  } else if (baseRelative > 0 && baseRelative < 4096) {
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[0]), 16) * 65536 + 131072 + 65536 + 16384 + Integer.parseInt(String.valueOf(baseRelative)));
                  }
                }

              }
            }
            Database.objCode.put(location, obj);
          } else if (Database.order.containsKey(words[1]) || words[1].equals("RESB") || words[1].equals("RESW") || words[1].equals("BYTE") || words[1].equals("WORD")) {
            switch (words[1]) {
              /*
              case "START" -> {
                codeName = words[0];
                pc = Integer.parseInt(words[2], 16);
                start = location;
                cardLength[0] = location;
              }

               */
              case "RESB" -> pc = location + (Integer.parseInt(words[2]));
              case "BYTE" -> {
                String wordString = words[2].substring(words[2].indexOf("'") + 1, words[2].lastIndexOf("'"));
                if (words[2].charAt(0) == 'C') {
                  byte[] bytes = wordString.getBytes(StandardCharsets.US_ASCII);
                  pc = location + bytes.length;
                } else {
                  pc = location + wordString.length() / 2;
                }

              }
              case "RESW" -> {
                pc = location + 3 * (Integer.parseInt(words[2]));

              }
              default -> {
                pc = location + 3;
              }
            }

            switch (words[2].charAt(0)) {
              case '#' -> {
                if (isNum(words[2].split("#")[1])) {
                  obj = String.format("%06X", Integer.parseInt(Database.order.get(words[1]), 16) * 65536 + 65536 + Integer.parseInt(words[2].split("#")[1]));
                } else {
                  int pcRelative = Database.symTab.get(words[2].split("#")[1]) - pc;
                  int baseRelative = Database.symTab.get(words[2].split("#")[1]) - base;
                  if (pcRelative >= -2048 && pcRelative <= 2047) {
                    if (pcRelative < 0) {
                      pcRelative = 4096 + pcRelative;
                    }
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[1]), 16) * 65536 + 65536 + 8192 + Integer.parseInt(String.valueOf(pcRelative), 16));
                  } else if (baseRelative > 0 && baseRelative < 4096) {
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[1]), 16) * 65536 + 65536 + 16384 + Integer.parseInt(String.valueOf(baseRelative), 16));
                  }
                }
              }
              //1049008
              case '@' -> {
                if (isNum(words[2].split("@")[1])) {
                  obj = String.format("%06X", Integer.parseInt(Database.order.get(words[1]), 16) * 65536 + 131072 + Integer.parseInt(words[2].split("@")[1]));
                } else {
                  int pcRelative = Database.symTab.get(words[2].split("@")[1]) - pc;
                  int baseRelative = Database.symTab.get(words[2].split("@")[1]) - base;
                  if (pcRelative >= -2048 && pcRelative <= 2047) {
                    if (pcRelative < 0) {
                      pcRelative = 4096 + pcRelative;
                    }
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[1]), 16) * 65536 + 131072 + 8192 + Integer.parseInt(String.valueOf(pcRelative), 16));
                  } else if (baseRelative > 0 && baseRelative < 4096) {
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[1]), 16) * 65536 + 131072 + 16384 + Integer.parseInt(String.valueOf(baseRelative), 16));
                  }
                }
              }
              default -> {
                if (words[2].contains(",") && Database.order.containsKey(words[1])) {
                  int pcRelative = Database.symTab.get(words[2].split(",")[0]) - pc;
                  int baseRelative = Database.symTab.get(words[2].split(",")[0]) - base;
                  if (pcRelative >= -2048 && pcRelative <= 2047) {
                    if (pcRelative < 0) {
                      pcRelative = 4096 + pcRelative;
                    }
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[1]), 16) * 65536 + 131072 + 32768 + 65536 + 8192 + Integer.parseInt(String.valueOf(pcRelative)));
                  } else if (baseRelative > 0 && baseRelative < 4096) {
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[1]), 16) * 65536 + 131072 + 32768 + 65536 + 16384 + Integer.parseInt(String.valueOf(baseRelative)));
                  }
                } else if (Database.order.containsKey(words[1])) {

                  int pcRelative = Database.symTab.get(words[2]) - pc;
                  int baseRelative = Database.symTab.get(words[2]) - base;
                  if (pcRelative >= -2048 && pcRelative <= 2047) {
                    if (pcRelative < 0) {
                      pcRelative = 4096 + pcRelative;
                    }
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[1]), 16) * 65536 + 131072 + 65536 + 8192 + Integer.parseInt(String.valueOf(pcRelative)));
                  } else if (baseRelative > 0 && baseRelative < 4096) {
                    obj = String.format("%06X", Integer.parseInt(Database.order.get(words[1]), 16) * 65536 + 131072 + 65536 + 16384 + Integer.parseInt(String.valueOf(baseRelative)));
                  }
                }

              }
              //default -> obj = String.format("%06X", Integer.parseInt(Database.order.get(words[1]), 16)*65536 + 1049008 + 65536 + Integer.parseInt(String.valueOf(Database.symTab.get(words[2].split("@")[1])), 16));
            }
            if (Database.order.containsKey(words[1])) {
              Database.objCode.put(location, obj);
            }
          } else if (Database.order4.containsKey(words[0]) || Database.order4.containsKey(words[1])) {
            pc = location + 4;
            if (Database.order4.containsKey(words[0])) {
              switch (words[1].charAt(0)) {
                case '#' -> {
                  if (isNum(words[1].split("#")[1])) {
                    obj = String.format("%08X", Integer.parseInt(Database.order.get(words[0]), 16) * 16777216 + 1048576 + 16777216 + Integer.parseInt(words[1].split("#")[1]));
                  } else {
                    obj = String.format("%08X", Integer.parseInt(Database.order.get(words[0]), 16) * 16777216 + 1048576 + 16777216 + Database.symTab.get(words[1].split("#")[1]));
                  }
                }
                //1049008
                case '@' -> {
                  obj = String.format("%08X", Integer.parseInt(Database.order4.get(words[0]), 16) * 16777216 + 1048576 + 33554432 + Database.symTab.get(words[1].split("@")[1]));
                }
                default -> {
                  if (words[1].contains(",")) {
                    obj = String.format("%08X", Integer.parseInt(Database.order4.get(words[0]), 16) * 16777216 + 1048576 + 16777216 + 33554432 + 8388608 + Database.symTab.get(words[1].split(",")[0]));
                  } else {
                    obj = String.format("%08X", Integer.parseInt(Database.order4.get(words[0]), 16) * 16777216 + 1048576 + 16777216 + 33554432 + Database.symTab.get(words[1]));
                  }
                }
              }

            } else {
              switch (words[2].charAt(0)) {
                case '#' -> {
                  if (isNum(words[2].split("#")[1])) {
                    obj = String.format("%08X", Integer.parseInt(Database.order.get(words[1]), 16) * 16777216 + 1048576 + 16777216 + Integer.parseInt(words[2].split("#")[1]));
                  } else {
                    obj = String.format("%08X", Integer.parseInt(Database.order.get(words[1]), 16) * 16777216 + 1048576 + 16777216 + Database.symTab.get(words[2].split("#")[1]));
                  }
                }
                //1049008
                case '@' -> {
                  obj = String.format("%08X", Integer.parseInt(Database.order4.get(words[0]), 16) * 16777216 + 1048576 + 33, 554, 432 + Database.symTab.get(words[1].split("#")[1]));
                }
                default -> {
                  if (words[2].contains(",")) {
                    obj = String.format("%08X", Integer.parseInt(Database.order4.get(words[1]), 16) * 16777216 + 1048576 + 16777216 + 33554432 + 8388608 + Database.symTab.get(words[2].split(",")[0]));
                  } else {
                    obj = String.format("%08X", Integer.parseInt(Database.order4.get(words[1]), 16) * 16777216 + 1048576 + 16777216 + 33554432 + Database.symTab.get(words[2]));
                  }
                }
              }
            }
            Database.objCode.put(location, obj);
            m = 1;
          } else if (words[0].equals("BASE")) {
            isBase = 1;
            if (Database.symTab.containsKey(words[1])) {
              base = Database.symTab.get(words[1]);
            } else {
              base = Integer.parseInt(words[1].split("#")[1], 16);
            }
          } else if (words[1].equals("BASE")) {
            isBase = 1;
            if (Database.symTab.containsKey(words[2])) {
              base = Database.symTab.get(words[2]);
            } else {
              base = Integer.parseInt(words[2].split("#")[1], 16);
            }
          }
        } else {
          pc = location + 3;
          obj = String.format("%06X", (Integer.parseInt(Database.order.get(words[0]), 16) + 3) * 65536);
          Database.objCode.put(location, obj);
        }

        StringBuilder exception = new StringBuilder();
        //判斷是否為WORD和BYTE，再判斷是X還是C
        if (words.length > 1) {
          if (words[1].equals("WORD")) {
            switch (words[2].charAt(0)) {
              case 'C' -> {
                String wordString = words[2].substring(words[2].indexOf("'") + 1, words[2].lastIndexOf("'"));
                byte[] bytes = wordString.getBytes(StandardCharsets.US_ASCII);
                for (byte oneByte : bytes) {
                  exception.append(String.format("%X", oneByte));
                }
                Database.objCode.put(location, exception.toString());
              }
              case 'X' -> {

                exception = new StringBuilder(words[2].substring(words[2].indexOf("'") + 1, words[2].lastIndexOf("'")));
                if (exception.length() % 2 != 0 || exception.length() > 6) {
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
            list.write(text + "\n");
          } else if (words[1].equals("BYTE")) {
            switch (words[2].charAt(0)) {
              case 'C' -> {
                String wordString = words[2].substring(words[2].indexOf("'") + 1, words[2].lastIndexOf("'"));
                byte[] bytes = wordString.getBytes();
                for (byte oneByte : bytes) {
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
            list.write(text + "\n");
          } else if (m == 1) {
            putObjCode();

            System.out.printf("%05X\t", location);
            list.write(String.format("%05X\t", location));
            if (Database.objCode.containsKey(location)) {
              System.out.printf("%8S", Database.objCode.get(location));
              list.write(String.format("%8S", Database.objCode.get(location)));
            } else {
              System.out.print("      \t");
              list.write("      \t");
            }
            System.out.println(text + "");
            list.write(text + "\n");
          } else {
            putObjCode();

            System.out.printf("%05X\t", location);
            list.write(String.format("%05X\t", location));
            if (Database.objCode.containsKey(location)) {
              System.out.printf("%6S\t", Database.objCode.get(location));
              list.write(String.format("%6S\t", Database.objCode.get(location)));
            } else {
              System.out.print("      \t");
              list.write("      \t");
            }
            System.out.println(text);
            list.write(text + "\n");
          }

          if (words[0].equals("END")) {
            if (Database.symTab.containsKey(words[1])) {
              startPosition = Database.symTab.get(words[1]);
            } else {
              startPosition = start;
            }
            continue;
          } else if (words[1].equals("END")) {
            if (Database.symTab.containsKey(words[2])) {
              startPosition = Database.symTab.get(words[2]);
            } else {
              startPosition = start;
            }
            continue;
          }
        } else {
          putObjCode();
          System.out.printf("%05X\t", location);
          list.write(String.format("%05X\t", location));
          if (Database.objCode.containsKey(location)) {
            System.out.printf("%6S\t", Database.objCode.get(location));
            list.write(String.format("%6S\t", Database.objCode.get(location)));
          } else {
            System.out.print("      \t");
            list.write("      \t");
          }
          System.out.println(text + "");
          list.write(text + "\n");
        }


        //計算location
        /*
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
        */
        location = pc;
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
    if(m == 1){
      if(mCards[mCardTotal]==null){
        mCards[mCardTotal] = "";
      }

      mCards[mCardTotal] = mCards[mCardTotal] + String.format("%06X", location + 1) + "05";
      mCardTotal += 1;
    }
    if(cards[cardTotal] == null){
      cards[cardTotal] = "";
    }
    if((cards[cardTotal]+String.format("%S", Database.objCode.get(location))).length()<=60){
      if (Database.objCode.containsKey(location)){
        if(cards[cardTotal].equals("")){
          tStart[cardTotal] =location;
        }
        cards[cardTotal] = cards[cardTotal]+String.format("%S", Database.objCode.get(location));
        cardLength[cardTotal] = (cards[cardTotal].length())/2;
      }else if(cardTotal==0){
        tStart[cardTotal] = start;
        cardLength[cardTotal] = (cards[cardTotal].length())/2;
        cardTotal += 1;
        cards[cardTotal] = "";
      }else if(isBase != 1){
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
