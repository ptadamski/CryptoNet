/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crypto;

import java.security.Key;

interface ICipher
{
    byte[] encrypt(byte[] data) throws Exception;
    byte[] decrypt(byte[] data) throws Exception;
    void createKey() throws Exception;
    Key showKey();
    String getTransformation();
}

