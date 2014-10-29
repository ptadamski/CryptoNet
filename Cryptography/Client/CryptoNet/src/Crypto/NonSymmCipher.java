/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crypto;

/**
 *
 * @author progger
 */
public class NonSymmCipher 
{    
    public enum Mode 
    { None, EBC, CBC, CTR, CTS, OFM, PCBC };
    
    public enum Algorithm 
    { AES, DSA, DM, CCM, RC2, RC4, RC5, RSA, ECIES, GCM } 
    
    public enum Padding 
    //tu powinno byc cos wiecej, bo sa te AOEPWithSHA-1AndMGF1, czyli funkcja haszujaca jest podawana + MGF
    //trzeba rozdzielic symetryczne od asymetrycznych
    { None, PKCS1, PKCS5 }
    
}
