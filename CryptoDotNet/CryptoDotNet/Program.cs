using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Security.Cryptography;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;
using System.Runtime.Serialization;

namespace CryptoDotNet
{
    //class SymmetricCryptography { }

    //class ASymmetricCryptography { }

    public class Cryptography
    {
        public static byte[] EncryptDES(SymmetricAlgorithm symmAlgorithm, byte[] data)
        {
            byte[] encrypted = null;

            ICryptoTransform encryptor = symmAlgorithm.CreateEncryptor(symmAlgorithm.Key, symmAlgorithm.IV);

            using (MemoryStream msEncrypt = new MemoryStream())
            {
                using (CryptoStream csEncrypt = new CryptoStream(msEncrypt, encryptor, CryptoStreamMode.Write))
                {
                    csEncrypt.Write(data, 0, data.Length);
                }
                encrypted = msEncrypt.ToArray();
            }

            return encrypted;
        }

        public static byte[] DecryptDES(SymmetricAlgorithm symmAlgorithm, byte[] data)
        {
            byte[] decrypted = null;

            ICryptoTransform decryptor = symmAlgorithm.CreateDecryptor(symmAlgorithm.Key, symmAlgorithm.IV);
            using (MemoryStream msDecrypt = new MemoryStream(data))
            {
                using (CryptoStream csDecrypt = new CryptoStream(msDecrypt, decryptor, CryptoStreamMode.Read))
                {
                    using (StreamReader srDecrypt = new StreamReader(csDecrypt))
                    {
                        decrypted = System.Text.Encoding.ASCII.GetBytes(srDecrypt.ReadToEnd());
                    }
                }                
            }

            return decrypted;
        }

        public static byte[] EncryptRSA(RSACryptoServiceProvider rsa, byte[] data)
        {
            return rsa.Encrypt(data,false);
        }

        public static byte[] DecryptRSA(RSACryptoServiceProvider rsa, byte[] data)
        {
            return rsa.Decrypt(data, false);
        }

        public static void Main(String[] args) 
        {
            DESCryptoServiceProvider des = new DESCryptoServiceProvider();
            //des.GenerateKey();
            //des.GenerateIV();

           // string s = @"some local string tha shall be ecnrypted";
            string s = @"dzieki";


            byte[] encrypted = EncryptDES(des, Encoding.ASCII.GetBytes(s));
            byte[] decrypted = DecryptDES(des, encrypted);

            string t = Encoding.ASCII.GetString(decrypted);


            RSACryptoServiceProvider encryptor = new RSACryptoServiceProvider();
            RSACryptoServiceProvider decryptor = new RSACryptoServiceProvider();
            encryptor.FromXmlString(decryptor.ToXmlString(false));

            encrypted = encryptor.EncryptValue(Encoding.ASCII.GetBytes(s));
            decrypted = decryptor.DecryptValue(encrypted);

            string m = Encoding.ASCII.GetString(decrypted);


        }
    }

}
