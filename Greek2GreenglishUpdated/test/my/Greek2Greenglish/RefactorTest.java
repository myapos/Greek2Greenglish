/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.Greek2Greenglish;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author myros
 */
public class RefactorTest {
    
    public RefactorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of Count method, of class Refactor.
     */
    /*
    @Test
    public void testCount() {
        System.out.println("Count");
        Refactor instance = new Refactor();
        instance.Count();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    */
    /**
     * Test of Convert method, of class Refactor.
     */
    /*
    @Test
    public void testConvert() {
        System.out.println("Convert");
        Refactor instance = new Refactor();
        instance.Convert();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    */
    /**
     * Test of Map method, of class Refactor.
     */
    /*
    @Test
    public void testMap() {
        System.out.println("Map");
        char c = ' ';
        Refactor instance = new Refactor();
        char expResult = ' ';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    */
    
    
    /* Tests for lower case letters*/
    
    @Test
    public void testMap1() {
        System.out.println("Map");
        char c = 'α';
        Refactor instance = new Refactor();
        char expResult = 'a';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    @Test
    public void testMap2() {
        System.out.println("Map");
        char c = 'β';
        Refactor instance = new Refactor();
        char expResult = 'b';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap3() {
        System.out.println("Map");
        char c = 'γ';
        Refactor instance = new Refactor();
        char expResult = 'g';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap4() {
        System.out.println("Map");
        char c = 'δ';
        Refactor instance = new Refactor();
        char expResult = 'd';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap5() {
        System.out.println("Map");
        char c = 'ε';
        Refactor instance = new Refactor();
        char expResult = 'e';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap6() {
        System.out.println("Map");
        char c = 'ζ';
        Refactor instance = new Refactor();
        char expResult = 'z';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap7() {
        System.out.println("Map");
        char c = 'η';
        Refactor instance = new Refactor();
        char expResult = 'h';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap8() {
        System.out.println("Map");
        char c = 'θ';
        Refactor instance = new Refactor();
        char expResult = '8';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap9() {
        System.out.println("Map");
        char c = 'ι';
        Refactor instance = new Refactor();
        char expResult = 'i';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap10() {
        System.out.println("Map");
        char c = 'κ';
        Refactor instance = new Refactor();
        char expResult = 'k';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap11() {
        System.out.println("Map");
        char c = 'λ';
        Refactor instance = new Refactor();
        char expResult = 'l';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }   
    
    @Test
    public void testMap12() {
        System.out.println("Map");
        char c = 'μ';
        Refactor instance = new Refactor();
        char expResult = 'm';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }   
    
    @Test
    public void testMap13() {
        System.out.println("Map");
        char c = 'ν';
        Refactor instance = new Refactor();
        char expResult = 'n';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }   
    
    @Test
    public void testMap14() {
        System.out.println("Map");
        char c = 'ξ';
        Refactor instance = new Refactor();
        char expResult = '3';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }   
    
    @Test
    public void testMap15() {
        System.out.println("Map");
        char c = 'ο';
        Refactor instance = new Refactor();
        char expResult = 'o';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
    
    @Test
    public void testMap16() {
        System.out.println("Map");
        char c = 'π';
        Refactor instance = new Refactor();
        char expResult = 'p';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
    
    @Test
    public void testMap17() {
        System.out.println("Map");
        char c = 'ρ';
        Refactor instance = new Refactor();
        char expResult = 'r';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
    
    @Test
    public void testMap18() {
        System.out.println("Map");
        char c = 'σ';
        Refactor instance = new Refactor();
        char expResult = 's';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
    
    @Test
    public void testMap19() {
        System.out.println("Map");
        char c = 'τ';
        Refactor instance = new Refactor();
        char expResult = 't';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
    
    @Test
    public void testMap20() {
        System.out.println("Map");
        char c = 'υ';
        Refactor instance = new Refactor();
        char expResult = 'u';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap21() {
        System.out.println("Map");
        char c = 'φ';
        Refactor instance = new Refactor();
        char expResult = 'f';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap22() {
        System.out.println("Map");
        char c = 'χ';
        Refactor instance = new Refactor();
        char expResult = 'x';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
    
    @Test
    public void testMap23() {
        System.out.println("Map");
        char c = 'ψ';
        Refactor instance = new Refactor();
        char expResult = 'y';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
    
    @Test
    public void testMap24() {
        System.out.println("Map");
        char c = 'ω';
        Refactor instance = new Refactor();
        char expResult = 'w';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
     /* Tests for upper case letters*/
    
    @Test
    public void testMap25() {
        System.out.println("Map");
        char c = 'Α';
        Refactor instance = new Refactor();
        char expResult = 'A';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    @Test
    public void testMap26() {
        System.out.println("Map");
        char c = 'Β';
        Refactor instance = new Refactor();
        char expResult = 'B';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap27() {
        System.out.println("Map");
        char c = 'Γ';
        Refactor instance = new Refactor();
        char expResult = 'G';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap28() {
        System.out.println("Map");
        char c = 'Δ';
        Refactor instance = new Refactor();
        char expResult = 'D';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap29() {
        System.out.println("Map");
        char c = 'E';
        Refactor instance = new Refactor();
        char expResult = 'E';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap30() {
        System.out.println("Map");
        char c = 'Ζ';
        Refactor instance = new Refactor();
        char expResult = 'Z';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap31() {
        System.out.println("Map");
        char c = 'Η';
        Refactor instance = new Refactor();
        char expResult = 'H';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap32() {
        System.out.println("Map");
        char c = 'Θ';
        Refactor instance = new Refactor();
        char expResult = '8';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap33() {
        System.out.println("Map");
        char c = 'Ι';
        Refactor instance = new Refactor();
        char expResult = 'I';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap34() {
        System.out.println("Map");
        char c = 'Κ';
        Refactor instance = new Refactor();
        char expResult = 'K';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap35() {
        System.out.println("Map");
        char c = 'Λ';
        Refactor instance = new Refactor();
        char expResult = 'L';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }   
    
    @Test
    public void testMap36() {
        System.out.println("Map");
        char c = 'Μ';
        Refactor instance = new Refactor();
        char expResult = 'M';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }   
    
    @Test
    public void testMap37() {
        System.out.println("Map");
        char c = 'N';
        Refactor instance = new Refactor();
        char expResult = 'N';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }   
    
    @Test
    public void testMap38() {
        System.out.println("Map");
        char c = 'Ξ';
        Refactor instance = new Refactor();
        char expResult = '3';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }   
    
    @Test
    public void testMap39() {
        System.out.println("Map");
        char c = 'Ο';
        Refactor instance = new Refactor();
        char expResult = 'O';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
    
    @Test
    public void testMap40() {
        System.out.println("Map");
        char c = 'Π';
        Refactor instance = new Refactor();
        char expResult = 'P';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
    
    @Test
    public void testMap41() {
        System.out.println("Map");
        char c = 'Ρ';
        Refactor instance = new Refactor();
        char expResult = 'R';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
    
    @Test
    public void testMap42() {
        System.out.println("Map");
        char c = 'Σ';
        Refactor instance = new Refactor();
        char expResult = 'S';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
    
    @Test
    public void testMap43() {
        System.out.println("Map");
        char c = 'Τ';
        Refactor instance = new Refactor();
        char expResult = 'T';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
    
    @Test
    public void testMap44() {
        System.out.println("Map");
        char c = 'Υ';
        Refactor instance = new Refactor();
        char expResult = 'U';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap45() {
        System.out.println("Map");
        char c = 'Φ';
        Refactor instance = new Refactor();
        char expResult = 'F';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap46() {
        System.out.println("Map");
        char c = 'Χ';
        Refactor instance = new Refactor();
        char expResult = 'X';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
    
    @Test
    public void testMap47() {
        System.out.println("Map");
        char c = 'Ψ';
        Refactor instance = new Refactor();
        char expResult = 'Y';
        //int charv =c; 
        //System.out.println(charv);
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
    
    @Test
    public void testMap48() {
        System.out.println("Map");
        char c = 'Ω';
        Refactor instance = new Refactor();
        char expResult = 'W';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
     /* Tests for special case letters ά, έ, ύ, ί, ό, ΰ, ϊ, ϋ  */
    
    @Test
    public void testMap49() {
        System.out.println("Map");
        char c = 'ά';
        Refactor instance = new Refactor();
        char expResult = 'a';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap50() {
        System.out.println("Map");
        char c = 'έ';
        Refactor instance = new Refactor();
        char expResult = 'e';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap51() {
        System.out.println("Map");
        char c = 'ύ';
        Refactor instance = new Refactor();
        char expResult = 'u';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap52() {
        System.out.println("Map");
        char c = 'ί';
        Refactor instance = new Refactor();
        char expResult = 'i';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap53() {
        System.out.println("Map");
        char c = 'ό';
        Refactor instance = new Refactor();
        char expResult = 'o';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap54() {
        System.out.println("Map");
        char c = 'ΰ';
        Refactor instance = new Refactor();
        char expResult = 'u';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    @Test
    public void testMap54_() {
        System.out.println("Map");
        char c = 'ϊ';
        Refactor instance = new Refactor();
        char expResult = 'i';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap54__() {
        System.out.println("Map");
        char c = 'ϋ';
        Refactor instance = new Refactor();
        char expResult = 'u';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
     /* Tests for special case letters Ά,Έ,Ύ,Ί,Ό, Ϋ, Ϊ  */
    
    @Test
    public void testMap55() {
        System.out.println("Map");
        char c = 'Ά';
        //int charvalue1 = c;
        //System.out.println(charvalue1);
        Refactor instance = new Refactor();
        char expResult = 'A';
        //int charvalue2 = expResult;
        //System.out.println(charvalue2);
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap56() {
        System.out.println("Map");
        char c = 'Έ';
        Refactor instance = new Refactor();
        char expResult = 'E';
        //int charvalue2 = expResult;
        //System.out.println(charvalue2);
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap57() {
        System.out.println("Map");
        char c = 'Ύ';
        int charvalue1 = c;
        //System.out.println(charvalue1);
        Refactor instance = new Refactor();
        char expResult = 'Y';
        int charvalue2=expResult;
        //System.out.println(charvalue2);
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap58() {
        System.out.println("Map");
        char c = 'Ί';
        Refactor instance = new Refactor();
        char expResult = 'I';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    @Test
    public void testMap59() {
        System.out.println("Map");
        char c = 'Ό';
        Refactor instance = new Refactor();
        char expResult = 'O';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap60() {
        System.out.println("Map");
        char c = 'Ϋ';
        int charvalue1 = c;
        //System.out.println(charvalue1);
        Refactor instance = new Refactor();
        char expResult = 'Υ';
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testMap61() {
        System.out.println("Map");
        char c = 'Ϊ';
        int charvalue1 = c;
        //System.out.println(charvalue1);
        Refactor instance = new Refactor();
        char expResult = 'Ι';
        
        int charvalue2=expResult;
        //System.out.println(charvalue2);
        char result = instance.Map(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    
    
}
