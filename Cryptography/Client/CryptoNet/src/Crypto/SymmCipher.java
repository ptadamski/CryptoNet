/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.security.Key;

//package Cryptography.Interfaces;

public class SymmCipher implements ICipher
{
    public enum Mode 
    {ECB, CBC, CFB, OFB8, OFB16, OFB32};
    
    public enum Algorithm 
    {DES, TripleDES, Blowfish} 
    
    public enum Padding 
    {None,  PKCS5}
   
    public SymmCipher(SymmCipher symmCipher, SecretKey key) throws Exception
    {
        this.algorithm = symmCipher.algorithm;
        this.mode = symmCipher.mode;
        this.padding = symmCipher.padding;
        this.iv = symmCipher.iv;
        this.cipher = Cipher.getInstance(this.getTransformation());
        //this.iv = new IvParameterSpec();
        this.key = key;
    }
    
    public SymmCipher(Algorithm algorithm, Mode mode, Padding padding) throws Exception
    {
        this.algorithm = algorithm;
        this.mode = mode;
        this.padding = padding;
        this.cipher = Cipher.getInstance(this.getTransformation());
        //this.iv = new IvParameterSpec();
        this.createKey();
    }      
    
    public SymmCipher(Algorithm algorithm, Mode mode, Padding padding, SecretKey key) throws Exception
    {
        this.algorithm = algorithm;
        this.mode = mode;
        this.padding = padding;
        this.cipher = Cipher.getInstance(this.getTransformation());
        //this.iv = new IvParameterSpec();
        this.key = key;
    }  
    
    private SecretKey key;
    private Cipher cipher;
    private IvParameterSpec iv;
    
    private Algorithm algorithm;
    private Padding padding;
    private Mode mode;

    public String getAlgorithm()
    {
        String strAlgorithm = "";
        //if(strAlgorithm==null)
            switch(algorithm)
            {
                case DES:
                    strAlgorithm = "DES";
                    break;
                case TripleDES:
                    strAlgorithm = "DESede";
                    break;
                case Blowfish:
                    strAlgorithm = "Blowfish";
                    break;
            }
        return strAlgorithm;
    }

    public String getPadding()
    {
        String strPadding = "";
        //if(strPadding==null)
            switch(padding)
            {
                case None:
                    strPadding = "NoPadding";
                    break;
                case PKCS5:
                    strPadding = "PKCS5Padding"; 
                    break;
            }
        return strPadding;
    }

    public String getMode()
    {
        String strMode = "";
        //if(strMode==null)
            switch(mode)
            {
                case ECB:
                    strMode = "ECB";
                    break;
                case CBC:
                    strMode = "CBC";
                    break;
                case CFB:
                    strMode = "CFB";
                    break;
                case OFB8:
                    strMode = "OFB8";
                    break;
                case OFB16:
                    strMode = "OFB16";
                    break;
                case OFB32:
                    strMode = "OFB32";
                    break;
            }
        return strMode;
    }
    
    public void getTransformation(String algorithm, String mode, String padding)
    {
        algorithm = getAlgorithm();
        mode = getMode();
        padding = getPadding();
    }
    
    public void getTransformation(Algorithm algorithm, Mode mode, Padding padding)
    {
        algorithm = this.algorithm;
        mode = this.mode;
        padding = this.padding;
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception
    {        
        //dobrze by bylo ustalic IV oraz secret key pomiedzy dwoma jednostkami na bazie DM
        
        switch(this.mode)
        {
            case CBC:
            case CFB:
            case OFB8:
            case OFB16:
            case OFB32:
                cipher.init(Cipher.ENCRYPT_MODE, key, iv);
                break;
                
            case ECB:
                cipher.init(Cipher.ENCRYPT_MODE, key);
                break;
        }
        return cipher.doFinal(data);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception
    {
        switch(this.mode)
        {
            case CBC:
            case CFB:
            case OFB8:
            case OFB16:
            case OFB32:
                cipher.init(Cipher.DECRYPT_MODE, key, iv);
                break;
                
            case ECB:
                cipher.init(Cipher.DECRYPT_MODE, key);
                break;
        }
        return cipher.doFinal(data);
    }
    
    @Override
    public void createKey() throws Exception
    {
        KeyGenerator kg = KeyGenerator.getInstance(this.getTransformation());
        key = kg.generateKey();
    }    
    
    @Override
    public String getTransformation() 
    {
        String algorithm = "", mode = "", padding = "";
        getTransformation(algorithm, mode, padding);
        StringBuilder sb = new StringBuilder();
        sb.append(algorithm).
                append("\\").append(mode).
                append("\\").append(padding);
        return sb.toString();
    }
    
    @Override
    public Key showKey()
    {
        return key;
    }
    
    public static void main(String[] args){
        
        //SymmCipher symm = new SymmCipher(Algorithm.DES, Mode.CBC, Padding.PKCS5, key);
                
        String s="ala ma kota a kot ma ale";
        byte[] bytes = s.getBytes();
        
        SecretKey key = new SecretKeySpec(bytes, "DES");
        SecretKey key2 = new SecretKeySpec(bytes, "DES");
        
        //if(new String(new byte[]{1,2}).equals(new String(new byte[]{1,2})))
        
        String str = new String(key2.getEncoded());
        if( new String(key.getEncoded()).equals(str) )
            System.out.println("takie same");
      else
            System.out.println("inne");
        
       System.out.println(new String(key.getEncoded()));
        System.out.println(new String(key2.getEncoded()));
        
    }
}
