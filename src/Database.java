import java.util.HashMap;
import java.util.Map;
public class Database {

  public Map<String, String> order = new HashMap<>();
  public Map<String, String> order2 = new HashMap<>();
  public Map<String, String> order4 = new HashMap<>();
  public Map<String, Integer> register = new HashMap<>();
  public Map<String, Integer> symTab = new HashMap<>();
  public Map<Integer, String> objCode = new HashMap<>();

  public Database() {
    register.put("A", 0);
    register.put("X", 1);
    register.put("L", 2);
    register.put("PC", 8);
    register.put("SW", 9);
    register.put("B", 3);
    register.put("S", 4);
    register.put("T", 5);
    register.put("F", 6);

    order2.put("ADDR", "90");
    order2.put("COMPR", "A0");
    order2.put("DIVR", "9C");
    order2.put("MULR", "98");
    order2.put("RMO", "AC");
    order2.put("SUBR", "94");
    order2.put("TIXR", "B8");

    order.put("ADD", "18");
    order.put("ADDF", "58");
    order.put("AND", "40");
    order.put("CLEAR", "B4");
    order.put("COMP", "28");
    order.put("COMPF", "88");
    order.put("DIV", "24");
    order.put("DIVF", "64");
    order.put("FIX", "C4");
    order.put("FLOAT", "C0");
    order.put("J", "3C");
    order.put("JEQ", "30");
    order.put("JGT", "34");
    order.put("JLT", "38");
    order.put("JSUB", "48");
    order.put("LDA", "00");
    order.put("LDB", "68");
    order.put("LDCH", "50");
    order.put("LDF", "70");
    order.put("LDL", "08");
    order.put("LDS", "6C");
    order.put("LDT", "74");
    order.put("LDX", "04");
    order.put("LPS", "D0");
    order.put("MUL", "20");
    order.put("MULF", "60");
    order.put("NORM", "C8");
    order.put("OR", "44");
    order.put("RD", "D8");
    order.put("RSUB", "4C");
    order.put("SHIFTL", "A4");
    order.put("SHIFIR", "A8");
    order.put("SSK", "EC");
    order.put("STA", "0C");
    order.put("STB", "78");
    order.put("STCH", "54");
    order.put("STF", "80");
    order.put("STI", "D4");
    order.put("STL", "14");
    order.put("STS", "7C");
    order.put("STSW", "E8");
    order.put("STT", "84");
    order.put("STX", "10");
    order.put("SUB", "1C");
    order.put("SUBF", "5C");
    order.put("SVC", "B0");
    order.put("TD", "E0");
    order.put("TIO", "F8");
    order.put("TIX", "2C");
    order.put("WD", "DC");

    order4.put("+ADD", "18");
    order4.put("+ADDF", "58");
    order4.put("+AND", "40");
    order4.put("+CLEAR", "B4");
    order4.put("+COMP", "28");
    order4.put("+COMPF", "88");
    order4.put("+DIV", "24");
    order4.put("+DIVF", "64");
    order4.put("+FIX", "C4");
    order4.put("+FLOAT", "C0");
    order4.put("+J", "3C");
    order4.put("+JEQ", "30");
    order4.put("+JGT", "34");
    order4.put("+JLT", "38");
    order4.put("+JSUB", "48");
    order4.put("+LDA", "00");
    order4.put("+LDB", "68");
    order4.put("+LDCH", "50");
    order4.put("+LDF", "70");
    order4.put("+LDL", "08");
    order4.put("+LDS", "6C");
    order4.put("+LDT", "74");
    order4.put("+LDX", "04");
    order4.put("+LPS", "D0");
    order4.put("+MUL", "20");
    order4.put("+MULF", "60");
    order4.put("+NORM", "C8");
    order4.put("+OR", "44");
    order4.put("+RD", "D8");
    order4.put("+RSUB", "4C");
    order4.put("+SHIFTL", "A4");
    order4.put("+SHIFIR", "A8");
    order4.put("+SSK", "EC");
    order4.put("+STA", "0C");
    order4.put("+STB", "78");
    order4.put("+STCH", "54");
    order4.put("+STF", "80");
    order4.put("+STI", "D4");
    order4.put("+STL", "14");
    order4.put("+STS", "7C");
    order4.put("+STSW", "E8");
    order4.put("+STT", "84");
    order4.put("+STX", "10");
    order4.put("+SUB", "1C");
    order4.put("+SUBF", "5C");
    order4.put("+SVC", "B0");
    order4.put("+TD", "E0");
    order4.put("+TIO", "F8");
    order4.put("+TIX", "2C");
    order4.put("+WD", "DC");
  }




}
