/*   1:    */ package org.jdesktop.layout;
/*   2:    */ 
/*   3:    */ import java.awt.Container;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import javax.swing.ButtonModel;
/*   7:    */ import javax.swing.DefaultButtonModel;
/*   8:    */ import javax.swing.JComponent;
/*   9:    */ import javax.swing.JToggleButton;
/*  10:    */ import javax.swing.plaf.metal.MetalLookAndFeel;
/*  11:    */ import javax.swing.plaf.metal.MetalTheme;
/*  12:    */ 
/*  13:    */ class MetalLayoutStyle
/*  14:    */   extends LayoutStyle
/*  15:    */ {
/*  16:    */   private boolean isOcean;
/*  17:    */   
/*  18:    */   public MetalLayoutStyle()
/*  19:    */   {
/*  20: 33 */     this.isOcean = false;
/*  21:    */     try
/*  22:    */     {
/*  23: 35 */       Method method = MetalLookAndFeel.class.getMethod("getCurrentTheme", (Class[])null);
/*  24:    */       
/*  25: 37 */       this.isOcean = (((MetalTheme)method.invoke(null, (Object[])null)).getName() == "Ocean");
/*  26:    */     }
/*  27:    */     catch (NoSuchMethodException nsme) {}catch (IllegalAccessException iae) {}catch (IllegalArgumentException iae2) {}catch (InvocationTargetException ite) {}
/*  28:    */   }
/*  29:    */   
/*  30:    */   public int getPreferredGap(JComponent source, JComponent target, int type, int position, Container parent)
/*  31:    */   {
/*  32: 56 */     super.getPreferredGap(source, target, type, position, parent);
/*  33: 58 */     if (type == 3)
/*  34:    */     {
/*  35: 59 */       if ((position == 3) || (position == 7))
/*  36:    */       {
/*  37: 60 */         int gap = getButtonChildIndent(source, position);
/*  38: 61 */         if (gap != 0) {
/*  39: 62 */           return gap;
/*  40:    */         }
/*  41: 64 */         return 12;
/*  42:    */       }
/*  43: 67 */       type = 0;
/*  44:    */     }
/*  45: 70 */     String sourceCID = source.getUIClassID();
/*  46: 71 */     String targetCID = target.getUIClassID();
/*  47:    */     int offset;
/*  48:    */     int offset;
/*  49: 74 */     if (type == 0)
/*  50:    */     {
/*  51: 75 */       if ((sourceCID == "ToggleButtonUI") && (targetCID == "ToggleButtonUI"))
/*  52:    */       {
/*  53: 77 */         ButtonModel sourceModel = ((JToggleButton)source).getModel();
/*  54: 78 */         ButtonModel targetModel = ((JToggleButton)target).getModel();
/*  55: 79 */         if (((sourceModel instanceof DefaultButtonModel)) && ((targetModel instanceof DefaultButtonModel)) && (((DefaultButtonModel)sourceModel).getGroup() == ((DefaultButtonModel)targetModel).getGroup()) && (((DefaultButtonModel)sourceModel).getGroup() != null)) {
/*  56: 91 */           return 2;
/*  57:    */         }
/*  58: 96 */         if (this.isOcean) {
/*  59: 97 */           return 6;
/*  60:    */         }
/*  61: 99 */         return 5;
/*  62:    */       }
/*  63:101 */       offset = 6;
/*  64:    */     }
/*  65:    */     else
/*  66:    */     {
/*  67:104 */       offset = 12;
/*  68:    */     }
/*  69:106 */     if (((position == 3) || (position == 7)) && (((sourceCID == "LabelUI") && (targetCID != "LabelUI")) || ((sourceCID != "LabelUI") && (targetCID == "LabelUI")))) {
/*  70:117 */       return getCBRBPadding(source, target, position, offset + 6);
/*  71:    */     }
/*  72:119 */     return getCBRBPadding(source, target, position, offset);
/*  73:    */   }
/*  74:    */   
/*  75:    */   int getCBRBPadding(JComponent source, JComponent target, int position, int offset)
/*  76:    */   {
/*  77:124 */     offset = super.getCBRBPadding(source, target, position, offset);
/*  78:125 */     if (offset > 0)
/*  79:    */     {
/*  80:126 */       int buttonAdjustment = getButtonAdjustment(source, position);
/*  81:127 */       if (buttonAdjustment == 0) {
/*  82:128 */         buttonAdjustment = getButtonAdjustment(target, flipDirection(position));
/*  83:    */       }
/*  84:131 */       offset -= buttonAdjustment;
/*  85:    */     }
/*  86:133 */     if (offset < 0) {
/*  87:134 */       return 0;
/*  88:    */     }
/*  89:136 */     return offset;
/*  90:    */   }
/*  91:    */   
/*  92:    */   private int getButtonAdjustment(JComponent source, int edge)
/*  93:    */   {
/*  94:140 */     String uid = source.getUIClassID();
/*  95:141 */     if ((uid == "ButtonUI") || (uid == "ToggleButtonUI"))
/*  96:    */     {
/*  97:142 */       if ((!this.isOcean) && ((edge == 3) || (edge == 5))) {
/*  98:144 */         return 1;
/*  99:    */       }
/* 100:    */     }
/* 101:147 */     else if ((edge == 5) && (
/* 102:148 */       (uid == "RadioButtonUI") || ((!this.isOcean) && (uid == "CheckBoxUI")))) {
/* 103:149 */       return 1;
/* 104:    */     }
/* 105:152 */     return 0;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public int getContainerGap(JComponent component, int position, Container parent)
/* 109:    */   {
/* 110:157 */     super.getContainerGap(component, position, parent);
/* 111:    */     
/* 112:    */ 
/* 113:    */ 
/* 114:    */ 
/* 115:    */ 
/* 116:    */ 
/* 117:    */ 
/* 118:    */ 
/* 119:    */ 
/* 120:    */ 
/* 121:    */ 
/* 122:    */ 
/* 123:    */ 
/* 124:    */ 
/* 125:    */ 
/* 126:    */ 
/* 127:    */ 
/* 128:175 */     return getCBRBPadding(component, position, 12 - getButtonAdjustment(component, position));
/* 129:    */   }
/* 130:    */ }


/* Location:           C:\Users\myros\Desktop\NewGreek2Greenglish.jar
 * Qualified Name:     org.jdesktop.layout.MetalLayoutStyle
 * JD-Core Version:    0.7.0.1
 */