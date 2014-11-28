/*   1:    */ package my.Greek2Greeglish;
/*   2:    */ 
/*   3:    */ import java.awt.Container;
/*   4:    */ import java.awt.EventQueue;
/*   5:    */ import java.awt.event.ActionEvent;
/*   6:    */ import java.awt.event.ActionListener;
/*   7:    */ import java.io.File;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import javax.swing.JFileChooser;
/*  10:    */ import javax.swing.JFrame;
/*  11:    */ import org.jdesktop.layout.GroupLayout;
/*  12:    */ import org.jdesktop.layout.GroupLayout.ParallelGroup;
/*  13:    */ import org.jdesktop.layout.GroupLayout.SequentialGroup;
/*  14:    */ 
/*  15:    */ public class DirectoryChooser
/*  16:    */   extends JFrame
/*  17:    */ {
/*  18: 23 */   String filename = "";
/*  19: 24 */   String fileseparator = "";
/*  20:    */   private JFileChooser jFileChooser1;
/*  21:    */   
/*  22:    */   public DirectoryChooser()
/*  23:    */   {
/*  24: 27 */     String filesep = System.getProperty("file.separator");
/*  25: 28 */     this.fileseparator = filesep;
/*  26: 29 */     initComponents();
/*  27:    */   }
/*  28:    */   
/*  29:    */   private void initComponents()
/*  30:    */   {
/*  31: 39 */     this.jFileChooser1 = new JFileChooser();
/*  32:    */     
/*  33: 41 */     setDefaultCloseOperation(3);
/*  34: 42 */     this.jFileChooser1.setApproveButtonText("Rename directories");
/*  35: 43 */     this.jFileChooser1.setApproveButtonToolTipText("Renames the directories that are selected");
/*  36: 44 */     this.jFileChooser1.setFileSelectionMode(1);
/*  37: 45 */     this.jFileChooser1.setMultiSelectionEnabled(true);
/*  38: 46 */     this.jFileChooser1.addActionListener(new ActionListener()
/*  39:    */     {
/*  40:    */       public void actionPerformed(ActionEvent evt)
/*  41:    */       {
/*  42: 48 */         DirectoryChooser.this.ActionHandler(evt);
/*  43:    */       }
/*  44: 51 */     });
/*  45: 52 */     GroupLayout layout = new GroupLayout(getContentPane());
/*  46: 53 */     getContentPane().setLayout(layout);
/*  47: 54 */     layout.setHorizontalGroup(layout.createParallelGroup(1).add(2, layout.createSequentialGroup().addContainerGap(-1, 32767).add(this.jFileChooser1, -2, -1, -2).addContainerGap()));
/*  48:    */     
/*  49:    */ 
/*  50:    */ 
/*  51:    */ 
/*  52:    */ 
/*  53:    */ 
/*  54: 61 */     layout.setVerticalGroup(layout.createParallelGroup(1).add(2, layout.createSequentialGroup().addContainerGap(-1, 32767).add(this.jFileChooser1, -2, -1, -2)));
/*  55:    */     
/*  56:    */ 
/*  57:    */ 
/*  58:    */ 
/*  59:    */ 
/*  60: 67 */     pack();
/*  61:    */   }
/*  62:    */   
/*  63:    */   private void ActionHandler(ActionEvent evt)
/*  64:    */   {
/*  65: 72 */     if (evt.getActionCommand() == "ApproveSelection")
/*  66:    */     {
/*  67: 74 */       File[] dirar = this.jFileChooser1.getSelectedFiles();
/*  68: 75 */       printDirectoryArray(dirar);
/*  69: 80 */       for (int j = 0; j < dirar.length; j++) {
/*  70: 81 */         renamedir(dirar[j]);
/*  71:    */       }
/*  72:    */     }
/*  73: 84 */     else if (evt.getActionCommand() == "CancelSelection")
/*  74:    */     {
/*  75: 87 */       System.out.println("Exit");
/*  76:    */       
/*  77:    */ 
/*  78: 90 */       dispose();
/*  79:    */     }
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static void GeneralMethod()
/*  83:    */   {
/*  84: 99 */     EventQueue.invokeLater(new Runnable()
/*  85:    */     {
/*  86:    */       public void run()
/*  87:    */       {
/*  88:101 */         new DirectoryChooser().setVisible(true);
/*  89:    */       }
/*  90:    */     });
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void printDirectoryArray(File[] dirar)
/*  94:    */   {
/*  95:107 */     for (int k = 0; k < dirar.length; k++) {
/*  96:108 */       System.out.println("Selected:" + dirar[k].getName());
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void renamedir(File d)
/* 101:    */   {
/* 102:112 */     this.filename = d.getName();
/* 103:    */     
/* 104:    */ 
/* 105:115 */     String path = d.getParent();
/* 106:116 */     System.out.println(path);
/* 107:    */     
/* 108:118 */     Refactor ref = new Refactor(this.filename, d);
/* 109:    */     
/* 110:120 */     System.out.println("New name:" + ref.filename);
/* 111:    */     
/* 112:122 */     File dfile = new File(path + this.fileseparator + ref.filename);
/* 113:123 */     d.renameTo(dfile);
/* 114:    */   }
/* 115:    */ }


/* Location:           C:\Users\myros\Desktop\NewGreek2Greenglish.jar
 * Qualified Name:     my.Greek2Greeglish.DirectoryChooser
 * JD-Core Version:    0.7.0.1
 */