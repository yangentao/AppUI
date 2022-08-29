package dev.entao.app.ui

import java.net.NetworkInterface

object LocalNet {
    const val wlan0 = "wlan0"
    const val eth0 = "eth0"

    fun find(name: String): NetworkInterface? {
        val ls = NetworkInterface.getNetworkInterfaces() ?: return null
        for (n in ls) {
            if (name == n.name.lowercase()) {
                return n
            }
        }
        return null
    }

    fun ipOf(name: String = wlan0): String? {
        val ni = find(name) ?: return null
        for (addr in ni.inetAddresses) {
            if (addr.address?.size == 4) {
                if (!addr.isLoopbackAddress) {
                    return addr.hostAddress
                }
            }
        }
        return null
    }

    fun macOf(name: String = wlan0): String? {
        val ni = find(name) ?: return null
        val bs = ni.hardwareAddress ?: return null
        return bs.joinToString(":") { String.format("%02X", it) }
    }
}
