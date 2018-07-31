package presentation.tools;

public class InputCheck {

	/**
	 * 字符串是否为n位数字，若n<=0则表示不检查位数
	 * @param s 检查的字符串
	 * @param n 数字位数
	 * @return
	 */
	public static boolean isAllNumber(String s, int n) {
		if (s == null || "".equals(s)) return false;
		if (n > 0 && s.length() != n) return false;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) < '0' || s.charAt(i) > '9') return false;
		}
		return true;
	}
	/**
	 * 判断是否为一个小数
	 * @param s
	 * @return
	 */
	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isLegal(String s) {
		char[] list = {'-',';','%','[',']','@','!','*','/','\\','|'};
		String[] keyword = {"select", "insert", "delete", "from", "count", "drop table", "update", "truncate", "xp_cmdshell", "exec", "master", "netlocalgroup administrators", "net user", "or", "and"};
		if (s == null || "".equals(s)) return false;
		for (char c : list) if (s.contains(c+"")) return false;
		for (String str : keyword) if (s.toLowerCase().contains(str)) return false;
		return true;
	}
	
	public static boolean isLegalOrBlank(String s) {
		char[] list = {'-',';','%','[',']','@','!','*','/','\\','|'};
		String[] keyword = {"select", "insert", "delete", "from", "count", "drop table", "update", "truncate", "xp_cmdshell", "exec", "master", "netlocalgroup administrators", "net user", "or", "and"};
		if (s == null) return false;
		if ("".equals(s)) return true;
		for (char c : list) if (s.contains(c+"")) return false;
		for (String str : keyword) if (s.toLowerCase().contains(str)) return false;
		return true;
	}
	/**
	 * 字符串是否为n位数字或字母，若n<=0则表示不检查位数
	 * @param s 检查的字符串
	 * @param n 数字位数
	 * @return
	 */
	public static boolean isAlnum(String s, int n) {
		if (s == null || "".equals(s)) return false;
		if (! isLegal(s)) return false;
		if (n > 0 && s.length() != n) return false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c < '0') return false;
			if (c > '9' && c < 'A') return false;
			if (c > 'Z' && c < 'a') return false;
			if (c > 'z') return false;
		}
		return true;
	}
}
