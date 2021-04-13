package com.arlania;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * 
 * @author Nick Hartskeerl <apachenick@hotmail.com>
 *
 */
public class HardwareInformation {
	
	private static SystemInfo systemInfo = new SystemInfo();
	
	private static HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
	
	private static CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
	
	public static String getSerial() {
		return centralProcessor.getSystemSerialNumber();
	}
	
}
