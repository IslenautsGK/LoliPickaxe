package com.anotherstar.util;

import java.util.Map;

import com.google.common.collect.Maps;

public class LoliRomeDigitalUtil {

	private static final Map<Integer, String> lookup = Maps.newLinkedHashMap();

	static {
		lookup.put(1000000, "m");
		lookup.put(900000, "cm");
		lookup.put(500000, "d");
		lookup.put(400000, "cd");
		lookup.put(100000, "c");
		lookup.put(90000, "xc");
		lookup.put(50000, "l");
		lookup.put(40000, "xl");
		lookup.put(10000, "x");
		lookup.put(9000, "ix");
		lookup.put(5000, "v");
		lookup.put(4000, "iv");
		lookup.put(1000, "M");
		lookup.put(900, "CM");
		lookup.put(500, "D");
		lookup.put(400, "CD");
		lookup.put(100, "C");
		lookup.put(90, "XC");
		lookup.put(50, "L");
		lookup.put(40, "XL");
		lookup.put(10, "X");
		lookup.put(9, "IX");
		lookup.put(5, "V");
		lookup.put(4, "IV");
		lookup.put(1, "I");
	}

	public static String intToRoman(int num) {
		StringBuilder sb = new StringBuilder();
		for (Integer key : lookup.keySet()) {
			int n = num / key;
			if (n == 0) {
				continue;
			}
			for (int i = n; i > 0; i--) {
				sb.append(lookup.get(key));
			}
			num -= n * key;
			if (num == 0) {
				break;
			}
		}
		return sb.toString();
	}

}
