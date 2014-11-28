/*   1:    */ package org.jdesktop.layout;
/*   2:    */ 
/*   3:    */ import java.awt.Container;
/*   4:    */ import java.lang.reflect.Field;
/*   5:    */ import java.lang.reflect.InvocationTargetException;
/*   6:    */ import java.lang.reflect.Method;
/*   7:    */ import javax.swing.JComponent;
/*   8:    */ 
/*   9:    */ class SwingLayoutStyle
/*  10:    */   extends LayoutStyle
/*  11:    */ {
/*  12:    */   private static final Method SWING_GET_LAYOUT_STYLE_METHOD;
/*  13:    */   private static final Method SWING_GET_PREFERRED_GAP_METHOD;
/*  14:    */   private static final Method SWING_GET_CONTAINER_GAP_METHOD;
/*  15:    */   private static final Object RELATED_TYPE;
/*  16:    */   private static final Object UNRELATED_TYPE;
/*  17:    */   private static final Object INDENT_TYPE;
/*  18:    */   
/*  19:    */   static
/*  20:    */   {
/*  21: 28 */     Method getLayoutStyle = null;
/*  22: 29 */     Method getPreferredGap = null;
/*  23: 30 */     Method getContainerGap = null;
/*  24: 31 */     Object relatedType = null;
/*  25: 32 */     Object unrelatedType = null;
/*  26: 33 */     Object indentType = null;
/*  27:    */     try
/*  28:    */     {
/*  29: 38 */       Class swingLayoutStyleClass = Class.forName("javax.swing.LayoutStyle");
/*  30: 39 */       Class swingComponentPlacementClass = Class.forName("javax.swing.LayoutStyle$ComponentPlacement");
/*  31:    */       
/*  32: 41 */       swingLayoutStyleClass = Class.forName("javax.swing.LayoutStyle");
/*  33: 42 */       getLayoutStyle = swingLayoutStyleClass.getMethod("getInstance", null);
/*  34:    */       
/*  35: 44 */       getPreferredGap = swingLayoutStyleClass.getMethod("getPreferredGap", new Class[] { JComponent.class, JComponent.class, swingComponentPlacementClass, Integer.TYPE, Container.class });
/*  36:    */       
/*  37:    */ 
/*  38:    */ 
/*  39:    */ 
/*  40: 49 */       getContainerGap = swingLayoutStyleClass.getMethod("getContainerGap", new Class[] { JComponent.class, Integer.TYPE, Container.class });
/*  41:    */       
/*  42:    */ 
/*  43: 52 */       relatedType = swingComponentPlacementClass.getField("RELATED").get(null);
/*  44: 53 */       unrelatedType = swingComponentPlacementClass.getField("UNRELATED").get(null);
/*  45: 54 */       indentType = swingComponentPlacementClass.getField("INDENT").get(null);
/*  46:    */     }
/*  47:    */     catch (ClassNotFoundException cnfe) {}catch (NoSuchMethodException nsme) {}catch (NoSuchFieldException nsfe) {}catch (IllegalAccessException iae) {}
/*  48: 60 */     SWING_GET_LAYOUT_STYLE_METHOD = getLayoutStyle;
/*  49: 61 */     SWING_GET_PREFERRED_GAP_METHOD = getPreferredGap;
/*  50: 62 */     SWING_GET_CONTAINER_GAP_METHOD = getContainerGap;
/*  51: 63 */     RELATED_TYPE = relatedType;
/*  52: 64 */     UNRELATED_TYPE = unrelatedType;
/*  53: 65 */     INDENT_TYPE = indentType;
/*  54:    */   }
/*  55:    */   
/*  56:    */   private static final Object layoutStyleTypeToComponentPlacement(int type)
/*  57:    */   {
/*  58: 69 */     if (type == 0) {
/*  59: 70 */       return RELATED_TYPE;
/*  60:    */     }
/*  61: 71 */     if (type == 1) {
/*  62: 72 */       return UNRELATED_TYPE;
/*  63:    */     }
/*  64: 74 */     assert (type == 3);
/*  65: 75 */     return INDENT_TYPE;
/*  66:    */   }
/*  67:    */   
/*  68:    */   private static final Object getSwingLayoutStyle()
/*  69:    */   {
/*  70:    */     try
/*  71:    */     {
/*  72: 81 */       return SWING_GET_LAYOUT_STYLE_METHOD.invoke(null, null);
/*  73:    */     }
/*  74:    */     catch (IllegalAccessException iae) {}catch (InvocationTargetException ite) {}
/*  75: 85 */     return null;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public int getPreferredGap(JComponent component1, JComponent component2, int type, int position, Container parent)
/*  79:    */   {
/*  80: 90 */     super.getPreferredGap(component1, component2, type, position, parent);
/*  81: 91 */     Object componentPlacement = layoutStyleTypeToComponentPlacement(type);
/*  82: 92 */     Object layoutStyle = getSwingLayoutStyle();
/*  83:    */     try
/*  84:    */     {
/*  85: 94 */       return ((Integer)SWING_GET_PREFERRED_GAP_METHOD.invoke(layoutStyle, new Object[] { component1, component2, componentPlacement, new Integer(position), parent })).intValue();
/*  86:    */     }
/*  87:    */     catch (IllegalAccessException iae) {}catch (InvocationTargetException ite) {}
/*  88:100 */     return 0;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public int getContainerGap(JComponent component, int position, Container parent)
/*  92:    */   {
/*  93:105 */     super.getContainerGap(component, position, parent);
/*  94:106 */     Object layoutStyle = getSwingLayoutStyle();
/*  95:    */     try
/*  96:    */     {
/*  97:108 */       return ((Integer)SWING_GET_CONTAINER_GAP_METHOD.invoke(layoutStyle, new Object[] { component, new Integer(position), parent })).intValue();
/*  98:    */     }
/*  99:    */     catch (IllegalAccessException iae) {}catch (InvocationTargetException ite) {}
/* 100:114 */     return 0;
/* 101:    */   }
/* 102:    */ }


/* Location:           C:\Users\myros\Desktop\NewGreek2Greenglish.jar
 * Qualified Name:     org.jdesktop.layout.SwingLayoutStyle
 * JD-Core Version:    0.7.0.1
 */