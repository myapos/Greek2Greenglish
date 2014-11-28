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
/*  15:    */ public class FileChooser
/*  16:    */   extends JFrame
/*  17:    */ {
/*  18: 25 */   boolean morefiles = true;
/*  19: 28 */   String fileseparator = "";
/*  20: 29 */   String filename = "";
/*  21:    */   private JFileChooser jFileChooser1;
/*  22:    */   
/*  23:    */   public FileChooser()
/*  24:    */   {
/*  25: 33 */     String filesep = System.getProperty("file.separator");
/*  26: 34 */     this.fileseparator = filesep;
/*  27: 35 */     initComponents();
/*  28:    */   }
/*  29:    */   
/*  30:    */   private void initComponents()
/*  31:    */   {
/*  32: 45 */     this.jFileChooser1 = new JFileChooser();
/*  33:    */     
/*  34: 47 */     setDefaultCloseOperation(3);
/*  35: 48 */     this.jFileChooser1.setApproveButtonText("Rename files");
/*  36: 49 */     this.jFileChooser1.setApproveButtonToolTipText("Renames the files that are selected");
/*  37: 50 */     this.jFileChooser1.setCurrentDirectory(this.jFileChooser1.getSelectedFile());
/*  38: 51 */     this.jFileChooser1.setDialogTitle("");
/*  39: 52 */     this.jFileChooser1.setFileSystemView(this.jFileChooser1.getFileSystemView());
/*  40: 53 */     this.jFileChooser1.setMultiSelectionEnabled(true);
/*  41: 54 */     this.jFileChooser1.setName("");
/*  42: 55 */     this.jFileChooser1.setOpaque(true);
/*  43: 56 */     this.jFileChooser1.addActionListener(new ActionListener()
/*  44:    */     {
/*  45:    */       public void actionPerformed(ActionEvent evt)
/*  46:    */       {
/*  47: 58 */         FileChooser.this.ActionHandler(evt);
/*  48:    */       }
/*  49: 61 */     });
/*  50: 62 */     GroupLayout layout = new GroupLayout(getContentPane());
/*  51: 63 */     getContentPane().setLayout(layout);
/*  52: 64 */     layout.setHorizontalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().addContainerGap().add(this.jFileChooser1, -2, -1, -2).addContainerGap(-1, 32767)));
/*  53:    */     
/*  54:    */ 
/*  55:    */ 
/*  56:    */ 
/*  57:    */ 
/*  58:    */ 
/*  59: 71 */     layout.setVerticalGroup(layout.createParallelGroup(1).add(2, layout.createSequentialGroup().addContainerGap(-1, 32767).add(this.jFileChooser1, -2, -1, -2).addContainerGap()));
/*  60:    */     
/*  61:    */ 
/*  62:    */ 
/*  63:    */ 
/*  64:    */ 
/*  65:    */ 
/*  66: 78 */     pack();
/*  67:    */   }
/*  68:    */   
/*  69:    */   private void ActionHandler(ActionEvent evt)
/*  70:    */   {
/*  71: 84 */     if (evt.getActionCommand() == "ApproveSelection")
/*  72:    */     {
/*  73: 86 */       File[] filear = this.jFileChooser1.getSelectedFiles();
/*  74: 87 */       printFileArray(filear);
/*  75: 92 */       for (int j = 0; j < filear.length; j++) {
/*  76: 93 */         renameFile(filear[j]);
/*  77:    */       }
/*  78:    */     }
/*  79: 96 */     else if (evt.getActionCommand() == "CancelSelection")
/*  80:    */     {
/*  81: 99 */       System.out.println("Exit");
/*  82:    */       
/*  83:    */ 
/*  84:102 */       dispose();
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void GeneralMethod()
/*  89:    */   {
/*  90:113 */     EventQueue.invokeLater(new Runnable()
/*  91:    */     {
/*  92:    */       public void run()
/*  93:    */       {
/*  94:116 */         new FileChooser().setVisible(true);
/*  95:    */       }
/*  96:    */     });
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void renameFile(File f)
/* 100:    */   {
/* 101:130 */     this.filename = f.getName();
/* 102:    */     
/* 103:132 */     String path = f.getParent();
/* 104:133 */     Refactor ref = new Refactor(this.filename, f);
/* 105:    */     
/* 106:135 */     File nfile = new File(path + this.fileseparator + ref.filename);
/* 107:136 */     f.renameTo(nfile);
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void printFileArray(File[] filearray)
/* 111:    */   {
/* 112:142 */     for (int k = 0; k < filearray.length; k++) {
/* 113:143 */       System.out.println("Selected:" + filearray[k].getName());
/* 114:    */     }
/* 115:    */   }
/* 116:    */ }


/* Location:           C:\Users\myros\Desktop\NewGreek2Greenglish.jar
 * Qualified Name:     my.Greek2Greeglish.FileChooser
 * JD-Core Version:    0.7.0.1
 */