/*   1:    */ package org.jdesktop.layout;
/*   2:    */ 
/*   3:    */ import java.awt.Component;
/*   4:    */ import java.awt.Dimension;
/*   5:    */ import java.awt.Font;
/*   6:    */ import java.awt.FontMetrics;
/*   7:    */ import java.awt.Insets;
/*   8:    */ import java.awt.Rectangle;
/*   9:    */ import java.util.Dictionary;
/*  10:    */ import java.util.Enumeration;
/*  11:    */ import javax.swing.AbstractButton;
/*  12:    */ import javax.swing.ComboBoxEditor;
/*  13:    */ import javax.swing.Icon;
/*  14:    */ import javax.swing.JComboBox;
/*  15:    */ import javax.swing.JComponent;
/*  16:    */ import javax.swing.JLabel;
/*  17:    */ import javax.swing.JList;
/*  18:    */ import javax.swing.JPanel;
/*  19:    */ import javax.swing.JProgressBar;
/*  20:    */ import javax.swing.JScrollPane;
/*  21:    */ import javax.swing.JSlider;
/*  22:    */ import javax.swing.JSpinner;
/*  23:    */ import javax.swing.JSpinner.DefaultEditor;
/*  24:    */ import javax.swing.JTabbedPane;
/*  25:    */ import javax.swing.JTable;
/*  26:    */ import javax.swing.JTextArea;
/*  27:    */ import javax.swing.JTextField;
/*  28:    */ import javax.swing.JTree;
/*  29:    */ import javax.swing.JViewport;
/*  30:    */ import javax.swing.ListCellRenderer;
/*  31:    */ import javax.swing.LookAndFeel;
/*  32:    */ import javax.swing.SwingUtilities;
/*  33:    */ import javax.swing.UIManager;
/*  34:    */ import javax.swing.border.Border;
/*  35:    */ import javax.swing.border.EmptyBorder;
/*  36:    */ import javax.swing.border.TitledBorder;
/*  37:    */ import javax.swing.plaf.TextUI;
/*  38:    */ import javax.swing.text.JTextComponent;
/*  39:    */ import javax.swing.text.View;
/*  40:    */ 
/*  41:    */ class AquaBaseline
/*  42:    */   extends Baseline
/*  43:    */ {
/*  44: 33 */   static final AquaBaseline INSTANCE = new AquaBaseline();
/*  45: 39 */   private static final Rectangle viewRect = new Rectangle();
/*  46: 40 */   private static final Rectangle textRect = new Rectangle();
/*  47: 41 */   private static final Rectangle iconRect = new Rectangle();
/*  48:    */   private static final int EDGE_SPACING = 2;
/*  49:    */   private static final int TEXT_SPACING = 2;
/*  50: 51 */   private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
/*  51:    */   private static JLabel TABLE_LABEL;
/*  52:    */   private static JLabel LIST_LABEL;
/*  53:    */   private static JLabel TREE_LABEL;
/*  54:    */   
/*  55:    */   public int getComponentBaseline(JComponent component, int width, int height)
/*  56:    */   {
/*  57: 75 */     String uid = component.getUIClassID();
/*  58: 76 */     int baseline = -1;
/*  59: 77 */     if ((uid == "ButtonUI") || (uid == "ToggleButtonUI"))
/*  60:    */     {
/*  61: 78 */       baseline = getButtonBaseline((AbstractButton)component, height);
/*  62:    */     }
/*  63: 81 */     else if ((uid == "CheckBoxUI") || (uid == "RadioButtonUI"))
/*  64:    */     {
/*  65: 82 */       baseline = getCheckBoxBaseline((AbstractButton)component, height) + 1;
/*  66:    */     }
/*  67:    */     else
/*  68:    */     {
/*  69: 84 */       if (uid == "ComboBoxUI") {
/*  70: 85 */         return getComboBoxBaseline((JComboBox)component, height);
/*  71:    */       }
/*  72: 88 */       if (uid == "TextAreaUI") {
/*  73: 89 */         return getTextAreaBaseline((JTextArea)component, height);
/*  74:    */       }
/*  75: 91 */       if ((uid == "FormattedTextFieldUI") || (uid == "PasswordFieldUI") || (uid == "TextFieldUI")) {
/*  76: 94 */         baseline = getSingleLineTextBaseline((JTextComponent)component, height);
/*  77: 97 */       } else if (uid == "LabelUI") {
/*  78: 98 */         baseline = getLabelBaseline((JLabel)component, height);
/*  79:100 */       } else if (uid == "ListUI") {
/*  80:101 */         baseline = getListBaseline((JList)component, height);
/*  81:103 */       } else if (uid == "PanelUI") {
/*  82:104 */         baseline = getPanelBaseline((JPanel)component, height);
/*  83:106 */       } else if (uid == "ProgressBarUI") {
/*  84:107 */         baseline = getProgressBarBaseline((JProgressBar)component, height);
/*  85:109 */       } else if (uid == "SliderUI") {
/*  86:110 */         baseline = getSliderBaseline((JSlider)component, height);
/*  87:112 */       } else if (uid == "SpinnerUI") {
/*  88:113 */         baseline = getSpinnerBaseline((JSpinner)component, height);
/*  89:115 */       } else if (uid == "ScrollPaneUI") {
/*  90:116 */         baseline = getScrollPaneBaseline((JScrollPane)component, height);
/*  91:118 */       } else if (uid == "TabbedPaneUI") {
/*  92:119 */         baseline = getTabbedPaneBaseline((JTabbedPane)component, height);
/*  93:121 */       } else if (uid == "TableUI") {
/*  94:122 */         baseline = getTableBaseline((JTable)component, height);
/*  95:124 */       } else if (uid == "TreeUI") {
/*  96:125 */         baseline = getTreeBaseline((JTree)component, height);
/*  97:    */       }
/*  98:    */     }
/*  99:127 */     return Math.max(baseline, -1);
/* 100:    */   }
/* 101:    */   
/* 102:    */   private static Insets rotateInsets(Insets topInsets, int targetPlacement)
/* 103:    */   {
/* 104:131 */     switch (targetPlacement)
/* 105:    */     {
/* 106:    */     case 2: 
/* 107:133 */       return new Insets(topInsets.left, topInsets.top, topInsets.right, topInsets.bottom);
/* 108:    */     case 3: 
/* 109:136 */       return new Insets(topInsets.bottom, topInsets.left, topInsets.top, topInsets.right);
/* 110:    */     case 4: 
/* 111:139 */       return new Insets(topInsets.left, topInsets.bottom, topInsets.right, topInsets.top);
/* 112:    */     }
/* 113:142 */     return new Insets(topInsets.top, topInsets.left, topInsets.bottom, topInsets.right);
/* 114:    */   }
/* 115:    */   
/* 116:    */   private int getMaxTabHeight(JTabbedPane tp)
/* 117:    */   {
/* 118:148 */     int fontHeight = tp.getFontMetrics(tp.getFont()).getHeight();
/* 119:149 */     int height = fontHeight;
/* 120:150 */     boolean tallerIcons = false;
/* 121:151 */     for (int counter = tp.getTabCount() - 1; counter >= 0; counter--)
/* 122:    */     {
/* 123:152 */       Icon icon = tp.getIconAt(counter);
/* 124:153 */       if (icon != null)
/* 125:    */       {
/* 126:154 */         int iconHeight = icon.getIconHeight();
/* 127:155 */         height = Math.max(height, iconHeight);
/* 128:156 */         if (iconHeight > fontHeight) {
/* 129:157 */           tallerIcons = true;
/* 130:    */         }
/* 131:    */       }
/* 132:    */     }
/* 133:161 */     Insets tabInsets = UIManager.getInsets("TabbedPane.tabInsets");
/* 134:162 */     height += 2;
/* 135:    */     
/* 136:    */ 
/* 137:    */ 
/* 138:    */ 
/* 139:167 */     return height;
/* 140:    */   }
/* 141:    */   
/* 142:    */   private int getTabbedPaneBaseline(JTabbedPane tp, int height)
/* 143:    */   {
/* 144:171 */     if (tp.getTabCount() > 0)
/* 145:    */     {
/* 146:172 */       Insets insets = tp.getInsets();
/* 147:173 */       Insets contentBorderInsets = UIManager.getInsets("TabbedPane.contentBorderInsets");
/* 148:    */       
/* 149:175 */       Insets tabAreaInsets = rotateInsets(UIManager.getInsets("TabbedPane.tabAreaInsets"), tp.getTabPlacement());
/* 150:    */       
/* 151:    */ 
/* 152:178 */       FontMetrics metrics = tp.getFontMetrics(tp.getFont());
/* 153:179 */       int maxHeight = getMaxTabHeight(tp);
/* 154:180 */       iconRect.setBounds(0, 0, 0, 0);
/* 155:181 */       textRect.setBounds(0, 0, 0, 0);
/* 156:182 */       viewRect.setBounds(0, 0, 32767, maxHeight);
/* 157:183 */       SwingUtilities.layoutCompoundLabel(tp, metrics, "A", null, 0, 0, 0, 11, viewRect, iconRect, textRect, 0);
/* 158:    */       
/* 159:    */ 
/* 160:    */ 
/* 161:    */ 
/* 162:    */ 
/* 163:    */ 
/* 164:    */ 
/* 165:    */ 
/* 166:192 */       int baseline = textRect.y + metrics.getAscent();
/* 167:193 */       switch (tp.getTabPlacement())
/* 168:    */       {
/* 169:    */       case 1: 
/* 170:195 */         baseline += insets.top + tabAreaInsets.top + 3;
/* 171:    */         
/* 172:    */ 
/* 173:    */ 
/* 174:    */ 
/* 175:    */ 
/* 176:    */ 
/* 177:    */ 
/* 178:    */ 
/* 179:    */ 
/* 180:205 */         return baseline;
/* 181:    */       case 3: 
/* 182:207 */         baseline = tp.getHeight() - insets.bottom - tabAreaInsets.bottom - maxHeight + baseline;
/* 183:    */         
/* 184:    */ 
/* 185:    */ 
/* 186:    */ 
/* 187:    */ 
/* 188:    */ 
/* 189:    */ 
/* 190:    */ 
/* 191:    */ 
/* 192:    */ 
/* 193:218 */         return baseline;
/* 194:    */       case 2: 
/* 195:    */       case 4: 
/* 196:221 */         baseline += insets.top + tabAreaInsets.top;
/* 197:    */         
/* 198:    */ 
/* 199:    */ 
/* 200:    */ 
/* 201:226 */         return baseline;
/* 202:    */       }
/* 203:    */     }
/* 204:229 */     return -1;
/* 205:    */   }
/* 206:    */   
/* 207:    */   private int getSliderBaseline(JSlider slider, int height)
/* 208:    */   {
/* 209:233 */     if (slider.getPaintLabels())
/* 210:    */     {
/* 211:234 */       FontMetrics metrics = slider.getFontMetrics(slider.getFont());
/* 212:235 */       Insets insets = slider.getInsets();
/* 213:236 */       Insets focusInsets = (Insets)UIManager.get("Slider.focusInsets");
/* 214:237 */       if (slider.getOrientation() == 0)
/* 215:    */       {
/* 216:238 */         int tickLength = 8;
/* 217:239 */         int contentHeight = height - insets.top - insets.bottom - focusInsets.top - focusInsets.bottom;
/* 218:    */         
/* 219:241 */         int thumbHeight = 20;
/* 220:    */         
/* 221:    */ 
/* 222:    */ 
/* 223:    */ 
/* 224:    */ 
/* 225:    */ 
/* 226:    */ 
/* 227:    */ 
/* 228:    */ 
/* 229:    */ 
/* 230:    */ 
/* 231:    */ 
/* 232:    */ 
/* 233:255 */         int centerSpacing = thumbHeight;
/* 234:256 */         if (slider.getPaintTicks()) {
/* 235:258 */           centerSpacing += tickLength;
/* 236:    */         }
/* 237:261 */         centerSpacing += metrics.getAscent() + metrics.getDescent();
/* 238:262 */         int trackY = insets.top + focusInsets.top + (contentHeight - centerSpacing - 1) / 2;
/* 239:    */         
/* 240:264 */         int trackHeight = thumbHeight;
/* 241:265 */         int tickY = trackY + trackHeight;
/* 242:266 */         int tickHeight = tickLength;
/* 243:267 */         if (!slider.getPaintTicks()) {
/* 244:268 */           tickHeight = 0;
/* 245:    */         }
/* 246:270 */         int labelY = tickY + tickHeight;
/* 247:271 */         return labelY + metrics.getAscent();
/* 248:    */       }
/* 249:274 */       boolean inverted = slider.getInverted();
/* 250:275 */       Integer value = inverted ? getMinSliderValue(slider) : getMaxSliderValue(slider);
/* 251:277 */       if (value != null)
/* 252:    */       {
/* 253:278 */         int thumbHeight = 11;
/* 254:    */         
/* 255:    */ 
/* 256:    */ 
/* 257:    */ 
/* 258:    */ 
/* 259:284 */         int trackBuffer = Math.max(metrics.getHeight() / 2, thumbHeight / 2);
/* 260:    */         
/* 261:286 */         int contentY = focusInsets.top + insets.top;
/* 262:287 */         int trackY = contentY + trackBuffer;
/* 263:288 */         int trackHeight = height - focusInsets.top - focusInsets.bottom - insets.top - insets.bottom - trackBuffer - trackBuffer;
/* 264:    */         
/* 265:    */ 
/* 266:291 */         int maxValue = getMaxSliderValue(slider).intValue();
/* 267:292 */         int min = slider.getMinimum();
/* 268:293 */         int max = slider.getMaximum();
/* 269:294 */         double valueRange = max - min;
/* 270:295 */         double pixelsPerValue = trackHeight / valueRange;
/* 271:    */         
/* 272:297 */         int trackBottom = trackY + (trackHeight - 1);
/* 273:300 */         if (!inverted)
/* 274:    */         {
/* 275:301 */           int yPosition = trackY;
/* 276:302 */           yPosition = (int)(yPosition + Math.round(pixelsPerValue * (max - value.intValue())));
/* 277:    */         }
/* 278:    */         else
/* 279:    */         {
/* 280:306 */           yPosition = trackY;
/* 281:307 */           yPosition = (int)(yPosition + Math.round(pixelsPerValue * (value.intValue() - min)));
/* 282:    */         }
/* 283:310 */         int yPosition = Math.max(trackY, yPosition);
/* 284:311 */         yPosition = Math.min(trackBottom, yPosition);
/* 285:312 */         return yPosition - metrics.getHeight() / 2 + metrics.getAscent();
/* 286:    */       }
/* 287:    */     }
/* 288:317 */     return -1;
/* 289:    */   }
/* 290:    */   
/* 291:    */   private Integer getMaxSliderValue(JSlider slider)
/* 292:    */   {
/* 293:321 */     Dictionary dictionary = slider.getLabelTable();
/* 294:322 */     if (dictionary != null)
/* 295:    */     {
/* 296:323 */       Enumeration keys = dictionary.keys();
/* 297:324 */       int max = slider.getMinimum() - 1;
/* 298:325 */       while (keys.hasMoreElements()) {
/* 299:326 */         max = Math.max(max, ((Integer)keys.nextElement()).intValue());
/* 300:    */       }
/* 301:328 */       if (max == slider.getMinimum() - 1) {
/* 302:329 */         return null;
/* 303:    */       }
/* 304:331 */       return new Integer(max);
/* 305:    */     }
/* 306:333 */     return null;
/* 307:    */   }
/* 308:    */   
/* 309:    */   private Integer getMinSliderValue(JSlider slider)
/* 310:    */   {
/* 311:337 */     Dictionary dictionary = slider.getLabelTable();
/* 312:338 */     if (dictionary != null)
/* 313:    */     {
/* 314:339 */       Enumeration keys = dictionary.keys();
/* 315:340 */       int min = slider.getMaximum() + 1;
/* 316:341 */       while (keys.hasMoreElements()) {
/* 317:342 */         min = Math.min(min, ((Integer)keys.nextElement()).intValue());
/* 318:    */       }
/* 319:344 */       if (min == slider.getMaximum() + 1) {
/* 320:345 */         return null;
/* 321:    */       }
/* 322:347 */       return new Integer(min);
/* 323:    */     }
/* 324:349 */     return null;
/* 325:    */   }
/* 326:    */   
/* 327:    */   private int getProgressBarBaseline(JProgressBar pb, int height)
/* 328:    */   {
/* 329:353 */     if ((pb.isStringPainted()) && (pb.getOrientation() == 0))
/* 330:    */     {
/* 331:355 */       FontMetrics metrics = pb.getFontMetrics(pb.getFont());
/* 332:356 */       Insets insets = pb.getInsets();
/* 333:357 */       int y = insets.top;
/* 334:    */       
/* 335:    */ 
/* 336:    */ 
/* 337:    */ 
/* 338:    */ 
/* 339:    */ 
/* 340:    */ 
/* 341:    */ 
/* 342:    */ 
/* 343:    */ 
/* 344:    */ 
/* 345:    */ 
/* 346:    */ 
/* 347:    */ 
/* 348:    */ 
/* 349:    */ 
/* 350:374 */       height -= insets.top + insets.bottom;
/* 351:    */       
/* 352:376 */       return y + (height + metrics.getAscent() - metrics.getLeading() - metrics.getDescent()) / 2;
/* 353:    */     }
/* 354:380 */     return -1;
/* 355:    */   }
/* 356:    */   
/* 357:    */   private int getTreeBaseline(JTree tree, int height)
/* 358:    */   {
/* 359:384 */     int rowHeight = tree.getRowHeight();
/* 360:385 */     if (TREE_LABEL == null)
/* 361:    */     {
/* 362:386 */       TREE_LABEL = new JLabel("X");
/* 363:387 */       TREE_LABEL.setIcon(UIManager.getIcon("Tree.closedIcon"));
/* 364:    */     }
/* 365:389 */     JLabel label = TREE_LABEL;
/* 366:390 */     label.setFont(tree.getFont());
/* 367:391 */     if (rowHeight <= 0) {
/* 368:392 */       rowHeight = label.getPreferredSize().height;
/* 369:    */     }
/* 370:394 */     return getLabelBaseline(label, rowHeight) + tree.getInsets().top;
/* 371:    */   }
/* 372:    */   
/* 373:    */   private int getTableBaseline(JTable table, int height)
/* 374:    */   {
/* 375:398 */     if (TABLE_LABEL == null)
/* 376:    */     {
/* 377:399 */       TABLE_LABEL = new JLabel("");
/* 378:400 */       TABLE_LABEL.setBorder(new EmptyBorder(1, 1, 1, 1));
/* 379:    */     }
/* 380:402 */     JLabel label = TABLE_LABEL;
/* 381:403 */     label.setFont(table.getFont());
/* 382:404 */     int rowMargin = table.getRowMargin();
/* 383:405 */     int baseline = getLabelBaseline(label, table.getRowHeight() - rowMargin);
/* 384:    */     
/* 385:407 */     return baseline += rowMargin / 2;
/* 386:    */   }
/* 387:    */   
/* 388:    */   private int getTextAreaBaseline(JTextArea text, int height)
/* 389:    */   {
/* 390:411 */     Insets insets = text.getInsets();
/* 391:412 */     FontMetrics fm = text.getFontMetrics(text.getFont());
/* 392:413 */     return insets.top + fm.getAscent();
/* 393:    */   }
/* 394:    */   
/* 395:    */   private int getListBaseline(JList list, int height)
/* 396:    */   {
/* 397:417 */     int rowHeight = list.getFixedCellHeight();
/* 398:418 */     if (LIST_LABEL == null)
/* 399:    */     {
/* 400:419 */       LIST_LABEL = new JLabel("X");
/* 401:420 */       LIST_LABEL.setBorder(new EmptyBorder(1, 1, 1, 1));
/* 402:    */     }
/* 403:422 */     JLabel label = LIST_LABEL;
/* 404:423 */     label.setFont(list.getFont());
/* 405:431 */     if (rowHeight == -1) {
/* 406:432 */       rowHeight = label.getPreferredSize().height;
/* 407:    */     }
/* 408:434 */     return getLabelBaseline(label, rowHeight) + list.getInsets().top;
/* 409:    */   }
/* 410:    */   
/* 411:    */   private int getScrollPaneBaseline(JScrollPane sp, int height)
/* 412:    */   {
/* 413:438 */     Component view = sp.getViewport().getView();
/* 414:439 */     if ((view instanceof JComponent))
/* 415:    */     {
/* 416:440 */       int baseline = getBaseline((JComponent)view);
/* 417:441 */       if (baseline > 0) {
/* 418:442 */         return baseline + sp.getViewport().getY();
/* 419:    */       }
/* 420:    */     }
/* 421:445 */     return -1;
/* 422:    */   }
/* 423:    */   
/* 424:    */   private int getPanelBaseline(JPanel panel, int height)
/* 425:    */   {
/* 426:449 */     Border border = panel.getBorder();
/* 427:450 */     if ((border instanceof TitledBorder))
/* 428:    */     {
/* 429:451 */       TitledBorder titledBorder = (TitledBorder)border;
/* 430:452 */       if ((titledBorder.getTitle() != null) && (!"".equals(titledBorder.getTitle())))
/* 431:    */       {
/* 432:454 */         Font font = titledBorder.getTitleFont();
/* 433:455 */         if (font == null)
/* 434:    */         {
/* 435:456 */           font = panel.getFont();
/* 436:457 */           if (font == null) {
/* 437:458 */             font = new Font("Dialog", 0, 12);
/* 438:    */           }
/* 439:    */         }
/* 440:461 */         Border border2 = titledBorder.getBorder();
/* 441:    */         Insets borderInsets;
/* 442:    */         Insets borderInsets;
/* 443:463 */         if (border2 != null) {
/* 444:464 */           borderInsets = border2.getBorderInsets(panel);
/* 445:    */         } else {
/* 446:467 */           borderInsets = EMPTY_INSETS;
/* 447:    */         }
/* 448:469 */         FontMetrics fm = panel.getFontMetrics(font);
/* 449:470 */         int fontHeight = fm.getHeight();
/* 450:471 */         int descent = fm.getDescent();
/* 451:472 */         int ascent = fm.getAscent();
/* 452:473 */         int y = 2;
/* 453:474 */         int h = height - 4;
/* 454:476 */         switch (((TitledBorder)border).getTitlePosition())
/* 455:    */         {
/* 456:    */         case 1: 
/* 457:478 */           int diff = ascent + descent + (Math.max(2, 4) - 2);
/* 458:    */           
/* 459:480 */           return y + diff - (descent + 2);
/* 460:    */         case 0: 
/* 461:    */         case 2: 
/* 462:483 */           int diff = Math.max(0, ascent / 2 + 2 - 2);
/* 463:    */           
/* 464:485 */           return y + diff - descent + (borderInsets.top + ascent + descent) / 2;
/* 465:    */         case 3: 
/* 466:488 */           return y + borderInsets.top + ascent + 2;
/* 467:    */         case 4: 
/* 468:490 */           return y + h - (borderInsets.bottom + descent + 2);
/* 469:    */         case 5: 
/* 470:493 */           h -= fontHeight / 2;
/* 471:494 */           return y + h - descent + (ascent + descent - borderInsets.bottom) / 2;
/* 472:    */         case 6: 
/* 473:497 */           h -= fontHeight;
/* 474:498 */           return y + h + ascent + 2;
/* 475:    */         }
/* 476:    */       }
/* 477:    */     }
/* 478:502 */     return -1;
/* 479:    */   }
/* 480:    */   
/* 481:    */   private int getSpinnerBaseline(JSpinner spinner, int height)
/* 482:    */   {
/* 483:506 */     JComponent editor = spinner.getEditor();
/* 484:507 */     if ((editor instanceof JSpinner.DefaultEditor))
/* 485:    */     {
/* 486:508 */       JSpinner.DefaultEditor defaultEditor = (JSpinner.DefaultEditor)editor;
/* 487:    */       
/* 488:510 */       JTextField tf = defaultEditor.getTextField();
/* 489:511 */       Insets spinnerInsets = spinner.getInsets();
/* 490:512 */       Insets editorInsets = defaultEditor.getInsets();
/* 491:513 */       int offset = spinnerInsets.top + editorInsets.top;
/* 492:514 */       height -= offset + spinnerInsets.bottom + editorInsets.bottom;
/* 493:515 */       if (height <= 0) {
/* 494:516 */         return -1;
/* 495:    */       }
/* 496:518 */       return offset + getSingleLineTextBaseline(tf, height);
/* 497:    */     }
/* 498:520 */     Insets insets = spinner.getInsets();
/* 499:521 */     FontMetrics fm = spinner.getFontMetrics(spinner.getFont());
/* 500:522 */     return insets.top + fm.getAscent();
/* 501:    */   }
/* 502:    */   
/* 503:    */   private int getLabelBaseline(JLabel label, int height)
/* 504:    */   {
/* 505:526 */     Icon icon = label.isEnabled() ? label.getIcon() : label.getDisabledIcon();
/* 506:    */     
/* 507:528 */     FontMetrics fm = label.getFontMetrics(label.getFont());
/* 508:    */     
/* 509:530 */     resetRects(label, height);
/* 510:    */     
/* 511:532 */     SwingUtilities.layoutCompoundLabel(label, fm, "a", icon, label.getVerticalAlignment(), label.getHorizontalAlignment(), label.getVerticalTextPosition(), label.getHorizontalTextPosition(), viewRect, iconRect, textRect, label.getIconTextGap());
/* 512:    */     
/* 513:    */ 
/* 514:    */ 
/* 515:    */ 
/* 516:    */ 
/* 517:538 */     return textRect.y + fm.getAscent();
/* 518:    */   }
/* 519:    */   
/* 520:    */   private int getComboBoxBaseline(JComboBox combobox, int height)
/* 521:    */   {
/* 522:542 */     Insets insets = combobox.getInsets();
/* 523:543 */     int y = insets.top;
/* 524:544 */     height -= insets.top + insets.bottom;
/* 525:545 */     if (combobox.isEditable())
/* 526:    */     {
/* 527:546 */       ComboBoxEditor editor = combobox.getEditor();
/* 528:547 */       if ((editor != null) && ((editor.getEditorComponent() instanceof JTextField)))
/* 529:    */       {
/* 530:549 */         JTextField tf = (JTextField)editor.getEditorComponent();
/* 531:550 */         return y + getSingleLineTextBaseline(tf, height);
/* 532:    */       }
/* 533:    */     }
/* 534:553 */     y--;
/* 535:    */     
/* 536:555 */     ListCellRenderer renderer = combobox.getRenderer();
/* 537:556 */     if ((renderer instanceof JLabel)) {
/* 538:557 */       return y + getLabelBaseline((JLabel)renderer, height);
/* 539:    */     }
/* 540:560 */     FontMetrics fm = combobox.getFontMetrics(combobox.getFont());
/* 541:561 */     return y + fm.getAscent();
/* 542:    */   }
/* 543:    */   
/* 544:    */   private int getSingleLineTextBaseline(JTextComponent textComponent, int h)
/* 545:    */   {
/* 546:570 */     View rootView = textComponent.getUI().getRootView(textComponent);
/* 547:571 */     if (rootView.getViewCount() > 0)
/* 548:    */     {
/* 549:572 */       Insets insets = textComponent.getInsets();
/* 550:573 */       int height = h - insets.top - insets.bottom;
/* 551:574 */       int y = insets.top;
/* 552:575 */       View fieldView = rootView.getView(0);
/* 553:576 */       int vspan = (int)fieldView.getPreferredSpan(1);
/* 554:577 */       if (height != vspan)
/* 555:    */       {
/* 556:578 */         int slop = height - vspan;
/* 557:579 */         y += slop / 2;
/* 558:    */       }
/* 559:581 */       FontMetrics fm = textComponent.getFontMetrics(textComponent.getFont());
/* 560:    */       
/* 561:583 */       y += fm.getAscent();
/* 562:584 */       return y;
/* 563:    */     }
/* 564:586 */     return -1;
/* 565:    */   }
/* 566:    */   
/* 567:    */   private int getCheckBoxBaseline(AbstractButton button, int height)
/* 568:    */   {
/* 569:593 */     FontMetrics fm = button.getFontMetrics(button.getFont());
/* 570:    */     
/* 571:595 */     resetRects(button, height);
/* 572:    */     
/* 573:    */ 
/* 574:    */ 
/* 575:    */ 
/* 576:600 */     SwingUtilities.layoutCompoundLabel(button, fm, "a", button.getIcon(), button.getVerticalAlignment(), button.getHorizontalAlignment(), button.getVerticalTextPosition(), button.getHorizontalTextPosition(), viewRect, iconRect, textRect, button.getText() == null ? 0 : button.getIconTextGap());
/* 577:    */     
/* 578:    */ 
/* 579:    */ 
/* 580:    */ 
/* 581:    */ 
/* 582:    */ 
/* 583:    */ 
/* 584:608 */     return textRect.y + fm.getAscent();
/* 585:    */   }
/* 586:    */   
/* 587:    */   private int getButtonBaseline(AbstractButton button, int height)
/* 588:    */   {
/* 589:614 */     FontMetrics fm = button.getFontMetrics(button.getFont());
/* 590:    */     
/* 591:616 */     resetRects(button, height);
/* 592:    */     
/* 593:    */ 
/* 594:    */ 
/* 595:    */ 
/* 596:621 */     SwingUtilities.layoutCompoundLabel(button, fm, "a", button.getIcon(), button.getVerticalAlignment(), button.getHorizontalAlignment(), button.getVerticalTextPosition(), button.getHorizontalTextPosition(), viewRect, iconRect, textRect, button.getText() == null ? 0 : button.getIconTextGap());
/* 597:    */     
/* 598:    */ 
/* 599:    */ 
/* 600:    */ 
/* 601:    */ 
/* 602:    */ 
/* 603:    */ 
/* 604:629 */     return textRect.y + fm.getAscent() + 1;
/* 605:    */   }
/* 606:    */   
/* 607:    */   private void resetRects(JComponent c, int height)
/* 608:    */   {
/* 609:633 */     Insets insets = c.getInsets();
/* 610:634 */     viewRect.x = insets.left;
/* 611:635 */     viewRect.y = insets.top;
/* 612:636 */     viewRect.width = (c.getWidth() - (insets.right + viewRect.x));
/* 613:637 */     viewRect.height = (height - (insets.bottom + viewRect.y));
/* 614:638 */     textRect.x = (textRect.y = textRect.width = textRect.height = 0);
/* 615:639 */     iconRect.x = (iconRect.y = iconRect.width = iconRect.height = 0);
/* 616:    */   }
/* 617:    */   
/* 618:    */   private static boolean isMac()
/* 619:    */   {
/* 620:643 */     return UIManager.getLookAndFeel().getID() == "Mac";
/* 621:    */   }
/* 622:    */   
/* 623:    */   private static boolean isAqua()
/* 624:    */   {
/* 625:647 */     return UIManager.getLookAndFeel().getID() == "Aqua";
/* 626:    */   }
/* 627:    */ }


/* Location:           C:\Users\myros\Desktop\NewGreek2Greenglish.jar
 * Qualified Name:     org.jdesktop.layout.AquaBaseline
 * JD-Core Version:    0.7.0.1
 */