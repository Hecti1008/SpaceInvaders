/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nauespaial;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import javax.swing.*;

public class NauEspaial extends javax.swing.JFrame {   
    
    ThreadGroup dispars = new ThreadGroup("");
    
    public NauEspaial() {
        initComponents();
        }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGap(0, 300, Short.MAX_VALUE));
        pack();
        }
    
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                    }
                }
            }
        catch (Exception ex) {
            java.util.logging.Logger.getLogger(NauEspaial.class.getName()).log(
                java.util.logging.Level.SEVERE, null, ex);
            }       
        NauEspaial f = new NauEspaial();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("Naus Espaials");
        f.setContentPane(new PanelNau());
        f.setSize(500, 550);
        f.setVisible(true);
        }
    }


class PanelNau extends JPanel implements Runnable, KeyListener{
    private int numNaus=10;    
    Nau[] nau;
    Nau nauPropia;
    private static int contador = 0;
    int Y;
    Disparar[] dispars = new Disparar[5];
    Disparar disparar;
    
    public PanelNau(){        
        nau = new Nau[numNaus];
        for (int i=0;i<nau.length;i++) {
            Random rand = new Random();
            int velocitat=(rand.nextInt(3)+5)*10;
            int posX=rand.nextInt(100)+30;
            int posY=rand.nextInt(100)+30;
            int dX=rand.nextInt(3)+1;
            int dY=rand.nextInt(3)+1;
            String nomNau = Integer.toString(i);
            nau[i]= new Nau(nomNau,posX,posY,dX,dY,velocitat);
            }
        
        nauPropia = new Nau("NauNostre", 200, 440, 0, 0, 100);
        
        Thread n = new Thread(this);
        n.start();   
        
        addKeyListener((KeyListener) this);
        setFocusable(true);
        }

    public void run() {
        System.out.println("Inici fil repintar");
        while(true) {
            try { Thread.sleep(1);} catch(Exception e) {} // espero 0,1 segons
            System.out.println("Repintant");
            repaint();            
            }                   
        }
    
    public static void restaContador(){
        contador--;
    }
    
    public static int getContador(){
        return contador;
    }
    
    public static void setContador(int c) {
        contador = c;
    }
    
    public synchronized void novabala(){
        if (contador < 5) {
            if (dispars[contador] == null) {
                dispars[contador] = new Disparar(nauPropia.getX() + 19, nauPropia.getY() - +25, nauPropia.velocitat());
            }
        }
        
        contador++;
    }

    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i=0; i<nau.length;++i) 
            if (nau[i] != null) {
                nau[i].pinta(g);
            }
        nauPropia.pinta2(g);
        
        try{
            matarNau();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        
        //for de la bala que la pinta i desapareix si surt
        for(int i=0; i<dispars.length; i++) {
            if (dispars[i] != null) {
                Y = dispars[i].getY();
                if (Y <= 0) {
                    dispars[i].setSeguir(false);
                    dispars[i]=null;
                }else{
                    dispars[i].pintaDispar(g);
                }
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("Key pressed code=" + e.getKeyCode() + ", char=" + e.getKeyChar());
        if (e.getKeyCode() == 37) {
            nauPropia.esquerra();
        }//System.out.println("a la esquerra");
        if (e.getKeyCode() == 39) {
            nauPropia.dreta();
        }//System.out.println("a la dreta");
        if (e.getKeyCode()==32){
            novabala();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 37) {
            nauPropia.parar();
        }
        if (e.getKeyCode() == 39) {
            nauPropia.parar();
        }
    }
    
    public void matarNau() throws InterruptedException{
        int xNau;
        int yNau;
        int xDisparar;
        int yDisparar;
        double aprop;
        int contadorfinal=0;
        
        for(int i=0; i<nau.length; i++) {
            for (int j=0; j<dispars.length; j++) {
                if(dispars[j] != null && nau[i] != null) {
                    xNau = nau[i].getX();
                    xDisparar = dispars[j].getX();
                    yNau = nau[i].getY();
                    yDisparar = dispars[j].getY();
                    
                    aprop = Math.sqrt(Math.pow((yNau - yDisparar), 2) + Math.pow((xNau - xDisparar), 2));
                    
                    if(aprop < 35) {
                        dispars[j].setSeguir(true);
                        nau[i].setSeguir(false);
                        nau[i] = null;
                        dispars[j] = null;
                        
                        for(int f=0; f<nau.length; f++){
                            if(nau[f] == null){
                                contadorfinal++;
                            }
                            if(contadorfinal == nau.length){
                                Thread.sleep(2000);
                                System.exit(0);
                            }
                        }
                        
                    }
                }
            }
        }
    }
    
}

class Disparar extends Thread {
    ThreadGroup dispars = new ThreadGroup("");
    private int x,y;
    private int v;
    private int i = 0;
    private Image image;
    private boolean seguir = true;
    
        public Disparar(int x, int y, int v){
            this.x=x;
            this.y=y;
            this.v=v;
            
            image = new ImageIcon(Nau.class.getResource("dispars.jpg")).getImage();
            
            Thread t = new Thread(dispars, this);
            t.start();  
        }
        
        public void run() {
        while (seguir) {
            try { Thread.sleep(this.v); } catch (Exception e) {}
            moure();
            } 
        }
        
        public void setSeguir(boolean s){
        this.seguir = s;
        }
        public int getY(){
            return this.y;
        }
        public int getX(){
            return this.x;
        }
        
        public synchronized void pintaDispar(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(this.image, this.x, this.y, null);
        }
        
        private void moure() {
        int dsy = 30;
        y = y - dsy;
        if (y <= 0) {
            if (i < 1) {
                PanelNau.setContador(0);
                i++;
            }
        }
        }
    
    }

class Nau extends Thread {
    private String nomNau;
    private int x,y;
    private int dsx,dsy,v;
    private int tx = 10;
    private int ty = 10;
    private boolean seguir = true;
    
    private Image image;
    private Image image2;

    public Nau(String nomNau, int x, int y, int dsx, int dsy, int v ) {
        this.nomNau = nomNau;
        this.x=x;
        this.y=y;
        this.dsx=dsx;
        this.dsy=dsy;
        this.v=v;
        
        
            image = new ImageIcon(Nau.class.getResource("tie.png")).getImage();

        
            image2 = new ImageIcon(Nau.class.getResource("xwing.png")).getImage();
 
        
        
        Thread t = new Thread(this);
        t.start();
        }
    
    
    
    public int velocitat (){
        return v;
        }
    
    public void setSeguir(boolean s){
        this.seguir = s;
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
    
    public synchronized void moure (){
        x=x + dsx;
        y=y + dsy;
        if ( x>= 440 - tx || x<= tx) dsx = - dsx;
        if ( y >= 400 - ty || y<=ty ) dsy = - dsy;
        }
    
    public synchronized void pinta (Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(this.image, x, y, null);
        }
    public synchronized void pinta2 (Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(this.image2, x, y, null);
        }
    
    
    

    public void run() {
        while (true) {
            System.out.println("Movent nau numero " + this.nomNau);
            try { Thread.sleep(this.v); } catch (Exception e) {}
            moure();
            }
        }
    //Moure nau esquerra y dreta
    public void esquerra(){
        if(!(x<=15 - tx)) {
            this.dsx = -10;
        }
        
    }
    
    public void dreta(){
        if(!(x>=450 - tx)) {
        this.dsx = 10;
        }
    }
    
    public void parar(){
        this.dsx = 0;
    }
    }