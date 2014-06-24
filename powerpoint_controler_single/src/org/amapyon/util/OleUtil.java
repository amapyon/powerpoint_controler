package org.amapyon.util;

import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;

public class OleUtil {
	public static Variant getPropertyByName(OleAutomation auto, String name) {
		return auto.getProperty(id(auto, name));
	}

	public static Variant getPropertyByName(OleAutomation auto, String name, int intValue) {
		return auto.getProperty(id(auto, name), new Variant[]{new Variant(intValue)});
	}

	public static Variant getPropertyByName(OleAutomation auto, String name, Variant[] vArgs) {
		return auto.getProperty(id(auto, name), vArgs);
	}

	public static boolean setPropertyByName(OleAutomation auto, String name, int intValue) {
		return auto.setProperty(id(auto, name), new Variant(intValue));
	}

	public static boolean setPropertyByName(OleAutomation auto, String name, boolean boolValue) {
		return auto.setProperty(id(auto, name), new Variant(boolValue));
	}

	public static Variant invokeByName(OleAutomation auto, String name) {
		return auto.invoke(id(auto, name));
	}

	public static Variant invokeByName(OleAutomation auto, String name, int intValue) {
		Variant[] vArgs =  new Variant[1];
		vArgs[0] = new Variant(intValue);
		return auto.invoke(id(auto, name), vArgs);
	}

	public static Variant invokeByName(OleAutomation auto, String name, Variant[] vArgs) {
		return auto.invoke(id(auto, name), vArgs);
	}

	private static int id(OleAutomation auto, String name) {
		int[] id = auto.getIDsOfNames(new String[]{name});
		return id[0];
	}

}
