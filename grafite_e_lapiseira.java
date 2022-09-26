import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;


class Lead { //todo
    private float thickness; //calibre
    private String hardness; //dureza
    private int size; //tamanho em mm

    public Lead(float thickness, String hardness, int size) {
        this.thickness = thickness;
        this.hardness = hardness;
        this.size = size;
    }

    public float getThickness() {
        return thickness;
    }

    public String getHardness() {
        return hardness;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int usagePerSheet() {
        if(hardness.equals("HB"))
            return 1;
        else if(hardness.equals("2B"))
            return 2;
        else if(hardness.equals("4B"))
            return 4;
        else
            return 6;
    }

    public String toString() {
        DecimalFormat form = new DecimalFormat("0.0");
        return form.format(thickness) + ":" + hardness + ":" + size;
    }
}


class Pencil { //todo
    private float thickness;
    private Lead tip = null;

    public Pencil(float thickness) {
        this.thickness = thickness;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float value) {
        thickness = value;
    }

    public boolean hasGrafite() {
        return tip != null;
    }

    public boolean insert(Lead grafite) {
        if(hasGrafite()){
            System.out.println("fail: ja existe grafite");
            return false;
        
            
        } else if(getThickness() != grafite.getThickness()){
            System.out.println("fail: calibre incompatível");
            return false;
        
            
        } else {
            tip = grafite;
            return true;
        }
    }

    public Lead remove() {
        if(hasGrafite()){
            Lead t = tip;
            tip = null;
            return t;
        } else {
            return null;
        }
    }

    public void writePage() {
        if(!hasGrafite()){
            System.out.println("fail: ja existe grafite");
        } else if(tip.getSize() > 10){
            if(tip.getSize() < 10 + tip.usagePerSheet()){
                System.out.println("fail: folha incompleta");
                tip.setSize(10);
            } else {
                tip.setSize(tip.getSize() - tip.usagePerSheet());
            }
        } else {
            System.out.println("warning: grafite com tamanho insuficiente para escrever");
        }
    }
    
    public String toString() {
        String saida = "calibre: " + thickness + ", grafite: ";
        if (tip != null)
            saida += "[" + tip + "]";
        else
            saida += "null";
        return saida;
    }
}



public class Solver {

    static Pencil lap = new Pencil(0.5f);
    public static void main(String[] args) {
        chain.put("init",   () -> lap = new Pencil(Float.parseFloat(ui.get(1))));
        chain.put("insert", () -> lap.insert(new Lead(Float.parseFloat(ui.get(1)), ui.get(2), Integer.parseInt(ui.get(3)))));
        chain.put("remove", () -> lap.remove());
        chain.put("write",  () -> lap.writePage());
        chain.put("show",   () -> System.out.println(lap.toString()));

        execute(chain, ui);
    }

    static Scanner scanner = new Scanner(System.in);
    static HashMap<String, Runnable> chain = new HashMap<>();
    static ArrayList<String> ui = new ArrayList<>();

    static void execute(HashMap<String, Runnable> chain , ArrayList<String> ui) {
        while(true) {
            ui.clear();
            String line = scanner.nextLine();
            Collections.addAll(ui, line.split(" "));
            System.out.println("$" + line);
            if(ui.get(0).equals("end")) {
                break;
            } else if (chain.containsKey(ui.get(0))) {
                chain.get(ui.get(0)).run();
            } else {
                System.out.println("fail: comando invalido");
            }
        }
    }
}
