/*  1:   */ package org.jdesktop.layout;
/*  2:   */ 
/*  3:   */ import java.awt.Container;
/*  4:   */ import javax.swing.JComponent;
/*  5:   */ 
/*  6:   */ class GnomeLayoutStyle
/*  7:   */   extends LayoutStyle
/*  8:   */ {
/*  9:   */   public int getPreferredGap(JComponent source, JComponent target, int type, int position, Container parent)
/* 10:   */   {
/* 11:23 */     super.getPreferredGap(source, target, type, position, parent);
/* 12:25 */     if (type == 3)
/* 13:   */     {
/* 14:26 */       if ((position == 3) || (position == 7))
/* 15:   */       {
/* 16:27 */         int gap = getButtonChildIndent(source, position);
/* 17:28 */         if (gap != 0) {
/* 18:29 */           return gap;
/* 19:   */         }
/* 20:33 */         return 12;
/* 21:   */       }
/* 22:36 */       type = 0;
/* 23:   */     }
/* 24:40 */     if ((position == 3) || (position == 7))
/* 25:   */     {
/* 26:42 */       boolean sourceLabel = source.getUIClassID() == "LabelUI";
/* 27:43 */       boolean targetLabel = target.getUIClassID() == "LabelUI";
/* 28:44 */       if (((sourceLabel) && (!targetLabel)) || ((!sourceLabel) && (targetLabel))) {
/* 29:46 */         return 12;
/* 30:   */       }
/* 31:   */     }
/* 32:60 */     if (type == 0) {
/* 33:61 */       return 6;
/* 34:   */     }
/* 35:63 */     return 12;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public int getContainerGap(JComponent component, int position, Container parent)
/* 39:   */   {
/* 40:68 */     super.getContainerGap(component, position, parent);
/* 41:   */     
/* 42:   */ 
/* 43:   */ 
/* 44:   */ 
/* 45:   */ 
/* 46:74 */     return 12;
/* 47:   */   }
/* 48:   */ }


/* Location:           C:\Users\myros\Desktop\NewGreek2Greenglish.jar
 * Qualified Name:     org.jdesktop.layout.GnomeLayoutStyle
 * JD-Core Version:    0.7.0.1
 */