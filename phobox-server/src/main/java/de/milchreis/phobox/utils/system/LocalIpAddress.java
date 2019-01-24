package de.milchreis.phobox.utils.system;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class LocalIpAddress {
	
	public static List<String> getLocalIP() throws SocketException {
		List<String> list = new ArrayList<String>();
		
		Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
		while(n.hasMoreElements()) {
			NetworkInterface e = n.nextElement();
			Enumeration<InetAddress> a = e.getInetAddresses();
			while(a.hasMoreElements()) {
				InetAddress addr = a.nextElement();
				if(!addr.isLoopbackAddress() && addr.isSiteLocalAddress()) {
					list.add(addr.getHostAddress());
				}
			}
		}
		
		return list;
	}
}
