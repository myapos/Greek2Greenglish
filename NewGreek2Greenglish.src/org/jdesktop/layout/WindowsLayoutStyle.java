/*   1:    */ package org.jdesktop.layout;
/*   2:    */ 
/*   3:    */ import java.awt.Container;
/*   4:    */ import java.awt.FontMetrics;
/*   5:    */ import java.awt.Toolkit;
/*   6:    */ import javax.swing.JComponent;
/*   7:    */ import javax.swing.UIManager;
/*   8:    */ 
/*   9:    */ class WindowsLayoutStyle
/*  10:    */   extends LayoutStyle
/*  11:    */ {
/*  12:    */   private int baseUnitX;
/*  13:    */   private int baseUnitY;
/*  14:    */   
/*  15:    */   public int getPreferredGap(JComponent source, JComponent target, int type, int position, Container parent)
/*  16:    */   {
/*  17: 35 */     super.getPreferredGap(source, target, type, position, target);
/*  18: 37 */     if (type == 3)
/*  19:    */     {
/*  20: 38 */       if ((position == 3) || (position == 7))
/*  21:    */       {
/*  22: 39 */         int gap = getButtonChildIndent(source, position);
/*  23: 40 */         if (gap != 0) {
/*  24: 41 */           return gap;
/*  25:    */         }
/*  26: 43 */         return 10;
/*  27:    */       }
/*  28: 46 */       type = 0;
/*  29:    */     }
/*  30: 48 */     if (type == 1) {
/*  31: 50 */       return getCBRBPadding(source, target, position, dluToPixels(7, position));
/*  32:    */     }
/*  33: 54 */     boolean sourceLabel = source.getUIClassID() == "LabelUI";
/*  34: 55 */     boolean targetLabel = target.getUIClassID() == "LabelUI";
/*  35: 57 */     if (((sourceLabel) && (!targetLabel)) || ((targetLabel) && (!sourceLabel) && ((position == 3) || (position == 7)))) {
/*  36: 70 */       return getCBRBPadding(source, target, position, dluToPixels(3, position));
/*  37:    */     }
/*  38: 74 */     return getCBRBPadding(source, target, position, dluToPixels(4, position));
/*  39:    */   }
/*  40:    */   
/*  41:    */   public int getContainerGap(JComponent component, int position, Container parent)
/*  42:    */   {
/*  43: 81 */     super.getContainerGap(component, position, parent);
/*  44: 82 */     return getCBRBPadding(component, position, dluToPixels(7, position));
/*  45:    */   }
/*  46:    */   
/*  47:    */   private int dluToPixels(int dlu, int direction)
/*  48:    */   {
/*  49: 86 */     if (this.baseUnitX == 0) {
/*  50: 87 */       calculateBaseUnits();
/*  51:    */     }
/*  52: 89 */     if ((direction == 3) || (direction == 7)) {
/*  53: 91 */       return dlu * this.baseUnitX / 4;
/*  54:    */     }
/*  55: 93 */     assert ((direction == 1) || (direction == 5));
/*  56:    */     
/*  57: 95 */     return dlu * this.baseUnitY / 8;
/*  58:    */   }
/*  59:    */   
/*  60:    */   private void calculateBaseUnits()
/*  61:    */   {
/*  62:101 */     FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(UIManager.getFont("Button.font"));
/*  63:    */     
/*  64:103 */     this.baseUnitX = metrics.stringWidth("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
/*  65:    */     
/*  66:105 */     this.baseUnitX = ((this.baseUnitX / 26 + 1) / 2);
/*  67:    */     
/*  68:107 */     this.baseUnitY = (metrics.getAscent() + metrics.getDescent() - 1);
/*  69:    */   }
/*  70:    */ }


/* Location:           C:\Users\myros\Desktop\NewGreek2Greenglish.jar
 * Qualified Name:     org.jdesktop.layout.WindowsLayoutStyle
 * JD-Core Version:    0.7.0.1
 */