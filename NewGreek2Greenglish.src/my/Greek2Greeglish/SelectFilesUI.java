/*   1:    */ package my.Greek2Greeglish;
/*   2:    */ 
/*   3:    */ import java.awt.Container;
/*   4:    */ import java.awt.EventQueue;
/*   5:    */ import java.awt.event.ActionEvent;
/*   6:    */ import java.awt.event.ActionListener;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import javax.swing.JButton;
/*   9:    */ import javax.swing.JFrame;
/*  10:    */ import org.jdesktop.layout.GroupLayout;
/*  11:    */ import org.jdesktop.layout.GroupLayout.ParallelGroup;
/*  12:    */ import org.jdesktop.layout.GroupLayout.SequentialGroup;
/*  13:    */ 
/*  14:    */ public class SelectFilesUI
/*  15:    */   extends JFrame
/*  16:    */ {
/*  17:    */   private JButton jButton1;
/*  18:    */   private JButton jButton2;
/*  19:    */   private JButton jButton3;
/*  20:    */   
/*  21:    */   public SelectFilesUI()
/*  22:    */   {
/*  23: 26 */     initComponents();
/*  24:    */   }
/*  25:    */   
/*  26:    */   private void initComponents()
/*  27:    */   {
/*  28: 36 */     this.jButton1 = new JButton();
/*  29: 37 */     this.jButton2 = new JButton();
/*  30: 38 */     this.jButton3 = new JButton();
/*  31:    */     
/*  32: 40 */     setDefaultCloseOperation(3);
/*  33: 41 */     this.jButton1.setAction(this.jButton1.getAction());
/*  34: 42 */     this.jButton1.setText("select files");
/*  35: 43 */     this.jButton1.addActionListener(new ActionListener()
/*  36:    */     {
/*  37:    */       public void actionPerformed(ActionEvent evt)
/*  38:    */       {
/*  39: 45 */         SelectFilesUI.this.Test1(evt);
/*  40:    */       }
/*  41: 48 */     });
/*  42: 49 */     this.jButton2.setText("select directory");
/*  43: 50 */     this.jButton2.addActionListener(new ActionListener()
/*  44:    */     {
/*  45:    */       public void actionPerformed(ActionEvent evt)
/*  46:    */       {
/*  47: 52 */         SelectFilesUI.this.Test2(evt);
/*  48:    */       }
/*  49: 55 */     });
/*  50: 56 */     this.jButton3.setText("Refactor text file");
/*  51: 57 */     this.jButton3.addActionListener(new ActionListener()
/*  52:    */     {
/*  53:    */       public void actionPerformed(ActionEvent evt)
/*  54:    */       {
/*  55: 59 */         SelectFilesUI.this.Test3(evt);
/*  56:    */       }
/*  57: 62 */     });
/*  58: 63 */     GroupLayout layout = new GroupLayout(getContentPane());
/*  59: 64 */     getContentPane().setLayout(layout);
/*  60: 65 */     layout.setHorizontalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(2, false).add(1, this.jButton3, -1, -1, 32767).add(1, this.jButton1, -1, 108, 32767)).add(58, 58, 58).add(this.jButton2, -2, 114, -2).addContainerGap(-1, 32767)));
/*  61:    */     
/*  62:    */ 
/*  63:    */ 
/*  64:    */ 
/*  65:    */ 
/*  66:    */ 
/*  67:    */ 
/*  68:    */ 
/*  69:    */ 
/*  70:    */ 
/*  71: 76 */     layout.setVerticalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(2, false).add(1, this.jButton2, -1, -1, 32767).add(1, this.jButton1, -1, 44, 32767)).add(40, 40, 40).add(this.jButton3, -2, 45, -2).addContainerGap(-1, 32767)));
/*  72:    */     
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76:    */ 
/*  77:    */ 
/*  78:    */ 
/*  79:    */ 
/*  80:    */ 
/*  81:    */ 
/*  82: 87 */     pack();
/*  83:    */   }
/*  84:    */   
/*  85:    */   private void Test3(ActionEvent evt)
/*  86:    */   {
/*  87: 92 */     TextChooser textChooser = new TextChooser();
/*  88: 93 */     TextChooser.GeneralMethod();
/*  89: 94 */     System.out.println("Hello from button Refactor text files ");
/*  90:    */   }
/*  91:    */   
/*  92:    */   private void Test2(ActionEvent evt)
/*  93:    */   {
/*  94: 99 */     DirectoryChooser directoryChooser = new DirectoryChooser();
/*  95:100 */     DirectoryChooser.GeneralMethod();
/*  96:101 */     System.out.println("Hello from button Select directories ");
/*  97:    */   }
/*  98:    */   
/*  99:    */   private void Test1(ActionEvent evt)
/* 100:    */   {
/* 101:108 */     FileChooser fileChooser = new FileChooser();
/* 102:109 */     fileChooser.GeneralMethod();
/* 103:110 */     System.out.println("Hello from button Select files ");
/* 104:    */   }
/* 105:    */   
/* 106:    */   public static void main(String[] args)
/* 107:    */   {
/* 108:120 */     EventQueue.invokeLater(new Runnable()
/* 109:    */     {
/* 110:    */       public void run()
/* 111:    */       {
/* 112:122 */         new SelectFilesUI().setVisible(true);
/* 113:    */       }
/* 114:    */     });
/* 115:    */   }
/* 116:    */ }


/* Location:           C:\Users\myros\Desktop\NewGreek2Greenglish.jar
 * Qualified Name:     my.Greek2Greeglish.SelectFilesUI
 * JD-Core Version:    0.7.0.1
 */