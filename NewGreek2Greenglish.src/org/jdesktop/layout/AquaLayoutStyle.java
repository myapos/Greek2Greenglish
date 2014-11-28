/*   1:    */ package org.jdesktop.layout;
/*   2:    */ 
/*   3:    */ import java.applet.Applet;
/*   4:    */ import java.awt.Component;
/*   5:    */ import java.awt.Container;
/*   6:    */ import java.awt.Dialog;
/*   7:    */ import java.awt.Font;
/*   8:    */ import java.awt.Frame;
/*   9:    */ import java.awt.Insets;
/*  10:    */ import java.awt.Panel;
/*  11:    */ import java.io.PrintStream;
/*  12:    */ import java.util.HashMap;
/*  13:    */ import java.util.Map;
/*  14:    */ import javax.swing.AbstractButton;
/*  15:    */ import javax.swing.JButton;
/*  16:    */ import javax.swing.JComponent;
/*  17:    */ import javax.swing.JProgressBar;
/*  18:    */ import javax.swing.JRadioButton;
/*  19:    */ import javax.swing.JSlider;
/*  20:    */ import javax.swing.JTabbedPane;
/*  21:    */ import javax.swing.border.EmptyBorder;
/*  22:    */ 
/*  23:    */ class AquaLayoutStyle
/*  24:    */   extends LayoutStyle
/*  25:    */ {
/*  26: 25 */   private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
/*  27:    */   private static final int MINI = 0;
/*  28:    */   private static final int SMALL = 1;
/*  29:    */   private static final int REGULAR = 2;
/*  30: 53 */   private static final Object[][] containerGapDefinitions = { { "TabbedPaneUI", new Insets(6, 10, 10, 10), new Insets(6, 10, 10, 12), new Insets(12, 20, 20, 20) }, { "RootPaneUI", new Insets(8, 10, 10, 10), new Insets(8, 10, 10, 12), new Insets(14, 20, 20, 20) }, { "default", new Insets(8, 10, 10, 10), new Insets(8, 10, 10, 12), new Insets(14, 20, 20, 20) } };
/*  31: 98 */   private static final Object[][] relatedGapDefinitions = { { "ButtonUI", "ButtonUI.push", "ButtonUI.text", "ToggleButtonUI.push", "ToggleButtonUI.text", new Insets(8, 8, 8, 8), new Insets(10, 10, 10, 10), new Insets(12, 12, 12, 12) }, { "ButtonUI.metal", "ToggleButtonUI.metal", new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8), new Insets(12, 12, 12, 12) }, { "ButtonUI.bevel", "ButtonUI.toggle", "ButtonUI.square", "ToggleButtonUI", "ToggleButtonUI.bevel", "ToggleButtonUI.square", "ToggleButtonUI.toggle", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0) }, { "ButtonUI.bevel.largeIcon", "ToggleButtonUI.bevel.largeIcon", new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8) }, { "ButtonUI.icon", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0) }, { "ButtonUI.icon.largeIcon", new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8) }, { "ButtonUI.round", "ToggleButtonUI.round", new Insets(12, 12, 12, 12), new Insets(12, 12, 12, 12), new Insets(12, 12, 12, 12) }, { "ButtonUI.help", new Insets(12, 12, 12, 12), new Insets(12, 12, 12, 12), new Insets(12, 12, 12, 12) }, { "ButtonUI.toggleCenter", "ToggleButtonUI.toggleCenter", new Insets(8, 0, 8, 0), new Insets(10, 0, 10, 0), new Insets(12, 0, 12, 0) }, { "ButtonUI.toggleEast", "ToggleButtonUI.toggleEast", new Insets(8, 0, 8, 8), new Insets(10, 0, 10, 10), new Insets(12, 0, 12, 12) }, { "ButtonUI.toggleWest", "ToggleButtonUI.toggleWest", new Insets(8, 8, 8, 0), new Insets(10, 10, 10, 0), new Insets(12, 12, 12, 0) }, { "ButtonUI.toolBarTab", "ToggleButtonUI.toolBarTab", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0) }, { "ButtonUI.colorWell", "ToggleButtonUI.colorWell", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0) }, { "CheckBoxUI", new Insets(6, 5, 6, 5), new Insets(7, 6, 7, 6), new Insets(7, 6, 7, 6) }, { "ComboBoxUI", new Insets(8, 5, 8, 5), new Insets(10, 6, 10, 6), new Insets(12, 8, 12, 8) }, { "LabelUI", new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8) }, { "ListUI", new Insets(5, 5, 5, 5), new Insets(6, 6, 6, 6), new Insets(6, 6, 6, 6) }, { "PanelUI", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0) }, { "ProgressBarUI", new Insets(8, 8, 8, 8), new Insets(10, 10, 10, 10), new Insets(12, 12, 12, 12) }, { "RadioButtonUI", new Insets(5, 5, 5, 5), new Insets(6, 6, 6, 6), new Insets(6, 6, 6, 6) }, { "ScrollPaneUI", new Insets(6, 8, 6, 8), new Insets(6, 8, 6, 8), new Insets(8, 10, 8, 10) }, { "SeparatorUI", new Insets(8, 8, 8, 8), new Insets(10, 10, 10, 10), new Insets(12, 12, 12, 12) }, { "SliderUI.horizontal", new Insets(8, 8, 8, 8), new Insets(10, 10, 10, 10), new Insets(12, 12, 12, 12) }, { "SliderUI.vertical", new Insets(8, 8, 8, 8), new Insets(10, 10, 10, 10), new Insets(12, 12, 12, 12) }, { "SpinnerUI", new Insets(6, 8, 6, 8), new Insets(6, 8, 6, 8), new Insets(8, 10, 8, 10) }, { "SplitPaneUI", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0) }, { "TabbedPaneUI", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0) }, { "TableUI", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0) }, { "TextAreaUI", "EditorPaneUI", "TextPaneUI", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0) }, { "TextFieldUI", "FormattedTextFieldUI", "PasswordFieldUI", new Insets(6, 8, 6, 8), new Insets(6, 8, 6, 8), new Insets(8, 10, 8, 10) }, { "TreeUI", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0) } };
/*  32:276 */   private static final Object[][] unrelatedGapDefinitions = { { "ButtonUI.help", new Insets(24, 24, 24, 24), new Insets(24, 24, 24, 24), new Insets(24, 24, 24, 24) }, { "default", new Insets(10, 10, 10, 10), new Insets(12, 12, 12, 12), new Insets(14, 14, 14, 14) } };
/*  33:303 */   private static final Object[][] indentGapDefinitions = { { "CheckBoxUI", "RadioButtonUI", new Insets(16, 24, 16, 24), new Insets(20, 24, 20, 24), new Insets(24, 24, 24, 24) }, { "default", new Insets(16, 16, 16, 16), new Insets(20, 20, 20, 20), new Insets(24, 24, 24, 24) } };
/*  34:330 */   private static final Object[][] visualMarginDefinitions = { { "ButtonUI", "ButtonUI.text", "ToggleButtonUI", "ToggleButtonUI.text", new Insets(5, 3, 3, 3) }, { "ButtonUI.icon", "ToggleButtonUI.icon", new Insets(5, 2, 3, 2) }, { "ButtonUI.toolbar", "ToggleButtonUI.toolbar", new Insets(0, 0, 0, 0) }, { "CheckBoxUI", new Insets(4, 4, 3, 3) }, { "ComboBoxUI", new Insets(2, 3, 4, 3) }, { "DesktopPaneUI", new Insets(0, 0, 0, 0) }, { "EditorPaneUI", "TextAreaUI", "TextPaneUI", new Insets(0, 0, 0, 0) }, { "FormattedTextFieldUI", "PasswordFieldUI", "TextFieldUI", new Insets(0, 0, 0, 0) }, { "LabelUI", new Insets(0, 0, 0, 0) }, { "ListUI", new Insets(0, 0, 0, 0) }, { "PanelUI", new Insets(0, 0, 0, 0) }, { "ProgressBarUI", "ProgressBarUI.horizontal", new Insets(0, 2, 4, 2) }, { "ProgressBarUI.vertical", new Insets(2, 0, 2, 4) }, { "RadioButtonUI", new Insets(4, 4, 3, 3) }, { "ScrollBarUI", new Insets(0, 0, 0, 0) }, { "ScrollPaneUI", new Insets(0, 0, 0, 0) }, { "SpinnerUI", new Insets(0, 0, 0, 0) }, { "SeparatorUI", new Insets(0, 0, 0, 0) }, { "SplitPaneUI", new Insets(0, 0, 0, 0) }, { "SliderUI", "SliderUI.horizontal", new Insets(3, 6, 3, 6) }, { "SliderUI.vertical", new Insets(6, 3, 6, 3) }, { "TabbedPaneUI", "TabbedPaneUI.top", new Insets(5, 7, 10, 7) }, { "TabbedPaneUI.bottom", new Insets(4, 7, 5, 7) }, { "TabbedPaneUI.left", new Insets(4, 6, 10, 7) }, { "TabbedPaneUI.right", new Insets(4, 7, 10, 6) }, { "TableUI", new Insets(0, 0, 0, 0) }, { "TreeUI", new Insets(0, 0, 0, 0) }, { "default", new Insets(0, 0, 0, 0) } };
/*  35:379 */   private static final Map RELATED_GAPS = createInsetsMap(relatedGapDefinitions);
/*  36:384 */   private static final Map UNRELATED_GAPS = createInsetsMap(unrelatedGapDefinitions);
/*  37:389 */   private static final Map CONTAINER_GAPS = createInsetsMap(containerGapDefinitions);
/*  38:394 */   private static final Map INDENT_GAPS = createInsetsMap(indentGapDefinitions);
/*  39:399 */   private static final Map VISUAL_MARGINS = createInsetsMap(visualMarginDefinitions);
/*  40:    */   
/*  41:    */   private static Map createInsetsMap(Object[][] definitions)
/*  42:    */   {
/*  43:409 */     Map map = new HashMap();
/*  44:410 */     for (int i = 0; i < definitions.length; i++)
/*  45:    */     {
/*  46:411 */       int keys = 0;
/*  47:412 */       while ((keys < definitions[i].length) && ((definitions[i][keys] instanceof String))) {
/*  48:414 */         keys++;
/*  49:    */       }
/*  50:416 */       Insets[] values = new Insets[definitions[i].length - keys];
/*  51:417 */       for (int j = keys; j < definitions[i].length; j++) {
/*  52:418 */         values[(j - keys)] = ((Insets)definitions[i][j]);
/*  53:    */       }
/*  54:420 */       for (int j = 0; j < keys; j++)
/*  55:    */       {
/*  56:421 */         String key = (String)definitions[i][j];
/*  57:422 */         int subindex = key.indexOf('.');
/*  58:423 */         if (subindex == -1)
/*  59:    */         {
/*  60:424 */           ComponentInsets componentInsets = (ComponentInsets)map.get(key);
/*  61:425 */           if (componentInsets == null)
/*  62:    */           {
/*  63:426 */             componentInsets = new ComponentInsets(values);
/*  64:427 */             map.put(key, new ComponentInsets(values));
/*  65:    */           }
/*  66:    */           else
/*  67:    */           {
/*  68:429 */             assert (componentInsets.getInsets() == null);
/*  69:430 */             componentInsets.setInsets(values);
/*  70:    */           }
/*  71:    */         }
/*  72:    */         else
/*  73:    */         {
/*  74:433 */           String subkey = key.substring(subindex + 1);
/*  75:434 */           String parentKey = key.substring(0, subindex);
/*  76:435 */           ComponentInsets componentInsets = (ComponentInsets)map.get(parentKey);
/*  77:437 */           if (componentInsets == null)
/*  78:    */           {
/*  79:438 */             componentInsets = new ComponentInsets();
/*  80:439 */             map.put(parentKey, componentInsets);
/*  81:    */           }
/*  82:441 */           componentInsets.addSubinsets(subkey, new ComponentInsets(values));
/*  83:    */         }
/*  84:    */       }
/*  85:    */     }
/*  86:446 */     return map;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public static void main(String[] args)
/*  90:    */   {
/*  91:450 */     JButton button = new JButton();
/*  92:451 */     button.putClientProperty("JButton.buttonType", "metal");
/*  93:452 */     JButton button2 = new JButton();
/*  94:453 */     LayoutStyle style = new AquaLayoutStyle();
/*  95:454 */     int gap = style.getPreferredGap(button, button2, 0, 3, null);
/*  96:    */     
/*  97:    */ 
/*  98:457 */     System.err.println("gap= " + gap);
/*  99:458 */     button.putClientProperty("JButton.buttonType", "square");
/* 100:459 */     button2.putClientProperty("JButton.buttonType", "square");
/* 101:460 */     gap = style.getPreferredGap(button, button2, 0, 3, null);
/* 102:    */     
/* 103:    */ 
/* 104:463 */     System.err.println("gap= " + gap);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public int getPreferredGap(JComponent component1, JComponent component2, int type, int position, Container parent)
/* 108:    */   {
/* 109:538 */     super.getPreferredGap(component1, component2, type, position, parent);
/* 110:    */     int result;
/* 111:543 */     if (type == 3)
/* 112:    */     {
/* 113:545 */       if ((position == 3) || (position == 7))
/* 114:    */       {
/* 115:546 */         int gap = getButtonChildIndent(component1, position);
/* 116:547 */         if (gap != 0) {
/* 117:548 */           return gap;
/* 118:    */         }
/* 119:    */       }
/* 120:551 */       int sizeStyle = getSizeStyle(component1);
/* 121:552 */       Insets gap1 = getPreferredGap(component1, type, sizeStyle);
/* 122:    */       int result;
/* 123:    */       int result;
/* 124:    */       int result;
/* 125:    */       int result;
/* 126:553 */       switch (position)
/* 127:    */       {
/* 128:    */       case 1: 
/* 129:555 */         result = gap1.bottom;
/* 130:556 */         break;
/* 131:    */       case 5: 
/* 132:558 */         result = gap1.top;
/* 133:559 */         break;
/* 134:    */       case 3: 
/* 135:561 */         result = gap1.left;
/* 136:562 */         break;
/* 137:    */       case 2: 
/* 138:    */       case 4: 
/* 139:    */       case 6: 
/* 140:    */       case 7: 
/* 141:    */       default: 
/* 142:565 */         result = gap1.right;
/* 143:    */       }
/* 144:568 */       int raw = result;
/* 145:    */       
/* 146:570 */       Insets visualMargin2 = getVisualMargin(component2);
/* 147:571 */       switch (position)
/* 148:    */       {
/* 149:    */       case 1: 
/* 150:573 */         result -= visualMargin2.bottom;
/* 151:574 */         break;
/* 152:    */       case 5: 
/* 153:576 */         result -= visualMargin2.top;
/* 154:577 */         break;
/* 155:    */       case 3: 
/* 156:579 */         result -= visualMargin2.left;
/* 157:580 */         break;
/* 158:    */       case 7: 
/* 159:582 */         result -= visualMargin2.right;
/* 160:    */       }
/* 161:    */     }
/* 162:    */     else
/* 163:    */     {
/* 164:588 */       int sizeStyle = Math.min(getSizeStyle(component1), getSizeStyle(component2));
/* 165:    */       
/* 166:590 */       Insets gap1 = getPreferredGap(component1, type, sizeStyle);
/* 167:591 */       Insets gap2 = getPreferredGap(component2, type, sizeStyle);
/* 168:    */       int result;
/* 169:    */       int result;
/* 170:    */       int result;
/* 171:592 */       switch (position)
/* 172:    */       {
/* 173:    */       case 1: 
/* 174:594 */         result = Math.max(gap1.top, gap2.bottom);
/* 175:595 */         break;
/* 176:    */       case 5: 
/* 177:597 */         result = Math.max(gap1.bottom, gap2.top);
/* 178:598 */         break;
/* 179:    */       case 3: 
/* 180:600 */         result = Math.max(gap1.right, gap2.left);
/* 181:601 */         break;
/* 182:    */       case 2: 
/* 183:    */       case 4: 
/* 184:    */       case 6: 
/* 185:    */       case 7: 
/* 186:    */       default: 
/* 187:604 */         result = Math.max(gap1.left, gap2.right);
/* 188:    */       }
/* 189:609 */       Insets visualMargin1 = getVisualMargin(component1);
/* 190:610 */       Insets visualMargin2 = getVisualMargin(component2);
/* 191:612 */       switch (position)
/* 192:    */       {
/* 193:    */       case 1: 
/* 194:614 */         result -= visualMargin1.top + visualMargin2.bottom;
/* 195:615 */         break;
/* 196:    */       case 5: 
/* 197:617 */         result -= visualMargin1.bottom + visualMargin2.top;
/* 198:618 */         break;
/* 199:    */       case 3: 
/* 200:620 */         result -= visualMargin1.right + visualMargin2.left;
/* 201:621 */         break;
/* 202:    */       case 7: 
/* 203:623 */         result -= visualMargin1.left + visualMargin2.right;
/* 204:    */       }
/* 205:    */     }
/* 206:631 */     return Math.max(0, result);
/* 207:    */   }
/* 208:    */   
/* 209:    */   private Insets getPreferredGap(JComponent component, int type, int sizeStyle)
/* 210:    */   {
/* 211:    */     Map gapMap;
/* 212:    */     Map gapMap;
/* 213:    */     Map gapMap;
/* 214:637 */     switch (type)
/* 215:    */     {
/* 216:    */     case 3: 
/* 217:639 */       gapMap = INDENT_GAPS;
/* 218:640 */       break;
/* 219:    */     case 0: 
/* 220:642 */       gapMap = RELATED_GAPS;
/* 221:643 */       break;
/* 222:    */     case 1: 
/* 223:    */     case 2: 
/* 224:    */     default: 
/* 225:646 */       gapMap = UNRELATED_GAPS;
/* 226:    */     }
/* 227:650 */     String uid = component.getUIClassID();
/* 228:651 */     String style = null;
/* 229:654 */     if ((uid == "ButtonUI") || (uid == "ToggleButtonUI")) {
/* 230:655 */       style = (String)component.getClientProperty("JButton.buttonType");
/* 231:656 */     } else if (uid == "ProgressBarUI") {
/* 232:657 */       style = ((JProgressBar)component).getOrientation() == 0 ? "horizontal" : "vertical";
/* 233:659 */     } else if (uid == "SliderUI") {
/* 234:660 */       style = ((JSlider)component).getOrientation() == 0 ? "horizontal" : "vertical";
/* 235:662 */     } else if (uid == "TabbedPaneUI") {
/* 236:663 */       switch (((JTabbedPane)component).getTabPlacement())
/* 237:    */       {
/* 238:    */       case 1: 
/* 239:665 */         style = "top";
/* 240:666 */         break;
/* 241:    */       case 2: 
/* 242:668 */         style = "left";
/* 243:669 */         break;
/* 244:    */       case 3: 
/* 245:671 */         style = "bottom";
/* 246:672 */         break;
/* 247:    */       case 4: 
/* 248:674 */         style = "right";
/* 249:    */       }
/* 250:    */     }
/* 251:678 */     return getInsets(gapMap, uid, style, sizeStyle);
/* 252:    */   }
/* 253:    */   
/* 254:    */   public int getContainerGap(JComponent component, int position, Container parent)
/* 255:    */   {
/* 256:706 */     int sizeStyle = Math.min(getSizeStyle(component), getSizeStyle(parent));
/* 257:    */     
/* 258:    */ 
/* 259:709 */     Insets gap = getContainerGap(parent, sizeStyle);
/* 260:    */     int result;
/* 261:    */     int result;
/* 262:    */     int result;
/* 263:    */     int result;
/* 264:711 */     switch (position)
/* 265:    */     {
/* 266:    */     case 1: 
/* 267:713 */       result = gap.top;
/* 268:714 */       break;
/* 269:    */     case 5: 
/* 270:716 */       result = gap.bottom;
/* 271:717 */       break;
/* 272:    */     case 3: 
/* 273:719 */       result = gap.right;
/* 274:720 */       break;
/* 275:    */     case 2: 
/* 276:    */     case 4: 
/* 277:    */     case 6: 
/* 278:    */     case 7: 
/* 279:    */     default: 
/* 280:723 */       result = gap.left;
/* 281:    */     }
/* 282:728 */     Insets visualMargin = getVisualMargin(component);
/* 283:729 */     switch (position)
/* 284:    */     {
/* 285:    */     case 1: 
/* 286:731 */       result -= visualMargin.top;
/* 287:732 */       break;
/* 288:    */     case 5: 
/* 289:734 */       result -= visualMargin.bottom;
/* 290:738 */       if ((component instanceof JRadioButton)) {
/* 291:739 */         result--;
/* 292:    */       }
/* 293:    */       break;
/* 294:    */     case 3: 
/* 295:743 */       result -= visualMargin.left;
/* 296:744 */       break;
/* 297:    */     case 7: 
/* 298:746 */       result -= visualMargin.right;
/* 299:    */     }
/* 300:753 */     return Math.max(0, result);
/* 301:    */   }
/* 302:    */   
/* 303:    */   private Insets getContainerGap(Container container, int sizeStyle)
/* 304:    */   {
/* 305:    */     String uid;
/* 306:    */     String uid;
/* 307:759 */     if ((container instanceof JComponent))
/* 308:    */     {
/* 309:760 */       uid = ((JComponent)container).getUIClassID();
/* 310:    */     }
/* 311:    */     else
/* 312:    */     {
/* 313:    */       String uid;
/* 314:761 */       if ((container instanceof Dialog))
/* 315:    */       {
/* 316:762 */         uid = "Dialog";
/* 317:    */       }
/* 318:    */       else
/* 319:    */       {
/* 320:    */         String uid;
/* 321:763 */         if ((container instanceof Frame))
/* 322:    */         {
/* 323:764 */           uid = "Frame";
/* 324:    */         }
/* 325:    */         else
/* 326:    */         {
/* 327:    */           String uid;
/* 328:765 */           if ((container instanceof Applet))
/* 329:    */           {
/* 330:766 */             uid = "Applet";
/* 331:    */           }
/* 332:    */           else
/* 333:    */           {
/* 334:    */             String uid;
/* 335:767 */             if ((container instanceof Panel)) {
/* 336:768 */               uid = "Panel";
/* 337:    */             } else {
/* 338:770 */               uid = "default";
/* 339:    */             }
/* 340:    */           }
/* 341:    */         }
/* 342:    */       }
/* 343:    */     }
/* 344:774 */     return getInsets(CONTAINER_GAPS, uid, null, sizeStyle);
/* 345:    */   }
/* 346:    */   
/* 347:    */   private Insets getInsets(Map gapMap, String uid, String style, int sizeStyle)
/* 348:    */   {
/* 349:779 */     if (uid == null) {
/* 350:780 */       uid = "default";
/* 351:    */     }
/* 352:782 */     ComponentInsets componentInsets = (ComponentInsets)gapMap.get(uid);
/* 353:783 */     if (componentInsets == null)
/* 354:    */     {
/* 355:784 */       componentInsets = (ComponentInsets)gapMap.get("default");
/* 356:785 */       if (componentInsets == null) {
/* 357:786 */         return EMPTY_INSETS;
/* 358:    */       }
/* 359:    */     }
/* 360:788 */     else if (style != null)
/* 361:    */     {
/* 362:789 */       ComponentInsets subInsets = componentInsets.getSubinsets(style);
/* 363:790 */       if (subInsets != null) {
/* 364:791 */         componentInsets = subInsets;
/* 365:    */       }
/* 366:    */     }
/* 367:794 */     return componentInsets.getInsets(sizeStyle);
/* 368:    */   }
/* 369:    */   
/* 370:    */   private Insets getVisualMargin(JComponent component)
/* 371:    */   {
/* 372:798 */     String uid = component.getUIClassID();
/* 373:799 */     String style = null;
/* 374:800 */     if ((uid == "ButtonUI") || (uid == "ToggleButtonUI")) {
/* 375:801 */       style = (String)component.getClientProperty("JButton.buttonType");
/* 376:802 */     } else if (uid == "ProgressBarUI") {
/* 377:803 */       style = ((JProgressBar)component).getOrientation() == 0 ? "horizontal" : "vertical";
/* 378:806 */     } else if (uid == "SliderUI") {
/* 379:807 */       style = ((JSlider)component).getOrientation() == 0 ? "horizontal" : "vertical";
/* 380:810 */     } else if (uid == "TabbedPaneUI") {
/* 381:811 */       switch (((JTabbedPane)component).getTabPlacement())
/* 382:    */       {
/* 383:    */       case 1: 
/* 384:813 */         style = "top";
/* 385:814 */         break;
/* 386:    */       case 2: 
/* 387:816 */         style = "left";
/* 388:817 */         break;
/* 389:    */       case 3: 
/* 390:819 */         style = "bottom";
/* 391:820 */         break;
/* 392:    */       case 4: 
/* 393:822 */         style = "right";
/* 394:    */       }
/* 395:    */     }
/* 396:826 */     Insets gap = getInsets(VISUAL_MARGINS, uid, style, 0);
/* 397:828 */     if ((uid == "RadioButtonUI") || (uid == "CheckBoxUI"))
/* 398:    */     {
/* 399:829 */       switch (((AbstractButton)component).getHorizontalTextPosition())
/* 400:    */       {
/* 401:    */       case 4: 
/* 402:831 */         gap = new Insets(gap.top, gap.right, gap.bottom, gap.left);
/* 403:832 */         break;
/* 404:    */       case 0: 
/* 405:834 */         gap = new Insets(gap.top, gap.right, gap.bottom, gap.right);
/* 406:    */       }
/* 407:841 */       if ((component.getBorder() instanceof EmptyBorder))
/* 408:    */       {
/* 409:842 */         gap.left -= 2;
/* 410:843 */         gap.right -= 2;
/* 411:844 */         gap.top -= 2;
/* 412:845 */         gap.bottom -= 2;
/* 413:    */       }
/* 414:    */     }
/* 415:848 */     return gap;
/* 416:    */   }
/* 417:    */   
/* 418:    */   private int getSizeStyle(Component c)
/* 419:    */   {
/* 420:862 */     if (c == null) {
/* 421:863 */       return 2;
/* 422:    */     }
/* 423:865 */     Font font = c.getFont();
/* 424:866 */     if (font == null) {
/* 425:867 */       return 2;
/* 426:    */     }
/* 427:869 */     int fontSize = font.getSize();
/* 428:870 */     return fontSize > 9 ? 1 : fontSize >= 13 ? 2 : 0;
/* 429:    */   }
/* 430:    */   
/* 431:    */   private static class ComponentInsets
/* 432:    */   {
/* 433:    */     private Map children;
/* 434:    */     private Insets[] insets;
/* 435:    */     
/* 436:    */     public ComponentInsets() {}
/* 437:    */     
/* 438:    */     public ComponentInsets(Insets[] insets)
/* 439:    */     {
/* 440:890 */       this.insets = insets;
/* 441:    */     }
/* 442:    */     
/* 443:    */     public void setInsets(Insets[] insets)
/* 444:    */     {
/* 445:894 */       this.insets = insets;
/* 446:    */     }
/* 447:    */     
/* 448:    */     public Insets[] getInsets()
/* 449:    */     {
/* 450:898 */       return this.insets;
/* 451:    */     }
/* 452:    */     
/* 453:    */     public Insets getInsets(int size)
/* 454:    */     {
/* 455:902 */       if (this.insets == null) {
/* 456:903 */         return AquaLayoutStyle.EMPTY_INSETS;
/* 457:    */       }
/* 458:905 */       return this.insets[size];
/* 459:    */     }
/* 460:    */     
/* 461:    */     void addSubinsets(String subkey, ComponentInsets subinsets)
/* 462:    */     {
/* 463:909 */       if (this.children == null) {
/* 464:910 */         this.children = new HashMap(5);
/* 465:    */       }
/* 466:912 */       this.children.put(subkey, subinsets);
/* 467:    */     }
/* 468:    */     
/* 469:    */     ComponentInsets getSubinsets(String subkey)
/* 470:    */     {
/* 471:916 */       return this.children == null ? null : (ComponentInsets)this.children.get(subkey);
/* 472:    */     }
/* 473:    */   }
/* 474:    */ }


/* Location:           C:\Users\myros\Desktop\NewGreek2Greenglish.jar
 * Qualified Name:     org.jdesktop.layout.AquaLayoutStyle
 * JD-Core Version:    0.7.0.1
 */