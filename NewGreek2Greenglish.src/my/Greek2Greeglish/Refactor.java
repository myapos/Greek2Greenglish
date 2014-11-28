/*   1:    */ package my.Greek2Greeglish;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ 
/*   6:    */ public class Refactor
/*   7:    */ {
/*   8:    */   String filename;
/*   9:    */   File file;
/*  10:    */   char cret;
/*  11:    */   
/*  12:    */   public Refactor(String filename, File file)
/*  13:    */   {
/*  14: 32 */     this.filename = filename;
/*  15: 33 */     Convert();
/*  16: 34 */     this.file = file;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public Refactor() {}
/*  20:    */   
/*  21:    */   public void Count()
/*  22:    */   {
/*  23: 44 */     for (int i = 0; i < 5; i++) {
/*  24: 45 */       System.out.println("Metrisi " + i);
/*  25:    */     }
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void Convert()
/*  29:    */   {
/*  30: 49 */     String newfilename = "";
/*  31: 53 */     for (int i = 0; i < this.filename.length(); i++)
/*  32:    */     {
/*  33: 55 */       char englchar = Map(this.filename.charAt(i));
/*  34: 56 */       String str = Character.toString(englchar);
/*  35:    */       
/*  36:    */ 
/*  37: 59 */       newfilename = newfilename + str;
/*  38:    */     }
/*  39: 62 */     this.filename = newfilename;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public char Map(char c)
/*  43:    */   {
/*  44: 74 */     int charvalue = c;
/*  45: 77 */     if (charvalue == 945) {
/*  46: 78 */       charvalue = 97;
/*  47: 79 */     } else if (charvalue == 940) {
/*  48: 80 */       charvalue = 97;
/*  49: 81 */     } else if (charvalue == 946) {
/*  50: 82 */       charvalue = 98;
/*  51: 83 */     } else if (charvalue == 947) {
/*  52: 84 */       charvalue = 103;
/*  53: 85 */     } else if (charvalue == 948) {
/*  54: 86 */       charvalue = 100;
/*  55: 87 */     } else if (charvalue == 949) {
/*  56: 88 */       charvalue = 101;
/*  57: 89 */     } else if (charvalue == 941) {
/*  58: 90 */       charvalue = 101;
/*  59: 91 */     } else if (charvalue == 950) {
/*  60: 92 */       charvalue = 122;
/*  61: 93 */     } else if (charvalue == 951) {
/*  62: 94 */       charvalue = 104;
/*  63: 95 */     } else if (charvalue == 942) {
/*  64: 96 */       charvalue = 104;
/*  65: 97 */     } else if (charvalue == 952) {
/*  66: 98 */       charvalue = 56;
/*  67: 99 */     } else if (charvalue == 953) {
/*  68:100 */       charvalue = 105;
/*  69:101 */     } else if (charvalue == 943) {
/*  70:102 */       charvalue = 105;
/*  71:103 */     } else if (charvalue == 970) {
/*  72:104 */       charvalue = 105;
/*  73:105 */     } else if (charvalue == 954) {
/*  74:106 */       charvalue = 107;
/*  75:107 */     } else if (charvalue == 955) {
/*  76:108 */       charvalue = 108;
/*  77:109 */     } else if (charvalue == 956) {
/*  78:110 */       charvalue = 109;
/*  79:111 */     } else if (charvalue == 957) {
/*  80:112 */       charvalue = 110;
/*  81:113 */     } else if (charvalue == 958) {
/*  82:114 */       charvalue = 51;
/*  83:115 */     } else if (charvalue == 959) {
/*  84:116 */       charvalue = 111;
/*  85:117 */     } else if (charvalue == 972) {
/*  86:118 */       charvalue = 111;
/*  87:119 */     } else if (charvalue == 960) {
/*  88:120 */       charvalue = 112;
/*  89:121 */     } else if (charvalue == 961) {
/*  90:122 */       charvalue = 114;
/*  91:123 */     } else if (charvalue == 962) {
/*  92:124 */       charvalue = 115;
/*  93:125 */     } else if (charvalue == 963) {
/*  94:126 */       charvalue = 115;
/*  95:127 */     } else if (charvalue == 964) {
/*  96:128 */       charvalue = 116;
/*  97:129 */     } else if (charvalue == 965) {
/*  98:130 */       charvalue = 117;
/*  99:131 */     } else if (charvalue == 973) {
/* 100:132 */       charvalue = 117;
/* 101:133 */     } else if (charvalue == 971) {
/* 102:134 */       charvalue = 117;
/* 103:135 */     } else if (charvalue == 966) {
/* 104:136 */       charvalue = 102;
/* 105:137 */     } else if (charvalue == 967) {
/* 106:138 */       charvalue = 120;
/* 107:139 */     } else if (charvalue == 968) {
/* 108:140 */       charvalue = 121;
/* 109:141 */     } else if (charvalue == 969) {
/* 110:142 */       charvalue = 119;
/* 111:144 */     } else if (charvalue == 913) {
/* 112:145 */       charvalue = 65;
/* 113:146 */     } else if (charvalue == 914) {
/* 114:147 */       charvalue = 66;
/* 115:148 */     } else if (charvalue == 915) {
/* 116:149 */       charvalue = 71;
/* 117:150 */     } else if (charvalue == 916) {
/* 118:151 */       charvalue = 68;
/* 119:152 */     } else if (charvalue == 917) {
/* 120:153 */       charvalue = 69;
/* 121:154 */     } else if (charvalue == 904) {
/* 122:155 */       charvalue = 69;
/* 123:156 */     } else if (charvalue == 918) {
/* 124:157 */       charvalue = 90;
/* 125:158 */     } else if (charvalue == 919) {
/* 126:159 */       charvalue = 72;
/* 127:160 */     } else if (charvalue == 905) {
/* 128:161 */       charvalue = 72;
/* 129:162 */     } else if (charvalue == 920) {
/* 130:163 */       charvalue = 56;
/* 131:164 */     } else if (charvalue == 921) {
/* 132:165 */       charvalue = 73;
/* 133:166 */     } else if (charvalue == 906) {
/* 134:167 */       charvalue = 73;
/* 135:168 */     } else if (charvalue == 938) {
/* 136:169 */       charvalue = 73;
/* 137:170 */     } else if (charvalue == 922) {
/* 138:171 */       charvalue = 75;
/* 139:172 */     } else if (charvalue == 923) {
/* 140:173 */       charvalue = 76;
/* 141:174 */     } else if (charvalue == 924) {
/* 142:175 */       charvalue = 77;
/* 143:176 */     } else if (charvalue == 925) {
/* 144:177 */       charvalue = 78;
/* 145:178 */     } else if (charvalue == 926) {
/* 146:179 */       charvalue = 51;
/* 147:180 */     } else if (charvalue == 927) {
/* 148:181 */       charvalue = 79;
/* 149:182 */     } else if (charvalue == 908) {
/* 150:183 */       charvalue = 79;
/* 151:184 */     } else if (charvalue == 928) {
/* 152:185 */       charvalue = 80;
/* 153:186 */     } else if (charvalue == 929) {
/* 154:187 */       charvalue = 82;
/* 155:188 */     } else if (charvalue == 931) {
/* 156:189 */       charvalue = 83;
/* 157:190 */     } else if (charvalue == 932) {
/* 158:191 */       charvalue = 84;
/* 159:192 */     } else if (charvalue == 933) {
/* 160:193 */       charvalue = 85;
/* 161:194 */     } else if (charvalue == 910) {
/* 162:195 */       charvalue = 85;
/* 163:196 */     } else if (charvalue == 939) {
/* 164:197 */       charvalue = 85;
/* 165:198 */     } else if (charvalue == 934) {
/* 166:199 */       charvalue = 70;
/* 167:200 */     } else if (charvalue == 935) {
/* 168:201 */       charvalue = 88;
/* 169:202 */     } else if (charvalue == 949) {
/* 170:203 */       charvalue = 89;
/* 171:204 */     } else if (charvalue == 937) {
/* 172:205 */       charvalue = 87;
/* 173:206 */     } else if (charvalue == 911) {
/* 174:207 */       charvalue = 87;
/* 175:    */     }
/* 176:209 */     return (char)charvalue;
/* 177:    */   }
/* 178:    */ }


/* Location:           C:\Users\myros\Desktop\NewGreek2Greenglish.jar
 * Qualified Name:     my.Greek2Greeglish.Refactor
 * JD-Core Version:    0.7.0.1
 */