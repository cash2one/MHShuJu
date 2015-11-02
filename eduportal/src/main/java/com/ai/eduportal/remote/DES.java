package com.ai.eduportal.remote;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES加密算法
 * 		DES加密后的字符串有16个字符和32字符两种规格
 * @author Mov
 * @version 1.0 Mov 2009-07-28 create
 */
public class DES {
	/**
	 * 加密
	 * @param text 原文
	 * @return String 加密后的字符串
	 * @throws RuntimeException 算法不存在或计算错误时抛出RuntimeException
	 */
	public static String encrypt(String text) {
		return encrypt(text, null);
	}
	
	/**
	 * 加密
	 * @param text 原文
	 * @param key  加密密钥
	 * @return String 加密后的字符串
	 * @throws RuntimeException 算法不存在或计算错误时抛出RuntimeException
	 */
	public static String encrypt(String text, String key) {
		DES des = key != null && key.length() > 0 ? new DES(key) : new DES();
		return bytes2Hex(des.encrypt(text.getBytes()));
	}
	
	/**
	 * 解密
	 * @param text	  加密的字符串
	 * @return String 原文
	 * @throws RuntimeException 算法不存在或计算错误时抛出RuntimeException
	 */
	public static String decrypt(String text) {
		return decrypt(text, null);
	}
	
	/**
	 * 解密
	 * @param text	  加密的字符串
	 * @return String 原文
	 * @throws RuntimeException 算法不存在或计算错误时抛出RuntimeException
	 */
	public static String decrypt(String text, String key) {
		DES des = key != null 
			&& key.length() > 0 ? new DES(key) : new DES();
		return new String(des.decrypt(hex2Bytes(text.getBytes())));
	}

	/**
	 * 无参构造函数，当不需要指明加密串时使用
	 * @throws RuntimeException 当算法不存在时抛出运行时异常
	 * */
	public DES() {
		try {
			desCipher = Cipher.getInstance(ALGORITHM);
			KeyGenerator keygenerator = KeyGenerator.getInstance(ALGORITHM);
			secretKey = keygenerator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 构造时可以指定加密串
	 * @throws RuntimeException 当算法不存在时抛出运行时异常
	 * */
	public DES(String key) {
		try {
			desCipher = Cipher.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}

		secretKey = getSecretKey(key.getBytes());
	}

	/**
	 * 加密
	 * @param text 原文
	 * @return String 密文
	 * @throws RuntimeException 当算法不存在时抛出运行时异常
	 * */
	public byte[] encrypt(byte[] text) {
		try {
			desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return desCipher.doFinal(text);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解密
	 * @param text 密文
	 * @return String 原文
	 * @throws RuntimeException 当算法不存在或计算出问题时抛出运行时异常
	 * */
	public byte[] decrypt(byte[] text) {
		try {
			desCipher.init(Cipher.DECRYPT_MODE, secretKey);
			return desCipher.doFinal(text);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获得加解密时使用的SecretKey
	 * */
	public SecretKey getSecretKey() {
		return secretKey;
	}

	/**
	 * 私有方法，从一个byte[]获得一个SecretKey对象
	 * @throws RuntimeException
	 * */
	public static SecretKey getSecretKey(byte[] keyBytes) {
		KeySpec ks = null;
		SecretKey secretKey = null;
		SecretKeyFactory kf = null;
		try {
			ks = new DESKeySpec(keyBytes);
			kf = SecretKeyFactory.getInstance(ALGORITHM);
			secretKey = kf.generateSecret(ks);
			return secretKey;
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 二进制转十六进制
	 * */
	public static String bytes2Hex(byte[] textBytes) {
		StringBuffer sb = new StringBuffer();
		String stmp = "";
		
		for (int n = 0; n < textBytes.length; n++) {
			stmp = (java.lang.Integer.toHexString(textBytes[n] & 0XFF));
			sb.append(stmp.length() == 1 ? ("0" + stmp) : stmp);
		}

		return sb.toString();
	}

	/**
	 * 十六进制转二进制
	 * */
	public static byte[] hex2Bytes(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("bytes' length must be even");
		
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}

		return b2;
	}
	
	private Cipher desCipher;

	private SecretKey secretKey;

	private static final String ALGORITHM = "DES";
}
