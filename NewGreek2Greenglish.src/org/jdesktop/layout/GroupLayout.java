/*    1:     */ package org.jdesktop.layout;
/*    2:     */ 
/*    3:     */ import java.awt.Component;
/*    4:     */ import java.awt.Container;
/*    5:     */ import java.awt.Dimension;
/*    6:     */ import java.awt.Insets;
/*    7:     */ import java.awt.LayoutManager2;
/*    8:     */ import java.util.ArrayList;
/*    9:     */ import java.util.Collection;
/*   10:     */ import java.util.Collections;
/*   11:     */ import java.util.HashMap;
/*   12:     */ import java.util.Iterator;
/*   13:     */ import java.util.List;
/*   14:     */ import java.util.Map;
/*   15:     */ import javax.swing.JComponent;
/*   16:     */ 
/*   17:     */ public class GroupLayout
/*   18:     */   implements LayoutManager2
/*   19:     */ {
/*   20:     */   private static final int MIN_SIZE = 0;
/*   21:     */   private static final int PREF_SIZE = 1;
/*   22:     */   private static final int MAX_SIZE = 2;
/*   23:     */   private static final int UNSET = -2147483648;
/*   24:     */   public static final int HORIZONTAL = 1;
/*   25:     */   public static final int VERTICAL = 2;
/*   26:     */   private static final int NO_ALIGNMENT = 0;
/*   27:     */   public static final int LEADING = 1;
/*   28:     */   public static final int TRAILING = 2;
/*   29:     */   public static final int CENTER = 3;
/*   30:     */   public static final int BASELINE = 3;
/*   31:     */   public static final int DEFAULT_SIZE = -1;
/*   32:     */   public static final int PREFERRED_SIZE = -2;
/*   33:     */   private boolean autocreatePadding;
/*   34:     */   private boolean autocreateContainerPadding;
/*   35:     */   private Group horizontalGroup;
/*   36:     */   private Group verticalGroup;
/*   37:     */   private Map componentInfos;
/*   38:     */   private Container host;
/*   39:     */   private List parallelList;
/*   40:     */   private boolean springsChanged;
/*   41:     */   private boolean hasPreferredPaddingSprings;
/*   42:     */   
/*   43:     */   private static void checkSize(int min, int pref, int max, boolean isComponentSpring)
/*   44:     */   {
/*   45: 192 */     checkResizeType(min, isComponentSpring);
/*   46: 193 */     if ((!isComponentSpring) && (pref < 0)) {
/*   47: 194 */       throw new IllegalArgumentException("Pref must be >= 0");
/*   48:     */     }
/*   49: 196 */     checkResizeType(max, isComponentSpring);
/*   50: 197 */     checkLessThan(min, pref);
/*   51: 198 */     checkLessThan(min, max);
/*   52: 199 */     checkLessThan(pref, max);
/*   53:     */   }
/*   54:     */   
/*   55:     */   private static void checkResizeType(int type, boolean isComponentSpring)
/*   56:     */   {
/*   57: 203 */     if ((type < 0) && (((isComponentSpring) && (type != -1) && (type != -2)) || ((!isComponentSpring) && (type != -2)))) {
/*   58: 206 */       throw new IllegalArgumentException("Invalid size");
/*   59:     */     }
/*   60:     */   }
/*   61:     */   
/*   62:     */   private static void checkLessThan(int min, int max)
/*   63:     */   {
/*   64: 211 */     if ((min >= 0) && (max >= 0) && (min > max)) {
/*   65: 212 */       throw new IllegalArgumentException("Following is not met: min<=pref<=max");
/*   66:     */     }
/*   67:     */   }
/*   68:     */   
/*   69:     */   private static void checkAlignment(int alignment, boolean allowsBaseline)
/*   70:     */   {
/*   71: 219 */     if ((alignment == 1) || (alignment == 2) || (alignment == 3)) {
/*   72: 221 */       return;
/*   73:     */     }
/*   74: 223 */     if ((allowsBaseline) && (alignment != 3)) {
/*   75: 224 */       throw new IllegalArgumentException("Alignment must be one of:LEADING, TRAILING, CENTER or BASELINE");
/*   76:     */     }
/*   77: 227 */     throw new IllegalArgumentException("Alignment must be one of:LEADING, TRAILING or CENTER");
/*   78:     */   }
/*   79:     */   
/*   80:     */   static boolean isVisible(Component c)
/*   81:     */   {
/*   82: 235 */     return c.isVisible();
/*   83:     */   }
/*   84:     */   
/*   85:     */   public GroupLayout(Container host)
/*   86:     */   {
/*   87: 245 */     if (host == null) {
/*   88: 246 */       throw new IllegalArgumentException("Container must be non-null");
/*   89:     */     }
/*   90: 248 */     this.host = host;
/*   91: 249 */     setHorizontalGroup(createParallelGroup(1, true));
/*   92: 250 */     setVerticalGroup(createParallelGroup(1, true));
/*   93: 251 */     this.componentInfos = new HashMap();
/*   94: 252 */     this.autocreatePadding = false;
/*   95: 253 */     this.parallelList = new ArrayList();
/*   96:     */   }
/*   97:     */   
/*   98:     */   public String toString()
/*   99:     */   {
/*  100: 263 */     StringBuffer buffer = new StringBuffer();
/*  101: 264 */     buffer.append("HORIZONTAL\n");
/*  102: 265 */     dump(buffer, this.horizontalGroup, "  ", 1);
/*  103: 266 */     buffer.append("\nVERTICAL\n");
/*  104: 267 */     dump(buffer, this.verticalGroup, "  ", 2);
/*  105: 268 */     return buffer.toString();
/*  106:     */   }
/*  107:     */   
/*  108:     */   private void dump(StringBuffer buffer, Spring spring, String indent, int axis)
/*  109:     */   {
/*  110: 273 */     String origin = "";
/*  111: 274 */     String padding = "";
/*  112: 275 */     if ((spring instanceof ComponentSpring))
/*  113:     */     {
/*  114: 276 */       ComponentSpring cSpring = (ComponentSpring)spring;
/*  115: 277 */       origin = Integer.toString(cSpring.getOrigin()) + " ";
/*  116: 278 */       String name = cSpring.getComponent().getName();
/*  117: 279 */       if (name != null) {
/*  118: 280 */         origin = "name=" + name + ", ";
/*  119:     */       }
/*  120:     */     }
/*  121: 283 */     if ((spring instanceof AutopaddingSpring))
/*  122:     */     {
/*  123: 284 */       AutopaddingSpring paddingSpring = (AutopaddingSpring)spring;
/*  124: 285 */       padding = ", userCreated=" + paddingSpring.getUserCreated() + ", matches=" + paddingSpring.getMatchDescription();
/*  125:     */     }
/*  126: 288 */     buffer.append(indent + spring.getClass().getName() + " " + Integer.toHexString(spring.hashCode()) + " " + origin + ", size=" + spring.getSize() + ", alignment=" + spring.getAlignment() + " prefs=[" + spring.getMinimumSize(axis) + " " + spring.getPreferredSize(axis) + " " + spring.getMaximumSize(axis) + padding + "]\n");
/*  127: 297 */     if ((spring instanceof Group))
/*  128:     */     {
/*  129: 298 */       List springs = ((Group)spring).springs;
/*  130: 299 */       indent = indent + "  ";
/*  131: 300 */       for (int counter = 0; counter < springs.size(); counter++) {
/*  132: 301 */         dump(buffer, (Spring)springs.get(counter), indent, axis);
/*  133:     */       }
/*  134:     */     }
/*  135:     */   }
/*  136:     */   
/*  137:     */   public void setAutocreateGaps(boolean autocreatePadding)
/*  138:     */   {
/*  139: 317 */     this.autocreatePadding = autocreatePadding;
/*  140:     */   }
/*  141:     */   
/*  142:     */   public boolean getAutocreateGaps()
/*  143:     */   {
/*  144: 326 */     return this.autocreatePadding;
/*  145:     */   }
/*  146:     */   
/*  147:     */   public void setAutocreateContainerGaps(boolean autocreatePadding)
/*  148:     */   {
/*  149: 338 */     if (autocreatePadding != this.autocreateContainerPadding)
/*  150:     */     {
/*  151: 339 */       this.autocreateContainerPadding = autocreatePadding;
/*  152: 340 */       this.horizontalGroup = createTopLevelGroup(getHorizontalGroup());
/*  153: 341 */       this.verticalGroup = createTopLevelGroup(getVerticalGroup());
/*  154:     */     }
/*  155:     */   }
/*  156:     */   
/*  157:     */   public boolean getAutocreateContainerGaps()
/*  158:     */   {
/*  159: 354 */     return this.autocreateContainerPadding;
/*  160:     */   }
/*  161:     */   
/*  162:     */   public void setHorizontalGroup(Group group)
/*  163:     */   {
/*  164: 366 */     if (group == null) {
/*  165: 367 */       throw new IllegalArgumentException("Group must be non-null");
/*  166:     */     }
/*  167: 369 */     this.horizontalGroup = createTopLevelGroup(group);
/*  168:     */   }
/*  169:     */   
/*  170:     */   public Group getHorizontalGroup()
/*  171:     */   {
/*  172: 380 */     int index = 0;
/*  173: 381 */     if (this.horizontalGroup.springs.size() > 1) {
/*  174: 382 */       index = 1;
/*  175:     */     }
/*  176: 384 */     return (Group)this.horizontalGroup.springs.get(index);
/*  177:     */   }
/*  178:     */   
/*  179:     */   public void setVerticalGroup(Group group)
/*  180:     */   {
/*  181: 396 */     if (group == null) {
/*  182: 397 */       throw new IllegalArgumentException("Group must be non-null");
/*  183:     */     }
/*  184: 399 */     this.verticalGroup = createTopLevelGroup(group);
/*  185:     */   }
/*  186:     */   
/*  187:     */   public Group getVerticalGroup()
/*  188:     */   {
/*  189: 410 */     int index = 0;
/*  190: 411 */     if (this.verticalGroup.springs.size() > 1) {
/*  191: 412 */       index = 1;
/*  192:     */     }
/*  193: 414 */     return (Group)this.verticalGroup.springs.get(index);
/*  194:     */   }
/*  195:     */   
/*  196:     */   private Group createTopLevelGroup(Group specifiedGroup)
/*  197:     */   {
/*  198: 423 */     SequentialGroup group = createSequentialGroup();
/*  199: 424 */     if (getAutocreateContainerGaps())
/*  200:     */     {
/*  201: 425 */       group.addSpring(new ContainerAutopaddingSpring());
/*  202: 426 */       group.add(specifiedGroup);
/*  203: 427 */       group.addSpring(new ContainerAutopaddingSpring());
/*  204:     */     }
/*  205:     */     else
/*  206:     */     {
/*  207: 429 */       group.add(specifiedGroup);
/*  208:     */     }
/*  209: 431 */     return group;
/*  210:     */   }
/*  211:     */   
/*  212:     */   public SequentialGroup createSequentialGroup()
/*  213:     */   {
/*  214: 440 */     return new SequentialGroup();
/*  215:     */   }
/*  216:     */   
/*  217:     */   public ParallelGroup createParallelGroup()
/*  218:     */   {
/*  219: 452 */     return createParallelGroup(1);
/*  220:     */   }
/*  221:     */   
/*  222:     */   public ParallelGroup createParallelGroup(int alignment)
/*  223:     */   {
/*  224: 472 */     return createParallelGroup(alignment, true);
/*  225:     */   }
/*  226:     */   
/*  227:     */   public ParallelGroup createParallelGroup(int alignment, boolean resizable)
/*  228:     */   {
/*  229: 495 */     if (alignment == 3) {
/*  230: 496 */       return new BaselineGroup(resizable);
/*  231:     */     }
/*  232: 498 */     return new ParallelGroup(alignment, resizable);
/*  233:     */   }
/*  234:     */   
/*  235:     */   public void linkSize(Component[] components)
/*  236:     */   {
/*  237: 513 */     linkSize(components, 3);
/*  238:     */   }
/*  239:     */   
/*  240:     */   public void linkSize(Component[] components, int axis)
/*  241:     */   {
/*  242: 532 */     if (components == null) {
/*  243: 533 */       throw new IllegalArgumentException("Components must be non-null");
/*  244:     */     }
/*  245: 535 */     boolean horizontal = (axis & 0x1) == 1;
/*  246: 536 */     boolean vertical = (axis & 0x2) == 2;
/*  247: 537 */     if ((!vertical) && (!horizontal)) {
/*  248: 538 */       throw new IllegalArgumentException("Axis must contain HORIZONTAL or VERTICAL");
/*  249:     */     }
/*  250: 541 */     for (int counter = components.length - 1; counter >= 0; counter--)
/*  251:     */     {
/*  252: 542 */       Component c = components[counter];
/*  253: 543 */       if (components[counter] == null) {
/*  254: 544 */         throw new IllegalArgumentException("Components must be non-null");
/*  255:     */       }
/*  256: 548 */       getComponentInfo(c);
/*  257:     */     }
/*  258: 550 */     if (horizontal) {
/*  259: 551 */       linkSize0(components, 1);
/*  260:     */     }
/*  261: 553 */     if (vertical) {
/*  262: 554 */       linkSize0(components, 2);
/*  263:     */     }
/*  264:     */   }
/*  265:     */   
/*  266:     */   private void linkSize0(Component[] components, int axis)
/*  267:     */   {
/*  268: 559 */     ComponentInfo master = getComponentInfo(components[(components.length - 1)]).getMasterComponentInfo(axis);
/*  269: 561 */     for (int counter = components.length - 2; counter >= 0; counter--) {
/*  270: 562 */       master.addChild(getComponentInfo(components[counter]), axis);
/*  271:     */     }
/*  272:     */   }
/*  273:     */   
/*  274:     */   public void replace(Component existingComponent, Component newComponent)
/*  275:     */   {
/*  276: 576 */     if ((existingComponent == null) || (newComponent == null)) {
/*  277: 577 */       throw new IllegalArgumentException("Components must be non-null");
/*  278:     */     }
/*  279: 581 */     if (this.springsChanged)
/*  280:     */     {
/*  281: 582 */       registerComponents(this.horizontalGroup, 1);
/*  282: 583 */       registerComponents(this.verticalGroup, 2);
/*  283:     */     }
/*  284: 585 */     ComponentInfo info = (ComponentInfo)this.componentInfos.remove(existingComponent);
/*  285: 587 */     if (info == null) {
/*  286: 588 */       throw new IllegalArgumentException("Component must already exist");
/*  287:     */     }
/*  288: 590 */     this.host.remove(existingComponent);
/*  289: 591 */     this.host.add(newComponent);
/*  290: 592 */     info.setComponent(newComponent);
/*  291: 593 */     this.componentInfos.put(newComponent, info);
/*  292: 594 */     invalidateLayout(this.host);
/*  293:     */   }
/*  294:     */   
/*  295:     */   public void addLayoutComponent(String name, Component component) {}
/*  296:     */   
/*  297:     */   public void removeLayoutComponent(Component component)
/*  298:     */   {
/*  299: 622 */     ComponentInfo info = (ComponentInfo)this.componentInfos.remove(component);
/*  300: 623 */     if (info != null)
/*  301:     */     {
/*  302: 624 */       info.dispose();
/*  303: 625 */       this.springsChanged = true;
/*  304:     */     }
/*  305:     */   }
/*  306:     */   
/*  307:     */   public Dimension preferredLayoutSize(Container parent)
/*  308:     */   {
/*  309: 640 */     checkParent(parent);
/*  310: 641 */     prepare(1);
/*  311: 642 */     return adjustSize(this.horizontalGroup.getPreferredSize(1), this.verticalGroup.getPreferredSize(2));
/*  312:     */   }
/*  313:     */   
/*  314:     */   public Dimension minimumLayoutSize(Container parent)
/*  315:     */   {
/*  316: 657 */     checkParent(parent);
/*  317: 658 */     prepare(0);
/*  318: 659 */     return adjustSize(this.horizontalGroup.getMinimumSize(1), this.verticalGroup.getMinimumSize(2));
/*  319:     */   }
/*  320:     */   
/*  321:     */   public void layoutContainer(Container parent)
/*  322:     */   {
/*  323: 671 */     prepare();
/*  324: 672 */     Insets insets = parent.getInsets();
/*  325: 674 */     if ((getAutocreateGaps()) || (getAutocreateContainerGaps()) || (this.hasPreferredPaddingSprings))
/*  326:     */     {
/*  327: 676 */       resetAutopadding(this.horizontalGroup, 1, -1, 0, parent.getWidth() - insets.left - insets.right);
/*  328:     */       
/*  329: 678 */       resetAutopadding(this.verticalGroup, 2, -1, 0, parent.getHeight() - insets.top - insets.bottom);
/*  330:     */     }
/*  331: 681 */     this.horizontalGroup.setSize(1, 0, parent.getWidth() - insets.left - insets.right);
/*  332:     */     
/*  333: 683 */     this.verticalGroup.setSize(2, 0, parent.getHeight() - insets.top - insets.bottom);
/*  334:     */     
/*  335:     */ 
/*  336: 686 */     Iterator componentInfo = this.componentInfos.values().iterator();
/*  337: 687 */     while (componentInfo.hasNext())
/*  338:     */     {
/*  339: 688 */       ComponentInfo info = (ComponentInfo)componentInfo.next();
/*  340: 689 */       Component c = info.getComponent();
/*  341: 690 */       info.setBounds(insets);
/*  342:     */     }
/*  343:     */   }
/*  344:     */   
/*  345:     */   public void addLayoutComponent(Component component, Object constraints) {}
/*  346:     */   
/*  347:     */   public Dimension maximumLayoutSize(Container parent)
/*  348:     */   {
/*  349: 720 */     checkParent(parent);
/*  350: 721 */     prepare(2);
/*  351: 722 */     return adjustSize(this.horizontalGroup.getMaximumSize(1), this.verticalGroup.getMaximumSize(2));
/*  352:     */   }
/*  353:     */   
/*  354:     */   public float getLayoutAlignmentX(Container parent)
/*  355:     */   {
/*  356: 739 */     checkParent(parent);
/*  357: 740 */     return 0.5F;
/*  358:     */   }
/*  359:     */   
/*  360:     */   public float getLayoutAlignmentY(Container parent)
/*  361:     */   {
/*  362: 756 */     checkParent(parent);
/*  363: 757 */     return 0.5F;
/*  364:     */   }
/*  365:     */   
/*  366:     */   public void invalidateLayout(Container parent)
/*  367:     */   {
/*  368: 769 */     checkParent(parent);
/*  369: 774 */     synchronized (parent.getTreeLock())
/*  370:     */     {
/*  371: 775 */       this.horizontalGroup.setSize(1, -2147483648, -2147483648);
/*  372: 776 */       this.verticalGroup.setSize(2, -2147483648, -2147483648);
/*  373: 777 */       Iterator cis = this.componentInfos.values().iterator();
/*  374: 778 */       while (cis.hasNext())
/*  375:     */       {
/*  376: 779 */         ComponentInfo ci = (ComponentInfo)cis.next();
/*  377: 780 */         ci.clear();
/*  378:     */       }
/*  379:     */     }
/*  380:     */   }
/*  381:     */   
/*  382:     */   private void resetAutopadding(Group group, int axis, int sizeType, int origin, int size)
/*  383:     */   {
/*  384: 787 */     group.resetAutopadding();
/*  385: 788 */     switch (sizeType)
/*  386:     */     {
/*  387:     */     case 0: 
/*  388: 790 */       size = group.getMinimumSize(axis);
/*  389: 791 */       break;
/*  390:     */     case 1: 
/*  391: 793 */       size = group.getPreferredSize(axis);
/*  392: 794 */       break;
/*  393:     */     case 2: 
/*  394: 796 */       size = group.getMaximumSize(axis);
/*  395:     */     }
/*  396: 799 */     group.setSize(axis, origin, size);
/*  397: 800 */     group.calculateAutopadding(axis);
/*  398:     */   }
/*  399:     */   
/*  400:     */   private void prepare(int sizeType)
/*  401:     */   {
/*  402: 804 */     prepare();
/*  403: 805 */     if ((getAutocreateGaps()) || (getAutocreateContainerGaps()) || (this.hasPreferredPaddingSprings))
/*  404:     */     {
/*  405: 807 */       resetAutopadding(this.horizontalGroup, 1, sizeType, 0, 0);
/*  406: 808 */       resetAutopadding(this.verticalGroup, 2, sizeType, 0, 0);
/*  407:     */     }
/*  408:     */   }
/*  409:     */   
/*  410:     */   private void prepare()
/*  411:     */   {
/*  412: 814 */     if (this.springsChanged)
/*  413:     */     {
/*  414: 815 */       registerComponents(this.horizontalGroup, 1);
/*  415: 816 */       registerComponents(this.verticalGroup, 2);
/*  416:     */     }
/*  417: 818 */     if ((this.springsChanged) || (hasVisibilityChanged()))
/*  418:     */     {
/*  419: 819 */       checkComponents();
/*  420: 820 */       this.horizontalGroup.removeAutopadding();
/*  421: 821 */       this.verticalGroup.removeAutopadding();
/*  422: 822 */       if (getAutocreateGaps()) {
/*  423: 823 */         adjustAutopadding(true);
/*  424: 824 */       } else if ((this.hasPreferredPaddingSprings) || (getAutocreateContainerGaps())) {
/*  425: 826 */         adjustAutopadding(false);
/*  426:     */       }
/*  427: 828 */       this.springsChanged = false;
/*  428:     */     }
/*  429:     */   }
/*  430:     */   
/*  431:     */   private boolean hasVisibilityChanged()
/*  432:     */   {
/*  433: 833 */     Iterator infos = this.componentInfos.values().iterator();
/*  434: 834 */     boolean visibilityChanged = false;
/*  435: 835 */     while (infos.hasNext())
/*  436:     */     {
/*  437: 836 */       ComponentInfo info = (ComponentInfo)infos.next();
/*  438: 837 */       if (info.updateVisibility()) {
/*  439: 838 */         visibilityChanged = true;
/*  440:     */       }
/*  441:     */     }
/*  442: 841 */     return visibilityChanged;
/*  443:     */   }
/*  444:     */   
/*  445:     */   private void checkComponents()
/*  446:     */   {
/*  447: 845 */     Iterator infos = this.componentInfos.values().iterator();
/*  448: 846 */     while (infos.hasNext())
/*  449:     */     {
/*  450: 847 */       ComponentInfo info = (ComponentInfo)infos.next();
/*  451: 848 */       if (info.horizontalSpring == null) {
/*  452: 849 */         throw new IllegalStateException(info.component + " is not attached to a horizontal group");
/*  453:     */       }
/*  454: 852 */       if (info.verticalSpring == null) {
/*  455: 853 */         throw new IllegalStateException(info.component + " is not attached to a vertical group");
/*  456:     */       }
/*  457:     */     }
/*  458:     */   }
/*  459:     */   
/*  460:     */   private void registerComponents(Group group, int axis)
/*  461:     */   {
/*  462: 860 */     List springs = group.springs;
/*  463: 861 */     for (int counter = springs.size() - 1; counter >= 0; counter--)
/*  464:     */     {
/*  465: 862 */       Spring spring = (Spring)springs.get(counter);
/*  466: 863 */       if ((spring instanceof ComponentSpring)) {
/*  467: 864 */         ((ComponentSpring)spring).installIfNecessary(axis);
/*  468: 865 */       } else if ((spring instanceof Group)) {
/*  469: 866 */         registerComponents((Group)spring, axis);
/*  470:     */       }
/*  471:     */     }
/*  472:     */   }
/*  473:     */   
/*  474:     */   private Dimension adjustSize(int width, int height)
/*  475:     */   {
/*  476: 872 */     Insets insets = this.host.getInsets();
/*  477: 873 */     return new Dimension(width + insets.left + insets.right, height + insets.top + insets.bottom);
/*  478:     */   }
/*  479:     */   
/*  480:     */   private void checkParent(Container parent)
/*  481:     */   {
/*  482: 878 */     if (parent != this.host) {
/*  483: 879 */       throw new IllegalArgumentException("GroupLayout can only be used with one Container at a time");
/*  484:     */     }
/*  485:     */   }
/*  486:     */   
/*  487:     */   private ComponentInfo getComponentInfo(Component component)
/*  488:     */   {
/*  489: 888 */     ComponentInfo info = (ComponentInfo)this.componentInfos.get(component);
/*  490: 889 */     if (info == null)
/*  491:     */     {
/*  492: 890 */       this.componentInfos.put(component, new ComponentInfo(component));
/*  493: 891 */       this.host.add(component);
/*  494:     */     }
/*  495: 893 */     return info;
/*  496:     */   }
/*  497:     */   
/*  498:     */   private void adjustAutopadding(boolean insert)
/*  499:     */   {
/*  500: 903 */     this.horizontalGroup.insertAutopadding(1, new ArrayList(1), new ArrayList(1), new ArrayList(1), new ArrayList(1), insert);
/*  501:     */     
/*  502: 905 */     this.verticalGroup.insertAutopadding(2, new ArrayList(1), new ArrayList(1), new ArrayList(1), new ArrayList(1), insert);
/*  503:     */   }
/*  504:     */   
/*  505:     */   private boolean areParallelSiblings(Component source, Component target, int axis)
/*  506:     */   {
/*  507: 915 */     ComponentInfo sourceInfo = getComponentInfo(source);
/*  508: 916 */     ComponentInfo targetInfo = getComponentInfo(target);
/*  509:     */     Spring targetSpring;
/*  510:     */     Spring sourceSpring;
/*  511:     */     Spring targetSpring;
/*  512: 919 */     if (axis == 1)
/*  513:     */     {
/*  514: 920 */       Spring sourceSpring = sourceInfo.horizontalSpring;
/*  515: 921 */       targetSpring = targetInfo.horizontalSpring;
/*  516:     */     }
/*  517:     */     else
/*  518:     */     {
/*  519: 923 */       sourceSpring = sourceInfo.verticalSpring;
/*  520: 924 */       targetSpring = targetInfo.verticalSpring;
/*  521:     */     }
/*  522: 926 */     List sourcePath = this.parallelList;
/*  523: 927 */     sourcePath.clear();
/*  524: 928 */     Spring spring = sourceSpring.getParent();
/*  525: 929 */     while (spring != null)
/*  526:     */     {
/*  527: 930 */       sourcePath.add(spring);
/*  528: 931 */       spring = spring.getParent();
/*  529:     */     }
/*  530: 933 */     spring = targetSpring.getParent();
/*  531: 934 */     while (spring != null)
/*  532:     */     {
/*  533: 935 */       if (sourcePath.contains(spring))
/*  534:     */       {
/*  535: 936 */         while (spring != null)
/*  536:     */         {
/*  537: 937 */           if ((spring instanceof ParallelGroup)) {
/*  538: 938 */             return true;
/*  539:     */           }
/*  540: 940 */           spring = spring.getParent();
/*  541:     */         }
/*  542: 942 */         return false;
/*  543:     */       }
/*  544: 944 */       spring = spring.getParent();
/*  545:     */     }
/*  546: 946 */     return false;
/*  547:     */   }
/*  548:     */   
/*  549:     */   abstract class Spring
/*  550:     */   {
/*  551:     */     private int size;
/*  552:     */     private int min;
/*  553:     */     private int max;
/*  554:     */     private int pref;
/*  555:     */     private Spring parent;
/*  556:     */     private int alignment;
/*  557:     */     
/*  558:     */     Spring()
/*  559:     */     {
/*  560: 968 */       this.min = (this.pref = this.max = -2147483648);
/*  561:     */     }
/*  562:     */     
/*  563:     */     abstract int getMinimumSize0(int paramInt);
/*  564:     */     
/*  565:     */     abstract int getPreferredSize0(int paramInt);
/*  566:     */     
/*  567:     */     abstract int getMaximumSize0(int paramInt);
/*  568:     */     
/*  569:     */     void setParent(Spring parent)
/*  570:     */     {
/*  571: 979 */       this.parent = parent;
/*  572:     */     }
/*  573:     */     
/*  574:     */     Spring getParent()
/*  575:     */     {
/*  576: 986 */       return this.parent;
/*  577:     */     }
/*  578:     */     
/*  579:     */     void setAlignment(int alignment)
/*  580:     */     {
/*  581: 992 */       GroupLayout.checkAlignment(alignment, false);
/*  582: 993 */       this.alignment = alignment;
/*  583:     */     }
/*  584:     */     
/*  585:     */     int getAlignment()
/*  586:     */     {
/*  587: 997 */       return this.alignment;
/*  588:     */     }
/*  589:     */     
/*  590:     */     final int getMinimumSize(int axis)
/*  591:     */     {
/*  592:1004 */       if (this.min == -2147483648) {
/*  593:1005 */         this.min = constrain(getMinimumSize0(axis));
/*  594:     */       }
/*  595:1007 */       return this.min;
/*  596:     */     }
/*  597:     */     
/*  598:     */     final int getPreferredSize(int axis)
/*  599:     */     {
/*  600:1014 */       if (this.pref == -2147483648) {
/*  601:1015 */         this.pref = constrain(getPreferredSize0(axis));
/*  602:     */       }
/*  603:1017 */       return this.pref;
/*  604:     */     }
/*  605:     */     
/*  606:     */     final int getMaximumSize(int axis)
/*  607:     */     {
/*  608:1024 */       if (this.max == -2147483648) {
/*  609:1025 */         this.max = constrain(getMaximumSize0(axis));
/*  610:     */       }
/*  611:1027 */       return this.max;
/*  612:     */     }
/*  613:     */     
/*  614:     */     void clear()
/*  615:     */     {
/*  616:1034 */       this.size = (this.min = this.pref = this.max = -2147483648);
/*  617:     */     }
/*  618:     */     
/*  619:     */     void setSize(int axis, int origin, int size)
/*  620:     */     {
/*  621:1047 */       this.size = size;
/*  622:1048 */       if (size == -2147483648) {
/*  623:1049 */         clear();
/*  624:     */       }
/*  625:     */     }
/*  626:     */     
/*  627:     */     int getSize()
/*  628:     */     {
/*  629:1057 */       return this.size;
/*  630:     */     }
/*  631:     */     
/*  632:     */     int constrain(int value)
/*  633:     */     {
/*  634:1061 */       return Math.min(value, 32767);
/*  635:     */     }
/*  636:     */   }
/*  637:     */   
/*  638:     */   public abstract class Group
/*  639:     */     extends GroupLayout.Spring
/*  640:     */   {
/*  641:     */     List springs;
/*  642:     */     
/*  643:     */     Group()
/*  644:     */     {
/*  645:1079 */       super();
/*  646:1080 */       this.springs = new ArrayList();
/*  647:     */     }
/*  648:     */     
/*  649:     */     int indexOf(GroupLayout.Spring spring)
/*  650:     */     {
/*  651:1084 */       return this.springs.indexOf(spring);
/*  652:     */     }
/*  653:     */     
/*  654:     */     Group addSpring(GroupLayout.Spring spring, int index)
/*  655:     */     {
/*  656:1092 */       this.springs.add(spring);
/*  657:1093 */       spring.setParent(this);
/*  658:1094 */       if (!(spring instanceof GroupLayout.AutopaddingSpring)) {
/*  659:1095 */         GroupLayout.this.springsChanged = true;
/*  660:     */       }
/*  661:1097 */       return this;
/*  662:     */     }
/*  663:     */     
/*  664:     */     Group addSpring(GroupLayout.Spring spring)
/*  665:     */     {
/*  666:1105 */       addSpring(spring, this.springs.size());
/*  667:1106 */       return this;
/*  668:     */     }
/*  669:     */     
/*  670:     */     void setParent(GroupLayout.Spring parent)
/*  671:     */     {
/*  672:1114 */       super.setParent(parent);
/*  673:1115 */       for (int counter = this.springs.size() - 1; counter >= 0; counter--) {
/*  674:1116 */         ((GroupLayout.Spring)this.springs.get(counter)).setParent(this);
/*  675:     */       }
/*  676:     */     }
/*  677:     */     
/*  678:     */     void setSize(int axis, int origin, int size)
/*  679:     */     {
/*  680:1121 */       super.setSize(axis, origin, size);
/*  681:1122 */       if (size == -2147483648) {
/*  682:1123 */         for (int counter = this.springs.size() - 1; counter >= 0; counter--) {
/*  683:1125 */           getSpring(counter).setSize(axis, origin, size);
/*  684:     */         }
/*  685:     */       } else {
/*  686:1128 */         setSize0(axis, origin, size);
/*  687:     */       }
/*  688:     */     }
/*  689:     */     
/*  690:     */     abstract void setSize0(int paramInt1, int paramInt2, int paramInt3);
/*  691:     */     
/*  692:     */     int getMinimumSize0(int axis)
/*  693:     */     {
/*  694:1139 */       return calculateSize(axis, 0);
/*  695:     */     }
/*  696:     */     
/*  697:     */     int getPreferredSize0(int axis)
/*  698:     */     {
/*  699:1143 */       return calculateSize(axis, 1);
/*  700:     */     }
/*  701:     */     
/*  702:     */     int getMaximumSize0(int axis)
/*  703:     */     {
/*  704:1147 */       return calculateSize(axis, 2);
/*  705:     */     }
/*  706:     */     
/*  707:     */     abstract int operator(int paramInt1, int paramInt2);
/*  708:     */     
/*  709:     */     int calculateSize(int axis, int type)
/*  710:     */     {
/*  711:1165 */       int count = this.springs.size();
/*  712:1166 */       if (count == 0) {
/*  713:1167 */         return 0;
/*  714:     */       }
/*  715:1169 */       if (count == 1) {
/*  716:1170 */         return getSize(getSpring(0), axis, type);
/*  717:     */       }
/*  718:1172 */       int size = constrain(operator(getSize(getSpring(0), axis, type), getSize(getSpring(1), axis, type)));
/*  719:1174 */       for (int counter = 2; counter < count; counter++) {
/*  720:1175 */         size = constrain(operator(size, getSize(getSpring(counter), axis, type)));
/*  721:     */       }
/*  722:1178 */       return size;
/*  723:     */     }
/*  724:     */     
/*  725:     */     GroupLayout.Spring getSpring(int index)
/*  726:     */     {
/*  727:1182 */       return (GroupLayout.Spring)this.springs.get(index);
/*  728:     */     }
/*  729:     */     
/*  730:     */     int getSize(GroupLayout.Spring spring, int axis, int type)
/*  731:     */     {
/*  732:1186 */       switch (type)
/*  733:     */       {
/*  734:     */       case 0: 
/*  735:1188 */         return spring.getMinimumSize(axis);
/*  736:     */       case 1: 
/*  737:1190 */         return spring.getPreferredSize(axis);
/*  738:     */       case 2: 
/*  739:1192 */         return spring.getMaximumSize(axis);
/*  740:     */       }
/*  741:1194 */       if (!$assertionsDisabled) {
/*  742:1194 */         throw new AssertionError();
/*  743:     */       }
/*  744:1195 */       return 0;
/*  745:     */     }
/*  746:     */     
/*  747:     */     abstract void insertAutopadding(int paramInt, List paramList1, List paramList2, List paramList3, List paramList4, boolean paramBoolean);
/*  748:     */     
/*  749:     */     void removeAutopadding()
/*  750:     */     {
/*  751:1224 */       for (int counter = this.springs.size() - 1; counter >= 0; counter--)
/*  752:     */       {
/*  753:1225 */         GroupLayout.Spring spring = (GroupLayout.Spring)this.springs.get(counter);
/*  754:1226 */         if ((spring instanceof GroupLayout.AutopaddingSpring))
/*  755:     */         {
/*  756:1227 */           if (((GroupLayout.AutopaddingSpring)spring).getUserCreated()) {
/*  757:1228 */             ((GroupLayout.AutopaddingSpring)spring).reset();
/*  758:     */           } else {
/*  759:1230 */             this.springs.remove(counter);
/*  760:     */           }
/*  761:     */         }
/*  762:1232 */         else if ((spring instanceof Group)) {
/*  763:1233 */           ((Group)spring).removeAutopadding();
/*  764:     */         }
/*  765:     */       }
/*  766:     */     }
/*  767:     */     
/*  768:     */     void resetAutopadding()
/*  769:     */     {
/*  770:1240 */       clear();
/*  771:1241 */       for (int counter = this.springs.size() - 1; counter >= 0; counter--)
/*  772:     */       {
/*  773:1242 */         GroupLayout.Spring spring = (GroupLayout.Spring)this.springs.get(counter);
/*  774:1243 */         if ((spring instanceof GroupLayout.AutopaddingSpring)) {
/*  775:1244 */           ((GroupLayout.AutopaddingSpring)spring).clear();
/*  776:1245 */         } else if ((spring instanceof Group)) {
/*  777:1246 */           ((Group)spring).resetAutopadding();
/*  778:     */         }
/*  779:     */       }
/*  780:     */     }
/*  781:     */     
/*  782:     */     void calculateAutopadding(int axis)
/*  783:     */     {
/*  784:1252 */       for (int counter = this.springs.size() - 1; counter >= 0; counter--)
/*  785:     */       {
/*  786:1253 */         GroupLayout.Spring spring = (GroupLayout.Spring)this.springs.get(counter);
/*  787:1254 */         if ((spring instanceof GroupLayout.AutopaddingSpring))
/*  788:     */         {
/*  789:1256 */           spring.clear();
/*  790:1257 */           ((GroupLayout.AutopaddingSpring)spring).calculatePadding(axis);
/*  791:     */         }
/*  792:1258 */         else if ((spring instanceof Group))
/*  793:     */         {
/*  794:1259 */           ((Group)spring).calculateAutopadding(axis);
/*  795:     */         }
/*  796:     */       }
/*  797:1263 */       clear();
/*  798:     */     }
/*  799:     */   }
/*  800:     */   
/*  801:     */   public class SequentialGroup
/*  802:     */     extends GroupLayout.Group
/*  803:     */   {
/*  804:     */     SequentialGroup()
/*  805:     */     {
/*  806:1276 */       super();
/*  807:     */     }
/*  808:     */     
/*  809:     */     public SequentialGroup add(GroupLayout.Group group)
/*  810:     */     {
/*  811:1287 */       return (SequentialGroup)addSpring(group);
/*  812:     */     }
/*  813:     */     
/*  814:     */     public SequentialGroup add(Component component)
/*  815:     */     {
/*  816:1298 */       return add(component, -1, -1, -1);
/*  817:     */     }
/*  818:     */     
/*  819:     */     public SequentialGroup add(Component component, int min, int pref, int max)
/*  820:     */     {
/*  821:1322 */       return (SequentialGroup)addSpring(new GroupLayout.ComponentSpring(GroupLayout.this, component, min, pref, max, null));
/*  822:     */     }
/*  823:     */     
/*  824:     */     public SequentialGroup add(int pref)
/*  825:     */     {
/*  826:1335 */       return add(pref, pref, pref);
/*  827:     */     }
/*  828:     */     
/*  829:     */     public SequentialGroup add(int min, int pref, int max)
/*  830:     */     {
/*  831:1349 */       return (SequentialGroup)addSpring(new GroupLayout.GapSpring(GroupLayout.this, min, pref, max));
/*  832:     */     }
/*  833:     */     
/*  834:     */     public SequentialGroup addPreferredGap(JComponent comp1, JComponent comp2, int type)
/*  835:     */     {
/*  836:1368 */       return addPreferredGap(comp1, comp2, type, false);
/*  837:     */     }
/*  838:     */     
/*  839:     */     public SequentialGroup addPreferredGap(JComponent comp1, JComponent comp2, int type, boolean canGrow)
/*  840:     */     {
/*  841:1389 */       if ((type != 0) && (type != 1) && (type != 3)) {
/*  842:1392 */         throw new IllegalArgumentException("Invalid type argument");
/*  843:     */       }
/*  844:1394 */       return (SequentialGroup)addSpring(new GroupLayout.PaddingSpring(GroupLayout.this, comp1, comp2, type, canGrow));
/*  845:     */     }
/*  846:     */     
/*  847:     */     public SequentialGroup addPreferredGap(int type)
/*  848:     */     {
/*  849:1414 */       return addPreferredGap(type, -1, -1);
/*  850:     */     }
/*  851:     */     
/*  852:     */     public SequentialGroup addPreferredGap(int type, int pref, int max)
/*  853:     */     {
/*  854:1444 */       if ((type != 0) && (type != 1)) {
/*  855:1445 */         throw new IllegalArgumentException("Padding type must be one of Padding.RELATED or Padding.UNRELATED");
/*  856:     */       }
/*  857:1448 */       if (((pref < 0) && (pref != -1)) || ((max < 0) && (max != -1) && (max != -2)) || ((pref >= 0) && (max >= 0) && (pref > max))) {
/*  858:1451 */         throw new IllegalArgumentException("Pref and max must be either DEFAULT_VALUE or >= 0 and pref <= max");
/*  859:     */       }
/*  860:1454 */       GroupLayout.this.hasPreferredPaddingSprings = true;
/*  861:1455 */       return (SequentialGroup)addSpring(new GroupLayout.AutopaddingSpring(GroupLayout.this, type, pref, max));
/*  862:     */     }
/*  863:     */     
/*  864:     */     public SequentialGroup addContainerGap()
/*  865:     */     {
/*  866:1468 */       return addContainerGap(-1, -1);
/*  867:     */     }
/*  868:     */     
/*  869:     */     public SequentialGroup addContainerGap(int pref, int max)
/*  870:     */     {
/*  871:1485 */       if (((pref < 0) && (pref != -1)) || ((max < 0) && (max != -1) && (max != -2)) || ((pref >= 0) && (max >= 0) && (pref > max))) {
/*  872:1488 */         throw new IllegalArgumentException("Pref and max must be either DEFAULT_VALUE or >= 0 and pref <= max");
/*  873:     */       }
/*  874:1491 */       GroupLayout.this.hasPreferredPaddingSprings = true;
/*  875:1492 */       return (SequentialGroup)addSpring(new GroupLayout.ContainerAutopaddingSpring(GroupLayout.this, pref, max));
/*  876:     */     }
/*  877:     */     
/*  878:     */     int operator(int a, int b)
/*  879:     */     {
/*  880:1497 */       return constrain(a) + constrain(b);
/*  881:     */     }
/*  882:     */     
/*  883:     */     void setSize0(int axis, int origin, int size)
/*  884:     */     {
/*  885:1501 */       int pref = getPreferredSize(axis);
/*  886:1502 */       if (size - pref == 0)
/*  887:     */       {
/*  888:1503 */         int counter = 0;
/*  889:1503 */         for (int max = this.springs.size(); counter < max; counter++)
/*  890:     */         {
/*  891:1505 */           GroupLayout.Spring spring = getSpring(counter);
/*  892:1506 */           int springPref = spring.getPreferredSize(axis);
/*  893:1507 */           spring.setSize(axis, origin, springPref);
/*  894:1508 */           origin += springPref;
/*  895:     */         }
/*  896:     */       }
/*  897:1510 */       else if (this.springs.size() == 1)
/*  898:     */       {
/*  899:1511 */         GroupLayout.Spring spring = getSpring(0);
/*  900:1512 */         spring.setSize(axis, origin, Math.min(size, spring.getMaximumSize(axis)));
/*  901:     */       }
/*  902:1514 */       else if (this.springs.size() > 1)
/*  903:     */       {
/*  904:1516 */         resize(axis, origin, size);
/*  905:     */       }
/*  906:     */     }
/*  907:     */     
/*  908:     */     private void resize(int axis, int origin, int size)
/*  909:     */     {
/*  910:1521 */       int delta = size - getPreferredSize(axis);
/*  911:1522 */       assert (delta != 0);
/*  912:1523 */       boolean useMin = delta < 0;
/*  913:1524 */       int springCount = this.springs.size();
/*  914:1525 */       if (useMin) {
/*  915:1526 */         delta *= -1;
/*  916:     */       }
/*  917:1530 */       List resizable = buildResizableList(axis, useMin);
/*  918:1531 */       int resizableCount = resizable.size();
/*  919:1533 */       if (resizableCount > 0)
/*  920:     */       {
/*  921:1534 */         int sDelta = delta / resizableCount;
/*  922:1535 */         int slop = delta - sDelta * resizableCount;
/*  923:1536 */         int[] sizes = new int[springCount];
/*  924:1537 */         int sign = useMin ? -1 : 1;
/*  925:1540 */         for (int counter = 0; counter < resizableCount; counter++)
/*  926:     */         {
/*  927:1541 */           GroupLayout.SpringDelta springDelta = (GroupLayout.SpringDelta)resizable.get(counter);
/*  928:1543 */           if (counter + 1 == resizableCount) {
/*  929:1544 */             sDelta += slop;
/*  930:     */           }
/*  931:1546 */           springDelta.delta = Math.min(sDelta, springDelta.delta);
/*  932:1547 */           delta -= springDelta.delta;
/*  933:1548 */           if ((springDelta.delta != sDelta) && (counter + 1 < resizableCount))
/*  934:     */           {
/*  935:1552 */             sDelta = delta / (resizableCount - counter - 1);
/*  936:1553 */             slop = delta - sDelta * (resizableCount - counter - 1);
/*  937:     */           }
/*  938:1555 */           GroupLayout.Spring spring = getSpring(springDelta.index);
/*  939:1556 */           sizes[springDelta.index] = (sign * springDelta.delta);
/*  940:     */         }
/*  941:1560 */         for (int counter = 0; counter < springCount; counter++)
/*  942:     */         {
/*  943:1561 */           GroupLayout.Spring spring = getSpring(counter);
/*  944:1562 */           int sSize = spring.getPreferredSize(axis) + sizes[counter];
/*  945:1563 */           spring.setSize(axis, origin, sSize);
/*  946:1564 */           origin += sSize;
/*  947:     */         }
/*  948:     */       }
/*  949:     */       else
/*  950:     */       {
/*  951:1569 */         for (int counter = 0; counter < springCount; counter++)
/*  952:     */         {
/*  953:1570 */           GroupLayout.Spring spring = getSpring(counter);
/*  954:     */           int sSize;
/*  955:     */           int sSize;
/*  956:1572 */           if (useMin) {
/*  957:1573 */             sSize = spring.getMinimumSize(axis);
/*  958:     */           } else {
/*  959:1575 */             sSize = spring.getMaximumSize(axis);
/*  960:     */           }
/*  961:1577 */           spring.setSize(axis, origin, sSize);
/*  962:1578 */           origin += sSize;
/*  963:     */         }
/*  964:     */       }
/*  965:     */     }
/*  966:     */     
/*  967:     */     private List buildResizableList(int axis, boolean useMin)
/*  968:     */     {
/*  969:1589 */       int size = this.springs.size();
/*  970:1590 */       List sorted = new ArrayList(size);
/*  971:1591 */       for (int counter = 0; counter < size; counter++)
/*  972:     */       {
/*  973:1592 */         GroupLayout.Spring spring = getSpring(counter);
/*  974:     */         int sDelta;
/*  975:     */         int sDelta;
/*  976:1594 */         if (useMin) {
/*  977:1595 */           sDelta = spring.getPreferredSize(axis) - spring.getMinimumSize(axis);
/*  978:     */         } else {
/*  979:1598 */           sDelta = spring.getMaximumSize(axis) - spring.getPreferredSize(axis);
/*  980:     */         }
/*  981:1601 */         if (sDelta > 0) {
/*  982:1602 */           sorted.add(new GroupLayout.SpringDelta(counter, sDelta));
/*  983:     */         }
/*  984:     */       }
/*  985:1605 */       Collections.sort(sorted);
/*  986:1606 */       return sorted;
/*  987:     */     }
/*  988:     */     
/*  989:     */     private GroupLayout.AutopaddingSpring getNextAutopadding(int index, boolean insert)
/*  990:     */     {
/*  991:1617 */       GroupLayout.Spring spring = getSpring(index);
/*  992:1618 */       if (((spring instanceof GroupLayout.AutopaddingSpring)) && (((GroupLayout.AutopaddingSpring)spring).getUserCreated())) {
/*  993:1620 */         return (GroupLayout.AutopaddingSpring)spring;
/*  994:     */       }
/*  995:1622 */       if (insert)
/*  996:     */       {
/*  997:1623 */         GroupLayout.AutopaddingSpring autoSpring = new GroupLayout.AutopaddingSpring(GroupLayout.this, null);
/*  998:1624 */         this.springs.add(index, autoSpring);
/*  999:1625 */         return autoSpring;
/* 1000:     */       }
/* 1001:1627 */       return null;
/* 1002:     */     }
/* 1003:     */     
/* 1004:     */     void insertAutopadding(int axis, List leadingPadding, List trailingPadding, List leading, List trailing, boolean insert)
/* 1005:     */     {
/* 1006:1633 */       List newLeadingPadding = new ArrayList(leadingPadding);
/* 1007:1634 */       List newTrailingPadding = new ArrayList(1);
/* 1008:1635 */       List newLeading = new ArrayList(leading);
/* 1009:1636 */       List newTrailing = null;
/* 1010:1637 */       for (int counter = 0; counter < this.springs.size(); counter++)
/* 1011:     */       {
/* 1012:1638 */         GroupLayout.Spring spring = getSpring(counter);
/* 1013:1639 */         if ((spring instanceof GroupLayout.AutopaddingSpring))
/* 1014:     */         {
/* 1015:1640 */           GroupLayout.AutopaddingSpring padding = (GroupLayout.AutopaddingSpring)spring;
/* 1016:1641 */           padding.setSources(newLeading);
/* 1017:1642 */           newLeading.clear();
/* 1018:1643 */           if (counter + 1 == this.springs.size())
/* 1019:     */           {
/* 1020:1644 */             if (!(padding instanceof GroupLayout.ContainerAutopaddingSpring)) {
/* 1021:1645 */               trailingPadding.add(padding);
/* 1022:     */             }
/* 1023:     */           }
/* 1024:     */           else
/* 1025:     */           {
/* 1026:1648 */             newLeadingPadding.clear();
/* 1027:1649 */             newLeadingPadding.add(padding);
/* 1028:     */           }
/* 1029:     */         }
/* 1030:1652 */         else if ((newLeading.size() > 0) && (insert))
/* 1031:     */         {
/* 1032:1653 */           GroupLayout.AutopaddingSpring padding = new GroupLayout.AutopaddingSpring(GroupLayout.this, null);
/* 1033:     */           
/* 1034:     */ 
/* 1035:1656 */           this.springs.add(counter--, padding);
/* 1036:     */         }
/* 1037:1659 */         else if ((spring instanceof GroupLayout.ComponentSpring))
/* 1038:     */         {
/* 1039:1660 */           GroupLayout.ComponentSpring cSpring = (GroupLayout.ComponentSpring)spring;
/* 1040:1661 */           if (GroupLayout.isVisible(cSpring.getComponent()))
/* 1041:     */           {
/* 1042:1664 */             for (int i = 0; i < newLeadingPadding.size(); i++) {
/* 1043:1665 */               ((GroupLayout.AutopaddingSpring)newLeadingPadding.get(i)).add(cSpring, axis);
/* 1044:     */             }
/* 1045:1668 */             newLeading.clear();
/* 1046:1669 */             newLeadingPadding.clear();
/* 1047:1670 */             if (counter + 1 == this.springs.size()) {
/* 1048:1671 */               trailing.add(cSpring);
/* 1049:     */             } else {
/* 1050:1673 */               newLeading.add(cSpring);
/* 1051:     */             }
/* 1052:     */           }
/* 1053:     */         }
/* 1054:1675 */         else if ((spring instanceof GroupLayout.Group))
/* 1055:     */         {
/* 1056:1676 */           if (newTrailing == null) {
/* 1057:1677 */             newTrailing = new ArrayList(1);
/* 1058:     */           } else {
/* 1059:1679 */             newTrailing.clear();
/* 1060:     */           }
/* 1061:1681 */           newTrailingPadding.clear();
/* 1062:1682 */           ((GroupLayout.Group)spring).insertAutopadding(axis, newLeadingPadding, newTrailingPadding, newLeading, newTrailing, insert);
/* 1063:     */           
/* 1064:     */ 
/* 1065:1685 */           newLeading.clear();
/* 1066:1686 */           newLeadingPadding.clear();
/* 1067:1687 */           if (counter + 1 == this.springs.size())
/* 1068:     */           {
/* 1069:1688 */             trailing.addAll(newTrailing);
/* 1070:1689 */             trailingPadding.addAll(newTrailingPadding);
/* 1071:     */           }
/* 1072:     */           else
/* 1073:     */           {
/* 1074:1691 */             newLeading.addAll(newTrailing);
/* 1075:1692 */             newLeadingPadding.addAll(newTrailingPadding);
/* 1076:     */           }
/* 1077:     */         }
/* 1078:     */         else
/* 1079:     */         {
/* 1080:1695 */           newLeadingPadding.clear();
/* 1081:1696 */           newLeading.clear();
/* 1082:     */         }
/* 1083:     */       }
/* 1084:     */     }
/* 1085:     */   }
/* 1086:     */   
/* 1087:     */   private static class SpringDelta
/* 1088:     */     implements Comparable
/* 1089:     */   {
/* 1090:     */     public int index;
/* 1091:     */     public int delta;
/* 1092:     */     
/* 1093:     */     public SpringDelta(int index, int delta)
/* 1094:     */     {
/* 1095:1714 */       this.index = index;
/* 1096:1715 */       this.delta = delta;
/* 1097:     */     }
/* 1098:     */     
/* 1099:     */     public int compareTo(Object o)
/* 1100:     */     {
/* 1101:1719 */       return this.delta - ((SpringDelta)o).delta;
/* 1102:     */     }
/* 1103:     */     
/* 1104:     */     public String toString()
/* 1105:     */     {
/* 1106:1723 */       return super.toString() + "[index=" + this.index + ", delta=" + this.delta + "]";
/* 1107:     */     }
/* 1108:     */   }
/* 1109:     */   
/* 1110:     */   public class ParallelGroup
/* 1111:     */     extends GroupLayout.Group
/* 1112:     */   {
/* 1113:     */     private int childAlignment;
/* 1114:     */     private boolean resizable;
/* 1115:     */     
/* 1116:     */     ParallelGroup(int childAlignment, boolean resizable)
/* 1117:     */     {
/* 1118:1743 */       super();
/* 1119:1744 */       GroupLayout.checkAlignment(childAlignment, true);
/* 1120:1745 */       this.childAlignment = childAlignment;
/* 1121:1746 */       this.resizable = resizable;
/* 1122:     */     }
/* 1123:     */     
/* 1124:     */     public ParallelGroup add(GroupLayout.Group group)
/* 1125:     */     {
/* 1126:1756 */       return (ParallelGroup)addSpring(group);
/* 1127:     */     }
/* 1128:     */     
/* 1129:     */     public ParallelGroup add(Component component)
/* 1130:     */     {
/* 1131:1767 */       return add(component, -1, -1, -1);
/* 1132:     */     }
/* 1133:     */     
/* 1134:     */     public ParallelGroup add(Component component, int min, int pref, int max)
/* 1135:     */     {
/* 1136:1791 */       return (ParallelGroup)addSpring(new GroupLayout.ComponentSpring(GroupLayout.this, component, min, pref, max, null));
/* 1137:     */     }
/* 1138:     */     
/* 1139:     */     public ParallelGroup add(int pref)
/* 1140:     */     {
/* 1141:1804 */       return add(pref, pref, pref);
/* 1142:     */     }
/* 1143:     */     
/* 1144:     */     public ParallelGroup add(int min, int pref, int max)
/* 1145:     */     {
/* 1146:1818 */       return (ParallelGroup)addSpring(new GroupLayout.GapSpring(GroupLayout.this, min, pref, max));
/* 1147:     */     }
/* 1148:     */     
/* 1149:     */     public ParallelGroup add(int alignment, GroupLayout.Group group)
/* 1150:     */     {
/* 1151:1832 */       group.setAlignment(alignment);
/* 1152:1833 */       return (ParallelGroup)addSpring(group);
/* 1153:     */     }
/* 1154:     */     
/* 1155:     */     public ParallelGroup add(int alignment, Component component)
/* 1156:     */     {
/* 1157:1848 */       return add(alignment, component, -1, -1, -1);
/* 1158:     */     }
/* 1159:     */     
/* 1160:     */     public ParallelGroup add(int alignment, Component component, int min, int pref, int max)
/* 1161:     */     {
/* 1162:1874 */       GroupLayout.ComponentSpring spring = new GroupLayout.ComponentSpring(GroupLayout.this, component, min, pref, max, null);
/* 1163:     */       
/* 1164:1876 */       spring.setAlignment(alignment);
/* 1165:1877 */       return (ParallelGroup)addSpring(spring);
/* 1166:     */     }
/* 1167:     */     
/* 1168:     */     boolean isResizable()
/* 1169:     */     {
/* 1170:1881 */       return this.resizable;
/* 1171:     */     }
/* 1172:     */     
/* 1173:     */     int operator(int a, int b)
/* 1174:     */     {
/* 1175:1885 */       return Math.max(a, b);
/* 1176:     */     }
/* 1177:     */     
/* 1178:     */     int getMinimumSize0(int axis)
/* 1179:     */     {
/* 1180:1889 */       if (!isResizable()) {
/* 1181:1890 */         return getPreferredSize(axis);
/* 1182:     */       }
/* 1183:1892 */       return super.getMinimumSize0(axis);
/* 1184:     */     }
/* 1185:     */     
/* 1186:     */     int getMaximumSize0(int axis)
/* 1187:     */     {
/* 1188:1896 */       if (!isResizable()) {
/* 1189:1897 */         return getPreferredSize(axis);
/* 1190:     */       }
/* 1191:1899 */       return super.getMaximumSize0(axis);
/* 1192:     */     }
/* 1193:     */     
/* 1194:     */     void setSize0(int axis, int origin, int size)
/* 1195:     */     {
/* 1196:1903 */       int alignment = this.childAlignment;
/* 1197:1904 */       if (alignment == 3) {
/* 1198:1907 */         alignment = 1;
/* 1199:     */       }
/* 1200:1909 */       int counter = 0;
/* 1201:1909 */       for (int max = this.springs.size(); counter < max; counter++)
/* 1202:     */       {
/* 1203:1911 */         GroupLayout.Spring spring = getSpring(counter);
/* 1204:1912 */         int sAlignment = spring.getAlignment();
/* 1205:1913 */         int springSize = Math.min(size, spring.getMaximumSize(axis));
/* 1206:1915 */         if (sAlignment == 0) {
/* 1207:1916 */           sAlignment = alignment;
/* 1208:     */         }
/* 1209:1918 */         switch (sAlignment)
/* 1210:     */         {
/* 1211:     */         case 2: 
/* 1212:1920 */           spring.setSize(axis, origin + size - springSize, springSize);
/* 1213:     */           
/* 1214:1922 */           break;
/* 1215:     */         case 3: 
/* 1216:1924 */           spring.setSize(axis, origin + (size - springSize) / 2, springSize);
/* 1217:     */           
/* 1218:1926 */           break;
/* 1219:     */         default: 
/* 1220:1928 */           spring.setSize(axis, origin, springSize);
/* 1221:     */         }
/* 1222:     */       }
/* 1223:     */     }
/* 1224:     */     
/* 1225:     */     void insertAutopadding(int axis, List leadingPadding, List trailingPadding, List leading, List trailing, boolean insert)
/* 1226:     */     {
/* 1227:1937 */       for (int counter = 0; counter < this.springs.size(); counter++)
/* 1228:     */       {
/* 1229:1938 */         GroupLayout.Spring spring = getSpring(counter);
/* 1230:1939 */         if ((spring instanceof GroupLayout.ComponentSpring))
/* 1231:     */         {
/* 1232:1940 */           for (int i = 0; i < leadingPadding.size(); i++) {
/* 1233:1941 */             ((GroupLayout.AutopaddingSpring)leadingPadding.get(i)).add((GroupLayout.ComponentSpring)spring, axis);
/* 1234:     */           }
/* 1235:1944 */           trailing.add(spring);
/* 1236:     */         }
/* 1237:1945 */         else if ((spring instanceof GroupLayout.Group))
/* 1238:     */         {
/* 1239:1946 */           ((GroupLayout.Group)spring).insertAutopadding(axis, leadingPadding, trailingPadding, leading, trailing, insert);
/* 1240:     */         }
/* 1241:1948 */         else if ((spring instanceof GroupLayout.AutopaddingSpring))
/* 1242:     */         {
/* 1243:1949 */           trailingPadding.add(spring);
/* 1244:     */         }
/* 1245:     */       }
/* 1246:     */     }
/* 1247:     */   }
/* 1248:     */   
/* 1249:     */   private class BaselineGroup
/* 1250:     */     extends GroupLayout.ParallelGroup
/* 1251:     */   {
/* 1252:     */     private boolean allSpringsHaveBaseline;
/* 1253:     */     private int prefAscent;
/* 1254:     */     private int prefDescent;
/* 1255:     */     
/* 1256:     */     BaselineGroup(boolean resizable)
/* 1257:     */     {
/* 1258:1972 */       super(1, resizable);
/* 1259:1973 */       this.prefAscent = (this.prefDescent = -1);
/* 1260:     */     }
/* 1261:     */     
/* 1262:     */     void setSize(int axis, int origin, int size)
/* 1263:     */     {
/* 1264:1977 */       if (size == -2147483648) {
/* 1265:1978 */         this.prefAscent = (this.prefDescent = -1);
/* 1266:     */       }
/* 1267:1980 */       super.setSize(axis, origin, size);
/* 1268:     */     }
/* 1269:     */     
/* 1270:     */     void setSize0(int axis, int origin, int size)
/* 1271:     */     {
/* 1272:1984 */       if ((axis == 1) || (this.prefAscent == -1)) {
/* 1273:1985 */         super.setSize0(axis, origin, size);
/* 1274:     */       } else {
/* 1275:1988 */         baselineLayout(origin, size);
/* 1276:     */       }
/* 1277:     */     }
/* 1278:     */     
/* 1279:     */     int calculateSize(int axis, int type)
/* 1280:     */     {
/* 1281:1993 */       if ((this.springs.size() < 2) || (axis != 2)) {
/* 1282:1994 */         return super.calculateSize(axis, type);
/* 1283:     */       }
/* 1284:1996 */       if (this.prefAscent == -1) {
/* 1285:1997 */         calculateBaseline();
/* 1286:     */       }
/* 1287:1999 */       if (this.allSpringsHaveBaseline) {
/* 1288:2000 */         return this.prefAscent + this.prefDescent;
/* 1289:     */       }
/* 1290:2002 */       return Math.max(this.prefAscent + this.prefDescent, super.calculateSize(axis, type));
/* 1291:     */     }
/* 1292:     */     
/* 1293:     */     private void calculateBaseline()
/* 1294:     */     {
/* 1295:2014 */       this.prefAscent = 0;
/* 1296:2015 */       this.prefDescent = 0;
/* 1297:2016 */       this.allSpringsHaveBaseline = true;
/* 1298:2017 */       for (int counter = this.springs.size() - 1; counter >= 0; counter--)
/* 1299:     */       {
/* 1300:2018 */         GroupLayout.Spring spring = getSpring(counter);
/* 1301:2019 */         int baseline = -1;
/* 1302:2020 */         if ((spring instanceof GroupLayout.ComponentSpring))
/* 1303:     */         {
/* 1304:2021 */           baseline = ((GroupLayout.ComponentSpring)spring).getBaseline();
/* 1305:2022 */           if (baseline >= 0)
/* 1306:     */           {
/* 1307:2023 */             this.prefAscent = Math.max(this.prefAscent, baseline);
/* 1308:2024 */             this.prefDescent = Math.max(this.prefDescent, spring.getPreferredSize(2) - baseline);
/* 1309:     */           }
/* 1310:     */         }
/* 1311:2028 */         if (baseline < 0) {
/* 1312:2029 */           this.allSpringsHaveBaseline = false;
/* 1313:     */         }
/* 1314:     */       }
/* 1315:     */     }
/* 1316:     */     
/* 1317:     */     private void baselineLayout(int origin, int size)
/* 1318:     */     {
/* 1319:2039 */       int counter = 0;
/* 1320:2039 */       for (int max = this.springs.size(); counter < max; counter++)
/* 1321:     */       {
/* 1322:2041 */         GroupLayout.Spring spring = getSpring(counter);
/* 1323:2042 */         int baseline = -1;
/* 1324:2043 */         if ((spring instanceof GroupLayout.ComponentSpring))
/* 1325:     */         {
/* 1326:2044 */           baseline = ((GroupLayout.ComponentSpring)spring).getBaseline();
/* 1327:2045 */           if (baseline >= 0) {
/* 1328:2046 */             spring.setSize(2, origin + this.prefAscent - baseline, spring.getPreferredSize(2));
/* 1329:     */           }
/* 1330:     */         }
/* 1331:2050 */         if (baseline < 0)
/* 1332:     */         {
/* 1333:2053 */           int sSize = Math.min(spring.getMaximumSize(2), size);
/* 1334:2054 */           spring.setSize(2, origin + (size - sSize) / 2, sSize);
/* 1335:     */         }
/* 1336:     */       }
/* 1337:     */     }
/* 1338:     */   }
/* 1339:     */   
/* 1340:     */   class ComponentSpring
/* 1341:     */     extends GroupLayout.Spring
/* 1342:     */   {
/* 1343:     */     private Component component;
/* 1344:     */     private int origin;
/* 1345:     */     private int min;
/* 1346:     */     private int pref;
/* 1347:     */     private int max;
/* 1348:     */     
/* 1349:     */     ComponentSpring(Component x1, int x2, int x3, int x4, GroupLayout.1 x5)
/* 1350:     */     {
/* 1351:2073 */       this(x1, x2, x3, x4);
/* 1352:     */     }
/* 1353:     */     
/* 1354:2082 */     private int baseline = -1;
/* 1355:     */     private boolean installed;
/* 1356:     */     
/* 1357:     */     private ComponentSpring(Component component, int min, int pref, int max)
/* 1358:     */     {
/* 1359:2088 */       super();
/* 1360:2089 */       this.component = component;
/* 1361:     */       
/* 1362:2091 */       GroupLayout.checkSize(min, pref, max, true);
/* 1363:     */       
/* 1364:2093 */       this.min = min;
/* 1365:2094 */       this.max = max;
/* 1366:2095 */       this.pref = pref;
/* 1367:     */       
/* 1368:2097 */       GroupLayout.this.getComponentInfo(component);
/* 1369:     */     }
/* 1370:     */     
/* 1371:     */     int getMinimumSize0(int axis)
/* 1372:     */     {
/* 1373:2101 */       if (isLinked(axis)) {
/* 1374:2102 */         return getLinkSize(axis, 0);
/* 1375:     */       }
/* 1376:2104 */       return getMinimumSize1(axis);
/* 1377:     */     }
/* 1378:     */     
/* 1379:     */     int getMinimumSize1(int axis)
/* 1380:     */     {
/* 1381:2108 */       if (!GroupLayout.isVisible(this.component)) {
/* 1382:2109 */         return 0;
/* 1383:     */       }
/* 1384:2111 */       if (this.min >= 0) {
/* 1385:2112 */         return this.min;
/* 1386:     */       }
/* 1387:2114 */       if (this.min == -2) {
/* 1388:2115 */         return getPreferredSize1(axis);
/* 1389:     */       }
/* 1390:2117 */       assert (this.min == -1);
/* 1391:2118 */       return getSizeAlongAxis(axis, this.component.getMinimumSize());
/* 1392:     */     }
/* 1393:     */     
/* 1394:     */     int getPreferredSize0(int axis)
/* 1395:     */     {
/* 1396:2122 */       if (isLinked(axis)) {
/* 1397:2123 */         return getLinkSize(axis, 1);
/* 1398:     */       }
/* 1399:2125 */       return Math.max(getMinimumSize(axis), getPreferredSize1(axis));
/* 1400:     */     }
/* 1401:     */     
/* 1402:     */     int getPreferredSize1(int axis)
/* 1403:     */     {
/* 1404:2129 */       if (!GroupLayout.isVisible(this.component)) {
/* 1405:2130 */         return 0;
/* 1406:     */       }
/* 1407:2132 */       if (this.pref >= 0) {
/* 1408:2133 */         return this.pref;
/* 1409:     */       }
/* 1410:2135 */       return getSizeAlongAxis(axis, this.component.getPreferredSize());
/* 1411:     */     }
/* 1412:     */     
/* 1413:     */     int getMaximumSize0(int axis)
/* 1414:     */     {
/* 1415:2139 */       if (!GroupLayout.isVisible(this.component)) {
/* 1416:2140 */         return 0;
/* 1417:     */       }
/* 1418:2142 */       if (isLinked(axis)) {
/* 1419:2143 */         return getLinkSize(axis, 2);
/* 1420:     */       }
/* 1421:2145 */       return Math.max(getMinimumSize(axis), getMaximumSize1(axis));
/* 1422:     */     }
/* 1423:     */     
/* 1424:     */     int getMaximumSize1(int axis)
/* 1425:     */     {
/* 1426:2149 */       if (this.max >= 0) {
/* 1427:2150 */         return this.max;
/* 1428:     */       }
/* 1429:2152 */       if (this.max == -2) {
/* 1430:2153 */         return getPreferredSize1(axis);
/* 1431:     */       }
/* 1432:2155 */       assert (this.max == -1);
/* 1433:2156 */       return getSizeAlongAxis(axis, this.component.getMaximumSize());
/* 1434:     */     }
/* 1435:     */     
/* 1436:     */     private int getSizeAlongAxis(int axis, Dimension size)
/* 1437:     */     {
/* 1438:2160 */       return axis == 1 ? size.width : size.height;
/* 1439:     */     }
/* 1440:     */     
/* 1441:     */     private int getLinkSize(int axis, int type)
/* 1442:     */     {
/* 1443:2164 */       if (!GroupLayout.isVisible(this.component)) {
/* 1444:2165 */         return 0;
/* 1445:     */       }
/* 1446:2167 */       GroupLayout.ComponentInfo ci = GroupLayout.this.getComponentInfo(this.component);
/* 1447:2168 */       return ci.getLinkSize(axis, type);
/* 1448:     */     }
/* 1449:     */     
/* 1450:     */     void setSize(int axis, int origin, int size)
/* 1451:     */     {
/* 1452:2172 */       super.setSize(axis, origin, size);
/* 1453:2173 */       this.origin = origin;
/* 1454:2174 */       if (size == -2147483648) {
/* 1455:2175 */         this.baseline = -1;
/* 1456:     */       }
/* 1457:     */     }
/* 1458:     */     
/* 1459:     */     int getOrigin()
/* 1460:     */     {
/* 1461:2180 */       return this.origin;
/* 1462:     */     }
/* 1463:     */     
/* 1464:     */     void setComponent(Component component)
/* 1465:     */     {
/* 1466:2184 */       this.component = component;
/* 1467:     */     }
/* 1468:     */     
/* 1469:     */     Component getComponent()
/* 1470:     */     {
/* 1471:2188 */       return this.component;
/* 1472:     */     }
/* 1473:     */     
/* 1474:     */     int getBaseline()
/* 1475:     */     {
/* 1476:2192 */       if ((this.baseline == -1) && ((this.component instanceof JComponent)))
/* 1477:     */       {
/* 1478:2193 */         GroupLayout.Spring horizontalSpring = GroupLayout.this.getComponentInfo(this.component).horizontalSpring;
/* 1479:     */         int width;
/* 1480:     */         int width;
/* 1481:2196 */         if (horizontalSpring != null) {
/* 1482:2197 */           width = horizontalSpring.getSize();
/* 1483:     */         } else {
/* 1484:2200 */           width = this.component.getPreferredSize().width;
/* 1485:     */         }
/* 1486:2202 */         this.baseline = Baseline.getBaseline((JComponent)this.component, width, getPreferredSize(2));
/* 1487:     */       }
/* 1488:2205 */       return this.baseline;
/* 1489:     */     }
/* 1490:     */     
/* 1491:     */     private boolean isLinked(int axis)
/* 1492:     */     {
/* 1493:2209 */       return GroupLayout.this.getComponentInfo(this.component).isLinked(axis);
/* 1494:     */     }
/* 1495:     */     
/* 1496:     */     void installIfNecessary(int axis)
/* 1497:     */     {
/* 1498:2213 */       if (!this.installed)
/* 1499:     */       {
/* 1500:2214 */         this.installed = true;
/* 1501:2215 */         if (axis == 1) {
/* 1502:2216 */           GroupLayout.this.getComponentInfo(this.component).horizontalSpring = this;
/* 1503:     */         } else {
/* 1504:2218 */           GroupLayout.this.getComponentInfo(this.component).verticalSpring = this;
/* 1505:     */         }
/* 1506:     */       }
/* 1507:     */     }
/* 1508:     */   }
/* 1509:     */   
/* 1510:     */   class PaddingSpring
/* 1511:     */     extends GroupLayout.Spring
/* 1512:     */   {
/* 1513:     */     private JComponent source;
/* 1514:     */     private JComponent target;
/* 1515:     */     private int type;
/* 1516:     */     private boolean canGrow;
/* 1517:     */     
/* 1518:     */     PaddingSpring(JComponent source, JComponent target, int type, boolean canGrow)
/* 1519:     */     {
/* 1520:2235 */       super();
/* 1521:2236 */       this.source = source;
/* 1522:2237 */       this.target = target;
/* 1523:2238 */       this.type = type;
/* 1524:2239 */       this.canGrow = canGrow;
/* 1525:     */     }
/* 1526:     */     
/* 1527:     */     int getMinimumSize0(int axis)
/* 1528:     */     {
/* 1529:2243 */       return getPadding(axis);
/* 1530:     */     }
/* 1531:     */     
/* 1532:     */     int getPreferredSize0(int axis)
/* 1533:     */     {
/* 1534:2247 */       return getPadding(axis);
/* 1535:     */     }
/* 1536:     */     
/* 1537:     */     int getMaximumSize0(int axis)
/* 1538:     */     {
/* 1539:2251 */       if (this.canGrow) {
/* 1540:2252 */         return 32767;
/* 1541:     */       }
/* 1542:2254 */       return getPadding(axis);
/* 1543:     */     }
/* 1544:     */     
/* 1545:     */     private int getPadding(int axis)
/* 1546:     */     {
/* 1547:     */       int position;
/* 1548:     */       int position;
/* 1549:2259 */       if (axis == 1) {
/* 1550:2260 */         position = 3;
/* 1551:     */       } else {
/* 1552:2262 */         position = 5;
/* 1553:     */       }
/* 1554:2264 */       return LayoutStyle.getSharedInstance().getPreferredGap(this.source, this.target, this.type, position, GroupLayout.this.host);
/* 1555:     */     }
/* 1556:     */   }
/* 1557:     */   
/* 1558:     */   class GapSpring
/* 1559:     */     extends GroupLayout.Spring
/* 1560:     */   {
/* 1561:     */     private int min;
/* 1562:     */     private int pref;
/* 1563:     */     private int max;
/* 1564:     */     
/* 1565:     */     GapSpring(int min, int pref, int max)
/* 1566:     */     {
/* 1567:2278 */       super();
/* 1568:2279 */       GroupLayout.checkSize(min, pref, max, false);
/* 1569:2280 */       this.min = min;
/* 1570:2281 */       this.pref = pref;
/* 1571:2282 */       this.max = max;
/* 1572:     */     }
/* 1573:     */     
/* 1574:     */     int getMinimumSize0(int axis)
/* 1575:     */     {
/* 1576:2286 */       if (this.min == -2) {
/* 1577:2287 */         return getPreferredSize(axis);
/* 1578:     */       }
/* 1579:2289 */       return this.min;
/* 1580:     */     }
/* 1581:     */     
/* 1582:     */     int getPreferredSize0(int axis)
/* 1583:     */     {
/* 1584:2293 */       return this.pref;
/* 1585:     */     }
/* 1586:     */     
/* 1587:     */     int getMaximumSize0(int axis)
/* 1588:     */     {
/* 1589:2297 */       if (this.max == -2) {
/* 1590:2298 */         return getPreferredSize(axis);
/* 1591:     */       }
/* 1592:2300 */       return this.max;
/* 1593:     */     }
/* 1594:     */   }
/* 1595:     */   
/* 1596:     */   private class AutopaddingSpring
/* 1597:     */     extends GroupLayout.Spring
/* 1598:     */   {
/* 1599:     */     List sources;
/* 1600:     */     GroupLayout.ComponentSpring source;
/* 1601:     */     private List matches;
/* 1602:     */     int size;
/* 1603:     */     int lastSize;
/* 1604:     */     private int pref;
/* 1605:     */     private int max;
/* 1606:     */     private int type;
/* 1607:     */     private boolean userCreated;
/* 1608:     */     
/* 1609:     */     AutopaddingSpring(GroupLayout.1 x1)
/* 1610:     */     {
/* 1611:2311 */       this();
/* 1612:     */     }
/* 1613:     */     
/* 1614:     */     private AutopaddingSpring()
/* 1615:     */     {
/* 1616:2322 */       super();
/* 1617:2323 */       this.pref = -2;
/* 1618:2324 */       this.max = -2;
/* 1619:2325 */       this.type = 0;
/* 1620:     */     }
/* 1621:     */     
/* 1622:     */     AutopaddingSpring(int pref, int max)
/* 1623:     */     {
/* 1624:2328 */       super();
/* 1625:2329 */       this.pref = pref;
/* 1626:2330 */       this.max = max;
/* 1627:     */     }
/* 1628:     */     
/* 1629:     */     AutopaddingSpring(int type, int pref, int max)
/* 1630:     */     {
/* 1631:2333 */       super();
/* 1632:2334 */       this.type = type;
/* 1633:2335 */       this.pref = pref;
/* 1634:2336 */       this.max = max;
/* 1635:2337 */       this.userCreated = true;
/* 1636:     */     }
/* 1637:     */     
/* 1638:     */     public void setSource(GroupLayout.ComponentSpring source)
/* 1639:     */     {
/* 1640:2341 */       this.source = source;
/* 1641:     */     }
/* 1642:     */     
/* 1643:     */     public void setSources(List sources)
/* 1644:     */     {
/* 1645:2345 */       this.sources = new ArrayList(sources.size());
/* 1646:2346 */       this.sources.addAll(sources);
/* 1647:     */     }
/* 1648:     */     
/* 1649:     */     public void setUserCreated(boolean userCreated)
/* 1650:     */     {
/* 1651:2350 */       this.userCreated = userCreated;
/* 1652:     */     }
/* 1653:     */     
/* 1654:     */     public boolean getUserCreated()
/* 1655:     */     {
/* 1656:2354 */       return this.userCreated;
/* 1657:     */     }
/* 1658:     */     
/* 1659:     */     void clear()
/* 1660:     */     {
/* 1661:2358 */       this.lastSize = getSize();
/* 1662:2359 */       super.clear();
/* 1663:2360 */       this.size = 0;
/* 1664:     */     }
/* 1665:     */     
/* 1666:     */     public void reset()
/* 1667:     */     {
/* 1668:2364 */       this.size = 0;
/* 1669:2365 */       this.sources = null;
/* 1670:2366 */       this.source = null;
/* 1671:2367 */       this.matches = null;
/* 1672:     */     }
/* 1673:     */     
/* 1674:     */     public void calculatePadding(int axis)
/* 1675:     */     {
/* 1676:2371 */       this.size = -2147483648;
/* 1677:2372 */       int maxPadding = -2147483648;
/* 1678:2373 */       if (this.matches != null)
/* 1679:     */       {
/* 1680:2374 */         LayoutStyle p = LayoutStyle.getSharedInstance();
/* 1681:     */         
/* 1682:2376 */         int position = axis == 1 ? 3 : 5;
/* 1683:2378 */         for (int i = this.matches.size() - 1; i >= 0; i--)
/* 1684:     */         {
/* 1685:2379 */           GroupLayout.AutopaddingMatch match = (GroupLayout.AutopaddingMatch)this.matches.get(i);
/* 1686:2380 */           maxPadding = Math.max(maxPadding, calculatePadding(p, position, match.source, match.target));
/* 1687:     */         }
/* 1688:     */       }
/* 1689:2385 */       if (this.size == -2147483648) {
/* 1690:2386 */         this.size = 0;
/* 1691:     */       }
/* 1692:2388 */       if (maxPadding == -2147483648) {
/* 1693:2389 */         maxPadding = 0;
/* 1694:     */       }
/* 1695:2391 */       if (this.lastSize != -2147483648) {
/* 1696:2392 */         this.size += Math.min(maxPadding, this.lastSize);
/* 1697:     */       }
/* 1698:     */     }
/* 1699:     */     
/* 1700:     */     private int calculatePadding(LayoutStyle p, int position, GroupLayout.ComponentSpring source, GroupLayout.ComponentSpring target)
/* 1701:     */     {
/* 1702:2399 */       int delta = target.getOrigin() - (source.getOrigin() + source.getSize());
/* 1703:2401 */       if (delta >= 0)
/* 1704:     */       {
/* 1705:     */         int padding;
/* 1706:     */         int padding;
/* 1707:2403 */         if (((source.getComponent() instanceof JComponent)) && ((target.getComponent() instanceof JComponent))) {
/* 1708:2405 */           padding = p.getPreferredGap((JComponent)source.getComponent(), (JComponent)target.getComponent(), this.type, position, GroupLayout.this.host);
/* 1709:     */         } else {
/* 1710:2408 */           padding = 10;
/* 1711:     */         }
/* 1712:2410 */         if (padding > delta) {
/* 1713:2411 */           this.size = Math.max(this.size, padding - delta);
/* 1714:     */         }
/* 1715:2413 */         return padding;
/* 1716:     */       }
/* 1717:2415 */       return 0;
/* 1718:     */     }
/* 1719:     */     
/* 1720:     */     public void add(GroupLayout.ComponentSpring spring, int axis)
/* 1721:     */     {
/* 1722:2419 */       int oAxis = axis == 1 ? 2 : 1;
/* 1723:2420 */       if (this.source != null)
/* 1724:     */       {
/* 1725:2421 */         if (GroupLayout.this.areParallelSiblings(this.source.getComponent(), spring.getComponent(), oAxis)) {
/* 1726:2423 */           addMatch(this.source, spring);
/* 1727:     */         }
/* 1728:     */       }
/* 1729:     */       else
/* 1730:     */       {
/* 1731:2426 */         Component component = spring.getComponent();
/* 1732:2427 */         for (int counter = this.sources.size() - 1; counter >= 0; counter--)
/* 1733:     */         {
/* 1734:2428 */           GroupLayout.ComponentSpring source = (GroupLayout.ComponentSpring)this.sources.get(counter);
/* 1735:2430 */           if (GroupLayout.this.areParallelSiblings(source.getComponent(), component, oAxis)) {
/* 1736:2432 */             addMatch(source, spring);
/* 1737:     */           }
/* 1738:     */         }
/* 1739:     */       }
/* 1740:     */     }
/* 1741:     */     
/* 1742:     */     private void addMatch(GroupLayout.ComponentSpring source, GroupLayout.ComponentSpring target)
/* 1743:     */     {
/* 1744:2439 */       if (this.matches == null) {
/* 1745:2440 */         this.matches = new ArrayList(1);
/* 1746:     */       }
/* 1747:2442 */       this.matches.add(new GroupLayout.AutopaddingMatch(source, target));
/* 1748:     */     }
/* 1749:     */     
/* 1750:     */     int getMinimumSize0(int axis)
/* 1751:     */     {
/* 1752:2446 */       return this.size;
/* 1753:     */     }
/* 1754:     */     
/* 1755:     */     int getPreferredSize0(int axis)
/* 1756:     */     {
/* 1757:2449 */       if ((this.pref == -2) || (this.pref == -1)) {
/* 1758:2450 */         return this.size;
/* 1759:     */       }
/* 1760:2452 */       return Math.max(this.size, this.pref);
/* 1761:     */     }
/* 1762:     */     
/* 1763:     */     int getMaximumSize0(int axis)
/* 1764:     */     {
/* 1765:2455 */       if (this.max >= 0) {
/* 1766:2456 */         return Math.max(getPreferredSize(axis), this.max);
/* 1767:     */       }
/* 1768:2458 */       return this.size;
/* 1769:     */     }
/* 1770:     */     
/* 1771:     */     String getMatchDescription()
/* 1772:     */     {
/* 1773:2462 */       return this.matches == null ? "" : this.matches.toString();
/* 1774:     */     }
/* 1775:     */     
/* 1776:     */     public String toString()
/* 1777:     */     {
/* 1778:2466 */       return super.toString() + getMatchDescription();
/* 1779:     */     }
/* 1780:     */   }
/* 1781:     */   
/* 1782:     */   private static class AutopaddingMatch
/* 1783:     */   {
/* 1784:     */     public GroupLayout.ComponentSpring source;
/* 1785:     */     public GroupLayout.ComponentSpring target;
/* 1786:     */     
/* 1787:     */     AutopaddingMatch(GroupLayout.ComponentSpring source, GroupLayout.ComponentSpring target)
/* 1788:     */     {
/* 1789:2480 */       this.source = source;
/* 1790:2481 */       this.target = target;
/* 1791:     */     }
/* 1792:     */     
/* 1793:     */     private String toString(GroupLayout.ComponentSpring spring)
/* 1794:     */     {
/* 1795:2485 */       return spring.getComponent().getName();
/* 1796:     */     }
/* 1797:     */     
/* 1798:     */     public String toString()
/* 1799:     */     {
/* 1800:2489 */       return "[" + toString(this.source) + "-" + toString(this.target) + "]";
/* 1801:     */     }
/* 1802:     */   }
/* 1803:     */   
/* 1804:     */   private class ContainerAutopaddingSpring
/* 1805:     */     extends GroupLayout.AutopaddingSpring
/* 1806:     */   {
/* 1807:     */     private List targets;
/* 1808:     */     
/* 1809:     */     ContainerAutopaddingSpring()
/* 1810:     */     {
/* 1811:2501 */       super(null);
/* 1812:2502 */       setUserCreated(true);
/* 1813:     */     }
/* 1814:     */     
/* 1815:     */     ContainerAutopaddingSpring(int pref, int max)
/* 1816:     */     {
/* 1817:2506 */       super(pref, max);
/* 1818:2507 */       setUserCreated(true);
/* 1819:     */     }
/* 1820:     */     
/* 1821:     */     public void add(GroupLayout.ComponentSpring spring, int axis)
/* 1822:     */     {
/* 1823:2511 */       if (this.targets == null) {
/* 1824:2512 */         this.targets = new ArrayList(1);
/* 1825:     */       }
/* 1826:2514 */       this.targets.add(spring);
/* 1827:     */     }
/* 1828:     */     
/* 1829:     */     public void calculatePadding(int axis)
/* 1830:     */     {
/* 1831:2518 */       LayoutStyle p = LayoutStyle.getSharedInstance();
/* 1832:2519 */       int maxPadding = 0;
/* 1833:2520 */       this.size = 0;
/* 1834:2521 */       if (this.targets != null)
/* 1835:     */       {
/* 1836:2524 */         int position = axis == 1 ? 7 : 1;
/* 1837:2526 */         for (int i = this.targets.size() - 1; i >= 0; i--)
/* 1838:     */         {
/* 1839:2527 */           GroupLayout.ComponentSpring targetSpring = (GroupLayout.ComponentSpring)this.targets.get(i);
/* 1840:     */           
/* 1841:2529 */           int padding = 10;
/* 1842:2530 */           if ((targetSpring.getComponent() instanceof JComponent))
/* 1843:     */           {
/* 1844:2531 */             padding = p.getContainerGap((JComponent)targetSpring.getComponent(), position, GroupLayout.this.host);
/* 1845:     */             
/* 1846:     */ 
/* 1847:2534 */             maxPadding = Math.max(padding, maxPadding);
/* 1848:2535 */             padding -= targetSpring.getOrigin();
/* 1849:     */           }
/* 1850:     */           else
/* 1851:     */           {
/* 1852:2537 */             maxPadding = Math.max(padding, maxPadding);
/* 1853:     */           }
/* 1854:2539 */           this.size = Math.max(this.size, padding);
/* 1855:     */         }
/* 1856:     */       }
/* 1857:     */       else
/* 1858:     */       {
/* 1859:2545 */         int position = axis == 1 ? 3 : 5;
/* 1860:2547 */         if (this.sources != null) {
/* 1861:2548 */           for (int i = this.sources.size() - 1; i >= 0; i--)
/* 1862:     */           {
/* 1863:2549 */             GroupLayout.ComponentSpring sourceSpring = (GroupLayout.ComponentSpring)this.sources.get(i);
/* 1864:     */             
/* 1865:2551 */             maxPadding = Math.max(maxPadding, updateSize(p, sourceSpring, position));
/* 1866:     */           }
/* 1867:2555 */         } else if (this.source != null) {
/* 1868:2556 */           maxPadding = updateSize(p, this.source, position);
/* 1869:     */         }
/* 1870:     */       }
/* 1871:2559 */       if (this.lastSize != -2147483648) {
/* 1872:2560 */         this.size += Math.min(maxPadding, this.lastSize);
/* 1873:     */       }
/* 1874:     */     }
/* 1875:     */     
/* 1876:     */     private int updateSize(LayoutStyle p, GroupLayout.ComponentSpring sourceSpring, int position)
/* 1877:     */     {
/* 1878:2566 */       int padding = 10;
/* 1879:2567 */       if ((sourceSpring.getComponent() instanceof JComponent)) {
/* 1880:2568 */         padding = p.getContainerGap((JComponent)sourceSpring.getComponent(), position, GroupLayout.this.host);
/* 1881:     */       }
/* 1882:2572 */       int delta = Math.max(0, getParent().getSize() - sourceSpring.getSize() - sourceSpring.getOrigin());
/* 1883:     */       
/* 1884:2574 */       this.size = Math.max(this.size, padding - delta);
/* 1885:2575 */       return padding;
/* 1886:     */     }
/* 1887:     */     
/* 1888:     */     String getMatchDescription()
/* 1889:     */     {
/* 1890:2579 */       if (this.targets != null) {
/* 1891:2580 */         return "leading: " + this.targets.toString();
/* 1892:     */       }
/* 1893:2582 */       if (this.sources != null) {
/* 1894:2583 */         return "trailing: " + this.sources.toString();
/* 1895:     */       }
/* 1896:2585 */       return "--";
/* 1897:     */     }
/* 1898:     */   }
/* 1899:     */   
/* 1900:     */   private static class ComponentInfo
/* 1901:     */   {
/* 1902:     */     private Component component;
/* 1903:     */     GroupLayout.ComponentSpring horizontalSpring;
/* 1904:     */     GroupLayout.ComponentSpring verticalSpring;
/* 1905:     */     private ComponentInfo horizontalMaster;
/* 1906:     */     private ComponentInfo verticalMaster;
/* 1907:     */     private List horizontalDependants;
/* 1908:     */     private List verticalDependants;
/* 1909:     */     private int[] horizontalSizes;
/* 1910:     */     private int[] verticalSizes;
/* 1911:     */     private boolean visible;
/* 1912:     */     
/* 1913:     */     ComponentInfo(Component component)
/* 1914:     */     {
/* 1915:2617 */       this.component = component;
/* 1916:2618 */       this.visible = GroupLayout.isVisible(component);
/* 1917:2619 */       clear();
/* 1918:     */     }
/* 1919:     */     
/* 1920:     */     public void dispose()
/* 1921:     */     {
/* 1922:2624 */       removeSpring(this.horizontalSpring);
/* 1923:2625 */       this.horizontalSpring = null;
/* 1924:2626 */       removeSpring(this.verticalSpring);
/* 1925:2627 */       this.verticalSpring = null;
/* 1926:2629 */       if (this.horizontalMaster == this)
/* 1927:     */       {
/* 1928:2630 */         this.horizontalDependants.remove(this);
/* 1929:2631 */         if (this.horizontalDependants.size() == 1)
/* 1930:     */         {
/* 1931:2632 */           ComponentInfo linked = (ComponentInfo)this.horizontalDependants.get(0);
/* 1932:     */           
/* 1933:2634 */           linked.horizontalMaster = null;
/* 1934:2635 */           linked.horizontalDependants = null;
/* 1935:     */         }
/* 1936:2636 */         else if (this.horizontalDependants.size() > 1)
/* 1937:     */         {
/* 1938:2637 */           ComponentInfo newMaster = (ComponentInfo)this.horizontalDependants.get(0);
/* 1939:     */           
/* 1940:2639 */           newMaster.horizontalMaster = newMaster;
/* 1941:2640 */           newMaster.horizontalDependants = this.horizontalDependants;
/* 1942:     */         }
/* 1943:     */       }
/* 1944:2642 */       else if (this.horizontalMaster != null)
/* 1945:     */       {
/* 1946:2643 */         this.horizontalMaster.horizontalDependants.remove(this);
/* 1947:2644 */         if (this.horizontalMaster.horizontalDependants.size() == 1)
/* 1948:     */         {
/* 1949:2645 */           this.horizontalMaster.horizontalDependants = null;
/* 1950:2646 */           this.horizontalMaster.horizontalMaster = null;
/* 1951:     */         }
/* 1952:     */       }
/* 1953:2650 */       if (this.verticalMaster == this)
/* 1954:     */       {
/* 1955:2651 */         this.verticalDependants.remove(this);
/* 1956:2652 */         if (this.verticalDependants.size() == 1)
/* 1957:     */         {
/* 1958:2653 */           ComponentInfo linked = (ComponentInfo)this.verticalDependants.get(0);
/* 1959:     */           
/* 1960:2655 */           linked.verticalMaster = null;
/* 1961:2656 */           linked.verticalDependants = null;
/* 1962:     */         }
/* 1963:2657 */         else if (this.verticalDependants.size() > 1)
/* 1964:     */         {
/* 1965:2658 */           ComponentInfo newMaster = (ComponentInfo)this.verticalDependants.get(0);
/* 1966:     */           
/* 1967:2660 */           newMaster.verticalMaster = newMaster;
/* 1968:2661 */           newMaster.verticalDependants = this.verticalDependants;
/* 1969:     */         }
/* 1970:     */       }
/* 1971:2663 */       else if (this.verticalMaster != null)
/* 1972:     */       {
/* 1973:2664 */         this.verticalMaster.verticalDependants.remove(this);
/* 1974:2665 */         if (this.verticalMaster.verticalDependants.size() == 1)
/* 1975:     */         {
/* 1976:2666 */           this.verticalMaster.verticalDependants = null;
/* 1977:2667 */           this.verticalMaster.verticalMaster = null;
/* 1978:     */         }
/* 1979:     */       }
/* 1980:     */     }
/* 1981:     */     
/* 1982:     */     private void removeSpring(GroupLayout.Spring spring)
/* 1983:     */     {
/* 1984:2673 */       if (spring != null) {
/* 1985:2674 */         ((GroupLayout.Group)spring.getParent()).springs.remove(spring);
/* 1986:     */       }
/* 1987:     */     }
/* 1988:     */     
/* 1989:     */     public boolean isVisible()
/* 1990:     */     {
/* 1991:2679 */       return this.visible;
/* 1992:     */     }
/* 1993:     */     
/* 1994:     */     boolean updateVisibility()
/* 1995:     */     {
/* 1996:2688 */       boolean newVisible = GroupLayout.isVisible(this.component);
/* 1997:2689 */       if (this.visible != newVisible)
/* 1998:     */       {
/* 1999:2690 */         this.visible = newVisible;
/* 2000:2691 */         return true;
/* 2001:     */       }
/* 2002:2693 */       return false;
/* 2003:     */     }
/* 2004:     */     
/* 2005:     */     public void setBounds(Insets insets)
/* 2006:     */     {
/* 2007:2697 */       int x = 0;
/* 2008:2698 */       int y = 0;
/* 2009:2699 */       int w = 0;
/* 2010:2700 */       int h = 0;
/* 2011:2702 */       if (this.horizontalSpring != null)
/* 2012:     */       {
/* 2013:2703 */         x = this.horizontalSpring.getOrigin();
/* 2014:2704 */         w = this.horizontalSpring.getSize();
/* 2015:     */       }
/* 2016:2706 */       if (this.verticalSpring != null)
/* 2017:     */       {
/* 2018:2707 */         y = this.verticalSpring.getOrigin();
/* 2019:2708 */         h = this.verticalSpring.getSize();
/* 2020:     */       }
/* 2021:2710 */       this.component.setBounds(x + insets.left, y + insets.top, w, h);
/* 2022:     */     }
/* 2023:     */     
/* 2024:     */     public void setComponent(Component component)
/* 2025:     */     {
/* 2026:2714 */       this.component = component;
/* 2027:2715 */       if (this.horizontalSpring != null) {
/* 2028:2716 */         this.horizontalSpring.setComponent(component);
/* 2029:     */       }
/* 2030:2718 */       if (this.verticalSpring != null) {
/* 2031:2719 */         this.verticalSpring.setComponent(component);
/* 2032:     */       }
/* 2033:     */     }
/* 2034:     */     
/* 2035:     */     public Component getComponent()
/* 2036:     */     {
/* 2037:2724 */       return this.component;
/* 2038:     */     }
/* 2039:     */     
/* 2040:     */     public boolean isLinked(int axis)
/* 2041:     */     {
/* 2042:2732 */       if (axis == 1) {
/* 2043:2733 */         return this.horizontalMaster != null;
/* 2044:     */       }
/* 2045:2735 */       return this.verticalMaster != null;
/* 2046:     */     }
/* 2047:     */     
/* 2048:     */     public ComponentInfo getMasterComponentInfo(int axis)
/* 2049:     */     {
/* 2050:2739 */       if (axis == 1)
/* 2051:     */       {
/* 2052:2740 */         if (this.horizontalMaster == null)
/* 2053:     */         {
/* 2054:2741 */           this.horizontalMaster = this;
/* 2055:2742 */           this.horizontalDependants = new ArrayList(1);
/* 2056:2743 */           this.horizontalDependants.add(this);
/* 2057:2744 */           this.horizontalSizes = new int[3];
/* 2058:2745 */           clear();
/* 2059:     */         }
/* 2060:2747 */         return this.horizontalMaster;
/* 2061:     */       }
/* 2062:2749 */       if (this.verticalMaster == null)
/* 2063:     */       {
/* 2064:2750 */         this.verticalMaster = this;
/* 2065:2751 */         this.verticalDependants = new ArrayList(1);
/* 2066:2752 */         this.verticalDependants.add(this);
/* 2067:2753 */         this.verticalSizes = new int[3];
/* 2068:2754 */         clear();
/* 2069:     */       }
/* 2070:2756 */       return this.verticalMaster;
/* 2071:     */     }
/* 2072:     */     
/* 2073:     */     public void addChild(ComponentInfo child, int axis)
/* 2074:     */     {
/* 2075:2761 */       if (axis == 1)
/* 2076:     */       {
/* 2077:2762 */         addChild0(child, 1);
/* 2078:     */       }
/* 2079:     */       else
/* 2080:     */       {
/* 2081:2764 */         assert (axis == 2);
/* 2082:2765 */         addChild0(child, 2);
/* 2083:     */       }
/* 2084:     */     }
/* 2085:     */     
/* 2086:     */     private void addChild0(ComponentInfo child, int axis)
/* 2087:     */     {
/* 2088:2770 */       if (axis == 1)
/* 2089:     */       {
/* 2090:2771 */         if (child.horizontalMaster == child)
/* 2091:     */         {
/* 2092:2774 */           this.horizontalDependants.addAll(child.horizontalDependants);
/* 2093:2775 */           child.horizontalDependants = null;
/* 2094:2776 */           child.horizontalSizes = null;
/* 2095:     */         }
/* 2096:     */         else
/* 2097:     */         {
/* 2098:2779 */           this.horizontalDependants.add(child);
/* 2099:     */         }
/* 2100:2781 */         child.horizontalMaster = this;
/* 2101:     */       }
/* 2102:     */       else
/* 2103:     */       {
/* 2104:2783 */         if (child.verticalMaster == child)
/* 2105:     */         {
/* 2106:2786 */           this.verticalDependants.addAll(child.verticalDependants);
/* 2107:2787 */           child.verticalDependants = null;
/* 2108:2788 */           child.verticalSizes = null;
/* 2109:     */         }
/* 2110:     */         else
/* 2111:     */         {
/* 2112:2791 */           this.verticalDependants.add(child);
/* 2113:     */         }
/* 2114:2793 */         child.verticalMaster = this;
/* 2115:     */       }
/* 2116:     */     }
/* 2117:     */     
/* 2118:     */     public void clear()
/* 2119:     */     {
/* 2120:2798 */       clear(this.horizontalSizes);
/* 2121:2799 */       clear(this.verticalSizes);
/* 2122:     */     }
/* 2123:     */     
/* 2124:     */     private void clear(int[] sizes)
/* 2125:     */     {
/* 2126:2803 */       if (sizes != null) {
/* 2127:2804 */         for (int counter = sizes.length - 1; counter >= 0; counter--) {
/* 2128:2805 */           sizes[counter] = -2147483648;
/* 2129:     */         }
/* 2130:     */       }
/* 2131:     */     }
/* 2132:     */     
/* 2133:     */     int getLinkSize(int axis, int type)
/* 2134:     */     {
/* 2135:2817 */       int[] sizes = null;
/* 2136:2818 */       List dependants = null;
/* 2137:2819 */       if (axis == 1)
/* 2138:     */       {
/* 2139:2820 */         if (this.horizontalMaster != this) {
/* 2140:2821 */           return this.horizontalMaster.getLinkSize(axis, type);
/* 2141:     */         }
/* 2142:2823 */         sizes = this.horizontalSizes;
/* 2143:2824 */         dependants = this.horizontalDependants;
/* 2144:     */       }
/* 2145:2825 */       else if (axis == 2)
/* 2146:     */       {
/* 2147:2826 */         if (this.verticalMaster != this) {
/* 2148:2827 */           return this.verticalMaster.getLinkSize(axis, type);
/* 2149:     */         }
/* 2150:2829 */         sizes = this.verticalSizes;
/* 2151:2830 */         dependants = this.verticalDependants;
/* 2152:     */       }
/* 2153:2832 */       if (sizes[type] == -2147483648) {
/* 2154:2833 */         sizes[type] = calcLinkSize(dependants, axis, type);
/* 2155:     */       }
/* 2156:2835 */       return sizes[type];
/* 2157:     */     }
/* 2158:     */     
/* 2159:     */     private int calcLinkSize(List dependants, int axis, int type)
/* 2160:     */     {
/* 2161:2839 */       int count = dependants.size() - 1;
/* 2162:2840 */       int size = getDependantSpringSize(dependants, axis, type, count--);
/* 2163:2841 */       while (count >= 0) {
/* 2164:2842 */         size = Math.max(size, getDependantSpringSize(dependants, axis, type, count--));
/* 2165:     */       }
/* 2166:2845 */       return size;
/* 2167:     */     }
/* 2168:     */     
/* 2169:     */     private int getDependantSpringSize(List dependants, int axis, int type, int index)
/* 2170:     */     {
/* 2171:2850 */       ComponentInfo ci = (ComponentInfo)dependants.get(index);
/* 2172:     */       GroupLayout.ComponentSpring spring;
/* 2173:     */       GroupLayout.ComponentSpring spring;
/* 2174:2852 */       if (axis == 1) {
/* 2175:2853 */         spring = ci.horizontalSpring;
/* 2176:     */       } else {
/* 2177:2855 */         spring = ci.verticalSpring;
/* 2178:     */       }
/* 2179:2857 */       return spring.getPreferredSize1(axis);
/* 2180:     */     }
/* 2181:     */   }
/* 2182:     */ }


/* Location:           C:\Users\myros\Desktop\NewGreek2Greenglish.jar
 * Qualified Name:     org.jdesktop.layout.GroupLayout
 * JD-Core Version:    0.7.0.1
 */