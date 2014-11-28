/*   1:    */ package org.jdesktop.layout;
/*   2:    */ 
/*   3:    */ import java.awt.Component;
/*   4:    */ import java.awt.Dimension;
/*   5:    */ import java.awt.Font;
/*   6:    */ import java.awt.FontMetrics;
/*   7:    */ import java.awt.Insets;
/*   8:    */ import java.awt.Rectangle;
/*   9:    */ import java.awt.Toolkit;
/*  10:    */ import java.lang.reflect.Field;
/*  11:    */ import java.lang.reflect.InvocationTargetException;
/*  12:    */ import java.lang.reflect.Method;
/*  13:    */ import java.util.Collections;
/*  14:    */ import java.util.Dictionary;
/*  15:    */ import java.util.Enumeration;
/*  16:    */ import java.util.HashMap;
/*  17:    */ import java.util.Map;
/*  18:    */ import javax.swing.AbstractButton;
/*  19:    */ import javax.swing.ComboBoxEditor;
/*  20:    */ import javax.swing.Icon;
/*  21:    */ import javax.swing.JComboBox;
/*  22:    */ import javax.swing.JComponent;
/*  23:    */ import javax.swing.JLabel;
/*  24:    */ import javax.swing.JList;
/*  25:    */ import javax.swing.JPanel;
/*  26:    */ import javax.swing.JProgressBar;
/*  27:    */ import javax.swing.JScrollPane;
/*  28:    */ import javax.swing.JSlider;
/*  29:    */ import javax.swing.JSpinner;
/*  30:    */ import javax.swing.JSpinner.DefaultEditor;
/*  31:    */ import javax.swing.JTabbedPane;
/*  32:    */ import javax.swing.JTable;
/*  33:    */ import javax.swing.JTextArea;
/*  34:    */ import javax.swing.JTextField;
/*  35:    */ import javax.swing.JTree;
/*  36:    */ import javax.swing.JViewport;
/*  37:    */ import javax.swing.ListCellRenderer;
/*  38:    */ import javax.swing.LookAndFeel;
/*  39:    */ import javax.swing.SwingUtilities;
/*  40:    */ import javax.swing.UIManager;
/*  41:    */ import javax.swing.border.Border;
/*  42:    */ import javax.swing.border.EmptyBorder;
/*  43:    */ import javax.swing.border.TitledBorder;
/*  44:    */ import javax.swing.plaf.SliderUI;
/*  45:    */ import javax.swing.plaf.TextUI;
/*  46:    */ import javax.swing.plaf.metal.MetalLookAndFeel;
/*  47:    */ import javax.swing.text.JTextComponent;
/*  48:    */ import javax.swing.text.View;
/*  49:    */ 
/*  50:    */ public class Baseline
/*  51:    */ {
/*  52: 46 */   private static final Rectangle viewRect = new Rectangle();
/*  53: 47 */   private static final Rectangle textRect = new Rectangle();
/*  54: 48 */   private static final Rectangle iconRect = new Rectangle();
/*  55:    */   private static final int EDGE_SPACING = 2;
/*  56:    */   private static final int TEXT_SPACING = 2;
/*  57: 58 */   private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
/*  58:    */   private static JLabel TABLE_LABEL;
/*  59:    */   private static JLabel LIST_LABEL;
/*  60:    */   private static JLabel TREE_LABEL;
/*  61:    */   private static Class CLASSIC_WINDOWS;
/*  62:    */   private static boolean checkedForClassic;
/*  63: 75 */   private static final Map BASELINE_MAP = Collections.synchronizedMap(new HashMap());
/*  64: 80 */   private static Method COMPONENT_BASELINE_METHOD = null;
/*  65:    */   
/*  66:    */   static
/*  67:    */   {
/*  68:    */     try
/*  69:    */     {
/*  70: 82 */       COMPONENT_BASELINE_METHOD = class$java$awt$Component.getMethod("getBaseline", new Class[] { Integer.TYPE, Integer.TYPE });
/*  71:    */     }
/*  72:    */     catch (NoSuchMethodException nsme) {}
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static int getBaseline(JComponent component)
/*  76:    */   {
/*  77: 98 */     Dimension pref = component.getPreferredSize();
/*  78: 99 */     return getBaseline(component, pref.width, pref.height);
/*  79:    */   }
/*  80:    */   
/*  81:    */   private static Method getBaselineMethod(JComponent component)
/*  82:    */   {
/*  83:103 */     if (COMPONENT_BASELINE_METHOD != null) {
/*  84:104 */       return COMPONENT_BASELINE_METHOD;
/*  85:    */     }
/*  86:106 */     Class klass = component.getClass();
/*  87:107 */     while (klass != null)
/*  88:    */     {
/*  89:108 */       if (BASELINE_MAP.containsKey(klass))
/*  90:    */       {
/*  91:109 */         Method method = (Method)BASELINE_MAP.get(klass);
/*  92:110 */         return method;
/*  93:    */       }
/*  94:112 */       klass = klass.getSuperclass();
/*  95:    */     }
/*  96:114 */     klass = component.getClass();
/*  97:115 */     Method[] methods = klass.getMethods();
/*  98:116 */     for (int i = methods.length - 1; i >= 0; i--)
/*  99:    */     {
/* 100:117 */       Method method = methods[i];
/* 101:118 */       if ("getBaseline".equals(method.getName()))
/* 102:    */       {
/* 103:119 */         Class[] params = method.getParameterTypes();
/* 104:120 */         if ((params.length == 2) && (params[0] == Integer.TYPE) && (params[1] == Integer.TYPE))
/* 105:    */         {
/* 106:122 */           BASELINE_MAP.put(klass, method);
/* 107:123 */           return method;
/* 108:    */         }
/* 109:    */       }
/* 110:    */     }
/* 111:127 */     BASELINE_MAP.put(klass, null);
/* 112:128 */     return null;
/* 113:    */   }
/* 114:    */   
/* 115:    */   private static int invokeBaseline(Method method, JComponent c, int width, int height)
/* 116:    */   {
/* 117:133 */     int baseline = -1;
/* 118:    */     try
/* 119:    */     {
/* 120:135 */       baseline = ((Integer)method.invoke(c, new Object[] { new Integer(width), new Integer(height) })).intValue();
/* 121:    */     }
/* 122:    */     catch (IllegalAccessException iae) {}catch (IllegalArgumentException iae2) {}catch (InvocationTargetException ite2) {}
/* 123:142 */     return baseline;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public static int getBaseline(JComponent component, int width, int height)
/* 127:    */   {
/* 128:156 */     Method baselineMethod = getBaselineMethod(component);
/* 129:157 */     if (baselineMethod != null) {
/* 130:158 */       return invokeBaseline(baselineMethod, component, width, height);
/* 131:    */     }
/* 132:160 */     Object baselineImpl = UIManager.get("Baseline.instance");
/* 133:161 */     if ((baselineImpl != null) && ((baselineImpl instanceof Baseline))) {
/* 134:162 */       return ((Baseline)baselineImpl).getComponentBaseline(component, width, height);
/* 135:    */     }
/* 136:165 */     String lookAndFeelID = UIManager.getLookAndFeel().getID();
/* 137:166 */     if ((lookAndFeelID != "Windows") && (lookAndFeelID != "Metal") && (lookAndFeelID != "GTK") && (lookAndFeelID != "Aqua")) {
/* 138:168 */       return -1;
/* 139:    */     }
/* 140:170 */     String uid = component.getUIClassID();
/* 141:171 */     int baseline = -1;
/* 142:172 */     if ((uid == "ButtonUI") || (uid == "CheckBoxUI") || (uid == "RadioButtonUI") || (uid == "ToggleButtonUI"))
/* 143:    */     {
/* 144:174 */       baseline = getButtonBaseline((AbstractButton)component, height);
/* 145:    */     }
/* 146:    */     else
/* 147:    */     {
/* 148:177 */       if (uid == "ComboBoxUI") {
/* 149:178 */         return getComboBoxBaseline((JComboBox)component, height);
/* 150:    */       }
/* 151:181 */       if (uid == "TextAreaUI") {
/* 152:182 */         return getTextAreaBaseline((JTextArea)component, height);
/* 153:    */       }
/* 154:184 */       if ((uid == "FormattedTextFieldUI") || (uid == "PasswordFieldUI") || (uid == "TextFieldUI")) {
/* 155:187 */         baseline = getSingleLineTextBaseline((JTextComponent)component, height);
/* 156:190 */       } else if (uid == "LabelUI") {
/* 157:191 */         baseline = getLabelBaseline((JLabel)component, height);
/* 158:193 */       } else if (uid == "ListUI") {
/* 159:194 */         baseline = getListBaseline((JList)component, height);
/* 160:196 */       } else if (uid == "PanelUI") {
/* 161:197 */         baseline = getPanelBaseline((JPanel)component, height);
/* 162:199 */       } else if (uid == "ProgressBarUI") {
/* 163:200 */         baseline = getProgressBarBaseline((JProgressBar)component, height);
/* 164:202 */       } else if (uid == "SliderUI") {
/* 165:203 */         baseline = getSliderBaseline((JSlider)component, height);
/* 166:205 */       } else if (uid == "SpinnerUI") {
/* 167:206 */         baseline = getSpinnerBaseline((JSpinner)component, height);
/* 168:208 */       } else if (uid == "ScrollPaneUI") {
/* 169:209 */         baseline = getScrollPaneBaseline((JScrollPane)component, height);
/* 170:211 */       } else if (uid == "TabbedPaneUI") {
/* 171:212 */         baseline = getTabbedPaneBaseline((JTabbedPane)component, height);
/* 172:214 */       } else if (uid == "TableUI") {
/* 173:215 */         baseline = getTableBaseline((JTable)component, height);
/* 174:217 */       } else if (uid == "TreeUI") {
/* 175:218 */         baseline = getTreeBaseline((JTree)component, height);
/* 176:    */       }
/* 177:    */     }
/* 178:220 */     return Math.max(baseline, -1);
/* 179:    */   }
/* 180:    */   
/* 181:    */   private static Insets rotateInsets(Insets topInsets, int targetPlacement)
/* 182:    */   {
/* 183:224 */     switch (targetPlacement)
/* 184:    */     {
/* 185:    */     case 2: 
/* 186:226 */       return new Insets(topInsets.left, topInsets.top, topInsets.right, topInsets.bottom);
/* 187:    */     case 3: 
/* 188:229 */       return new Insets(topInsets.bottom, topInsets.left, topInsets.top, topInsets.right);
/* 189:    */     case 4: 
/* 190:232 */       return new Insets(topInsets.left, topInsets.bottom, topInsets.right, topInsets.top);
/* 191:    */     }
/* 192:235 */     return new Insets(topInsets.top, topInsets.left, topInsets.bottom, topInsets.right);
/* 193:    */   }
/* 194:    */   
/* 195:    */   private static int getMaxTabHeight(JTabbedPane tp)
/* 196:    */   {
/* 197:241 */     int fontHeight = tp.getFontMetrics(tp.getFont()).getHeight();
/* 198:242 */     int height = fontHeight;
/* 199:243 */     boolean tallerIcons = false;
/* 200:244 */     for (int counter = tp.getTabCount() - 1; counter >= 0; counter--)
/* 201:    */     {
/* 202:245 */       Icon icon = tp.getIconAt(counter);
/* 203:246 */       if (icon != null)
/* 204:    */       {
/* 205:247 */         int iconHeight = icon.getIconHeight();
/* 206:248 */         height = Math.max(height, iconHeight);
/* 207:249 */         if (iconHeight > fontHeight) {
/* 208:250 */           tallerIcons = true;
/* 209:    */         }
/* 210:    */       }
/* 211:    */     }
/* 212:254 */     Insets tabInsets = UIManager.getInsets("TabbedPane.tabInsets");
/* 213:255 */     height += 2;
/* 214:256 */     if ((!isMetal()) || (!tallerIcons)) {
/* 215:257 */       height += tabInsets.top + tabInsets.bottom;
/* 216:    */     }
/* 217:259 */     return height;
/* 218:    */   }
/* 219:    */   
/* 220:    */   private static int getTabbedPaneBaseline(JTabbedPane tp, int height)
/* 221:    */   {
/* 222:263 */     if (tp.getTabCount() > 0)
/* 223:    */     {
/* 224:264 */       if (isAqua()) {
/* 225:265 */         return getAquaTabbedPaneBaseline(tp, height);
/* 226:    */       }
/* 227:267 */       Insets insets = tp.getInsets();
/* 228:268 */       Insets contentBorderInsets = UIManager.getInsets("TabbedPane.contentBorderInsets");
/* 229:    */       
/* 230:270 */       Insets tabAreaInsets = rotateInsets(UIManager.getInsets("TabbedPane.tabAreaInsets"), tp.getTabPlacement());
/* 231:    */       
/* 232:    */ 
/* 233:273 */       FontMetrics metrics = tp.getFontMetrics(tp.getFont());
/* 234:274 */       int maxHeight = getMaxTabHeight(tp);
/* 235:275 */       iconRect.setBounds(0, 0, 0, 0);
/* 236:276 */       textRect.setBounds(0, 0, 0, 0);
/* 237:277 */       viewRect.setBounds(0, 0, 32767, maxHeight);
/* 238:278 */       SwingUtilities.layoutCompoundLabel(tp, metrics, "A", null, 0, 0, 0, 11, viewRect, iconRect, textRect, 0);
/* 239:    */       
/* 240:    */ 
/* 241:    */ 
/* 242:    */ 
/* 243:    */ 
/* 244:    */ 
/* 245:    */ 
/* 246:    */ 
/* 247:287 */       int baseline = textRect.y + metrics.getAscent();
/* 248:288 */       switch (tp.getTabPlacement())
/* 249:    */       {
/* 250:    */       case 1: 
/* 251:290 */         baseline += insets.top + tabAreaInsets.top;
/* 252:291 */         if (isWindows()) {
/* 253:292 */           if (tp.getTabCount() > 1) {
/* 254:293 */             baseline++;
/* 255:    */           } else {
/* 256:296 */             baseline--;
/* 257:    */           }
/* 258:    */         }
/* 259:299 */         return baseline;
/* 260:    */       case 3: 
/* 261:301 */         baseline = tp.getHeight() - insets.bottom - tabAreaInsets.bottom - maxHeight + baseline;
/* 262:303 */         if (isWindows()) {
/* 263:304 */           if (tp.getTabCount() > 1) {
/* 264:305 */             baseline--;
/* 265:    */           } else {
/* 266:308 */             baseline++;
/* 267:    */           }
/* 268:    */         }
/* 269:311 */         return baseline;
/* 270:    */       case 2: 
/* 271:    */       case 4: 
/* 272:314 */         if (isAqua()) {
/* 273:317 */           return -1;
/* 274:    */         }
/* 275:319 */         baseline += insets.top + tabAreaInsets.top;
/* 276:320 */         if (isWindows()) {
/* 277:321 */           baseline += maxHeight % 2;
/* 278:    */         }
/* 279:323 */         return baseline;
/* 280:    */       }
/* 281:    */     }
/* 282:326 */     return -1;
/* 283:    */   }
/* 284:    */   
/* 285:    */   private static int getAquaTabbedPaneBaseline(JTabbedPane tp, int height)
/* 286:    */   {
/* 287:330 */     Font font = tp.getFont();
/* 288:331 */     FontMetrics metrics = tp.getFontMetrics(font);
/* 289:332 */     int ascent = metrics.getAscent();
/* 290:334 */     switch (tp.getTabPlacement())
/* 291:    */     {
/* 292:    */     case 1: 
/* 293:336 */       int offset = 5;
/* 294:337 */       if (tp.getFont().getSize() > 12) {
/* 295:338 */         offset = 6;
/* 296:    */       }
/* 297:340 */       int yOffset = 20 - metrics.getHeight();
/* 298:341 */       yOffset /= 2;
/* 299:342 */       return offset + yOffset + ascent - 1;
/* 300:    */     case 3: 
/* 301:    */       int offset;
/* 302:    */       int offset;
/* 303:344 */       if (tp.getFont().getSize() > 12) {
/* 304:345 */         offset = 6;
/* 305:    */       } else {
/* 306:347 */         offset = 4;
/* 307:    */       }
/* 308:349 */       return height - (20 - ((20 - metrics.getHeight()) / 2 + ascent)) - offset;
/* 309:    */     case 2: 
/* 310:    */     case 4: 
/* 311:355 */       return -1;
/* 312:    */     }
/* 313:357 */     return -1;
/* 314:    */   }
/* 315:    */   
/* 316:    */   private static int getSliderBaseline(JSlider slider, int height)
/* 317:    */   {
/* 318:362 */     if ((slider.getPaintLabels()) && (!isGTK()))
/* 319:    */     {
/* 320:363 */       boolean isAqua = isAqua();
/* 321:364 */       FontMetrics metrics = slider.getFontMetrics(slider.getFont());
/* 322:365 */       Insets insets = slider.getInsets();
/* 323:366 */       Insets focusInsets = (Insets)UIManager.get("Slider.focusInsets");
/* 324:367 */       if (slider.getOrientation() == 0)
/* 325:    */       {
/* 326:368 */         int tickLength = 8;
/* 327:369 */         int contentHeight = height - insets.top - insets.bottom - focusInsets.top - focusInsets.bottom;
/* 328:    */         
/* 329:371 */         int thumbHeight = 20;
/* 330:372 */         if (isMetal())
/* 331:    */         {
/* 332:373 */           tickLength = ((Integer)UIManager.get("Slider.majorTickLength")).intValue() + 5;
/* 333:    */           
/* 334:375 */           thumbHeight = UIManager.getIcon("Slider.horizontalThumbIcon").getIconHeight();
/* 335:    */         }
/* 336:378 */         else if ((isWindows()) && (isXP()))
/* 337:    */         {
/* 338:382 */           thumbHeight++;
/* 339:    */         }
/* 340:384 */         int centerSpacing = thumbHeight;
/* 341:385 */         if ((isAqua) || (slider.getPaintTicks())) {
/* 342:387 */           centerSpacing += tickLength;
/* 343:    */         }
/* 344:390 */         centerSpacing += metrics.getAscent() + metrics.getDescent();
/* 345:391 */         int trackY = insets.top + focusInsets.top + (contentHeight - centerSpacing - 1) / 2;
/* 346:393 */         if (isAqua) {
/* 347:394 */           if (slider.getPaintTicks())
/* 348:    */           {
/* 349:395 */             int prefHeight = slider.getUI().getPreferredSize(slider).height;
/* 350:    */             
/* 351:397 */             int prefDelta = height - prefHeight;
/* 352:398 */             if (prefDelta > 0) {
/* 353:399 */               trackY -= Math.min(1, prefDelta);
/* 354:    */             }
/* 355:    */           }
/* 356:    */           else
/* 357:    */           {
/* 358:402 */             trackY--;
/* 359:    */           }
/* 360:    */         }
/* 361:406 */         int trackHeight = thumbHeight;
/* 362:407 */         int tickY = trackY + trackHeight;
/* 363:408 */         int tickHeight = tickLength;
/* 364:409 */         if ((!isAqua) && (!slider.getPaintTicks())) {
/* 365:410 */           tickHeight = 0;
/* 366:    */         }
/* 367:412 */         int labelY = tickY + tickHeight;
/* 368:413 */         return labelY + metrics.getAscent();
/* 369:    */       }
/* 370:416 */       boolean inverted = slider.getInverted();
/* 371:417 */       Integer value = inverted ? getMinSliderValue(slider) : getMaxSliderValue(slider);
/* 372:419 */       if (value != null)
/* 373:    */       {
/* 374:420 */         int thumbHeight = 11;
/* 375:421 */         if (isMetal()) {
/* 376:422 */           thumbHeight = UIManager.getIcon("Slider.verticalThumbIcon").getIconHeight();
/* 377:    */         }
/* 378:425 */         int trackBuffer = Math.max(metrics.getHeight() / 2, thumbHeight / 2);
/* 379:    */         
/* 380:427 */         int contentY = focusInsets.top + insets.top;
/* 381:428 */         int trackY = contentY + trackBuffer;
/* 382:429 */         int trackHeight = height - focusInsets.top - focusInsets.bottom - insets.top - insets.bottom - trackBuffer - trackBuffer;
/* 383:    */         
/* 384:    */ 
/* 385:432 */         int maxValue = getMaxSliderValue(slider).intValue();
/* 386:433 */         int min = slider.getMinimum();
/* 387:434 */         int max = slider.getMaximum();
/* 388:435 */         double valueRange = max - min;
/* 389:436 */         double pixelsPerValue = trackHeight / valueRange;
/* 390:    */         
/* 391:438 */         int trackBottom = trackY + (trackHeight - 1);
/* 392:439 */         if (isAqua)
/* 393:    */         {
/* 394:440 */           trackY -= 3;
/* 395:441 */           trackBottom += 6;
/* 396:    */         }
/* 397:443 */         int yPosition = trackY;
/* 398:    */         double offset;
/* 399:    */         double offset;
/* 400:446 */         if (!inverted) {
/* 401:447 */           offset = pixelsPerValue * (max - value.intValue());
/* 402:    */         } else {
/* 403:451 */           offset = pixelsPerValue * (value.intValue() - min);
/* 404:    */         }
/* 405:454 */         if (isAqua) {
/* 406:455 */           yPosition = (int)(yPosition + Math.floor(offset));
/* 407:    */         } else {
/* 408:457 */           yPosition = (int)(yPosition + Math.round(offset));
/* 409:    */         }
/* 410:459 */         yPosition = Math.max(trackY, yPosition);
/* 411:460 */         yPosition = Math.min(trackBottom, yPosition);
/* 412:461 */         if (isAqua) {
/* 413:462 */           return yPosition + metrics.getAscent();
/* 414:    */         }
/* 415:464 */         return yPosition - metrics.getHeight() / 2 + metrics.getAscent();
/* 416:    */       }
/* 417:    */     }
/* 418:469 */     return -1;
/* 419:    */   }
/* 420:    */   
/* 421:    */   private static Integer getMaxSliderValue(JSlider slider)
/* 422:    */   {
/* 423:473 */     Dictionary dictionary = slider.getLabelTable();
/* 424:474 */     if (dictionary != null)
/* 425:    */     {
/* 426:475 */       Enumeration keys = dictionary.keys();
/* 427:476 */       int max = slider.getMinimum() - 1;
/* 428:477 */       while (keys.hasMoreElements()) {
/* 429:478 */         max = Math.max(max, ((Integer)keys.nextElement()).intValue());
/* 430:    */       }
/* 431:480 */       if (max == slider.getMinimum() - 1) {
/* 432:481 */         return null;
/* 433:    */       }
/* 434:483 */       return new Integer(max);
/* 435:    */     }
/* 436:485 */     return null;
/* 437:    */   }
/* 438:    */   
/* 439:    */   private static Integer getMinSliderValue(JSlider slider)
/* 440:    */   {
/* 441:489 */     Dictionary dictionary = slider.getLabelTable();
/* 442:490 */     if (dictionary != null)
/* 443:    */     {
/* 444:491 */       Enumeration keys = dictionary.keys();
/* 445:492 */       int min = slider.getMaximum() + 1;
/* 446:493 */       while (keys.hasMoreElements()) {
/* 447:494 */         min = Math.min(min, ((Integer)keys.nextElement()).intValue());
/* 448:    */       }
/* 449:496 */       if (min == slider.getMaximum() + 1) {
/* 450:497 */         return null;
/* 451:    */       }
/* 452:499 */       return new Integer(min);
/* 453:    */     }
/* 454:501 */     return null;
/* 455:    */   }
/* 456:    */   
/* 457:    */   private static int getProgressBarBaseline(JProgressBar pb, int height)
/* 458:    */   {
/* 459:505 */     if ((pb.isStringPainted()) && (pb.getOrientation() == 0))
/* 460:    */     {
/* 461:507 */       FontMetrics metrics = pb.getFontMetrics(pb.getFont());
/* 462:508 */       Insets insets = pb.getInsets();
/* 463:509 */       int y = insets.top;
/* 464:510 */       if ((isWindows()) && (isXP()))
/* 465:    */       {
/* 466:511 */         if (pb.isIndeterminate())
/* 467:    */         {
/* 468:512 */           y = -1;
/* 469:513 */           height--;
/* 470:    */         }
/* 471:    */         else
/* 472:    */         {
/* 473:516 */           y = 0;
/* 474:517 */           height -= 3;
/* 475:    */         }
/* 476:    */       }
/* 477:    */       else
/* 478:    */       {
/* 479:520 */         if (isGTK()) {
/* 480:521 */           return (height - metrics.getAscent() - metrics.getDescent()) / 2 + metrics.getAscent();
/* 481:    */         }
/* 482:524 */         if (isAqua())
/* 483:    */         {
/* 484:525 */           if (pb.isIndeterminate()) {
/* 485:528 */             return -1;
/* 486:    */           }
/* 487:530 */           y--;
/* 488:531 */           height -= insets.top + insets.bottom;
/* 489:    */         }
/* 490:    */         else
/* 491:    */         {
/* 492:534 */           height -= insets.top + insets.bottom;
/* 493:    */         }
/* 494:    */       }
/* 495:536 */       return y + (height + metrics.getAscent() - metrics.getLeading() - metrics.getDescent()) / 2;
/* 496:    */     }
/* 497:540 */     return -1;
/* 498:    */   }
/* 499:    */   
/* 500:    */   private static int getTreeBaseline(JTree tree, int height)
/* 501:    */   {
/* 502:544 */     int rowHeight = tree.getRowHeight();
/* 503:545 */     if (TREE_LABEL == null)
/* 504:    */     {
/* 505:546 */       TREE_LABEL = new JLabel("X");
/* 506:547 */       TREE_LABEL.setIcon(UIManager.getIcon("Tree.closedIcon"));
/* 507:    */     }
/* 508:549 */     JLabel label = TREE_LABEL;
/* 509:550 */     label.setFont(tree.getFont());
/* 510:551 */     if (rowHeight <= 0) {
/* 511:552 */       rowHeight = label.getPreferredSize().height;
/* 512:    */     }
/* 513:554 */     return getLabelBaseline(label, rowHeight) + tree.getInsets().top;
/* 514:    */   }
/* 515:    */   
/* 516:    */   private static int getTableBaseline(JTable table, int height)
/* 517:    */   {
/* 518:558 */     if (TABLE_LABEL == null)
/* 519:    */     {
/* 520:559 */       TABLE_LABEL = new JLabel("");
/* 521:560 */       TABLE_LABEL.setBorder(new EmptyBorder(1, 1, 1, 1));
/* 522:    */     }
/* 523:562 */     JLabel label = TABLE_LABEL;
/* 524:563 */     label.setFont(table.getFont());
/* 525:564 */     int rowMargin = table.getRowMargin();
/* 526:565 */     int baseline = getLabelBaseline(label, table.getRowHeight() - rowMargin);
/* 527:    */     
/* 528:567 */     return baseline += rowMargin / 2;
/* 529:    */   }
/* 530:    */   
/* 531:    */   private static int getTextAreaBaseline(JTextArea text, int height)
/* 532:    */   {
/* 533:571 */     Insets insets = text.getInsets();
/* 534:572 */     FontMetrics fm = text.getFontMetrics(text.getFont());
/* 535:573 */     return insets.top + fm.getAscent();
/* 536:    */   }
/* 537:    */   
/* 538:    */   private static int getListBaseline(JList list, int height)
/* 539:    */   {
/* 540:577 */     int rowHeight = list.getFixedCellHeight();
/* 541:578 */     if (LIST_LABEL == null)
/* 542:    */     {
/* 543:579 */       LIST_LABEL = new JLabel("X");
/* 544:580 */       LIST_LABEL.setBorder(new EmptyBorder(1, 1, 1, 1));
/* 545:    */     }
/* 546:582 */     JLabel label = LIST_LABEL;
/* 547:583 */     label.setFont(list.getFont());
/* 548:591 */     if (rowHeight == -1) {
/* 549:592 */       rowHeight = label.getPreferredSize().height;
/* 550:    */     }
/* 551:594 */     return getLabelBaseline(label, rowHeight) + list.getInsets().top;
/* 552:    */   }
/* 553:    */   
/* 554:    */   private static int getScrollPaneBaseline(JScrollPane sp, int height)
/* 555:    */   {
/* 556:598 */     Component view = sp.getViewport().getView();
/* 557:599 */     if ((view instanceof JComponent))
/* 558:    */     {
/* 559:600 */       int baseline = getBaseline((JComponent)view);
/* 560:601 */       if (baseline > 0) {
/* 561:602 */         return baseline + sp.getViewport().getY();
/* 562:    */       }
/* 563:    */     }
/* 564:605 */     return -1;
/* 565:    */   }
/* 566:    */   
/* 567:    */   private static int getPanelBaseline(JPanel panel, int height)
/* 568:    */   {
/* 569:609 */     Border border = panel.getBorder();
/* 570:610 */     if ((border instanceof TitledBorder))
/* 571:    */     {
/* 572:611 */       TitledBorder titledBorder = (TitledBorder)border;
/* 573:612 */       if ((titledBorder.getTitle() != null) && (!"".equals(titledBorder.getTitle())))
/* 574:    */       {
/* 575:614 */         Font font = titledBorder.getTitleFont();
/* 576:615 */         if (font == null)
/* 577:    */         {
/* 578:616 */           font = panel.getFont();
/* 579:617 */           if (font == null) {
/* 580:618 */             font = new Font("Dialog", 0, 12);
/* 581:    */           }
/* 582:    */         }
/* 583:621 */         Border border2 = titledBorder.getBorder();
/* 584:    */         Insets borderInsets;
/* 585:    */         Insets borderInsets;
/* 586:623 */         if (border2 != null) {
/* 587:624 */           borderInsets = border2.getBorderInsets(panel);
/* 588:    */         } else {
/* 589:627 */           borderInsets = EMPTY_INSETS;
/* 590:    */         }
/* 591:629 */         FontMetrics fm = panel.getFontMetrics(font);
/* 592:630 */         int fontHeight = fm.getHeight();
/* 593:631 */         int descent = fm.getDescent();
/* 594:632 */         int ascent = fm.getAscent();
/* 595:633 */         int y = 2;
/* 596:634 */         int h = height - 4;
/* 597:636 */         switch (((TitledBorder)border).getTitlePosition())
/* 598:    */         {
/* 599:    */         case 1: 
/* 600:638 */           int diff = ascent + descent + (Math.max(2, 4) - 2);
/* 601:    */           
/* 602:640 */           return y + diff - (descent + 2);
/* 603:    */         case 0: 
/* 604:    */         case 2: 
/* 605:643 */           int diff = Math.max(0, ascent / 2 + 2 - 2);
/* 606:    */           
/* 607:645 */           return y + diff - descent + (borderInsets.top + ascent + descent) / 2;
/* 608:    */         case 3: 
/* 609:648 */           return y + borderInsets.top + ascent + 2;
/* 610:    */         case 4: 
/* 611:650 */           return y + h - (borderInsets.bottom + descent + 2);
/* 612:    */         case 5: 
/* 613:653 */           h -= fontHeight / 2;
/* 614:654 */           return y + h - descent + (ascent + descent - borderInsets.bottom) / 2;
/* 615:    */         case 6: 
/* 616:657 */           h -= fontHeight;
/* 617:658 */           return y + h + ascent + 2;
/* 618:    */         }
/* 619:    */       }
/* 620:    */     }
/* 621:662 */     return -1;
/* 622:    */   }
/* 623:    */   
/* 624:    */   private static int getSpinnerBaseline(JSpinner spinner, int height)
/* 625:    */   {
/* 626:666 */     JComponent editor = spinner.getEditor();
/* 627:667 */     if ((editor instanceof JSpinner.DefaultEditor))
/* 628:    */     {
/* 629:668 */       JSpinner.DefaultEditor defaultEditor = (JSpinner.DefaultEditor)editor;
/* 630:    */       
/* 631:670 */       JTextField tf = defaultEditor.getTextField();
/* 632:671 */       Insets spinnerInsets = spinner.getInsets();
/* 633:672 */       Insets editorInsets = defaultEditor.getInsets();
/* 634:673 */       int offset = spinnerInsets.top + editorInsets.top;
/* 635:674 */       height -= offset + spinnerInsets.bottom + editorInsets.bottom;
/* 636:675 */       if (height <= 0) {
/* 637:676 */         return -1;
/* 638:    */       }
/* 639:678 */       return offset + getSingleLineTextBaseline(tf, height);
/* 640:    */     }
/* 641:680 */     Insets insets = spinner.getInsets();
/* 642:681 */     FontMetrics fm = spinner.getFontMetrics(spinner.getFont());
/* 643:682 */     return insets.top + fm.getAscent();
/* 644:    */   }
/* 645:    */   
/* 646:    */   private static int getLabelBaseline(JLabel label, int height)
/* 647:    */   {
/* 648:686 */     Icon icon = label.isEnabled() ? label.getIcon() : label.getDisabledIcon();
/* 649:    */     
/* 650:688 */     FontMetrics fm = label.getFontMetrics(label.getFont());
/* 651:    */     
/* 652:690 */     resetRects(label, height);
/* 653:    */     
/* 654:692 */     SwingUtilities.layoutCompoundLabel(label, fm, "a", icon, label.getVerticalAlignment(), label.getHorizontalAlignment(), label.getVerticalTextPosition(), label.getHorizontalTextPosition(), viewRect, iconRect, textRect, label.getIconTextGap());
/* 655:    */     
/* 656:    */ 
/* 657:    */ 
/* 658:    */ 
/* 659:    */ 
/* 660:698 */     return textRect.y + fm.getAscent();
/* 661:    */   }
/* 662:    */   
/* 663:    */   private static int getComboBoxBaseline(JComboBox combobox, int height)
/* 664:    */   {
/* 665:702 */     Insets insets = combobox.getInsets();
/* 666:703 */     int y = insets.top;
/* 667:704 */     height -= insets.top + insets.bottom;
/* 668:705 */     if (combobox.isEditable())
/* 669:    */     {
/* 670:706 */       ComboBoxEditor editor = combobox.getEditor();
/* 671:707 */       if ((editor != null) && ((editor.getEditorComponent() instanceof JTextField)))
/* 672:    */       {
/* 673:709 */         JTextField tf = (JTextField)editor.getEditorComponent();
/* 674:710 */         return y + getSingleLineTextBaseline(tf, height);
/* 675:    */       }
/* 676:    */     }
/* 677:714 */     if (isMetal())
/* 678:    */     {
/* 679:715 */       if (isOceanTheme())
/* 680:    */       {
/* 681:716 */         y += 2;
/* 682:717 */         height -= 4;
/* 683:    */       }
/* 684:    */     }
/* 685:720 */     else if (isWindows())
/* 686:    */     {
/* 687:723 */       String osVersion = System.getProperty("os.version");
/* 688:724 */       if (osVersion != null)
/* 689:    */       {
/* 690:725 */         Float version = Float.valueOf(osVersion);
/* 691:726 */         if (version.floatValue() > 4.0D)
/* 692:    */         {
/* 693:727 */           y += 2;
/* 694:728 */           height -= 4;
/* 695:    */         }
/* 696:    */       }
/* 697:    */     }
/* 698:732 */     ListCellRenderer renderer = combobox.getRenderer();
/* 699:733 */     if ((renderer instanceof JLabel))
/* 700:    */     {
/* 701:734 */       int baseline = y + getLabelBaseline((JLabel)renderer, height);
/* 702:735 */       if (isAqua()) {
/* 703:736 */         return baseline - 1;
/* 704:    */       }
/* 705:738 */       return baseline;
/* 706:    */     }
/* 707:741 */     FontMetrics fm = combobox.getFontMetrics(combobox.getFont());
/* 708:742 */     return y + fm.getAscent();
/* 709:    */   }
/* 710:    */   
/* 711:    */   private static int getSingleLineTextBaseline(JTextComponent textComponent, int h)
/* 712:    */   {
/* 713:751 */     View rootView = textComponent.getUI().getRootView(textComponent);
/* 714:752 */     if (rootView.getViewCount() > 0)
/* 715:    */     {
/* 716:753 */       Insets insets = textComponent.getInsets();
/* 717:754 */       int height = h - insets.top - insets.bottom;
/* 718:755 */       int y = insets.top;
/* 719:756 */       View fieldView = rootView.getView(0);
/* 720:757 */       int vspan = (int)fieldView.getPreferredSpan(1);
/* 721:758 */       if (height != vspan)
/* 722:    */       {
/* 723:759 */         int slop = height - vspan;
/* 724:760 */         y += slop / 2;
/* 725:    */       }
/* 726:762 */       FontMetrics fm = textComponent.getFontMetrics(textComponent.getFont());
/* 727:    */       
/* 728:764 */       y += fm.getAscent();
/* 729:765 */       return y;
/* 730:    */     }
/* 731:767 */     return -1;
/* 732:    */   }
/* 733:    */   
/* 734:    */   private static int getButtonBaseline(AbstractButton button, int height)
/* 735:    */   {
/* 736:774 */     FontMetrics fm = button.getFontMetrics(button.getFont());
/* 737:    */     
/* 738:776 */     resetRects(button, height);
/* 739:    */     
/* 740:778 */     String text = button.getText();
/* 741:779 */     if ((text != null) && (text.startsWith("<html>"))) {
/* 742:780 */       return -1;
/* 743:    */     }
/* 744:785 */     SwingUtilities.layoutCompoundLabel(button, fm, "a", button.getIcon(), button.getVerticalAlignment(), button.getHorizontalAlignment(), button.getVerticalTextPosition(), button.getHorizontalTextPosition(), viewRect, iconRect, textRect, text == null ? 0 : button.getIconTextGap());
/* 745:793 */     if (isAqua()) {
/* 746:794 */       return textRect.y + fm.getAscent() + 1;
/* 747:    */     }
/* 748:796 */     return textRect.y + fm.getAscent();
/* 749:    */   }
/* 750:    */   
/* 751:    */   private static void resetRects(JComponent c, int height)
/* 752:    */   {
/* 753:800 */     Insets insets = c.getInsets();
/* 754:801 */     viewRect.x = insets.left;
/* 755:802 */     viewRect.y = insets.top;
/* 756:803 */     viewRect.width = (c.getWidth() - (insets.right + viewRect.x));
/* 757:804 */     viewRect.height = (height - (insets.bottom + viewRect.y));
/* 758:805 */     textRect.x = (textRect.y = textRect.width = textRect.height = 0);
/* 759:806 */     iconRect.x = (iconRect.y = iconRect.width = iconRect.height = 0);
/* 760:    */   }
/* 761:    */   
/* 762:    */   private static boolean isOceanTheme()
/* 763:    */   {
/* 764:    */     try
/* 765:    */     {
/* 766:811 */       Field field = MetalLookAndFeel.class.getDeclaredField("currentTheme");
/* 767:812 */       field.setAccessible(true);
/* 768:813 */       Object theme = field.get(null);
/* 769:814 */       return "javax.swing.plaf.metal.OceanTheme".equals(theme.getClass().getName());
/* 770:    */     }
/* 771:    */     catch (Exception ex)
/* 772:    */     {
/* 773:816 */       ex.printStackTrace();
/* 774:    */     }
/* 775:818 */     return false;
/* 776:    */   }
/* 777:    */   
/* 778:    */   private static boolean isWindows()
/* 779:    */   {
/* 780:822 */     return UIManager.getLookAndFeel().getID() == "Windows";
/* 781:    */   }
/* 782:    */   
/* 783:    */   private static boolean isMetal()
/* 784:    */   {
/* 785:826 */     return UIManager.getLookAndFeel().getID() == "Metal";
/* 786:    */   }
/* 787:    */   
/* 788:    */   private static boolean isGTK()
/* 789:    */   {
/* 790:830 */     return UIManager.getLookAndFeel().getID() == "GTK";
/* 791:    */   }
/* 792:    */   
/* 793:    */   private static boolean isAqua()
/* 794:    */   {
/* 795:834 */     return UIManager.getLookAndFeel().getID() == "Aqua";
/* 796:    */   }
/* 797:    */   
/* 798:    */   private static boolean isXP()
/* 799:    */   {
/* 800:838 */     if (!checkedForClassic)
/* 801:    */     {
/* 802:    */       try
/* 803:    */       {
/* 804:840 */         CLASSIC_WINDOWS = Class.forName("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
/* 805:    */       }
/* 806:    */       catch (ClassNotFoundException e) {}
/* 807:844 */       checkedForClassic = true;
/* 808:    */     }
/* 809:846 */     if ((CLASSIC_WINDOWS != null) && (CLASSIC_WINDOWS.isInstance(UIManager.getLookAndFeel()))) {
/* 810:848 */       return false;
/* 811:    */     }
/* 812:850 */     Toolkit toolkit = Toolkit.getDefaultToolkit();
/* 813:851 */     Boolean themeActive = (Boolean)toolkit.getDesktopProperty("win.xpstyle.themeActive");
/* 814:853 */     if (themeActive == null) {
/* 815:854 */       themeActive = Boolean.FALSE;
/* 816:    */     }
/* 817:856 */     return themeActive.booleanValue();
/* 818:    */   }
/* 819:    */   
/* 820:    */   public int getComponentBaseline(JComponent component, int width, int height)
/* 821:    */   {
/* 822:886 */     return -1;
/* 823:    */   }
/* 824:    */ }


/* Location:           C:\Users\myros\Desktop\NewGreek2Greenglish.jar
 * Qualified Name:     org.jdesktop.layout.Baseline
 * JD-Core Version:    0.7.0.1
 */