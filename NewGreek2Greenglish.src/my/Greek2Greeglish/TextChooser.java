/*   1:    */ package my.Greek2Greeglish;
/*   2:    */ 
/*   3:    */ import java.awt.Container;
/*   4:    */ import java.awt.EventQueue;
/*   5:    */ import java.awt.event.ActionEvent;
/*   6:    */ import java.awt.event.ActionListener;
/*   7:    */ import java.io.BufferedReader;
/*   8:    */ import java.io.File;
/*   9:    */ import java.io.FileInputStream;
/*  10:    */ import java.io.FileOutputStream;
/*  11:    */ import java.io.FileWriter;
/*  12:    */ import java.io.IOException;
/*  13:    */ import java.io.InputStreamReader;
/*  14:    */ import java.io.PrintStream;
/*  15:    */ import javax.swing.JFileChooser;
/*  16:    */ import javax.swing.JFrame;
/*  17:    */ import org.jdesktop.layout.GroupLayout;
/*  18:    */ import org.jdesktop.layout.GroupLayout.ParallelGroup;
/*  19:    */ import org.jdesktop.layout.GroupLayout.SequentialGroup;
/*  20:    */ 
/*  21:    */ public class TextChooser
/*  22:    */   extends JFrame
/*  23:    */ {
/*  24: 23 */   String linesep = "";
/*  25: 24 */   String filename = "";
/*  26: 25 */   String fileseparator = "";
/*  27: 26 */   Refactor ref = new Refactor();
/*  28: 27 */   boolean append = true;
/*  29:    */   private JFileChooser jFileChooser1;
/*  30:    */   
/*  31:    */   public TextChooser()
/*  32:    */   {
/*  33: 30 */     initComponents();
/*  34: 31 */     this.linesep = System.getProperty("line.separator");
/*  35: 32 */     this.linesep = this.linesep;
/*  36: 33 */     String filesep = System.getProperty("file.separator");
/*  37: 34 */     this.fileseparator = filesep;
/*  38:    */   }
/*  39:    */   
/*  40:    */   private void initComponents()
/*  41:    */   {
/*  42: 44 */     this.jFileChooser1 = new JFileChooser();
/*  43:    */     
/*  44: 46 */     setDefaultCloseOperation(3);
/*  45: 47 */     this.jFileChooser1.setApproveButtonText("Refactor text files");
/*  46: 48 */     this.jFileChooser1.setApproveButtonToolTipText("Changes the text data to Greenglish data");
/*  47: 49 */     this.jFileChooser1.setMultiSelectionEnabled(true);
/*  48: 50 */     this.jFileChooser1.addActionListener(new ActionListener()
/*  49:    */     {
/*  50:    */       public void actionPerformed(ActionEvent evt)
/*  51:    */       {
/*  52: 52 */         TextChooser.this.ActionHandler(evt);
/*  53:    */       }
/*  54: 55 */     });
/*  55: 56 */     GroupLayout layout = new GroupLayout(getContentPane());
/*  56: 57 */     getContentPane().setLayout(layout);
/*  57: 58 */     layout.setHorizontalGroup(layout.createParallelGroup(1).add(2, layout.createSequentialGroup().addContainerGap(-1, 32767).add(this.jFileChooser1, -2, -1, -2).addContainerGap()));
/*  58:    */     
/*  59:    */ 
/*  60:    */ 
/*  61:    */ 
/*  62:    */ 
/*  63:    */ 
/*  64: 65 */     layout.setVerticalGroup(layout.createParallelGroup(1).add(2, layout.createSequentialGroup().addContainerGap(-1, 32767).add(this.jFileChooser1, -2, -1, -2)));
/*  65:    */     
/*  66:    */ 
/*  67:    */ 
/*  68:    */ 
/*  69:    */ 
/*  70: 71 */     pack();
/*  71:    */   }
/*  72:    */   
/*  73:    */   private void ActionHandler(ActionEvent evt)
/*  74:    */   {
/*  75: 76 */     if (evt.getActionCommand() == "ApproveSelection")
/*  76:    */     {
/*  77: 78 */       File[] filear = this.jFileChooser1.getSelectedFiles();
/*  78: 79 */       printFileArray(filear);
/*  79: 85 */       for (int k = 0; k < filear.length; k++) {
/*  80: 86 */         openFile(filear[k]);
/*  81:    */       }
/*  82:    */     }
/*  83: 88 */     else if (evt.getActionCommand() == "CancelSelection")
/*  84:    */     {
/*  85: 91 */       System.out.println("Exit");
/*  86:    */       
/*  87:    */ 
/*  88: 94 */       dispose();
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   public static void GeneralMethod()
/*  93:    */   {
/*  94:103 */     EventQueue.invokeLater(new Runnable()
/*  95:    */     {
/*  96:    */       public void run()
/*  97:    */       {
/*  98:105 */         new TextChooser().setVisible(true);
/*  99:    */       }
/* 100:    */     });
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void printFileArray(File[] filearray)
/* 104:    */   {
/* 105:111 */     for (int k = 0; k < filearray.length; k++) {
/* 106:112 */       System.out.println("Selected:" + filearray[k].getName());
/* 107:    */     }
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void openFile(File f)
/* 111:    */   {
/* 112:120 */     System.out.println("trying to open file:" + f.getName() + ".................");
/* 113:    */     try
/* 114:    */     {
/* 115:124 */       FileInputStream fstream = new FileInputStream(f);
/* 116:    */       
/* 117:    */ 
/* 118:    */ 
/* 119:128 */       BufferedReader in = new BufferedReader(new InputStreamReader(fstream));
/* 120:    */       
/* 121:    */ 
/* 122:131 */       System.out.println("File opened succesfully.................");
/* 123:    */       
/* 124:133 */       int i = 10000;
/* 125:134 */       String next_line = "";
/* 126:135 */       String textdata = "";
/* 127:    */       
/* 128:    */ 
/* 129:138 */       File newf = createFile(f);
/* 130:139 */       FileWriter fw = new FileWriter(newf, this.append);
/* 131:    */       for (;;)
/* 132:    */       {
/* 133:142 */         if (!in.ready())
/* 134:    */         {
/* 135:144 */           this.append = false;
/* 136:145 */           System.out.println("Closing fileWriter.................");
/* 137:146 */           fw.close();
/* 138:147 */           break;
/* 139:    */         }
/* 140:149 */         next_line = in.readLine();
/* 141:    */         
/* 142:    */ 
/* 143:152 */         processLine(next_line, fw);
/* 144:    */       }
/* 145:155 */       in.close();
/* 146:    */     }
/* 147:    */     catch (Exception e)
/* 148:    */     {
/* 149:159 */       System.err.println("File input error");
/* 150:    */     }
/* 151:    */   }
/* 152:    */   
/* 153:    */   public File createFile(File f)
/* 154:    */   {
/* 155:165 */     File file = new File(f.getParent() + this.fileseparator + "new" + f.getName());
/* 156:166 */     System.out.println("Created file " + file.getName() + " in path " + file.getAbsolutePath());
/* 157:    */     try
/* 158:    */     {
/* 159:174 */       FileOutputStream out = new FileOutputStream(file.getAbsolutePath(), this.append);
/* 160:    */       
/* 161:    */ 
/* 162:177 */       PrintStream p = new PrintStream(out);
/* 163:    */       
/* 164:    */ 
/* 165:    */ 
/* 166:181 */       p.close();
/* 167:    */     }
/* 168:    */     catch (Exception e)
/* 169:    */     {
/* 170:185 */       System.err.println("Error writing to file");
/* 171:    */     }
/* 172:187 */     return file;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void processLine(String line, FileWriter fw)
/* 176:    */   {
/* 177:193 */     String newlinestr = "";
/* 178:    */     try
/* 179:    */     {
/* 180:195 */       for (int i = 0; i < line.length(); i++)
/* 181:    */       {
/* 182:197 */         char c = this.ref.Map(line.charAt(i));
/* 183:198 */         newlinestr = newlinestr + c;
/* 184:    */       }
/* 185:203 */       fw.write(newlinestr + this.linesep);
/* 186:    */     }
/* 187:    */     catch (IOException ioe)
/* 188:    */     {
/* 189:207 */       System.err.println("IOException: " + ioe.getMessage());
/* 190:    */     }
/* 191:209 */     System.out.println(newlinestr);
/* 192:    */   }
/* 193:    */ }


/* Location:           C:\Users\myros\Desktop\NewGreek2Greenglish.jar
 * Qualified Name:     my.Greek2Greeglish.TextChooser
 * JD-Core Version:    0.7.0.1
 */