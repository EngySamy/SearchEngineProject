/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectapt;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Mennah Rabie
 */
public class StringStream extends InputStream {
    
 private String string; 
 private int count = 0; 
 
 @Override 
 public int read() throws IOException { 
  if (count >= string.length()) { 
   return -1; 
  } 
  return string.charAt(count++); 
 } 
 
 public StringStream(String string) { 
  super(); 
  this.string = string; 
 } 
}
