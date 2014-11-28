/*   1:    */ package org.jdesktop.layout;
/*   2:    */ 
/*   3:    */ import java.awt.ComponentOrientation;
/*   4:    */ import java.awt.Container;
/*   5:    */ import java.awt.Insets;
/*   6:    */ import javax.swing.AbstractButton;
/*   7:    */ import javax.swing.Icon;
/*   8:    */ import javax.swing.JCheckBox;
/*   9:    */ import javax.swing.JComponent;
/*  10:    */ import javax.swing.JRadioButton;
/*  11:    */ import javax.swing.LookAndFeel;
/*  12:    */ import javax.swing.UIDefaults;
/*  13:    */ import javax.swing.UIManager;
/*  14:    */ import javax.swing.border.Border;
/*  15:    */ import javax.swing.plaf.UIResource;
/*  16:    */ 
/*  17:    */ public class LayoutStyle
/*  18:    */ {
/*  19:    */   private static final boolean USE_CORE_LAYOUT_STYLE;
/*  20:    */   public static final int RELATED = 0;
/*  21:    */   public static final int UNRELATED = 1;
/*  22:    */   public static final int INDENT = 3;
/*  23:    */   private static LayoutStyle layoutStyle;
/*  24:    */   private static LookAndFeel laf;
/*  25:    */   
/*  26:    */   static
/*  27:    */   {
/*  28: 61 */     boolean useCoreLayoutStyle = false;
/*  29:    */     try
/*  30:    */     {
/*  31: 63 */       Class.forName("javax.swing.LayoutStyle");
/*  32: 64 */       useCoreLayoutStyle = true;
/*  33:    */     }
/*  34:    */     catch (ClassNotFoundException cnfe) {}
/*  35: 67 */     USE_CORE_LAYOUT_STYLE = useCoreLayoutStyle;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static void setSharedInstance(LayoutStyle layoutStyle)
/*  39:    */   {
/*  40: 78 */     UIManager.getLookAndFeelDefaults().put("LayoutStyle.instance", layoutStyle);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static LayoutStyle getSharedInstance()
/*  44:    */   {
/*  45: 89 */     Object layoutImpl = UIManager.get("LayoutStyle.instance");
/*  46: 90 */     if ((layoutImpl != null) && ((layoutImpl instanceof LayoutStyle))) {
/*  47: 91 */       return (LayoutStyle)layoutImpl;
/*  48:    */     }
/*  49: 93 */     LookAndFeel currentLAF = UIManager.getLookAndFeel();
/*  50: 94 */     if ((layoutStyle == null) || (currentLAF != laf))
/*  51:    */     {
/*  52: 95 */       laf = currentLAF;
/*  53: 96 */       String lafID = laf.getID();
/*  54: 97 */       if (USE_CORE_LAYOUT_STYLE) {
/*  55: 98 */         layoutStyle = new SwingLayoutStyle();
/*  56: 99 */       } else if ("Metal" == lafID) {
/*  57:100 */         layoutStyle = new MetalLayoutStyle();
/*  58:102 */       } else if ("Windows" == lafID) {
/*  59:103 */         layoutStyle = new WindowsLayoutStyle();
/*  60:105 */       } else if ("GTK" == lafID) {
/*  61:106 */         layoutStyle = new GnomeLayoutStyle();
/*  62:108 */       } else if ("Aqua" == lafID) {
/*  63:109 */         layoutStyle = new AquaLayoutStyle();
/*  64:    */       } else {
/*  65:111 */         layoutStyle = new LayoutStyle();
/*  66:    */       }
/*  67:    */     }
/*  68:114 */     return layoutStyle;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public int getPreferredGap(JComponent component1, JComponent component2, int type, int position, Container parent)
/*  72:    */   {
/*  73:182 */     if ((position != 1) && (position != 5) && (position != 7) && (position != 3)) {
/*  74:186 */       throw new IllegalArgumentException("Invalid position");
/*  75:    */     }
/*  76:188 */     if ((component1 == null) || (component2 == null)) {
/*  77:189 */       throw new IllegalArgumentException("Components must be non-null");
/*  78:    */     }
/*  79:191 */     if (type == 0) {
/*  80:192 */       return 6;
/*  81:    */     }
/*  82:193 */     if (type == 1) {
/*  83:194 */       return 12;
/*  84:    */     }
/*  85:195 */     if (type == 3)
/*  86:    */     {
/*  87:196 */       if ((position == 3) || (position == 7))
/*  88:    */       {
/*  89:197 */         int gap = getButtonChildIndent(component1, position);
/*  90:198 */         if (gap != 0) {
/*  91:199 */           return gap;
/*  92:    */         }
/*  93:201 */         return 6;
/*  94:    */       }
/*  95:203 */       return 6;
/*  96:    */     }
/*  97:205 */     throw new IllegalArgumentException("Invalid type");
/*  98:    */   }
/*  99:    */   
/* 100:    */   public int getContainerGap(JComponent component, int position, Container parent)
/* 101:    */   {
/* 102:232 */     if ((position != 1) && (position != 5) && (position != 7) && (position != 3)) {
/* 103:236 */       throw new IllegalArgumentException("Invalid position");
/* 104:    */     }
/* 105:238 */     if (component == null) {
/* 106:239 */       throw new IllegalArgumentException("Component must be non-null");
/* 107:    */     }
/* 108:241 */     return 12;
/* 109:    */   }
/* 110:    */   
/* 111:    */   boolean isDialog(JComponent component)
/* 112:    */   {
/* 113:249 */     String name = component.getName();
/* 114:250 */     return (name != null) && (name.endsWith(".contentPane"));
/* 115:    */   }
/* 116:    */   
/* 117:    */   int getCBRBPadding(JComponent source, JComponent target, int position, int offset)
/* 118:    */   {
/* 119:267 */     offset -= getCBRBPadding(source, position);
/* 120:268 */     if (offset > 0) {
/* 121:269 */       offset -= getCBRBPadding(target, flipDirection(position));
/* 122:    */     }
/* 123:271 */     if (offset < 0) {
/* 124:272 */       return 0;
/* 125:    */     }
/* 126:274 */     return offset;
/* 127:    */   }
/* 128:    */   
/* 129:    */   int getCBRBPadding(JComponent source, int position, int offset)
/* 130:    */   {
/* 131:289 */     offset -= getCBRBPadding(source, position);
/* 132:290 */     return Math.max(offset, 0);
/* 133:    */   }
/* 134:    */   
/* 135:    */   int flipDirection(int position)
/* 136:    */   {
/* 137:294 */     switch (position)
/* 138:    */     {
/* 139:    */     case 1: 
/* 140:296 */       return 5;
/* 141:    */     case 5: 
/* 142:298 */       return 1;
/* 143:    */     case 3: 
/* 144:300 */       return 7;
/* 145:    */     case 7: 
/* 146:302 */       return 3;
/* 147:    */     }
/* 148:304 */     if (!$assertionsDisabled) {
/* 149:304 */       throw new AssertionError();
/* 150:    */     }
/* 151:305 */     return 0;
/* 152:    */   }
/* 153:    */   
/* 154:    */   private int getCBRBPadding(JComponent c, int position)
/* 155:    */   {
/* 156:309 */     if ((c.getUIClassID() == "CheckBoxUI") || (c.getUIClassID() == "RadioButtonUI"))
/* 157:    */     {
/* 158:311 */       Border border = c.getBorder();
/* 159:312 */       if ((border instanceof UIResource)) {
/* 160:313 */         return getInset(c, position);
/* 161:    */       }
/* 162:    */     }
/* 163:316 */     return 0;
/* 164:    */   }
/* 165:    */   
/* 166:    */   private int getInset(JComponent c, int position)
/* 167:    */   {
/* 168:320 */     Insets insets = c.getInsets();
/* 169:321 */     switch (position)
/* 170:    */     {
/* 171:    */     case 1: 
/* 172:323 */       return insets.top;
/* 173:    */     case 5: 
/* 174:325 */       return insets.bottom;
/* 175:    */     case 3: 
/* 176:327 */       return insets.right;
/* 177:    */     case 7: 
/* 178:329 */       return insets.left;
/* 179:    */     }
/* 180:331 */     if (!$assertionsDisabled) {
/* 181:331 */       throw new AssertionError();
/* 182:    */     }
/* 183:332 */     return 0;
/* 184:    */   }
/* 185:    */   
/* 186:    */   private boolean isLeftAligned(AbstractButton button, int position)
/* 187:    */   {
/* 188:336 */     if (position == 7)
/* 189:    */     {
/* 190:337 */       boolean ltr = button.getComponentOrientation().isLeftToRight();
/* 191:338 */       int hAlign = button.getHorizontalAlignment();
/* 192:339 */       return ((ltr) && ((hAlign == 2) || (hAlign == 10))) || ((!ltr) && (hAlign == 11));
/* 193:    */     }
/* 194:343 */     return false;
/* 195:    */   }
/* 196:    */   
/* 197:    */   private boolean isRightAligned(AbstractButton button, int position)
/* 198:    */   {
/* 199:347 */     if (position == 3)
/* 200:    */     {
/* 201:348 */       boolean ltr = button.getComponentOrientation().isLeftToRight();
/* 202:349 */       int hAlign = button.getHorizontalAlignment();
/* 203:350 */       return ((ltr) && ((hAlign == 4) || (hAlign == 11))) || ((!ltr) && (hAlign == 10));
/* 204:    */     }
/* 205:354 */     return false;
/* 206:    */   }
/* 207:    */   
/* 208:    */   private Icon getIcon(AbstractButton button)
/* 209:    */   {
/* 210:358 */     Icon icon = button.getIcon();
/* 211:359 */     if (icon != null) {
/* 212:360 */       return icon;
/* 213:    */     }
/* 214:362 */     String key = null;
/* 215:363 */     if ((button instanceof JCheckBox)) {
/* 216:364 */       key = "CheckBox.icon";
/* 217:365 */     } else if ((button instanceof JRadioButton)) {
/* 218:366 */       key = "RadioButton.icon";
/* 219:    */     }
/* 220:368 */     if (key != null)
/* 221:    */     {
/* 222:369 */       Object oIcon = UIManager.get(key);
/* 223:370 */       if ((oIcon instanceof Icon)) {
/* 224:371 */         return (Icon)oIcon;
/* 225:    */       }
/* 226:    */     }
/* 227:374 */     return null;
/* 228:    */   }
/* 229:    */   
/* 230:    */   int getButtonChildIndent(JComponent c, int position)
/* 231:    */   {
/* 232:383 */     if (((c instanceof JRadioButton)) || ((c instanceof JCheckBox)))
/* 233:    */     {
/* 234:384 */       AbstractButton button = (AbstractButton)c;
/* 235:385 */       Insets insets = c.getInsets();
/* 236:386 */       Icon icon = getIcon(button);
/* 237:387 */       int gap = button.getIconTextGap();
/* 238:388 */       if (isLeftAligned(button, position)) {
/* 239:389 */         return insets.left + icon.getIconWidth() + gap;
/* 240:    */       }
/* 241:390 */       if (isRightAligned(button, position)) {
/* 242:391 */         return insets.right + icon.getIconWidth() + gap;
/* 243:    */       }
/* 244:    */     }
/* 245:394 */     return 0;
/* 246:    */   }
/* 247:    */ }


/* Location:           C:\Users\myros\Desktop\NewGreek2Greenglish.jar
 * Qualified Name:     org.jdesktop.layout.LayoutStyle
 * JD-Core Version:    0.7.0.1
 */